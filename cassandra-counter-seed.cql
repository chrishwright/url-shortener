CREATE KEYSPACE url_counter_keyspace WITH REPLICATION = { 'class': 'SimpleStrategy', 'replication_factor': 2 };
USE url_counter_keyspace;
CREATE TABLE url_counter (id varchar PRIMARY KEY, count counter);
UPDATE url_counter SET count = count + 1 WHERE id = 'current_range';