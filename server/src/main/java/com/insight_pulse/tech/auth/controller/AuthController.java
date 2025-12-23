package com.insight_pulse.tech.auth.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.insight_pulse.tech.auth.dto.login.LoginRequest;
import com.insight_pulse.tech.auth.dto.login.LoginResponse;
import com.insight_pulse.tech.auth.dto.register.RegisterRequest;
import com.insight_pulse.tech.auth.dto.register.RegisterResponse;
import com.insight_pulse.tech.auth.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        RegisterResponse response = authService.register(request);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(response.id()) 
            .toUri();
        return ResponseEntity
            .created(location) 
            .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        List<ResponseCookie> cookies = authService.login(request);
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok();
        for(ResponseCookie cookie : cookies) {
            responseBuilder.header(HttpHeaders.SET_COOKIE, cookie.toString());
        }
        return responseBuilder.body(new LoginResponse("Login successfully"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@CookieValue("refresh_token") String cookie) {
        List<ResponseCookie> cookies = authService.refreshToken(cookie);
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok();
        for (ResponseCookie cookieResponse : cookies) {
            responseBuilder.header(HttpHeaders.SET_COOKIE, cookieResponse.toString());
        }
        return responseBuilder.body(new LoginResponse("Login successfully"));
    }

    @PostMapping("/logout")
    public  ResponseEntity<List<ResponseCookie>> logout(@CookieValue("refresh_token") String cookie) {
        List<ResponseCookie> cookies = authService.logout(cookie);
        ResponseEntity.HeadersBuilder<?> response = ResponseEntity.noContent();
        for (ResponseCookie c : cookies) {
            response.header(HttpHeaders.SET_COOKIE, c.toString());
        }
        return response.build();
    }
}
