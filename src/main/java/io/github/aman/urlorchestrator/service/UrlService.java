package io.github.aman.urlorchestrator.service;

import io.github.aman.urlorchestrator.persistence.UrlMappingPersistence;
import io.github.aman.urlorchestrator.utility.Base62;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UrlService {


    private final UrlMappingPersistence presistence;
    private final SnowflakeIdGenerator idGenerator;
    private final RedisTemplate<String,String> redisTemplate;

    @Transactional
    public String generatedShortenCode(String longUrl) {
        log.info("Generating snowflake id for url: {}", longUrl);
        final var snowflakeId = idGenerator.nextId();
        final var shortenCode = Base62.encode(snowflakeId);
        log.info("Generated Snowflake id: {}", snowflakeId);
        log.info("Generated shorten code: {}", shortenCode);
        var savedUrlMapping = presistence.save(shortenCode, longUrl);
        // Cache the mapping in Redis
        redisTemplate.opsForValue().set("url:"+shortenCode, longUrl);
        log.info("Cached in Redis - Code: {}, URL: {}", shortenCode, longUrl);

        return savedUrlMapping.getShortCode();
    }

    public String resolvedUrl(String code) {
        log.info("code for url shorten: {}", code);
        return "https://www.google.com";
    }
}