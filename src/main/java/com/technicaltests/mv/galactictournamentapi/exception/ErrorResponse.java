package com.technicaltests.mv.galactictournamentapi.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Standard error response format for API errors.
 *
 * @author Backend Team
 * @version 1.0
 * @since 2026
 */
@Getter
@AllArgsConstructor
@Schema(description = "Standard error response")
public class ErrorResponse {

    @JsonProperty("timestamp")
    @Schema(description = "Timestamp of the error occurrence")
    private LocalDateTime timestamp;

    @JsonProperty("status")
    @Schema(description = "HTTP status code")
    private int status;

    @JsonProperty("error")
    @Schema(description = "Error message")
    private String error;

    @JsonProperty("message")
    @Schema(description = "Detailed error message")
    private String message;

    @JsonProperty("path")
    @Schema(description = "Request path that caused the error")
    private String path;

    @JsonProperty("validation_errors")
    @Schema(description = "List of validation errors (if any)")
    private List<ValidationError> validationErrors;

    /**
     * Nested class for validation error details.
     */
    @Getter
    @AllArgsConstructor
    @Schema(description = "Validation error detail")
    public static class ValidationError {

        @JsonProperty("field")
        @Schema(description = "Field that failed validation")
        private String field;

        @JsonProperty("message")
        @Schema(description = "Validation error message")
        private String message;

        @JsonProperty("rejected_value")
        @Schema(description = "The value that was rejected")
        private Object rejectedValue;
    }
}
