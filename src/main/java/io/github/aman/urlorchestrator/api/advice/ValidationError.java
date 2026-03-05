package io.github.aman.urlorchestrator.api.advice;

public record ValidationError(
        String field,
        String error
) {}
