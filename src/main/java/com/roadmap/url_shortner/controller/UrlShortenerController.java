package com.roadmap.url_shortner.controller;

import com.roadmap.url_shortner.model.UrlMapping;
import com.roadmap.url_shortner.service.UrlShortenerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UrlShortenerController {
    @Autowired
    private UrlShortenerService service;

    @PostMapping("/shorten")
    public ResponseEntity<?> createShortUrl(@Valid @RequestBody UrlMapping urlMapping, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            UrlMapping savedMapping = service.createShortUrl(urlMapping.getUrl());
            Map<String, Object> response = new HashMap<>();
            response.put("id", savedMapping.getShortCode());
            response.put("url", savedMapping.getUrl());
            response.put("shortCode", savedMapping.getShortCode());
            response.put("createdAt", savedMapping.getCreatedAt());
            response.put("updatedAt", savedMapping.getUpdatedAt());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error creating short URL: " + e.getMessage());
        }
    }

    @GetMapping("/shorten/{shortCode}")
    public ResponseEntity<?> getOriginalUrl(@PathVariable String shortCode) {
        Optional<UrlMapping> urlMapping = service.getOriginalUrl(shortCode);
        if (urlMapping.isPresent()) {
            UrlMapping mapping = urlMapping.get();
            Map<String, Object> response = new HashMap<>();
            response.put("id", mapping.getShortCode());
            response.put("url", mapping.getUrl());
            response.put("shortCode", mapping.getShortCode());
            response.put("createdAt", mapping.getCreatedAt());
            response.put("updatedAt", mapping.getUpdatedAt());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<?> redirectToOriginalUrl(@PathVariable String shortCode) {
        Optional<UrlMapping> urlMapping = service.getOriginalUrl(shortCode);
        if (urlMapping.isPresent()) {
            UrlMapping mapping = urlMapping.get();
            return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", mapping.getUrl())
                .build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/shorten/{shortCode}")
    public ResponseEntity<?> updateShortUrl(@PathVariable String shortCode, @Valid @RequestBody UrlMapping urlMapping, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        Optional<UrlMapping> updatedMapping = service.updateShortUrl(shortCode, urlMapping.getUrl());
        if (updatedMapping.isPresent()) {
            UrlMapping mapping = updatedMapping.get();
            Map<String, Object> response = new HashMap<>();
            response.put("id", mapping.getShortCode());
            response.put("url", mapping.getUrl());
            response.put("shortCode", mapping.getShortCode());
            response.put("createdAt", mapping.getCreatedAt());
            response.put("updatedAt", mapping.getUpdatedAt());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/shorten/{shortCode}")
    public ResponseEntity<Void> deleteShortUrl(@PathVariable String shortCode) {
        boolean deleted = service.deleteShortUrl(shortCode);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/shorten/{shortCode}/stats")
    public ResponseEntity<?> getUrlStats(@PathVariable String shortCode) {
        Optional<UrlMapping> urlMapping = service.getUrlStats(shortCode);
        if (urlMapping.isPresent()) {
            UrlMapping mapping = urlMapping.get();
            Map<String, Object> response = new HashMap<>();
            response.put("id", mapping.getShortCode());
            response.put("url", mapping.getUrl());
            response.put("shortCode", mapping.getShortCode());
            response.put("createdAt", mapping.getCreatedAt());
            response.put("updatedAt", mapping.getUpdatedAt());
            response.put("accessCount", mapping.getAccessCount());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}