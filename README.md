# Template for secured Java web applications

Simple Java web application template with the secured content.

## How to get it

You should have [git](http://git-scm.com/) installed

	$ git clone git://github.com/kwart/secured-webapp-template.git

or you can download [current sources as a zip file](https://github.com/kwart/secured-webapp-template/archive/master.zip)

## How to build it

You need to have [Maven](http://maven.apache.org/) installed

	$ cd secured-webapp-template
	$ mvn clean package

If the target container doesn't include JSTL implementation, then set the `jstl` property while calling the Maven build

	$ mvn clean package -Djstl

## How to install it

Copy the produced `secured-webapp.war` from the `target` folder to the deployment folder of your container.

Open the application URL in the browser. E.g. [http://localhost:8080/secured-webapp/](http://localhost:8080/secured-webapp/)

### How to configure it on JBoss AS 7.x / EAP 6.x / WildFly 8.x +

The JBoss specific deployment descriptor (WEB-INF/jboss-web.xml) refers to a `web-tests` security domain. You have to add it to your configuration.
Define the new security domain, either by using JBoss CLI (`jboss-cli.sh` / `jboss-cli.bat`):

	/subsystem=security/security-domain=web-tests:add(cache-type=default)
	/subsystem=security/security-domain=web-tests/authentication=classic:add(login-modules=[{"code"=>"UsersRoles", "flag"=>"required"}]) {allow-resource-service-restart=true}

or by editing `standalone/configuration/standalone.xml`, where you have to add a new child to the `<security-domains>` element

```xml
<security-domain name="web-tests" cache-type="default">
	<authentication>
		<login-module code="UsersRoles" flag="required"/>
	</authentication>
</security-domain>
```

### How to use DIGEST authentication

If you want to enable the `DIGEST` authentication in `web.xml` deployment descriptor, you also need to configure the  `web-tests` security to hash passwords
stored in the  `user.properties` files.

The CLI commands to do it:

	/subsystem=security/security-domain=web-tests:add(cache-type=default)
	/subsystem=security/security-domain=web-tests/authentication=classic:add(login-modules=[{"code"=>"UsersRoles", "flag"=>"required", "module-options" => {"hashAlgorithm" => "MD5", "hashEncoding" => "RFC2617","hashUserPassword" => "false", "hashStorePassword" => "true","passwordIsA1Hash" => "false", "storeDigestCallback" => "org.jboss.security.auth.callback.RFC2617Digest" }}]) {allow-resource-service-restart=true}

which results in following XML representation:

```xml
<security-domain name="web-tests" cache-type="default">
    <authentication>
        <login-module code="UsersRoles" flag="required">
            <module-option name="hashAlgorithm" value="MD5"/>
            <module-option name="hashEncoding" value="RFC2617"/>
            <module-option name="hashUserPassword" value="false"/>
            <module-option name="hashStorePassword" value="true"/>
            <module-option name="passwordIsA1Hash" value="false"/>
            <module-option name="storeDigestCallback" value="org.jboss.security.auth.callback.RFC2617Digest"/>
        </login-module>
    </authentication>
</security-domain>
```

### How to configure it on EAP 7.1 + / WildFly 11 +
	/subsystem=undertow/application-security-domain=web-tests:add(http-authentication-factory=application-http-authentication)

## License

* [GNU Lesser General Public License Version 2.1](http://www.gnu.org/licenses/lgpl-2.1-standalone.html)
