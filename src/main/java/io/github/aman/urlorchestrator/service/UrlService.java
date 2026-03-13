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
        // First, try to get the long URL from Redis cache
        String cachedUrl = redisTemplate.opsForValue().get("url:" + code);
        if (cachedUrl != null) {
            log.info("Cache hit for code: {}. Resolved URL: {}", code, cachedUrl);
            return cachedUrl;
        }
        final var mapping = presistence.findByShortCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Invalid shorten code: " + code));
        String longUrl = mapping.getLongUrl();
        // Cache the resolved URL in Redis for future requests
        redisTemplate.opsForValue().set("url:" + code, longUrl);
        log.info("Cache miss for code(Original): {}. Resolved URL(sortenCode): {}", code, longUrl);
        return longUrl;

    }
}