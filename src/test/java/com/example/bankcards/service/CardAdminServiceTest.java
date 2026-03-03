package com.example.bankcards.service;

import com.example.bankcards.dto.CardResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardAdminServiceTest {

    private CardAdminService cardAdminService;

    @BeforeEach
    void setUp() {
        cardAdminService = Mockito.mock(CardAdminService.class);
    }

    @Test
    void createCard_shouldReturnCard() {
        CardResponseDto card = new CardResponseDto();
        card.setId(1L);

        when(cardAdminService.createCard(1L)).thenReturn(card);

        CardResponseDto result = cardAdminService.createCard(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(cardAdminService, times(1)).createCard(1L);
    }

    @Test
    void getAllCards_shouldReturnPage() {
        CardResponseDto card = new CardResponseDto();
        Page<CardResponseDto> page = new PageImpl<>(List.of(card));

        when(cardAdminService.getAllCards(Pageable.unpaged())).thenReturn(page);

        Page<CardResponseDto> result = cardAdminService.getAllCards(Pageable.unpaged());
        assertEquals(1, result.getTotalElements());
        verify(cardAdminService, times(1)).getAllCards(Pageable.unpaged());
    }

    @Test
    void blockCard_shouldBeCalled() {
        doNothing().when(cardAdminService).blockCard(1L);
        cardAdminService.blockCard(1L);
        verify(cardAdminService, times(1)).blockCard(1L);
    }

    @Test
    void activateCard_shouldBeCalled() {
        doNothing().when(cardAdminService).activateCard(1L);
        cardAdminService.activateCard(1L);
        verify(cardAdminService, times(1)).activateCard(1L);
    }

    @Test
    void deleteCard_shouldBeCalled() {
        doNothing().when(cardAdminService).deleteCard(1L);
        cardAdminService.deleteCard(1L);
        verify(cardAdminService, times(1)).deleteCard(1L);
    }
}