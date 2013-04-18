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
@WebServlet(SimpleServlet.SERVLET_PATH)
public class SimpleServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public static final String SERVLET_PATH = "/SimpleServlet";
    public static final String PARAM_CREATE_SESSION = "createSession";
    public static final String NO_PRINCIPAL = "<no principal>";

    /**
     * Writes principal name as a simple text response.
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
        if (req.getParameter(PARAM_CREATE_SESSION) != null) {
            //workaround to get UserPrincipal also in this unprotected servlet
            req.getSession();
        }
        writer.write(req.getUserPrincipal() == null ? NO_PRINCIPAL : req.getUserPrincipal().getName());
        writer.close();
    }
}
