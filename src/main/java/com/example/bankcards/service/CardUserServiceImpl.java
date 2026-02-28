package com.example.bankcards.service;

import com.example.bankcards.dto.TransferRequestDto;
import com.example.bankcards.dto.mappers.CardMapper;
import com.example.bankcards.entity.*;
import com.example.bankcards.exception.AccessDeniedException;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.exception.InvalidRequestException;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.TransactionRepository;
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

@Service
@RequiredArgsConstructor
@Transactional
public class CardUserServiceImpl implements CardUserService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final CardMapper cardMapper;
    private final CryptoUtil cryptoUtil;

    @Override
    @Transactional(readOnly = true)
    public Page<CardResponseDto> getMyCards(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return cardRepository.findByOwner(user, pageable)
                .map(card -> {
                    updateExpiredStatus(card);

                    CardResponseDto dto = cardMapper.toDto(card);

                    String decryptedNumber = cryptoUtil.decrypt(card.getEncryptedNumber());
                    dto.setMaskedNumber(maskNumber(decryptedNumber));

                    return dto;
                });
    }

    @Override
    public void requestBlock(Long cardId, String username) {

        Card card = getCardOrThrow(cardId);

        if (!card.getOwner().getUsername().equals(username)) {
            throw new AccessDeniedException("Access denied");
        }

        card.setStatus(CardStatus.BLOCKED);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getBalance(Long cardId, String username) {

        Card card = getCardOrThrow(cardId);

        if (!card.getOwner().getUsername().equals(username)) {
            throw new AccessDeniedException("Access denied");
        }

        updateExpiredStatus(card);

        return card.getBalance();
    }

    @Override
    @Transactional
    public void transfer(String username, TransferRequestDto request) {

        if (request.getFromCardId().equals(request.getToCardId())) {
            throw new RuntimeException("Cannot transfer to the same card");
        }

        Card fromCard = cardRepository.findWithLockById(request.getFromCardId())
                .orElseThrow(() -> new CardNotFoundException("From card not found"));

        Card toCard = cardRepository.findWithLockById(request.getToCardId())
                .orElseThrow(() -> new CardNotFoundException("To card not found"));

        if (!fromCard.getOwner().getUsername().equals(username) ||
                !toCard.getOwner().getUsername().equals(username)) {
            throw new AccessDeniedException("Access denied");
        }

        updateExpiredStatus(fromCard);
        updateExpiredStatus(toCard);

        if (fromCard.getStatus() != CardStatus.ACTIVE ||
                toCard.getStatus() != CardStatus.ACTIVE) {
            throw new InvalidRequestException("Both cards must be ACTIVE");
        }

        if (fromCard.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InvalidRequestException("Insufficient funds");
        }

        fromCard.setBalance(fromCard.getBalance().subtract(request.getAmount()));
        toCard.setBalance(toCard.getBalance().add(request.getAmount()));

        Transaction transaction = Transaction.builder()
                .fromCard(fromCard)
                .toCard(toCard)
                .amount(request.getAmount())
                .build();

        transactionRepository.save(transaction);
    }

    private Card getCardOrThrow(Long cardId) {
        return cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Card not found"));
    }

    private void updateExpiredStatus(Card card) {
        if (card.getExpirationDate().isBefore(LocalDate.now())
                && card.getStatus() != CardStatus.EXPIRED) {
            card.setStatus(CardStatus.EXPIRED);
        }
    }

    private String maskNumber(String rawNumber) {
        if (rawNumber == null || rawNumber.length() < 4) return "****";
        return "**** **** **** " + rawNumber.substring(rawNumber.length() - 4);
    }
}