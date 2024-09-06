package com.roadmap.url_shortner.service;

import com.roadmap.url_shortner.model.UrlMapping;
import com.roadmap.url_shortner.repository.UrlMappingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UrlShortenerServiceTest {

    @Mock
    private UrlMappingRepository repository;

    @InjectMocks
    private UrlShortenerService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createShortUrl() {
        String originalUrl = "https://example.com";
        UrlMapping mapping = new UrlMapping();
        mapping.setUrl(originalUrl);
        mapping.setShortCode("abc123");

        when(repository.save(any(UrlMapping.class))).thenReturn(mapping);

        UrlMapping result = service.createShortUrl(originalUrl);

        assertNotNull(result);
        assertEquals(originalUrl, result.getUrl());
        assertNotNull(result.getShortCode());
        verify(repository, times(1)).save(any(UrlMapping.class));
    }

    @Test
    void getOriginalUrl() {
        String shortCode = "abc123";
        UrlMapping mapping = new UrlMapping();
        mapping.setUrl("https://example.com");
        mapping.setShortCode(shortCode);
        mapping.setAccessCount(0);

        when(repository.findById(shortCode)).thenReturn(Optional.of(mapping));
        when(repository.save(any(UrlMapping.class))).thenReturn(mapping);

        Optional<UrlMapping> result = service.getOriginalUrl(shortCode);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getAccessCount());
        verify(repository, times(1)).findById(shortCode);
        verify(repository, times(1)).save(any(UrlMapping.class));
    }

    @Test
    void getUrlStats() {
        String shortCode = "abc123";
        UrlMapping mapping = new UrlMapping();
        mapping.setUrl("https://example.com");
        mapping.setShortCode(shortCode);
        mapping.setAccessCount(5);

        when(repository.findById(shortCode)).thenReturn(Optional.of(mapping));

        Optional<UrlMapping> result = service.getUrlStats(shortCode);

        assertTrue(result.isPresent());
        assertEquals(5, result.get().getAccessCount());
        verify(repository, times(1)).findById(shortCode);
    }

    @Test
    void updateShortUrl() {
        String shortCode = "abc123";
        String newUrl = "https://newexample.com";
        UrlMapping mapping = new UrlMapping();
        mapping.setUrl("https://example.com");
        mapping.setShortCode(shortCode);

        when(repository.findById(shortCode)).thenReturn(Optional.of(mapping));
        when(repository.save(any(UrlMapping.class))).thenReturn(mapping);

        Optional<UrlMapping> result = service.updateShortUrl(shortCode, newUrl);

        assertTrue(result.isPresent());
        assertEquals(newUrl, result.get().getUrl());
        verify(repository, times(1)).findById(shortCode);
        verify(repository, times(1)).save(any(UrlMapping.class));
    }

    @Test
    void deleteShortUrl() {
        String shortCode = "abc123";

        when(repository.existsById(shortCode)).thenReturn(true);
        doNothing().when(repository).deleteById(shortCode);

        boolean result = service.deleteShortUrl(shortCode);

        assertTrue(result);
        verify(repository, times(1)).existsById(shortCode);
        verify(repository, times(1)).deleteById(shortCode);
    }
}