package com.iteso.steganography;

import java.awt.image.BufferedImage;

public class Steganography {
    private final Algorithm algorithm;
    private final BufferedImage image;

    public Steganography(Algorithm algorithm, BufferedImage image) {
        this.algorithm = algorithm;
        this.image = image;
    }

    public BufferedImage hideMessage(String message) {
        return algorithm.hideMessage(image, message);
    }
    public String extractMessage() {
        return algorithm.extractMessage(image);
    }
}
