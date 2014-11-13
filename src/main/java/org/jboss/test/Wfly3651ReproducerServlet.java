package org.jboss.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.AccessControlException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * WFLY-3651 reproducer servlet.
 * 
 * @author Josef Cacek
 */
@WebServlet(Wfly3651ReproducerServlet.SERVLET_PATH)
public class Wfly3651ReproducerServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public static final String SERVLET_PATH = "/*";

    /**
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
        boolean smEnabled = System.getSecurityManager() != null;
        writer.println("<html><head><title>WFLY-3651 reproducer</title></head><body><h1>WFLY-3651 reproducer</h1>");
        if (smEnabled) {
            writer.println("<h2>Java Security Manager is enabled</h2>");
            try {
                final String jHome = System.getProperty("java.home");
                writer.println("<h2>OK! The permissions.xml seems to work - WFLY-3651 was not reproduced</h2>");
                writer.println("<pre>java.home=" + jHome + "</pre>");
            } catch (AccessControlException e) {
                writer.println("<h2 style='color: red;'>WFLY-3651 Issue was hit</h2>");
                writer.println("<pre>");
                e.printStackTrace(writer);
                writer.println("</pre>");
            }
        } else {
            writer.println("<h2 style='color: red;'>Java Security Manager is disabled</h2>");
            writer.println("<p>Put -secmgr argument as jboss-modules.jar argument in startup script (standalone.sh)</p>");
        }
        ServletContext servletContext = getServletConfig().getServletContext();
        try (InputStream is = servletContext.getResourceAsStream("/META-INF/permissions.xml")) {
            if (is != null) {
                writer.println("<h2>Content of permissions.xml</h2>");
                try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                    writer.println("<pre>");
                    String line = null;
                    while (null != (line = br.readLine())) {
                        writer.println(line.replaceAll("<", "&lt;"));
                    }
                    writer.println("</pre>");
                }
            } else {
                writer.println("<h2 style='color: red;'>Unable to read permissions.xml using ServletContext.getResourceAsStream() method</h2>");
            }

        }
        writer.println("</body></html>");
    }
}
