package com.example.bankcards.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardResponseDto {

    private Long id;
    private String maskedNumber;
    private String owner;
    private String expirationDate;
    private String status;
    private String balance;
}