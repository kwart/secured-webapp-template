package org.jboss.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Reproducer for DataSource issue <a
 * href="https://bugzilla.redhat.com/show_bug.cgi?id=1172830">https://bugzilla.redhat.com/show_bug.cgi?id=1172830</a>. <br>
 * Before deploying configure the datasource in JBoss CLI:
 * 
 * <pre>
 * /subsystem=security/security-domain=test-sec-domain:add(cache-type=default)
 * /subsystem=security/security-domain=test-sec-domain/authentication=classic:add
 * /subsystem=security/security-domain=test-sec-domain/authentication=classic/login-module=CallerIdentity:add(code=org.picketbox.datasource.security.CallerIdentityLoginModule, flag=required, module-options=[("principal"=>"principal"), ("username"=>"username"), ("password"=>"password")])
 * 
 * /subsystem=datasources/data-source=ExampleDS:disable
 * reload
 * /subsystem=datasources/data-source=ExampleDS:remove
 * /subsystem=datasources/data-source=ExampleDS:add(jndi-name="java:jboss/datasources/ExampleDS",use-java-context=true,connection-url="jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",driver-name=h2,min-pool-size=15,max-pool-size=150,pool-prefill=true,security-domain=test-sec-domain)
 * /subsystem=datasources/data-source=ExampleDS:enable
 * </pre>
 * 
 * @author Josef Cacek
 */
@WebServlet(DataSourceServlet.SERVLET_PATH)
public class DataSourceServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  public static final String SERVLET_PATH = "/*";

  @Resource(mappedName = "java:jboss/datasources/ExampleDS")
  DataSource ds;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Connection con = null;
    response.setContentType("text/plain");
    final PrintWriter out = response.getWriter();
    out.println("BZ-1172830 reproducer\n");

    try {
      out.println("Calling DataSource.getConnection()");
      con = ds.getConnection();

      out.println("Calling Connection.createStatement()");
      Statement stmt = con.createStatement();

      String sql = "create table testtable(testid int)";
      out.println("Executing DDL: " + sql);
      stmt.executeUpdate(sql);

      sql = "insert into testtable values (1)";
      out.println("Executing DML: " + sql);
      stmt.executeUpdate(sql);

      sql = "select * from testtable";
      out.println("Executing Query: " + sql);
      ResultSet rs = stmt.executeQuery(sql);

      out.println("Results:");
      while (rs.next()) {
        out.println(rs.getInt(1));
      }
      out.println("Calling ResultSet.close()");
      rs.close();

      sql = "drop table testtable";
      out.println("Executing DDL: " + sql);
      stmt.executeUpdate(sql);

      stmt.close();

      out.println("Issue https://bugzilla.redhat.com/show_bug.cgi?id=1172830 was not reproduced on this EAP/JDK combination.");
    } catch (SQLException e) {
      e.printStackTrace(out);
    } finally {
      if (con != null) {
        try {
          out.println("Calling Connection.close()");
          con.close();
        } catch (SQLException e) {
          e.printStackTrace(out);
        }
      }
    }
  }

}