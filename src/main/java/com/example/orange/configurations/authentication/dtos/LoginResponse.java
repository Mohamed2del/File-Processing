package com.example.orange.configurations.authentication.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {

    private String token;

    private long expiresIn;

}
