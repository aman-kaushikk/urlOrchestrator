package io.github.aman.urlorchestrator.api;


import io.github.aman.urlorchestrator.dto.ShortenRequest;
import io.github.aman.urlorchestrator.dto.ShortenResponse;
import io.github.aman.urlorchestrator.service.UrlService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@SuppressWarnings("unused")
public class UrlController {

    private final UrlService service;

    public UrlController(UrlService service) {
        this.service = service;
    }

    @PostMapping("/shorten")
    public ResponseEntity<@NonNull ShortenResponse> shorten(@Valid @RequestBody ShortenRequest shortenRequest) {
        log.info("shorten url request: {}", shortenRequest);
        final var shortenResponse = new ShortenResponse(service.generatedShortenCode(shortenRequest.longUrl()));
        log.info("shorten url response: {}", shortenResponse);
        return ResponseEntity.ok(shortenResponse);
    }
}
