package de.hhu.cs.dbs.propra.presentation.rest;

import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.sql.DataSource;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.GET;
import javax.ws.rs.Path;


import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.List;


@Path("/")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)

public class FahrlehrerAuthenticated extends Application
{
    @Inject
    private DataSource dataSource;

    @Context
    private SecurityContext securityContext;

    @Context
    private UriInfo uriInfo;

    public void setUriInfo(UriInfo uri)
    {
        this.uriInfo = uri;
    }

    public void setDatasource(DataSource ds)
    {
        this.dataSource = ds;
    }

    public void setSecurityContext(de.hhu.cs.dbs.propra.application.configurations.SecurityContext sc)
    {
        this.securityContext = sc;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Path("fahrstunden")
    @RolesAllowed("FAHRLEHRER")
    @POST
    public Response addFahrstundenbyFahrlehrer(@FormDataParam("fahrschuelerid") Integer fahrschuelerid,
                                               @FormDataParam("fahrzeugid") Integer fahrzeugid,
                                               @FormDataParam("preis") Double preis,
                                               @FormDataParam("dauer") Integer dauer,
                                               @FormDataParam("typ") String typ)
    {
        Connection connection = null;
        try
        {
            connection = this.dataSource.getConnection();
            if (fahrschuelerid == null)
            {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            if (fahrzeugid == null)
            {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            if (preis == null)
            {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            if (dauer == null)
            {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            if (typ == null)
            {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            String fahrlehrermail = securityContext.getUserPrincipal().getName();
            String getfahrschulemail = "select fahrschule.email from fahrschule where fahrschule.email = (select fahrzeug.email from fahrzeug where fahrzeug.rowid = " + fahrzeugid + ");";
            Statement stmt1 = connection.createStatement();
            ResultSet rs = stmt1.executeQuery(getfahrschulemail);
            if (rs.next() == false)
            {
                String fehler2 = "{\"message\":\"Bad Request\"}";
                return Response.status(Response.Status.BAD_REQUEST).entity(fehler2).build();
            }
            String fahrschulemail = rs.getString(1);


            String getfahrschueleremail = "Select email from Schueler where Schueler.rowid = " + fahrschuelerid + ";";
            Statement stmt2 = connection.createStatement();
            ResultSet rs1 = stmt2.executeQuery(getfahrschueleremail);
            if (rs1.next() == false)
            {
                String fehler2 = "{\"message\":\"Bad Request\"}";
                return Response.status(Response.Status.BAD_REQUEST).entity(fehler2).build();
            }
            String fahrschueleremail = rs1.getString(1);


            String getFahrstundeID = "select max(fahrstunde.fahrstundeid) from fahrstunde;";
            Statement stmt3 = connection.createStatement();
            ResultSet rs2 = stmt3.executeQuery(getFahrstundeID);
            if (rs2.next() == false)
            {
                String fehler2 = "{\"message\":\"Bad Request\"}";
                return Response.status(Response.Status.BAD_REQUEST).entity(fehler2).build();
            }
            int maxID = rs2.getInt(1);
            int newID = maxID + 1;

            String addFahrstunden = "Insert into Fahrstunde(FahrstundeID, Typ, Dauer, Preis, Schueleremail, Fahrlehreremail, Fahrschulemail) " +
                                    "Values(?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(addFahrstunden);
            preparedStatement.setObject(1, newID);
            preparedStatement.setObject(2, typ);
            preparedStatement.setObject(3, dauer);
            preparedStatement.setObject(4, preis);
            preparedStatement.setObject(5, fahrschueleremail);
            preparedStatement.setObject(6, fahrlehrermail);
            preparedStatement.setObject(7, fahrschulemail);
            preparedStatement.execute();

            String getFahrlehrerID = "Select max(rowid) from Fahrstunde;";
            Statement stmtFahrlehrerID = connection.createStatement();
            ResultSet rs3 = stmtFahrlehrerID.executeQuery(getFahrlehrerID);
            int rownum = rs3.getInt(1);
            rs3.close();
            return Response.created(uriInfo.getAbsolutePathBuilder().path(Integer.toString(rownum)).build()).build();
        }
        catch (Exception se)
        {
            se.printStackTrace();
            try
            {
                connection.rollback();
            }
            catch (Exception rollb)
            {
                System.err.println(rollb.getMessage());
            }
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        finally
        {
            try
            {
                connection.close();
            }
            catch (Exception sl)
            {
                System.err.println(sl.getMessage());
            }
        }
    }

    //
    // curl --user fahrlehrer1@web.de:fahrL3hrer1passwort -X POST "http://localhost:8080/fahrstunden" -H  "accept: */*" -H  "Content-Type: multipart/form-data" -F "fahrschuelerid=1" -F "fahrzeugid=1" -F "preis=30.5" -F "dauer=90" -F "typ=DriveByShooting"
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //get fahrstunden als fahrlehrer
    @Path("fahrstunden")
    @RolesAllowed("FAHRLEHRER")
    @GET
    public Response getFahrstundenbyFahrlehrer(@QueryParam("dauer") Integer dauer)
    {
        Connection connection = null;
        try
        {
            connection = this.dataSource.getConnection();
            String getFahrstunde = "select fahrzeug.rowid as fahrzeugid, fahrschule.rowid as fahrschulid, schueler.rowid as fahrschuelerid, fahrstunde.Preis,Fahrstunde.Dauer, Fahrstunde.Typ  " +
                                   "from fahrschule join fahrstunde on fahrschule.email =fahrstunde.fahrschulemail " +
                                   "join fahrzeug on fahrstunde.fahrschulemail = fahrzeug.email " +
                                   "join schueler on fahrstunde.schueleremail = schueler.email where TRUE ";
            if (dauer != null)
            {
                getFahrstunde = getFahrstunde + "AND fahrstunde.dauer >= " + dauer + " ";
            }

            getFahrstunde = getFahrstunde + ";";
            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery(getFahrstunde);
            String[] keys = new String[]{"fahrzeugid", "fahrschuleid", "fahrschuelerid", "preis", "dauer", "typ"};
            String jString = Jasonize.makeJsonString(rs, keys);
            rs.close();
            return Response.status(Response.Status.OK).entity(jString).build();
        }
        catch (Exception se)
        {
            se.printStackTrace();
            try
            {
                connection.rollback();
            }
            catch (Exception rollb)
            {
                System.err.println(rollb.getMessage());
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        finally
        {
            try
            {
                connection.close();
            }
            catch (Exception sl)
            {
                System.err.println(sl.getMessage());
            }
        }
    }

    // ok
    // curl --user fahrlehrer1@web.de:fahrL3hrer1passwort -X GET "http://localhost:8080/fahrstunden" -H  "accept: application/json;charset=UTF-8"
    // curl --user fahrlehrer1@web.de:fahrL3hrer1passwort -X GET "http://localhost:8080/fahrstunden?dauer=45" -H  "accept: application/json;charset=UTF-8"
    // curl --user fahrlehrer1@web.de:fahrL3hrer1passwort -X GET "http://localhost:8080/fahrstunden?dauer=90" -H  "accept: application/json;charset=UTF-8"
    // curl --user fahrlehrer1@web.de:fahrL3hrer1passwort -X GET "http://localhost:8080/fahrstunden?dauer=135" -H  "accept: application/json;charset=UTF-8"
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


}
