package net.sf.rose.web.utils;

/** 
 * @author fengjian E-mail: 9110530@qq.com 
 * @version 创建时间：2017年2月25日
 * 类说明：Web文件上传下载数据类对象
 */
public class FileBean {

	private String uuid = null;
	private String name = null;
	private String path = null;

	/**
	 * 返回uuid
	 * @return
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * 设置uuid
	 * @param uuid
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * 返回文件名称
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置文件名称
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 返回文件全路径
	 * @return
	 */
	public String getPath() {
		return path;
	}

	/**
	 * 设置文件全路径
	 * @param path
	 */
	public void setPath(String path) {
		this.path = path;
	}
}
