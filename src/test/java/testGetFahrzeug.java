import de.hhu.cs.dbs.propra.presentation.rest.OpenAccess;
import org.junit.*;

import static org.junit.Assert.*;


import javax.sql.DataSource;
import javax.ws.rs.core.*;

import java.sql.*;

public class testGetFahrzeug
{
    private static DataSource ds;

    static OpenAccess c;

    @BeforeClass
    public static void setup()
    {
        ds = TestUtil.getDataSource();
        c = new OpenAccess();
        c.setDatasource(ds);

        MyUriInfo uriinfo = new MyUriInfo("//127.0.0.1/", 8080, "/fahrzeuge/");
        c.setUriInfo(uriinfo);
    }

    @Test
    public void getFahrzeug200WithoutParams() throws SQLException
    {
        try
        {
            Response resp = c.getFahrzeuge(null, null);
            System.out.println("Location: " + resp.getLocation());
            assertEquals(200, resp.getStatus());
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    public void getFahrzeug200WithKennzeichen() throws SQLException
    {
        try
        {
            Response resp = c.getFahrzeuge("NE-0101", null);
            System.out.println("Location: " + resp.getLocation());
            assertEquals(200, resp.getStatus());
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    public void getFahrzeug200WithErstzulassungsdatum() throws SQLException
    {
        try
        {
            Response resp = c.getFahrzeuge(null, "2001-01-01 11:11:11");
            System.out.println("Location: " + resp.getLocation());
            assertEquals(200, resp.getStatus());
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    public void getFahrzeug200WithBothParams() throws SQLException
    {
        try
        {
            Response resp = c.getFahrzeuge("NE-0101", "2001-01-01 11:11:11");
            System.out.println("Location: " + resp.getLocation());
            assertEquals(200, resp.getStatus());
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
    }
}
