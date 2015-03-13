package de.papke.ad.password.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.io.IOUtils;

public class PasswordChangeHandler extends Handler {
	
	private String host;
	private String username;
	private String password;
	
	public PasswordChangeHandler(File configFile) {
		
		super(configFile);
		
		this.host = properties.getProperty(Constants.CONFIG_HOST);
		this.username = properties.getProperty(Constants.CONFIG_USERNAME);
		this.password = properties.getProperty(Constants.CONFIG_PASSWORD);
	}
	
	public File createScriptFile() {
		
		File scriptFile = null;
		FileOutputStream fout = null;
		
		try {
			
			// write out script file an set executable
			String scriptFilePath = "/" + Constants.SCRIPT_FILE_PREFIX + Constants.SCRIPT_FILE_SUFFIX;
			InputStream is = getClass().getResourceAsStream(scriptFilePath);
			scriptFile = File.createTempFile(Constants.SCRIPT_FILE_PREFIX, Constants.SCRIPT_FILE_SUFFIX);
			fout = new FileOutputStream(scriptFile);
			IOUtils.copy(is, fout);
			scriptFile.setExecutable(true);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (fout != null) {
					fout.close();
				}
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return scriptFile;
	}
	
	public boolean changePassword(String newPassword) {
		
		boolean success = false;
		File scriptFile = null;
		
		try {
			
			// write out change password script as temp file
			scriptFile = createScriptFile();
			
			// generate command line for change password script
			CommandLine cmdLine = new CommandLine(scriptFile);
			cmdLine.addArgument(username);
			cmdLine.addArgument(host);
			cmdLine.addArgument(password);
			cmdLine.addArgument(newPassword);
			
			// execute change password script
			DefaultExecutor executor = new DefaultExecutor();
			executor.execute(cmdLine);
			
			// change password in config file
			changePasswordInConfigFile(newPassword);
			
			// operation was successfull
			success = true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (scriptFile != null) {
				scriptFile.delete();
			}
		}
		
		return success;
	}
	
	private void changePasswordInConfigFile(String newPassword) throws IOException {
		String smbConfigFilePath = configDir.getAbsolutePath() + "/" + Constants.CONFIG_SMB_FILE_NAME; 
		Properties properties = new Properties();
		properties.load(new FileInputStream(smbConfigFilePath));
		properties.setProperty(Constants.CONFIG_PASSWORD, newPassword);
		properties.store(new FileOutputStream(smbConfigFilePath), "");
	}
	
	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	public String getHost() {
		return host;
	}
}
