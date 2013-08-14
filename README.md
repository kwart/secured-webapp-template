# BZ-958572 reproducer

## Steps to reproduce

1. save the attached reproducer to `/tmp`

1. unzip EAP 6.1.1 ER4 and open `[EAP_INST]\bin` folder in a terminal window
 
1. Add a new user with Admin role to the ApplicationRealm:

		./add-user.sh -a -u admin -p pass.1234 -r ApplicationRealm -ro Admin

1. start EAP in a domain mode:

		./domain.sh

1. set the `ha` profile for `main-server-group`, enable clustered SSO and deploy the reproducer

		./jboss-cli.sh -c

		/profile=ha/subsystem=web/virtual-server=default-host/sso=configuration:add(cache-container="web",cache-name="sso",reauthenticate="false")
		/server-group=main-server-group:write-attribute(name=profile,value=ha)
		/server-group=main-server-group:write-attribute(name=socket-binding-group, value=ha-sockets)
		/server-group=main-server-group:restart-servers
		deploy /tmp/bz-958572.war --server-groups=main-server-group
		
1. open following link in your browser [http://localhost:8080/bz-958572/](http://localhost:8080/bz-958572/) and go through
the reproducer using "`Next step`" navigation link (you'll need to provide the credentials during 2nd step - `admin/pass.1234`)
1. step 11 doesn't require login  => BUG

## Redeploy reproducer

	./jboss-cli.sh -c

	/server-group=main-server-group/deployment=bz-958572.war:remove()
	undeploy bz-958572.war
	deploy /tmp/bz-958572.war --server-groups=main-server-group