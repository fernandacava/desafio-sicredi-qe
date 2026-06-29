package br.com.sicredi.qe.factory;

import br.com.sicredi.qe.config.ConfigManager;
import br.com.sicredi.qe.models.LoginRequest;
import br.com.sicredi.qe.models.Product;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class TestDataFactory {

    private TestDataFactory() {
    }

    public static LoginRequest validLoginRequest() {
        return new LoginRequest(
                ConfigManager.get("valid.username"),
                ConfigManager.get("valid.password"));
    }

    public static LoginRequest loginWithInvalidPassword() {
        return new LoginRequest(
                ConfigManager.get("valid.username"),
                ConfigManager.get("invalid.password"));
    }

    public static LoginRequest loginWithInvalidUsername() {
        return new LoginRequest(
                ConfigManager.get("invalid.username"),
                ConfigManager.get("valid.password"));
    }

    public static LoginRequest loginWithUsernameOnly() {
        return new LoginRequest(ConfigManager.get("valid.username"), null);
    }

    public static LoginRequest loginWithPasswordOnly() {
        return new LoginRequest(null, ConfigManager.get("valid.password"));
    }

    public static Product validElectronicProduct() {
        Product product = new Product();
        product.setTitle("Sicredi QA Smartphone " + UUID.randomUUID().toString().substring(0, 8));
        product.setDescription("Smartphone eletrônico para testes de API");
        product.setCategory("smartphones");
        product.setPrice(1999.99);
        product.setBrand("Sicredi Tech");
        product.setStock(50);
        return product;
    }

    public static Product productWithNegativePrice() {
        Product product = validElectronicProduct();
        product.setPrice(-150.00);
        return product;
    }

    public static Map<String, Object> invalidProductPayload() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("title", 12345);
        payload.put("price", "not-a-number");
        return payload;
    }

    public static Map<String, Object> emptyProductPayload() {
        return new HashMap<>();
    }

    public static int existingProductId() {
        return ConfigManager.getInt("existing.product.id");
    }

    public static int nonExistingProductId() {
        return ConfigManager.getInt("non.existing.product.id");
    }

    public static String invalidProductId() {
        return ConfigManager.get("invalid.product.id");
    }
}
