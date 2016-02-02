package org.jboss.test;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Servlet, which prints supported keystore types.
 * 
 * @author Josef Cacek
 */
@WebServlet(ListKeystoreTypesServlet.SERVLET_PATH)
public class ListKeystoreTypesServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public static final String SERVLET_PATH = "/ListKeystoreTypesServlet";

	/**
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 * @see HttpServlet#doGet(HttpServletRequest,
	 *      HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/plain");
		final PrintWriter writer = resp.getWriter();
		java.security.Security.getAlgorithms("KeyStore").stream().sorted().forEach(writer::println);
	}

}
