--
-- HIVE-1823 Upgrade the database thrift interface to allow parameters key-value pairs
--
CREATE TABLE "DATABASE_PARAMS" (
  "DB_ID" BIGINT NOT NULL,
  "PARAM_KEY" VARCHAR(180) NOT NULL,
  "PARAM_VALUE" VARCHAR(4000));

ALTER TABLE "DATABASE_PARAMS" ADD CONSTRAINT "DATABASE_PARAMS_FK1"
  FOREIGN KEY ("DB_ID") REFERENCES "DBS" ("DB_ID")
  ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "DATABASE_PARAMS" ADD CONSTRAINT "DATABASE_PARAMS_PK"
  PRIMARY KEY ("DB_ID", "PARAM_KEY");
