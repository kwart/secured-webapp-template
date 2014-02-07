<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" session="false"
  import="org.jboss.security.config.*"
%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"
%>
<!doctype html>
<html lang="en">

<head>
<meta charset="utf-8">

<title>Java Programming Course</title>

<meta name="description" content="Java Programming Course">
<meta name="author" content="Josef Cacek">

<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="apple-mobile-web-app-status-bar-style"
	content="black-translucent" />

<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">

<link rel="stylesheet" href="css/reveal.min.css">
<link rel="stylesheet" href="css/theme/sky.css" id="theme">

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
				<h1>Java<h1><h2>Learn by Examples</h2>
				<img src="images/duke.png" style="border: none;"
					alt="Java Duke" />
				<h4>Josef Cacek / JBoss EAP QE / Red Hat</h4>
			</section>

			<section>
				<h3>Assigning variables</h3>
					<pre>
						<code data-trim contenteditable
							style="font-size: 24px; margin-top: 20px;">
Integer a = 1;
System.out.println("Variable 'a' contains value " + a);
</code>
					</pre>
					<h3 class="fragment">Outputs</h3>
					<pre class="fragment">
						<code data-trim contenteditable
							style="font-size: 24px; margin-top: 20px;">
<%
Integer a = 1;
out.println("Variable 'a' contains value " + a);
%>
</code>
					</pre>
			</section>

				<section>
					<h3>For Loops</h3>
					<pre>
						<code data-trim contenteditable
							style="font-size: 24px; margin-top: 20px;">
System.out.println("Hello World! This is my first loop:");
for (int i=1; i<=5; i++) {
  System.out.println((Integer) i);
}
</code>
					</pre>
					<h3 class="fragment">Outputs</h3>
					<pre class="fragment">
						<code data-trim contenteditable
							style="font-size: 24px; margin-top: 20px;">
<%
out.println("Hello World! This is my first loop:");
for (int i=1; i<=5; i++) {
  out.println((Integer) i);
}
%>
</code>
					</pre>
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
