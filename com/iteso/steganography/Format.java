package com.iteso.steganography;

public enum Format {
    PNG("png"),
    JPG("JPG");

    private final String FormatName;

    Format(String FormatName) {
        this.FormatName = FormatName;
    }

    public String getFormatName() {
        return FormatName;
    }
}
