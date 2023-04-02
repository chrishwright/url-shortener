package com.wright.urlshortener.dao.cassandra.repository;

import com.wright.urlshortener.dao.cassandra.model.UrlRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UrlRepositoryTestIT {
    @Autowired
    private UrlRepository urlRepository;

    @Test
    public void testCassandraPersist() {
        UrlRecord urlRecord = new UrlRecord(0, "0000000", "https://www.test.com");
        UrlRecord persistedRecord = urlRepository.save(urlRecord);
        assertEquals(urlRecord, persistedRecord);
        urlRepository.delete(persistedRecord);
    }

    @Test
    public void testCassandraGet() {
        UrlRecord urlRecord = new UrlRecord(0, "0000000", "https://www.test.com");
        UrlRecord persistedRecord = urlRepository.save(urlRecord);

        UrlRecord urlRecord2 = new UrlRecord(1, "0000001", "https://www.test-2.com");
        UrlRecord persistedRecord2 = urlRepository.save(urlRecord2);

        UrlRecord foundLongUrlRecord = urlRepository.findByLongUrl("https://www.test.com");
        UrlRecord foundShortUrlRecord = urlRepository.findByShortUrl("0000000");

        UrlRecord foundLongUrlRecord2 = urlRepository.findByLongUrl("https://www.test-2.com");
        UrlRecord foundShortUrlRecord2 = urlRepository.findByShortUrl("0000001");

        assertEquals(urlRecord, persistedRecord);
        assertEquals(urlRecord2, persistedRecord2);
        assertEquals(foundShortUrlRecord.getShortUrl(), "0000000");
        assertEquals(foundShortUrlRecord2.getShortUrl(), "0000001");
        assertEquals(foundLongUrlRecord.getLongUrl(), "https://www.test.com");
        assertEquals(foundLongUrlRecord2.getLongUrl(), "https://www.test-2.com");
        assertEquals(urlRepository.count(), 2);

        urlRepository.delete(urlRecord);
        urlRepository.delete(urlRecord2);

        assertEquals(urlRepository.count(), 0);
    }
}
