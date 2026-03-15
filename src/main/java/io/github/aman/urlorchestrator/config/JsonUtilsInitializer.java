package io.github.aman.urlorchestrator.config;

import io.github.aman.urlorchestrator.utility.JsonUtils;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class JsonUtilsInitializer {

    public JsonUtilsInitializer(ObjectMapper objectMapper) {
        JsonUtils.init(objectMapper);
    }
}
