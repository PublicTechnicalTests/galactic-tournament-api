package com.technicaltests.mv.galactictournamentapi.exception;

/**
 * Exception thrown when a species with the given ID is not found.
 *
 * @author Backend Team
 * @version 1.0
 * @since 2026
 */
public class SpecieNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new SpecieNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public SpecieNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new SpecieNotFoundException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause
     */
    public SpecieNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

