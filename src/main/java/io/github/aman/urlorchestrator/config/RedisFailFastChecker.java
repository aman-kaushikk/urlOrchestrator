package io.github.aman.urlorchestrator.config;




import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisFailFastChecker {

    private final StringRedisTemplate redis;

    @PostConstruct
    public void checkRedisConnection() {
        try {
            assert redis.getConnectionFactory() != null;
            redis.getConnectionFactory()
                    .getConnection()
                    .ping();
            log.info("Redis connection verified");
        } catch (Exception ex) {
            log.error("Redis is unavailable. Failing fast.: {}",ex.getMessage(), ex);
            throw new IllegalStateException("Redis is required but not available", ex);
        }
    }
}

/*
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }
}*/
