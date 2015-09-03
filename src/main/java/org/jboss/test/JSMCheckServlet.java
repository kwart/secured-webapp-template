package org.jboss.test;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet, which only checks JSM permissions
 *
 * @author Josef Cacek
 */
@WebServlet(JSMCheckServlet.SERVLET_PATH)
public class JSMCheckServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public static final String SERVLET_PATH = "/*";

	public static final String PARAM_PROPERTY = "property";
	public static final String PARAM_CUSTOM_NAME = "custom";

	/**
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/plain");

		final String propertyName = req.getParameter(PARAM_PROPERTY);
		String customPermName = req.getParameter(PARAM_CUSTOM_NAME);
		if (customPermName == null && propertyName == null) {
			customPermName = "test";
		}
		final PrintWriter writer = resp.getWriter();

		if (customPermName != null) {
			writer.println("Checking custom permission with name " + customPermName);
			try {
				CheckJSMUtils.checkCustomPermission(customPermName);
				writer.println("OK");
			} catch (Exception e) {
				e.printStackTrace(writer);
			}
			writer.println();
		}

		if (propertyName != null) {
			writer.println("Checking property 'read' permission for " + propertyName);
			try {
				CheckJSMUtils.checkPropertyPermission(propertyName);
				writer.println("OK");
			} catch (Exception e) {
				e.printStackTrace(writer);
			}
			writer.println();
		}
		writer.close();
	}
}
