<%@ page language="java" pageEncoding="UTF-8" contentType="text/plain;charset=utf-8" session="false"
%>SecurityIdentity=<%= 
  org.wildfly.security.auth.server.SecurityDomain.getCurrent()
    .authenticate("guest", 
      new org.wildfly.security.evidence.PasswordGuessEvidence("guest".toCharArray()))
%>
