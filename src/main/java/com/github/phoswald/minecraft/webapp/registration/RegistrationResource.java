package com.github.phoswald.minecraft.webapp.registration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneOffset;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

@RequestScoped
@Path("/rest/registration")
public class RegistrationResource {
    
    private final Logger logger = Logger.getLogger(getClass());

    @Inject
    @ConfigProperty(name = "app.registration.directory")
    String registrationDirectory;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(RegistrationRequest request) {
        try {
            var now = Instant.now();
            var path = Paths.get(registrationDirectory, request.getEmail() + "@" + now.toEpochMilli() + ".txt");
            var content = "REGISTRATION OF " + now.atOffset(ZoneOffset.UTC) + ":\n"
                    + "EMAIL:   " + request.getEmail() + "\n" 
                    + "NAME:    " + request.getName() + "\n"
                    + "USERID:  " + request.getUserid() + "\n"
                    + "SCHOOL:  " + request.getSchool() + "\n"
                    + "COMMENT: " + request.getComment().replace("\r", "").replace("\n", "\n         ") + "\n";
            Files.writeString(path, content, StandardCharsets.UTF_8);
            var response = new RegistrationResponse();
            response.setMessage("User " + request.getEmail() + " registered");
            logger.infov("Registration successful: email={0}, path={1}", request.getEmail(), path);
            return Response.ok(response).build();
        } catch (IOException e) {
            logger.error("Registration failed", e);
            var response = new RegistrationResponse();
            response.setMessage("Error: " + e);
            return Response.serverError().entity(response).build();
        }
    }
}
