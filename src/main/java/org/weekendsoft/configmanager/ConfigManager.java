/**
 * 
 */
package org.weekendsoft.configmanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author Vivek Kant
 *
 */
public class ConfigManager {
	
	private static final Logger LOG = Logger.getLogger(ConfigManager.class);
	
	private static final String PROP_RESORUCE = "app.properties";
	
	private static ConfigManager INSTANCE = null;
	
	private static Properties props = new Properties();

	private ConfigManager() {
		super();
	}
	
	public static ConfigManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ConfigManager();
			INSTANCE.init();
		}
		
		return INSTANCE;
	}
	
	private void init() {
		
		String pFileName = null;
		try {
			InputStream is = this.getClass().getResourceAsStream('/' + PROP_RESORUCE);
			if (is != null) {
				LOG.info("Loading properties from : /app.properties");
				loadProperties(is);
			}
			is.close();

			pFileName = System.getProperty(PROP_RESORUCE);
			File pFile = new File(pFileName);
			if (pFile.exists()) {
			
				FileInputStream fis = new FileInputStream(pFile);
				if (fis != null) loadProperties(fis);
				fis.close();
			}
		} 
		catch (FileNotFoundException e) {
			LOG.info("File Not Found: " + pFileName, e);
		} 
		catch (NullPointerException npe) {
			LOG.info("System parameter for property does not exist");
		} 
		catch (IOException e) {
			LOG.info("IO Exception", e);
		}
		
		LOG.debug("Final Properties: " + props);
		
	}
	
	public void loadProperties(InputStream is) {
		
		try {
			LOG.info("Loading properties fron input stream");
			props.load(is);
		} 
		catch (IOException e) {
			LOG.error("Exception while loading properties", e);
		}
	}
	
	public void setConfigProperty(String key, String value) {
		props.setProperty(key, value);
	}
	
	public String getProperty(String key) {
		return props.getProperty(key);
	}
	
	public String getProperty(String key, String defaultValue) {
		return (getProperty(key) != null) ? getProperty(key) : defaultValue;
	}
	
	public int getIntProperty(String key, int defaultValue) {
		
		String strVal = props.getProperty(key);
		try {
			int val = Integer.parseInt(strVal);
			return val;
		} 
		catch (NumberFormatException e) {
			LOG.info("Not an integer: " + strVal);
		}
				
		return defaultValue;
	}
	
	public boolean getBooleanProperty(String key, boolean defaultValue) {
		
		String strVal = props.getProperty(key);
		if (strVal != null && !"".equals(strVal.trim())) {
			if (strVal.trim().toLowerCase().equalsIgnoreCase("true")) return true;
			if (strVal.trim().toLowerCase().equalsIgnoreCase("false")) return false;
		}
		
		return defaultValue;
	}

}
