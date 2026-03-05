package io.github.aman.urlorchestrator.repo;

import io.github.aman.urlorchestrator.entity.UrlMapping;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UrlRepository extends JpaRepository<@NonNull UrlMapping,@NonNull Long> {
    Optional<UrlMapping> findByShortCode(@NonNull String shortCode);
}