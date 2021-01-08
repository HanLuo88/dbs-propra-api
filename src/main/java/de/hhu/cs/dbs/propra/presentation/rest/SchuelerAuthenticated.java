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

public class SchuelerAuthenticated extends Application
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

            String getFahrlehrerID = "Select max(rowid) from nehmen_teil;";
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
    // curl --user schueler1@web.de:schueL3r1passw0rt -X POST "http://localhost:8080/theorieuebungen/4/fahrschueler" -H "accept: */*" -H "Content-Type: multipart/form-data"
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
