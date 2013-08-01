package org.jboss.test;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(SimpleServlet.SERVLET_PATH)
public class SimpleServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    public static final String SERVLET_PATH = "/";

    public static final String PARAM_STEP = "step";

    public static final String SERVER1 = "http://localhost:8080";
    public static final String SERVER2 = "http://localhost:8230";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        final String context = req.getContextPath();
        final PrintWriter writer = resp.getWriter();
        HttpSession session = req.getSession(false);
        int step = 0;
        try {
            step = Integer.parseInt(req.getParameter(PARAM_STEP));
        } catch (Exception e) {
        }
        writer.print("[" + (req.getLocalPort() == 8080 ? "server-one" : "server-two") + "] Step " + step + ": ");

        String nextServer = SERVER1;
        String nextPath = SERVLET_PATH;
        switch (step) {
            case 1:
                writer.print("Read and write session attribute");
                if (session == null)
                    session = req.getSession();
                writer.print("<br/>Check session attribute 'STEP': value stored in the session = "
                        + session.getAttribute(PARAM_STEP) + ", writing a new value = " + step);
                session.setAttribute(PARAM_STEP, step);
                nextPath = SimpleSecuredServlet.SERVLET_PATH;
                break;
            case 2:
                writer.print("Access protected resource");
                nextServer = SERVER2;
                break;
            case 3:
                writer.print("Read and write session attribute");
                if (session == null)
                    session = req.getSession();
                writer.print("<br/>Check session attribute 'STEP': value stored in the session = "
                        + session.getAttribute(PARAM_STEP) + ", writing a new value = " + step);
                session.setAttribute(PARAM_STEP, step);
                nextPath = SimpleSecuredServlet.SERVLET_PATH;
                nextServer = SERVER2;
                break;
            case 4:
                writer.print("Access protected resource");
                break;
            case 5:
                writer.print("Logout");
                req.logout();
                nextPath = SimpleSecuredServlet.SERVLET_PATH;
                nextServer = SERVER2;
                break;
            case 6:
                writer.print("Access protected resource - <b>FAILs in some EAP versions</b>. If you see this message, your EAP doesn't contain the issue.");
                break;
            default:
                step = 0;
                writer.print("Default step: Invalidating session");
                if (session != null)
                    session.invalidate();
                break;
        }
        writer.print("<br/><a href='" + nextServer + context + nextPath + "?" + PARAM_STEP + "=" + (step + 1)
                + "'>Next step</a>");
    }
}
