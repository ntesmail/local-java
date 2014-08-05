package com.heroyang;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Map;

import com.heroyang.mapping.MappingRegistry;
import com.heroyang.mdl.ConfigFile;
import com.heroyang.mdl.Mapping;
import com.heroyang.util.ConfigUtil;
import com.heroyang.util.FmppUtil;
import com.heroyang.util.HttpUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class LocalDevelop {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			if (args == null || args.length == 0) {
				System.out.println("请输入配置文件地址。");
				System.out
						.println("例如： java -jar local-1.0.0.jar E:/git/young/src/test/mock/project_config.cfg 8081");

				/** for test **/
//				args = new String[] {"E:/git/young/src/test/mock/project_config.cfg", "8081" };
				/** for test **/
			}
			String configF = args[0].trim();
			File file = new File(configF);
			if (!file.exists()) {
				throw new IOException("找不到配置文件：" + file);
			}
			ConfigFile config = ConfigUtil.parseConfigFile(file);
			// 默认80端口
			int port = 80;

			if (args.length > 1) {
				port = Integer.parseInt(args[1]);
			}

			HttpServer hs = HttpServer.create(new InetSocketAddress(port), 0);// 设置HttpServer的端口为8888
			hs.createContext("/", new MyHandler(configF, config));// 用MyHandler类内处理到/chinajash的请求
			hs.setExecutor(null); // creates a default executor
			hs.start();
			System.out.println("Local server started successfully on port: "
					+ port);
		} catch (IOException e) {
			e.printStackTrace();
			try {
				System.in.read();
			} catch (IOException e1) {
				// ignore
			}
		}
	}

}

class MyHandler implements HttpHandler {

	private ConfigFile config;
	private String configF;
	private long configTime;

	public MyHandler(String configF, ConfigFile config) {
		this.configF = configF;
		this.config = config;
		this.configTime = System.currentTimeMillis();
	}

	public void handle(HttpExchange t) throws IOException {
		try {
			long start = System.currentTimeMillis();
			String fullPath = HttpUtil.getUrl(t);
			String fullQuery = HttpUtil.getQueryString(t);
			Map<String, String> querys = HttpUtil.getQuery(t);
			// if("/favicon.ico".equals(t.getRequestURI().getPath())) {
			// return;
			// }
			System.out.println(fullPath);

			// 10秒缓存
			if (System.currentTimeMillis() - this.configTime > 10 * 1000) {
				this.configTime = System.currentTimeMillis();
				File file = new File(configF);
				if (!file.exists()) {
					throw new IOException("找不到配置文件：" + file);
				}
				this.config = ConfigUtil.parseConfigFile(file);
			}
			// 配置
			ConfigFile config = this.config.clone();
			Mapping map = MappingRegistry.mapping(config, fullPath, fullQuery);
			if (map == Mapping.NoMapping) {
				// 没有找到匹配项
				throw new ConfigurationException("找不到匹配项：" + fullPath);
			}
			String outputFile = null;
			if ("true".equalsIgnoreCase(map.getConfig().getScript())) {
				outputFile = ConfigUtil.combine(
						map.getConfig().getOutputRoot(), map.getConfig()
								.getOutputFileName());
				// delete outputFile
				try {
					new File(outputFile).deleteOnExit();
				} catch (Exception ex) {
					// delete if exist
				}
				// Fmpp生成文件
				try {
					FmppUtil.generateFileDirectly(map);
				} catch (Exception ex) {
					// fmpp生成失败，但是文件还是会生成.
					System.err.println(ex.getMessage());
				}
			} else {
				if (map.getConfig().getOutputFileName() != null
						&& map.getConfig().getOutputFileName().length() > 0) {
					outputFile = ConfigUtil.combine(map.getConfig()
							.getOutputRoot(), map.getConfig()
							.getOutputFileName());
				} else {
					String path = fullPath.substring(map.getConfig()
							.getHttpUrlRoot().length());
					outputFile = ConfigUtil.combine(map.getConfig()
							.getOutputRoot(), path);
				}
			}

			if (!new File(outputFile).exists()) {
				// 返回404
				// 文件不存在
				System.out.println("找不到OutputFile：" + outputFile);

				t.sendResponseHeaders(404, 0);
				OutputStream os = t.getResponseBody();
				os.write(null);
				os.close();
			} else {
				System.out.println(outputFile);
			}

			System.out.println("parse cost: "
					+ (System.currentTimeMillis() - start) + "ms");
			// 处理parameter
			byte[] array = null;
			if (map.getConfig().isSupportQuery()) {
				array = HttpUtil.getBytes(outputFile, querys);
			} else {
				array = HttpUtil.getBytes(outputFile, null);
			}
			if (array == null || array.length == 0) {
				// 文件读取失败
				throw new ConfigurationException("读取文件内容失败：" + outputFile);
			}

			String head = map.getConfig().getHeaders();
			if (head != null && head.trim().length() > 0) {
				String[] headers = head.split("\\|");
				for (String h : headers) {
					int firstIndex = h.indexOf(':');
					if (firstIndex > 0 && firstIndex < h.length()) {
						String key = h.substring(0, firstIndex);
						String val = h.substring(firstIndex + 1);
						t.getResponseHeaders().set(key, val);
					}
				}
			}
			// t.getResponseHeaders().set(arg0, arg1)
			t.sendResponseHeaders(200, array.length);
			OutputStream os = t.getResponseBody();
			os.write(array);
			os.close();
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
			t.getResponseBody().close();
		}
	}

}
