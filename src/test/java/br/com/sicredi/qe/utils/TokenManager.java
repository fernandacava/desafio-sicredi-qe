package br.com.sicredi.qe.utils;

import br.com.sicredi.qe.clients.AuthClient;
import br.com.sicredi.qe.models.LoginRequest;
import br.com.sicredi.qe.models.LoginResponse;

public final class TokenManager {

    private static String cachedAccessToken;

    private TokenManager() {
    }

    public static String getValidAccessToken() {
        if (cachedAccessToken == null) {
            cachedAccessToken = new AuthClient()
                    .login(new LoginRequest(
                            br.com.sicredi.qe.config.ConfigManager.get("valid.username"),
                            br.com.sicredi.qe.config.ConfigManager.get("valid.password")))
                    .extract()
                    .as(LoginResponse.class)
                    .getAccessToken();
        }
        return cachedAccessToken;
    }

    public static String getShortLivedAccessToken() {
        return new AuthClient()
                .login(new LoginRequest(
                        br.com.sicredi.qe.config.ConfigManager.get("valid.username"),
                        br.com.sicredi.qe.config.ConfigManager.get("valid.password"),
                        1))
                .extract()
                .as(LoginResponse.class)
                .getAccessToken();
    }

    public static void clearCache() {
        cachedAccessToken = null;
    }

    public static final String INVALID_TOKEN = "invalid.token.value";
}
