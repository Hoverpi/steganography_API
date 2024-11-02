package com.iteso.test;

import com.iteso.steganography.StegoAlgorithmFactory;
import com.iteso.steganography.FileHandler;
import com.iteso.steganography.Format;
import com.iteso.steganography.LSBAlgorithm;
import com.iteso.steganography.DCTAlgorithm;

public class Main {
    public static void main(String[] args) {
        FileHandler fileHandler = FileHandler.createInstance("nature.png");

        if (fileHandler != null) {
            String message = "la naturaleza es muy bonita";
            fileHandler.hideMessage(message);
            fileHandler.saveImage("imagen_oculta.png");

            String extractedMessage = fileHandler.extractMessage();
            System.out.println("Mensaje extraído: " + extractedMessage);
        } else {
            System.err.println("No se pudo crear el manejador de archivos.");
        }
    }
}


