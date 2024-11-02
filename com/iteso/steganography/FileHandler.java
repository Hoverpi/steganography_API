package com.iteso.steganography;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FileHandler {
    private BufferedImage image;
    private Format format;
    private final StegoAlgorithmFactory stegoAlgorithm;

    public FileHandler(String pathname) throws IOException, UnsupportedAlgorithmException {
        loadImage(pathname);
        this.stegoAlgorithm = createStegoAlgorithm();
        this.stegoAlgorithm.initialize(image, format);
    }

    public static FileHandler createInstance(String pathname) {
        try {
            return new FileHandler(pathname);
        } catch (IOException e) {
            System.err.println("Error de E/S al cargar la imagen: " + e.getMessage());
        } catch (UnsupportedAlgorithmException e) {
            System.err.println("Algoritmo no soportado: " + e.getMessage());
        }
        return null;  // Devolver null en caso de error para indicar fallo de creación
    }

    private void loadImage(String pathname) throws IOException {
        image = ImageIO.read(new File(pathname));
        format = determineFormat(pathname);
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
            ImageIO.write(image, format.name().toLowerCase(), new File(outputname));
        } catch (IOException e) {
            System.err.println("Error al guardar la imagen: " + e.getMessage());
        }
    }

    public StegoAlgorithmFactory createStegoAlgorithm() throws UnsupportedAlgorithmException {
        return switch (format) {
            case PNG -> {
                if (imageValidFormat(LSBAlgorithm.class, format)) yield new LSBAlgorithm();
                else throw new UnsupportedAlgorithmException("El algoritmo LSB solo es compatible con el formato PNG");
            }
            case JPG -> {
                if (imageValidFormat(DCTAlgorithm.class, format)) yield new DCTAlgorithm();
                else throw new UnsupportedAlgorithmException("El algoritmo DCT solo es compatible con el formato JPG");
            }
            default -> throw new UnsupportedAlgorithmException("Formato no soportado");
        };
    }

    private boolean imageValidFormat(Class<? extends StegoAlgorithmFactory> algorithm, Format format) {
        return (algorithm == LSBAlgorithm.class && format == Format.PNG) ||
                (algorithm == DCTAlgorithm.class && format == Format.JPG);
    }

    public void hideMessage(String message) {
        try {
            if (stegoAlgorithm != null) {
                stegoAlgorithm.hideMessage(message);
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
                return stegoAlgorithm.extractMessage();
            } else {
                throw new UnsupportedAlgorithmException("Algoritmo de esteganografía no válido.");
            }
        } catch (UnsupportedAlgorithmException e) {
            System.err.println("Error extrayendo el mensaje: " + e.getMessage());
            return null;
        }
    }

}
