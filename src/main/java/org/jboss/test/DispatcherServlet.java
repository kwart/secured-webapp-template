package org.jboss.test;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Josef Cacek
 */
@WebServlet(DispatcherServlet.SERVLET_PATH)
public class DispatcherServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public static final String SERVLET_PATH = "/DispatcherServlet";
	public static final String PARAM_PATH = "path";
	public static final String PARAM_METHOD = "method";

	public static final String PARAM_PATH_DEFAULT = "/";

	private final static java.util.logging.Logger LOGGER = java.util.logging.Logger
			.getLogger(LoggingTestServlet.class.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/plain");
		final PrintWriter writer = resp.getWriter();
		writer.println("START");
		String path = req.getParameter(PARAM_PATH);
		if (path == null) {
			path = PARAM_PATH_DEFAULT;
		}
		RequestDispatcher dispatcher = req.getRequestDispatcher(path);
		String method = req.getParameter(PARAM_METHOD);
		LOGGER.info("Dispatching to " + path + " with method " + method);
		if ("include".equals(method)) {
			dispatcher.include(req, resp);
		} else {
			dispatcher.forward(req, resp);
		}
		writer.println("END");
	}
}
