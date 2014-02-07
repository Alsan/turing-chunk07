package org.wso2.carbon.identity.application.mgt;

public class DBQueries {

	public static String STORE_BASIC_APPINFO = "INSERT INTO IDN_APPMGT_APP "
			+ "(APP_ID, TENANT_ID, USERNAME) VALUES (?,?,?) ";
	
	public static String STORE_CLIENT_INFO = "INSERT INTO IDN_APPMGT_CLIENT "
			+ "(APP_ID, CLIENT_ID, CLIENT_SECRETE, CALLBACK_URL, TYPE) VALUES (?,?,?,?,?)";
	
	public static String STORE_STEP_INFO = "INSERT INTO IDN_APPMGT_STEP "
			+ "(APP_ID, STEP_ID, AUTHN_ID, IDP_ID, ENDPOINT, TYPE) VALUES (?,?,?,?,?,?)";

    public static String GAT_BASIC_APP_INFO = "SELECT USERNAME " +
            "FROM IDN_APPMGT_APP" +
            "WHERE APP_ID = ? ADN TENANT_ID = ?";

    public static String GET_CLIENT_INFO = "SELECT CLIENT_ID, CLIENT_SECRETE, CALLBACK_URL, TYPE " +
            "FROM IDN_APPMGT_CLIENT " +
            "WHERE APP_ID = ? ";

    public static String GET_STEP_INFO = "SELECT STEP_ID, AUTHN_ID, IDP_ID, ENDPOINT, TYPE " +
            "FROM IDN_APPMGT_STEP" +
            "WHERE APP_ID = ? ";

    public static String GET_ALL_APP = "SELECT APP_ID FROM IDN_APPMGT_APP WHERE TENANT_ID = ?";


}


