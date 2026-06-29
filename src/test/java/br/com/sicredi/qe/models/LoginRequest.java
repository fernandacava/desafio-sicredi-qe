package br.com.sicredi.qe.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginRequest {

    private String username;
    private String password;
    private Integer expiresInMins;

    public LoginRequest() {
    }

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public LoginRequest(String username, String password, Integer expiresInMins) {
        this.username = username;
        this.password = password;
        this.expiresInMins = expiresInMins;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getExpiresInMins() {
        return expiresInMins;
    }

    public void setExpiresInMins(Integer expiresInMins) {
        this.expiresInMins = expiresInMins;
    }
}
