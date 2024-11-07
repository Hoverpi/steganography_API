package com.iteso.test;

import com.iteso.steganography.StegoManager;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        StegoManager stegoManager = new StegoManager("nature.png", "la naturaleza es muy bonita");

        stegoManager.hideMessage();
        stegoManager.saveImage("imagen_oculta.png");

        String extractedMessage = stegoManager.extractMessage();
        System.out.println("Mensaje extraído: " + extractedMessage);
    }
}


