CREATE TABLE IDP_BASE_TABLE (
            NAME VARCHAR(20),
            PRIMARY KEY (NAME)
)ENGINE INNODB;

INSERT INTO IDP_BASE_TABLE values ('IdP');

CREATE TABLE IF NOT EXISTS UM_TENANT_IDP (
			UM_ID INTEGER AUTO_INCREMENT,
			UM_TENANT_ID INTEGER,
			UM_TENANT_IDP_NAME VARCHAR(254),
			UM_TENANT_IDP_ISSUER VARCHAR(512),
            		UM_TENANT_IDP_URL VARCHAR(2048),
			UM_TENANT_IDP_THUMBPRINT VARCHAR(2048),
			UM_TENANT_IDP_PRIMARY CHAR(1) NOT NULL,
			UM_TENANT_IDP_AUDIENCE VARCHAR(2048),
			UM_TENANT_IDP_TOKEN_EP_ALIAS VARCHAR(2048),
			PRIMARY KEY (UM_ID),
			CONSTRAINT CON_IDP_KEY UNIQUE (UM_TENANT_ID, UM_TENANT_IDP_NAME))
ENGINE INNODB;

CREATE TABLE IF NOT EXISTS UM_TENANT_IDP_ROLES (
			UM_ID INTEGER AUTO_INCREMENT,
			UM_TENANT_IDP_ID INTEGER,
			UM_TENANT_IDP_ROLE VARCHAR(254),
			PRIMARY KEY (UM_ID),
			CONSTRAINT CON_ROLES_KEY UNIQUE (UM_TENANT_IDP_ID, UM_TENANT_IDP_ROLE),
			FOREIGN KEY (UM_TENANT_IDP_ID) REFERENCES UM_TENANT_IDP(UM_ID) ON DELETE CASCADE)
ENGINE INNODB;

CREATE TABLE IF NOT EXISTS UM_TENANT_IDP_ROLE_MAPPINGS (
			UM_ID INTEGER AUTO_INCREMENT,
			UM_TENANT_IDP_ROLE_ID INTEGER,
			UM_TENANT_ID INTEGER,
			UM_TENANT_ROLE VARCHAR(253),	
			PRIMARY KEY (UM_ID),
			CONSTRAINT CON_ROLE_MAPPINGS_KEY UNIQUE (UM_TENANT_IDP_ROLE_ID, UM_TENANT_ID, UM_TENANT_ROLE),
			FOREIGN KEY (UM_TENANT_IDP_ROLE_ID) REFERENCES UM_TENANT_IDP_ROLES(UM_ID) ON DELETE CASCADE)
ENGINE INNODB;
