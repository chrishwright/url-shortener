package com.wright.urlshortener.service;

import com.wright.urlshortener.base62.Base62Encoder;
import com.wright.urlshortener.dao.cassandra.model.UrlRecord;
import com.wright.urlshortener.dao.cassandra.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UrlServiceImpl implements UrlService {

    // TODO: create range from ticket server
    private long counter = 0;

    @Autowired
    private UrlRepository urlRepository;

    @Override
    public String createShortUrl(String longUrl) {
        UrlRecord urlRecord = urlRepository.findByLongUrl(longUrl);

        if (urlRecord != null) {
            return urlRecord.getShortUrl();
        }

        String shortUrl = Base62Encoder.encode(counter);

        urlRepository.save(new UrlRecord(counter++, shortUrl, longUrl));

        // TODO: should urlRecord be returned here?
        return shortUrl;
    }

    @Override
    public String getLongUrl(String shortUrl) {
        UrlRecord urlRecord = urlRepository.findByShortUrl(shortUrl);

        // TODO: should urlRecord be returned here?
        if (urlRecord != null && urlRecord.getLongUrl() != null) {
            return urlRecord.getLongUrl();
        }
        return null;
    }
}
