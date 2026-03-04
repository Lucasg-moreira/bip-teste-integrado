package com.example.backend.exceptions;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        int status,
        String error,
        String message,
        String path,
        LocalDateTime timestamp,
        List<FieldErrorResponse> fieldErrors
) {}

record FieldErrorResponse(
        String field,
        String message
) {}