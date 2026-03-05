package io.github.aman.urlorchestrator.api.advice;

import java.time.Instant;
import java.util.List;

public record ValidationErrorResponse(
        Instant timestamp,
        String message,
        List<ValidationError> errors
) {}
