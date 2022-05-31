package com.github.phoswald.minecraft.webapp.registration;

import java.io.UncheckedIOException;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;

@RequestScoped
@Path("/rest/registration")
public class RegistrationResource {

    private final Logger logger = Logger.getLogger(getClass());

    @Inject
    RegistrationRepository repository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRegistrations(//
            @QueryParam("skip") Integer skip, //
            @QueryParam("limit") Integer limit) {
        try {
            List<RegistrationInfo> response = repository.getRegistrations(skip, limit);
            logger.infov("Registrations found: count={0}", response.size());
            return Response.ok(new GenericEntity<List<RegistrationInfo>>(response) { }).build();
        } catch (UncheckedIOException e) {
            logger.error("Registration search failed", e);
            GenericResponse response = new GenericResponse();
            response.setMessage("Error: " + e);
            return Response.serverError().entity(response).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addRegistration(RegistrationRequest request) {
        try {
            String id = repository.addRegistration(request);
            GenericResponse response = new GenericResponse();
            response.setMessage("Registration " + id + " created.");
            logger.infov("Registration successful: email={0}, userId={1}, id={2}", request.getEmail(),
                    request.getUserId(), id);
            return Response.ok(response).build();
        } catch (UncheckedIOException e) {
            logger.error("Registration failed", e);
            GenericResponse response = new GenericResponse();
            response.setMessage("Error: " + e);
            return Response.serverError().entity(response).build();
        }
    }
}
