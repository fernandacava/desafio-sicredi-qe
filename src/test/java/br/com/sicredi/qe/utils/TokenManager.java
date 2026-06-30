package br.com.sicredi.qe.utils;

import br.com.sicredi.qe.clients.AuthClient;
import br.com.sicredi.qe.config.ConfigManager;
import br.com.sicredi.qe.models.LoginRequest;
import br.com.sicredi.qe.models.LoginResponse;
import io.restassured.response.Response;

public final class TokenManager {

    public static final String INVALID_TOKEN = "invalid-token";

    private TokenManager() {
    }

    public static String getValidAccessToken() {
        return loginWithCredentials(ConfigManager.get("valid.username"), ConfigManager.get("valid.password"), null);
    }

    public static String getShortLivedAccessToken() {
        return loginWithCredentials(ConfigManager.get("valid.username"), ConfigManager.get("valid.password"), 1);
    }

    private static String loginWithCredentials(String username, String password, Integer expiresInMins) {
        LoginRequest request = new LoginRequest(username, password, expiresInMins);
        Response response = new AuthClient().login(request);
        LoginResponse loginResponse = response.then().extract().as(LoginResponse.class);
        return loginResponse.getAccessToken();
    }
}