/*
 * Copyright 2013, WSO2, Inc. http://wso2.org
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

function setStratosFormSubmitFunction(form, validateFunction,
		ajaxprocessorPage, successPage, commandButton) {
	jQuery(form).submit(
			function(event) {
				if (validateFunction && typeof validateFunction == "function"
						&& !validateFunction()) {
					return false;
				}

				postToBEService(ajaxprocessorPage, jQuery(form).serialize(), successPage, commandButton);
				return false;
			});
}

function postToBEService(ajaxprocessorPage, data, successPage, commandButton, validateFunction) {
	if (validateFunction && typeof validateFunction == "function"
		&& !validateFunction()) {
		return;
	}
	showWait(commandButton);
	jQuery.post(ajaxprocessorPage, data).done(
			function(data, textStatus, jqXHR) {
				stopWait(commandButton);
				var msg = jQuery(jqXHR.responseText).filter('#responseMsg')
						.text();
				if (successPage) {
					CARBON.showInfoDialog(msg, function() {
						location.href = successPage;
					});
				} else {
					CARBON.showInfoDialog(msg);
				}
			}).fail(function(jqXHR, textStatus, errorThrown) {
		var msg = jQuery(jqXHR.responseText).filter('#responseMsg').text();
		if (msg && msg.length > 0) {
			CARBON.showErrorDialog(msg);
		} else {
			// Ignore
			// Refer SPI-310
		}
	}).complete(function() {
		stopWait(commandButton);
	});
}

function showWait(commandButton) {
	jQuery.blockUI('Please wait...');
	if (commandButton) {
		jQuery(commandButton).attr("disabled", "disabled");
	}
	//jQuery("body").css("cursor", "wait");
}

function stopWait(commandButton) {
	jQuery.unblockUI();
	if (commandButton) {
		jQuery(commandButton).removeAttr("disabled");
	}
	//jQuery("body").css("cursor", "auto");
}