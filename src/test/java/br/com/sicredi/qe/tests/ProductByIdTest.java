package br.com.sicredi.qe.tests;

import br.com.sicredi.qe.base.BaseTest;
import br.com.sicredi.qe.factory.TestDataFactory;
import br.com.sicredi.qe.models.Product;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Epic("DummyJSON API")
@Feature("Products by ID")
class ProductByIdTest extends BaseTest {

    @Test
    @DisplayName("GET /products/{id} deve retornar produto existente")
    @Severity(SeverityLevel.CRITICAL)
    void shouldReturnExistingProduct() {
        int productId = TestDataFactory.existingProductId();

        var product = productClient.getProductById(productId)
                .then()
                .statusCode(200)
                .body("id", equalTo(productId))
                .body("title", notNullValue())
                .body("price", notNullValue())
                .extract()
                .as(Product.class);

        assertThat(product.getId()).isEqualTo(productId);
        assertThat(product.getTitle()).isNotBlank();
        assertThat(product.getCategory()).isNotBlank();
    }

    @Test
    @DisplayName("GET /products/{id} com produto inexistente deve retornar 404")
    @Severity(SeverityLevel.CRITICAL)
    void shouldReturnNotFoundForNonExistingProduct() {
        productClient.getProductById(TestDataFactory.nonExistingProductId())
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("GET /products/{id} com id inválido deve retornar 404")
    @Description("A API retorna 404 para identificadores não numéricos")
    @Severity(SeverityLevel.NORMAL)
    void shouldReturnNotFoundForInvalidProductId() {
        productClient.getProductById(TestDataFactory.invalidProductId())
                .then()
                .statusCode(404);
    }
}
