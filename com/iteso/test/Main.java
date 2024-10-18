package com.iteso.test;

import com.iteso.steganography.Steganography;
import com.iteso.steganography.Format;
import com.iteso.steganography.Algorithm;
import com.iteso.steganography.LSBAlgorithm;
import com.iteso.steganography.DCTAlgorithm;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            BufferedImage inputImage = ImageIO.read(new File("~/Downloads/tunel.jpg"));
            Steganography stego = new Steganography(new DCTAlgorithm(Format.JPG), inputImage);

            String message = "Mensaje oculto";
            BufferedImage outputImage = stego.hideMessage(message);
            File outputFile = new File("output.jpg");

            ImageIO.write(outputImage, "jpg", outputFile);
            String extractedMessage = stego.extractMessage();

            System.out.println("Mensaje extraído: " + extractedMessage);

        } catch (IOException e) {
            System.err.println("Error al procesar la imagen: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

