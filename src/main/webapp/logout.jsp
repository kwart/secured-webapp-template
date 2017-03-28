<%@ include file="/taglibs.jsp"%>
User '<%=request.getRemoteUser()%>' has been logged out.
<% session.invalidate(); %>
<br/>
Return to the <a href="<c:url value='/'/>">Index page</a>

