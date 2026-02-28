package com.example.bankcards.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class CardNumberGenerator {

    private static final SecureRandom random = new SecureRandom();
    private static final int CARD_LENGTH = 16;

    public String generate() {
        StringBuilder sb = new StringBuilder(CARD_LENGTH);

        for (int i = 0; i < CARD_LENGTH - 1; i++) {
            sb.append(random.nextInt(10));
        }

        int checkDigit = calculateLuhnCheckDigit(sb.toString());
        sb.append(checkDigit);

        return sb.toString();
    }

    private static int calculateLuhnCheckDigit(String number) {
        int sum = 0;
        boolean alternate = true;
        for (int i = number.length() - 1; i >= 0; i--) {
            int n = Character.getNumericValue(number.charAt(i));
            if (alternate) {
                n *= 2;
                if (n > 9) n -= 9;
            }
            sum += n;
            alternate = !alternate;
        }
        return (10 - (sum % 10)) % 10;
    }
}