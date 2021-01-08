import de.hhu.cs.dbs.propra.presentation.rest.OpenAccess;
import org.junit.*;

import static org.junit.Assert.*;


import javax.sql.DataSource;
import javax.ws.rs.core.*;

import java.sql.*;

public class testGetFahrlehrer
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
    public void getFahrlehrer200WithoutParams() throws SQLException
    {
        try
        {
            Response resp = c.getFahrlehrer(null, null);
            System.out.println("Location: " + resp.getLocation());
            assertEquals(200, resp.getStatus());
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    public void getFahrlehrer200Withlizenzdatum() throws SQLException
    {
        try
        {
            Response resp = c.getFahrlehrer("2012-06-12 00:00:00", null);
            System.out.println("Location: " + resp.getLocation());
            assertEquals(200, resp.getStatus());
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    public void getFahrlehrer200Withnachname() throws SQLException
    {
        try
        {
            Response resp = c.getFahrlehrer(null, "fahrlehrer2nachname");
            System.out.println("Location: " + resp.getLocation());
            assertEquals(200, resp.getStatus());
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    public void getFahrlehrer200WithBothParams() throws SQLException
    {
        try
        {
            Response resp = c.getFahrlehrer("2012-06-12 00:00:00", "fahrlehrer2nachname");
            System.out.println("Location: " + resp.getLocation());
            assertEquals(200, resp.getStatus());
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
    }
}
