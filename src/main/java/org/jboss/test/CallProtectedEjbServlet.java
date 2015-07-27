/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2014, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.test;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.test.ejb.Hello;
import org.jboss.test.ejb.HelloBean;

/**
 * Servlet which calls protected EJB method {@link Hello#sayHello()}.
 *
 * @author Josef Cacek
 */
@WebServlet(CallProtectedEjbServlet.SERVLET_PATH)
@DeclareRoles({ HelloBean.AUTHORIZED_ROLE, HelloBean.NOT_AUTHZ_ROLE })
public class CallProtectedEjbServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public static final String SERVLET_PATH = "/CallProtectedEjbServlet";

    @EJB
    Hello ejb;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        PrintWriter writer = resp.getWriter();
        try {
            writer.print(callProtectedEJB());
        } catch (Exception e) {
            e.printStackTrace(writer);
        }
    }

    protected String callProtectedEJB() throws NamingException {
        // InitialContext context = new InitialContext();
        // ProtectedEJB ejb = (ProtectedEJB) context.lookup("java:global/secured-webapp/" + HelloBean.class.getSimpleName()
        // + "!" + Hello.class.getName());
        return ejb.sayHello();
    }

}
