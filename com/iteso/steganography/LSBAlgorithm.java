package com.iteso.steganography;

import java.awt.image.BufferedImage;

public class LSBAlgorithm implements Algorithm {

    public LSBAlgorithm(Format format) {
        if (format != Format.PNG) throw new IllegalArgumentException("LSB solo soporta imágenes PNG.");
    }

    @Override
    public BufferedImage hideMessage(BufferedImage image, String message) {
        String messageBits = wordsToBits(message);

        int indexBit = 0;
        int messageLength = messageBits.length();
        int width = image.getWidth();
        int height = image.getHeight();

        int maxCapacity = width * height * 3;
        if (messageLength > maxCapacity) {
            throw new IllegalArgumentException("El mensaje es demasiado largo para esta imagen.");
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                rgb = insertBitsInColor(rgb, messageBits, indexBit);
                indexBit += 3;

                image.setRGB(x, y, rgb);

                if (indexBit >= messageLength) {
                    return image;
                }
            }
        }

        return image;
    }

    private String wordsToBits(String message) {
        byte[] byteMessage = message.getBytes();
        StringBuilder messageBits = new StringBuilder();
        for (byte b : byteMessage) {
            messageBits.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
        }
        return messageBits.toString();
    }

    private int insertBitsInColor(int rgb, String messageBits, int indexBit) {
        if (indexBit < messageBits.length()) {
            int red = (rgb >> 16) & 0xFF;
            red = (red & 0xFE) | (messageBits.charAt(indexBit++) - '0');

            int green = (rgb >> 8) & 0xFF;
            if (indexBit < messageBits.length()) {
                green = (green & 0xFE) | (messageBits.charAt(indexBit++) - '0');
            }

            int blue = rgb & 0xFF;
            if (indexBit < messageBits.length()) {
                blue = (blue & 0xFE) | (messageBits.charAt(indexBit++) - '0');
            }

            return (red << 16) | (green << 8) | blue;
        }

        return rgb;
    }

    @Override
    public String extractMessage(BufferedImage image) {
        return "";
    }
}
