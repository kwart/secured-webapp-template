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

import java.security.acl.Group;
import java.util.*;
import java.util.stream.Collectors;

import javax.security.auth.Subject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;

/**
 * Resource used to login into the application.
 *
 * @author Josef Cacek
 */
@Path("/login")
public class Login {

    @Context
    private HttpServletRequest req;

    @GET
    public String login(@QueryParam("username") String username, @QueryParam("password") String password) {
        try {
            req.login(username, password);
            Subject subject = org.jboss.security.SecurityContextAssociation.getSubject();
            Optional<Group> rolesGroup = subject.getPrincipals(Group.class).stream().filter(p -> "Roles".equals(p.getName()))
                    .findFirst();
            if (rolesGroup.isPresent()) {
                List<String> roleNames = Collections.list(rolesGroup.get().members()).stream().map(p -> p.getName())
                        .collect(Collectors.toList());
                return "You're logged with roles: " + roleNames;
            } else {
                return "You're logged without roles: " + subject;
            }
        } catch (ServletException e) {
            e.printStackTrace();
        }
        return "Login failed for user " + username;
    }

}