package com.petstore.api.config;

import java.io.InputStream;
import java.util.Properties;


public final class Config {

	private static final Properties props = new Properties();

	static {
		try (InputStream in = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
			if (in != null) {
				props.load(in);
			}
		} catch (Exception e) {
		}
	}

	private Config() {
	}

	public static String get(String key) {
		String systemValue = System.getProperty(key);
		if (systemValue != null && !systemValue.isBlank()) {
			return systemValue;
		}
		return props.getProperty(key);
	}

	public static String baseUrl() {
		String url = get("base.url");
		return (url != null && !url.isBlank()) ? url : "https://petstore.swagger.io/v2";
	}

	public static String apiKey() {
		String key = get("api.key");
		return (key != null && !key.isBlank()) ? key : "special-key";
	}
}
