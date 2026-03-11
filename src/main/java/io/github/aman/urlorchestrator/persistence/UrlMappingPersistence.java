package io.github.aman.urlorchestrator.persistence;

import io.github.aman.urlorchestrator.entity.UrlMapping;
import io.github.aman.urlorchestrator.repo.UrlRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class UrlMappingPersistence {
    private final UrlRepository repository;
    public UrlMapping save(String shortCode, String longUrl) {
        UrlMapping mapping = new UrlMapping();
        mapping.setShortCode(shortCode);
        mapping.setLongUrl(longUrl);
        UrlMapping saved = repository.save(mapping);

        log.debug("Persisted UrlMapping [id={}, shortCode={}]", saved.getId(), saved.getShortCode());

        return saved;
    }

}
