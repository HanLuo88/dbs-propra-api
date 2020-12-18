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

public class testGetAdresse
{
    private static DataSource ds;

    static Control c;

    @BeforeClass
    public static void setup()
    {
        ds = TestUtil.getDataSource();
        c = new Control();
        c.setDatasource(ds);

        MyUriInfo uriinfo = new MyUriInfo("//127.0.0.1/", 8080, "/adressen/");
        c.setUriInfo(uriinfo);
    }

    @Test
    public void getAdresse200WithoutParams() throws SQLException
    {
        try
        {
            Response resp = c.getAdresse(null);
            System.out.println("Location: " + resp.getLocation());
            assertEquals(200, resp.getStatus());
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    public void getAdresse200WithHausnummer() throws SQLException
    {
        try
        {
            Response resp = c.getAdresse("1");
            System.out.println("Location: " + resp.getLocation());
            assertEquals(200, resp.getStatus());
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
    }
}
