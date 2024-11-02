package com.iteso.steganography;

import java.awt.image.BufferedImage;

public class DCTAlgorithm extends StegoAlgorithmFactory {

    @Override
    public void hideMessage(String message) {
        if (super.format != Format.JPG) {
            throw new UnsupportedOperationException("DCT solo se puede usar con imágenes JPG.");
        }

        System.out.println("Mensaje oculto (simulado): " + message);
    }

    @Override
    public String extractMessage() {
        if (super.format != Format.JPG) {
            throw new UnsupportedOperationException("DCT solo se puede usar con imágenes JPG.");
        }

        return "Mensaje extraído (simulado)";
    }
}
