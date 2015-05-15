package org.jboss.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.AccessController;
import java.security.Principal;

import javax.security.auth.Subject;
import javax.security.jacc.PolicyContext;
import javax.security.jacc.PolicyContextException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet which prints info about an authenticated Subject. By default it retrieves the Subject using JACC method -
 * <code>PolicyContext.getContext("javax.security.auth.Subject.container")</code>. If the {@value #PARAM_JAAS} request parameter
 * is provided, then <code>Subject.getSubject(...)</code> call is used.
 * 
 * @author Josef Cacek
 */
@WebServlet(SubjectInfoServlet.SERVLET_PATH)
public class SubjectInfoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public static final String SERVLET_PATH = "/SubjectInfoServlet";

    public static final String PARAM_JAAS = "jaas";

    /**
     * Writes Subject info as a simple text response.
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        final PrintWriter writer = resp.getWriter();
        try {
            Subject subject = null;
            if (req.getParameter(PARAM_JAAS) != null) {
                subject = Subject.getSubject(AccessController.getContext());
            } else {
                subject = (Subject) PolicyContext.getContext("javax.security.auth.Subject.container");
            }
            writer.println("Subject: " + subject);
            if (subject != null) {
                writer.println("Principals:");
                for (Principal principal : subject.getPrincipals()) {
                    writer.println("\t" + principal.getClass() + " " + principal.getName() + " (" + principal + ")");
                }
                writer.println("Public credentials:");
                for (Object publicCred : subject.getPublicCredentials()) {
                    writer.println("\t" + publicCred.getClass() + " " + publicCred);
                }
                writer.println("Private credentials:");
                for (Object privateCred : subject.getPrivateCredentials()) {
                    writer.println("\t" + privateCred.getClass() + " " + privateCred);
                }
            }
        } catch (PolicyContextException e) {
            e.printStackTrace(writer);
        }
        writer.close();
    }
}
