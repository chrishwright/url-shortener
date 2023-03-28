package com.wright.urlshortener.service;

import org.springframework.stereotype.Service;

@Service
public class UrlServiceImpl implements UrlService {

    @Override
    public String createShortUrl(String longUrl) {
        // 1. Check DB if exists
        // 2. If exists, return shortUrl
        // 3. If not exists, get next number
        // 4. Post to DB
        return longUrl;
    }

    @Override
    public String getLongUrl(String shortUrl) {
        return shortUrl;
    }
}
