CREATE TABLE AM_WORKFLOWS(
    WF_ID INTEGER AUTO_INCREMENT,
    WF_REFERENCE VARCHAR(255) NOT NULL,
    WF_TYPE VARCHAR(255) NOT NULL,
    WF_STATUS VARCHAR(255) NOT NULL,
    WF_CREATED_TIME TIMESTAMP,
    WF_UPDATED_TIME TIMESTAMP,
    WF_STATUS_DESC VARCHAR(1000),
    TENANT_ID INTEGER,
    TENANT_DOMAIN VARCHAR(255),
    WF_EXTERNAL_REFERENCE VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY (WF_ID)
)
/

CREATE SEQUENCE AM_WORKFLOWS_SEQUENCE START WITH 1 INCREMENT BY 1
/

CREATE OR REPLACE AM_WORKFLOWS_TRIGGER
		            BEFORE INSERT
                    ON AM_WORKFLOWS
                    REFERENCING NEW AS NEW
                    FOR EACH ROW
                    BEGIN
                    SELECT AM_WORKFLOWS_SEQUENCE.nextval INTO :NEW.WF_ID FROM dual;
                    END
/

ALTER TABLE AM_APPLICATION ADD APPLICATION_STATUS VARCHAR2(50) DEFAULT 'APPROVED'
/