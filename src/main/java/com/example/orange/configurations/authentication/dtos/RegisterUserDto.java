package com.example.orange.configurations.authentication.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserDto {
    private String email;
    private String userName;
    private String password;

    private String fullName;
}