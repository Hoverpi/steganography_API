package com.iteso.steganography;

public enum Format {
    PNG("png"),
    JPG("jpg");

    private final String FormatName;

    Format(String FormatName) {
        this.FormatName = FormatName;
    }

    public String getFormatName() {
        return FormatName;
    }
}
