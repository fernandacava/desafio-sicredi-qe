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
import static org.hamcrest.Matchers.notNullValue;

@Epic("DummyJSON API")
@Feature("Product Creation")
class ProductCreateTest extends BaseTest {

    @Test
    @DisplayName("POST /products/add com payload válido deve criar produto simulado")
    @Severity(SeverityLevel.BLOCKER)
    void shouldCreateProductWithValidPayload() {
        Product request = TestDataFactory.validElectronicProduct();

        var created = productClient.addProduct(request)
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("title", notNullValue())
                .extract()
                .as(Product.class);

        assertThat(created.getId()).isPositive();
        assertThat(created.getTitle()).isEqualTo(request.getTitle());
        assertThat(created.getCategory()).isEqualTo(request.getCategory());
        assertThat(created.getPrice()).isEqualTo(request.getPrice());
    }

    @Test
    @DisplayName("POST /products/add com payload vazio deve simular criação com id gerado")
    @Description("A API DummyJSON simula POST e retorna novo id mesmo sem campos obrigatórios")
    @Severity(SeverityLevel.NORMAL)
    void shouldSimulateCreationWithEmptyPayload() {
        var created = productClient.addProduct(TestDataFactory.emptyProductPayload())
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .extract()
                .as(Product.class);

        assertThat(created.getId()).isPositive();
    }

    @Test
    @DisplayName("POST /products/add com preço negativo é aceito pela API (comportamento observado)")
    @Description("Documenta ausência de validação de preço negativo na API fake")
    @Severity(SeverityLevel.NORMAL)
    void shouldAcceptNegativePrice() {
        Product request = TestDataFactory.productWithNegativePrice();

        var created = productClient.addProduct(request)
                .then()
                .statusCode(201)
                .extract()
                .as(Product.class);

        assertThat(created.getPrice()).isNegative();
    }

    @Test
    @DisplayName("POST /products/add com payload inválido retorna resposta simulada")
    @Description("API fake não valida tipos de campos e retorna 200 com dados parciais")
    @Severity(SeverityLevel.NORMAL)
    void shouldHandleInvalidPayloadTypes() {
        productClient.addProduct(TestDataFactory.invalidProductPayload())
                .then()
                .statusCode(201)
                .body("id", notNullValue());
    }
}
