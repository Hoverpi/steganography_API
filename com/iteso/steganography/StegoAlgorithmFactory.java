package com.iteso.steganography;

import java.awt.image.BufferedImage;

public abstract class StegoAlgorithmFactory {
    protected BufferedImage image;
    protected Format format;

    public void initialize(BufferedImage image, Format format) {
        this.image = image;
        this.format = format;
    }

    public abstract void hideMessage( String message);
    public abstract String extractMessage();
}
