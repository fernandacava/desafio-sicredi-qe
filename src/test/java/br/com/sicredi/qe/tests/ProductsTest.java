package br.com.sicredi.qe.tests;

import br.com.sicredi.qe.base.BaseTest;
import br.com.sicredi.qe.models.ProductsResponse;
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
@Feature("Products")
class ProductsTest extends BaseTest {

    @Test
    @DisplayName("GET /products deve retornar lista de produtos com total, skip e limit")
    @Description("Valida listagem padrão de produtos eletrônicos e metadados de paginação")
    @Severity(SeverityLevel.CRITICAL)
    void shouldReturnProductsList() {
        var response = productClient.getProducts()
                .then()
                .statusCode(200)
                .body("products", notNullValue())
                .body("total", greaterThan(0))
                .extract()
                .as(ProductsResponse.class);

        assertThat(response.getProducts()).isNotEmpty();
        assertThat(response.getTotal()).isGreaterThan(0);
        assertThat(response.getSkip()).isGreaterThanOrEqualTo(0);
        assertThat(response.getLimit()).isGreaterThan(0);

        response.getProducts().forEach(product -> {
            assertThat(product.getId()).isPositive();
            assertThat(product.getTitle()).isNotBlank();
            assertThat(product.getPrice()).isGreaterThanOrEqualTo(0);
        });
    }

    @Test
    @DisplayName("GET /products com limit e skip deve aplicar paginação corretamente")
    @Severity(SeverityLevel.NORMAL)
    void shouldApplyPagination() {
        int limit = 10;
        int skip = 5;

        var response = productClient.getProducts(limit, skip)
                .then()
                .statusCode(200)
                .extract()
                .as(ProductsResponse.class);

        assertThat(response.getProducts()).hasSizeLessThanOrEqualTo(limit);
        assertThat(response.getLimit()).isEqualTo(limit);
        assertThat(response.getSkip()).isEqualTo(skip);
        assertThat(response.getTotal()).isGreaterThan(limit);
    }
}
