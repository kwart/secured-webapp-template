# Single JSP - Elytron current domain
Simple Java web application with single `index.jsp`.
It just prints result of the following call:

```java
org.wildfly.security.auth.server.SecurityDomain.getCurrent()
```

## How to install it

Copy the WAR into the WildFly/EAP deployment folder (`$JBOSS_HOME/standalone/deployments`).

## License

* [GNU Lesser General Public License Version 2.1](http://www.gnu.org/licenses/lgpl-2.1-standalone.html)
