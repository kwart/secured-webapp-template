package org.jboss.test;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Simple servlet, which returns the principal name or {@value #NO_PRINCIPAL} when the principal is empty.
 * 
 * @author Josef Cacek
 */
@WebServlet(LogoutServlet.SERVLET_PATH)
public class LogoutServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public static final String SERVLET_PATH = "/LogoutServlet";
    public static final String PARAM_CREATE_SESSION = "createSession";
    public static final String PARAM_INVALIDATE_SESSION = "invalidateSession";

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
        HttpSession session = req.getSession(false);
        if (session == null && req.getParameter(PARAM_CREATE_SESSION) != null) {
            writer.println("Creating session<br/>");
            session = req.getSession();
        }
        try {
            writer.println("Session exists before logout: " + (session != null) + "<br/>");
            req.logout();
            session = req.getSession(false);
            writer.println("Session exists after logout: " + (session != null) + "<br/>");
            if (session != null) {
                writer.println("Session isNew: " + session.isNew() + "<br/>");
                if (req.getParameter(PARAM_INVALIDATE_SESSION) != null) {
                    writer.println("Invalidating session<br/>");
                    session.invalidate();
                }
            }
            writer.println("<br/>Logout successful <a href='index.jsp'>index.jsp</a>");
        } catch (ServletException e) {
            writer.println("<pre>Logout failed:");
            e.printStackTrace(writer);
            writer.println("</pre>");
        } finally {
            writer.close();
        }
    }
}
