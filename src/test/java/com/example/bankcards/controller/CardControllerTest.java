package com.example.bankcards.controller;

import com.example.bankcards.dto.CardResponseDto;
import com.example.bankcards.dto.TransferRequestDto;
import com.example.bankcards.service.CardUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CardControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CardUserService cardService;

    @InjectMocks
    private CardController cardController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cardController).build();
        when(authentication.getName()).thenReturn("user");
    }

    @Test
    void getBalance_shouldReturnAmount() throws Exception {
        when(cardService.getBalance(1L, "user")).thenReturn(BigDecimal.valueOf(5000));

        mockMvc.perform(get("/cards/1/balance").principal(authentication))
                .andExpect(status().isOk())
                .andExpect(content().string("5000"));
    }

    @Test
    void transfer_shouldCallService() throws Exception {
        TransferRequestDto request = new TransferRequestDto(1L, 2L, BigDecimal.valueOf(100));

        mockMvc.perform(post("/cards/transfer")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(cardService).transfer(eq("user"), any(TransferRequestDto.class));
    }
}