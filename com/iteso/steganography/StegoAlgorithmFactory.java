package com.iteso.steganography;

import java.awt.image.BufferedImage;

import static com.iteso.steganography.AlgorithmSelector.isCompatible;

public abstract class StegoAlgorithmFactory {
    protected BufferedImage image;
    protected Format format;

    public void initialize(BufferedImage image, Format format) throws UnsupportedAlgorithmException {
        this.image = image;
        this.format = format;
        if (!isCompatible(format)) {
            throw new UnsupportedAlgorithmException("El formato " + format + " no es compatible con el algoritmo " + this.getClass().getSimpleName());
        }
    }

    public abstract void hideMessage(BufferedImage image, String message) throws UnsupportedAlgorithmException;
    public abstract String extractMessage(BufferedImage image) throws UnsupportedAlgorithmException;

    protected abstract boolean isCompatible(Format format);
}
