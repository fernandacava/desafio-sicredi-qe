package br.com.sicredi.qe.clients;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class HealthClient {

    public Response getTest() {
        return given()
                .when()
                .get("/test");
    }
}
