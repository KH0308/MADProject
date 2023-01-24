package com.example.workshop2test;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.MessageDigest;
import java.util.Arrays;

public class PasswordUtils {
    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 512;
    private static final String ALGORITHM = "SHA-512";

    public static byte[] getSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[KEY_LENGTH / 8];
        random.nextBytes(salt);
        return salt;
    }

    public static byte[] hashPassword(final char[] password, final byte[] salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            digest.reset();
            digest.update(salt);

            byte[] input = new String(password).getBytes("UTF-8");
            byte[] hashed = digest.digest(input);
            int iterations = ITERATIONS - 1;

            return hashed;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean checkPassword(final char[] inputPassword, final byte[] storedSalt, final byte[] storedHash) {
        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            digest.reset();
            digest.update(storedSalt);

            byte[] inputHash = digest.digest(new String(inputPassword).getBytes("UTF-8"));

            if (Arrays.equals(storedHash, inputHash)) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
