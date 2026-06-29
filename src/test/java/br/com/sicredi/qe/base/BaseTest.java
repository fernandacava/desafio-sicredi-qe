package br.com.sicredi.qe.base;

import br.com.sicredi.qe.clients.AuthClient;
import br.com.sicredi.qe.clients.HealthClient;
import br.com.sicredi.qe.clients.ProductClient;
import br.com.sicredi.qe.clients.UserClient;
import br.com.sicredi.qe.config.ConfigManager;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public abstract class BaseTest {

    protected HealthClient healthClient;
    protected AuthClient authClient;
    protected UserClient userClient;
    protected ProductClient productClient;

    @BeforeAll
    static void configureRestAssured() {
        RestAssured.baseURI = ConfigManager.getBaseUri();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @BeforeEach
    void setUpClients() {
        RestAssured.requestSpecification = defaultRequestSpec();
        healthClient = new HealthClient();
        authClient = new AuthClient();
        userClient = new UserClient();
        productClient = new ProductClient();
    }

    protected RequestSpecification defaultRequestSpec() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();
    }
}
