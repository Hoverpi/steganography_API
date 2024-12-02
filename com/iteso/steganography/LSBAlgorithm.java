package com.iteso.steganography;

import java.awt.image.BufferedImage;

/**
 * Least Significant Bit (LSB) algorithm for hiding and extracting messages from images.
 * This implementation supports formats like PNG and BMP.
 */

public class LSBAlgorithm extends StegoAlgorithmFactory {

    private static final int HEADER_SIZE = 32;

    /**
     * Hides a message in the image using the LSB algorithm.
     *
     * @param image  The image in which the message will be hidden.
     * @param message The message to hide in the image.
     * @throws IllegalArgumentException If the message is too long for the image.
     */

    @Override
    public void hideMessage(BufferedImage image, String message) {
        String messageBits = wordsToBits(message);
        String headerBits = intToBits(messageBits.length());
        String dataBits = headerBits + messageBits;

        int indexBit = 0;
        int width = super.image.getWidth();
        int height = super.image.getHeight();

        int maxCapacity = width * height * 3;
        if (dataBits.length() > maxCapacity) {
            throw new IllegalArgumentException("El mensaje es demasiado largo para esta imagen.");
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = super.image.getRGB(x, y);
                rgb = insertBitsInColor(rgb, dataBits, indexBit);
                indexBit += 3;

                super.image.setRGB(x, y, rgb);

                if (indexBit >= dataBits.length()) {
                    return;
                }
            }
        }
    }

    /**
     * Converts a string message into a binary representation.
     *
     * @param message The message to convert.
     * @return A binary string representing the message.
     */

    private String wordsToBits(String message) {
        byte[] byteMessage = message.getBytes();
        StringBuilder messageBits = new StringBuilder();
        for (byte b : byteMessage) {
            messageBits.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
        }
        return messageBits.toString();
    }

    /**
     * Converts an integer value into a 32-bit binary string.
     *
     * @param value The integer value to convert.
     * @return A 32-bit binary string representation of the value.
     */

    private String intToBits(int value) {
        return String.format("%32s", Integer.toBinaryString(value)).replace(' ', '0');
    }

    /**
     * Inserts the bits of the message into the color values of a pixel.
     *
     * @param rgb       The color value of the pixel.
     * @param messageBits The binary string of the message.
     * @param indexBit   The current bit index to insert.
     * @return The new color value with the inserted bits.
     */

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

    /**
     * Extracts the hidden message from the image.
     *
     * @param image The image from which the message will be extracted.
     * @return The extracted message.
     * @throws IllegalArgumentException If the data in the image is insufficient.
     */

    @Override
    public String extractMessage(BufferedImage image) {
        StringBuilder dataBits = new StringBuilder();
        int width = image.getWidth();
        int height = image.getHeight();

        // Recuperar bits de los píxeles
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);

                // Extraer los bits menos significativos de los colores
                dataBits.append(rgb >> 16 & 1); // Rojo
                dataBits.append(rgb >> 8 & 1);  // Verde
                dataBits.append(rgb & 1);       // Azul
            }
        }

        // Leer encabezado para obtener la longitud del mensaje
        int messageLength = bitsToInt(dataBits.substring(0, HEADER_SIZE));
        int messageEndIndex = HEADER_SIZE + messageLength;

        if (messageEndIndex > dataBits.length()) {
            throw new IllegalArgumentException("Los datos en la imagen son insuficientes para extraer el mensaje.");
        }

        // Extraer los bits del mensaje usando la longitud del encabezado
        String messageBits = dataBits.substring(HEADER_SIZE, messageEndIndex);
        return bitsToWords(messageBits);
    }

    /**
     * Converts a binary string to an integer.
     *
     * @param bits The binary string to convert.
     * @return The integer value represented by the binary string.
     */

    private int bitsToInt(String bits) {
        return Integer.parseInt(bits, 2);
    }

    /**
     * Converts a binary string of message bits into a human-readable string.
     *
     * @param messageBits The binary string of the message.
     * @return The decoded message as a string.
     */

    private String bitsToWords(String messageBits) {
        StringBuilder message = new StringBuilder();
        for (int i = 0; i < messageBits.length(); i += 8) {
            int charCode = Integer.parseInt(messageBits.substring(i, i + 8), 2);
            message.append((char) charCode);
        }
        return message.toString();
    }

    /**
     * Checks if the algorithm is compatible with the given format.
     *
     * @param format The format of the image.
     * @return True if the algorithm supports the format, false otherwise.
     */

    @Override
    protected boolean isCompatible(String format) {
        return format.equals("png") || format.equals("bmp");
    }
}
