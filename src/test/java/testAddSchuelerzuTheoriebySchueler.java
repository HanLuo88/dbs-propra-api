import de.hhu.cs.dbs.propra.application.configurations.SecurityContext;
import de.hhu.cs.dbs.propra.domain.model.Role;
import de.hhu.cs.dbs.propra.domain.model.User;
import de.hhu.cs.dbs.propra.presentation.rest.Control;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.sql.DataSource;
import javax.ws.rs.core.Response;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class testAddSchuelerzuTheoriebySchueler
{
    private static DataSource ds;

    static Control c;

    @BeforeClass
    public static void setDataSource()
    {
        ds = TestUtil.getDataSource();
        c = new Control();

        c.setDatasource(ds);

        MyUriInfo uriinfo = new MyUriInfo("//127.0.0.1/", 8080, "/theorieuebungen/4/fahrschueler/");
        c.setUriInfo(uriinfo);

        Set<Role> roles = new HashSet<>();
        roles.add(Role.VERWALTER);
        User usr = new User("schueler1@web.de", roles);
        SecurityContext sc = new SecurityContext();
        sc.setUser(usr);
        c.setSecurityContext(sc);
    }

    @Test
    public void addSchuelerzuPruefung201() throws SQLException
    {
        try
        {
            Response resp = c.addSchuelerzuTheoriestunde(4);
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
                stmt.executeUpdate("DELETE FROM nehmen_teil WHERE nehmen_teil.rowid = 18;");
                cn.close();
            }
            catch (Exception e)
            {

            }
        }
    }
}
