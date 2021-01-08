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

public class OpenAccess extends Application
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
    @Path("fahrschueler")
    @POST
    public Response addfahrschueler(@FormDataParam("email") String emailaddresse,
                                    @FormDataParam("passwort") String passwort,
                                    @FormDataParam("vorname") String vorname,
                                    @FormDataParam("nachname") String nachname,
                                    @FormDataParam("geschlecht") String geschlecht,
                                    @FormDataParam("adresseid") Integer adresseid)
    {
        if (emailaddresse == null)
        {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        if (passwort == null)
        {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        if (vorname == null)
        {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        if (nachname == null)
        {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        if (geschlecht == null)
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
            String addNutzer = "INSERT INTO Nutzer(Email, Vorname, Nachname, Passwort) " +
                               "VALUES(?,?,?,?)";
            PreparedStatement prpaddnutzer = connection.prepareStatement(addNutzer);
            prpaddnutzer.setObject(1, emailaddresse);
            prpaddnutzer.setObject(2, vorname);
            prpaddnutzer.setObject(3, nachname);
            prpaddnutzer.setObject(4, passwort);
            prpaddnutzer.executeUpdate();

            String addSchueler = "INSERT INTO Schueler(Email, Geschlecht, AdressID) " +
                                 "VALUES(?,?,?)";
            PreparedStatement prpaddschueler = connection.prepareStatement(addSchueler);
            prpaddschueler.setObject(1, emailaddresse);
            prpaddschueler.setObject(2, geschlecht);
            prpaddschueler.setObject(3, adresseid);
            prpaddschueler.executeUpdate();

            String getSchuelerID = "Select max(rowid) from Schueler;";
            Statement stmtSchuelerID = connection.createStatement();
            ResultSet rs1 = stmtSchuelerID.executeQuery(getSchuelerID);
            int rownum = rs1.getInt(1);
            rs1.close();
            Response resp = Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(rownum)).build()).build();
            return resp;
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
    // curl -X POST "http://localhost:8080/fahrschueler" -H  "accept: */*" -H  "Content-Type: multipart/form-data" -F "email=schueler14@web.de" -F "passwort=schueLer1z4passwort" -F "vorname=schueler14vorname" -F "nachname=schueler14nachname" -F "geschlecht=w" -F "adresseid=3"
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Path("admins")
    @POST
    public Response addAdmin(@FormDataParam("email") String emailaddresse,
                             @FormDataParam("passwort") String passwort,
                             @FormDataParam("vorname") String vorname,
                             @FormDataParam("nachname") String nachname,
                             @FormDataParam("telefonnummer") Integer telefonnummer)
    {
        if (emailaddresse == null)
        {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        if (passwort == null)
        {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        if (vorname == null)
        {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        if (nachname == null)
        {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        if (telefonnummer == null)
        {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        Connection connection = null;
        try
        {
            connection = this.dataSource.getConnection();
            String addNutzer = "INSERT INTO Nutzer(Email, Vorname, Nachname, Passwort) " +
                               "VALUES(?,?,?,?)";
            PreparedStatement prpaddnutzer = connection.prepareStatement(addNutzer);
            prpaddnutzer.setObject(1, emailaddresse);
            prpaddnutzer.setObject(2, vorname);
            prpaddnutzer.setObject(3, nachname);
            prpaddnutzer.setObject(4, passwort);
            prpaddnutzer.executeUpdate();


            String addAdmin = "INSERT INTO Verwalter(Email, Telefonnummer) " +
                              "VALUES(?,?)";
            PreparedStatement prpaddAdmin = connection.prepareStatement(addAdmin);
            prpaddAdmin.setObject(1, emailaddresse);
            prpaddAdmin.setObject(2, telefonnummer);
            prpaddAdmin.executeUpdate();

            String getVerwalterID = "Select max(rowid) from Verwalter;";
            Statement stmtVerwalterID = connection.createStatement();
            ResultSet rs1 = stmtVerwalterID.executeQuery(getVerwalterID);
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
    // curl -X POST "http://localhost:8080/admins" -H  "accept: */*" -H  "Content-Type: multipart/form-data" -F "email=admin4@web.de" -F "passwort=admin4p4Sswort" -F "vorname=admin4vorname" -F "nachname=admin4nachname" -F "telefonnummer=444444"
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Path("fahrlehrer")
    @POST
    public Response addFahrlehrer(@FormDataParam("email") String emailaddresse,
                                  @FormDataParam("passwort") String passwort,
                                  @FormDataParam("vorname") String vorname,
                                  @FormDataParam("nachname") String nachname,
                                  @FormDataParam("lizenzdatum") String lizenzdatum)
    {
        if (emailaddresse == null)
        {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        if (passwort == null)
        {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        if (vorname == null)
        {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        if (nachname == null)
        {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        if (lizenzdatum == null)
        {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Connection connection = null;
        try
        {
            connection = this.dataSource.getConnection();

            String addNutzer = "INSERT INTO Nutzer(Email, Vorname, Nachname, Passwort) " +
                               "VALUES(?,?,?,?)";
            PreparedStatement prpaddnutzer = connection.prepareStatement(addNutzer);
            prpaddnutzer.setObject(1, emailaddresse);
            prpaddnutzer.setObject(2, vorname);
            prpaddnutzer.setObject(3, nachname);
            prpaddnutzer.setObject(4, passwort);
            prpaddnutzer.executeUpdate();

            String addFahrlehrer = "INSERT INTO Fahrlehrer(Email, Erstlizenzdatum) " +
                                   "VALUES(?,?)";
            PreparedStatement prpaddFahrlehrer = connection.prepareStatement(addFahrlehrer);
            prpaddFahrlehrer.setObject(1, emailaddresse);
            prpaddFahrlehrer.setObject(2, lizenzdatum);
            prpaddFahrlehrer.executeUpdate();

            String getFahrlehrerID = "Select max(rowid) from Fahrlehrer;";
            Statement stmtFahrlehrerID = connection.createStatement();
            ResultSet rs1 = stmtFahrlehrerID.executeQuery(getFahrlehrerID);
            int rownum = rs1.getInt(1);
            rs1.close();
            return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(rownum)).build()).build(); //
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
    // curl -X POST "http://localhost:8080/fahrlehrer" -H  "accept: */*" -H  "Content-Type: multipart/form-data" -F "email=fahrlehrer8@web.de" -F "passwort=fahrL3hrer8passwort" -F "vorname=fahrlehrer8vorname" -F "nachname=fahrlehrer8nachname" -F "lizenzdatum=2018-06-12 00:00:00"
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Path("fahrlehrer")
    @GET
    public Response getFahrlehrer(@QueryParam("lizenzdatum") String lizenzdatum,
                                  @QueryParam("nachname") String nachname)
    {

        Connection connection = null;
        try
        {
            connection = this.dataSource.getConnection();
            String getFahrlehrer = "select fahrlehrer.rowid, fahrlehrer.erstlizenzdatum, fahrlehrer.email, nutzer.passwort, nutzer.vorname, nutzer.nachname from fahrlehrer join nutzer on fahrlehrer.email = nutzer.email where true ";
            if (lizenzdatum != null)
            {
                getFahrlehrer = getFahrlehrer + "AND fahrlehrer.erstlizenzdatum >= '" + lizenzdatum + "' ";
            }
            if (nachname != null)
            {
                getFahrlehrer = getFahrlehrer + "AND nutzer.nachname LIKE '%" + nachname + "%' ";
            }
            getFahrlehrer = getFahrlehrer + ";";
            Statement stmt = connection.createStatement();

            ResultSet rs1 = stmt.executeQuery(getFahrlehrer);
            String[] keys = new String[]{"fahrlehrerid", "lizenzdatum", "email", "passwort", "vorname", "nachname"};
            String jString = Jasonize.makeJsonString(rs1, keys);
            rs1.close();
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
    // curl -X GET "http://localhost:8080/fahrlehrer" -H  "accept: application/json;charset=UTF-8"
    // curl -X GET "http://localhost:8080/fahrlehrer?lizenzdatum=2012-06-12%2000%3A00%3A00" -H  "accept: application/json;charset=UTF-8"
    // curl -X GET "http://localhost:8080/fahrlehrer?nachname=fahrlehrer2nachname" -H  "accept: application/json;charset=UTF-8"
    // curl -X GET "http://localhost:8080/fahrlehrer?lizenzdatum=2012-06-12%2000%3A00%3A00&nachname=fahrlehrer2nachname" -H  "accept: application/json;charset=UTF-8"
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Path("fahrschulen")
    @GET
    public Response getFahrschule(@QueryParam("bezeichnung") String bezeichnung,
                                  @QueryParam("fahrzeugklasse") String fahrzeugklasse)
    {
        Connection connection = null;
        try
        {
            connection = this.dataSource.getConnection();
            String getFahrschulen = "select fahrschule.rowid, fahrschule.adressid, fahrschule.email, fahrschule.website, fahrschule.fahrschul_bezeichnung " +
                                    "from fahrschule join fahrzeug on fahrschule.email = fahrzeug.email where true ";
            if (bezeichnung != null)
            {
                getFahrschulen = getFahrschulen + "AND Fahrschule.Fahrschul_Bezeichnung LIKE '%" + bezeichnung + "%' ";
            }
            if (fahrzeugklasse != null)
            {
                getFahrschulen = getFahrschulen + "AND Fahrzeug.Fahrzeugklasse_Bezeichnung = '" + fahrzeugklasse + "' ";
            }
            getFahrschulen = getFahrschulen + ";";
            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery(getFahrschulen);

            String[] keys = new String[]{"fahrschuleid", "adresseid", "email", "website", "bezeichnung"};
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
    // curl -X GET "http://localhost:8080/fahrschulen" -H  "accept: application/json;charset=UTF-8"
    // curl -X GET "http://localhost:8080/fahrschulen?bezeichnung=Bezeichnung%201" -H  "accept: application/json;charset=UTF-8"
    // curl -X GET "http://localhost:8080/fahrschulen?fahrzeugklasse=A1" -H  "accept: application/json;charset=UTF-8"
    // curl -X GET "http://localhost:8080/fahrschulen?bezeichnung=Bezeichnung%201&fahrzeugklasse=A1" -H  "accept: application/json;charset=UTF-8"
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Path("fahrzeuge")
    @GET
    public Response getFahrzeuge(@QueryParam("kennzeichen") String kennzeichen,
                                 @QueryParam("erstzulassungsdatum") String erstzulassungsdatum)
    {

        Connection connection = null;
        try
        {
            connection = this.dataSource.getConnection();
            String getFahrzeug = "select fahrzeug.rowid, fahrschule.rowid, fahrzeug.kennzeichen, fahrzeug.hudatum, fahrzeug.erstzulassungsdatum " +
                                 "from fahrzeug join fahrschule on fahrzeug.email = fahrschule.email where TRUE ";
            if (kennzeichen != null)
            {
                getFahrzeug = getFahrzeug + "AND Fahrzeug.Kennzeichen LIKE '%" + kennzeichen + "%' ";
            }
            if (erstzulassungsdatum != null)
            {

                getFahrzeug = getFahrzeug + "AND Fahrzeug.Erstzulassungsdatum >= '" + erstzulassungsdatum + "' ";
            }
            getFahrzeug = getFahrzeug + ";";
            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery(getFahrzeug);

            String[] keys = new String[]{"fahrzeugid", "fahrschuleid", "kennzeichen", "hudatum", "erstzulassung"};
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
    // curl -X GET "http://localhost:8080/fahrzeuge?erstzulassungsdatum=2001-01-01%2011%3A11%3A11" -H  "accept: application/json;charset=UTF-8"
    // curl -X GET "http://localhost:8080/fahrzeuge?kennzeichen=NE-0101" -H  "accept: application/json;charset=UTF-8"
    // curl -X GET "http://localhost:8080/fahrzeuge?kennzeichen=NE-0101&erstzulassungsdatum=2001-01-01%2011%3A11%3A11" -H  "accept: application/json;charset=UTF-8"
    // curl -X GET "http://localhost:8080/fahrzeuge" -H  "accept: application/json;charset=UTF-8"
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @Path("adressen")
    @GET
    public Response getAdresse(@QueryParam("hausnummer") String hausnummer)
    {
        Connection connection = null;
        try
        {
            connection = this.dataSource.getConnection();
            String getAdressebyhausnummer = "SELECT * FROM Adresse WHERE TRUE ";
            if (hausnummer != null)
            {
                getAdressebyhausnummer = getAdressebyhausnummer + "AND Hausnummer LIKE '%" + hausnummer + "%' ";
            }
            getAdressebyhausnummer = getAdressebyhausnummer + ";";
            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery(getAdressebyhausnummer);


            String[] keys = new String[]{"AdressID", "PLZ", "Stadt", "Strasse", "Hausnummer"};
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
    // curl -X GET "http://localhost:8080/adressen?hausnummer=1" -H  "accept: application/json;charset=UTF-8"
    // curl -X GET "http://localhost:8080/adressen" -H  "accept: application/json;charset=UTF-8"
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Path("theorieuebungen")
    @GET
    public Response getTheorie(@QueryParam("fahrschuleid") Integer fahrschuleid,
                               @QueryParam("themabezeichnung") String themabezeichnung,
                               @QueryParam("dauer") Integer dauer,
                               @QueryParam("verpflichtend") Boolean verpflichtend)
    {
        Connection connection = null;
        try
        {
            connection = this.dataSource.getConnection();
            String getTheoriestunde = "select theoriestunde.rowid, fahrschule.rowid, theoriestunde.thema, theoriestunde.verpflichtend, theoriestunde.dauer " +
                                      "from theoriestunde join fahrschule on theoriestunde.email = fahrschule.email where TRUE ";
            if (fahrschuleid != null)
            {
                getTheoriestunde = getTheoriestunde + "AND Fahrschule.rowid = " + fahrschuleid + " ";
            }
            if (themabezeichnung != null)
            {
                getTheoriestunde = getTheoriestunde + "AND Theoriestunde.Thema = '" + themabezeichnung + "' ";
            }
            if (dauer != null)
            {
                getTheoriestunde = getTheoriestunde + "AND Theoriestunde.Dauer >= " + dauer + " ";
            }
            if (verpflichtend != null)
            {
                getTheoriestunde = getTheoriestunde + "AND Theoriestunde.Verpflichtend = " + verpflichtend + " ";
            }
            getTheoriestunde = getTheoriestunde + ";";
            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery(getTheoriestunde);
            String[] keys = new String[]{"theorieuebungid", "fahrschuleid", "themabezeichnung", "verpflichtend", "dauer"};
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
    // curl -X GET "http://localhost:8080/theorieuebungen?fahrschuleid=3&verpflichtend=true" -H  "accept: application/json;charset=UTF-8"
    // curl -X GET "http://localhost:8080/theorieuebungen?fahrschuleid=3&verpflichtend=false" -H  "accept: application/json;charset=UTF-8"
    // curl -X GET "http://localhost:8080/theorieuebungen?fahrschuleid=1&themabezeichnung=Schilder" -H  "accept: application/json;charset=UTF-8"
    // curl -X GET "http://localhost:8080/theorieuebungen?fahrschuleid=1" -H  "accept: application/json;charset=UTF-8"
    // curl -X GET "http://localhost:8080/theorieuebungen" -H  "accept: application/json;charset=UTF-8"
    // curl -X GET "http://localhost:8080/theorieuebungen?themabezeichnung=Schilder&verpflichtend=true" -H  "accept: application/json;charset=UTF-8"
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


}
