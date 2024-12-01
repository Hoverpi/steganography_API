package com.iteso.steganography;

import java.awt.image.BufferedImage;
import java.util.Objects;

public class LSBAlgorithm extends StegoAlgorithmFactory {

    @Override
    public void hideMessage(BufferedImage image, String message) {


        String messageBits = wordsToBits(message);

        int indexBit = 0;
        int messageLength = messageBits.length();
        int width = super.image.getWidth();
        int height = super.image.getHeight();

        int maxCapacity = width * height * 3;
        if (messageLength > maxCapacity) {
            throw new IllegalArgumentException("El mensaje es demasiado largo para esta imagen.");
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = super.image.getRGB(x, y);
                rgb = insertBitsInColor(rgb, messageBits, indexBit);
                indexBit += 3;

                super.image.setRGB(x, y, rgb);

                if (indexBit >= messageLength) {
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
        StringBuilder messageBits = new StringBuilder();
        int width = image.getWidth();
        int height = image.getHeight();

        // Iterar sobre cada píxel para extraer los bits LSB
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);

                // Extraer los LSB de los componentes rojo, verde y azul
                int redLSB = (rgb >> 16) & 1;
                int greenLSB = (rgb >> 8) & 1;
                int blueLSB = rgb & 1;

                // Agregar los bits al StringBuilder
                messageBits.append(redLSB).append(greenLSB).append(blueLSB);

                // Verificar si se ha encontrado el delimitador (byte nulo)
                if (messageBits.length() >= 8) {
                    String lastByte = messageBits.substring(messageBits.length() - 8);
                    if (lastByte.equals("00000000")) {
                        return bitsToWords(messageBits.toString());
                    }
                }
            }
        }

        throw new IllegalStateException("No se encontró un mensaje oculto en la imagen.");
    }

    private String bitsToWords(String messageBits) {
        StringBuilder message = new StringBuilder();

        // Procesar los bits en bloques de 8 (un byte)
        for (int i = 0; i < messageBits.length(); i += 8) {
            if (i + 8 > messageBits.length()) {
                break;
            }

            String byteString = messageBits.substring(i, i + 8);

            // Si encontramos el delimitador (byte nulo), terminamos
            if (byteString.equals("00000000")) {
                break;
            }

            int charCode = Integer.parseInt(byteString, 2);
            message.append((char) charCode);
        }

        return message.toString();
    }

    @Override
    protected boolean isCompatible(Format format) {
        return format.equals(Format.PNG) || format.equals(Format.BMP);
    }
}
