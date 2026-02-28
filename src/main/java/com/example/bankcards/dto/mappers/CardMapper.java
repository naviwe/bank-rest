package com.example.bankcards.dto.mappers;

import com.example.bankcards.dto.CardResponseDto;
import com.example.bankcards.entity.Card;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CardMapper {

    @Mapping(target = "owner", expression = "java(card.getOwner() != null ? card.getOwner().getId().toString() : null)")
    @Mapping(target = "maskedNumber", expression = "java(mask(card.getEncryptedNumber()))")
    @Mapping(target = "expirationDate", expression = "java(card.getExpirationDate() != null ? card.getExpirationDate().toString() : null)")
    @Mapping(target = "status", expression = "java(card.getStatus() != null ? card.getStatus().name() : null)")
    @Mapping(target = "balance", expression = "java(card.getBalance() != null ? card.getBalance().toString() : null)")
    CardResponseDto toDto(Card card);

    default String mask(String encryptedNumber) {
        if (encryptedNumber == null || encryptedNumber.length() < 4) {
            return "****";
        }
        return "**** **** **** " + encryptedNumber.substring(encryptedNumber.length() - 4);
    }
}
