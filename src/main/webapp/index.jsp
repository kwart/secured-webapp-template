<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" session="false"
  import="org.jboss.security.config.*"
%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"
%>
<!doctype html>
<html lang="en">

<head>
<meta charset="utf-8">

<title>Java Security Manager + Prograde</title>

<meta name="description" content="Java Security Manager + Prograde">
<meta name="author" content="Josef Cacek">

<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="apple-mobile-web-app-status-bar-style"
	content="black-translucent" />

<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">

<link rel="stylesheet" href="css/reveal.min.css">
<link rel="stylesheet" href="css/theme/default.css" id="theme">

<!-- For syntax highlighting -->
<link rel="stylesheet" href="lib/css/zenburn.css">

<!-- If the query includes 'print-pdf', use the PDF print sheet -->
<script>
			document.write( '<link rel="stylesheet" href="css/print/' + ( window.location.search.match( /print-pdf/gi ) ? 'pdf' : 'paper' ) + '.css" type="text/css" media="print">' );
		</script>

<!--[if lt IE 9]>
		<script src="lib/js/html5shiv.js"></script>
		<![endif]-->
</head>

<body>

	<div class="reveal">

		<!-- Any section element inside of this container is displayed as a slide -->
		<div class="slides">

			<section>
				<h1>Do you run ...</h1>
				<ul class="fragment">
					<li>applications deployed on an application server</li>
					<li>modular Java applications</li>
					<ul>
					<li>IDE with plugins</li>
					<li>RapidShare downloaders, etc.</li>
					</ul>
				</ul>
				<h1 style="padding-top: 30px">?</h1>
			</section>

			<section data-background="images/joker.jpg">
				<h2 style="color: red;">You should be affraid</h2>
				<h1 class="fragment" style="color: red;">You are treatened!</h1>
			</section>

			<section>
				<h2>What can evil applications and lazy programmers do?</h2>
			</section>

<section>
			<section>
				<h3>Play with numbers</h3>
					<pre>
						<code data-trim contenteditable
							style="font-size: 24px; margin-top: 20px;">
java.lang.reflect.Field field = 
  Integer.class.getDeclaredField( "value" );
field.setAccessible( true );
field.setInt(Integer.valueOf(1),
  Integer.parseInt(request.getParameter("oneVal")));
</code>
					</pre>
<div>					
<form action="int.jsp" method="get">
Fill a magic value:<br>
<input value="42" name="oneVal"><br>
<input type="submit" name="submit" value="Do a magic">
</form>
</div>
			</section>


			<section>
				<h3>Get a coffee break</h3>
<pre><code data-trim contenteditable
		style="font-size: 28px; margin-top: 20px;">
System.exit(0);
</code></pre>
<form action="exit.jsp" method="get">
<input type="submit" name="submit" value="Try it!">
</form>
			</section>

			<section>
				<h3>... and more</h3>
				<ul>
				<li>Access system resources</li>
				<ul>
				<li>network</li>
				<li>filesystem</li>
				</ul>
				<li>Call static methods</li>
				<ul>
				<li>singletons,</li>
				<li>caches,</li>
				<li class="fragment highlight-red">... are they protected?</li>
				</ul>
				</ul>
			</section>
</section>
<section>
			<section>
				<h3>Solution - Java Security Manager</h3>
				<ul>
				<li>standard part of Java SE</li>
				<li>used to check if the comming user/code has permission to run the protected code</li>
				</ul>
				<h4 style="padding-top: 30px">Protected method sample</h4>
<pre><code data-trim contenteditable style="font-size: 24px; margin-top: 20px;">
// this check could be on the top of method Util.getPasswordCache()
SecurityManager sm = System.getSecurityManager();
if (sm != null) {
  RuntimePermission p = new RuntimePermission(
    "org.jboss.test.Util.getPasswordCache");
  sm.checkPermission(p);
}
doSomething();
</code></pre>
			</section>

			<section>
				<h3>Security Manager - Quickstart</h3>
<pre class="fragment"><code data-trim contenteditable style="font-size: 24px; margin-top: 20px;">
java -Djava.security.manager \
-Djava.security.policy==/home/test/application.policy \
...
</code></pre>
				<h4  class="fragment">Policy</h4>
					<pre class="fragment">
<code data-trim contenteditable
	style="font-size: 24px; margin-top: 20px;">
// Grant all to the AS itself
grant codeBase "file:\${jboss.home.dir}/jboss-modules.jar" {
  permission java.security.AllPermission;
};
grant codeBase "file:\${jboss.home.dir}/modules/system/layers/base/-" {
  permission java.security.AllPermission;
};
// Grant this to everyone - Jasper needs this
grant { 
  permission java.lang.RuntimePermission &quot;getClassLoader&quot;;    
};
</code>
					</pre>
</section>

			<section>
				<h3>Policy file</h3>
				<ul>
				<li>Grant permissions to</li>
				<ul>
				<li>codeBase (path)</li>
				<li>signed code</li>
				<li>principal</li>
				<li>all</li>
				</ul>
				</ul>
<pre><code data-trim contenteditable
	style="font-size: 24px; margin-top: 20px;">
keystore "/home/sysadmin/code.keystore";

grant signedBy "sysadmin", codeBase "file:/home/sysadmin/*" {
  permission java.security.SecurityPermission "Security.insertProvider.*";
  permission java.security.SecurityPermission "Security.setProperty.*";
};
</code></pre>
			</section>
</section>
<section>
			<section>
				<h3>ProGrade library</h3>
				<ul>
				<li>Policy Rules Of GRanting And DEnying</li>
				<li>adds <strong class="fragment highlight-red">deny</strong> entries to policy files</li>
				<li>result of a diploma thesis (for Red Hat)</li>
				<li>backward compatible</li>
				<li>LGPL</li>
				</ul>
</section>

			<section>
				<h3>ProGrade - Show me the code!</h3>
<pre><code data-trim contenteditable
	style="font-size: 24px; margin-top: 20px;">
&lt;dependency&gt;
	&lt;groupId&gt;net.sourceforge.pro-grade&lt;/groupId&gt;
	&lt;artifactId&gt;pro-grade&lt;/artifactId&gt;
	&lt;version&gt;1.0&lt;/version&gt;
&lt;/dependency&gt;
</code></pre>

<pre><code data-trim contenteditable
	style="font-size: 24px; margin-top: 20px;">
-Djava.security.manager=net.sourceforge.prograde.sm.ProgradeSecurityManager
</code></pre>
<pre><code data-trim contenteditable
	style="font-size: 24px; margin-top: 20px;">
grant {
  permission java.security.AllPermission;
};

deny codeBase "file:${jboss.home.dir}/standalone/-" {
  permission java.lang.RuntimePermission "accessDeclaredMembers";
  permission java.lang.RuntimePermission "exitVM.*";
};
</code></pre>
</section>
</section>
			<section>
				<h3>Is the security problem only on app servers?</h3>
				<h1 class="fragment" style="color: red;">No!</h1>
				<ul class="fragment" >
				  <li>Whenever you use 3rd party libraries</li>
				  <li>Whenever your application allows use plugins</li>
				</ul>
				<br/>
				<h2 class="fragment" style="color: red;padding-top:50px;">Use the Security Manager, Luke!</h2>
			</section>

			<section>
				<h1>The End</h1>
			</section>
		</div>

	</div>

	<script src="lib/js/head.min.js"></script>
	<script src="js/reveal.min.js"></script>

	<script>

			// Full list of configuration options available here:
			// https://github.com/hakimel/reveal.js#configuration
			Reveal.initialize({
				controls: true,
				progress: true,
				history: true,
				center: true,

				theme: Reveal.getQueryHash().theme, // available themes are in /css/theme
				transition: Reveal.getQueryHash().transition || 'default', // default/cube/page/concave/zoom/linear/fade/none

				// Optional libraries used to extend on reveal.js
				dependencies: [
					{ src: 'lib/js/classList.js', condition: function() { return !document.body.classList; } },
					{ src: 'plugin/markdown/marked.js', condition: function() { return !!document.querySelector( '[data-markdown]' ); } },
					{ src: 'plugin/markdown/markdown.js', condition: function() { return !!document.querySelector( '[data-markdown]' ); } },
					{ src: 'plugin/highlight/highlight.js', async: true, callback: function() { hljs.initHighlightingOnLoad(); } },
					{ src: 'plugin/zoom-js/zoom.js', async: true, condition: function() { return !!document.body.classList; } },
					{ src: 'plugin/notes/notes.js', async: true, condition: function() { return !!document.body.classList; } }
				]
			});

		</script>

</body>
</html>
