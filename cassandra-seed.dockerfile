FROM cassandra:4.1.1

COPY ./cassandra-counter-seed.cql /cassandra-counter-seed.cql

RUN chmod u+x /cassandra-counter-seed.cql