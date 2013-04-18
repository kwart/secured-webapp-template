package org.jboss.test;

import javax.annotation.security.DeclareRoles;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;

/**
 * Protected version of {@link SimpleServlet}. Only {@value #ALLOWED_ROLE} role has access right.
 * 
 * @author Josef Cacek
 */
@DeclareRoles({ SimpleSecuredServlet.ALLOWED_ROLE })
@ServletSecurity(@HttpConstraint(rolesAllowed = { SimpleSecuredServlet.ALLOWED_ROLE }))
@WebServlet(SimpleSecuredServlet.SERVLET_PATH)
public class SimpleSecuredServlet extends SimpleServlet {

    private static final long serialVersionUID = 1L;
    public static final String SERVLET_PATH = "/SimpleSecuredServlet";
    public static final String ALLOWED_ROLE = "Admin";
}
