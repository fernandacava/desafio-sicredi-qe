package br.com.sicredi.qe.tests;

import br.com.sicredi.qe.base.BaseTest;
import br.com.sicredi.qe.config.ConfigManager;
import br.com.sicredi.qe.models.UsersResponse;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;

@Epic("DummyJSON API")
@Feature("Users")
class UsersTest extends BaseTest {

    @Test
    @DisplayName("GET /users deve retornar lista com campos obrigatórios preenchidos")
    @Description("Valida contrato da listagem de usuários e regras de negócio básicas")
    @Severity(SeverityLevel.CRITICAL)
    void shouldReturnUsersListWithRequiredFields() {
        var response = userClient.getUsers()
                .then()
                .statusCode(200)
                .body("users", notNullValue())
                .body("total", greaterThan(0))
                .extract()
                .as(UsersResponse.class);

        assertThat(response.getUsers()).isNotEmpty();
        assertThat(response.getTotal()).isGreaterThan(0);
        assertThat(response.getLimit()).isGreaterThan(0);
        assertThat(response.getSkip()).isGreaterThanOrEqualTo(0);

        response.getUsers().forEach(user -> {
            assertThat(user.getId()).isPositive();
            assertThat(user.getUsername()).isNotBlank();
            assertThat(user.getPassword()).isNotBlank();
        });
    }

    @Test
    @DisplayName("GET /users com limit e skip deve respeitar paginação")
    @Severity(SeverityLevel.NORMAL)
    void shouldRespectPaginationParameters() {
        int limit = 5;
        int skip = 10;

        var response = userClient.getUsers(limit, skip)
                .then()
                .statusCode(200)
                .extract()
                .as(UsersResponse.class);

        assertThat(response.getUsers()).hasSizeLessThanOrEqualTo(limit);
        assertThat(response.getLimit()).isEqualTo(limit);
        assertThat(response.getSkip()).isEqualTo(skip);
        assertThat(response.getTotal()).isGreaterThan(ConfigManager.getInt("default.limit"));
    }
}
