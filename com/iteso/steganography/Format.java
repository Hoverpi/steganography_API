package com.iteso.steganography;

public enum Format {
    PNG(".png"),
    BMP(".bmp");

    private final String extension;

    Format(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

    public static Format fromString(String format) {
        for (Format f : Format.values()) {
            if (f.getExtension().equalsIgnoreCase(format)) {
                return f;
            }
        }
        throw new IllegalArgumentException("Formato no soportado: " + format);
    }
}
