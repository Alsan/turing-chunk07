
/*
 * Copyright 2005-2009 WSO2, Inc. http://wso2.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

function isSessionValid() {
    var response = jQuery.ajax({
        type: "POST",
        url: "GSUserMgt-ajaxprocessor.jsp",
        data: "func=sessionValid&nocache=" + new Date().getTime(),
        async:   false
    }).responseText;

    return (response.search('true'))
}

function checkSession() {
    if (isSessionValid() > 0) {
        return true;
    } else {

        window.location.href='../admin/login.jsp';
        return false;
    }
}