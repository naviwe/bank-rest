package com.example.bankcards.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDto(

        @NotBlank
        @Size(min = 4, max = 30)
        String username,

        @NotBlank
        @Size(min = 8)
        String password
) {}