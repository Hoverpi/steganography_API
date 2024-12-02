package com.iteso.steganography;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * The StegoManager class handles the process of hiding and extracting messages
 * in image files using steganography techniques. It manages image loading,
 * message encryption, and applies the selected algorithm.
 */

public class StegoManager {
    private BufferedImage image;
    private String message;
    private String format;
    private StegoAlgorithmFactory stegoAlgorithm;
    private String encryptionKey;

    /**
     * Constructor to initialize the StegoManager with an image, message, and encryption key.
     * @param pathname Path to the image file.
     * @param message The message to hide in the image.
     * @param encryptionKey The key used to encrypt/decrypt the message.
     */

    public StegoManager(String pathname, String message, String encryptionKey) {
        try {
            System.out.println("Inicializando StegoManager...");
            this.encryptionKey = encryptionKey;
            if (message != null) {
                this.message = EncryptionUtils.encrypt(message, this.encryptionKey); // Encrypt message if provided
            } else {
                this.message = null; // If no message, set to null
            }
            this.image = loadImage(pathname);
            if (this.image == null) {
                throw new IOException("La imagen no se cargó correctamente.");
            }
            this.determineFormat(pathname);
            if (this.format == null) {
                throw new IllegalStateException("El formato no fue asignado correctamente.");
            }
            this.stegoAlgorithm = AlgorithmSelector.selectAlgorithm(format);
            this.stegoAlgorithm.initialize(image, format);
        } catch (IOException e) {
            System.err.println("Error al inicializar StegoManager: " + e.getMessage());
        } catch (UnsupportedAlgorithmException e) {
            System.err.println("Error al seleccionar algoritmo: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Constructor to initialize the StegoManager with just an image and encryption key.
     * @param pathname Path to the image file.
     * @param encryptionKey The key used to encrypt/decrypt messages.
     */

    public StegoManager(String pathname, String encryptionKey) {
        this(pathname, null, encryptionKey);
    }

    /**
     * Loads an image from the specified path.
     * @param pathname Path to the image file.
     * @return The loaded BufferedImage.
     * @throws IOException If the image could not be loaded.
     */

    private BufferedImage loadImage(String pathname) throws IOException {
        File file = new File(pathname);
        if (!file.exists() || !file.isFile()) {
            System.err.println("El archivo no existe o no es válido: " + pathname);
            return null;
        }
        BufferedImage image = ImageIO.read(file);
        if (image == null) {
            System.err.println("Error al cargar la imagen. Formato: " + getFileExtension(file) + " no soportado o archivo corrupto.");
            throw new IOException("Error al cargar la imagen. Formato no compatible o archivo corrupto.");
        }
        return image;
    }

    public static String getFileExtension(File file) {
        String name = file.getName();
        int dotIndex = name.lastIndexOf('.');
        return (dotIndex > 0) ? name.substring(dotIndex + 1) : "";
    }

    /**
     * Determines the format of the image based on the file extension.
     * This method sets the format of the image to the appropriate type (e.g., "png", "bmp").
     *
     * @param pathname The file path to the image.
     */

    private void determineFormat(String pathname) {
        System.out.println("Determinando formato para el archivo: " + pathname);
        if (pathname.endsWith(".png")) {
            this.format = "png";
            System.out.println("Formato PNG detectado.");
        } else if (pathname.endsWith(".bmp")) {
            this.format = "bmp";
            System.out.println("Formato BMP detectado.");
        } else {
            System.err.println("Formato no soportado.");
            throw new IllegalArgumentException("Formato de imagen no soportado.");
        }
        System.out.println("Formato final asignado: " + this.format);
    }

    public BufferedImage getImage() {
        return image;
    }

    public String getFormat() {
        return format;
    }

    public void saveImage(String outputname) {
        if (this.getFormat().isEmpty()) {
            System.err.println("Error: El formato de imagen no está definido.");
            return;
        }
        try {
            ImageIO.write(this.image, getFormat().toLowerCase(), new File(outputname));
        } catch (IOException e) {
            System.err.println("Error al guardar la imagen: " + e.getMessage());
        }
    }

    /**
     * Hides the message in the image using the selected steganography algorithm.
     * If a message has been provided, it is hidden in the image using the algorithm.
     */

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

    /**
     * Extracts the hidden message from the image using the selected steganography algorithm.
     *
     * @return The extracted message, or null if no message is hidden.
     */

    public String extractMessage() {
        try {
            if (stegoAlgorithm != null) {
                String encryptedMessage = stegoAlgorithm.extractMessage(this.image);
                if (encryptedMessage != null && !encryptedMessage.isEmpty()) {
                    return EncryptionUtils.decrypt(encryptedMessage, this.encryptionKey); // Desencriptar al extraer
                } else {
                    return null; // Si no hay mensaje, retornar null
                }
            } else {
                throw new UnsupportedAlgorithmException("Algoritmo de esteganografía no válido.");
            }
        } catch (UnsupportedAlgorithmException e) {
            System.err.println("Error extrayendo el mensaje: " + e.getMessage());
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
