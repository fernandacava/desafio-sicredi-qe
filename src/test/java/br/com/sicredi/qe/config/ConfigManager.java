package br.com.sicredi.qe.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigManager {

    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream input = ConfigManager.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IllegalStateException("Arquivo config.properties não encontrado em src/test/resources");
            }
            PROPERTIES.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Falha ao carregar config.properties", e);
        }
    }

    private ConfigManager() {
    }

    public static String get(String key) {
        String value = PROPERTIES.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Propriedade não configurada: " + key);
        }
        return value.trim();
    }

    public static String getBaseUri() {
        return get("base.uri");
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }
}
