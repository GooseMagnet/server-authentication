package com.karasik.db.security;

import com.karasik.util.helpers.HashingHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class HashingTest {

    @Test
    public void hashSamePasswordUsingSameSalt() {
        try {
            byte[] salt = HashingHelper.generateSalt();
            String badPassword = "password";
            String firstHash = HashingHelper.generatePBKDF2WithHmacSHA512Hash(badPassword, salt);
            System.out.println(firstHash);
            String secondHash = HashingHelper.generatePBKDF2WithHmacSHA512Hash(badPassword, salt);
            System.out.println(secondHash);
            Assertions.assertEquals(firstHash, secondHash);
        } catch (InvalidKeySpecException ikse) {
            throw new RuntimeException(ikse);
        }
    }

    @Test
    public void hashSamePasswordUsingDifferentSalts() {
        try {
            String badPassword = "password";

            byte[] firstSalt = HashingHelper.generateSalt();
            String firstHash = HashingHelper.generatePBKDF2WithHmacSHA512Hash(badPassword, firstSalt);

            byte[] secondSalt = HashingHelper.generateSalt();
            String secondHash = HashingHelper.generatePBKDF2WithHmacSHA512Hash(badPassword, secondSalt);

            System.out.println(firstHash);
            System.out.println(secondHash);
            Assertions.assertNotEquals(firstHash, secondHash);
        } catch (InvalidKeySpecException ikse) {
            throw new RuntimeException(ikse);
        }
    }
}
