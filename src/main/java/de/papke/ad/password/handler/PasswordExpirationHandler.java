package de.papke.ad.password.handler;

import java.io.File;

import com.unboundid.ldap.sdk.Attribute;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPURL;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchResultEntry;
import com.unboundid.ldap.sdk.SearchScope;


public class PasswordExpirationHandler extends Handler {

	private String username;
	private String password;
	private String host;
	private String port;
	private String url;
	private String baseDn;
	private String userDn;
	private String userSecret;
	private int daysTillPasswordExpires;

	public PasswordExpirationHandler(File configFile) {
		
		super(configFile);

		// smb credentials
		this.username = properties.getProperty(Constants.CONFIG_USERNAME);
		this.password = properties.getProperty(Constants.CONFIG_PASSWORD);
		
		// ad configuration
		this.host = properties.getProperty(Constants.CONFIG_HOST);
		this.port = properties.getProperty(Constants.CONFIG_PORT);
		this.baseDn = properties.getProperty(Constants.CONFIG_BASE_DN);
		this.userDn = properties.getProperty(Constants.CONFIG_USER_DN);
		this.userSecret = properties.getProperty(Constants.CONFIG_USER_SECRET);
		this.url = "ldap://" + host + ":" + port;
	}
	
	public int getDaysTillPasswordExpires() {
		
		// calculate days till password expires
		long now = System.currentTimeMillis();
		long maxPwdAge = getMaxPasswordAge();
		long pwdLastSet = getPasswordLastSet();
		
		if (maxPwdAge != -1 && pwdLastSet != -1) {
			long expires = getTimestamp(pwdLastSet + Math.abs(maxPwdAge));
			daysTillPasswordExpires = (int) ((expires - now) / 1000 / 60 / 60 / 24);
		}
		else {
			daysTillPasswordExpires = -1;
		}
		
		return daysTillPasswordExpires;
	}
	
	private long getPasswordLastSet() {

		long pwdLastSet = -1;
		LDAPConnection ldapConnection = null;
		
		try {
			ldapConnection = getLdapConnection(userDn, getLdapPassword(), url);	
			String[] attributes = new String[] {Constants.AD_PWD_LAST_SET};
			String filter = "(" + Constants.AD_SAM_ACCOUNT_NAME + "=" + username + ")";
			SearchResult searchResult = ldapConnection.search(baseDn, SearchScope.SUB, filter, attributes);
			
			for (SearchResultEntry searchResultEntry : searchResult.getSearchEntries()) {
				Attribute pwdLastSetAttribute = searchResultEntry.getAttribute(Constants.AD_PWD_LAST_SET);
				pwdLastSet = pwdLastSetAttribute.getValueAsLong();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (ldapConnection != null) {
				ldapConnection.close();
			}
		}
		
		return pwdLastSet;
	}
	
	private long getMaxPasswordAge() {
		
		long maxPwdAge = -1;
		LDAPConnection ldapConnection = null;
		
		try {
			ldapConnection = getLdapConnection(userDn, getLdapPassword(), url);
			String[] attributes = new String[] {Constants.AD_MAX_PWD_AGE};
			String filter = "(" + Constants.AD_OBJECT_CLASS + "=" + Constants.AD_DOMAIN + ")";
			SearchResult searchResult = ldapConnection.search(baseDn, SearchScope.BASE, filter, attributes);
			
			for (SearchResultEntry searchResultEntry : searchResult.getSearchEntries()) {
				Attribute maxPwdAgeAttribute = searchResultEntry.getAttribute(Constants.AD_MAX_PWD_AGE);
				maxPwdAge = maxPwdAgeAttribute.getValueAsLong();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (ldapConnection != null) {
				ldapConnection.close();
			}
		}
		
		return maxPwdAge;
	}
	
	private long getTimestamp(long adTimestamp) {
        return (adTimestamp / 10000) - Constants.AD_TIMESTAMP_DIFFERENCE;
    }
	
	private String getLdapPassword() {
		return userSecret != null ? userSecret : password;
	}
	
	private LDAPConnection getLdapConnection(String username, String password, String url) throws Exception {
		LDAPURL ldapUrl = new LDAPURL(url);
		return new LDAPConnection(ldapUrl.getHost(), ldapUrl.getPort(), username, password);
	}
}
