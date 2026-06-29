package br.com.sicredi.qe.clients;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserClient {

    public Response getUsers() {
        return given()
                .when()
                .get("/users");
    }

    public Response getUsers(int limit, int skip) {
        return given()
                .queryParam("limit", limit)
                .queryParam("skip", skip)
                .when()
                .get("/users");
    }
}
