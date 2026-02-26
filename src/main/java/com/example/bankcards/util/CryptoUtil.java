package com.example.bankcards.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class CryptoUtil {

    private static final String ALGORITHM = "AES";
    private static final String SECRET = "1234567890123456";

    private static SecretKeySpec getKey() {
        return new SecretKeySpec(SECRET.getBytes(), ALGORITHM);
    }

    public static String encrypt(String value) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, getKey());
            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed");
        }
    }

    public static String decrypt(String encrypted) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, getKey());
            byte[] decoded = Base64.getDecoder().decode(encrypted);
            return new String(cipher.doFinal(decoded));
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed");
        }
    }
}
