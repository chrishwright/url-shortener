CREATE KEYSPACE url_counter_keyspace WITH REPLICATION = { 'class': 'SimpleStrategy', 'replication_factor': 2 };
USE url_counter_keyspace;
CREATE TABLE url_counter (id varchar PRIMARY KEY, count counter);
UPDATE url_counter SET count = count + 1 WHERE id = 'current_range';


CREATE KEYSPACE counter WITH REPLICATION = { 'class': 'SimpleStrategy', 'replication_factor': 1 };
USE counter;
CREATE TABLE urlrecord (id bigint PRIMARY KEY, shortUrl varchar, longUrl varchar);
CREATE INDEX shortUrlIndex ON counter.urlrecord(shortUrl);