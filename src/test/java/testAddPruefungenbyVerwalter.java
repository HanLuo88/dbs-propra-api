import de.hhu.cs.dbs.propra.application.configurations.SecurityContext;
import de.hhu.cs.dbs.propra.domain.model.Role;
import de.hhu.cs.dbs.propra.domain.model.User;
import de.hhu.cs.dbs.propra.presentation.rest.VerwalterAuthenticated;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.sql.DataSource;
import javax.ws.rs.core.Response;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class testAddPruefungenbyVerwalter
{
    private static DataSource ds;

    static VerwalterAuthenticated c;

    @BeforeClass
    public static void setDataSource()
    {
        ds = TestUtil.getDataSource();
        c = new VerwalterAuthenticated();

        c.setDatasource(ds);

        MyUriInfo uriinfo = new MyUriInfo("//127.0.0.1/", 8080, "/pruefungen/");
        c.setUriInfo(uriinfo);

        Set<Role> roles = new HashSet<>();
        roles.add(Role.VERWALTER);
        User usr = new User("admin1@web.de", roles);
        SecurityContext sc = new SecurityContext();
        sc.setUser(usr);
        c.setSecurityContext(sc);
    }

    @Test
    public void addPruefung201() throws SQLException
    {
        try
        {
            Response resp = c.addPruefungenbyVerwalter(1, 30.0, true, true);
            assertEquals(201, resp.getStatus());

            System.out.println("Location: " + resp.getLocation());
            System.out.println();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("fail");
        }
        finally
        {
            try
            {
                Connection cn = ds.getConnection();
                Statement stmt = cn.createStatement();
                stmt.executeUpdate("DELETE FROM Pruefung WHERE Pruefung.PruefungsID = (SELECT max(Pruefung.PruefungsID) from Pruefung);");
                cn.close();
            }
            catch (Exception e)
            {

            }
        }
    }

    @Test
    public void addPruefung400V1() throws SQLException
    {
        try
        {
            Response resp = c.addPruefungenbyVerwalter(1, 30.0, null, null);
            assertEquals(400, resp.getStatus());

            System.out.println("Location: " + resp.getLocation());
            System.out.println();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("fail");
        }
        finally
        {
            try
            {
                Connection cn = ds.getConnection();
                Statement stmt = cn.createStatement();
                int rowid;
                PreparedStatement getIDr = cn.prepareStatement("SELECT last_insert_rowid()");
                ResultSet rset1 = getIDr.executeQuery();
                rowid = rset1.getInt(1);
                stmt.executeUpdate("DELETE FROM Pruefung WHERE pruefungsid = " + rowid + ";");
                cn.close();
            }
            catch (Exception e)
            {

            }
        }
    }

    @Test
    public void addPruefung400V2() throws SQLException
    {
        try
        {
            Response resp = c.addPruefungenbyVerwalter(100, 30.0, true, true);
            assertEquals(400, resp.getStatus());

            System.out.println("Location: " + resp.getLocation());
            System.out.println();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("fail");
        }
        finally
        {
            try
            {
                Connection cn = ds.getConnection();
                Statement stmt = cn.createStatement();
                int rowid;
                PreparedStatement getIDr = cn.prepareStatement("SELECT last_insert_rowid()");
                ResultSet rset1 = getIDr.executeQuery();
                rowid = rset1.getInt(1);
                stmt.executeUpdate("DELETE FROM Pruefung WHERE pruefungsid = " + rowid + ";");
                cn.close();
            }
            catch (Exception e)
            {

            }
        }
    }

    @Test
    public void addPruefung400V3() throws SQLException
    {
        try
        {
            Response resp = c.addPruefungenbyVerwalter(6, 30.0, true, true);
            assertEquals(400, resp.getStatus());

            System.out.println("Location: " + resp.getLocation());
            System.out.println();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("fail");
        }
        finally
        {
            try
            {
                Connection cn = ds.getConnection();
                Statement stmt = cn.createStatement();
                int rowid;
                PreparedStatement getIDr = cn.prepareStatement("SELECT last_insert_rowid()");
                ResultSet rset1 = getIDr.executeQuery();
                rowid = rset1.getInt(1);
                stmt.executeUpdate("DELETE FROM Pruefung WHERE pruefungsid = " + rowid + ";");
                cn.close();
            }
            catch (Exception e)
            {

            }
        }
    }

    @Test
    public void addPruefung400V4() throws SQLException
    {
        try
        {
            Response resp = c.addPruefungenbyVerwalter(6, 30.0, false, true);
            assertEquals(400, resp.getStatus());

            System.out.println("Location: " + resp.getLocation());
            System.out.println();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("fail");
        }
        finally
        {
            try
            {
                Connection cn = ds.getConnection();
                Statement stmt = cn.createStatement();
                int rowid;
                PreparedStatement getIDr = cn.prepareStatement("SELECT last_insert_rowid()");
                ResultSet rset1 = getIDr.executeQuery();
                rowid = rset1.getInt(1);
                stmt.executeUpdate("DELETE FROM Pruefung WHERE TheoriestundeID = " + rowid);
                cn.close();
            }
            catch (Exception e)
            {

            }
        }
    }
}
