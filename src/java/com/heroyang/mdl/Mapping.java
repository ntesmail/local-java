package com.heroyang.mdl;


public class Mapping {

	public Mapping(){
	}
	
	public static Mapping NoMapping = new Mapping();
	
	public ConfigEntry getConfig() {
		return config;
	}

	public void setConfig(ConfigEntry config) {
		this.config = config;
	}

	public ConfigFile getBaseConfig() {
		return baseConfig;
	}

	public void setBaseConfig(ConfigFile baseConfig) {
		this.baseConfig = baseConfig;
	}

	private ConfigEntry config;
	
	private ConfigFile baseConfig;
	
	
}
