package com.roadmap.url_shortner.repository;

import com.roadmap.url_shortner.model.UrlMapping;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UrlMappingRepositoryTest {

    @Autowired
    private UrlMappingRepository repository;

    @Test
    void saveAndFindById() {
        UrlMapping mapping = new UrlMapping();
        mapping.setShortCode("abc123");
        mapping.setUrl("https://example.com");
        mapping.setCreatedAt(LocalDateTime.now());
        mapping.setUpdatedAt(LocalDateTime.now());

        UrlMapping savedMapping = repository.save(mapping);

        Optional<UrlMapping> foundMapping = repository.findById("abc123");

        assertTrue(foundMapping.isPresent());
        assertEquals(savedMapping.getShortCode(), foundMapping.get().getShortCode());
        assertEquals(savedMapping.getUrl(), foundMapping.get().getUrl());
    }

    @Test
    void deleteById() {
        UrlMapping mapping = new UrlMapping();
        mapping.setShortCode("abc123");
        mapping.setUrl("https://example.com");
        mapping.setCreatedAt(LocalDateTime.now());
        mapping.setUpdatedAt(LocalDateTime.now());

        repository.save(mapping);

        repository.deleteById("abc123");

        Optional<UrlMapping> deletedMapping = repository.findById("abc123");

        assertFalse(deletedMapping.isPresent());
    }
}