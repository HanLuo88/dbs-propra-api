import de.hhu.cs.dbs.propra.application.configurations.SecurityContext;
import de.hhu.cs.dbs.propra.domain.model.Role;
import de.hhu.cs.dbs.propra.domain.model.User;
import de.hhu.cs.dbs.propra.presentation.rest.FahrlehrerAuthenticated;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.sql.DataSource;
import javax.ws.rs.core.Response;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class testAddFahrstundenbyFahrlehrer
{
    private static DataSource ds;

    static FahrlehrerAuthenticated c;

    @BeforeClass
    public static void setDataSource()
    {
        ds = TestUtil.getDataSource();
        c = new FahrlehrerAuthenticated();

        c.setDatasource(ds);

        MyUriInfo uriinfo = new MyUriInfo("//127.0.0.1/", 8080, "/fahrstunden/");
        c.setUriInfo(uriinfo);

        Set<Role> roles = new HashSet<>();
        roles.add(Role.VERWALTER);
        User usr = new User("fahrlehrer1@web.de", roles);
        SecurityContext sc = new SecurityContext();
        sc.setUser(usr);
        c.setSecurityContext(sc);
    }

    @Test
    public void addFahrstunde201() throws SQLException
    {
        try
        {
            Response resp = c.addFahrstundenbyFahrlehrer(1, 1, 30.5, 90, "DriveByShooting");
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
                stmt.executeUpdate("DELETE FROM Fahrstunde WHERE FahrstundeID = (SELECT max(fahrstunde.FahrstundeID) from Fahrstunde);");
                cn.close();
            }
            catch (Exception e)
            {

            }
        }
    }

    @Test
    public void addFahrstunde400V1() throws SQLException
    {
        try
        {
            Response resp = c.addFahrstundenbyFahrlehrer(100, 1, 30.5, 90, "DriveByShooting");
            assertEquals(400, resp.getStatus());

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
                int rowid;
                PreparedStatement getIDr = cn.prepareStatement("SELECT last_insert_rowid()");
                ResultSet rset1 = getIDr.executeQuery();
                rowid = rset1.getInt(1);
                stmt.executeUpdate("DELETE FROM Fahrstunde WHERE FahrstundeID = " + rowid + ";");
                cn.close();
            }
            catch (Exception e)
            {

            }
        }
    }

    @Test
    public void addFahrstunde400V2() throws SQLException
    {
        try
        {
            Response resp = c.addFahrstundenbyFahrlehrer(1, 1, 30.5, null, "DriveByShooting");
            assertEquals(400, resp.getStatus());

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
                int rowid;
                PreparedStatement getIDr = cn.prepareStatement("SELECT last_insert_rowid()");
                ResultSet rset1 = getIDr.executeQuery();
                rowid = rset1.getInt(1);
                stmt.executeUpdate("DELETE FROM Fahrstunde WHERE FahrstundeID = " + rowid + ";");
                cn.close();
            }
            catch (Exception e)
            {

            }
        }
    }
}
