package io.github.aman.urlorchestrator.service;

import io.github.aman.urlorchestrator.repo.UrlRepository;
import io.github.aman.urlorchestrator.utility.Base62;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UrlService {

//    public String shorten(String longUrl) {
//        return longUrl;
//    }
    private final UrlRepository urlRepository;
    private final SnowflakeIdGenerator idGenerator;

    public String generatedShortenCode(String longUrl) {
        log.info("Generating snowflake id for url: {}", longUrl);
        final var snowflakeId = idGenerator.nextId();
        final var shortenCode = Base62.encode(snowflakeId);
        log.info("Generated Snowflake id: {}", snowflakeId);
        log.info("Generated shorten code: {}", shortenCode);
        return shortenCode;
    }

    public String resolvedUrl(String code) {
        log.info("code for url shorten: {}", code);
        return "https://www.google.com";
    }
}