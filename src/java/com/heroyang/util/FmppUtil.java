package com.heroyang.util;

import org.apache.tools.ant.Project;

import com.heroyang.mdl.Mapping;

import fmpp.tools.AntTask;

public final class FmppUtil {

	private FmppUtil(){
	}
	
	public static void generateFileDirectly(Mapping map) {
		// fmpp生成
		AntTask task = new AntTask();
		// fmpp config
		Project project = new Project();
		project.setName("main");
		project.setBasedir(map.getBaseConfig().getProjectBaseDir());

		task.setProject(project);

		java.io.File sourceRoot = new java.io.File(map.getConfig().getSourceRoot());
		task.setSourceRoot(sourceRoot);

		task.setIncludes(map.getConfig().getFtlFileName());

		java.io.File outputRoot = new java.io.File(map.getConfig().getOutputRoot());
		task.setOutputRoot(outputRoot);
		
		// 文件名后缀
		if(map.getConfig().getFtlFileName() != null && map.getConfig().getOutputFileName() != null) {
			String ftlExt = map.getConfig().getFtlFileName().split("\\.")[1];
			String outExt = map.getConfig().getOutputFileName().split("\\.")[1];
			
			task.setReplaceExtensions(ftlExt + ","+ outExt);
		} else {
			task.setReplaceExtensions(map.getConfig().getReplaceExtentions());
		}
		// base + tdd
		String[] tdds = map.getConfig().getData().split(",");
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < tdds.length; i++)
		{
		    String path =  ConfigUtil.combine(map.getBaseConfig().getProjectBaseDir(), tdds[i]);
		    builder.append("tdd(").append(path).append(")");
		    if (i < tdds.length - 1)
		    {
		        builder.append(",");
		    }
		}
		task.setData(builder.toString());
		task.execute();
	}
}
