import de.hhu.cs.dbs.propra.application.configurations.SecurityContext;
import de.hhu.cs.dbs.propra.domain.model.Role;
import de.hhu.cs.dbs.propra.domain.model.User;
import de.hhu.cs.dbs.propra.presentation.rest.VerwalterAuthenticated;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.sql.DataSource;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class testAddFahrzeugbyVerwalter
{
    private static DataSource ds;

    static VerwalterAuthenticated c;

    @BeforeClass
    public static void setDataSource()
    {
        ds = TestUtil.getDataSource();
        c = new VerwalterAuthenticated();

        c.setDatasource(ds);

        MyUriInfo uriinfo = new MyUriInfo("//127.0.0.1/", 8080, "/fahrzeuge/");
        c.setUriInfo(uriinfo);

        Set<Role> roles = new HashSet<>();
        roles.add(Role.VERWALTER);
        User usr = new User("admin1@web.de", roles);
        SecurityContext sc = new SecurityContext();
        sc.setUser(usr);
        c.setSecurityContext(sc);

    }

    @Test
    public void addFahrzeug() throws SQLException
    {
        try
        {
            Response resp = c.addFahrzeugebyVerwalter(1, 1, "NE-0404", "2004-04-04", "2004-04-04 04:04:04");
            assertEquals(201, resp.getStatus());

            System.out.println("Location: " + resp.getLocation());
            System.out.println();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("fail");
        }
        finally
        {
            try
            {
                Connection cn = ds.getConnection();
                Statement stmt = cn.createStatement();
                stmt.executeUpdate("DELETE FROM Fahrzeug WHERE Kennzeichen = 'NE-0404';");
                cn.close();
            }
            catch (Exception e)
            {

            }
        }
    }
}
