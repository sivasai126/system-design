package com.urlshortener.service;

import com.urlshortener.model.UrlEntry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

@Service
public class UrlService {

    private final StringRedisTemplate redis;

    @Value("${app.url-ttl-days}")
    private long ttlDays;

    public UrlService(StringRedisTemplate redis) {
        this.redis = redis;
    }

    public String shorten(String originalUrl) {
        String code = Base64.getUrlEncoder()
                .encodeToString(Long.toString(System.nanoTime()).getBytes())
                .substring(0, 6);

        redis.opsForValue().set("url:" + code, originalUrl, Duration.ofDays(ttlDays));
        redis.opsForValue().set("clicks:" + code, "0", Duration.ofDays(ttlDays));
        return code;
    }

    public Optional<String> resolve(String code) {
        String url = redis.opsForValue().get("url:" + code);
        if (url != null) {
            redis.opsForValue().increment("clicks:" + code);
        }
        return Optional.ofNullable(url);
    }

    public Optional<UrlEntry> stats(String code) {
        String url = redis.opsForValue().get("url:" + code);
        if (url == null) return Optional.empty();

        String clicks = redis.opsForValue().get("clicks:" + code);
        return Optional.of(new UrlEntry(code, url,
                Long.parseLong(clicks != null ? clicks : "0"),
                LocalDateTime.now().toString()));
    }
}
