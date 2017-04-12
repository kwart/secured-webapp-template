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

### How to configure it with Elytron

The JBoss specific deployment descriptor (WEB-INF/jboss-web.xml) refers to a `web-tests` security domain. You have to add it to your configuration.
Define the new security domain, either by using JBoss CLI (`jboss-cli.sh` / `jboss-cli.bat`):

Elytron doesn't allow to load property files from classpath in `properties-realm` implementation. Let's use a `FileSystemRealm` to introduce user population. 

```bash
bin/jboss-cli.sh << EOT
embed-server

# create realm with users
/subsystem=elytron/filesystem-realm=web-tests:add(path=web-tests,relative-to=jboss.server.config.dir)

/subsystem=elytron/filesystem-realm=web-tests/identity=user:add()
/subsystem=elytron/filesystem-realm=web-tests/identity=user:set-password(clear={password="user"})
/subsystem=elytron/filesystem-realm=web-tests/identity=user:add-attribute(name=groups, value=["User"])

/subsystem=elytron/filesystem-realm=web-tests/identity=admin:add()
/subsystem=elytron/filesystem-realm=web-tests/identity=admin:set-password(clear={password="admin"})
/subsystem=elytron/filesystem-realm=web-tests/identity=admin:add-attribute(name=groups, value=["User", "Admin"])

# create security domain and other necessary config objects
/subsystem=elytron/simple-role-decoder=web-tests:add(attribute=groups)
/subsystem=elytron/constant-permission-mapper=web-tests:add(permissions=[{class-name="org.wildfly.security.auth.permission.LoginPermission"}])
/subsystem=elytron/security-domain=web-tests:add(default-realm=web-tests, permission-mapper=web-tests, realms=[{role-decoder=web-tests, realm=web-tests}]

# add Elytron security domain mapping from Undertow and EJB subsystems 
/subsystem=elytron/provider-http-server-mechanism-factory=web-tests:add()
/subsystem=elytron/http-authentication-factory=web-tests:add(security-domain=web-tests, \
  http-server-mechanism-factory=web-tests, \
  mechanism-configurations=[ \
    {mechanism-name=DIGEST,mechanism-realm-configurations=[{realm-name=web-tests}]}, \
    {mechanism-name=BASIC,mechanism-realm-configurations=[{realm-name=web-tests}]}, \
    {mechanism-name=FORM]}])
/subsystem=undertow/application-security-domain=web-tests:add(http-authentication-factory=web-tests)

/subsystem=ejb3/application-security-domain=web-tests:add(security-domain=web-tests)
EOT
```

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

## License

* [GNU Lesser General Public License Version 2.1](http://www.gnu.org/licenses/lgpl-2.1-standalone.html)
