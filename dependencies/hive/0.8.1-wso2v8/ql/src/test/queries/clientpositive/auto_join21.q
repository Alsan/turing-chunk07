set hive.auto.convert.join = true;
explain
SELECT * FROM src src1 LEFT OUTER JOIN src src2 ON (src1.key = src2.key AND src1.key < 10 AND src2.key > 10) RIGHT OUTER JOIN src src3 ON (src2.key = src3.key AND src3.key < 10) SORT BY src1.key, src1.value, src2.key, src2.value, src3.key, src3.value;

SELECT * FROM src src1 LEFT OUTER JOIN src src2 ON (src1.key = src2.key AND src1.key < 10 AND src2.key > 10) RIGHT OUTER JOIN src src3 ON (src2.key = src3.key AND src3.key < 10) SORT BY src1.key, src1.value, src2.key, src2.value, src3.key, src3.value;
