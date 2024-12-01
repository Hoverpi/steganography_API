package com.iteso.steganography;

import java.util.Objects;

public class AlgorithmSelector {
    public static StegoAlgorithmFactory selectAlgorithm(Format format) throws UnsupportedAlgorithmException {
        StegoAlgorithmFactory stegoAlgorithm;

        switch (format) {
            case Format.PNG, Format.BMP -> stegoAlgorithm = new LSBAlgorithm();
            default -> throw new UnsupportedAlgorithmException("Formato no soportado");
        }

        if (!isCompatible(stegoAlgorithm, format)) {
        throw new UnsupportedAlgorithmException("El algoritmo " + stegoAlgorithm.getClass().getSimpleName() +
                " no es compatible con el formato " + format);
        }
        return stegoAlgorithm;
    }

    static boolean isCompatible(StegoAlgorithmFactory StegoAlgorithm, Format format) {
        return (StegoAlgorithm instanceof LSBAlgorithm && format == Format.PNG) ||
                (StegoAlgorithm instanceof LSBAlgorithm && format == Format.BMP);
    }
}
