<%@ include file="/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>EJB test servlets</title>
</head>
<body>
<h1>Servlets which calls protected EJB</h1>
<ul>
	<li><a href="<c:url value='/CallProtectedEjbServlet'/>">CallProtectedEjbServlet</a> - unprotected servlet which calls a simple protected <code>HelloWorld</code> EJB<br/>
		Expected behavior: <b>Should fail with javax.ejb.EJBAccessException</b>
	</li>
	<li><a href="<c:url value='/RunAsServlet'/>">RunAsServlet</a> - <code>@RunAs</code> child of <code>/CallProtectedEjbServlet</code> servlet.
		This servlet also calls the protected EJB method in its <code>init()</code> and <code>destroy()</code> methods.<br/>
		Expected behavior: <b>Should proceed and print "Hello world!"</b>
	</li>
</ul>
</body>
</html>