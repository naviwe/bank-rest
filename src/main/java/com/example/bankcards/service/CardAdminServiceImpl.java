package com.example.bankcards.service;

import com.example.bankcards.dto.CardResponseDto;
import com.example.bankcards.dto.mappers.CardMapper;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.CardExpiredException;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.CardNumberGenerator;
import com.example.bankcards.util.CryptoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class CardAdminServiceImpl implements CardAdminService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CardMapper cardMapper;
    private final CardNumberGenerator cardNumberGenerator;
    private final CryptoUtil cryptoUtil;

    @Override
    public CardResponseDto createCard(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String rawCardNumber = cardNumberGenerator.generate();

        String encryptedNumber = cryptoUtil.encrypt(rawCardNumber);

        Card card = Card.builder()
                .encryptedNumber(encryptedNumber)
                .owner(user)
                .expirationDate(LocalDate.now().plusYears(3))
                .status(CardStatus.ACTIVE)
                .balance(BigDecimal.ZERO)
                .build();

        cardRepository.save(card);

        CardResponseDto dto = cardMapper.toDto(card);

        dto.setMaskedNumber(maskNumber(rawCardNumber));

        return dto;
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
            throw new CardExpiredException("Card is expired");
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
                .map(card -> {

                    CardResponseDto dto = cardMapper.toDto(card);

                    String decryptedNumber = cryptoUtil.decrypt(card.getEncryptedNumber());
                    dto.setMaskedNumber(maskNumber(decryptedNumber));

                    return dto;
                });
    }

    private String maskNumber(String rawNumber) {
        if (rawNumber == null || rawNumber.length() < 4) return "****";
        return "**** **** **** " + rawNumber.substring(rawNumber.length() - 4);
    }

    private Card getCardOrThrow(Long cardId) {
        return cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Card not found"));
    }
}
