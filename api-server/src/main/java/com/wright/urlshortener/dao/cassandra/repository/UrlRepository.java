package com.wright.urlshortener.dao.cassandra.repository;

import com.wright.urlshortener.dao.cassandra.model.UrlRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRepository extends CrudRepository<UrlRecord, Long> {
    UrlRecord findByShortUrl(String shortUrl);

    UrlRecord findByLongUrl(String longUrl);
}
