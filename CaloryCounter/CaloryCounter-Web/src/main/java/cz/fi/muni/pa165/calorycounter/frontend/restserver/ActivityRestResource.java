/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.pa165.calorycounter.frontend.restserver;

import cz.fi.muni.pa165.calorycounter.serviceapi.ActivityService;
import cz.fi.muni.pa165.calorycounter.serviceapi.dto.ActivityDto;
import cz.fi.muni.pa165.calorycounter.serviceapi.dto.WeightCategory;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.NoResultException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.RecoverableDataAccessException;

/**
 *
 * @author martin-bryndza
 */
@Path("/activities")
public class ActivityRestResource {

    @Autowired
    private ActivityService activityService;
    final static Logger log = LoggerFactory.getLogger(ActivityRestResource.class);

    /**
     *
     * @return all activities
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getAll() {
        log.debug("Server: getAll()");
        List<ActivityDto> activities = null;
        try {
            activities = activityService.getActive();
        } catch (RecoverableDataAccessException ex) {
            if (ex.getCause().getClass().equals(NoResultException.class)) {
                activities = new LinkedList<>();
            } else {
                throw new WebApplicationException(ex, Response.Status.INTERNAL_SERVER_ERROR);
            }
        }
        GenericEntity<List<ActivityDto>> entity = new GenericEntity<List<ActivityDto>>(activities) {};
        return Response.status(Response.Status.OK).entity(entity).build();
    }

    /**
     *
     * @param weightCategory name of the WeightCategory of which activities
     * shall be returned (in format _130_)
     * @return activities containing information only about given weight
     * category
     */
    @GET
    @Path("/weightcategory/{weightcategory}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getAll(@PathParam("weightcategory") String weightCategory) {
        log.debug("Server: getAll() with weight category: " + weightCategory);
        WeightCategory wc = null;
        try {
            wc = WeightCategory.valueOf(weightCategory);    
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        List<ActivityDto> activities = null;
        try {
            activities = activityService.getActive(wc);
        } catch (RecoverableDataAccessException ex) {
            if (ex.getCause().getClass().equals(NoResultException.class)) {
                activities = new LinkedList<>();
            } else {
                throw new WebApplicationException(ex, Response.Status.INTERNAL_SERVER_ERROR);
            }
        }
        GenericEntity<List<ActivityDto>> entity = new GenericEntity<List<ActivityDto>>(activities) {};
        return Response.status(Response.Status.OK).entity(entity).build();
    }

    /**
     *
     * @param activityId
     * @return information about activity with given name for all weight
     * categories
     */
    @GET
    @Path("/id/{activityId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getById(@PathParam("activityId") String activityId) {
        log.debug("Server: getById(activityId) with id: " + activityId);
        if (activityId == null || activityId.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        Long id;
        try{
           id = Long.parseLong(activityId);
        } catch (NumberFormatException ex){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        ActivityDto activity = null;
        try {
            activity = activityService.get(id);
        } catch (RecoverableDataAccessException ex) {
            if (ex.getCause().getClass().equals(NoResultException.class)) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            } else {
                throw new WebApplicationException(ex, Response.Status.INTERNAL_SERVER_ERROR);
            }
        }
        return Response.status(Response.Status.OK).entity(activity).build();
    }

    /**
     *
     * @param activityName name of the activity
     * @return information about activity with given name for all weight
     * categories
     */
    @GET
    @Path("/name/{activityName}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getByName(@PathParam("activityName") String activityName) {
        log.debug("Server: getByName(activityName) with name: " + activityName);
        if (activityName == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        ActivityDto activity = null;
        try {
            activity = activityService.get(activityName);
        } catch (RecoverableDataAccessException ex) {
            if (ex.getCause().getClass().equals(NoResultException.class)) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            } else {
                throw new WebApplicationException(ex, Response.Status.INTERNAL_SERVER_ERROR);
            }
        }
        return Response.status(Response.Status.OK).entity(activity).build();
    }

}
