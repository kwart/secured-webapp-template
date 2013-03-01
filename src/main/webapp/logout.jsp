<%@ page language="java" errorPage="/error.jsp" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" session="true"
%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<% session.invalidate(); %>
<c:redirect url="/"/>