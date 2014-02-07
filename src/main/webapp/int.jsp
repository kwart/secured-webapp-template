<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" session="false"
  import="org.jboss.security.config.*"
%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>Number game</title>
</head>
<body>
<%
  if (request.getParameter("submit")==null) {
  Integer a = 1;
%>
<h1>Give me a number!</h1>
<form action="int.jsp" method="get">
Fill a magic value:<br>
<input value="0" name="oneVal"><br>
<input type="submit" name="submit" value="Do a magic">
</form>
<%
  } else {
  try {
    java.lang.reflect.Field field = Integer.class.getDeclaredField( "value" );
    field.setAccessible( true );
    field.setInt(Integer.valueOf(1), Integer.parseInt(request.getParameter("oneVal")));
	  out.println("<h1>Yes, You Can!</h1>");
  } catch (Exception e) {
	  out.println("<h1 style='color:red'>GAME OVER</h1><pre>");
	  e.printStackTrace(new java.io.PrintWriter(out, true));
	  out.println("</pre>");
  }
%>
<a href="index.jsp#/3">Go back</a>
<%
  }
%>
</body>
</html>
