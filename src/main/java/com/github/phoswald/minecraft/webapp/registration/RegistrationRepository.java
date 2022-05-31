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

    List<RegistrationInfo> getRegistrations(Integer skip, Integer limit) {
        return findRegistrations(skip, limit).map(this::loadRegistration).toList();
    }

    private Stream<Path> findRegistrations(Integer skip, Integer limit) {
        try {
            return Files.list(Paths.get(registrationDirectory)) //
                    .filter(path -> path.getFileName().toString().endsWith(".json")) //
                    .sorted(Comparator.comparing(Path::toString)) // 
                    .skip(skip == null ? 0 : skip.intValue()) //
                    .limit(limit == null ? 100 : limit.intValue());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private RegistrationInfo loadRegistration(Path path) {
        try (Reader reader = Files.newBufferedReader(path)) {
            return mapper.readValue(reader, RegistrationInfo.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    String addRegistration(RegistrationRequest request) {
        var now = Instant.now();
        var id = request.getEmail() + "@" + now.toEpochMilli();
        var path = Paths.get(registrationDirectory, id + ".json");
        var registration = new RegistrationInfo();
        registration.setId(id);
        registration.setTimestamp(now.atOffset(ZoneOffset.UTC).toString());
        registration.setEmail(request.getEmail());
        registration.setName(request.getName());
        registration.setUserId(request.getUserId());
        registration.setSchool(request.getSchool());
        registration.setComment(request.getComment());
        storeRegistration(path, registration);
        return id;
    }

    private void storeRegistration(Path path, RegistrationInfo registration) {
        try (Writer writer = Files.newBufferedWriter(path)) {
            mapper.writeValue(writer, registration);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
