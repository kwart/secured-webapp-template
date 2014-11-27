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
 * Session test servlet, which is able to read and write an attribute from/to the HttpSession.
 * 
 * @author Josef Cacek
 */
@WebServlet(SessionCounterServlet.SERVLET_PATH)
public class SessionCounterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public static final String SERVLET_PATH = "/SessionCounterServlet";

    public static final String PARAM_INVALIDATE_SESSION = "invalidateSession";
    public static final String PARAM_REMOVE_COUNTER = "removeCounter";

    public static final String ATTR_COUNTER = SessionCounterServlet.class.getName() + ".counter";

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
        resp.setContentType("text/html");
        final PrintWriter writer = resp.getWriter();
        writer.println("<a href='index.jsp'>index.jsp</a>, <a href='." + SERVLET_PATH + "'>increment counter</a><br/>");
        HttpSession session = req.getSession();
        if (req.getParameter(PARAM_INVALIDATE_SESSION) != null) {
            writer.println("Invalidating session<br/>");
            session.invalidate();
            session = req.getSession();
        }
        writer.println("Session isNew: " + session.isNew() + "<br/>");
        if (req.getParameter(PARAM_REMOVE_COUNTER) != null) {
            writer.println("Removing attribute " + ATTR_COUNTER + "<br/>");
            session.removeAttribute(ATTR_COUNTER);
        } else {
            Integer counter = (Integer) session.getAttribute(ATTR_COUNTER);
            writer.println(ATTR_COUNTER + " value in the session: " + counter + "<br/>");
            if (counter == null) {
                counter = 0;
            } else {
                counter++;
            }
            writer.println("Setting counter value: " + counter + "<br/>");
            session.setAttribute(ATTR_COUNTER, counter);
        }
        writer.println(ATTR_COUNTER + " value in the session: " + session.getAttribute(ATTR_COUNTER));
        writer.close();
    }
}
