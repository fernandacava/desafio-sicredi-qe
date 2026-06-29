package br.com.sicredi.qe.tests;

import br.com.sicredi.qe.base.BaseTest;
import br.com.sicredi.qe.factory.TestDataFactory;
import br.com.sicredi.qe.models.LoginResponse;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;

@Epic("DummyJSON API")
@Feature("Authentication")
class LoginTest extends BaseTest {

    @Test
    @DisplayName("POST /auth/login com credenciais válidas deve retornar token")
    @Description("Valida autenticação bem-sucedida e presença de accessToken")
    @Severity(SeverityLevel.BLOCKER)
    void shouldLoginWithValidCredentials() {
        var response = authClient.login(TestDataFactory.validLoginRequest())
                .then()
                .statusCode(200)
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .extract()
                .as(LoginResponse.class);

        assertThat(response.getAccessToken()).isNotBlank();
        assertThat(response.getRefreshToken()).isNotBlank();
        assertThat(response.getUsername()).isEqualTo(TestDataFactory.validLoginRequest().getUsername());
        assertThat(response.getId()).isPositive();
    }

    @Test
    @DisplayName("POST /auth/login com senha inválida deve retornar 400")
    @Severity(SeverityLevel.CRITICAL)
    void shouldRejectInvalidPassword() {
        authClient.login(TestDataFactory.loginWithInvalidPassword())
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("POST /auth/login com usuário inexistente deve retornar 400")
    @Severity(SeverityLevel.CRITICAL)
    void shouldRejectNonExistentUser() {
        authClient.login(TestDataFactory.loginWithInvalidUsername())
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("POST /auth/login com body vazio deve retornar 400")
    @Severity(SeverityLevel.CRITICAL)
    void shouldRejectEmptyBody() {
        authClient.loginRaw("{}")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("POST /auth/login sem username deve retornar 400")
    @Severity(SeverityLevel.NORMAL)
    void shouldRejectMissingUsername() {
        authClient.login(TestDataFactory.loginWithUsernameOnly())
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("POST /auth/login sem password deve retornar 400")
    @Severity(SeverityLevel.NORMAL)
    void shouldRejectMissingPassword() {
        authClient.login(TestDataFactory.loginWithPasswordOnly())
                .then()
                .statusCode(400);
    }
}
