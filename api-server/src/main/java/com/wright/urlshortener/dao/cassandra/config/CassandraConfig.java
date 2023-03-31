package com.wright.urlshortener.dao.cassandra.config;

import com.datastax.oss.driver.api.core.CqlSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CassandraConfig {
    public @Bean CqlSession session() {
        return CqlSession.builder().withLocalDatacenter("datacenter1").withKeyspace("counter").build();
    }
}

