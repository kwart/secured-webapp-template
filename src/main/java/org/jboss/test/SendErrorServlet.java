package org.jboss.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;

/**
 * Servlet, which simply calls {@link HttpServletResponse#sendError(int)} with
 * code provided as {@value #PARAM_ERROR_CODE} request parameter value. If the
 * request parameter is not provided or it can't be parsed then
 * {@value #DEFAULT_ERROR_CODE} is used.
 * 
 * @author Josef Cacek
 */
@WebServlet(SendErrorServlet.SERVLET_PATH)
public class SendErrorServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public static final String SERVLET_PATH = "/SendErrorServlet";
	public static final String PARAM_ERROR_CODE = "code";
	public static final int DEFAULT_ERROR_CODE = 304; // not modified

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/plain");
		int code = DEFAULT_ERROR_CODE;
		final String codeStr = req.getParameter(PARAM_ERROR_CODE);
		if (codeStr != null) {
			try {
				code = Integer.parseInt(codeStr);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		resp.sendError(code);
	}

}
