package com.iteso.steganography;

import java.awt.image.BufferedImage;

public class LSBAlgorithm extends StegoAlgorithmFactory {

    private static final int HEADER_SIZE = 32;

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

    private String wordsToBits(String message) {
        byte[] byteMessage = message.getBytes();
        StringBuilder messageBits = new StringBuilder();
        for (byte b : byteMessage) {
            messageBits.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
        }
        return messageBits.toString();
    }

    private String intToBits(int value) {
        return String.format("%32s", Integer.toBinaryString(value)).replace(' ', '0');
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

    private int bitsToInt(String bits) {
        return Integer.parseInt(bits, 2);
    }

    private String bitsToWords(String messageBits) {
        StringBuilder message = new StringBuilder();
        for (int i = 0; i < messageBits.length(); i += 8) {
            int charCode = Integer.parseInt(messageBits.substring(i, i + 8), 2);
            message.append((char) charCode);
        }
        return message.toString();
    }



    @Override
    protected boolean isCompatible(String format) {
        return format.equals("png") || format.equals("bmp");
    }
}
