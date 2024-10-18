package com.iteso.steganography;

import java.awt.image.BufferedImage;

public class DCTAlgorithm implements Algorithm {

    public DCTAlgorithm(Format format) {
        if (format != Format.JPG) throw new IllegalArgumentException("DCT solo soporta imágenes JPG.");
    }

    @Override
    public BufferedImage hideMessage(BufferedImage image, String message) {
        return image;
    }

    @Override
    public String extractMessage(BufferedImage image) {
        return "";
    }
}
