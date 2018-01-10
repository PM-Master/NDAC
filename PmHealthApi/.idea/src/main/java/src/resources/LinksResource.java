package src.resources;

import src.dao.DAO;
import src.response.ApiResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.beans.PropertyVetoException;
import java.sql.SQLException;

@Path("links")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LinksResource {
    private DAO dao;
    public LinksResource() throws PropertyVetoException, SQLException, ClassNotFoundException {
        dao = DAO.getDao();
    }

    @GET
    public Response getLinks(@QueryParam("sessionId") String sessionId) throws SQLException {
        return new ApiResponse(dao.getLinks(sessionId)).toResponse();
    }
}
