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

/**
 * Servlet, which prints file content. By default it prints content of file {@value #DEFAULT_FILE_NAME}, but you can specify
 * another file path by using request parameter {@value #PARAM_FILE_NAME}.
 * 
 * @author Josef Cacek
 */
@WebServlet(ReadFileServlet.SERVLET_PATH)
public class ReadFileServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    public static final String SERVLET_PATH = "/ReadFileServlet";
    public static final String PARAM_FILE_NAME = "file";
    public static final String DEFAULT_FILE_NAME = "/etc/passwd";

    /**
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        String property = req.getParameter(PARAM_FILE_NAME);
        if (property == null) {
            property = DEFAULT_FILE_NAME;
        }
        InputStream is = new FileInputStream(property);
        OutputStream os = resp.getOutputStream();
        copy(is, os);
        is.close();
        os.close();
    }

    public static long copy(InputStream input, OutputStream output) throws IOException {
        long count = 0;
        int n = 0;
        final byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

}
