package src.resources;

import src.dao.DAO;
import src.model.User;
import src.requests.LoginRequest;
import src.response.ApiResponse;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.beans.PropertyVetoException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.UUID;

@Path("sessions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SessionsResource {

    private DAO dao;
    public SessionsResource() throws PropertyVetoException, SQLException, ClassNotFoundException {
        dao = DAO.getDao();
    }

    @Path("/{sessionId}")
    @DELETE
    public Response logout(@PathParam("sessionId") String sessionId) throws SQLException, InvalidKeySpecException, NoSuchAlgorithmException {
        dao.deleteSession(sessionId);
        return null;
    }

    @Path("/{sessionId}")
    @GET
    public Response getSession(@PathParam("sessionId") String sessionId) throws SQLException, InvalidKeySpecException, NoSuchAlgorithmException {
        return new ApiResponse(dao.getSession(sessionId)).toResponse();
    }
}
