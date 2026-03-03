package com.example.bankcards.service;

import com.example.bankcards.dto.CardResponseDto;
import com.example.bankcards.dto.TransferRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface CardUserService {
    Page<CardResponseDto> getMyCards(String username, Pageable pageable);
    void requestBlock(Long cardId, String username);
    BigDecimal getBalance(Long cardId, String username);
    void transfer(String username, TransferRequestDto request);
}
