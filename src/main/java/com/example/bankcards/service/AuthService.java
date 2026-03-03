package com.example.bankcards.service;

import com.example.bankcards.dto.LoginRequestDto;
import com.example.bankcards.dto.RegisterRequestDto;

public interface AuthService {

    String register(RegisterRequestDto request);

    String login(LoginRequestDto request);
}