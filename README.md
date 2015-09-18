# Single JSP protected app with DIGEST authentication

Simple Java web application with the secured content. It contains single JSP (`index.jsp`) which prints name of currently logged user.

## Server configuration to use DIGEST authentication

Use JBoss CLI to configure security domain:

    /subsystem=security/security-domain=web-tests:add
    /subsystem=security/security-domain=web-tests/authentication=classic:add {allow-resource-service-restart=true}
    /subsystem=security/security-domain=web-tests/authentication=classic/login-module=UsersRoles:add( \
      code=UsersRoles, flag=required, module-options=[ \
        ("hashAlgorithm"=>"MD5"), \
        ("hashEncoding"=>"RFC2617"), \
        ("hashUserPassword"=>"false"), \
        ("hashStorePassword"=>"true"), \
        ("passwordIsA1Hash"=>"false"), \
        ("storeDigestCallback"=>"org.jboss.security.auth.callback.RFC2617Digest") \
      ]) {allow-resource-service-restart=true}


## How to install it

Copy the `secured-webapp.war` to the WildFly/EAP deployment folder (`$JBOSS_HOME/standalone/deployments`).

## How to use it

Open the application URL in the browser. E.g. [http://localhost:8080/secured-webapp/](http://localhost:8080/secured-webapp/)

Authenticate with username `"admin"` and password `"admin"`.

You should see following text on the page:

    Principal: admin

## License

* [GNU Lesser General Public License Version 2.1](http://www.gnu.org/licenses/lgpl-2.1-standalone.html)
