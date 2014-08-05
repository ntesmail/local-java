package com.heroyang.mdl;


public class ConfigFile {
	 private ConfigEntry[] Entries;
     /**
      * 项目根目录
      */
	 private String ProjectBaseDir;
     /**
      * 配置文件地址
      */
	 private String ConfigFilePath;

     public ConfigEntry[] getEntries() {
		return Entries;
	}

	public void setEntries(ConfigEntry[] entries) {
		Entries = entries;
	}

	public String getProjectBaseDir() {
		return ProjectBaseDir;
	}

	public void setProjectBaseDir(String projectBaseDir) {
		ProjectBaseDir = projectBaseDir;
	}

	public String getConfigFilePath() {
		return ConfigFilePath;
	}

	public void setConfigFilePath(String configFilePath) {
		ConfigFilePath = configFilePath;
	}

	public ConfigFile clone() {
		ConfigFile file = new ConfigFile();
		file.setConfigFilePath(this.getConfigFilePath());
		file.setProjectBaseDir(this.getProjectBaseDir());
		ConfigEntry[] entries = this.getEntries();
		ConfigEntry[] entries2 = new ConfigEntry[entries.length];
		
		for (int i = 0; i < entries2.length; i++) {
			entries2[i] = entries[i].clone();
		}
		file.setEntries(entries2);
		return file;
	}
	public ConfigFile()
     {
     }
}
