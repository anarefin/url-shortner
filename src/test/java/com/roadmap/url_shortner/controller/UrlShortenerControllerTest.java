package com.roadmap.url_shortner.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roadmap.url_shortner.model.UrlMapping;
import com.roadmap.url_shortner.service.UrlShortenerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UrlShortenerController.class)
class UrlShortenerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UrlShortenerService service;

    @Autowired
    private ObjectMapper objectMapper;

    private UrlMapping urlMapping;

    @BeforeEach
    void setUp() {
        urlMapping = new UrlMapping();
        urlMapping.setShortCode("abc123");
        urlMapping.setUrl("https://example.com");
        urlMapping.setCreatedAt(LocalDateTime.now());
        urlMapping.setUpdatedAt(LocalDateTime.now());
        urlMapping.setAccessCount(0);
    }

    @Test
    void createShortUrl() throws Exception {
        when(service.createShortUrl(any(String.class))).thenReturn(urlMapping);

        mockMvc.perform(post("/api/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(urlMapping)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.shortCode").value("abc123"))
                .andExpect(jsonPath("$.url").value("https://example.com"));
    }

    @Test
    void getOriginalUrl() throws Exception {
        when(service.getOriginalUrl("abc123")).thenReturn(Optional.of(urlMapping));

        mockMvc.perform(get("/api/shorten/abc123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortCode").value("abc123"))
                .andExpect(jsonPath("$.url").value("https://example.com"));
    }

    @Test
    void redirectToOriginalUrl() throws Exception {
        when(service.getOriginalUrl("abc123")).thenReturn(Optional.of(urlMapping));

        mockMvc.perform(get("/api/abc123"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "https://example.com"));
    }

    @Test
    void updateShortUrl() throws Exception {
        UrlMapping updatedMapping = new UrlMapping();
        updatedMapping.setShortCode("abc123");
        updatedMapping.setUrl("https://newexample.com");
        when(service.updateShortUrl(eq("abc123"), any(String.class))).thenReturn(Optional.of(updatedMapping));

        mockMvc.perform(put("/api/shorten/abc123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedMapping)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortCode").value("abc123"))
                .andExpect(jsonPath("$.url").value("https://newexample.com"));
    }

    @Test
    void deleteShortUrl() throws Exception {
        when(service.deleteShortUrl("abc123")).thenReturn(true);

        mockMvc.perform(delete("/api/shorten/abc123"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getUrlStats() throws Exception {
        urlMapping.setAccessCount(5);
        when(service.getUrlStats("abc123")).thenReturn(Optional.of(urlMapping));

        mockMvc.perform(get("/api/shorten/abc123/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortCode").value("abc123"))
                .andExpect(jsonPath("$.url").value("https://example.com"))
                .andExpect(jsonPath("$.accessCount").value(5));
    }
}