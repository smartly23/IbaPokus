/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.pa165.calorycounter.frontend.restserver;

import cz.fi.muni.pa165.calorycounter.serviceapi.ActivityRecordService;
import cz.fi.muni.pa165.calorycounter.serviceapi.ActivityService;
import cz.fi.muni.pa165.calorycounter.serviceapi.UserActivityRecordsService;
import cz.fi.muni.pa165.calorycounter.serviceapi.UserService;
import cz.fi.muni.pa165.calorycounter.serviceapi.dto.ActivityDto;
import cz.fi.muni.pa165.calorycounter.serviceapi.dto.ActivityRecordDto;
import cz.fi.muni.pa165.calorycounter.serviceapi.dto.AuthUserDto;
import cz.fi.muni.pa165.calorycounter.serviceapi.dto.UserActivityRecordsDto;
import java.util.List;
import javax.persistence.NoResultException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.RecoverableDataAccessException;

/**
 *
 * @author bryndza
 */
@Path("/record")
public class RecordRestResource {

    @Autowired
    private ActivityRecordService recordService;
    @Autowired
    private UserActivityRecordsService recordsService;
    @Autowired
    private UserService userService;
    @Autowired
    private ActivityService activityService;
    final static Logger log = LoggerFactory.getLogger(ActivityRestResource.class);

    @GET
    public Response getText() {
        throw new WebApplicationException(Response.Status.NOT_FOUND);
    }

    @GET
    @Path("/user/{username}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getRecordsOfUser(@PathParam("username") String username) {
        log.debug("Server: getRecordsOfUser(username) with username: " + username);
        if (username == null || username.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        AuthUserDto user = null;
        try {
            user = userService.getByUsername(username);
        } catch (RecoverableDataAccessException ex) {
            if (ex.getCause().getClass().equals(NoResultException.class)) {
                return Response.status(Response.Status.NOT_FOUND).build();
            } else {
                throw new WebApplicationException(ex, Response.Status.INTERNAL_SERVER_ERROR);
            }
        }
        UserActivityRecordsDto records = null;
        try {
            records = recordsService.getAllActivityRecords(user);
        } catch (RecoverableDataAccessException ex) {
            if (ex.getCause().getClass().equals(NoResultException.class)) {
                records = new UserActivityRecordsDto();
                records.setNameOfUser(user.getName());
            } else {
                throw new WebApplicationException(ex, Response.Status.INTERNAL_SERVER_ERROR);
            }
        }
        return Response.status(Response.Status.OK).entity(records).build();
    }

    @GET
    @Path("/id/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getRecord(@PathParam("id") String id) {
        log.debug("Server: getRecord(id) with id: " + id);
        if (id == null || id.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        Long idL;
        try {
            idL = Long.parseLong(id);
        } catch (NumberFormatException ex) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        ActivityRecordDto record = null;
        try {
            record = recordService.get(idL);
        } catch (RecoverableDataAccessException ex) {
            if (ex.getCause().getClass().equals(NoResultException.class)) {
                return Response.status(Response.Status.NOT_FOUND).build();
            } else {
                throw new WebApplicationException(ex, Response.Status.INTERNAL_SERVER_ERROR);
            }
        }
        return Response.status(Response.Status.OK).entity(record).build();
    }

    @POST
    @Path("/create")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response create(ActivityRecordDto record) {
        log.debug("Server: create(record): " + record);
        if (record == null || record.getActivityName() == null || record.getActivityDate() == null || record.getUserId() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        if (record.getWeightCategory() == null) {
            AuthUserDto user = null;
            try {
                user = userService.getById(record.getUserId());
            } catch (RecoverableDataAccessException ex) {
                if (ex.getCause().getClass().equals(NoResultException.class)) {
                    return Response.status(Response.Status.NOT_FOUND).build();
                } else {
                    throw new WebApplicationException(ex, Response.Status.INTERNAL_SERVER_ERROR);
                }
            }
            record.setWeightCategory(user.getWeightCategory());
        }
        if (record.getCaloriesBurnt() == 0) {
            List<ActivityDto>activities = activityService.getActive(record.getWeightCategory());
            ActivityDto activity = null;
            for (ActivityDto act : activities) {
                if (act.getActivityName().equals(record.getActivityName())) {
                    activity = act;
                    break;
                }
            }
            record.setCaloriesBurnt(record.getDuration() * (activity.getCaloriesAmount(record.getWeightCategory())) / 60);
        }
        Long id;
        try {
            id = recordService.create(record);
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        ActivityRecordDto newRecord;
        try {
            newRecord = recordService.get(id);
        } catch (RecoverableDataAccessException ex) {
            throw new WebApplicationException(ex, Response.Status.INTERNAL_SERVER_ERROR);
        }
        Response.ResponseBuilder builder = Response.status(Response.Status.OK);
        builder.entity(newRecord);
        return makeCORS(builder);
    }
    
    @PUT
    @Path("/update")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response update(ActivityRecordDto recordToUpdate) {
        log.debug("Server: update with record: " + recordToUpdate);
        if (recordToUpdate == null || recordToUpdate.getActivityRecordId()== null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        //TODO > count again caloriesBurnt and fill original if params are null
        try {
            recordService.update(recordToUpdate);
        } catch (RecoverableDataAccessException ex) {
            if (ex.getCause().getClass().equals(IllegalArgumentException.class)) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            throw new WebApplicationException(ex, Response.Status.INTERNAL_SERVER_ERROR);
        }
        ActivityRecordDto updatedRecord;
        try {
            updatedRecord = recordService.get(recordToUpdate.getActivityRecordId());
        } catch (RecoverableDataAccessException ex) {
            throw new WebApplicationException(ex, Response.Status.INTERNAL_SERVER_ERROR);
        }
        Response.ResponseBuilder builder = Response.status(Response.Status.OK);
        builder.entity(updatedRecord);
        return makeCORS(builder);
    }
    
    @DELETE
    @Path("/remove/{id}")
    public Response remove(@PathParam("id") String id) {
        log.debug("Server: remove(id) with id: " + id);
        if (id == null || id.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        Long idL;
        try {
            idL = Long.parseLong(id);
        } catch (NumberFormatException ex) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        try {
            recordService.remove(idL);
        } catch (RecoverableDataAccessException ex) {
            if (ex.getCause().getClass().equals(IllegalArgumentException.class)
                    || ex.getCause().getClass().equals(NoResultException.class)) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            throw new WebApplicationException(ex, Response.Status.INTERNAL_SERVER_ERROR);
        }
        Response.ResponseBuilder builder = Response.status(Response.Status.OK);
        return makeCORS(builder);
    }

    private String _corsHeaders;

    private Response makeCORS(Response.ResponseBuilder req, String returnMethod) {
        Response.ResponseBuilder rb = req.header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");

        if (!"".equals(returnMethod)) {
            rb.header("Access-Control-Allow-Headers", returnMethod);
        }

        return rb.build();
    }

    private Response makeCORS(Response.ResponseBuilder req) {
        return makeCORS(req, _corsHeaders);
    }

    // This OPTIONS request/response is necessary
    // if you consumes other format than text/plain or
    // if you use other HTTP verbs than GET and POST
    // nevím jak mapovat vice cest najednou, podle různých zdroju to snad ani nejde?
    @OPTIONS
    @Path("/create")
    public Response corsMyResource(@HeaderParam("Access-Control-Request-Headers") String requestH) {
        _corsHeaders = requestH;
        return makeCORS(Response.ok(), requestH);
    }

    @OPTIONS
    @Path("/update")
    public Response corsMyResource2(@HeaderParam("Access-Control-Request-Headers") String requestH) {
        _corsHeaders = requestH;
        return makeCORS(Response.ok(), requestH);
    }

    @OPTIONS
    @Path("/remove/{uname}")
    public Response corsMyResource3(@HeaderParam("Access-Control-Request-Headers") String requestH) {
        _corsHeaders = requestH;
        return makeCORS(Response.ok(), requestH);
    }

}
