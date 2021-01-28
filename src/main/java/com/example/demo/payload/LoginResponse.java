package com.example.demo.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    private String token;
    public final String tokenType = "Bearer";
    private String username;
    private String refreshToken;

    public LoginResponse(String jwt, String refreshToken) {
        this.token = jwt;
        this.refreshToken = refreshToken;
    }

}
