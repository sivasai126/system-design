package com.urlshortener.model;

public class UrlEntry {
    private String shortCode;
    private String originalUrl;
    private long clicks;
    private String createdAt;

    public UrlEntry(String shortCode, String originalUrl, long clicks, String createdAt) {
        this.shortCode = shortCode;
        this.originalUrl = originalUrl;
        this.clicks = clicks;
        this.createdAt = createdAt;
    }

    public String getShortCode() { return shortCode; }
    public String getOriginalUrl() { return originalUrl; }
    public long getClicks() { return clicks; }
    public String getCreatedAt() { return createdAt; }
}
