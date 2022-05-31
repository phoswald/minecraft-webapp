package com.github.phoswald.minecraft.webapp.registration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.startsWith;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class RegistrationResourceTest {

    @Test
    public void testEndpoint() {
        given() //
                .when() //
                .get("/app/rest/registration") //
                .then() //
                .statusCode(200) //
                .body(startsWith("["), endsWith("]"));
    }
}
