<%@ include file="/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>Sample secured web application</title>
</head>
<body>
<h1>
	You are authorized to view
	<%=((HttpServletRequest) pageContext.getRequest()).getServletPath().replaceAll("/(.*)/index.*", "$1")
                   .replaceAll(".*index.*", "unprotected home")%>
	page
</h1>
<pre>
ServletPath: ${pageContext.request.servletPath}
PathInfo: ${pageContext.request.pathInfo}
AuthType:  ${pageContext.request.authType}
Principal: ${empty pageContext.request.userPrincipal ?"": pageContext.request.userPrincipal.name}
</pre>
<p>This application contains 3 pages:</p>
<ul>
	<li><a href="<c:url value='/'/>">Home page</a> - unprotected</li>
	<li><a href="<c:url value='/user/'/>">User page</a> - only users with User or Admin role can access it</li>
	<li><a href="<c:url value='/admin/'/>">Admin page</a> - only users with Admin role can access it</li>
</ul>
<c:if test="${not empty pageContext.request.userPrincipal}">
	<a href="<c:url value='/logout.jsp'/>">Logout</a>
</c:if>
<p>There are 2 user accounts prepared for JBoss AS testing:</p>
<ul>
	<li>user/user with role User</li>
	<li>admin/admin with role Admin</li>
</ul>
<p>AS 5.x should use the user accounts automatically
(just remove jboss-web.xml from WEB-INF folder), AS 7 needs a new security domain:</p>
<pre>
Add security domain to the JBosss AS standalone.xml:

&lt;security-domain name=&quot;web-tests&quot; cache-type=&quot;default&quot;&gt;
	&lt;authentication&gt;
		&lt;login-module code=&quot;UsersRoles&quot; flag=&quot;required&quot;/&gt;
	&lt;/authentication&gt;
&lt;/security-domain&gt;

or use CLI (jboss-cli.sh):

connect
/subsystem=security/security-domain=web-tests:add(cache-type=default)
/subsystem=security/security-domain=web-tests/authentication=classic:add(login-modules=[{"code"=>"UsersRoles", "flag"=>"required"}]) {allow-resource-service-restart=true}
</pre>
</body>
</html>