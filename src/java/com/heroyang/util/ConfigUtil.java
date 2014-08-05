package com.heroyang.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.alibaba.fastjson.JSONObject;
import com.heroyang.mdl.ConfigEntry;
import com.heroyang.mdl.ConfigFile;

public final class ConfigUtil {

	private ConfigUtil() {
	}

	/**
	 * 反序列化xml
	 * 
	 * @param file
	 * @return
	 */
	public static ConfigFile parseConfigFile(File file) throws IOException {
		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(file));
			StringBuilder buffer = new StringBuilder();
			String data = br.readLine();// 一次读入一行，直到读入null为文件结束
			while (data != null) {
				buffer.append(data);
				data = br.readLine(); // 接着读下一行
			}

			ConfigFile config = JSONObject.parseObject(buffer.toString(),
					ConfigFile.class);
			// 配置文件所在的目录
			config.setConfigFilePath(file.getParentFile().getAbsolutePath());
			// 使用相对目录配置
			if (!new File(config.getProjectBaseDir()).isAbsolute()) {
				config.setProjectBaseDir(new File(ConfigUtil.combine(
						config.getConfigFilePath(), config.getProjectBaseDir()))
						.getCanonicalPath());
			}
			if (config.getEntries() != null) {
				for (ConfigEntry entry : config.getEntries()) {
					entry.setOutputRoot(combine(config.getProjectBaseDir(),
							entry.getOutputRoot()));
					entry.setSourceRoot(combine(config.getProjectBaseDir(),
							entry.getSourceRoot()));
				}
			}
			return config;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			br.close();
		}
	}

	/**
	 * 链接两个目录
	 * 
	 * @param path1
	 * @param path2
	 * @return
	 */
	public static String combine(String path1, String path2) {
		if (path2 == null)
			return path1;
		File file1 = new File(path1);
		File file2 = new File(file1, path2);
		return file2.getPath();
	}

}
