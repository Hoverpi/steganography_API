package com.iteso.steganography;

/**
 * This class is responsible for selecting the appropriate steganography algorithm
 * based on the image format provided.
 */

public class AlgorithmSelector {

    /**
     * Selects the algorithm based on the provided format.
     * @param format The image format (e.g., "png", "bmp").
     * @return A stego algorithm factory that corresponds to the format.
     * @throws UnsupportedAlgorithmException If the format is not supported.
     */

    public static StegoAlgorithmFactory selectAlgorithm(String format) throws UnsupportedAlgorithmException {
        StegoAlgorithmFactory stegoAlgorithm;

        switch (format) {
            case "png", "bmp" -> stegoAlgorithm = new LSBAlgorithm(); // LSB for PNG and BMP
            default -> throw new UnsupportedAlgorithmException("Formato no soportado");
        }

        if (!isCompatible(stegoAlgorithm, format)) {
        throw new UnsupportedAlgorithmException("El algoritmo " + stegoAlgorithm.getClass().getSimpleName() +
                " no es compatible con el formato " + format);
        }
        return stegoAlgorithm;
    }

    /**
     * Checks if the selected algorithm is compatible with the image format.
     * @param StegoAlgorithm The stego algorithm to be checked.
     * @param format The image format (e.g., "png", "bmp").
     * @return True if the algorithm is compatible with the format.
     */

    static boolean isCompatible(StegoAlgorithmFactory StegoAlgorithm, String format) {
        return (StegoAlgorithm instanceof LSBAlgorithm && format.equals("png")) ||
                (StegoAlgorithm instanceof LSBAlgorithm && format.equals("bmp"));
    }
}
