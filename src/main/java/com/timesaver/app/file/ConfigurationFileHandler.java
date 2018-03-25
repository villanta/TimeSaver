package com.timesaver.app.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationFileHandler {

	private static final String FILE_NAME = "time_saver.properties";
	private static final String APP_DIRECTORY = "TimeSaver";

	private static final String WINDOWS_BASE_DIRECTORY = System.getenv("LOCALAPPDATA");

	private static ConfigurationFileHandler instance;

	private ConfigurationFileHandler() {

	}

	public static ConfigurationFileHandler getInstance() {
		if (ConfigurationFileHandler.instance == null) {
			ConfigurationFileHandler.instance = new ConfigurationFileHandler();
		}
		return ConfigurationFileHandler.instance;
	}

	public boolean isConfigurationPresent() {
		File configurationFile = new File(getOSSpecificFilePath().concat(FILE_NAME));
		return configurationFile.canRead();
	}

	public Configuration loadConfiguration() {
		File configurationFile = new File(getOSSpecificFilePath().concat(FILE_NAME));
		Properties props = new Properties();
		try {
			props.load(new FileInputStream(configurationFile));
			Configuration c = new Configuration();
			c.readFrom(props);
			return c;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean saveConfiguration(Configuration c) {
		File configurationFolder = new File(getOSSpecificFilePath());
		File configurationFile = new File(getOSSpecificFilePath().concat(FILE_NAME));
		Properties props = new Properties();
		try {
			c.writeTo(props);
			configurationFolder.mkdirs();
			props.store(new FileOutputStream(configurationFile), "");
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	private String getOSSpecificFilePath() {
		if (System.getProperty("os.name").toUpperCase().contains("WIN")) {
			return windowsFilePath();
		} else {
			return "";
		}
	}

	private String windowsFilePath() {
		return WINDOWS_BASE_DIRECTORY.concat("\\" + APP_DIRECTORY + "\\");
	}
}
