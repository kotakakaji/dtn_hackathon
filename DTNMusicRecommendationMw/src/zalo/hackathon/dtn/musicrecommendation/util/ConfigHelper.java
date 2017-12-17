/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zalo.hackathon.dtn.musicrecommendation.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author Kiss
 */
public class ConfigHelper {

	public static final ConfigHelper Instance = new ConfigHelper();

	private final Properties _properties;

	private ConfigHelper() {
		_properties = new Properties();
		InputStream input = null;
		try {
			if (new File("conf/app.conf").exists()) {
				input = new FileInputStream("conf/app.conf");
			} else {
				input = new FileInputStream("../conf/app.conf");
			}
			_properties.load(input);
		} catch (IOException ex) {
			System.err.println("[WARNING] Cannot load config file.");
		} finally {
			try {
				if (input != null) {
					input.close();
				}
			} catch (Exception e) {
			}
		}
	}

	public String getString(String name, String defaultVal) {
		String property = _properties.getProperty(name);
		if (property == null) {
			property = defaultVal;
		}
		return property;
	}

	public int getInt(String name, int defaultVal) {
		String property = _properties.getProperty(name);
		int ret;
		if (property == null) {
			ret = defaultVal;
		} else {
			try {
				ret = Integer.parseInt(property);
			} catch (Exception e) {
				ret = defaultVal;
			}
		}
		return ret;
	}

	public long getLong(String name, long defaultVal) {
		String property = _properties.getProperty(name);
		long ret;
		if (property == null) {
			ret = defaultVal;
		} else {
			try {
				ret = Long.parseLong(property);
			} catch (Exception e) {
				ret = defaultVal;
			}
		}
		return ret;
	}

	public boolean getBoolean(String name, boolean defaultVal) {
		String property = _properties.getProperty(name);
		boolean ret;
		if (property == null) {
			ret = defaultVal;
		} else {
			try {
				ret = Boolean.parseBoolean(property);
			} catch (Exception e) {
				ret = defaultVal;
			}
		}
		return ret;
	}

	public static void main(String[] args) {
		Properties p = Instance._properties;
		for (Map.Entry<Object, Object> entry : p.entrySet()) {
			Object key = entry.getKey();
			Object value = entry.getValue();
			System.err.println(key + " -- " + value);
		}
	}

}
