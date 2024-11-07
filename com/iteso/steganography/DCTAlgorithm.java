package com.iteso.steganography;

import java.awt.image.BufferedImage;

public class DCTAlgorithm extends StegoAlgorithmFactory {

    @Override
    public void hideMessage(BufferedImage image, String message) {
        if (super.format != Format.JPG) {
            throw new UnsupportedOperationException("DCT solo se puede usar con imágenes JPG.");
        }

        System.out.println("Mensaje oculto (simulado): " + message);
    }

    @Override
    public String extractMessage(BufferedImage image) {
        if (super.format != Format.JPG) {
            throw new UnsupportedOperationException("DCT solo se puede usar con imágenes JPG.");
        }

        return "Mensaje extraído (simulado)";
    }

    @Override
    protected boolean isCompatible(Format format) {
        return format == Format.JPG;
    }
}
