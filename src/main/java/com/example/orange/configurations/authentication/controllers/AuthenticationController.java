package com.example.orange.configurations.authentication.controllers;

import com.example.orange.common.dto.ResponseDTO;
import com.example.orange.configurations.authentication.dtos.LoginResponse;
import com.example.orange.configurations.authentication.dtos.LoginUserDto;
import com.example.orange.configurations.authentication.dtos.RegisterUserDto;
import com.example.orange.configurations.authentication.services.AuthenticationService;
import com.example.orange.configurations.authentication.services.JwtService;
import com.example.orange.entites.User;
import com.example.orange.montoring.CustomMetrics;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final CustomMetrics customMetrics;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService, BCryptPasswordEncoder passwordEncoder, CustomMetrics customMetrics) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.passwordEncoder = passwordEncoder;
        this.customMetrics = customMetrics;
    }
    @PostMapping("/signup")
    public ResponseEntity<ResponseDTO> register(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);
        ResponseDTO response = new ResponseDTO( "User registered successfully", HttpStatus.OK, registeredUser);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken("Bearer "+ jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());
        ResponseDTO response = new ResponseDTO( "Login successful",HttpStatus.OK, loginResponse);
        customMetrics.incrementLoginCount();
        return ResponseEntity.ok(response);
    }
}