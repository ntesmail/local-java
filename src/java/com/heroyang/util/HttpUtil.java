package com.heroyang.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

public final class HttpUtil {

	private HttpUtil() {

	}

	/**
	 * 获取访问的url，去掉port，用于mapping。
	 * 
	 * @param t
	 * @return
	 */
	public static String getUrl(HttpExchange t) {
		String schema = "http://";
		String host = t.getRequestHeaders().get("host").get(0);
		// 去掉port
		host = host.split(":")[0];
		String path = t.getRequestURI().getPath();

		String fullPath = schema + host + path;

		return fullPath;
	}

	public static String getQueryString(HttpExchange t) {
		return t.getRequestURI().getQuery();
	}
	/**
	 * 获取访问的Query的map表
	 * 
	 * @param t
	 * @return
	 */
	public static Map<String, String> getQuery(HttpExchange t) {
		String query = t.getRequestURI().getQuery();
		if(query != null && query.trim().length() > 0) {
			return convertQuery(query);
		}

		return new HashMap<String, String>();
	}

	public static Map<String, String> convertQuery(String query) {
		Map<String, String> map =  new HashMap<String, String>();
		String[] qs = query.split("&");
		for (String q : qs) {
			String[] pair = q.split("=");
			if(pair.length > 1) {
				map.put(pair[0], pair[1]);
			}
		}
		return map;
	}
	

	/**
	 * 获取bytes
	 * @param outputFile
	 * @return
	 */
	public static byte[] getBytes(String outputFile, Map<String, String> queries) {
		
		FileInputStream in = null;
		try {
			in = new FileInputStream(outputFile);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int c;
			byte buffer[] = new byte[1024];
			while ((c = in.read(buffer)) != -1) {
				for (int i = 0; i < c; i++)
					out.write(buffer[i]);
			}
			out.close();
			if(queries != null && queries.size() > 0) {
				String content = out.toString("utf-8");
				for (String key : queries.keySet()) {
					content = content.replaceAll("%" + key + "%", queries.get(key));
				}
				return content.getBytes("utf-8");
			}
			return out.toByteArray();
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
			return new byte[0];
		} finally{
			if(in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
	}
}
