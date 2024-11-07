package com.iteso.steganography;

public class AlgorithmSelector {
    public static StegoAlgorithmFactory selectAlgorithm(Format format) throws UnsupportedAlgorithmException {
        StegoAlgorithmFactory stegoAlgorithm;

        switch (format) {
            case PNG -> stegoAlgorithm = new LSBAlgorithm();
            case JPG -> stegoAlgorithm = new DCTAlgorithm();
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
                (StegoAlgorithm instanceof DCTAlgorithm && format == Format.JPG);
    }
}
