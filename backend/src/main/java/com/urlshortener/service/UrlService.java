package com.urlshortener.service;

import com.urlshortener.model.UrlEntry;
import com.urlshortener.model.UrlMapping;
import com.urlshortener.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Base64;
import java.util.Optional;

@Service
public class UrlService {

    private final StringRedisTemplate redis;
    private final UrlRepository repo;

    @Value("${app.url-ttl-days}")
    private long ttlDays;

    public UrlService(StringRedisTemplate redis, UrlRepository repo) {
        this.redis = redis;
        this.repo = repo;
    }

    public String shorten(String originalUrl) {
        String code = Base64.getUrlEncoder()
                .encodeToString(Long.toString(System.nanoTime()).getBytes())
                .substring(0, 6);

        // 1. Persist to Postgres (source of truth)
        repo.save(new UrlMapping(code, originalUrl));

        // 2. Cache in Redis for fast resolution
        redis.opsForValue().set("url:" + code, originalUrl, Duration.ofDays(ttlDays));
        redis.opsForValue().set("clicks:" + code, "0", Duration.ofDays(ttlDays));

        return code;
    }

    public Optional<String> resolve(String code) {
        // 1. Try Redis cache first (fast path)
        String cached = redis.opsForValue().get("url:" + code);
        if (cached != null) {
            redis.opsForValue().increment("clicks:" + code);  // atomic increment in Redis
            return Optional.of(cached);
        }

        // 2. Cache miss → fallback to Postgres, re-populate cache
        return repo.findByShortCode(code).map(mapping -> {
            redis.opsForValue().set("url:" + code, mapping.getOriginalUrl(), Duration.ofDays(ttlDays));
            redis.opsForValue().set("clicks:" + code, String.valueOf(mapping.getClicks()), Duration.ofDays(ttlDays));
            redis.opsForValue().increment("clicks:" + code);
            return mapping.getOriginalUrl();
        });
    }

    public Optional<UrlEntry> stats(String code) {
        return repo.findByShortCode(code).map(mapping -> {
            // Read click count from Redis if available (more up-to-date), else use Postgres value
            String redisClicks = redis.opsForValue().get("clicks:" + code);
            long clicks = redisClicks != null ? Long.parseLong(redisClicks) : mapping.getClicks();
            return new UrlEntry(code, mapping.getOriginalUrl(), clicks, mapping.getCreatedAt().toString());
        });
    }
}
