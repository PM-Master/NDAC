package src.resources;

import src.dao.DAO;
import src.model.Record;
import src.model.Visit;
import src.response.ApiResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.beans.PropertyVetoException;
import java.sql.SQLException;

@Path("records")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RecordsResource {
    private DAO dao;
    public RecordsResource() throws PropertyVetoException, SQLException, ClassNotFoundException {
        dao = DAO.getDao();
    }

    @GET
    public Response getRecords(@QueryParam("sessionId") String sessionId) throws SQLException {
        return new ApiResponse(dao.getRecords(sessionId)).toResponse();
    }

    @Path("/{patientId}")
    @GET
    public Response getRecord(@PathParam("patientId") int patientId, @QueryParam("sessionId") String sessionId) throws SQLException {
        return new ApiResponse(dao.getRecord(patientId, sessionId)).toResponse();
    }

    @Path("/{patientId}")
    @PUT
    public Response updateRecord(@PathParam("patientId") int patientId, Record data, @QueryParam("sessionId") String sessionId) throws SQLException {
        dao.updateRecord(patientId, data, sessionId);
        return new ApiResponse("success").toResponse();
    }

    /**
     * Get the vitals that were most recently entered for the patient
     * @param patientId the ID of the patient
     * @return the Vitals
     * @throws SQLException
     */
    @Path("/{patientId}/vitals")
    @GET
    public Response getLastVitals(@PathParam("patientId") int patientId, @QueryParam("sessionId") String sessionId) throws SQLException {
        return new ApiResponse(dao.getLastVitals(patientId, sessionId)).toResponse();
    }

    @Path("/{patientId}/visits")
    @GET
    public Response getVisits(@PathParam("patientId") int patientId, @QueryParam("sessionId") String sessionId) throws SQLException {
        return new ApiResponse(dao.getVisits(patientId, sessionId)).toResponse();
    }

    @Path("/{patientId}/visits")
    @POST
    public Response createVisit(@PathParam("patientId") int patientId, @QueryParam("sessionId") String sessionId) throws SQLException {
        return new ApiResponse(dao.createVisit(patientId, sessionId)).toResponse();
    }

    @Path("/{patientId}/visits/{visitId}")
    @PUT
    public Response updateVisit(@PathParam("patientId") int patientId, @PathParam("visitId") int visitId, Visit data, @QueryParam("sessionId") String sessionId) throws SQLException {
        return new ApiResponse(dao.updateVisit(patientId, visitId, data, sessionId)).toResponse();
    }

    @Path("/{patientId}/medicines")
    @GET
    public Response getMedicines(@PathParam("patientId") int patientId, @QueryParam("sessionId") String sessionId) throws SQLException {
        return new ApiResponse(dao.getMedicines(patientId, sessionId)).toResponse();
    }

    /*@Path("/visits")
    @GET
    public Response getVisits(@QueryParam("sessionId") String sessionId) throws SQLException {
        return new ApiResponse(dao.getVisits()).toResponse();
    }*/
}
