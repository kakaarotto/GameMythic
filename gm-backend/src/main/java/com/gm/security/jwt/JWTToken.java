package com.gm.security.jwt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JWTToken {

    private Integer code = 1;

    private String message = "Login successful";

    @JsonProperty("id_token")
    private String idToken;

    public JWTToken(String idToken) {
        this.idToken = idToken;
    }

    public JWTToken(String idToken, String message) {
        this.idToken = idToken;
        this.message = message;
    }

}