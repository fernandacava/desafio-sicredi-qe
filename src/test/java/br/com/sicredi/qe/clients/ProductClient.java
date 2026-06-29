package br.com.sicredi.qe.clients;

import br.com.sicredi.qe.models.Product;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class ProductClient {

    public Response getProducts() {
        return given()
                .when()
                .get("/products");
    }

    public Response getProducts(int limit, int skip) {
        return given()
                .queryParam("limit", limit)
                .queryParam("skip", skip)
                .when()
                .get("/products");
    }

    public Response getProductById(int id) {
        return given()
                .when()
                .get("/products/{id}", id);
    }

    public Response getProductById(String id) {
        return given()
                .when()
                .get("/products/{id}", id);
    }

    public Response addProduct(Product product) {
        return given()
                .contentType(ContentType.JSON)
                .body(product)
                .when()
                .post("/products/add");
    }

    public Response addProduct(Map<String, Object> payload) {
        return given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/products/add");
    }
}
