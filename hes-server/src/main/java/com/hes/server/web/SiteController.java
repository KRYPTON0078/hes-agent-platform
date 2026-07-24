package com.hes.server.web;

import com.hes.server.domain.site.SiteEntity;
import com.hes.server.domain.site.SiteRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ops/sites")
@Tag(name = "Sites")
public class SiteController {
    private final SiteRepository siteRepository;

    public SiteController(SiteRepository siteRepository) {
        this.siteRepository = siteRepository;
    }

    @GetMapping
    public List<SiteEntity> list() {
        return siteRepository.findAll();
    }

    @PostMapping
    public SiteEntity create(@RequestBody Map<String, String> body) {
        String code = require(body.get("siteCode"), "siteCode");
        String name = require(body.get("name"), "name");
        return siteRepository.findBySiteCode(code).orElseGet(() -> {
            SiteEntity site = new SiteEntity();
            site.setSiteCode(code);
            site.setName(name);
            site.setTimezone(body.getOrDefault("timezone", "UTC"));
            return siteRepository.save(site);
        });
    }

    private static String require(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " is required");
        }
        return value;
    }
}
