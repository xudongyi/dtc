package net.sf.rose.web.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.rose.jdbc.PageBean;
import net.sf.rose.util.StringUtil;

/** 
 * @author fengjian E-mail: 9110530@qq.com 
 * @version 创建时间：2016年10月2日
 * 类说明：Web服务通用工具类
 */
public class WebUtils {

	private static final String ENCODING_DEFAULT = "UTF-8";
	private static final String TEXT_TYPE = "text/plain";
	private static final String JSON_TYPE = "application/json";
	private static final String XML_TYPE = "text/xml";
	private static final String HTML_TYPE = "text/html";

	/**
	 * 从Http请求中获取所有的请求参数，转化为Map类型
	 * @param request - Http请求
	 * @return 转化后的键值对
	 */
	public static Map<String, Object> getRequestData(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Object val = null;
		String[] arr = null;
		Map<?, ?> m = request.getParameterMap();
		for (Object o : m.keySet()) {
			arr = (String[]) m.get(o);
			// 如果是数组
			if (arr.length > 1) {
				map.put(o.toString(), arr);
			} else {
				val = arr[0];
				// 如果不是空值
				if (!val.toString().trim().equals("")) {
					val = arr[0];
					map.put(o.toString(), val);
				}
			}
		}
		return map;
	}

	/**
	 * 获取分页对象
	 * @param request - Http请求
	 * @return 分页对象
	 */
	public static PageBean getPageBean(HttpServletRequest request) {
		PageBean page = new PageBean();
		String pageIndex = request.getParameter("pageIndex");
		String pageSize = request.getParameter("pageSize");
		page.setPageIndex(pageIndex == null ? 0 : Integer.parseInt(pageIndex));
		page.setpageSize(pageSize == null ? 0 : Integer.parseInt(pageSize));
		return page;
	}

	/**
	 * 将json格式的字符串请求参数转化为Map类型
	 * @param json - json请求参数
	 * @return 转化后的键值对
	 */
	public static Map<String, String> getRequestData(String json) {
		Map<String, String> map = new HashMap<String, String>();
		String val = null;
		JSONObject js = JSONObject.fromObject(json);
		for (Object key : js.keySet()) {
			val = js.get(key).toString();
			if (!val.equals("")) {
				map.put(key.toString(), val);
			}
		}
		return map;
	}

	/**
	 * 返回字符串给Http响应
	 * @param response - Http响应
	 * @param contentType - 头文件类型
	 * @param content - 文本内容
	 * @throws IOException - 异常
	 */
	public static void render(HttpServletResponse response, String contentType, final String content) throws IOException {
		// 设置headers参数
		String fullContentType = contentType + ";charset=" + ENCODING_DEFAULT;
		response.setContentType(fullContentType);

		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);

		response.getWriter().write(content);
		response.getWriter().flush();
	}

	/**
	 * 直接输出文本.
	 * @param response - Http响应
	 * @param text - 文本内容
	 * @throws IOException - 异常
	 */
	public static void renderText(HttpServletResponse response, String text) throws IOException {
		render(response, TEXT_TYPE, text);
	}

	/**
	 * 直接输出HTML.
	 * @param response - Http响应
	 * @param html - html文件正文
	 * @throws IOException - 异常
	 */
	public static void renderHtml(HttpServletResponse response, String html) throws IOException {
		render(response, HTML_TYPE, html);
	}

	/**
	 * 直接输出XML.
	 * @param response - Http响应
	 * @param xml - xml文件正文
	 * @throws IOException - 异常
	 */
	public static void renderXml(HttpServletResponse response, String xml) throws IOException {
		render(response, XML_TYPE, xml);
	}

	/**
	 * 直接输出JSON.
	 * @param response - Http响应
	 * @param json - Json格式正文
	 * @throws IOException - 异常 
	 */
	public static void renderJson(HttpServletResponse response, String json) throws IOException {
		render(response, JSON_TYPE, json);
	}

	/**
	 * 直接输出JSON. 
	 * @param response - Http响应
	 * @param object - Java对象,将被转化为json字符串.
	 * @throws IOException - 异常
	 */
	public static void renderJson(HttpServletResponse response, Object object) throws IOException {
		String jsonString = StringUtil.render(object);
		render(response, JSON_TYPE, jsonString);
	}

	/**
	 * 输出图片等二进制格式
	 * @param response - Http响应
	 * @param data - 字节流
	 * @param type - 类型
	 * @throws IOException - 异常
	 */
	public static void renderImage(HttpServletResponse response, byte[] data, String type) throws IOException {
		response.setContentType(type);
		OutputStream out = response.getOutputStream();
		out.write(data, 0, data.length);
		out.flush();
		out.close();
	}
}
