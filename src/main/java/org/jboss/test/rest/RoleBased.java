/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2015, Red Hat, Inc., and individual contributors
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
package org.jboss.test.rest;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * RestEasy implements Role-based security. It has to be enabled in
 * application's context parameter "resteasy.role.based.security" in web.xml
 *
 * @author Josef Cacek
 */
@Path("/role-based")
@RolesAllowed({ "Admin", "User" })
public class RoleBased {

	@GET
	public String getProtected() {
		return "Class level @RolesAllowed({Admin, User})";
	}

	@GET
	@Path("/admin")
	@RolesAllowed("Admin")
	public String getAdmin() {
		return "method level @RolesAllowed(Admin)";
	}

	@GET
	@Path("/permit")
	@PermitAll
	public String getPermit() {
		return "@PermitAll";
	}
}