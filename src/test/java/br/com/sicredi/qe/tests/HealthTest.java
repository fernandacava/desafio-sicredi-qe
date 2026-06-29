package br.com.sicredi.qe.tests;

import br.com.sicredi.qe.base.BaseTest;
import br.com.sicredi.qe.models.HealthResponse;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Epic("DummyJSON API")
@Feature("Health Check")
class HealthTest extends BaseTest {

    @Test
    @DisplayName("GET /test deve retornar 200 com status ok e method GET")
    @Description("Valida disponibilidade da API e contrato básico do endpoint de teste")
    @Severity(SeverityLevel.BLOCKER)
    void shouldReturnHealthyStatus() {
        var response = healthClient.getTest()
                .then()
                .statusCode(200)
                .body("status", equalTo("ok"))
                .body("method", equalTo("GET"))
                .extract()
                .as(HealthResponse.class);

        assertThat(response.getStatus()).isEqualTo("ok");
        assertThat(response.getMethod()).isEqualTo("GET");
    }
}
