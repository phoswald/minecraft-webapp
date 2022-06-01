package com.github.phoswald.minecraft.webapp.registration;

import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;

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

    List<Registration> findRegistrations(Integer skip, Integer limit) {
        try {
            return Files.list(Paths.get(registrationDirectory)) //
                    .filter(path -> path.getFileName().toString().endsWith(".json")) //
                    .sorted(Comparator.comparing(Path::toString)) //
                    .skip(skip == null ? 0 : skip.intValue()) //
                    .limit(limit == null ? 100 : limit.intValue()) //
                    .map(this::loadRegistration) //
                    .toList();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private Registration loadRegistration(Path path) {
        try (Reader reader = Files.newBufferedReader(path)) {
            return mapper.readValue(reader, Registration.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    String createRegistration(Registration registration) {
        registration.validate();
        var now = Instant.now();
        var id = registration.getEmail() + "@" + now.toEpochMilli();
        registration.setId(id);
        registration.setTimestamp(now.atOffset(ZoneOffset.UTC).toString());
        storeRegistration(registration);
        return id;
    }

    private void storeRegistration(Registration registration) {
        var path = Paths.get(registrationDirectory, registration.getId() + ".json");
        try (Writer writer = Files.newBufferedWriter(path)) {
            mapper.writeValue(writer, registration);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    void deleteRegistration(String id) {
        try {
            var path = Paths.get(registrationDirectory, id + ".json");
            var pathNew = Paths.get(registrationDirectory, id + ".json.old");
            Files.move(path, pathNew);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
