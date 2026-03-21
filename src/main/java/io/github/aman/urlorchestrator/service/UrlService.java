package io.github.aman.urlorchestrator.service;

import io.github.aman.urlorchestrator.api.advice.LinkNotFoundException;
import io.github.aman.urlorchestrator.kafka.RedirectEventProducer;
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
//    private final RedisTemplate<String,String> redisTemplate;
    private final StringRedisTemplate redisTemplate;
    private final RedirectEventProducer producer;

    @Transactional
    public String generatedShortenCode(String longUrl) {
        log.debug("Generating snowflake id for url: {}", longUrl);
        final var snowflakeId = idGenerator.nextId();
        final var shortenCode = Base62.encode(snowflakeId);
        log.debug("Generated shorten code: {}", shortenCode);
        var savedUrlMapping = presistence.save(shortenCode, longUrl);
        // Cache the mapping in Redis
        redisTemplate.opsForValue().set("url:"+shortenCode, longUrl);
        log.debug("Cached in Redis - Code: {}, URL: {}", shortenCode, longUrl);

        return savedUrlMapping.getShortCode();
    }

    public String resolvedUrl(String code) {
        // First, try to get the long URL from Redis cache
        final var cachedUrl = redisTemplate.opsForValue().get("url:" + code);
        if (cachedUrl != null) {
            log.debug("cachedUrl original url for shorten code {} : {}", code, cachedUrl);
            producer.send(code, cachedUrl);
            return cachedUrl;
        }
        final var mapping = presistence.findByShortCode(code)
                .orElseThrow(() -> new LinkNotFoundException(code));
        String longUrl = mapping.getLongUrl();
        // Cache the resolved URL in Redis for future requests
        redisTemplate.opsForValue().set("url:" + code, longUrl);
        log.debug("Cache miss for code(Original): {}. Resolved URL(sortenCode): {}", code, longUrl);
        producer.send(code,longUrl);
        return longUrl;

    }
}