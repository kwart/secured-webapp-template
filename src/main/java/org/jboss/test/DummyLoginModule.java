package org.jboss.test;

import java.security.acl.Group;

import javax.security.auth.login.LoginException;

import org.jboss.security.SimpleGroup;
import org.jboss.security.SimplePrincipal;
import org.jboss.security.auth.spi.UsernamePasswordLoginModule;

/**
 * Authenticates every login request (independent on username and/or password) and maps "Admin" and "User" roles to the
 * authenticated user.
 * 
 * @author Josef Cacek
 */
public class DummyLoginModule extends UsernamePasswordLoginModule {

    @Override
    protected String getUsersPassword() throws LoginException {
        return "XXX";
    }

    @Override
    protected boolean validatePassword(String inputPassword, String expectedPassword) {
        return true;
    }


    @Override
    protected Group[] getRoleSets() throws LoginException {
        SimpleGroup group = new SimpleGroup("Roles");

        group.addMember(new SimplePrincipal("Admin"));
        group.addMember(new SimplePrincipal("User"));
        return new Group[] { group };
    }

}
