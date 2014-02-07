add jar ${system:build.dir}/hive-contrib-${system:hive.version}.jar;

CREATE TEMPORARY FUNCTION example_arraysum    AS 'org.apache.hadoop.hive.contrib.udf.example.UDFExampleArraySum';
CREATE TEMPORARY FUNCTION example_mapconcat   AS 'org.apache.hadoop.hive.contrib.udf.example.UDFExampleMapConcat';
CREATE TEMPORARY FUNCTION example_structprint AS 'org.apache.hadoop.hive.contrib.udf.example.UDFExampleStructPrint';

EXPLAIN
SELECT example_arraysum(lint), example_mapconcat(mstringstring), example_structprint(lintstring[0])
FROM src_thrift;

SELECT example_arraysum(lint), example_mapconcat(mstringstring), example_structprint(lintstring[0])
FROM src_thrift;

DROP TEMPORARY FUNCTION example_arraysum;
DROP TEMPORARY FUNCTION example_mapconcat;
DROP TEMPORARY FUNCTION example_structprint;
