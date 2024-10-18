package com.iteso.steganography;

import java.awt.image.BufferedImage;

public interface Algorithm {
    BufferedImage hideMessage(BufferedImage image, String message);
    String extractMessage(BufferedImage image);
}
