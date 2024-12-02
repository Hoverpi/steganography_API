package com.iteso.steganography;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.security.MessageDigest;

/**
 * Utility class for performing encryption and decryption using AES algorithm.
 * This class provides methods to encrypt and decrypt messages using a secret key.
 */

public class EncryptionUtils {

    /**
     * Encrypts a message using AES encryption and returns the result as a Base64 encoded string.
     * @param message The message to be encrypted.
     * @param key The secret key used for encryption.
     * @return The encrypted message as a Base64 encoded string.
     * @throws Exception If encryption fails.
     */

        public static String encrypt(String message, String key) throws Exception {
        SecretKey secretKey = getKeyFromString(key);  // Get the AES key
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(message.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);  // Return encrypted message in Base64
    }

    /**
     * Decrypts a Base64 encoded encrypted message using AES encryption.
     * @param encryptedMessage The encrypted message as a Base64 encoded string.
     * @param key The secret key used for decryption.
     * @return The decrypted message.
     * @throws Exception If decryption fails.
     */

    public static String decrypt(String encryptedMessage, String key) throws Exception {
        SecretKey secretKey = getKeyFromString(key);  // Get the AES key
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedMessage);  // Decode the encrypted message
        return new String(cipher.doFinal(decodedBytes));  // Return the decrypted message
    }

    /**
     * Generates a secret AES key from a string using SHA-256 hashing.
     * @param key The input string used to generate the AES key.
     * @return The generated AES key.
     * @throws Exception If key generation fails.
     */

    private static SecretKey getKeyFromString(String key) throws Exception {
        // Use SHA-256 to generate a 32-byte hash and take the first 16 bytes for AES
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] keyBytes = digest.digest(key.getBytes("UTF-8"));
        return new SecretKeySpec(keyBytes, 0, 16, "AES");  // Use the first 16 bytes for AES
    }

    /**
     * Generates a random AES key and returns it as a Base64 encoded string.
     * @return The generated AES key as a Base64 encoded string.
     * @throws Exception If key generation fails.
     */

    public static String generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);  // Use AES-128
        SecretKey secretKey = keyGen.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());  // Generate and return the key in Base64
    }
}
