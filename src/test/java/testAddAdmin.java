import de.hhu.cs.dbs.propra.presentation.rest.OpenAccess;
import org.junit.*;

import static org.junit.Assert.*;


import javax.sql.DataSource;
import javax.ws.rs.core.*;

import java.sql.*;

public class testAddAdmin
{
    private static DataSource ds;

    static OpenAccess c;

    @BeforeClass
    public static void setup()
    {
        ds = TestUtil.getDataSource();
        c = new OpenAccess();
        c.setDatasource(ds);

        MyUriInfo uriinfo = new MyUriInfo("//127.0.0.1/", 8080, "/admins/");
        c.setUriInfo(uriinfo);

    }

    @Test
    public void addschueler201() throws SQLException
    {
        try
        {
            Response resp = c.addAdmin("admin4@web.de", "admin4p4Sswort", "admin4vorname", "admin4nachname", 444444);
            int stat = resp.getStatus();
            System.out.println("Location: " + resp.getLocation());

            assertEquals(201, stat);

            System.out.println();
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
        finally
        {
            try
            {
                Connection cn = ds.getConnection();
                Statement stmt = cn.createStatement();
                stmt.executeUpdate("DELETE FROM Nutzer WHERE Email = 'admin4@web.de';");
                stmt.executeUpdate("DELETE FROM Verwalter WHERE Email = 'admin4@web.de';");
                cn.close();
            }
            catch (Exception e)
            {

            }
        }
    }

    @Test
    public void addschueler400V1() throws SQLException
    {
        try
        {
            Response resp = c.addAdmin("admin4@web.de", "admin4p4Sswort", "admin4vorname", "nachname=admin4nachname", null);
            int stat = resp.getStatus();


            assertEquals(400, stat);

            System.out.println();
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
        finally
        {
            try
            {
                Connection cn = ds.getConnection();
                Statement stmt = cn.createStatement();
                stmt.executeUpdate("DELETE FROM Nutzer WHERE Email = 'admin4@web.de';");
                stmt.executeUpdate("DELETE FROM Verwalter WHERE Email = 'admin4@web.de';");
                cn.close();
            }
            catch (Exception e)
            {

            }
        }
    }

    @Test
    public void addschueler400V2() throws SQLException
    {
        try
        {
            Response resp = c.addAdmin(null, "admin4p4Sswort", "admin4vorname", "nachname=admin4nachname", null);
            int stat = resp.getStatus();


            assertEquals(400, stat);

            System.out.println();
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
        finally
        {
            try
            {
                Connection cn = ds.getConnection();
                Statement stmt = cn.createStatement();
                stmt.executeUpdate("DELETE FROM Nutzer WHERE Email = 'admin4@web.de';");
                stmt.executeUpdate("DELETE FROM Verwalter WHERE Email = 'admin4@web.de';");
                cn.close();
            }
            catch (Exception e)
            {

            }
        }
    }

    @Test
    public void addschueler400V3() throws SQLException
    {
        try
        {
            Response resp = c.addAdmin(null, null, null, null, null);
            int stat = resp.getStatus();


            assertEquals(400, stat);

            System.out.println();
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
        finally
        {
            try
            {
                Connection cn = ds.getConnection();
                Statement stmt = cn.createStatement();
                stmt.executeUpdate("DELETE FROM Nutzer WHERE Email = 'admin4@web.de';");
                stmt.executeUpdate("DELETE FROM Verwalter WHERE Email = 'admin4@web.de';");
                cn.close();
            }
            catch (Exception e)
            {

            }
        }
    }
}
