-- Verify that a stateful UDF cannot be used outside of the SELECT list

drop temporary function row_sequence;

add jar ${system:build.dir}/hive-contrib-${system:hive.version}.jar;

create temporary function row_sequence as 
'org.apache.hadoop.hive.contrib.udf.UDFRowSequence';

select key
from (select key from src order by key) x
where row_sequence() < 5
order by key;
