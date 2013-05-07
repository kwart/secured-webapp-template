/*
 * JBoss, Home of Professional Open Source.
 *
 * Copyright 2007, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.test;

import static org.apache.catalina.authenticator.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.Realm;
import org.apache.catalina.Session;
import org.apache.catalina.authenticator.FormAuthenticator;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.deploy.LoginConfig;
import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;
import org.jboss.logging.Logger;
import org.jboss.security.negotiation.DelegationCredentialContext;
import org.jboss.security.negotiation.MessageFactory;
import org.jboss.security.negotiation.NegotiationException;
import org.jboss.security.negotiation.NegotiationMessage;
import org.jboss.security.negotiation.common.MessageTrace;
import org.jboss.security.negotiation.common.NegotiationContext;
import org.picketbox.commons.cipher.Base64;

/**
 * An authenticator to manage Negotiation based authentication in connection with the Negotiation login module.
 * 
 * @author darran.lofthouse@jboss.com
 * @version $Revision: 114368 $
 */
public class NegotiationAuthenticator extends FormAuthenticator {

    private static final Logger log = Logger.getLogger(NegotiationAuthenticator.class);

    private static final String NEGOTIATE = "Negotiate";

    private static final String NEGOTIATION_CONTEXT = "NEGOTIATION_CONTEXT";

    private static final String DELEGATION_CREDENTIAL = "DELEGATION_CREDENTIAL";

    private static final String FORM_METHOD = "FORM";

    protected String getNegotiateScheme() {
        return NEGOTIATE;
    }

    @Override
    public boolean authenticate(final Request request, final HttpServletResponse response, final LoginConfig config)
            throws IOException {

        boolean DEBUG = log.isDebugEnabled();
        log.trace("Authenticating user");

        Principal principal = request.getUserPrincipal();
        if (principal != null) {
            if (log.isTraceEnabled())
                log.trace("Already authenticated '" + principal.getName() + "'");
            return true;
        }

        String contextPath = request.getContextPath();
        String requestURI = request.getDecodedRequestURI();
        boolean loginAction = requestURI.startsWith(contextPath) && requestURI.endsWith(FORM_ACTION);
        if (loginAction) {
            Realm realm = context.getRealm();
            String username = request.getParameter(FORM_USERNAME);
            String password = request.getParameter(FORM_PASSWORD);
            principal = realm.authenticate(username, password);
            if (principal == null) {
                RequestDispatcher disp = context.getServletContext().getRequestDispatcher(config.getErrorPage());
                try {
                    disp.forward(request.getRequest(), response);
                } catch (ServletException e) {
                    IOException ex = new IOException("Unable to forward to error page.");
                    ex.initCause(e);

                    throw ex;
                }
                return false;
            }

            Session session = request.getSessionInternal();
            requestURI = savedRequestURL(session);

            session.setNote(FORM_PRINCIPAL_NOTE, principal);
            session.setNote(SESS_USERNAME_NOTE, username);
            session.setNote(SESS_PASSWORD_NOTE, password);

            register(request, response, principal, FORM_METHOD, username, password);
            response.sendRedirect(response.encodeRedirectURL(requestURI));

            return false;
        }

        String negotiateScheme = getNegotiateScheme();

        if (DEBUG)
            log.debug("Header - " + request.getHeader("Authorization"));
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {

            log.debug("No Authorization Header, initiating negotiation");
            initiateNegotiation(request, response, config);

            return false;
        } else if (authHeader.startsWith(negotiateScheme + " ") == false) {
            throw new IOException("Invalid 'Authorization' header.");
        }

        String authTokenBase64 = authHeader.substring(negotiateScheme.length() + 1);
        byte[] authToken = Base64.decode(authTokenBase64);
        ByteArrayInputStream authTokenIS = new ByteArrayInputStream(authToken);
        MessageTrace.logRequestBase64(authTokenBase64);
        MessageTrace.logRequestHex(authToken);

        Session session = request.getSessionInternal();
        NegotiationContext negotiationContext = (NegotiationContext) session.getNote(NEGOTIATION_CONTEXT);
        if (negotiationContext == null) {
            log.debug("Creating new NegotiationContext");
            negotiationContext = new NegotiationContext();
            session.setNote(NEGOTIATION_CONTEXT, negotiationContext);
        }

        String username = negotiationContext.getUsername();
        if (username == null || username.length() == 0) {
            username = session.getId() + "_" + String.valueOf(System.currentTimeMillis());
            negotiationContext.setUsername(username);
        }
        String authenticationMethod = "";
        try {
            // Set the ThreadLocal association.
            negotiationContext.associate();

            MessageFactory mf = MessageFactory.newInstance();
            if (mf.accepts(authTokenIS) == false) {
                throw new IOException("Unsupported negotiation mechanism.");
            }

            NegotiationMessage requestMessage = mf.createMessage(authTokenIS);
            negotiationContext.setRequestMessage(requestMessage);

            Realm realm = context.getRealm();
            principal = realm.authenticate(username, (String) null);

            authenticationMethod = negotiationContext.getAuthenticationMethod();

            if (DEBUG && principal != null)
                log.debug("authenticated principal = " + principal);

            NegotiationMessage responseMessage = negotiationContext.getResponseMessage();
            if (responseMessage != null) {
                ByteArrayOutputStream responseMessageOS = new ByteArrayOutputStream();
                responseMessage.writeTo(responseMessageOS, true);
                String responseHeader = responseMessageOS.toString();

                MessageTrace.logResponseBase64(responseHeader);

                response.setHeader("WWW-Authenticate", negotiateScheme + " " + responseHeader);
            }

        } catch (NegotiationException e) {
            IOException ioe = new IOException("Error processing " + negotiateScheme + " header.");
            ioe.initCause(e);
            throw ioe;
        } finally {
            // Clear the headers and remove the ThreadLocal association.
            negotiationContext.clear();
        }

        if (principal == null) {
            response.sendError(Response.SC_UNAUTHORIZED);
        } else {
            Object schemeContext = negotiationContext.getSchemeContext();
            if (schemeContext instanceof GSSContext) {
                GSSContext gssContext = (GSSContext) schemeContext;
                if (gssContext.getCredDelegState()) {
                    try {
                        GSSCredential delegCredential = gssContext.getDelegCred();
                        session.setNote(DELEGATION_CREDENTIAL, delegCredential);
                        DelegationCredentialManager.setDelegationCredential(delegCredential);
                    } catch (GSSException e) {
                        log.warn("Unable to obtain delegation credential.", e);
                    }
                }
            }

            register(request, response, principal, authenticationMethod, username, null);
        }

        return (principal != null);
    }

    private void initiateNegotiation(final Request request, final HttpServletResponse response, final LoginConfig config)
            throws IOException {
        String loginPage = config.getLoginPage();
        if (loginPage != null) {
            // TODO - Logic to cache and restore request.
            ServletContext servletContext = context.getServletContext();
            RequestDispatcher disp = servletContext.getRequestDispatcher(loginPage);

            try {
                Session session = request.getSessionInternal();
                saveRequest(request, session);

                disp.include(request.getRequest(), response);
                response.setHeader("WWW-Authenticate", getNegotiateScheme());
                response.setStatus(Response.SC_UNAUTHORIZED);
            } catch (ServletException e) {
                IOException ex = new IOException("Unable to include loginPage");
                ex.initCause(e);

                throw ex;
            }

        } else {
            response.setHeader("WWW-Authenticate", getNegotiateScheme());
            response.sendError(Response.SC_UNAUTHORIZED);
        }

        response.flushBuffer();
    }

    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
        Session session = request.getSessionInternal();
        GSSCredential credential = (GSSCredential) session.getNote(DELEGATION_CREDENTIAL);
        try {
            DelegationCredentialManager.setDelegationCredential(credential);
            super.invoke(request, response);
        } finally {
            DelegationCredentialManager.removeDelegationCredential();
        }
    }

    private static class DelegationCredentialManager extends DelegationCredentialContext {

        private static void setDelegationCredential(final GSSCredential credential) {
            currentCredential.set(credential);
        }

        private static void removeDelegationCredential() {
            currentCredential.remove();
        }

    }

}
