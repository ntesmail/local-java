package com.heroyang;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.heroyang.mapping.MappingRegistry;
import com.heroyang.mdl.ConfigFile;
import com.heroyang.mdl.Mapping;
import com.heroyang.util.ConfigUtil;
import com.heroyang.util.FmppUtil;


public class Generator {
	
	private static Logger log = Logger.getLogger(Generator.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
//			String[] args = new String[2];
//			args[0] = "E:/git/local/src/test/mock/project_config.cfg";
//			args[1] = "http://l.mail.163.com/demo/main.jsp";
			// args[2] = null;
			String configF = args[0].trim();
			File file = new File(configF);
			if (!file.exists()) {
				throw new IOException("找不到配置文件：" + file);
			}
			ConfigFile config = ConfigUtil.parseConfigFile(file);

			String fullPath = args[1];
			String fullQuery = null;
			if(args.length == 3) {
				fullQuery = args[2];
			}

			Mapping map = MappingRegistry.mapping(config, fullPath, fullQuery);

			if (map == Mapping.NoMapping) {
				 // 没有找到匹配项
				 throw new ConfigurationException("找不到匹配项：" + fullPath);
			}
			String outputFile = null;
			if ("true".equalsIgnoreCase(map.getConfig().getScript())) {
				// 检查下ftl文件
				String ftlFile = ConfigUtil.combine(map.getConfig().getSourceRoot(), map.getConfig().getFtlFileName());
				if (!new File(ftlFile).exists()) {
					throw new IOException("找不到ftl文件：" + ftlFile);
				}
				outputFile = ConfigUtil.combine(
						map.getConfig().getOutputRoot(), map.getConfig()
								.getOutputFileName());
				// delete outputFile
				try {
//					System.out.println("delete: " + outputFile);
					new File(outputFile).delete();
				} catch (Exception ex) {
					// delete if exist
				}
				// Fmpp生成文件
				try {
					FmppUtil.generateFileDirectly(map);
					System.out.println("ftl generate complete");
				} catch (Exception ex) {
					// fmpp生成失败，但是文件还是会生成.
					log.error("generate ftl file failed: " + ex.getMessage(), ex);
					System.out.println("ftl generate failed");
					ex.printStackTrace();
				}
			}
		} catch (Exception e) {
			log.error("generate direct fail: " +e .getMessage(), e);
			e.printStackTrace();
		}
	}

}