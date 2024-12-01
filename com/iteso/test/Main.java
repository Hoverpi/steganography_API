package com.iteso.test;

import com.iteso.steganography.StegoManager;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String pathname = "../steganography_API/nat.png";

        StegoManager stegoManager = new StegoManager(pathname, "la naturaleza es muy bonita");

        stegoManager.hideMessage();
        stegoManager.saveImage("img.png");


    }
}


