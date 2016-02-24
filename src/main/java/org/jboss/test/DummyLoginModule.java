package org.jboss.test;

import java.security.acl.Group;
import java.util.Arrays;
import java.util.Collections;
import java.util.logging.Level;
import java.util.stream.Collectors;

import javax.security.auth.login.LoginException;
import javax.security.jacc.PolicyContextException;
import javax.servlet.http.HttpServletRequest;

import org.jboss.security.SimpleGroup;
import org.jboss.security.SimplePrincipal;
import org.jboss.security.auth.spi.UsernamePasswordLoginModule;

/**
 * Authenticates every login request (independent on username and/or password)
 * and maps "Admin" and "User" roles to the authenticated user.
 *
 * @author Josef Cacek
 */
public class DummyLoginModule extends UsernamePasswordLoginModule {

	private final static java.util.logging.Logger LOGGER = java.util.logging.Logger
			.getLogger(DummyLoginModule.class.getName());

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

	@Override
	public boolean login() throws LoginException {
		dumpHttpRequest("login");
		return super.login();
	}

	@Override
	public boolean commit() throws LoginException {
		dumpHttpRequest("commit");
		return super.commit();
	}

	@Override
	public boolean abort() throws LoginException {
		dumpHttpRequest("abort");
		return super.abort();
	}

	@Override
	public boolean logout() throws LoginException {
		dumpHttpRequest("logout");
		return super.logout();
	}

	private static void dumpHttpRequest(String caller) {
		try {
			HttpServletRequest request = (HttpServletRequest) javax.security.jacc.PolicyContext
					.getContext(HttpServletRequest.class.getName());
			if (request == null) {
				LOGGER.warning("Unable to retrieve HttpServletRequest object from LoginModule#" + caller);
			} else {
				StringBuilder reqDump = new StringBuilder("HttpServletRequest retrieved from LoginModule#");
				reqDump.append(caller).append(":\n");
				reqDump.append("Headers: ")
						.append(Collections.list(request.getHeaderNames()).stream()
								.map(s -> (s + "=" + Collections.list(request.getHeaders(s))))
								.collect(Collectors.joining(", ", "{", "}")))
						.append("\n");
				reqDump.append("Parameters: ")
						.append(request.getParameterMap().entrySet().stream()
								.map(e -> (e.getKey() + "=" + Arrays.toString(e.getValue())))
								.collect(Collectors.joining(", ", "{", "}")))
						.append("\n");
				reqDump.append("Attributes: ").append(Collections.list(request.getAttributeNames()).stream()
						.map(s -> (s + "=" + request.getAttribute(s))).collect(Collectors.joining(", ", "{", "}")));
				LOGGER.info(reqDump.toString());
			}
		} catch (PolicyContextException e) {
			LOGGER.log(Level.SEVERE, "", e);
		}
	}

}
