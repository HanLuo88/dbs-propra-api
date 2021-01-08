import de.hhu.cs.dbs.propra.presentation.rest.OpenAccess;
import org.junit.*;

import static org.junit.Assert.*;


import javax.sql.DataSource;
import javax.ws.rs.core.*;

import java.sql.*;

public class testGetTheoriestunde
{
    private static DataSource ds;

    static OpenAccess c;

    @BeforeClass
    public static void setup()
    {
        ds = TestUtil.getDataSource();
        c = new OpenAccess();
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
