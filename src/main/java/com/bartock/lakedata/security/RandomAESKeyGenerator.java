package com.bartock.lakedata.security;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class RandomAESKeyGenerator {
    private RandomAESKeyGenerator() {

    }

    public static String generate() {
        KeyGenerator keyGen;
        try {
            keyGen = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Unable to generate random aes key", e);
        }
        keyGen.init(256);
        SecretKey secretKey = keyGen.generateKey();

        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

}
