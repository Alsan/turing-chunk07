FROM src
SELECT cast('-30.33' as DOUBLE)
GROUP BY cast('-30.33' as DOUBLE)
LIMIT 1;


FROM src
SELECT '-30.33'
GROUP BY '-30.33'
LIMIT 1;
