EXPLAIN EXTENDED
SELECT s.*
FROM (SELECT a.* FROM srcbucket TABLESAMPLE (BUCKET 1 OUT OF 2 on key) a) s;

SELECT s.*
FROM (SELECT a.* FROM srcbucket TABLESAMPLE (BUCKET 1 OUT OF 2 on key) a) s;
