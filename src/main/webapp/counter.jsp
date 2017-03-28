<%@ include file="/taglibs.jsp"
%><%
    Integer requestCount = (Integer)session.getAttribute("requestCount");
    if (requestCount == null) {
        requestCount = 0;
    }
    session.setAttribute("requestCount", ++requestCount);

%>
requestCount=<%= requestCount %>
<br/><br/>
Return to the <a href="<c:url value='/'/>">Index page</a>
