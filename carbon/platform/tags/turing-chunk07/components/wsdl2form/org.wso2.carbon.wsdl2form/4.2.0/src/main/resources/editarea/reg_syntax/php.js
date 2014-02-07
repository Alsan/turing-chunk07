editAreaLoader.load_syntax["php"] = {
	'DISPLAY_NAME' : 'Php'
	,'COMMENT_SINGLE' : {1 : '//', 2 : '#'}
	,'COMMENT_MULTI' : {'/*' : '*/'}
	,'QUOTEMARKS' : {1: "'", 2: '"'}
	,'KEYWORD_CASE_SENSITIVE' : false
	,'KEYWORDS' : {
		'statements' : [
			'include', 'require', 'include_once', 'require_once',
			'for', 'foreach', 'as', 'if', 'elseif', 'else', 'while', 'do', 'endwhile',
            'endif', 'switch', 'case', 'endswitch',
			'return', 'break', 'continue'
		]
		,'reserved' : [
			'_GET', '_POST', '_SESSION', '_SERVER', '_FILES', '_ENV', '_COOKIE', '_REQUEST',
			'null', '__LINE__', '__FILE__',
			'false', '&lt;?php', '?&gt;', '&lt;?',
			'&lt;script language', '&lt;/script&gt;',
			'true', 'var', 'default',
			'function', 'class', 'new', '&amp;new', 'this',
			'__FUNCTION__', '__CLASS__', '__METHOD__', 'PHP_VERSION',
			'PHP_OS', 'DEFAULT_INCLUDE_PATH', 'PEAR_INSTALL_DIR', 'PEAR_EXTENSION_DIR',
			'PHP_EXTENSION_DIR', 'PHP_BINDIR', 'PHP_LIBDIR', 'PHP_DATADIR', 'PHP_SYSCONFDIR',
			'PHP_LOCALSTATEDIR', 'PHP_CONFIG_FILE_PATH', 'PHP_OUTPUT_HANDLER_START', 'PHP_OUTPUT_HANDLER_CONT',
			'PHP_OUTPUT_HANDLER_END', 'E_ERROR', 'E_WARNING', 'E_PARSE', 'E_NOTICE',
			'E_CORE_ERROR', 'E_CORE_WARNING', 'E_COMPILE_ERROR', 'E_COMPILE_WARNING', 'E_USER_ERROR',
			'E_USER_WARNING', 'E_USER_NOTICE', 'E_ALL'
			
		]
		,'functions' : [
			'func_num_args', 'func_get_arg', 'func_get_args', 'strlen', 'strcmp', 'strncmp', 'strcasecmp', 'strncasecmp', 'each', 'error_reporting', 'define', 'defined',
			'trigger_error', 'user_error', 'set_error_handler', 'restore_error_handler', 'get_declared_classes', 'get_loaded_extensions',
			'extension_loaded', 'get_extension_funcs', 'debug_backtrace',
			'constant', 'bin2hex', 'sleep', 'usleep', 'time', 'mktime', 'gmmktime', 'strftime', 'gmstrftime', 'strtotime', 'date', 'gmdate', 'getdate', 'localtime', 'checkdate', 'flush', 'wordwrap', 'htmlspecialchars', 'htmlentities', 'html_entity_decode', 'md5', 'md5_file', 'crc32', 'getimagesize', 'image_type_to_mime_type', 'phpinfo', 'phpversion', 'phpcredits', 'strnatcmp', 'strnatcasecmp', 'substr_count', 'strspn', 'strcspn', 'strtok', 'strtoupper', 'strtolower', 'strpos', 'strrpos', 'strrev', 'hebrev', 'hebrevc', 'nl2br', 'basename', 'dirname', 'pathinfo', 'stripslashes', 'stripcslashes', 'strstr', 'stristr', 'strrchr', 'str_shuffle', 'str_word_count', 'strcoll', 'substr', 'substr_replace', 'quotemeta', 'ucfirst', 'ucwords', 'strtr', 'addslashes', 'addcslashes', 'rtrim', 'str_replace', 'str_repeat', 'count_chars', 'chunk_split', 'trim', 'ltrim', 'strip_tags', 'similar_text', 'explode', 'implode', 'setlocale', 'localeconv',
			'parse_str', 'str_pad', 'chop', 'strchr', 'sprintf', 'printf', 'vprintf', 'vsprintf', 'sscanf', 'fscanf', 'parse_url', 'urlencode', 'urldecode', 'rawurlencode', 'rawurldecode', 'readlink', 'linkinfo', 'link', 'unlink', 'exec', 'system', 'escapeshellcmd', 'escapeshellarg', 'passthru', 'shell_exec', 'proc_open', 'proc_close', 'rand', 'srand', 'getrandmax', 'mt_rand', 'mt_srand', 'mt_getrandmax', 'base64_decode', 'base64_encode', 'abs', 'ceil', 'floor', 'round', 'is_finite', 'is_nan', 'is_infinite', 'bindec', 'hexdec', 'octdec', 'decbin', 'decoct', 'dechex', 'base_convert', 'number_format', 'fmod', 'ip2long', 'long2ip', 'getenv', 'putenv', 'getopt', 'microtime', 'gettimeofday', 'getrusage', 'uniqid', 'quoted_printable_decode', 'set_time_limit', 'get_cfg_var', 'magic_quotes_runtime', 'set_magic_quotes_runtime', 'get_magic_quotes_gpc', 'get_magic_quotes_runtime',
			'import_request_variables', 'error_log', 'serialize', 'unserialize', 'memory_get_usage', 'var_dump', 'var_export', 'debug_zval_dump', 'print_r','highlight_file', 'show_source', 'highlight_string', 'ini_get', 'ini_get_all', 'ini_set', 'ini_alter', 'ini_restore', 'get_include_path', 'set_include_path', 'restore_include_path', 'setcookie', 'header', 'headers_sent', 'connection_aborted', 'connection_status', 'ignore_user_abort', 'parse_ini_file', 'is_uploaded_file', 'move_uploaded_file', 'intval', 'floatval', 'doubleval', 'strval', 'gettype', 'settype', 'is_null', 'is_resource', 'is_bool', 'is_long', 'is_float', 'is_int', 'is_integer', 'is_double', 'is_real', 'is_numeric', 'is_string', 'is_array', 'is_object', 'is_scalar',
			'ereg', 'ereg_replace', 'eregi', 'eregi_replace', 'split', 'spliti', 'join', 'sql_regcase', 'dl', 'pclose', 'popen', 'readfile', 'rewind', 'rmdir', 'umask', 'fclose', 'feof', 'fgetc', 'fgets', 'fgetss', 'fread', 'fopen', 'fpassthru', 'ftruncate', 'fstat', 'fseek', 'ftell', 'fflush', 'fwrite', 'fputs', 'mkdir', 'rename', 'copy', 'tempnam', 'tmpfile', 'file', 'file_get_contents', 'stream_select', 'stream_context_create', 'stream_context_set_params', 'stream_context_set_option', 'stream_context_get_options', 'stream_filter_prepend', 'stream_filter_append', 'fgetcsv', 'flock', 'get_meta_tags', 'stream_set_write_buffer', 'set_file_buffer', 'set_socket_blocking', 'stream_set_blocking', 'socket_set_blocking', 'stream_get_meta_data', 'stream_register_wrapper', 'stream_wrapper_register', 'stream_set_timeout', 'socket_set_timeout', 'socket_get_status', 'realpath', 'fnmatch', 'fsockopen', 'pfsockopen', 'pack', 'unpack', 'get_browser', 'crypt', 'opendir', 'closedir', 'chdir', 'getcwd', 'rewinddir', 'readdir', 'dir', 'glob', 'fileatime', 'filectime', 'filegroup', 'fileinode', 'filemtime', 'fileowner', 'fileperms', 'filesize', 'filetype', 'file_exists', 'is_writable', 'is_writeable', 'is_readable', 'is_executable', 'is_file', 'is_dir', 'is_link', 'stat', 'lstat', 'chown',
			'touch', 'clearstatcache', 'mail', 'ob_start', 'ob_flush', 'ob_clean', 'ob_end_flush', 'ob_end_clean', 'ob_get_flush', 'ob_get_clean', 'ob_get_length', 'ob_get_level', 'ob_get_status', 'ob_get_contents', 'ob_implicit_flush', 'ob_list_handlers', 'ksort', 'krsort', 'natsort', 'natcasesort', 'asort', 'arsort', 'sort', 'rsort', 'usort', 'uasort', 'uksort', 'shuffle', 'array_walk', 'count', 'end', 'prev', 'next', 'reset', 'current', 'key', 'min', 'max', 'in_array', 'array_search', 'extract', 'compact', 'array_fill', 'range', 'array_multisort', 'array_push', 'array_pop', 'array_shift', 'array_unshift', 'array_splice', 'array_slice', 'array_merge', 'array_merge_recursive', 'array_keys', 'array_values', 'array_count_values', 'array_reverse', 'array_reduce', 'array_pad', 'array_flip', 'array_change_key_case', 'array_rand', 'array_unique', 'array_intersect', 'array_intersect_assoc', 'array_diff', 'array_diff_assoc', 'array_sum', 'array_filter', 'array_map', 'array_chunk', 'array_key_exists', 'pos', 'sizeof', 'key_exists', 'assert', 'assert_options', 'version_compare', 'ftok', 'str_rot13', 'aggregate',
			'session_name', 'session_module_name', 'session_save_path', 'session_id', 'session_regenerate_id', 'session_decode', 'session_register', 'session_unregister', 'session_is_registered', 'session_encode',
			'session_start', 'session_destroy', 'session_unset', 'session_set_save_handler', 'session_cache_limiter', 'session_cache_expire', 'session_set_cookie_params', 'session_get_cookie_params', 'session_write_close', 'preg_match', 'preg_match_all', 'preg_replace', 'preg_replace_callback', 'preg_split', 'preg_quote', 'preg_grep', 'overload', 'ctype_alnum', 'ctype_alpha', 'ctype_cntrl', 'ctype_digit', 'ctype_lower', 'ctype_graph', 'ctype_print', 'ctype_punct', 'ctype_space', 'ctype_upper', 'ctype_xdigit', 'virtual', 'apache_request_headers', 'apache_note', 'apache_lookup_uri', 'apache_child_terminate', 'apache_setenv', 'apache_response_headers', 'apache_get_version', 'getallheaders', 'mysql_connect', 'mysql_pconnect', 'mysql_close', 'mysql_select_db', 'mysql_create_db', 'mysql_drop_db', 'mysql_query', 'mysql_unbuffered_query', 'mysql_db_query', 'mysql_list_dbs', 'mysql_list_tables', 'mysql_list_fields', 'mysql_list_processes', 'mysql_error', 'mysql_errno', 'mysql_affected_rows', 'mysql_insert_id', 'mysql_result', 'mysql_num_rows', 'mysql_num_fields', 'mysql_fetch_row', 'mysql_fetch_array', 'mysql_fetch_assoc', 'mysql_fetch_object', 'mysql_data_seek', 'mysql_fetch_lengths', 'mysql_fetch_field', 'mysql_field_seek', 'mysql_free_result', 'mysql_field_name', 'mysql_field_table', 'mysql_field_len', 'mysql_field_type', 'mysql_field_flags', 'mysql_escape_string', 'mysql_real_escape_string', 'mysql_stat',
			'mysql_thread_id', 'mysql_client_encoding', 'mysql_get_client_info', 'mysql_get_host_info', 'mysql_get_proto_info', 'mysql_get_server_info', 'mysql_info', 'mysql', 'mysql_fieldname', 'mysql_fieldtable', 'mysql_fieldlen', 'mysql_fieldtype', 'mysql_fieldflags', 'mysql_selectdb', 'mysql_createdb', 'mysql_dropdb', 'mysql_freeresult', 'mysql_numfields', 'mysql_numrows', 'mysql_listdbs', 'mysql_listtables', 'mysql_listfields', 'mysql_db_name', 'mysql_dbname', 'mysql_tablename', 'mysql_table_name', 'pg_connect', 'pg_pconnect', 'pg_close', 'pg_connection_status', 'pg_connection_busy', 'pg_connection_reset', 'pg_host', 'pg_dbname', 'pg_port', 'pg_tty', 'pg_options', 'pg_ping', 'pg_query', 'pg_send_query', 'pg_cancel_query', 'pg_fetch_result', 'pg_fetch_row', 'pg_fetch_assoc', 'pg_fetch_array', 'pg_fetch_object', 'pg_fetch_all', 'pg_affected_rows', 'pg_get_result', 'pg_result_seek', 'pg_result_status', 'pg_free_result', 'pg_last_oid', 'pg_num_rows', 'pg_num_fields', 'pg_field_name', 'pg_field_num', 'pg_field_size', 'pg_field_type', 'pg_field_prtlen', 'pg_field_is_null', 'pg_get_notify', 'pg_get_pid', 'pg_result_error', 'pg_last_error', 'pg_last_notice', 'pg_put_line', 'pg_end_copy', 'pg_copy_to', 'pg_copy_from',
			'pg_trace', 'pg_untrace', 'pg_lo_create', 'pg_lo_unlink', 'pg_lo_open', 'pg_lo_close', 'pg_lo_read', 'pg_lo_write', 'pg_lo_read_all', 'pg_lo_import', 'pg_lo_export', 'pg_lo_seek', 'pg_lo_tell', 'pg_escape_string', 'pg_escape_bytea', 'pg_unescape_bytea', 'pg_client_encoding', 'pg_set_client_encoding', 'pg_meta_data', 'pg_convert', 'pg_insert', 'pg_update', 'pg_delete', 'pg_select', 'pg_exec', 'pg_getlastoid', 'pg_cmdtuples', 'pg_errormessage', 'pg_numrows', 'pg_numfields', 'pg_fieldname', 'pg_fieldsize', 'pg_fieldtype', 'pg_fieldnum', 'pg_fieldprtlen', 'pg_fieldisnull', 'pg_freeresult', 'pg_result', 'pg_loreadall', 'pg_locreate', 'pg_lounlink', 'pg_loopen', 'pg_loclose', 'pg_loread', 'pg_lowrite', 'pg_loimport', 'pg_loexport',
			'echo', 'print', 'global', 'static', 'exit', 'array', 'empty', 'eval', 'isset', 'unset', 'die'

		]
	}
	,'OPERATORS' :[
		'+', '-', '/', '*', '=', '<', '>', '%', '!', '&&', '||'
	]
	,'DELIMITERS' :[
		'(', ')', '[', ']', '{', '}'
	]
	,'REGEXPS' : {
		// highlight all variables ($...)
		'variables' : {
			'search' : '()(\\$\\w+)()'
			,'class' : 'variables'
			,'modifiers' : 'g'
			,'execute' : 'before' // before or after
		}
	}
	,'STYLES' : {
		'COMMENTS': 'color: #AAAAAA;'
		,'QUOTESMARKS': 'color: #879EFA;'
		,'KEYWORDS' : {
			'reserved' : 'color: #48BDDF;'
			,'functions' : 'color: #0040FD;'
			,'statements' : 'color: #60CA00;'
			}
		,'OPERATORS' : 'color: #FF00FF;'
		,'DELIMITERS' : 'color: #2B60FF;'
		,'REGEXPS' : {
			'variables' : 'color: #E0BD54;'
		}		
	}
	,'AUTO_COMPLETION' :  {
		"default": {	// the name of this definition group. It's posisble to have different rules inside the same definition file
			"REGEXP": { "before_word": "[^a-zA-Z0-9_]|^"	// \\s|\\.|
						,"possible_words_letters": "[a-zA-Z0-9_\$]+"
						,"letter_after_word_must_match": "[^a-zA-Z0-9_]|$"
						,"prefix_separator": "\\-\\>|\\:\\:"
					}
			,"CASE_SENSITIVE": true
			,"MAX_TEXT_LENGTH": 100		// the maximum length of the text being analyzed before the cursor position
			,"KEYWORDS": {
					'': [	// the prefix of thoses items
						/**
						 * 0 : the keyword the user is typing
						 * 1 : (optionnal) the string inserted in code ("{@}" being the new position of the cursor, "§" beeing the equivalent to the value the typed string indicated if the previous )
						 * 		If empty the keyword will be displayed
						 * 2 : (optionnal) the text that appear in the suggestion box (if empty, the string to insert will be displayed)
						 */
						 ['$_POST']
			    		,['$_GET']
			    		,['$_SESSION']
			    		,['$_SERVER']
			    		,['$_FILES']
			    		,['$_ENV']
			    		,['$_COOKIE']
			    		,['$_REQUEST']
			    		// magic methods
			    		,['__construct', '§( {@} )']
			    		,['__destruct', '§( {@} )']
			    		,['__sleep', '§( {@} )']
			    		,['__wakeup', '§( {@} )']
			    		,['__toString', '§( {@} )']
			    		// include
			    		,['include', '§ "{@}";']
			    		,['include_once', '§ "{@}";']
			    		,['require', '§ "{@}";']
			    		,['require_once', '§ "{@}";']
			    		// statements
			    		,['for', '§( {@} )']
			    		,['foreach', '§( {@} )']
			    		,['if', '§( {@} )']
			    		,['elseif', '§( {@} )']
			    		,['while', '§( {@} )']
			    		,['switch', '§( {@} )']
			    		,['break']
			    		,['case']
			    		,['continue']
			    		,['do']
			    		,['else']
			    		,['endif']
			    		,['endswitch']
			    		,['endwhile']
			    		,['return']
			    		// function
			    		,['unset', '§( {@} )']
					]
				}
			}
		,"live": {	
			
			// class NAME: /class\W+([a-z]+)\W+/gi
			// method: /^(public|private|protected)?\s*function\s+([a-z][a-z0-9\_]*)\s*(\([^\{]*\))/gmi
			// static: /^(public|private|protected)?\s+static\s+(public|private|protected)?\s*function\s+([a-z][a-z0-9\_]*)\s*(\([^\{]*\))/gmi 
			// attributes: /(\$this\-\>|(?:var|public|protected|private)\W+\$)([a-z0-9\_]+)(?!\()\b/gi 
			// 		v1 : /(\$this\-\>|var\W+|public\W+|protected\W+|private\W+)([a-z0-9\_]+)\W*(=|;)/gi 
			// var type: /(\$(this\-\>)?[a-z0-9\_]+)\s*\=\s*new\s+([a-z0-9\_])+/gi 
			
			
			"REGEXP": { "before_word": "[^a-zA-Z0-9_]|^"	// \\s|\\.|
						,"possible_words_letters": "[a-zA-Z0-9_\$]+"
						,"letter_after_word_must_match": "[^a-zA-Z0-9_]|$"
						,"prefix_separator": "\\-\\>"
					}
			,"CASE_SENSITIVE": true
			,"MAX_TEXT_LENGTH": 100		// the maximum length of the text being analyzed before the cursor position
			,"KEYWORDS": {
					'$this': [	// the prefix of thoses items
						['test']
					]
				}
			}
	}
};
