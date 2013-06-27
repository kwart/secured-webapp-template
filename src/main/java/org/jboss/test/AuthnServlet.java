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
@WebServlet(AuthnServlet.SERVLET_PATH)
public class AuthnServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public static final String SERVLET_PATH = "/AuthnServlet";
    public static final String PARAM_CREATE_SESSION = "createSession";

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
            boolean authn = req.authenticate(resp);
            if (!resp.isCommitted())
                writer.println("Authenticate returned " + authn + " (<a href='index.jsp'>index.jsp</a>)");
        } finally {
            writer.close();
        }
    }
}
