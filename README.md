# WFLY-3651 reproducer

Simple reproducer web application which shows that `META-INF/permissions.xml` are not used. in WildFly

## How to use it:

1. store `wfly3651.war` to `/tmp`
1. start WildFly with JMS enabled (add `-secmgr` as a JBoss Modules argument into `standalone.sh`)
1. deploy the app: `./jboss-cli.sh -c "deploy /tmp/wfly3651.war"`
1. go to [http://localhost:8080/wfly3651/](http://localhost:8080/wfly3651/) and check the output

## License

* [GNU Lesser General Public License Version 2.1](http://www.gnu.org/licenses/lgpl-2.1-standalone.html)
