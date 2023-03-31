package com.wright.urlshortener.service;

import com.datastax.oss.driver.api.core.CqlSession;
import com.wright.urlshortener.base62.Base62Encoder;
import com.wright.urlshortener.dao.cassandra.model.UrlRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.query.Criteria;
import org.springframework.data.cassandra.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class UrlServiceImpl implements UrlService {

    // TODO: create range from ticket server
    private long counter = 0;

    @Autowired
    CqlSession cqlSession;

    @Override
    public String createShortUrl(String longUrl) {
        String shortUrl = Base62Encoder.encode(counter);

        CassandraOperations template = new CassandraTemplate(cqlSession);

        template.insert(new UrlRecord(counter++, shortUrl, longUrl));

        // TODO: should urlRecord be returned here?
        // 1. Check DB if exists
        // 2. If exists, return shortUrl
        // 3. If not exists, get next number
        // 4. Post to DB
        return shortUrl;
    }

    @Override
    public String getLongUrl(String shortUrl) {
        CassandraOperations template = new CassandraTemplate(cqlSession);

        UrlRecord urlRecord = template.selectOne(Query.query(Criteria.where("shortUrl").is(shortUrl)),
                UrlRecord.class);

        // TODO: should urlRecord be returned here?
        if (urlRecord != null && urlRecord.getLongUrl() != null) {
            return urlRecord.getLongUrl();
        }
        return null;
    }
}
