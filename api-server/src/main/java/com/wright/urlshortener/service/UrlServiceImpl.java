package com.wright.urlshortener.service;

import com.wright.urlshortener.base62.Base62Encoder;
import com.wright.urlshortener.dao.cassandra.model.UrlRecord;
import com.wright.urlshortener.dao.cassandra.repository.UrlRepository;
import com.wright.urlshortener.idgenerator.TimeIncreasingUniqueIdGenerator;
import com.wright.urlshortener.idgenerator.UniqueIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UrlServiceImpl implements UrlService {

    public static final short DESIRED_URL_LENGTH = 9;

    private final UniqueIdGenerator uniqueIdGenerator;

    @Autowired
    private UrlRepository urlRepository;

    public UrlServiceImpl() {
        final int workerId = Integer.parseInt(System.getenv("WORKER_ID"));
        this.uniqueIdGenerator = new TimeIncreasingUniqueIdGenerator(workerId);
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
