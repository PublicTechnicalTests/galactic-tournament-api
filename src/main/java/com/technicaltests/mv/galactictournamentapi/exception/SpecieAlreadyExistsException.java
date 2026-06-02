package com.technicaltests.mv.galactictournamentapi.exception;

/**
 * Exception thrown when trying to create a species with a name that already exists.
 *
 * @author Backend Team
 * @version 1.0
 * @since 2026
 */
public class SpecieAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new SpecieAlreadyExistsException with the specified detail message.
     *
     * @param message the detail message
     */
    public SpecieAlreadyExistsException(String message) {
        super(message);
    }

    /**
     * Constructs a new SpecieAlreadyExistsException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause
     */
    public SpecieAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
