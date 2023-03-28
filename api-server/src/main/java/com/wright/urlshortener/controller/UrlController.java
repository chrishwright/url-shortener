package com.wright.urlshortener.controller;

import com.wright.urlshortener.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1")
public class UrlController {

    @Autowired
    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @GetMapping("/{shortUrl}")
    public String getLongUrl(@PathVariable String shortUrl) {
        return urlService.getLongUrl(shortUrl);
    }

    @PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public String createShortUrl(@RequestBody String longUrl) {
        return urlService.createShortUrl(longUrl);
    }
}
