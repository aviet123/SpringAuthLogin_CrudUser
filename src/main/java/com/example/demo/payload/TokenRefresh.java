package com.example.demo.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenRefresh {
    private String refreshToken;
    private final String type = "Bearer";
    private String username;
    private String password;
}
