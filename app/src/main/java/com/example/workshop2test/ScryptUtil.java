package com.example.workshop2test;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import android.util.Base64;
import java.util.Arrays;

public class ScryptUtil {

    public static String hashPassword(final String password) {
        try {
            byte[] salt = ScryptUtil.getSalt();
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return Base64.encodeToString(hash, Base64.DEFAULT);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean checkPassword(final String inputPassword, final String storedHash) {
        try {
            byte[] storedHashBytes = Base64.decode(storedHash, Base64.DEFAULT);
            KeySpec spec = new PBEKeySpec(inputPassword.toCharArray(), storedHashBytes, 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] inputHash = factory.generateSecret(spec).getEncoded();
            return Arrays.equals(storedHashBytes, inputHash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static byte[] getSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }
}