package com.urlshortener.controller;

import com.urlshortener.model.UrlEntry;
import com.urlshortener.service.UrlService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class UrlController {

    private final UrlService urlService;

    @Value("${app.base-url}")
    private String baseUrl;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/shorten")
    public Map<String, String> shorten(@RequestBody Map<String, String> body) {
        String code = urlService.shorten(body.get("url"));
        return Map.of("shortUrl", baseUrl + "/" + code, "code", code);
    }

    @GetMapping("/{code}")
    public ResponseEntity<Void> redirect(@PathVariable String code) {
        return urlService.resolve(code)
                .map(url -> ResponseEntity.status(HttpStatus.FOUND).location(URI.create(url)).<Void>build())
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/stats/{code}")
    public ResponseEntity<UrlEntry> stats(@PathVariable String code) {
        return urlService.stats(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
