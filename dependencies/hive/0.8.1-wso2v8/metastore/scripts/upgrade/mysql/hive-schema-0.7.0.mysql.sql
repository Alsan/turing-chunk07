-- MySQL dump 10.13  Distrib 5.1.48, for apple-darwin10.3.0 (i386)
--
-- Host: localhost    Database: mdb
-- ------------------------------------------------------
-- Server version	5.1.48

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `BUCKETING_COLS`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS `BUCKETING_COLS` (
  `SD_ID` bigint(20) NOT NULL,
  `BUCKET_COL_NAME` varchar(256) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `INTEGER_IDX` int(11) NOT NULL,
  PRIMARY KEY (`SD_ID`,`INTEGER_IDX`),
  KEY `BUCKETING_COLS_N49` (`SD_ID`),
  CONSTRAINT `BUCKETING_COLS_FK1` FOREIGN KEY (`SD_ID`) REFERENCES `SDS` (`SD_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `COLUMNS`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS `COLUMNS` (
  `SD_ID` bigint(20) NOT NULL,
  `COMMENT` varchar(256) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `COLUMN_NAME` varchar(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `TYPE_NAME` varchar(4000) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `INTEGER_IDX` int(11) NOT NULL,
  PRIMARY KEY (`SD_ID`,`COLUMN_NAME`),
  KEY `COLUMNS_N49` (`SD_ID`),
  CONSTRAINT `COLUMNS_FK1` FOREIGN KEY (`SD_ID`) REFERENCES `SDS` (`SD_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DATABASE_PARAMS`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS `DATABASE_PARAMS` (
  `DB_ID` bigint(20) NOT NULL,
  `PARAM_KEY` varchar(180) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `PARAM_VALUE` varchar(4000) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  PRIMARY KEY (`DB_ID`,`PARAM_KEY`),
  KEY `DATABASE_PARAMS_N49` (`DB_ID`),
  CONSTRAINT `DATABASE_PARAMS_FK1` FOREIGN KEY (`DB_ID`) REFERENCES `DBS` (`DB_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DB_PRIVS`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS `DB_PRIVS` (
  `DB_GRANT_ID` bigint(20) NOT NULL,
  `CREATE_TIME` int(11) NOT NULL,
  `DB_ID` bigint(20) DEFAULT NULL,
  `GRANT_OPTION` smallint(6) NOT NULL,
  `GRANTOR` varchar(128) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `GRANTOR_TYPE` varchar(128) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `PRINCIPAL_NAME` varchar(128) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `PRINCIPAL_TYPE` varchar(128) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `DB_PRIV` varchar(128) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  PRIMARY KEY (`DB_GRANT_ID`),
  UNIQUE KEY `DBPRIVILEGEINDEX` (`DB_ID`,`PRINCIPAL_NAME`,`PRINCIPAL_TYPE`,`DB_PRIV`,`GRANTOR`,`GRANTOR_TYPE`),
  KEY `DB_PRIVS_N49` (`DB_ID`),
  CONSTRAINT `DB_PRIVS_FK1` FOREIGN KEY (`DB_ID`) REFERENCES `DBS` (`DB_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IDXS`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS `IDXS` (
  `INDEX_ID` bigint(20) NOT NULL,
  `CREATE_TIME` int(11) NOT NULL,
  `DEFERRED_REBUILD` bit(1) NOT NULL,
  `INDEX_HANDLER_CLASS` varchar(4000) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `INDEX_NAME` varchar(128) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `INDEX_TBL_ID` bigint(20) DEFAULT NULL,
  `LAST_ACCESS_TIME` int(11) NOT NULL,
  `ORIG_TBL_ID` bigint(20) DEFAULT NULL,
  `SD_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`INDEX_ID`),
  UNIQUE KEY `UNIQUEINDEX` (`INDEX_NAME`,`ORIG_TBL_ID`),
  KEY `IDXS_N51` (`SD_ID`),
  KEY `IDXS_N50` (`INDEX_TBL_ID`),
  KEY `IDXS_N49` (`ORIG_TBL_ID`),
  CONSTRAINT `IDXS_FK3` FOREIGN KEY (`INDEX_TBL_ID`) REFERENCES `TBLS` (`TBL_ID`),
  CONSTRAINT `IDXS_FK1` FOREIGN KEY (`ORIG_TBL_ID`) REFERENCES `TBLS` (`TBL_ID`),
  CONSTRAINT `IDXS_FK2` FOREIGN KEY (`SD_ID`) REFERENCES `SDS` (`SD_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `INDEX_PARAMS`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS `INDEX_PARAMS` (
  `INDEX_ID` bigint(20) NOT NULL,
  `PARAM_KEY` varchar(256) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `PARAM_VALUE` varchar(4000) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  PRIMARY KEY (`INDEX_ID`,`PARAM_KEY`),
  KEY `INDEX_PARAMS_N49` (`INDEX_ID`),
  CONSTRAINT `INDEX_PARAMS_FK1` FOREIGN KEY (`INDEX_ID`) REFERENCES `IDXS` (`INDEX_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PARTITIONS`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS `PARTITIONS` (
  `PART_ID` bigint(20) NOT NULL,
  `CREATE_TIME` int(11) NOT NULL,
  `LAST_ACCESS_TIME` int(11) NOT NULL,
  `PART_NAME` varchar(767) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `SD_ID` bigint(20) DEFAULT NULL,
  `TBL_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`PART_ID`),
  UNIQUE KEY `UNIQUEPARTITION` (`PART_NAME`,`TBL_ID`),
  KEY `PARTITIONS_N49` (`TBL_ID`),
  KEY `PARTITIONS_N50` (`SD_ID`),
  CONSTRAINT `PARTITIONS_FK2` FOREIGN KEY (`SD_ID`) REFERENCES `SDS` (`SD_ID`),
  CONSTRAINT `PARTITIONS_FK1` FOREIGN KEY (`TBL_ID`) REFERENCES `TBLS` (`TBL_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PARTITION_KEYS`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS `PARTITION_KEYS` (
  `TBL_ID` bigint(20) NOT NULL,
  `PKEY_COMMENT` varchar(4000) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `PKEY_NAME` varchar(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `PKEY_TYPE` varchar(767) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `INTEGER_IDX` int(11) NOT NULL,
  PRIMARY KEY (`TBL_ID`,`PKEY_NAME`),
  KEY `PARTITION_KEYS_N49` (`TBL_ID`),
  CONSTRAINT `PARTITION_KEYS_FK1` FOREIGN KEY (`TBL_ID`) REFERENCES `TBLS` (`TBL_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PARTITION_KEY_VALS`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS `PARTITION_KEY_VALS` (
  `PART_ID` bigint(20) NOT NULL,
  `PART_KEY_VAL` varchar(256) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `INTEGER_IDX` int(11) NOT NULL,
  PRIMARY KEY (`PART_ID`,`INTEGER_IDX`),
  KEY `PARTITION_KEY_VALS_N49` (`PART_ID`),
  CONSTRAINT `PARTITION_KEY_VALS_FK1` FOREIGN KEY (`PART_ID`) REFERENCES `PARTITIONS` (`PART_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PARTITION_PARAMS`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS `PARTITION_PARAMS` (
  `PART_ID` bigint(20) NOT NULL,
  `PARAM_KEY` varchar(256) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `PARAM_VALUE` varchar(4000) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  PRIMARY KEY (`PART_ID`,`PARAM_KEY`),
  KEY `PARTITION_PARAMS_N49` (`PART_ID`),
  CONSTRAINT `PARTITION_PARAMS_FK1` FOREIGN KEY (`PART_ID`) REFERENCES `PARTITIONS` (`PART_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PART_COL_PRIVS`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS `PART_COL_PRIVS` (
  `PART_COLUMN_GRANT_ID` bigint(20) NOT NULL,
  `COLUMN_NAME` varchar(128) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `CREATE_TIME` int(11) NOT NULL,
  `GRANT_OPTION` smallint(6) NOT NULL,
  `GRANTOR` varchar(128) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `GRANTOR_TYPE` varchar(128) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `PART_ID` bigint(20) DEFAULT NULL,
  `PRINCIPAL_NAME` varchar(128) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `PRINCIPAL_TYPE` varchar(128) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `PART_COL_PRIV` varchar(128) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  PRIMARY KEY (`PART_COLUMN_GRANT_ID`),
  KEY `PART_COL_PRIVS_N49` (`PART_ID`),
  KEY `PARTITIONCOLUMNPRIVILEGEINDEX` (`PART_ID`,`COLUMN_NAME`,`PRINCIPAL_NAME`,`PRINCIPAL_TYPE`,`PART_COL_PRIV`,`GRANTOR`,`GRANTOR_TYPE`),
  CONSTRAINT `PART_COL_PRIVS_FK1` FOREIGN KEY (`PART_ID`) REFERENCES `PARTITIONS` (`PART_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PART_PRIVS`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS `PART_PRIVS` (
  `PART_GRANT_ID` bigint(20) NOT NULL,
  `CREATE_TIME` int(11) NOT NULL,
  `GRANT_OPTION` smallint(6) NOT NULL,
  `GRANTOR` varchar(128) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `GRANTOR_TYPE` varchar(128) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `PART_ID` bigint(20) DEFAULT NULL,
  `PRINCIPAL_NAME` varchar(128) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `PRINCIPAL_TYPE` varchar(128) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `PART_PRIV` varchar(128) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  PRIMARY KEY (`PART_GRANT_ID`),
  KEY `PARTPRIVILEGEINDEX` (`PART_ID`,`PRINCIPAL_NAME`,`PRINCIPAL_TYPE`,`PART_PRIV`,`GRANTOR`,`GRANTOR_TYPE`),
  KEY `PART_PRIVS_N49` (`PART_ID`),
  CONSTRAINT `PART_PRIVS_FK1` FOREIGN KEY (`PART_ID`) REFERENCES `PARTITIONS` (`PART_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ROLE_MAP`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS `ROLE_MAP` (
  `ROLE_GRANT_ID` bigint(20) NOT NULL,
  `ADD_TIME` int(11) NOT NULL,
  `GRANT_OPTION` smallint(6) NOT NULL,
  `GRANTOR` varchar(128) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `GRANTOR_TYPE` varchar(128) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `PRINCIPAL_NAME` varchar(128) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `PRINCIPAL_TYPE` varchar(128) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `ROLE_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ROLE_GRANT_ID`),
  UNIQUE KEY `USERROLEMAPINDEX` (`PRINCIPAL_NAME`,`ROLE_ID`,`GRANTOR`,`GRANTOR_TYPE`),
  KEY `ROLE_MAP_N49` (`ROLE_ID`),
  CONSTRAINT `ROLE_MAP_FK1` FOREIGN KEY (`ROLE_ID`) REFERENCES `ROLES` (`ROLE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SDS`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS `SDS` (
  `SD_ID` bigint(20) NOT NULL,
  `INPUT_FORMAT` varchar(4000) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `IS_COMPRESSED` bit(1) NOT NULL,
  `LOCATION` varchar(4000) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `NUM_BUCKETS` int(11) NOT NULL,
  `OUTPUT_FORMAT` varchar(4000) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `SERDE_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`SD_ID`),
  KEY `SDS_N49` (`SERDE_ID`),
  CONSTRAINT `SDS_FK1` FOREIGN KEY (`SERDE_ID`) REFERENCES `SERDES` (`SERDE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SD_PARAMS`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS `SD_PARAMS` (
  `SD_ID` bigint(20) NOT NULL,
  `PARAM_KEY` varchar(256) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `PARAM_VALUE` varchar(4000) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  PRIMARY KEY (`SD_ID`,`PARAM_KEY`),
  KEY `SD_PARAMS_N49` (`SD_ID`),
  CONSTRAINT `SD_PARAMS_FK1` FOREIGN KEY (`SD_ID`) REFERENCES `SDS` (`SD_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SERDES`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS `SERDES` (
  `SERDE_ID` bigint(20) NOT NULL,
  `NAME` varchar(128) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `SLIB` varchar(4000) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  PRIMARY KEY (`SERDE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SERDE_PARAMS`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS `SERDE_PARAMS` (
  `SERDE_ID` bigint(20) NOT NULL,
  `PARAM_KEY` varchar(256) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `PARAM_VALUE` varchar(4000) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  PRIMARY KEY (`SERDE_ID`,`PARAM_KEY`),
  KEY `SERDE_PARAMS_N49` (`SERDE_ID`),
  CONSTRAINT `SERDE_PARAMS_FK1` FOREIGN KEY (`SERDE_ID`) REFERENCES `SERDES` (`SERDE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SORT_COLS`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS `SORT_COLS` (
  `SD_ID` bigint(20) NOT NULL,
  `COLUMN_NAME` varchar(128) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `ORDER` int(11) NOT NULL,
  `INTEGER_IDX` int(11) NOT NULL,
  PRIMARY KEY (`SD_ID`,`INTEGER_IDX`),
  KEY `SORT_COLS_N49` (`SD_ID`),
  CONSTRAINT `SORT_COLS_FK1` FOREIGN KEY (`SD_ID`) REFERENCES `SDS` (`SD_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TABLE_PARAMS`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS `TABLE_PARAMS` (
  `TBL_ID` bigint(20) NOT NULL,
  `PARAM_KEY` varchar(256) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `PARAM_VALUE` varchar(4000) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  PRIMARY KEY (`TBL_ID`,`PARAM_KEY`),
  KEY `TABLE_PARAMS_N49` (`TBL_ID`),
  CONSTRAINT `TABLE_PARAMS_FK1` FOREIGN KEY (`TBL_ID`) REFERENCES `TBLS` (`TBL_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TBLS`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS `TBLS` (
  `TBL_ID` bigint(20) NOT NULL,
  `CREATE_TIME` int(11) NOT NULL,
  `DB_ID` bigint(20) DEFAULT NULL,
  `LAST_ACCESS_TIME` int(11) NOT NULL,
  `OWNER` varchar(767) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `RETENTION` int(11) NOT NULL,
  `SD_ID` bigint(20) DEFAULT NULL,
  `TBL_NAME` varchar(128) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `TBL_TYPE` varchar(128) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `VIEW_EXPANDED_TEXT` mediumtext,
  `VIEW_ORIGINAL_TEXT` mediumtext,
  PRIMARY KEY (`TBL_ID`),
  UNIQUE KEY `UNIQUETABLE` (`TBL_NAME`,`DB_ID`),
  KEY `TBLS_N50` (`SD_ID`),
  KEY `TBLS_N49` (`DB_ID`),
  CONSTRAINT `TBLS_FK1` FOREIGN KEY (`SD_ID`) REFERENCES `SDS` (`SD_ID`),
  CONSTRAINT `TBLS_FK2` FOREIGN KEY (`DB_ID`) REFERENCES `DBS` (`DB_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TBL_COL_PRIVS`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS `TBL_COL_PRIVS` (
  `TBL_COLUMN_GRANT_ID` bigint(20) NOT NULL,
  `COLUMN_NAME` varchar(128) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `CREATE_TIME` int(11) NOT NULL,
  `GRANT_OPTION` smallint(6) NOT NULL,
  `GRANTOR` varchar(128) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `GRANTOR_TYPE` varchar(128) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `PRINCIPAL_NAME` varchar(128) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `PRINCIPAL_TYPE` varchar(128) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `TBL_COL_PRIV` varchar(128) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `TBL_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`TBL_COLUMN_GRANT_ID`),
  KEY `TABLECOLUMNPRIVILEGEINDEX` (`TBL_ID`,`COLUMN_NAME`,`PRINCIPAL_NAME`,`PRINCIPAL_TYPE`,`TBL_COL_PRIV`,`GRANTOR`,`GRANTOR_TYPE`),
  KEY `TBL_COL_PRIVS_N49` (`TBL_ID`),
  CONSTRAINT `TBL_COL_PRIVS_FK1` FOREIGN KEY (`TBL_ID`) REFERENCES `TBLS` (`TBL_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TBL_PRIVS`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS `TBL_PRIVS` (
  `TBL_GRANT_ID` bigint(20) NOT NULL,
  `CREATE_TIME` int(11) NOT NULL,
  `GRANT_OPTION` smallint(6) NOT NULL,
  `GRANTOR` varchar(128) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `GRANTOR_TYPE` varchar(128) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `PRINCIPAL_NAME` varchar(128) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `PRINCIPAL_TYPE` varchar(128) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `TBL_PRIV` varchar(128) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `TBL_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`TBL_GRANT_ID`),
  KEY `TBL_PRIVS_N49` (`TBL_ID`),
  KEY `TABLEPRIVILEGEINDEX` (`TBL_ID`,`PRINCIPAL_NAME`,`PRINCIPAL_TYPE`,`TBL_PRIV`,`GRANTOR`,`GRANTOR_TYPE`),
  CONSTRAINT `TBL_PRIVS_FK1` FOREIGN KEY (`TBL_ID`) REFERENCES `TBLS` (`TBL_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TYPE_FIELDS`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS `TYPE_FIELDS` (
  `TYPE_NAME` bigint(20) NOT NULL,
  `COMMENT` varchar(256) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `FIELD_NAME` varchar(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `FIELD_TYPE` varchar(767) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `INTEGER_IDX` int(11) NOT NULL,
  PRIMARY KEY (`TYPE_NAME`,`FIELD_NAME`),
  KEY `TYPE_FIELDS_N49` (`TYPE_NAME`),
  CONSTRAINT `TYPE_FIELDS_FK1` FOREIGN KEY (`TYPE_NAME`) REFERENCES `TYPES` (`TYPES_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DBS`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS `DBS` (
  `DB_ID` bigint(20) NOT NULL,
  `DESC` varchar(4000) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `DB_LOCATION_URI` varchar(4000) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `NAME` varchar(128) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  PRIMARY KEY (`DB_ID`),
  UNIQUE KEY `UNIQUE_DATABASE` (`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `GLOBAL_PRIVS`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS `GLOBAL_PRIVS` (
  `USER_GRANT_ID` bigint(20) NOT NULL,
  `CREATE_TIME` int(11) NOT NULL,
  `GRANT_OPTION` smallint(6) NOT NULL,
  `GRANTOR` varchar(128) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `GRANTOR_TYPE` varchar(128) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `PRINCIPAL_NAME` varchar(128) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `PRINCIPAL_TYPE` varchar(128) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `USER_PRIV` varchar(128) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  PRIMARY KEY (`USER_GRANT_ID`),
  UNIQUE KEY `GLOBALPRIVILEGEINDEX` (`PRINCIPAL_NAME`,`PRINCIPAL_TYPE`,`USER_PRIV`,`GRANTOR`,`GRANTOR_TYPE`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ROLES`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS `ROLES` (
  `ROLE_ID` bigint(20) NOT NULL,
  `CREATE_TIME` int(11) NOT NULL,
  `OWNER_NAME` varchar(128) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `ROLE_NAME` varchar(128) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  PRIMARY KEY (`ROLE_ID`),
  UNIQUE KEY `ROLEENTITYINDEX` (`ROLE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEQUENCE_TABLE`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS `SEQUENCE_TABLE` (
  `SEQUENCE_NAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `NEXT_VAL` bigint(20) NOT NULL,
  PRIMARY KEY (`SEQUENCE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `NUCLEUS_TABLES`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS `NUCLEUS_TABLES` (
  `CLASS_NAME` varchar(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `TABLE_NAME` varchar(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `TYPE` varchar(4) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `OWNER` varchar(2) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `VERSION` varchar(20) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `INTERFACE_NAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  PRIMARY KEY (`CLASS_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TYPES`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS `TYPES` (
  `TYPES_ID` bigint(20) NOT NULL,
  `TYPE_NAME` varchar(128) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `TYPE1` varchar(767) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `TYPE2` varchar(767) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  PRIMARY KEY (`TYPES_ID`),
  UNIQUE KEY `UNIQUE_TYPE` (`TYPE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2011-03-14 15:30:49
