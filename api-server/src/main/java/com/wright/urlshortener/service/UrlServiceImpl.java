package com.wright.urlshortener.service;

import com.wright.urlshortener.base62.Base62Encoder;
import com.wright.urlshortener.dao.cassandra.model.UrlRecord;
import com.wright.urlshortener.dao.cassandra.repository.UrlRepository;
import com.wright.urlshortener.idgenerator.UniqueIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UrlServiceImpl implements UrlService {

    private final UniqueIdGenerator uniqueIdGenerator;

    @Autowired
    private UrlRepository urlRepository;

    public UrlServiceImpl() {
        // TODO: change to a system env var
        // TODO: should this not be set in the CTOR?  Should the ID generator class be static?
        this.uniqueIdGenerator = new UniqueIdGenerator(0);
    }

    @Override
    public String createShortUrl(String longUrl) {
        UrlRecord urlRecord = urlRepository.findByLongUrl(longUrl);

        if (urlRecord != null) {
            return urlRecord.getShortUrl();
        }

        long nextId = this.uniqueIdGenerator.nextId();
        String shortUrl = Base62Encoder.encode(nextId);
        urlRepository.save(new UrlRecord(nextId, shortUrl, longUrl));

        return shortUrl;
    }

    @Override
    public String getLongUrl(String shortUrl) {
        UrlRecord urlRecord = urlRepository.findByShortUrl(shortUrl);

        if (urlRecord != null && urlRecord.getLongUrl() != null) {
            return urlRecord.getLongUrl();
        }

        return null;
    }
}
