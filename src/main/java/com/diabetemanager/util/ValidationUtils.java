package com.diabetemanager.util;

import com.diabetemanager.exception.InvalidGlucoseException;

public final class ValidationUtils {

    private ValidationUtils() {
    }

    public static void validateGlucose(double glucoseLevel) {

        final double MIN_VALID_GLUCOSE = 39.0;
        final double MAX_VALID_GLUCOSE = 399.0;

        if (Double.isNaN(glucoseLevel)
                || Double.isInfinite(glucoseLevel)
                || glucoseLevel < MIN_VALID_GLUCOSE
                || glucoseLevel > MAX_VALID_GLUCOSE) {

            throw new InvalidGlucoseException(
                    "Nieprawidłowy poziom glukozy: "
                            + glucoseLevel
                            + ". Dozwolony zakres danych: "
                            + MIN_VALID_GLUCOSE
                            + " - "
                            + MAX_VALID_GLUCOSE
                            + " mg/dL."
            );
        }
    }

    public static void validatePositive(
            double value,
            String fieldName) {

        if (Double.isNaN(value)
                || Double.isInfinite(value)
                || value <= 0) {

            throw new IllegalArgumentException(
                    fieldName +
                            " musi być większe od 0."
            );
        }
    }

    public static void validatePositive(
            int value,
            String fieldName) {

        if (value <= 0) {

            throw new IllegalArgumentException(
                    fieldName +
                            " musi być większe od 0."
            );
        }
    }

    public static void validateNotBlank(
            String value,
            String fieldName) {

        if (value == null || value.isBlank()) {

            throw new IllegalArgumentException(
                    fieldName +
                            " nie może być puste."
            );
        }
    }
}