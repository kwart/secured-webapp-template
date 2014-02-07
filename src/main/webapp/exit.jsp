<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" session="false"
  import="org.jboss.security.config.*"
%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>System exit</title>
</head>
<body>
<%
  if (request.getParameter("submit")==null) {
  Integer a = 1;
%>
<h1>Take a break!</h1>
<form action="exit.jsp" method="get">
<input type="submit" name="submit" value="I want a break">
</form>
<%
  } else {
  try {
    System.exit(0);
  } catch (Exception e) {
	  out.println("<h1 style='color:red'>GAME OVER</h1><pre>");
	  e.printStackTrace(new java.io.PrintWriter(out, true));
	  out.println("</pre>");
  }
%>
<a href="index.jsp#/3/1">Go back</a>
<%
  }
%>
</body>
</html>
