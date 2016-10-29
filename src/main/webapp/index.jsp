<%@ page language="java" pageEncoding="UTF-8" contentType="text/plain;charset=utf-8"
%><%
    Integer requestCount = (Integer)session.getAttribute("requestCount");
    if (requestCount == null) {
        requestCount = 0;
    }
    session.setAttribute("requestCount", ++requestCount);

%>Host=<%= System.getProperty("jboss.node.name") %>
Request count=<%= requestCount %>
