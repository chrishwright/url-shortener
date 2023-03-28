package com.wright.urlshortener.service;

public interface UrlService {
    String createShortUrl(String longUrl);
    String getLongUrl(String shortUrl);
}
