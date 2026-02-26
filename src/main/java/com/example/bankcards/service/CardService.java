package com.example.bankcards.service;

import com.example.bankcards.dto.CardResponseDto;
import com.example.bankcards.dto.TransferRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface CardService {

    // ADMIN
    CardResponseDto createCard(Long userId);
    void blockCard(Long cardId);
    void activateCard(Long cardId);
    void deleteCard(Long cardId);
    Page<CardResponseDto> getAllCards(Pageable pageable);

    // USER
    Page<CardResponseDto> getMyCards(String username, Pageable pageable);
    void requestBlock(Long cardId, String username);
    BigDecimal getBalance(Long cardId, String username);
    void transfer(String username, TransferRequestDto request);
}
