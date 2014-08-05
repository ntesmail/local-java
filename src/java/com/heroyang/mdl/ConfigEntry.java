package com.heroyang.mdl;

/**
 * 
 * @author Hero
 * 
 */
public class ConfigEntry {
	public ConfigEntry(){
	}
	public String getHttpUrl() {
		return HttpUrl;
	}
	public void setHttpUrl(String httpUrl) {
		HttpUrl = httpUrl;
	}
	public String getHttpUrlRoot() {
		return HttpUrlRoot;
	}
	public void setHttpUrlRoot(String httpUrlRoot) {
		HttpUrlRoot = httpUrlRoot;
	}
	public String getOutputFileName() {
		return OutputFileName;
	}
	public void setOutputFileName(String outputFileName) {
		OutputFileName = outputFileName;
	}
	public String getScript() {
		return Script;
	}
	public void setScript(String script) {
		Script = script;
	}
	public String getFtlFileName() {
		return FtlFileName;
	}
	public void setFtlFileName(String ftlFileName) {
		FtlFileName = ftlFileName;
	}
	public String getSourceRoot() {
		return SourceRoot;
	}
	public void setSourceRoot(String sourceRoot) {
		SourceRoot = sourceRoot;
	}
	public String getOutputRoot() {
		return OutputRoot;
	}
	public void setOutputRoot(String outputRoot) {
		OutputRoot = outputRoot;
	}
	public String getData() {
		return Data;
	}
	public void setData(String data) {
		Data = data;
	}
	public String getReplaceExtentions() {
		return ReplaceExtentions;
	}
	public void setReplaceExtentions(String replaceExtentions) {
		ReplaceExtentions = replaceExtentions;
	}
	public String getHeaders() {
		return Headers;
	}
	public void setHeaders(String headers) {
		Headers = headers;
	}
	public boolean isSupportQuery() {
		return SupportQuery;
	}
	public void setSupportQuery(boolean supportQuery) {
		SupportQuery = supportQuery;
	}
	/**
	 * @return the matchQuery
	 */
	public String getMatchQuery() {
		return MatchQuery;
	}
	/**
	 * @param matchQuery the matchQuery to set
	 */
	public void setMatchQuery(String matchQuery) {
		MatchQuery = matchQuery;
	}
	/**
	 * 正则匹配的url
	 */
	private String HttpUrl;

	/**
	 * 正则匹配的url的根目录，用于静态文件的目录匹配
	 */
	private String HttpUrlRoot = "";
	/**
	 * 文件名，一般同ftl文件名，不同后缀
	 */
	private String OutputFileName;
	/**
	 * 是否包含脚本
	 */
	private String Script;
	/**
	 * 脚本中的对应的ftl文件名，包括路径
	 */
	private String FtlFileName;
	/**
	 * 脚本中的sourceroot
	 */
	private String SourceRoot = "";
	/**
	 * 脚本中的outputroot
	 */
	private String OutputRoot = "";
	/**
	 * 脚本中的data
	 */
	private String Data;
	/**
	 * 脚本中的replaceExtentions
	 */
	private String ReplaceExtentions;
	/**
	 * 返回的Head增加属性
	 */
	private String Headers;
	
	/**
	 * 是否支持参数方式填充返回内容.
	 */
	private boolean SupportQuery;
	
	/**
	 * 需要匹配的query参数
	 */
	private String MatchQuery;

	public ConfigEntry clone() {
		ConfigEntry entry = new ConfigEntry();
		entry.setData(this.getData());
		entry.setFtlFileName(this.getFtlFileName());
		entry.setHeaders(this.getHeaders());
		entry.setHttpUrl(this.getHttpUrl());
		entry.setHttpUrlRoot(this.getHttpUrlRoot());
		entry.setMatchQuery(this.getMatchQuery());
		entry.setOutputFileName(this.getOutputFileName());
		entry.setOutputRoot(this.getOutputRoot());
		entry.setReplaceExtentions(this.getReplaceExtentions());
		entry.setScript(this.getScript());
		entry.setSourceRoot(this.getSourceRoot());
		entry.setSupportQuery(this.isSupportQuery());
		return entry;
	}
	
	public String toString(){
		StringBuilder b = new StringBuilder();
		b.append("HttpUrl:").append(HttpUrl).append("\r\n");
		b.append("HttpUrlRoot:").append(HttpUrlRoot).append("\r\n");
		b.append("OutputFileName:").append(OutputFileName).append("\r\n");
		b.append("Script:").append(Script).append("\r\n");
		b.append("FtlFileName:").append(FtlFileName).append("\r\n");
		b.append("SourceRoot:").append(SourceRoot).append("\r\n");
		b.append("OutputRoot:").append(OutputRoot).append("\r\n");
		b.append("Data:").append(Data).append("\r\n");
		b.append("ReplaceExtentions:").append(ReplaceExtentions).append("\r\n");
		b.append("Headers:").append(Headers).append("\r\n");
		b.append("SupportQuery:").append(SupportQuery).append("\r\n");
		b.append("MatchQuery:").append(MatchQuery).append("\r\n");
		return b.toString();
	}

}
