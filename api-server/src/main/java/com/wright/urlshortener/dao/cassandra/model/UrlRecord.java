package com.wright.urlshortener.dao.cassandra.model;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Objects;

@Table
public class UrlRecord {

    @PrimaryKey
    private final long id;
    private final String shortUrl;
    private final String longUrl;

    public UrlRecord(long id, String shortUrl, String longUrl) {
        this.id = id;
        this.shortUrl = shortUrl;
        this.longUrl = longUrl;
    }

    public long getId() {
        return id;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public String getLongUrl() {
        return longUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UrlRecord urlRecord = (UrlRecord) o;
        return id == urlRecord.id && shortUrl.equals(urlRecord.shortUrl) && longUrl.equals(urlRecord.longUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, shortUrl, longUrl);
    }

    @Override
    public String toString() {
        return "UrlRecord{" +
                "id=" + id +
                ", shortUrl='" + shortUrl + '\'' +
                ", longUrl='" + longUrl + '\'' +
                '}';
    }
}
