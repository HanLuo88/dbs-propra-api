import de.hhu.cs.dbs.propra.Application;
import de.hhu.cs.dbs.propra.presentation.rest.Control;
import org.glassfish.jersey.server.internal.routing.UriRoutingContext;
import org.junit.*;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

import static org.junit.Assert.*;


import javax.sql.DataSource;
import javax.ws.rs.core.*;

import java.io.File;
import java.io.PrintWriter;
import java.net.URI;
import java.sql.*;
import java.util.List;
import java.util.Properties;

public class testAddSchueler extends Application
{
    private static DataSource ds;

    static Control c;

    @BeforeClass
    public static void setup()
    {
        ds = TestUtil.getDataSource();
        c = new Control();
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
