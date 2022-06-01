package com.github.phoswald.minecraft.webapp.registration;

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
@Path("/rest/registration")
public class RegistrationResource {

    private final Logger logger = Logger.getLogger(getClass());

    @Inject
    RegistrationRepository repository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findRegistrations(//
            @QueryParam("skip") Integer skip, //
            @QueryParam("limit") Integer limit) {
        try {
            var response = repository.findRegistrations(skip, limit);
            logger.infov("Registrations found: count={0}", response.size());
            return Response.ok(new GenericEntity<List<Registration>>(response) { }).build();
        } catch (UncheckedIOException e) {
            logger.error("Registration search failed: ", e);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createRegistration(Registration request) {
        try {
            var id = repository.createRegistration(request);
            logger.infov("Registration created: email={0}, userId={1}, id={2}", request.getEmail(),
                    request.getUserId(), id);
            return Response.ok().build();
        } catch (IllegalArgumentException e) {
            logger.warnv("Registration creation failed: {0}", e.toString());
            return Response.status(Status.BAD_REQUEST).build();
        } catch (UncheckedIOException e) {
            logger.error("Registration creation failed: ", e);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DELETE
    @Path("{id}")
    public Response deleteRegistration(@PathParam("id") String id) {
        try {
            repository.deleteRegistration(id);
            logger.infov("Registration deleted: id={0}", id);
            return Response.ok().build();
        } catch (UncheckedIOException e) {
            logger.error("Registration deletion failed: ", e);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
