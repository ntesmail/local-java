package com.heroyang.mapping;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.heroyang.mdl.ConfigEntry;
import com.heroyang.mdl.ConfigFile;
import com.heroyang.mdl.Mapping;

public class MappingRegistry {

	/**
	 * url mapping.
	 * @param config
	 * @param fullPath
	 * @return
	 */
	public static Mapping mapping(ConfigFile config, String fullPath, String fullQuery) {
		if (config.getEntries() != null) {
			for (ConfigEntry entry : config.getEntries()) {
				Pattern pattern = Pattern.compile(entry.getHttpUrl());
				Matcher matcher = pattern.matcher(fullPath);
				
				Matcher matcher2 = null;
				// 是否匹配
				if (matcher.matches()) {
					// 检查是否query参数匹配
					if(entry.getMatchQuery() != null) {
						String mqStr = ".*" + entry.getMatchQuery() + ".*";
						Pattern pattern2 = Pattern.compile(mqStr);
						matcher2 = pattern2.matcher(fullQuery);
						if(!matcher2.matches()) {
							System.out.println(fullQuery +" not matched: " + mqStr);
							continue;
						}
					}
					// 匹配到
					Mapping map = new Mapping();
					map.setBaseConfig(config);

					entry.setSourceRoot(format(entry.getSourceRoot(), matcher, matcher2));
					entry.setOutputFileName(format(entry.getOutputFileName(), matcher, matcher2));
					entry.setFtlFileName(format(entry.getFtlFileName(), matcher, matcher2));
					entry.setData(format(entry.getData(), matcher, matcher2));
					entry.setOutputRoot(format(entry.getOutputRoot(), matcher, matcher2));
					entry.setHttpUrlRoot(format(entry.getHttpUrlRoot(), matcher, matcher2));
					
					map.setConfig(entry);
					return  map;
				}

			}
		}
		return Mapping.NoMapping;
	}
	// 替换起来
	public static String format(String content, Matcher matcher, Matcher matcher2) {
		if(content == null) {
			return content;
		}
		if(matcher != null) {
			for(int i = matcher.groupCount(); i>0; i--) {
				content = content.replace("{" + i +"}", matcher.group(i));
			}
		}
		if(matcher2 != null) {
			for(int i = matcher2.groupCount(); i>0; i--) {
				content = content.replace("{Q" + i +"}", matcher2.group(i));
			}
		}
		return content;
	}
}
