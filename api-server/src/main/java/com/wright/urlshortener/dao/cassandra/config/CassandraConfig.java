package com.wright.urlshortener.dao.cassandra.config;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.*;

import javax.annotation.Nonnull;
import java.util.List;

@Configuration
public class CassandraConfig {

    private static final String KEYSPACE_NAME = "counter";

    @Configuration
    public static class CreateKeyspaceConfiguration extends AbstractCassandraConfiguration
            implements BeanClassLoaderAware {
        @Nonnull
        @Override
        public String getKeyspaceName() {
            return KEYSPACE_NAME;
        }

        @Nonnull
        @Override
        protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
            CreateKeyspaceSpecification specification = CreateKeyspaceSpecification.createKeyspace(KEYSPACE_NAME)
                    .ifNotExists()
                    .with(KeyspaceOption.DURABLE_WRITES, true)
                    .withSimpleReplication(2);

            return List.of(specification);
        }

        @Nonnull
        @Override
        protected List<DropKeyspaceSpecification> getKeyspaceDrops() {
            return List.of(DropKeyspaceSpecification.dropKeyspace(KEYSPACE_NAME));
        }

        @Nonnull
        @Override
        public String getContactPoints() {
            return "cassandra-counter";
        }

        @Nonnull
        @Override
        public SchemaAction getSchemaAction() {
            return SchemaAction.CREATE_IF_NOT_EXISTS;
        }

        @Nonnull
        @Override
        public String[] getEntityBasePackages() {
            return new String[]{"com.wright.urlshortener.dao"};
        }
    }
}

