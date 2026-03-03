package com.example.bankcards.service;

import com.example.bankcards.dto.LoginRequestDto;
import com.example.bankcards.dto.RegisterRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = Mockito.mock(AuthService.class);
    }

    @Test
    void register_shouldReturnToken() {
        RegisterRequestDto request = new RegisterRequestDto("test", "pass");

        when(authService.register(request)).thenReturn("token123");

        String token = authService.register(request);
        assertEquals("token123", token);
        verify(authService, times(1)).register(request);
    }

    @Test
    void login_shouldReturnToken() {
        LoginRequestDto request = new LoginRequestDto("test", "pass");

        when(authService.login(request)).thenReturn("token456");

        String token = authService.login(request);
        assertEquals("token456", token);
        verify(authService, times(1)).login(request);
    }
}