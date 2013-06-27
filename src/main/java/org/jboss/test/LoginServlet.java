package org.jboss.test;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Simple servlet, which returns the principal name or {@value #NO_PRINCIPAL} when the principal is empty.
 * 
 * @author Josef Cacek
 */
@WebServlet(LoginServlet.SERVLET_PATH)
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public static final String SERVLET_PATH = "/LoginServlet";
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
            writer.println("User principal: " + req.getUserPrincipal());
            req.login(req.getParameter(PARAM_USER), req.getParameter(PARAM_USER));
            writer.println("<br/>Login successful <a href='index.jsp'>index.jsp</a>");
        } catch (ServletException e) {
            writer.println("<br/>Login failed <a href='index.jsp'>index.jsp</a>");
            writer.println("<pre>");
            e.printStackTrace(writer);
            writer.println("</pre>");
        } finally {
            writer.close();
        }
    }
}
