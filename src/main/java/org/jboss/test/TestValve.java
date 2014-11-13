package org.jboss.test;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

import org.apache.catalina.Valve;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;

public class TestValve extends ValveBase {

    @Override
    public void invoke(Request req, Response resp) throws IOException, ServletException {
        System.out.println(">>> Adding cookie");
        resp.addCookie(new Cookie(getClass().getName(), "start"));
        System.out.println(">>> Session: " + req.getSession(false));
        final Valve next = getNext();
        System.out.println(">>> Invoking next Valve: " + next);
        if (next != null) {
            next.invoke(req, resp);
        }
    }
}
