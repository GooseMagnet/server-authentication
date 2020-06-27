package com.karasik.util.helpers;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

@Slf4j
public final class HashingHelper {

    private static final String PBKDF2WithHmacSHA512 = "PBKDF2WithHmacSHA512";
    private static final int ITERATIONS = 65536;
    private static final short KEY_LENGTH = 512;

    private HashingHelper() {
    }

    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    public static String generatePBKDF2WithHmacSHA512Hash(String plaintextPassword, byte[] salt) throws InvalidKeySpecException {
        SecretKeyFactory skf = null;
        try {
            skf = SecretKeyFactory.getInstance(PBKDF2WithHmacSHA512);
        } catch (NoSuchAlgorithmException nsae) {
            log.error("Algorithm {} doesn't exist for encrypting password", PBKDF2WithHmacSHA512);
        }
        PBEKeySpec spec = new PBEKeySpec(plaintextPassword.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
        SecretKey key = skf.generateSecret(spec);
        byte[] hashBytes = key.getEncoded();
        return DatatypeConverter.printHexBinary(hashBytes);
    }
}
