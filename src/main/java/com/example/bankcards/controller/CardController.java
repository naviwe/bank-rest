package com.example.bankcards.controller;

import com.example.bankcards.dto.CardResponseDto;
import com.example.bankcards.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    // =========================================
    // 🔵 ADMIN ENDPOINTS
    // =========================================

    @PostMapping("/admin/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public CardResponseDto createCard(@PathVariable Long userId) {
        return cardService.createCard(userId);
    }

    @PatchMapping("/admin/{cardId}/block")
    @PreAuthorize("hasRole('ADMIN')")
    public void blockCard(@PathVariable Long cardId) {
        cardService.blockCard(cardId);
    }

    @PatchMapping("/admin/{cardId}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public void activateCard(@PathVariable Long cardId) {
        cardService.activateCard(cardId);
    }

    @DeleteMapping("/admin/{cardId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCard(@PathVariable Long cardId) {
        cardService.deleteCard(cardId);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<CardResponseDto> getAllCards(
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return cardService.getAllCards(pageable);
    }

    // =========================================
    // 🟢 USER ENDPOINTS
    // =========================================

    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    public Page<CardResponseDto> getMyCards(
            Authentication authentication,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return cardService.getMyCards(authentication.getName(), pageable);
    }

    @PatchMapping("/{cardId}/request-block")
    @PreAuthorize("hasRole('USER')")
    public void requestBlock(
            @PathVariable Long cardId,
            Authentication authentication
    ) {
        cardService.requestBlock(cardId, authentication.getName());
    }

    @GetMapping("/{cardId}/balance")
    @PreAuthorize("hasRole('USER')")
    public BigDecimal getBalance(
            @PathVariable Long cardId,
            Authentication authentication
    ) {
        return cardService.getBalance(cardId, authentication.getName());
    }
}