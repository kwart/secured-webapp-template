# WildFly - JSP source code leak reproducer 

Simple Java web application which reproduces the JSP source code leak.

## Get it and build it

You should have [git](http://git-scm.com/) and  [Maven](http://maven.apache.org/) installed

	git clone git://github.com/kwart/secured-webapp-template.git undertow-jsp-source-reproducer
	cd undertow-jsp-source-reproducer
	git checkout undertow-jsp-source-reproducer
	mvn clean package

## Deploy it

Copy the produced `jsp-source.war` from the `target` folder to the deployment folder of your container:

    cp target/jsp-source.war ${WILDFLY_HOME}/standalone/deployments/

where the `${WILDFLY_HOME}` is your WildFly installation directory.

## Reproduce the issue

Try the application URLs in the browser:

* [application root](http://localhost:8080/jsp-source/)
* [index.jsp](http://localhost:8080/jsp-source/index.jsp)
* [index.jsp/](http://localhost:8080/jsp-source/index.jsp/) (i.e. with trailing slash) - ***This one reproduces the issue***

## License

* [GNU Lesser General Public License Version 2.1](http://www.gnu.org/licenses/lgpl-2.1-standalone.html)
