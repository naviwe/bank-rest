package com.example.bankcards.service;

import com.example.bankcards.dto.CardResponseDto;
import com.example.bankcards.dto.TransferRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardUserServiceTest {

    private CardUserService cardUserService;

    @BeforeEach
    void setUp() {
        cardUserService = Mockito.mock(CardUserService.class);
    }

    @Test
    void getMyCards_shouldReturnPage() {
        CardResponseDto card = new CardResponseDto();
        Page<CardResponseDto> page = new PageImpl<>(List.of(card));

        when(cardUserService.getMyCards("user", Pageable.unpaged())).thenReturn(page);

        Page<CardResponseDto> result = cardUserService.getMyCards("user", Pageable.unpaged());
        assertEquals(1, result.getTotalElements());
        verify(cardUserService, times(1)).getMyCards("user", Pageable.unpaged());
    }

    @Test
    void requestBlock_shouldBeCalled() {
        doNothing().when(cardUserService).requestBlock(1L, "user");
        cardUserService.requestBlock(1L, "user");
        verify(cardUserService, times(1)).requestBlock(1L, "user");
    }

    @Test
    void getBalance_shouldReturnAmount() {
        when(cardUserService.getBalance(1L, "user")).thenReturn(BigDecimal.valueOf(100));

        BigDecimal balance = cardUserService.getBalance(1L, "user");
        assertEquals(BigDecimal.valueOf(100), balance);
        verify(cardUserService, times(1)).getBalance(1L, "user");
    }

    @Test
    void transfer_shouldBeCalled() {
        TransferRequestDto request = new TransferRequestDto();
        request.setAmount(BigDecimal.valueOf(50));

        doNothing().when(cardUserService).transfer("user", request);
        cardUserService.transfer("user", request);
        verify(cardUserService, times(1)).transfer("user", request);
    }
}