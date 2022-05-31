package com.github.phoswald.minecraft.webapp.registration;

import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.fasterxml.jackson.databind.ObjectMapper;

@Dependent
class RegistrationRepository {

    private final ObjectMapper mapper = new ObjectMapper();

    @Inject
    @ConfigProperty(name = "app.registration.directory")
    String registrationDirectory;

    List<RegistrationRequest> getRegistrations(Integer skip, Integer limit) {
        return findRegistrations(skip, limit).map(this::loadRegistration).toList();
    }

    private Stream<Path> findRegistrations(Integer skip, Integer limit) {
        try {
            return Files.list(Paths.get(registrationDirectory)) //
                    .filter(path -> path.getFileName().toString().endsWith(".json")) //
                    .filter(path -> path.getFileName().toString().contains("@")) //
                    .skip(skip == null ? 0 : skip.intValue()) //
                    .limit(limit == null ? 100 : limit.intValue());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private RegistrationRequest loadRegistration(Path path) {
        try (Reader reader = Files.newBufferedReader(path)) {
            return mapper.readValue(reader, RegistrationRequest.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    String addRegistration(RegistrationRequest registration) {
        var now = Instant.now();
        var id = registration.getEmail() + "@" + now.toEpochMilli();
        var path = Paths.get(registrationDirectory, id + ".json");
        storeRegistration(path, registration, now);
        return id;
    }

    private void storeRegistration(Path path, RegistrationRequest registration, Instant now) {
        try (Writer writer = Files.newBufferedWriter(path)) {
            mapper.writeValue(writer, registration);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
