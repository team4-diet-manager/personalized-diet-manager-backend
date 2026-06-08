package com.pdm.dietmanager.dto.response;

import java.util.List;
import org.springframework.http.HttpStatus;

public class ErrorResponse {
    private final int status;
    private final String error;
    private final String message;
    private final List<FieldErrorResponse> fieldErrors;

    private ErrorResponse(
            int status,
            String error,
            String message,
            List<FieldErrorResponse> fieldErrors
    ) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.fieldErrors = fieldErrors;
    }

    public static ErrorResponse of(HttpStatus status, String message) {
        return new ErrorResponse(status.value(), status.name(), message, List.of());
    }

    public static ErrorResponse of(
            HttpStatus status,
            String message,
            List<FieldErrorResponse> fieldErrors
    ) {
        return new ErrorResponse(status.value(), status.name(), message, fieldErrors);
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public List<FieldErrorResponse> getFieldErrors() {
        return fieldErrors;
    }

    public static class FieldErrorResponse {
        private final String field;
        private final String message;

        public FieldErrorResponse(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public String getMessage() {
            return message;
        }
    }
}
