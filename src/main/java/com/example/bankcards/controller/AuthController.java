package com.example.bankcards.controller;

import com.example.bankcards.dto.LoginRequestDto;
import com.example.bankcards.dto.RegisterRequestDto;
import com.example.bankcards.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public Map<String, String> register(@Valid @RequestBody RegisterRequestDto request) {
        return Map.of(
                "token", authService.register(request)
        );
    }

    @PostMapping("/login")
    public Map<String, String> login(@Valid @RequestBody LoginRequestDto request) {
        return Map.of(
                "token", authService.login(request)
        );
    }
}