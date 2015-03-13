package de.papke.ad.password.handler;

public class Constants {
	
	// Global
	public static final String APP_NAME = "ad-password-handler"; 
	
	// Script File
	public static final String SCRIPT_FILE_PREFIX = "change_password";
	public static final String SCRIPT_FILE_SUFFIX = ".sh";
	
	// Active Directory
	public static final long AD_TIMESTAMP_DIFFERENCE = 11644473600000L;	
	public static final String AD_PWD_LAST_SET = "pwdLastSet";
	public static final String AD_MAX_PWD_AGE = "maxPwdAge";
	public static final String AD_SAM_ACCOUNT_NAME = "sAMAccountName";
	public static final String AD_OBJECT_CLASS = "objectClass";
	public static final String AD_DOMAIN = "domain";
	
	// Config File
	public static final String CONFIG_SMB_FILE_NAME = "smb.properties"; 
	public static final String CONFIG_USERNAME = "username";
	public static final String CONFIG_PASSWORD = "password";
	public static final String CONFIG_DOMAIN = "domain";
	public static final String CONFIG_HOST = "host";
	public static final String CONFIG_PORT = "port";
	public static final String CONFIG_BASE_DN = "baseDn";
	public static final String CONFIG_USER_DN = "userDn";
	public static final String CONFIG_USER_SECRET = "userSecret";
}
