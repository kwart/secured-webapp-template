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

package org.jboss.test.ejb;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Secured stateless EJB.
 *
 * @author Josef Cacek
 */
@Stateless
@DeclareRoles(ForwardCallBean.AUTHORIZED_ROLE)
@Remote(Hello.class)
@RolesAllowed(ForwardCallBean.AUTHORIZED_ROLE)
public class ForwardCallBean implements Hello {

    public static final String AUTHORIZED_ROLE = "Admin";
    public static final String NOT_AUTHZ_ROLE = "User";

    /*
     * (non-Javadoc)
     *
     * @see org.jboss.test.ejb.Hello#sayHello()
     */
    @Override
    public String sayHello() {
        InitialContext context;
		try {
			context = new InitialContext();
	        Hello ejb = (Hello) context.lookup("java:global/secured-webapp/" + HelloBean.class.getSimpleName()
	                + "!" + Hello.class.getName());
	        return ejb.sayHello();
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return null;
    }
}
