import de.hhu.cs.dbs.propra.presentation.rest.OpenAccess;
import org.junit.*;

import static org.junit.Assert.*;


import javax.sql.DataSource;
import javax.ws.rs.core.*;

import java.sql.*;

public class testGetAdresse
{
    private static DataSource ds;

    static OpenAccess c;

    @BeforeClass
    public static void setup()
    {
        ds = TestUtil.getDataSource();
        c = new OpenAccess();
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
