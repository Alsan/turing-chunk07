add jar ${system:build.dir}/hive-contrib-${system:hive.version}.jar;
set hive.metastore.force.reload.conf=true;
SHOW TABLES 'src';
set hive.metastore.ds.connection.url.hook=org.apache.hadoop.hive.contrib.metastore.hooks.TestURLHook;
SHOW TABLES 'src';
SHOW TABLES 'src';
set hive.metastore.force.reload.conf=false;
set hive.metastore.ds.connection.url.hook=;
SHOW TABLES 'src';
