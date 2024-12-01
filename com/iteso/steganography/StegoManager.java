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
            System.out.println("Inicializando StegoManager...");
            this.message = message;
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
        }
    }

    public StegoManager(String pathname) {
        this(pathname, null);
    }

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

    private static String getFileExtension(File file) {
        String name = file.getName();
        int dotIndex = name.lastIndexOf('.');
        return (dotIndex > 0) ? name.substring(dotIndex + 1) : "";
    }


    private void determineFormat(String pathname) {
        System.out.println("Determinando formato para el archivo: " + pathname);
        if (pathname.endsWith(".png")) {
            this.format = Format.PNG;
            System.out.println("Formato PNG detectado.");
        } else if (pathname.endsWith(".bmp")) {
            this.format = Format.BMP;
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

    public Format getFormat() {
        return format;
    }


    public void saveImage(String outputname) {
        if (this.format == null) {
            System.err.println("Error: El formato de imagen no está definido.");
            return;  // Evita intentar guardar si el formato es null
        }
        try {
            System.out.println("Guardando imagen: " + outputname + " con el format " + this.format.getExtension());

            ImageIO.write(this.image, this.format.getExtension(), new File(outputname));
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
