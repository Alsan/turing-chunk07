EXPLAIN
SELECT * FROM (INSERT OVERWRITE TABLE src1 SELECT * FROM src ) y;
