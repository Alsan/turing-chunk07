editAreaLoader.load_syntax["tsql"] = {
	'DISPLAY_NAME' : 'T-SQL'
	,'COMMENT_SINGLE' : {1 : '--'}
	,'COMMENT_MULTI' : {'/*' : '*/'}
	,'QUOTEMARKS' : {1: "'" }
	,'KEYWORD_CASE_SENSITIVE' : false
	,'KEYWORDS' : {
		'statements': [
		    'ADD', 'EXCEPT', 'PERCENT', 'EXEC', 'PLAN', 'ALTER', 'EXECUTE', 'PRECISION',
		    'PRIMARY', 'EXIT', 'PRINT', 'AS', 'FETCH', 'PROC', 'ASC',
		    'FILE', 'PROCEDURE', 'AUTHORIZATION', 'FILLFACTOR', 'PUBLIC', 'BACKUP', 'FOR', 'RAISERROR',
		    'BEGIN', 'FOREIGN', 'READ', 'FREETEXT', 'READTEXT', 'BREAK', 'FREETEXTTABLE',
		    'RECONFIGURE', 'BROWSE', 'FROM', 'REFERENCES', 'BULK', 'FULL', 'REPLICATION', 'BY',
		    'FUNCTION', 'RESTORE', 'CASCADE', 'GOTO', 'RESTRICT', 'CASE', 'GRANT', 'RETURN',
		    'CHECK', 'GROUP', 'REVOKE', 'CHECKPOINT', 'HAVING', 'RIGHT', 'CLOSE', 'HOLDLOCK', 'ROLLBACK',
		    'CLUSTERED', 'IDENTITY', 'ROWCOUNT', 'IDENTITY_INSERT', 'ROWGUIDCOL', 'COLLATE', 
		    'IDENTITYCOL', 'RULE', 'COLUMN', 'IF', 'SAVE', 'COMMIT', 'SCHEMA', 'COMPUTE', 'INDEX',
		    'SELECT', 'CONSTRAINT', 'CONTAINS', 'INSERT', 'SET',
		    'CONTAINSTABLE', 'INTERSECT', 'SETUSER', 'CONTINUE', 'INTO', 'SHUTDOWN', 'SOME',
		    'CREATE', 'STATISTICS', 'KEY', 'CURRENT', 'KILL', 'TABLE',
		    'CURRENT_DATE', 'TEXTSIZE', 'CURRENT_TIME', 'THEN', 'LINENO',
		    'TO', 'LOAD', 'TOP', 'CURSOR', 'NATIONAL', 'TRAN', 'DATABASE', 'NOCHECK', 
		    'TRANSACTION', 'DBCC', 'NONCLUSTERED', 'TRIGGER', 'DEALLOCATE', 'TRUNCATE',
		    'DECLARE', 'TSEQUAL', 'DEFAULT', 'UNION', 'DELETE', 'OF', 'UNIQUE',
		    'DENY', 'OFF', 'UPDATE', 'DESC', 'OFFSETS', 'UPDATETEXT', 'DISK', 'ON', 'USE', 'DISTINCT', 'OPEN',
		    'DISTRIBUTED', 'OPENDATASOURCE', 'VALUES', 'DOUBLE', 'OPENQUERY', 'VARYING', 'DROP', 
		    'OPENROWSET', 'VIEW', 'DUMMY', 'OPENXML', 'WAITFOR', 'DUMP', 'OPTION', 'WHEN', 'ELSE', 'WHERE',
		    'END', 'ORDER', 'WHILE', 'ERRLVL', 'WITH', 'ESCAPE', 'OVER', 'WRITETEXT'
		],
		'functions': [
		    'COALESCE', 'SESSION_USER', 'CONVERT', 'SYSTEM_USER', 'CURRENT_TIMESTAMP', 'CURRENT_USER', 'NULLIF', 'USER',
			'AVG', 'MIN', 'CHECKSUM', 'SUM', 'CHECKSUM_AGG', 'STDEV', 'COUNT', 'STDEVP', 'COUNT_BIG', 'VAR', 'GROUPING', 'VARP', 'MAX',
			'@@DATEFIRST', '@@OPTIONS', '@@DBTS', '@@REMSERVER', '@@LANGID', '@@SERVERNAME', '@@LANGUAGE', '@@SERVICENAME', '@@LOCK_TIMEOUT',
			'@@SPID', '@@MAX_CONNECTIONS', '@@TEXTSIZE', '@@MAX_PRECISION', '@@VERSION', '@@NESTLEVEL',
			'@@CURSOR_ROWS', 'CURSOR_STATUS', '@@FETCH_STATUS',
			'DATEADD', 'DATEDIFF', 'DATENAME', 'DATEPART', 'DAY', 'GETDATE', 'GETUTCDATE', 'MONTH', 'YEAR',
			'ABS', 'DEGREES', 'RAND', 'ACOS', 'EXP', 'ROUND', 'ASIN', 'FLOOR', 'SIGN', 'ATAN', 'LOG', 'SIN', 'ATN2', 'LOG10', 'SQRT',
			'CEILING', 'PI ', 'SQUARE', 'COS', 'POWER', 'TAN', 'COT', 'RADIANS',
			'@@PROCID', 'COL_LENGTH', 'FULLTEXTCATALOGPROPERTY', 'COL_NAME', 'FULLTEXTSERVICEPROPERTY', 'COLUMNPROPERTY', 'INDEX_COL',
			'DATABASEPROPERTY', 'INDEXKEY_PROPERTY', 'DATABASEPROPERTYEX', 'INDEXPROPERTY', 'DB_ID', 'OBJECT_ID', 'DB_NAME', 'OBJECT_NAME',
			'FILE_ID', 'OBJECTPROPERTY', 'OBJECTPROPERTYEX', 'FILE_NAME', 'SQL_VARIANT_PROPERTY', 'FILEGROUP_ID', 'FILEGROUP_NAME',
			'FILEGROUPPROPERTY', 'TYPEPROPERTY', 'FILEPROPERTY',
			'CURRENT_USER', 'SUSER_ID', 'SUSER_SID', 'IS_MEMBER', 'SUSER_SNAME', 'IS_SRVROLEMEMBER', 'PERMISSIONS', 'SYSTEM_USER',
			'SUSER_NAME', 'USER_ID', 'SESSION_USER', 'USER_NAME', 'ASCII', 'SOUNDEX', 'PATINDEX', 'SPACE', 'CHARINDEX', 'QUOTENAME',
			'STR', 'DIFFERENCE', 'REPLACE', 'STUFF', 'REPLICATE', 'SUBSTRING', 'LEN', 'REVERSE', 'UNICODE', 'LOWER',
			'UPPER', 'LTRIM', 'RTRIM', 'APP_NAME', 'CAST', 'CONVERT', 'COALESCE', 'COLLATIONPROPERTY', 'COLUMNS_UPDATED', 'CURRENT_TIMESTAMP',
			'CURRENT_USER', 'DATALENGTH', '@@ERROR', 'FORMATMESSAGE', 'GETANSINULL', 'HOST_ID', 'HOST_NAME', 'IDENT_CURRENT', 'IDENT_INCR',
			'IDENT_SEED', '@@IDENTITY', 'ISDATE', 'ISNULL', 'ISNUMERIC', 'NEWID', 'NULLIF', 'PARSENAME', '@@ROWCOUNT',
			'SCOPE_IDENTITY', 'SERVERPROPERTY', 'SESSIONPROPERTY', 'SESSION_USER', 'STATS_DATE', 'SYSTEM_USER', '@@TRANCOUNT', 'USER_NAME',
			'@@CONNECTIONS', '@@PACK_RECEIVED', '@@CPU_BUSY', '@@PACK_SENT', '@@TIMETICKS', '@@IDLE', '@@TOTAL_ERRORS', '@@IO_BUSY', '@@TOTAL_READ',
			'@@PACKET_ERRORS', '@@TOTAL_WRITE', 'PATINDEX', 'TEXTVALID', 'TEXTPTR'
		],
		'reserved': [
			'RIGHT', 'INNER', 'IS', 'JOIN', 'CROSS', 'LEFT', 'NULL', 'OUTER'
		]
	}
	,'OPERATORS' :[
		'+', '-', '*', '/', '%', '=', '&' ,'|', '^', '>', '<', '>=', '<=', '<>', '!=', '!<', '!>', 'ALL', 'AND', 'ANY', 'BETWEEN', 'EXISTS', 'IN', 'LIKE', 'NOT', 'OR', '~'
	]
	,'DELIMITERS' :[
		'(', ')', '[', ']', '{', '}'
	]
	,'REGEXPS' : {
		// highlight all variables (@...)
		'variables' : {
			'search' : '()(\\@\\w+)()'
			,'class' : 'variables'
			,'modifiers' : 'g'
			,'execute' : 'before' // before or after
		}
	}
	,'STYLES' : {
		'COMMENTS': 'color: #008000;'
		,'QUOTESMARKS': 'color: #FF0000;'
		,'KEYWORDS' : {
			'reserved' : 'color: #808080;'
			,'functions' : 'color: #FF00FF;'
			,'statements' : 'color: #0000FF;'
			}
		,'OPERATORS' : 'color: #808080;'
		,'DELIMITERS' : 'color: #FF8000;'
		,'REGEXPS' : {
			'variables' : 'color: #E0BD54;'
		}		
	}
};

 	  	 
