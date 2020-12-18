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

public class testGetTheoriestunde
{
    private static DataSource ds;

    static Control c;

    @BeforeClass
    public static void setup()
    {
        ds = TestUtil.getDataSource();
        c = new Control();
        c.setDatasource(ds);

        MyUriInfo uriinfo = new MyUriInfo("//127.0.0.1/", 8080, "/theorieuebungen/");
        c.setUriInfo(uriinfo);
    }

    @Test
    public void getTheoriestunde200WithoutParams() throws SQLException
    {
        try
        {
            Response resp = c.getTheorie(null, null, null, null);
            System.out.println("Location: " + resp.getLocation());
            assertEquals(200, resp.getStatus());
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    public void getTheoriestunde200Withfahrschulid() throws SQLException
    {
        try
        {
            Response resp = c.getTheorie(1, null, null, null);
            System.out.println("Location: " + resp.getLocation());
            assertEquals(200, resp.getStatus());
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    public void getTheoriestunde200Withfahrschulidandthemabezeichnung() throws SQLException
    {
        try
        {
            Response resp = c.getTheorie(1, "Schilder", null, null);
            System.out.println("Location: " + resp.getLocation());
            assertEquals(200, resp.getStatus());
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    public void getTheoriestunde200Withfahrschulidandthemabezeichnungunddauer() throws SQLException
    {
        try
        {
            Response resp = c.getTheorie(1, "Schilder", 90, null);
            System.out.println("Location: " + resp.getLocation());
            assertEquals(200, resp.getStatus());
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    public void getTheoriestunde200WithAllParams() throws SQLException
    {
        try
        {
            Response resp = c.getTheorie(1, "Schilder", 90, true);
            System.out.println("Location: " + resp.getLocation());
            assertEquals(200, resp.getStatus());
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
    }
}
