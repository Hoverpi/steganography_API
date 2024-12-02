package com.iteso.steganography;

/**
 * Exception thrown when an unsupported algorithm is used.
 */

public class UnsupportedAlgorithmException extends Exception {
    public UnsupportedAlgorithmException(String message) {
        super(message);
    }
}
