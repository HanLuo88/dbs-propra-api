import de.hhu.cs.dbs.propra.presentation.rest.OpenAccess;
import org.junit.*;

import static org.junit.Assert.*;


import javax.sql.DataSource;
import javax.ws.rs.core.*;

import java.sql.*;

public class testAddSchueler
{
    private static DataSource ds;

    static OpenAccess c;

    @BeforeClass
    public static void setup()
    {
        ds = TestUtil.getDataSource();
        c = new OpenAccess();
        c.setDatasource(ds);

        MyUriInfo uriinfo = new MyUriInfo("//127.0.0.1/", 8080, "/fahrschueler/");
        c.setUriInfo(uriinfo);

    }

    @Test
    public void addschueler201() throws SQLException
    {
        try
        {
            Response resp = c.addfahrschueler("schueler14@web.de", "schueLer1z4passwort", "schueler14vorname", "schueler14nachname", "w", 3);
            int stat = resp.getStatus();
            System.out.println("Location: " + resp.getLocation());
            assertEquals(201, stat);
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
                stmt.executeUpdate("DELETE FROM Nutzer WHERE Email = 'schueler14@web.de';");
                stmt.executeUpdate("DELETE FROM Schueler WHERE Email = 'schueler14@web.de';");
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
            Response resp = c.addfahrschueler("schueler14@web.de", "schueLer1z4passwort", "schueler14vorname", "schueler14nachname", "w", null);
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
                stmt.executeUpdate("DELETE FROM Nutzer WHERE Email = 'schueler14@web.de';");
                stmt.executeUpdate("DELETE FROM Schueler WHERE Email = 'schueler14@web.de';");
                cn.close();
            }
            catch (Exception e)
            {

            }
        }
    }

    @Test
    public void addschueler400mitSQLExcep() throws SQLException
    {
        try
        {
            Response resp = c.addfahrschueler("schueler14@web.de", "schueLer1z4passwort", "schueler14vorname", "schueler14nachname", "w", 10);
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
                stmt.executeUpdate("DELETE FROM Nutzer WHERE Email = 'schueler14@web.de';");
                stmt.executeUpdate("DELETE FROM Schueler WHERE Email = 'schueler14@web.de';");
                cn.close();
            }
            catch (Exception e)
            {

            }
        }
    }
}
