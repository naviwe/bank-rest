package com.example.bankcards.controller;

import com.example.bankcards.dto.CardResponseDto;
import com.example.bankcards.dto.TransferRequestDto;
import com.example.bankcards.service.CardUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@PreAuthorize("hasRole('USER')")
@RequestMapping("/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardUserService cardService;
    @GetMapping()
    public Page<CardResponseDto> getMyCards(
            Authentication authentication,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return cardService.getMyCards(authentication.getName(), pageable);
    }

    @PatchMapping("/{cardId}/request-block")
    public void requestBlock(
            @PathVariable Long cardId,
            Authentication authentication
    ) {
        cardService.requestBlock(cardId, authentication.getName());
    }

    @GetMapping("/{cardId}/balance")
    public BigDecimal getBalance(
            @PathVariable Long cardId,
            Authentication authentication
    ) {
        return cardService.getBalance(cardId, authentication.getName());
    }

    @PostMapping("/transfer")
    public void transfer(
            @Valid @RequestBody TransferRequestDto request,
            Authentication authentication
    ) {
        cardService.transfer(authentication.getName(), request);
    }
}