<%@ page language="java" pageEncoding="UTF-8" contentType="text/plain;charset=utf-8"
%>
Principal: ${empty pageContext.request.userPrincipal ?"": pageContext.request.userPrincipal.name}
