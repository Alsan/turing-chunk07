<!DOCTYPE html>
<!--
~ Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
~
~ WSO2 Inc. licenses this file to you under the Apache License,
~ Version 2.0 (the "License"); you may not use this file except
~ in compliance with the License.
~ You may obtain a copy of the License at
~
~ http://www.apache.org/licenses/LICENSE-2.0
~
~ Unless required by applicable law or agreed to in writing,
~ software distributed under the License is distributed on an
~ "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
~ KIND, either express or implied. See the License for the
~ specific language governing permissions and limitations
~ under the License.
-->
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:bundle basename="org.wso2.carbon.identity.application.authentication.endpoint.i18n.passivests.Resources">
<html lang="en">
    <head>
        <meta charset="utf-8">
        <title>PASSIVE STS</title>
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content="">
        <meta name="author" content="">

        <!-- Le styles -->
        <link href="passivests/assets/css/bootstrap.min.css" rel="stylesheet">
        <link href="passivests/css/localstyles.css" rel="stylesheet">
        <!--[if lt IE 8]>
        <link href="passivests/css/localstyles-ie7.css" rel="stylesheet">
        <![endif]-->

        <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
        <!--[if lt IE 9]>
        <script src="passivests/assets/js/html5.js"></script>
        <![endif]-->
        <script src="passivests/assets/js/jquery-1.7.1.min.js"></script>
        <script type="text/javascript">
                function doLogin() {
                    var loginForm = document.getElementById('loginForm');
                    loginForm.submit();
                }
            </script>


    </head>

    <body>
        <div class="header-strip">&nbsp;</div>
        <div class="header-back">
            <div class="container">
                <div class="row">
                    <div class="span4 offset3">
                        <a class="logo">&nbsp</a>
                    </div>
                </div>
            </div>
        </div>

        <div class="header-text">
                <!-- put the info you want to display in the black strip here -->
        </div>

        <div class="container">
            <div class="row">
                <div class="span5 offset3 content-section">
                    <form action="../../passivests" method="post" id="loginForm" class="well form-horizontal" >

                            <div class="alert alert-error" id="loginbox" style="display:none">
                                <strong id="loginDisplayText"><fmt:message
                                        key='enter.password.to.signin'/></strong>
                            </div>

                            <div class="control-group">
                                <label class="control-label"><fmt:message key='username'/></label>
                                <div class="controls">
                                    <input type="text" id='username' name="username" size='30'/>
                                </div>
                            </div>

                            <div class="control-group">
                                <label class="control-label"><fmt:message key='password'/></label>
                                <div class="controls">
                                    <input type="password" id='password' name="password" size='30'/>
                                </div>
                            </div>

                            <div class="form-actions">
                                <input type="submit" value="<fmt:message key='login'/>" class="btn btn-primary" id="loginBtn">
                            </div>

                    </form>
                </div>
            </div>
        </div>


    </body>
</html>
</fmt:bundle>
