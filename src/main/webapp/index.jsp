<%@ page language="java" errorPage="/error.jsp" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
  // Can you see this text? Then you've reproduced the issue!

  System.out.println("JSP SOURCE LEAK REPRODUCER - Just put some text into the log file when we hit the JSP in the correct way... ");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>JSP Source code leak reproducer</title>
</head>
<body>
<h1>JSP Source code leak reproducer</h1>
<em>When a trailing slash is added to a JSP URL then source code of the JSP is displayed/downloaded in WildFly 
(tested with 8.1, 8.2 and 9.0 CR1).</em>
<p>
This was originally reported by Abhinav Gupta on <a href="http://stackoverflow.com/questions/30028346/with-trailing-slash-in-url-jsp-show-source-code">stackoverflow</a>.
</p>
<pre>
ServletPath: ${pageContext.request.servletPath}
</pre>
<p>This application contains single JSP, but you can acceess it in several ways:</p>
<ul>
	<li><a href="<c:url value='/'/>">As an application root</a></li>
	<li><a href="<c:url value='/index.jsp'/>">index.jsp</a></li>
	<li><a href="<c:url value='/index.jsp/'/>">index.jsp/</a> (i.e. with trailing slash)- <font color=red>This one reproduces the issue</font></li>
</ul>
</body>
</html>