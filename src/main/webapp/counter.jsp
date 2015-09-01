<%@ page language="java" pageEncoding="UTF-8" contentType="text/plain" session="true"
%><%
final String attrName = "test.hitCounter"; 
Integer counter = (Integer) session.getAttribute(attrName);
if (counter == null) {
    counter = 0;
}
session.setAttribute(attrName, counter++);
out.println(counter);
%>