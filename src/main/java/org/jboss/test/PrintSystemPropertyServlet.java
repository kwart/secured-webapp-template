package org.jboss.test;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet, which prints value of system property. By default it prints value of
 * property {@value #DEFAULT_PROPERTY_NAME}, but you can specify another
 * property name by using request parameter {@value #PARAM_PROPERTY_NAME}.
 * 
 * @author Josef Cacek
 */
@WebServlet(PrintSystemPropertyServlet.SERVLET_PATH)
public class PrintSystemPropertyServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public static final String SERVLET_PATH = "/*";
	public static final String PARAM_PROPERTY_NAME = "property";
	public static final String DEFAULT_PROPERTY_NAME = "java.home";

	/**
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/plain");
		String property = req.getParameter(PARAM_PROPERTY_NAME);
		if (property == null) {
			property = DEFAULT_PROPERTY_NAME;
		}
		final PrintWriter writer = resp.getWriter();
		writer.write(System.getProperty(property));
		writer.close();
	}
}
