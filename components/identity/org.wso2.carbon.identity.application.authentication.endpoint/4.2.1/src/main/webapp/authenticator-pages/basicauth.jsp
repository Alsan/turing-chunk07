<form action="../../commonauth" method="post" id="loginForm" class="well form-horizontal" >
    <div id="loginTable1" class="identity-box">
        <% if (loginFailed) { %>
                    <div class="alert alert-error">
                        <fmt:message key='<%=errorMessage%>'/>
                    </div>
                    <% } %>


        <div class="control-group">
            <label class="control-label" for="oauth_user_name"><fmt:message key='username'/>:</label>

            <div class="controls">
                <input class="input-large" type="text" id='username' name="username"
                                          size='30'/>
                                   <input type="hidden" name="<%=SAMLSSOConstants.SESSION_DATA_KEY%>"
                                   value="<%=request.getParameter(SAMLSSOConstants.SESSION_DATA_KEY)%>"/>

            </div>
        </div>

        <!--Password-->
        <div class="control-group">
            <label class="control-label" for="oauth_user_password"><fmt:message key='password'/>:</label>

            <div class="controls">
                <input type="password" id='password' name="password"  class="input-large"
                                           size='30'/>

                <label class="checkbox" style="margin-top:10px"><input type="checkbox" id="chkRemember" name="chkRemember"><fmt:message key='remember.me'/></label>
            </div>
        </div>

        <div class="form-actions">
            <input type="submit" value="<fmt:message key='login'/>"
                                       class="btn btn-primary">
        </div>


    </div>
</form>
