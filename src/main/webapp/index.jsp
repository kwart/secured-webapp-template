<%@ include file="/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>Sample</title>
</head>
<body>
<pre>
<%
final String attrib="test-attr";
out.println(session.getAttribute(attrib));
session.setAttribute(attrib, "a");
out.println(session.getAttribute(attrib));
%>
</pre>
</body>
</html>