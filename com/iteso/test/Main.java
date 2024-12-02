package com.iteso.test;

import com.iteso.steganography.StegoManager;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        StegoManager stegoManager = new StegoManager("../steganography_API/nat.png", "la naturaleza es muy bonita", "my8bytekey");

        stegoManager.hideMessage();
        stegoManager.saveImage("img.png");

        StegoManager stegoManager2 = new StegoManager("../steganography_API/img.png", null, "my8bytekey");

        String extractedMessage = stegoManager2.extractMessage();
        System.out.println("Mensaje extraído: " + extractedMessage);
    }
}


