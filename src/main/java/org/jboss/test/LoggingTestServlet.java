package org.jboss.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Simple servlet, which logs a message on every JUL log level.
 * 
 * @author Josef Cacek
 */
@WebServlet(LoggingTestServlet.SERVLET_PATH)
public class LoggingTestServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public static final String SERVLET_PATH = "/LoggingServlet";

	private final static java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(LoggingTestServlet.class
			.getName());

	private static final Level[] LEVELS = { Level.SEVERE, Level.WARNING, Level.INFO, Level.CONFIG, Level.FINE,
			Level.FINER, Level.FINEST };

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/plain");
		final PrintWriter writer = resp.getWriter();

		for (Level level : LEVELS) {
			writer.println("Logging message with " + level.getName() + " importance level");
			LOGGER.log(level, "Test log message with level: " + level.getLocalizedName());
		}
		writer.close();
	}
}
