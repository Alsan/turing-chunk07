CREATE TABLE add_part_test (key STRING, value STRING) PARTITIONED BY (ds STRING);
SHOW PARTITIONS add_part_test;

ALTER TABLE add_part_test ADD PARTITION (ds='2010-01-01');
SHOW PARTITIONS add_part_test;

ALTER TABLE add_part_test ADD IF NOT EXISTS PARTITION (ds='2010-01-01');
SHOW PARTITIONS add_part_test;

ALTER TABLE add_part_test ADD IF NOT EXISTS PARTITION (ds='2010-01-02');
SHOW PARTITIONS add_part_test;

ALTER TABLE add_part_test ADD IF NOT EXISTS PARTITION (ds='2010-01-01') PARTITION (ds='2010-01-02') PARTITION (ds='2010-01-03');
SHOW PARTITIONS add_part_test;

DROP TABLE add_part_test;
SHOW TABLES;

-- Test ALTER TABLE ADD PARTITION in non-default Database
CREATE DATABASE add_part_test_db;
USE add_part_test_db;
SHOW TABLES;

CREATE TABLE add_part_test (key STRING, value STRING) PARTITIONED BY (ds STRING);
SHOW PARTITIONS add_part_test;

ALTER TABLE add_part_test ADD PARTITION (ds='2010-01-01');
SHOW PARTITIONS add_part_test;

ALTER TABLE add_part_test ADD IF NOT EXISTS PARTITION (ds='2010-01-01');
SHOW PARTITIONS add_part_test;

ALTER TABLE add_part_test ADD IF NOT EXISTS PARTITION (ds='2010-01-02');
SHOW PARTITIONS add_part_test;

ALTER TABLE add_part_test ADD IF NOT EXISTS PARTITION (ds='2010-01-01') PARTITION (ds='2010-01-02') PARTITION (ds='2010-01-03');
SHOW PARTITIONS add_part_test;
