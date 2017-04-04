# Single JSP - Elytron authenticate in default domain
Simple Java web application with a single `index.jsp`.
It tries to authenticate with `guest/guest` credentials into default Elytron security domain using call:

```java
  org.wildfly.security.auth.server.SecurityDomain.getCurrent()
    .authenticate("guest", 
      new org.wildfly.security.evidence.PasswordGuessEvidence("guest".toCharArray()))
```

## How to install it

1. Copy the WAR into the WildFly/EAP deployment folder (`$JBOSS_HOME/standalone/deployments`).
1. Configure Undertow default domain to use Elytron:

```bash
bin/jboss-cli.sh -c <<EOT
/subsystem=undertow/application-security-domain=other:add(http-authentication-factory=application-http-authentication)
/subsystem=elytron/filesystem-realm=ApplicationFsRealm:add(path=application-users,relative-to=jboss.server.config.dir)
/subsystem=elytron/security-domain=ApplicationDomain:list-add(name=realms, index=0, value={realm=ApplicationFsRealm, role-decoder=groups-to-roles})
/subsystem=elytron/security-domain=ApplicationDomain:write-attribute(name=default-realm, value=ApplicationFsRealm)
/subsystem=elytron/filesystem-realm=ApplicationFsRealm/identity=guest:add()
/subsystem=elytron/filesystem-realm=ApplicationFsRealm/identity=guest:set-password(clear={password="guest"})
/subsystem=elytron/filesystem-realm=ApplicationFsRealm/identity=guest:add-attribute(name=groups, value=["guest"])
reload
EOT

```

## License

* [GNU Lesser General Public License Version 2.1](http://www.gnu.org/licenses/lgpl-2.1-standalone.html)
