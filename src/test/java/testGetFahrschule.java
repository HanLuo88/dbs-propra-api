import de.hhu.cs.dbs.propra.presentation.rest.OpenAccess;
import org.junit.*;

import static org.junit.Assert.*;


import javax.sql.DataSource;
import javax.ws.rs.core.*;

import java.sql.*;

public class testGetFahrschule
{
    private static DataSource ds;

    static OpenAccess c;

    @BeforeClass
    public static void setup()
    {
        ds = TestUtil.getDataSource();
        c = new OpenAccess();
        c.setDatasource(ds);

        MyUriInfo uriinfo = new MyUriInfo("//127.0.0.1/", 8080, "/fahrschulen/");
        c.setUriInfo(uriinfo);
    }

    @Test
    public void getFahrschule200WithoutParams() throws SQLException
    {
        try
        {
            Response resp = c.getFahrschule(null, null);
            System.out.println("Location: " + resp.getLocation());
            assertEquals(200, resp.getStatus());
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    public void getFahrschule200WithBezeichnung() throws SQLException
    {
        try
        {
            Response resp = c.getFahrschule("Bezeichnung1", null);
            System.out.println("Location: " + resp.getLocation());
            assertEquals(200, resp.getStatus());
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    public void getFahrschule200WithFahrzeugklasse() throws SQLException
    {
        try
        {
            Response resp = c.getFahrschule(null, "A1");
            System.out.println("Location: " + resp.getLocation());
            assertEquals(200, resp.getStatus());
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    public void getFahrschule200WithBothParams() throws SQLException
    {
        try
        {
            Response resp = c.getFahrschule("Bezeichnung1", "A1");
            System.out.println("Location: " + resp.getLocation());
            assertEquals(200, resp.getStatus());
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
    }
}
