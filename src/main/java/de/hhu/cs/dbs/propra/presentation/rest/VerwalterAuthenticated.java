package de.hhu.cs.dbs.propra.presentation.rest;

import de.hhu.cs.dbs.propra.Application;
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

public class VerwalterAuthenticated extends Application
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
    @Path("fahrschulen")
    @RolesAllowed("VERWALTER")
    @POST
    public Response addfahrschulenbyVerwalter(@FormDataParam("email") String email,
                                              @FormDataParam("website") String website,
                                              @FormDataParam("bezeichnung") String bezeichnung,
                                              @FormDataParam("adresseid") Integer adresseid)
    {
        if (email == null)
        {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        if (website == null)
        {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        if (bezeichnung == null)
        {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        if (adresseid == null)
        {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        Connection connection = null;
        try
        {
            connection = this.dataSource.getConnection();
            String adminemail = securityContext.getUserPrincipal().getName();
            String addfahrschule = "Insert into Fahrschule(Email, Website, Fahrschul_Bezeichnung, AdressID, Adminemail) " +
                                   "Values(?, ?, ?, ?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(addfahrschule);
            preparedStatement.setObject(1, email);
            preparedStatement.setObject(2, website);
            preparedStatement.setObject(3, bezeichnung);
            preparedStatement.setObject(4, adresseid);
            preparedStatement.setObject(5, adminemail);
            preparedStatement.execute();


            String getFahrlehrerID = "Select max(rowid) from Fahrschule;";
            Statement stmtFahrlehrerID = connection.createStatement();
            ResultSet rs1 = stmtFahrlehrerID.executeQuery(getFahrlehrerID);
            int rownum = rs1.getInt(1);
            rs1.close();

            return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(rownum)).build()).build();
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

    // ok
    // curl --user admin1@web.de:admin1p4Sswort  -X POST "http://localhost:8080/fahrschulen" -H "accept: */*" -H "Content-Type: multipart/form-data" -F "email=testfahrschule@web.de" -F "website=https://testfahrschule.de" -F "bezeichnung=testbezeichnung" -F "adresseid=1"
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Path("fahrzeuge")
    @RolesAllowed("VERWALTER")
    @POST
    public Response addFahrzeugebyVerwalter(@FormDataParam("fahrschuleid") Integer fahrschuleid,
                                            @FormDataParam("fahrzeugklasse") Integer fahrzeugklasse,
                                            @FormDataParam("kennzeichen") String kennzeichen,
                                            @FormDataParam("hudatum") String hudatum,
                                            @FormDataParam("erstzulassung") String erstzulassung)
    {
        Connection connection = null;
        try
        {
            connection = this.dataSource.getConnection();
            if (fahrschuleid == null)
            {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            if (fahrzeugklasse == null)
            {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            if (kennzeichen == null)
            {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            if (hudatum == null)
            {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            if (erstzulassung == null)
            {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }


            String adminemailadresse = securityContext.getUserPrincipal().getName();
            String getfahrschulemail = "Select email from fahrschule where fahrschule.rowid = " + fahrschuleid + " and fahrschule.Adminemail= '" + adminemailadresse + "';";
            Statement stmt1 = connection.createStatement();
            ResultSet rs = stmt1.executeQuery(getfahrschulemail);
            if (rs.next() == false)
            {
                String fehler2 = "{\"message\":\"NOT FOUND\"}";
                return Response.status(Response.Status.BAD_REQUEST).entity(fehler2).build();
            }
            String fahrschulemail = rs.getString(1);
            rs.close();


            String getfahrzeugklassebezeichnung = "Select Fahrzeugklasse_Bezeichnung from Fahrzeugklasse where Fahrzeugklasse.rowid = " + fahrzeugklasse + ";";
            Statement stmt2 = connection.createStatement();
            ResultSet rs1 = stmt2.executeQuery(getfahrzeugklassebezeichnung);
            if (rs1.next() == false)
            {
                String fehler2 = "{\"message\":\"NOT FOUND\"}";
                return Response.status(Response.Status.BAD_REQUEST).entity(fehler2).build();
            }
            String fahrzeugklassebezeichnung = rs1.getString(1);
            rs1.close();


            String addfahrzeug = "Insert into Fahrzeug(Kennzeichen, HUDatum, Erstzulassungsdatum, Email, Fahrzeugklasse_Bezeichnung) " +
                                 "Values(?, ?, ?, ?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(addfahrzeug);
            preparedStatement.setObject(1, kennzeichen);
            preparedStatement.setObject(2, hudatum);
            preparedStatement.setObject(3, erstzulassung);
            preparedStatement.setObject(4, fahrschulemail);
            preparedStatement.setObject(5, fahrzeugklassebezeichnung);
            preparedStatement.execute();

            String getFahrlehrerID = "Select max(rowid) from Fahrzeug;";
            Statement stmtFahrlehrerID = connection.createStatement();
            ResultSet rs2 = stmtFahrlehrerID.executeQuery(getFahrlehrerID);
            int rownum = rs2.getInt(1);

            rs2.close();
            return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(rownum)).build()).build();
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

    // ok
    // curl --user admin1@web.de:admin1p4Sswort -X POST "http://localhost:8080/fahrzeuge" -H  "accept: */*" -H  "Content-Type: multipart/form-data" -F "fahrschuleid=1" -F "fahrzeugklasse=1" -F "kennzeichen=NE-0404" -F "hudatum=2004-04-04" -F "erstzulassung=2004-04-04 04:04:04"
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Path("theorieuebungen")
    @RolesAllowed("VERWALTER")
    @POST
    public Response addTheorieuebungbyVerwalter(@FormDataParam("fahrschuleid") Integer fahrschuleid,
                                                @FormDataParam("themabezeichnung") String themabezeichnung,
                                                @FormDataParam("dauer") Integer dauer,
                                                @FormDataParam("verpflichtend") Boolean verpflichtend)
    {
        Connection connection = null;
        try
        {
            connection = this.dataSource.getConnection();
            if (fahrschuleid == null)
            {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            if (themabezeichnung == null)
            {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            if (dauer == null)
            {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            if (verpflichtend == null)
            {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }


            String adminemailadresse = securityContext.getUserPrincipal().getName();
            String getfahrschulemail = "Select email from fahrschule where fahrschule.rowid = " + fahrschuleid + " and fahrschule.Adminemail= '" + adminemailadresse + "';";
            Statement stmt1 = connection.createStatement();
            ResultSet rs = stmt1.executeQuery(getfahrschulemail);
            if (rs.next() == false)
            {
                String fehler2 = "{\"message\":\"NOT FOUND\"}";
                return Response.status(Response.Status.BAD_REQUEST).entity(fehler2).build();
            }
            String fahrschulemail = rs.getString(1);
            rs.close();

            String getPruefungID = "select max(theoriestunde.theoriestundeid) from theoriestunde;";
            Statement stmt2 = connection.createStatement();
            ResultSet rs1 = stmt2.executeQuery(getPruefungID);
            if (rs1.next() == false)
            {
                String fehler2 = "{\"message\":\"Bad Request\"}";
                return Response.status(Response.Status.BAD_REQUEST).entity(fehler2).build();
            }
            int maxID = rs1.getInt(1);
            int newID = maxID + 1;

            String addTheorieuebung = "Insert into Theoriestunde(TheoriestundeID, Thema, Dauer, Verpflichtend, Email) " +
                                      "Values(?, ?, ?, ?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(addTheorieuebung);
            preparedStatement.setObject(1, newID);
            preparedStatement.setObject(2, themabezeichnung);
            preparedStatement.setObject(3, dauer);
            preparedStatement.setObject(4, verpflichtend);
            preparedStatement.setObject(5, fahrschulemail);
            preparedStatement.execute();

            String getFahrlehrerID = "Select max(rowid) from Theoriestunde;";
            Statement stmtFahrlehrerID = connection.createStatement();
            ResultSet rs2 = stmtFahrlehrerID.executeQuery(getFahrlehrerID);
            int rownum = rs2.getInt(1);
            rs2.close();
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
    // ok
    // curl --user admin1@web.de:admin1p4Sswort -X POST "http://localhost:8080/theorieuebungen" -H  "accept: */*" -H  "Content-Type: multipart/form-data" -F "fahrschuleid=1" -F "themabezeichnung=Testbezeichnung" -F "dauer=90" -F "verpflichtend=true"
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Path("pruefungen")
    @RolesAllowed("VERWALTER")
    @POST
    public Response addPruefungenbyVerwalter(@FormDataParam("fahrschuelerid") Integer fahrschuelerid,
                                             @FormDataParam("gebuehr") Double gebuehr,
                                             @FormDataParam("typ") Boolean typ,
                                             @FormDataParam("ergebnis") Boolean ergebnis)
    {
        Connection connection = null;
        try
        {
            connection = this.dataSource.getConnection();
            if (fahrschuelerid == null)
            {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            if (gebuehr == null)
            {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            if (typ == null)
            {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            if (ergebnis == null)
            {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }


            String getfahrschueleremail = "Select email from Schueler where Schueler.rowid = " + fahrschuelerid + ";";
            Statement stmt1 = connection.createStatement();
            ResultSet rs = stmt1.executeQuery(getfahrschueleremail);
            if (rs.next() == false)
            {
                String fehler2 = "{\"message\":\"NOT FOUND\"}";
                return Response.status(Response.Status.BAD_REQUEST).entity(fehler2).build();
            }
            String fahrschueleremail = rs.getString(1);
            rs.close();

            boolean theorie = false;
            if (typ == false)
            {
                theorie = true;
            }
            String getPruefungID = "select max(pruefung.pruefungsid) from pruefung;";
            Statement stmt3 = connection.createStatement();
            ResultSet rs2 = stmt3.executeQuery(getPruefungID);
            if (rs2.next() == false)
            {
                String fehler2 = "{\"message\":\"Bad Request\"}";
                return Response.status(Response.Status.BAD_REQUEST).entity(fehler2).build();
            }
            int maxID = rs2.getInt(1);
            int newID = maxID + 1;

            String addPruefungen = "Insert into Pruefung(PruefungsID, Theorie, Gebuehr, Bestanden, Email) " +
                                   "Values(?, ?, ?, ?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(addPruefungen);
            preparedStatement.setObject(1, newID);
            preparedStatement.setObject(2, theorie);
            preparedStatement.setObject(3, gebuehr);
            preparedStatement.setObject(4, ergebnis);
            preparedStatement.setObject(5, fahrschueleremail);
            preparedStatement.execute();

            String getFahrlehrerID = "Select max(rowid) from Pruefung;";
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

    // ok
    // curl --user admin1@web.de:admin1p4Sswort -X POST "http://localhost:8080/pruefungen" -H  "accept: */*" -H  "Content-Type: multipart/form-data" -F "fahrschuelerid=1" -F "gebuehr=30.0" -F "typ=true" -F "ergebnis=true"
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Path("pruefungen")
    @RolesAllowed("VERWALTER")
    @GET
    public Response getPruefungbyVerwalter(@QueryParam("fahrschuelerid") Integer fahrschuelerid,
                                           @QueryParam("gebuehr") Double gebuehr,
                                           @QueryParam("typ") Boolean typ,
                                           @QueryParam("ergebnis") Boolean ergebnis)
    {
        Connection connection = null;
        try
        {
            connection = this.dataSource.getConnection();
            String getPruefung = "select Schueler.rowid, Pruefung.gebuehr, Pruefung.theorie, Pruefung.Bestanden " +
                                 "from Schueler join Pruefung on Schueler.email = Pruefung.email where TRUE ";
            if (fahrschuelerid != null)
            {
                getPruefung = getPruefung + "AND Schueler.rowid = " + fahrschuelerid + " ";
            }
            if (gebuehr != null)
            {
                getPruefung = getPruefung + "AND Pruefung.Gebuehr >= " + gebuehr + " ";
            }
            if (typ != null)
            {
                getPruefung = getPruefung + "AND Pruefung.Theorie = " + typ + " ";
            }
            if (ergebnis != null)
            {
                getPruefung = getPruefung + "AND Pruefung.Bestanden = " + ergebnis + " ";
            }
            getPruefung = getPruefung + ";";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(getPruefung);

            String[] keys = new String[]{"fahrschulerid", "gebuehr", "typ", "ergebnis"};
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
    // curl --user admin1@web.de:admin1p4Sswort -X GET "http://localhost:8080/pruefungen?fahrschuelerid=1&gebuehr=15.5&typ=false&ergebnis=true" -H  "accept: application/json;charset=UTF-8"
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


}
