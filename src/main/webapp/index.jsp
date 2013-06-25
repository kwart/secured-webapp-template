<%@ page language="java" pageEncoding="UTF-8" contentType="text/plain;charset=utf-8" session="false"
%><%
ClassLoader cl = getClass().getClassLoader();
out.println("bar.txt: " + cl.getResource("bar.txt"));
out.println("root.txt: " + cl.getResource("root.txt"));
%>