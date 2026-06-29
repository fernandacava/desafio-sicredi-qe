package br.com.sicredi.qe.clients;

import br.com.sicredi.qe.models.LoginRequest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class AuthClient {

    public Response login(LoginRequest request) {
        return given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/auth/login");
    }

    public Response loginRaw(String rawBody) {
        return given()
                .contentType(ContentType.JSON)
                .body(rawBody)
                .when()
                .post("/auth/login");
    }

    public Response getAuthProducts(String accessToken) {
        var request = given();
        if (accessToken != null) {
            request = request.header("Authorization", "Bearer " + accessToken);
        }
        return request
                .when()
                .get("/auth/products");
    }
}
