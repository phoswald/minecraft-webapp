package com.github.phoswald.sample.task;

import java.io.UncheckedIOException;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jboss.logging.Logger;

@RequestScoped
@Path("/rest/tasks")
public class TaskResource {

    private final Logger logger = Logger.getLogger(getClass());

    @Inject
    TaskRepository repository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findTasks(//
            @QueryParam("skip") Integer skip, //
            @QueryParam("limit") Integer limit) {
        try {
            var response = repository.findTasks(skip, limit);
            logger.infov("Tasks found: count={0}", Integer.valueOf(response.size()));
            return Response.ok(new GenericEntity<List<Task>>(response) { }).build();
        } catch (UncheckedIOException e) {
            logger.error("Task search failed: ", e);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTask(Task request) {
        try {
            var id = repository.createTask(request);
            logger.infov("Task created: taskId={0}, title={1}", id, request.getTitle());
            return Response.ok().build();
        } catch (IllegalArgumentException e) {
            logger.warnv("Task creation failed: {0}", e.toString());
            return Response.status(Status.BAD_REQUEST).build();
        } catch (UncheckedIOException e) {
            logger.error("Task creation failed: ", e);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DELETE
    @Path("{taskId}")
    public Response deleteTask(@PathParam("taskId") String taskId) {
        try {
            repository.deleteTask(taskId);
            logger.infov("Task deleted: id={0}", taskId);
            return Response.ok().build();
        } catch (UncheckedIOException e) {
            logger.error("Task deletion failed: ", e);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
