package njtai.d;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import njtai.NJTAI;

public class Config extends Properties {

	private static Config inst = new Config();
	
	private Config() {
		
	}
	
	public static void init() {
		loadDefaults();
		loadConfig();
		saveConfig();
		NJTAI.loadCovers = false;
	}
	
	public static void loadDefaults() {
		set("itemWidth", 240);
		set("itemHeight", 320);
		set("coverWidth", 180);
		set("coverHeight", 240);
		set("proxy", "https://");
	}

	public static void saveConfig() {
		try {
			inst.save(new FileOutputStream(NJTAID.getConfigPath()), "NJTAID Configuration file");
		} catch (IOException e) {}
	}
	
	public static void loadConfig() {
		try {
			inst.load(new FileInputStream(NJTAID.getConfigPath()));
		} catch (IOException e) {
		}
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
		inst.setProperty(key, value);
	}

	public static boolean getBoolean(String key) {
		if(inst.containsKey(key)) return Boolean.parseBoolean((String) inst.get(key));
		else return false;
	}

	public static int getInt(String key) {
		if(inst.containsKey(key)) return Integer.parseInt((String) inst.get(key));
		else return 0;
	}

	public static long getLong(String key) {
		if(inst.containsKey(key)) return Long.parseLong((String) inst.get(key));
		else return 0;
	}

	public static String getString(String key) {
		if(inst.containsKey(key)) return (String) inst.get(key);
		else return null;
	}

	public static boolean contains(String key) {
		return inst.containsKey(key);
	}
	
	@Override
	public Object setProperty(String key, String value) {
		if("proxy".equals(key)) NJTAI.proxy = value;
		return super.setProperty(key, value);
	}
	
}
