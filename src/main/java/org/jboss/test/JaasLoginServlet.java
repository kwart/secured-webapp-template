package org.jboss.test;

import java.io.IOException;
import java.io.PrintWriter;

import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.LoginContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 * Servlet, which authenticates principal based on {@value #PARAM_USER} and {@value #PARAM_PASSWORD} request parameters.
 * 
 * @author Josef Cacek
 */
@WebServlet(JaasLoginServlet.SERVLET_PATH)
public class JaasLoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public static final String SERVLET_PATH = "/JaasLoginServlet";
    public static final String PARAM_CREATE_SESSION = "createSession";
    public static final String PARAM_USER = "user";
    public static final String PARAM_PASSWORD = "password";

    /**
     * Calls {@link HttpServletRequest#authenticate(HttpServletResponse) method.}
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        final PrintWriter writer = resp.getWriter();
        if (req.getParameter(PARAM_CREATE_SESSION) != null) {
            req.getSession();
        }
        try {
            writer.println("<a href='index.jsp'>index.jsp</a><br/>");
            writer.println("User principal before login: " + req.getUserPrincipal() + "<br/>");
            writer.println("Calling HttpServletRequest.login()<br/>");
            LoginContext loginContext = new LoginContext("web-tests", new CallbackHandler() {

                @Override
                public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
                    for (Callback c : callbacks) {
                        if (c instanceof PasswordCallback) {
                            ((PasswordCallback) c).setPassword(req.getParameter(PARAM_PASSWORD).toCharArray());
                        } else if (c instanceof NameCallback) {
                            ((NameCallback) c).setName(req.getParameter(PARAM_USER));
                        }
                    }
                }
            });
            loginContext.login();
            Subject subject = loginContext.getSubject();
            writer.println("Login successful. Subject: " + subject);
        } catch (Exception e) {
            writer.println("Login failed");
            writer.println("<pre>");
            e.printStackTrace(writer);
            writer.println("</pre>");
        } finally {
            writer.close();
        }
    }
}
