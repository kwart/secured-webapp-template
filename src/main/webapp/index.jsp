<%@ page language="java" pageEncoding="UTF-8" contentType="text/plain;charset=utf-8" session="false"
%><%
javax.servlet.http.HttpSession session = ((javax.servlet.http.HttpServletRequest) pageContext.getRequest()).getSession(false);
if (session==null) {
	out.println("Session is null!");
	session = ((javax.servlet.http.HttpServletRequest) pageContext.getRequest()).getSession();
}
out.println("Session attribute 'test': " + session.getAttribute("test"));
out.println("Writing 'value' to session attribute 'test'.");
session.setAttribute("test", "value");
%>