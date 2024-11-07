package com.iteso.steganography;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class StegoManager {
    private BufferedImage image;
    private String message;
    private Format format;
    private StegoAlgorithmFactory stegoAlgorithm;

    public StegoManager(String pathname, String message) {
        try {
            this.message = message;
            this.image = loadImage(pathname);
            this.format = determineFormat(pathname);
            this.stegoAlgorithm = AlgorithmSelector.selectAlgorithm(format);
            this.stegoAlgorithm.initialize(image, format);
        } catch (IOException | UnsupportedAlgorithmException e) {
            System.err.println("Error al inicializar StegoManager: " + e.getMessage());
        }
    }

    public StegoManager(String pathname) {
        this(pathname, null);
    }

    private BufferedImage loadImage(String pathname) throws IOException {
        BufferedImage image = ImageIO.read(new File(pathname));
        if (image == null) {
            throw new IOException("Error al cargar la imagen. Formato no compatible o archivo corrupto.");
        }
        return image;
    }

    private Format determineFormat(String pathname) {
        if (pathname.endsWith(".png")) {
            return Format.PNG;
        } else if (pathname.endsWith(".jpg") || pathname.endsWith(".jpeg")) {
            return Format.JPG;
        } else {
            throw new IllegalArgumentException("Formato de imagen no soportado.");
        }
    }

    public BufferedImage getImage() {
        return image;
    }

    public Format getFormat() {
        return format;
    }

    public void saveImage(String outputname) {
        try {
            ImageIO.write(this.image, this.format.name().toLowerCase(), new File(outputname));
        } catch (IOException e) {
            System.err.println("Error al guardar la imagen: " + e.getMessage());
        }
    }

    public void hideMessage() {
        if (this.message == null || this.message.isEmpty()) {
            throw new IllegalArgumentException("No se ha proporcionado un mensaje para ocultar.");
        }
        try {
            if (stegoAlgorithm != null) {
                stegoAlgorithm.hideMessage(this.image, this.message);
            } else {
                throw new UnsupportedAlgorithmException("Algoritmo de esteganografía no válido.");
            }
        } catch (UnsupportedAlgorithmException e) {
            System.err.println("Error ocultando el mensaje: " + e.getMessage());
        }
    }

    public String extractMessage() {
        try {
            if (stegoAlgorithm != null) {
                return stegoAlgorithm.extractMessage(this.image);
            } else {
                throw new UnsupportedAlgorithmException("Algoritmo de esteganografía no válido.");
            }
        } catch (UnsupportedAlgorithmException e) {
            System.err.println("Error extrayendo el mensaje: " + e.getMessage());
            return null;
        }
    }

}
