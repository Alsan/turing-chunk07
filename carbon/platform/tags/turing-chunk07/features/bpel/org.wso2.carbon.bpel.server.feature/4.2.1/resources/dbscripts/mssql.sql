--
-- Licensed to the Apache Software Foundation (ASF) under one
-- or more contributor license agreements.  See the NOTICE file
-- distributed with this work for additional information
-- regarding copyright ownership.  The ASF licenses this file
-- to you under the Apache License, Version 2.0 (the
-- "License"); you may not use this file except in compliance
-- with the License.  You may obtain a copy of the License at
--
--    http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing,
-- software distributed under the License is distributed on an
-- "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
-- KIND, either express or implied.  See the License for the
-- specific language governing permissions and limitations
-- under the License.
--

--
-- BPEL Related SQL Scripts
--

CREATE TABLE ODE_SCHEMA_VERSION (VERSION INT);
INSERT INTO ODE_SCHEMA_VERSION VALUES (6);

CREATE TABLE ODE_JOB (
  jobid VARCHAR(64)  NOT NULL DEFAULT '',
  ts BIGINT NOT NULL DEFAULT 0,
  nodeid VARCHAR(64)  NULL,
  scheduled INT NOT NULL DEFAULT 0,
  transacted INT NOT NULL DEFAULT 0,

  instanceId BIGINT,
  mexId VARCHAR(255),
  processId VARCHAR(255),
  type VARCHAR(255),
  channel VARCHAR(255),
  correlatorId VARCHAR(255),
  correlationKeySet VARCHAR(255),
  retryCount INT,
  inMem INT,
  detailsExt VARBINARY(4096)  NULL,

  PRIMARY KEY(jobid));

CREATE TABLE TASK_ATTACHMENT (ATTACHMENT_ID BIGINT NOT NULL, MESSAGE_EXCHANGE_ID VARCHAR(255), PRIMARY KEY (ATTACHMENT_ID));
CREATE TABLE ODE_ACTIVITY_RECOVERY (ID BIGINT NOT NULL, ACTIONS VARCHAR(255), ACTIVITY_ID BIGINT, CHANNEL VARCHAR(255), DATE_TIME DATETIME, DETAILS TEXT, INSTANCE_ID BIGINT, REASON VARCHAR(255), RETRIES INT, PRIMARY KEY (ID));
CREATE TABLE ODE_CORRELATION_SET (CORRELATION_SET_ID BIGINT NOT NULL, CORRELATION_KEY VARCHAR(255), NAME VARCHAR(255), SCOPE_ID BIGINT, PRIMARY KEY (CORRELATION_SET_ID));
CREATE TABLE ODE_CORRELATOR (CORRELATOR_ID BIGINT NOT NULL, CORRELATOR_KEY VARCHAR(255), PROC_ID BIGINT, PRIMARY KEY (CORRELATOR_ID));
CREATE TABLE ODE_CORSET_PROP (ID BIGINT NOT NULL, CORRSET_ID BIGINT, PROP_KEY VARCHAR(255), PROP_VALUE VARCHAR(255), PRIMARY KEY (ID));
CREATE TABLE ODE_EVENT (EVENT_ID BIGINT NOT NULL, DETAIL VARCHAR(255), DATA IMAGE, SCOPE_ID BIGINT, TSTAMP DATETIME, TYPE VARCHAR(255), INSTANCE_ID BIGINT, PROCESS_ID BIGINT, PRIMARY KEY (EVENT_ID));
CREATE TABLE ODE_FAULT (FAULT_ID BIGINT NOT NULL, ACTIVITY_ID INT, DATA TEXT, MESSAGE VARCHAR(4000), LINE_NUMBER INT, NAME VARCHAR(255), PRIMARY KEY (FAULT_ID));
CREATE TABLE ODE_MESSAGE (MESSAGE_ID BIGINT NOT NULL, DATA TEXT, HEADER TEXT, TYPE VARCHAR(255), MESSAGE_EXCHANGE_ID VARCHAR(255), PRIMARY KEY (MESSAGE_ID));
CREATE TABLE ODE_MESSAGE_EXCHANGE (MESSAGE_EXCHANGE_ID VARCHAR(255) NOT NULL, CALLEE VARCHAR(255), CHANNEL VARCHAR(255), CORRELATION_ID VARCHAR(255), CORRELATION_KEYS VARCHAR(255), CORRELATION_STATUS VARCHAR(255), CREATE_TIME DATETIME, DIRECTION INT, EPR TEXT, FAULT VARCHAR(255), FAULT_EXPLANATION VARCHAR(255), OPERATION VARCHAR(255), PARTNER_LINK_MODEL_ID INT, PATTERN VARCHAR(255), PIPED_ID VARCHAR(255), PORT_TYPE VARCHAR(255), PROPAGATE_TRANS BIT, STATUS VARCHAR(255), SUBSCRIBER_COUNT INT, CORR_ID BIGINT, PARTNER_LINK_ID BIGINT, PROCESS_ID BIGINT, PROCESS_INSTANCE_ID BIGINT, REQUEST_MESSAGE_ID BIGINT, RESPONSE_MESSAGE_ID BIGINT, PRIMARY KEY (MESSAGE_EXCHANGE_ID));
CREATE TABLE ODE_MESSAGE_ROUTE (MESSAGE_ROUTE_ID BIGINT NOT NULL, CORRELATION_KEY VARCHAR(255), GROUP_ID VARCHAR(255), ROUTE_INDEX INT, PROCESS_INSTANCE_ID INT, ROUTE_POLICY VARCHAR(16), CORR_ID BIGINT, PRIMARY KEY (MESSAGE_ROUTE_ID));
CREATE TABLE ODE_MEX_PROP (ID BIGINT NOT NULL, MEX_ID VARCHAR(255), PROP_KEY VARCHAR(255), PROP_VALUE VARCHAR(2000), PRIMARY KEY (ID));
CREATE TABLE ODE_PARTNER_LINK (PARTNER_LINK_ID BIGINT NOT NULL, MY_EPR TEXT, MY_ROLE_NAME VARCHAR(255), MY_ROLE_SERVICE_NAME VARCHAR(255), MY_SESSION_ID VARCHAR(255), PARTNER_EPR TEXT, PARTNER_LINK_MODEL_ID INT, PARTNER_LINK_NAME VARCHAR(255), PARTNER_ROLE_NAME VARCHAR(255), PARTNER_SESSION_ID VARCHAR(255), SCOPE_ID BIGINT, PRIMARY KEY (PARTNER_LINK_ID));
CREATE TABLE ODE_PROCESS (ID BIGINT NOT NULL, GUID VARCHAR(255), PROCESS_ID VARCHAR(255), PROCESS_TYPE VARCHAR(255), VERSION BIGINT, PRIMARY KEY (ID));
CREATE TABLE ODE_PROCESS_INSTANCE (ID BIGINT NOT NULL, DATE_CREATED DATETIME, EXECUTION_STATE IMAGE, FAULT_ID BIGINT, LAST_ACTIVE_TIME DATETIME, LAST_RECOVERY_DATE DATETIME, PREVIOUS_STATE SMALLINT, SEQUENCE BIGINT, INSTANCE_STATE SMALLINT, INSTANTIATING_CORRELATOR_ID BIGINT, PROCESS_ID BIGINT, ROOT_SCOPE_ID BIGINT, PRIMARY KEY (ID));
CREATE TABLE ODE_SCOPE (SCOPE_ID BIGINT NOT NULL, MODEL_ID INT, SCOPE_NAME VARCHAR(255), SCOPE_STATE VARCHAR(255), PROCESS_INSTANCE_ID BIGINT, PARENT_SCOPE_ID BIGINT, PRIMARY KEY (SCOPE_ID));
CREATE TABLE ODE_XML_DATA (XML_DATA_ID BIGINT NOT NULL, DATA TEXT, IS_SIMPLE_TYPE BIT, NAME VARCHAR(255), SCOPE_ID BIGINT, PRIMARY KEY (XML_DATA_ID));
CREATE TABLE ODE_XML_DATA_PROP (ID BIGINT NOT NULL, XML_DATA_ID BIGINT, PROP_KEY VARCHAR(255), PROP_VALUE VARCHAR(255), PRIMARY KEY (ID));
CREATE TABLE OPENJPA_SEQUENCE_TABLE (ID TINYINT NOT NULL, SEQUENCE_VALUE BIGINT, PRIMARY KEY (ID));
CREATE TABLE STORE_DU (NAME VARCHAR(255) NOT NULL, DEPLOYDT DATETIME, DEPLOYER VARCHAR(255), DIR VARCHAR(255), PRIMARY KEY (NAME));
CREATE TABLE STORE_PROCESS (PID VARCHAR(255) NOT NULL, STATE VARCHAR(255), TYPE VARCHAR(255), VERSION BIGINT, DU VARCHAR(255), PRIMARY KEY (PID));
CREATE TABLE STORE_PROCESS_PROP (id BIGINT NOT NULL, PROP_KEY VARCHAR(255), PROP_VAL VARCHAR(255), PRIMARY KEY (id));
CREATE TABLE STORE_PROC_TO_PROP (PROCESSCONFDAOIMPL_PID VARCHAR(255), ELEMENT_ID BIGINT);
CREATE TABLE STORE_VERSIONS (id BIGINT NOT NULL, VERSION BIGINT, PRIMARY KEY (id));
CREATE INDEX I_D_TASK_ATTACMENT ON TASK_ATTACHMENT (MESSAGE_EXCHANGE_ID);
CREATE INDEX I_D_CTVRY_INSTANCE ON ODE_ACTIVITY_RECOVERY (INSTANCE_ID);
CREATE INDEX I_D_CR_ST_SCOPE ON ODE_CORRELATION_SET (SCOPE_ID);
CREATE INDEX I_D_CRLTR_PROCESS ON ODE_CORRELATOR (PROC_ID);
CREATE INDEX I_D_CRPRP_CORRSET ON ODE_CORSET_PROP (CORRSET_ID);
CREATE INDEX I_OD_VENT_INSTANCE ON ODE_EVENT (INSTANCE_ID);
CREATE INDEX I_OD_VENT_PROCESS ON ODE_EVENT (PROCESS_ID);
CREATE INDEX I_OD_MSSG_MESSAGEEXCHANGE ON ODE_MESSAGE (MESSAGE_EXCHANGE_ID);
CREATE INDEX I_D_MSHNG_CORRELATOR ON ODE_MESSAGE_EXCHANGE (CORR_ID);
CREATE INDEX I_D_MSHNG_PARTNERLINK ON ODE_MESSAGE_EXCHANGE (PARTNER_LINK_ID);
CREATE INDEX I_D_MSHNG_PROCESS ON ODE_MESSAGE_EXCHANGE (PROCESS_ID);
CREATE INDEX I_D_MSHNG_PROCESSINST ON ODE_MESSAGE_EXCHANGE (PROCESS_INSTANCE_ID);
CREATE INDEX I_D_MSHNG_REQUEST ON ODE_MESSAGE_EXCHANGE (REQUEST_MESSAGE_ID);
CREATE INDEX I_D_MSHNG_RESPONSE ON ODE_MESSAGE_EXCHANGE (RESPONSE_MESSAGE_ID);
CREATE INDEX I_D_MS_RT_CORRELATOR ON ODE_MESSAGE_ROUTE (CORR_ID);
CREATE INDEX I_D_MS_RT_PROCESSINST ON ODE_MESSAGE_ROUTE (PROCESS_INSTANCE_ID);
CREATE INDEX I_D_MXPRP_MEX ON ODE_MEX_PROP (MEX_ID);
CREATE INDEX I_D_PRLNK_SCOPE ON ODE_PARTNER_LINK (SCOPE_ID);
CREATE INDEX I_D_PRTNC_FAULT ON ODE_PROCESS_INSTANCE (FAULT_ID);
CREATE INDEX I_D_PRTNC_INSTANTIATINGCORRELATOR ON ODE_PROCESS_INSTANCE (INSTANTIATING_CORRELATOR_ID);
CREATE INDEX I_D_PRTNC_PROCESS ON ODE_PROCESS_INSTANCE (PROCESS_ID);
CREATE INDEX I_D_PRTNC_ROOTSCOPE ON ODE_PROCESS_INSTANCE (ROOT_SCOPE_ID);
CREATE INDEX I_OD_SCOP_PARENTSCOPE ON ODE_SCOPE (PARENT_SCOPE_ID);
CREATE INDEX I_OD_SCOP_PROCESSINSTANCE ON ODE_SCOPE (PROCESS_INSTANCE_ID);
CREATE INDEX I_D_XM_DT_SCOPE ON ODE_XML_DATA (SCOPE_ID);
CREATE INDEX I_D_XMPRP_XMLDATA ON ODE_XML_DATA_PROP (XML_DATA_ID);
CREATE INDEX I_STR_CSS_DU ON STORE_PROCESS (DU);
CREATE INDEX I_STR_PRP_ELEMENT ON STORE_PROC_TO_PROP (ELEMENT_ID);
CREATE INDEX I_STR_PRP_PROCESSCONFDAOIMPL_PID ON STORE_PROC_TO_PROP (PROCESSCONFDAOIMPL_PID);

CREATE INDEX IDX_ODE_JOB_TS ON ODE_JOB(ts);
CREATE INDEX IDX_ODE_JOB_NODEID ON ODE_JOB(nodeid);

--
-- Human Task Related SQL Scripts
--
CREATE TABLE HT_DEADLINE (id BIGINT NOT NULL, DEADLINE_DATE DATETIME NOT NULL, DEADLINE_NAME VARCHAR(255) NOT NULL, STATUS_TOBE_ACHIEVED VARCHAR(255) NOT NULL, TASK_ID BIGINT, PRIMARY KEY (id));
CREATE TABLE HT_DEPLOYMENT_UNIT (id BIGINT NOT NULL, CHECKSUM VARCHAR(255) NOT NULL, DEPLOYED_ON DATETIME, DEPLOY_DIR VARCHAR(255) NOT NULL, NAME VARCHAR(255) NOT NULL, PACKAGE_NAME VARCHAR(255) NOT NULL, STATUS VARCHAR(255) NOT NULL, TENANT_ID BIGINT NOT NULL, VERSION BIGINT NOT NULL, PRIMARY KEY (id));
CREATE TABLE HT_EVENT (id BIGINT NOT NULL, EVENT_DETAILS VARCHAR(255), NEW_STATE VARCHAR(255), OLD_STATE VARCHAR(255), EVENT_TIMESTAMP DATETIME NOT NULL, EVENT_TYPE VARCHAR(255) NOT NULL, EVENT_USER VARCHAR(255) NOT NULL, TASK_ID BIGINT, PRIMARY KEY (id));
CREATE TABLE HT_GENERIC_HUMAN_ROLE (GHR_ID BIGINT NOT NULL, GHR_TYPE VARCHAR(255), TASK_ID BIGINT, PRIMARY KEY (GHR_ID));
CREATE TABLE HT_HUMANROLE_ORGENTITY (HUMANROLE_ID BIGINT, ORGENTITY_ID BIGINT);
CREATE TABLE HT_JOB (id BIGINT NOT NULL, JOB_DETAILS VARCHAR(4000), JOB_NAME VARCHAR(255), NODEID VARCHAR(255), SCHEDULED VARCHAR(1) NOT NULL, TASKID BIGINT NOT NULL, JOB_TIME BIGINT NOT NULL, TRANSACTED VARCHAR(1) NOT NULL, JOB_TYPE VARCHAR(255) NOT NULL, PRIMARY KEY (id));
CREATE TABLE HT_MESSAGE (MESSAGE_ID BIGINT NOT NULL, MESSAGE_DATA TEXT, MESSAGE_HEADER TEXT, MESSAGE_TYPE VARCHAR(255), MESSAGE_NAME VARCHAR(512), TASK_ID BIGINT, PRIMARY KEY (MESSAGE_ID));
CREATE TABLE HT_ORG_ENTITY (ORG_ENTITY_ID BIGINT NOT NULL, ORG_ENTITY_NAME VARCHAR(255), ORG_ENTITY_TYPE VARCHAR(255), PRIMARY KEY (ORG_ENTITY_ID));
CREATE TABLE HT_PRESENTATION_ELEMENT (id BIGINT NOT NULL, PE_CONTENT VARCHAR(2000), XML_LANG VARCHAR(255), PE_TYPE VARCHAR(31), CONTENT_TYPE VARCHAR(255), TASK_ID BIGINT, PRIMARY KEY (id));
CREATE TABLE HT_PRESENTATION_PARAM (id BIGINT NOT NULL, PARAM_NAME VARCHAR(255), PARAM_TYPE VARCHAR(255), PARAM_VALUE VARCHAR(2000), TASK_ID BIGINT, PRIMARY KEY (id));
CREATE TABLE HT_TASK (id BIGINT NOT NULL, ACTIVATION_TIME DATETIME, COMPLETE_BY_TIME DATETIME, CREATED_ON DATETIME, ESCALATED VARCHAR(1), EXPIRATION_TIME DATETIME, TASK_NAME VARCHAR(255) NOT NULL, PACKAGE_NAME VARCHAR(255) NOT NULL, PRIORITY INT NOT NULL, SKIPABLE VARCHAR(1), START_BY_TIME DATETIME, STATUS VARCHAR(255) NOT NULL, STATUS_BEFORE_SUSPENSION VARCHAR(255), TASK_DEF_NAME VARCHAR(255) NOT NULL, TASK_VERSION BIGINT NOT NULL, TENANT_ID INT NOT NULL, TASK_TYPE VARCHAR(255) NOT NULL, UPDATED_ON DATETIME, FAILURE_MESSAGE BIGINT, INPUT_MESSAGE BIGINT, OUTPUT_MESSAGE BIGINT, PARENTTASK_ID BIGINT, PRIMARY KEY (id));
CREATE TABLE HT_TASK_ATTACHMENT (id BIGINT NOT NULL, ACCESS_TYPE VARCHAR(255), ATTACHED_AT DATETIME, CONTENT_TYPE VARCHAR(255), ATTACHMENT_NAME VARCHAR(255), ATTACHMENT_VALUE VARCHAR(255), TASK_ID BIGINT, ATTACHED_BY BIGINT, PRIMARY KEY (id));
CREATE TABLE HT_TASK_COMMENT (id BIGINT NOT NULL, COMMENT_TEXT VARCHAR(4000), COMMENTED_BY VARCHAR(100), COMMENTED_ON DATETIME, MODIFIED_BY VARCHAR(100), MODIFIED_ON DATETIME, TASK_ID BIGINT, PRIMARY KEY (id));
CREATE TABLE HT_VERSIONS (id BIGINT NOT NULL, TASK_VERSION BIGINT NOT NULL, PRIMARY KEY (id));
CREATE INDEX I_HT_DDLN_TASK ON HT_DEADLINE (TASK_ID);
CREATE INDEX I_HT_VENT_TASK ON HT_EVENT (TASK_ID);
CREATE INDEX I_HT_G_RL_TASK ON HT_GENERIC_HUMAN_ROLE (TASK_ID);
CREATE INDEX I_HT_HTTY_ELEMENT ON HT_HUMANROLE_ORGENTITY (ORGENTITY_ID);
CREATE INDEX I_HT_HTTY_HUMANROLE_ID ON HT_HUMANROLE_ORGENTITY (HUMANROLE_ID);
CREATE INDEX I_HT_MSSG_TASK ON HT_MESSAGE (TASK_ID);
CREATE INDEX I_HT_PMNT_DTYPE ON HT_PRESENTATION_ELEMENT (PE_TYPE);
CREATE INDEX I_HT_PMNT_TASK ON HT_PRESENTATION_ELEMENT (TASK_ID);
CREATE INDEX I_HT_PPRM_TASK ON HT_PRESENTATION_PARAM (TASK_ID);
CREATE INDEX I_HT_TASK_FAILUREMESSAGE ON HT_TASK (FAILURE_MESSAGE);
CREATE INDEX I_HT_TASK_INPUTMESSAGE ON HT_TASK (INPUT_MESSAGE);
CREATE INDEX I_HT_TASK_OUTPUTMESSAGE ON HT_TASK (OUTPUT_MESSAGE);
CREATE INDEX I_HT_TASK_PARENTTASK ON HT_TASK (PARENTTASK_ID);
CREATE INDEX I_HT_TMNT_ATTACHEDBY ON HT_TASK_ATTACHMENT (ATTACHED_BY);
CREATE INDEX I_HT_TMNT_TASK ON HT_TASK_ATTACHMENT (TASK_ID);
CREATE INDEX I_HT_TMNT_TASK1 ON HT_TASK_COMMENT (TASK_ID);


--
-- Attachment Management Related SQL Scripts
--
CREATE TABLE ATTACHMENT (
    id BIGINT NOT NULL,
    ATTACHMENT_CONTENT IMAGE,
    CONTENT_TYPE VARCHAR(255) NOT NULL,
    CREATED_BY VARCHAR(255) NOT NULL,
    CREATED_TIME DATETIME NOT NULL DEFAULT GETDATE(),
    ATTACHMENT_NAME VARCHAR(255) NOT NULL,
    ATTACHMENT_URL VARCHAR(2048) NOT NULL,
    PRIMARY KEY (id)
);


--
-- B4P Related SQL Scripts
--
CREATE TABLE HT_COORDINATION_DATA (MESSAGE_ID VARCHAR(255) NOT NULL, PROCESS_INSTANCE_ID VARCHAR(255), PROTOCOL_HANDlER_URL VARCHAR(255) NOT NULL, TASK_ID VARCHAR(255), PRIMARY KEY (MESSAGE_ID));

