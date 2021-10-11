package njtai.d;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {

	private static Properties properties = new Properties();
	
	static {
		init();
	}
	
	public static void init() {
		loadDefaults();
		loadConfig();
		saveConfig();
	}
	
	public static void loadDefaults() {
		try {
			properties.load(new FileInputStream(NJTAID.getConfigPath()));
		} catch (IOException e) {
		}
	}

	public static void saveConfig() {
		try {
			properties.save(new FileOutputStream(NJTAID.getConfigPath()), "NJTAID Configuration file");
		} catch (IOException e) {}
	}
	
	public static void loadConfig() {
		
	}

	public static void set(String key, boolean value) {
		set(key, "" + value);
	}

	public static void set(String key, int value) {
		set(key, "" + value);
	}

	public static void set(String key, long value) {
		set(key, "" + value);
	}

	public static void set(String key, String value) {
		properties.setProperty(key, value);
	}

	public static boolean getBoolean(String key) {
		if(properties.containsKey(key)) return Boolean.parseBoolean((String) properties.get(key));
		else return false;
	}

	public static int getInt(String key) {
		if(properties.containsKey(key)) return Integer.parseInt((String) properties.get(key));
		else return 0;
	}

	public static long getLong(String key) {
		if(properties.containsKey(key)) return Long.parseLong((String) properties.get(key));
		else return 0;
	}

	public static String get(String key) {
		if(properties.containsKey(key)) return (String) properties.get(key);
		else return null;
	}

	public static boolean contains(String key) {
		return properties.containsKey(key);
	}


}
