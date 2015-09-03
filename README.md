# Single JSP protected app

Simple Java web application with the secured content. It contains single JSP (`index.jsp`) which prints name of currently logged user.

## Server configuration to use with LDAP

You can use [simple LDAP server](https://github.com/kwart/ldap-server) for authentication. 

Use JBoss CLI to configure security domain:

    /subsystem=security/security-domain=web-tests:add
    /subsystem=security/security-domain=web-tests/authentication=classic:add
    /subsystem=security/security-domain=web-tests/authentication=classic/login-module=Ldap:add( \
      code=Ldap, flag=required, module-options=[ \
      ("java.naming.provider.url"=>"ldap://127.0.01:10389"), \
      ("java.naming.security.authentication"=>"simple"), \
      ("java.naming.security.principal"=>"uid=admin,ou=system"), \
      ("java.naming.security.credentials"=>"secret"), \
      ("principalDNPrefix"=>"uid="), \
      ("principalDNSuffix"=>",ou=Users,dc=jboss,dc=org"), \
      ("rolesCtxDN"=>"ou=Roles,dc=jboss,dc=org"), \
      ("roleAttributeIsDN"=>"false"), \
      ("roleAttributeID"=>"cn"), \
      ("uidAttributeID"=>"member"), \
      ("matchOnUserDN"=>"true")])


## How to install it

Copy the `secured-webapp.war` to the WildFly/EAP deployment folder (`$JBOSS_HOME/standalone/deployments`).

Open the application URL in the browser. E.g. [http://localhost:8080/secured-webapp/](http://localhost:8080/secured-webapp/)

## License

* [GNU Lesser General Public License Version 2.1](http://www.gnu.org/licenses/lgpl-2.1-standalone.html)
