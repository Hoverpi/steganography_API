package com.iteso.steganography;

import java.awt.image.BufferedImage;

/**
 * Abstract factory class for steganography algorithms.
 * It initializes the image and format for the algorithm to hide or extract messages.
 */

public abstract class StegoAlgorithmFactory {
    protected BufferedImage image;
    protected String format;

    /**
     * Initializes the algorithm with an image and format.
     *
     * @param image  The image in which the message will be hidden or extracted.
     * @param format The format of the image (e.g., png, bmp).
     * @throws UnsupportedAlgorithmException If the format is not supported by the algorithm.
     */

    public void initialize(BufferedImage image, String format) throws UnsupportedAlgorithmException {
        this.image = image;
        this.format = format;
        if (!isCompatible(format)) {
            throw new UnsupportedAlgorithmException("El formato " + format + " no es compatible con el algoritmo " + this.getClass().getSimpleName());
        }
    }

    /**
     * Hides the message in the given image.
     *
     * @param image  The image in which the message will be hidden.
     * @param message The message to be hidden in the image.
     * @throws UnsupportedAlgorithmException If the format is not supported.
     */

    public abstract void hideMessage(BufferedImage image, String message) throws UnsupportedAlgorithmException;

    /**
     * Extracts the hidden message from the given image.
     *
     * @param image The image from which the message will be extracted.
     * @return The extracted message.
     * @throws UnsupportedAlgorithmException If the format is not supported.
     */

    public abstract String extractMessage(BufferedImage image) throws UnsupportedAlgorithmException;

    /**
     * Checks if the algorithm is compatible with the given format.
     *
     * @param format The format of the image.
     * @return True if the algorithm supports the format, false otherwise.
     */

    protected abstract boolean isCompatible(String format);
}
