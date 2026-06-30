package br.com.sicredi.qe.tests;

import br.com.sicredi.qe.base.BaseTest;
import br.com.sicredi.qe.models.ProductsResponse;
import br.com.sicredi.qe.utils.TokenManager;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import java.time.Duration;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;

@Epic("DummyJSON API")
@Feature("Authenticated Products")
class AuthProductsTest extends BaseTest {

    @Test
    @DisplayName("GET /auth/products com token válido deve retornar lista de produtos")
    @Severity(SeverityLevel.BLOCKER)
    void shouldReturnProductsWithValidToken() {
        String token = TokenManager.getValidAccessToken();

        var response = authClient.getAuthProducts(token)
                .then()
                .statusCode(200)
                .body("products", notNullValue())
                .body("total", greaterThan(0))
                .extract()
                .as(ProductsResponse.class);

        assertThat(response.getProducts()).isNotEmpty();
        assertThat(response.getTotal()).isGreaterThan(0);
    }

    @Test
    @DisplayName("GET /auth/products sem Authorization deve retornar 401")
    @Severity(SeverityLevel.CRITICAL)
    void shouldRejectRequestWithoutAuthorization() {
        authClient.getAuthProducts(null)
                .then()
                .statusCode(401)
                .body("message", containsString("Access Token"));
    }

 @Test
@DisplayName("GET /auth/products com token inválido retorna 500 na API DummyJSON")
@Description("Comportamento observado: a API retorna 500 para token inválido. Em produção, o esperado seria 401 Unauthorized.")
@Severity(SeverityLevel.CRITICAL)
void shouldRejectInvalidToken() {
    authClient.getAuthProducts(TokenManager.INVALID_TOKEN)
            .then()
            .statusCode(401);
}

    @Test
    @Tag("slow")
    @DisplayName("GET /auth/products com token expirado deve retornar 401")
    @Description("Gera token com expiresInMins=1 e aguarda expiração para validar rejeição")
    @Severity(SeverityLevel.CRITICAL)
    void shouldRejectExpiredToken() {
        String shortLivedToken = TokenManager.getShortLivedAccessToken();

        Awaitility.await()
                .atMost(Duration.ofSeconds(75))
                .pollInterval(Duration.ofSeconds(5))
                .untilAsserted(() ->
                        authClient.getAuthProducts(shortLivedToken)
                                .then()
                                .statusCode(401));
    }
}
