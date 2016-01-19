/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2015, Red Hat, Inc., and individual contributors
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
package org.jboss.test.rest;

import java.io.IOException;
import java.util.logging.Level;

import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.LoginContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jboss.test.LoggingTestServlet;

/**
 * Resource used to login into the application.
 */
@Path("/jaas-login")
public class JaasLogin {

    private final static java.util.logging.Logger LOGGER = java.util.logging.Logger
            .getLogger(LoggingTestServlet.class.getName());

    @GET
    public Response jaasLogin() {
        LOGGER.info("JAAS login initiated");
        try {
            LoginContext loginContext = new LoginContext("web-tests", new CallbackHandler() {

                @Override
                public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
                    for (Callback c : callbacks) {
                        if (c instanceof PasswordCallback) {
                            ((PasswordCallback) c).setPassword("user".toCharArray());
                        } else if (c instanceof NameCallback) {
                            ((NameCallback) c).setName("user");
                        }
                    }
                }
            });
            loginContext.login();
            Subject subject = loginContext.getSubject();
            return Response.ok().entity(subject.toString()).type(MediaType.TEXT_PLAIN).build();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Login failed", e);
            return Response.status(Status.FORBIDDEN).entity("Not logged").type(MediaType.TEXT_PLAIN).build();
        }
    }

}