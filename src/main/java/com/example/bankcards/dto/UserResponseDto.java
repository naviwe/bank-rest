package com.example.bankcards.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserResponseDto {

    private Long id;
    private String username;
    private String role;
    private LocalDateTime createdAt;
}