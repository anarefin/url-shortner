package com.roadmap.url_shortner.repository;

import com.roadmap.url_shortner.model.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlMappingRepository extends JpaRepository<UrlMapping, String> {
}