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

public class Control extends Application
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


    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    public String makeJsonString(ResultSet rs, String[] keys) throws SQLException
    {
        ResultSetMetaData rsmd = rs.getMetaData();

        List<String> rows = new ArrayList<>();
        int columnsNumber = rsmd.getColumnCount();
        while (rs.next())
        {
            StringBuilder sbRow = new StringBuilder();
            sbRow.append("\n\t{");
            for (int i = 1; i <= columnsNumber; i++)
            {
                String columnValue = rs.getString(i);
                sbRow.append("\n\t\t");

                String key = keys[i - 1];
                sbRow.append("\"" + key + "\":\"" + columnValue + "\"");
                if (i < columnsNumber)
                {
                    sbRow.append(",");
                }
                sbRow.append("\n");
            }
            sbRow.append("\t}");
            rows.add(sbRow.toString());
        }


        StringBuilder sbArray = new StringBuilder();
        sbArray.append("[");
        for (int i = 0; i < rows.size(); i++)
        {
            sbArray.append(rows.get(i));
            if (i < rows.size() - 1)
            {
                sbArray.append(",");
            }
        }
        sbArray.append("\n]");
        return sbArray.toString();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Ohne Rollen
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

            String getSchuelerID = "Select * from Schueler;";
            Statement stmtSchuelerID = connection.createStatement();
            ResultSet rs1 = stmtSchuelerID.executeQuery(getSchuelerID);
            int rownum = 0;
            while (rs1.next())
            {
                rownum = rownum + 1;
            }
            int lastrow = rownum;
            rs1.close();
            System.out.println("lastrow: " + lastrow);
            Response resp = Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(lastrow)).build()).build();
            int stat = resp.getStatus();
            System.out.println("statuscode: " + stat);
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

            String getVerwalterID = "Select * from Verwalter;";
            Statement stmtVerwalterID = connection.createStatement();
            ResultSet rs1 = stmtVerwalterID.executeQuery(getVerwalterID);
            int rownum = 0;
            while (rs1.next())
            {
                rownum = rownum + 1;
            }
            int lastrow = rownum;
            System.out.println("lastrow: " + lastrow);
            rs1.close();
            return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(lastrow)).build()).build();
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

            String getFahrlehrerID = "Select * from Fahrlehrer;";
            Statement stmtFahrlehrerID = connection.createStatement();
            ResultSet rs1 = stmtFahrlehrerID.executeQuery(getFahrlehrerID);
            int rownum = 0;
            while (rs1.next())
            {
                rownum = rownum + 1;
            }
            int lastrow = rownum;
            System.out.println("lastrow: " + lastrow);
            rs1.close();
            return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(lastrow)).build()).build(); //
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
            String jString = makeJsonString(rs1, keys);
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
            String jString = makeJsonString(rs, keys);
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
            String jString = makeJsonString(rs, keys);
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
            String jString = makeJsonString(rs, keys);
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
            String jString = makeJsonString(rs, keys);
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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Als Administrator authentifiziert
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


            String getFahrlehrerID = "Select * from Fahrschule;";
            Statement stmtFahrlehrerID = connection.createStatement();
            ResultSet rs1 = stmtFahrlehrerID.executeQuery(getFahrlehrerID);
            int rownum = 0;
            while (rs1.next())
            {
                rownum = rownum + 1;
            }
            int lastrow = rownum;
            System.out.println("lastrow: " + lastrow);
            rs1.close();

            return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(lastrow)).build()).build();
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

            String getFahrlehrerID = "Select * from Fahrzeug;";
            Statement stmtFahrlehrerID = connection.createStatement();
            ResultSet rs2 = stmtFahrlehrerID.executeQuery(getFahrlehrerID);
            int rownum = 0;
            while (rs2.next())
            {
                rownum = rownum + 1;
            }
            int lastrow = rownum;
            System.out.println("lastrow: " + lastrow);
            rs2.close();

            return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(lastrow)).build()).build();
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
            System.out.println("maxID: " + maxID);
            int currentID = maxID + 1;
            System.out.println("currentID: " + currentID);


            String addTheorieuebung = "Insert into Theoriestunde(TheoriestundeID, Thema, Dauer, Verpflichtend, Email) " +
                                      "Values(?, ?, ?, ?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(addTheorieuebung);
            preparedStatement.setObject(1, currentID);
            preparedStatement.setObject(2, themabezeichnung);
            preparedStatement.setObject(3, dauer);
            preparedStatement.setObject(4, verpflichtend);
            preparedStatement.setObject(5, fahrschulemail);
            preparedStatement.execute();

            String getFahrlehrerID = "Select * from Theoriestunde;";
            Statement stmtFahrlehrerID = connection.createStatement();
            ResultSet rs2 = stmtFahrlehrerID.executeQuery(getFahrlehrerID);
            int rownum = 0;
            while (rs2.next())
            {
                rownum = rownum + 1;
            }
            int lastrow = rownum;
            System.out.println("lastrow: " + lastrow);
            rs2.close();
            return Response.created(uriInfo.getAbsolutePathBuilder().path(Integer.toString(lastrow)).build()).build();
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
            System.out.println("maxID: " + maxID);
            int newID = maxID + 1;
            System.out.println("CurrentID: " + newID);

            String addPruefungen = "Insert into Pruefung(PruefungsID, Theorie, Gebuehr, Bestanden, Email) " +
                                   "Values(?, ?, ?, ?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(addPruefungen);
            preparedStatement.setObject(1, newID);
            preparedStatement.setObject(2, theorie);
            preparedStatement.setObject(3, gebuehr);
            preparedStatement.setObject(4, ergebnis);
            preparedStatement.setObject(5, fahrschueleremail);
            preparedStatement.execute();

            String getFahrlehrerID = "Select * from Pruefung;";
            Statement stmtFahrlehrerID = connection.createStatement();
            ResultSet rs3 = stmtFahrlehrerID.executeQuery(getFahrlehrerID);
            int rownum = 0;
            while (rs3.next())
            {
                rownum = rownum + 1;
            }
            int lastrow = rownum;
            System.out.println("lastrow: " + lastrow);
            rs3.close();

            return Response.created(uriInfo.getAbsolutePathBuilder().path(Integer.toString(lastrow)).build()).build();
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
            String jString = makeJsonString(rs, keys);
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
    // Als Fahrlehrer authentifiziert
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
            System.out.println("maxID: " + maxID);

            int newID = maxID + 1;
            System.out.println("newID: " + newID);


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

            String getFahrlehrerID = "Select * from Fahrstunde;";
            Statement stmtFahrlehrerID = connection.createStatement();
            ResultSet rs3 = stmtFahrlehrerID.executeQuery(getFahrlehrerID);
            int rownum = 0;
            while (rs3.next())
            {
                rownum = rownum + 1;
            }
            int lastrow = rownum;
            System.out.println("lastrow: " + lastrow);
            rs3.close();
            return Response.created(uriInfo.getAbsolutePathBuilder().path(Integer.toString(lastrow)).build()).build();
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
            String jString = makeJsonString(rs, keys);
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
    @Path("theorieuebungen/{theorieuebungid}/fahrschueler")
    @RolesAllowed("SCHUELER")
    @POST
    public Response addSchuelerzuTheoriestunde(@PathParam("theorieuebungid") Integer theorieuebungid)
    {
        Connection connection = null;
        try
        {
            connection = this.dataSource.getConnection();

            if (theorieuebungid == null)
            {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            String schueleremail = securityContext.getUserPrincipal().getName();
            int rowid = 0;

            String theoriestundeidexistiert =
                    "SELECT count(*) " +
                    "FROM Theoriestunde " +
                    "WHERE ? = TheoriestundeID;";
            PreparedStatement preparedStatement = connection.prepareStatement(theoriestundeidexistiert);
            preparedStatement.setObject(1, theorieuebungid);
            ResultSet rset = preparedStatement.executeQuery();
            if (rset.next() == false)
            {
                String fehler2 = "{\"message\":\"Not Found\"}";
                return Response.status(Response.Status.NOT_FOUND).entity(fehler2).build();
            }


            String addFahrstunden = "Insert into nehmen_teil(Email, TheoriestundeID) " +
                                    "Values(?, ?);";
            PreparedStatement preparedStatement1 = connection.prepareStatement(addFahrstunden);
            preparedStatement1.setObject(1, schueleremail);
            preparedStatement1.setObject(2, theorieuebungid);
            preparedStatement1.execute();
            PreparedStatement getIDr = connection.prepareStatement("SELECT last_insert_rowid()");
            ResultSet rset1 = getIDr.executeQuery();
            rowid = rset1.getInt(1);
            System.out.println("rowID:" + rowid);
            return Response.created(uriInfo.getAbsolutePathBuilder().path(Integer.toString(rowid)).build()).build();
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
    // curl --user schueler1@web.de:schueL3r1passw0rt -X POST "http://localhost:8080/theorieuebungen/4/fahrschueler" -H "accept: */*" -H "Content-Type: multipart/form-data"
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
