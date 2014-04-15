# Servlet with secured GET and POST

Simple Java web application template with the secured content.

## How to build it

You need to have [Maven 3.x](http://maven.apache.org/) installed

	mvn clean package

## How to install it

Start the application server.

### Deploy war

Copy the `secured-webapp.war` from the `target` folder to the `[EAP_HOME]/standalone/deployments`.

### Test applicaton

Use `curl` to test if the GET and POST HTTP methods are protected:

	curl -v -X GET http://localhost:8080/secured-webapp/

should result in "401 Unauthorized" response

	* Adding handle: conn: 0x1330a80
	* Adding handle: send: 0
	* Adding handle: recv: 0
	* Curl_addHandleToPipeline: length: 1
	* - Conn 0 (0x1330a80) send_pipe: 1, recv_pipe: 0
	* About to connect() to localhost port 8080 (#0)
	*   Trying 127.0.0.1...
	* Connected to localhost (127.0.0.1) port 8080 (#0)
	> GET /secured-webapp/ HTTP/1.1
	> User-Agent: curl/7.32.0
	> Host: localhost:8080
	> Accept: */*
	> 
	< HTTP/1.1 401 Unauthorized
	* Server Apache-Coyote/1.1 is not blacklisted
	< Server: Apache-Coyote/1.1
	< Pragma: No-cache
	< Cache-Control: no-cache
	< Expires: Thu, 01 Jan 1970 01:00:00 CET
	< WWW-Authenticate: Basic realm="Secured kingdom"
	< Content-Type: text/html;charset=utf-8
	< Content-Length: 1062
	< Date: Tue, 15 Apr 2014 12:11:28 GMT
	...

and 

	curl -v -X DELETE http://localhost:8080/secured-webapp/

should result in "200 OK" response 

	* Adding handle: conn: 0x1e35a80
	* Adding handle: send: 0
	* Adding handle: recv: 0
	* Curl_addHandleToPipeline: length: 1
	* - Conn 0 (0x1e35a80) send_pipe: 1, recv_pipe: 0
	* About to connect() to localhost port 8080 (#0)
	*   Trying 127.0.0.1...
	* Connected to localhost (127.0.0.1) port 8080 (#0)
	> DELETE /secured-webapp/ HTTP/1.1
	> User-Agent: curl/7.32.0
	> Host: localhost:8080
	> Accept: */*
	> 
	< HTTP/1.1 200 OK
	* Server Apache-Coyote/1.1 is not blacklisted
	< Server: Apache-Coyote/1.1
	< Content-Type: text/plain;charset=ISO-8859-1
	< Content-Length: 15
	< Date: Tue, 15 Apr 2014 12:27:13 GMT
	< 
	Hello, World!
	* Connection #0 to host localhost left intact

## License

* [GNU Lesser General Public License Version 2.1](http://www.gnu.org/licenses/lgpl-2.1-standalone.html)