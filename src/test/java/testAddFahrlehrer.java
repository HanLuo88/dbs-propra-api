import de.hhu.cs.dbs.propra.presentation.rest.OpenAccess;
import org.junit.*;

import static org.junit.Assert.*;


import javax.sql.DataSource;
import javax.ws.rs.core.*;

import java.sql.*;

public class testAddFahrlehrer
{
    private static DataSource ds;

    static OpenAccess c;

    @BeforeClass
    public static void setup()
    {
        ds = TestUtil.getDataSource();
        c = new OpenAccess();
        c.setDatasource(ds);

        MyUriInfo uriinfo = new MyUriInfo("//127.0.0.1/", 8080, "/fahrlehrer/");
        c.setUriInfo(uriinfo);

    }

    @Test
    public void addFahrlehrer201() throws SQLException
    {
        try
        {
            Response resp = c.addFahrlehrer("fahrlehrer8@web.de", "fahrL3hrer8passwort", "fahrlehrer8vorname", "fahrlehrer8nachname", "2018-06-12 00:00:00");
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
                stmt.executeUpdate("DELETE FROM Nutzer WHERE Email = 'fahrlehrer8@web.de';");
                stmt.executeUpdate("DELETE FROM Fahrlehrer WHERE Email = 'fahrlehrer8@web.de';");
                cn.close();
            }
            catch (Exception e)
            {

            }
        }
    }

    @Test
    public void addFahrlehrer400V1() throws SQLException
    {
        try
        {
            Response resp = c.addFahrlehrer(null, "fahrL3hrer8passwort", "fahrlehrer8vorname", "fahrlehrer8nachname", "2018-06-12 00:00:00");
            int stat = resp.getStatus();
            System.out.println("Location: " + resp.getLocation());

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
                stmt.executeUpdate("DELETE FROM Nutzer WHERE Email = 'fahrlehrer8@web.de';");
                stmt.executeUpdate("DELETE FROM Fahrlehrer WHERE Email = 'fahrlehrer8@web.de';");
                cn.close();
            }
            catch (Exception e)
            {

            }
        }
    }

    @Test
    public void addFahrlehrer400V2() throws SQLException
    {
        try
        {
            Response resp = c.addFahrlehrer(null, null, "fahrlehrer8vorname", "fahrlehrer8nachname", "2018-06-12 00:00:00");
            int stat = resp.getStatus();
            System.out.println("Location: " + resp.getLocation());

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
                stmt.executeUpdate("DELETE FROM Nutzer WHERE Email = 'fahrlehrer8@web.de';");
                stmt.executeUpdate("DELETE FROM Fahrlehrer WHERE Email = 'fahrlehrer8@web.de';");
                cn.close();
            }
            catch (Exception e)
            {

            }
        }
    }
}
