package com.example.bankcards.service;

import com.example.bankcards.dto.CardResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CardAdminService {
    CardResponseDto createCard(Long userId);
    void blockCard(Long cardId);
    void activateCard(Long cardId);
    void deleteCard(Long cardId);
    Page<CardResponseDto> getAllCards(Pageable pageable);
}
