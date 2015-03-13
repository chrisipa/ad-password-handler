package de.papke.ad.password.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public abstract class Handler {
	
	protected File configDir;
	protected Properties properties;
	
	public Handler(File configDir) {
		
		this.configDir = configDir;
		this.properties = new Properties();
		
		try {
			if (configDir != null && configDir.exists() && configDir.isDirectory()) {
				for (File configFile : configDir.listFiles()) {
					Properties properties = new Properties();
					properties.load(new FileInputStream(configFile));
					this.properties.putAll(properties);
				}
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
