# Permissions reproducer

Simple reproducer web application which shows that permissions `maximum-set` configured in security-manager subsystem is not used.

## How to use it:

1. store `permissions.war` to `/tmp`
1. start WildFly/EAP with the security manager enabled (`./standalone.sh -secmgr`)
1. configure the permissions `maximum-set` (only one FilePermission added):
```sh
./jboss-cli.sh -c << EOT
/subsystem=security-manager/deployment-permissions=default/maximum-set=default:remove
/subsystem=security-manager/deployment-permissions=default/maximum-set=default:add
/subsystem=security-manager/deployment-permissions=default/maximum-set=default/permission=perm:add(class=java.io.FilePermission, name="/-", actions=read)
reload
EOT
```
1. deploy the app: `./jboss-cli.sh -c "deploy /tmp/permissions.war"`
1. go to [http://localhost:8080/permissions/](http://localhost:8080/permissions/) and check the output

## License

* [GNU Lesser General Public License Version 2.1](http://www.gnu.org/licenses/lgpl-2.1-standalone.html)
