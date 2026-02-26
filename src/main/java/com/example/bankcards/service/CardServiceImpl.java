package com.example.bankcards.service;

import com.example.bankcards.entity.*;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.dto.CardResponseDto;
import com.example.bankcards.util.CryptoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    // ============================
    // 🔵 ADMIN
    // ============================

    @Override
    public CardResponseDto createCard(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String rawCardNumber = generateCardNumber();
        String encryptedNumber = CryptoUtil.encrypt(rawCardNumber);

        Card card = Card.builder()
                .encryptedNumber(encryptedNumber)
                .owner(user)
                .expirationDate(LocalDate.now().plusYears(3))
                .status(CardStatus.ACTIVE)
                .balance(BigDecimal.ZERO)
                .build();

        cardRepository.save(card);

        return mapToDto(card, rawCardNumber);
    }

    @Override
    public void blockCard(Long cardId) {
        Card card = getCardOrThrow(cardId);
        card.setStatus(CardStatus.BLOCKED);
    }

    @Override
    public void activateCard(Long cardId) {
        Card card = getCardOrThrow(cardId);

        if (card.getExpirationDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Card is expired");
        }

        card.setStatus(CardStatus.ACTIVE);
    }

    @Override
    public void deleteCard(Long cardId) {
        Card card = getCardOrThrow(cardId);
        cardRepository.delete(card);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardResponseDto> getAllCards(Pageable pageable) {
        return cardRepository.findAll(pageable)
                .map(this::mapToDtoWithoutDecrypt);
    }

    // ============================
    // 🟢 USER
    // ============================

    @Override
    @Transactional(readOnly = true)
    public Page<CardResponseDto> getMyCards(String username, Pageable pageable) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return cardRepository.findByOwner(user, pageable)
                .map(this::mapToDtoWithoutDecrypt);
    }

    @Override
    public void requestBlock(Long cardId, String username) {

        Card card = getCardOrThrow(cardId);

        if (!card.getOwner().getUsername().equals(username)) {
            throw new RuntimeException("Access denied");
        }

        card.setStatus(CardStatus.BLOCKED);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getBalance(Long cardId, String username) {

        Card card = getCardOrThrow(cardId);

        if (!card.getOwner().getUsername().equals(username)) {
            throw new RuntimeException("Access denied");
        }

        updateExpiredStatus(card);

        return card.getBalance();
    }

    // ============================
    // PRIVATE METHODS
    // ============================

    private Card getCardOrThrow(Long cardId) {
        return cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));
    }

    private String generateCardNumber() {
        Random random = new Random();
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < 16; i++) {
            builder.append(random.nextInt(10));
        }

        return builder.toString();
    }

    private void updateExpiredStatus(Card card) {
        if (card.getExpirationDate().isBefore(LocalDate.now())
                && card.getStatus() != CardStatus.EXPIRED) {
            card.setStatus(CardStatus.EXPIRED);
        }
    }

    private String mask(String fullNumber) {
        return "**** **** **** " + fullNumber.substring(12);
    }

    private CardResponseDto mapToDto(Card card, String rawNumber) {

        updateExpiredStatus(card);

        return CardResponseDto.builder()
                .id(card.getId())
                .maskedNumber(mask(rawNumber))
                .owner(card.getOwner().getUsername())
                .expirationDate(card.getExpirationDate().toString())
                .status(card.getStatus().name())
                .balance(card.getBalance().toString())
                .build();
    }

    private CardResponseDto mapToDtoWithoutDecrypt(Card card) {

        updateExpiredStatus(card);

        String decrypted = CryptoUtil.decrypt(card.getEncryptedNumber());

        return CardResponseDto.builder()
                .id(card.getId())
                .maskedNumber(mask(decrypted))
                .owner(card.getOwner().getUsername())
                .expirationDate(card.getExpirationDate().toString())
                .status(card.getStatus().name())
                .balance(card.getBalance().toString())
                .build();
    }
}