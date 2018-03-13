package net.sf.rose.web.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import net.sf.rose.initial.BootStart;
import net.sf.rose.util.DateFormat;
import sun.misc.BASE64Encoder;

/** 
 * @author fengjian E-mail: 9110530@qq.com 
 * @version 创建时间：2017年2月25日
 * 类说明：Web文件上传下载工具类
 */
public class WebFile {

	private static final Logger LOG = Logger.getLogger(WebFile.class);

	/** 附件保存根目录路径 */
	private static File ROOT_PATH = null;

	static {
		ROOT_PATH = BootStart.getInstance().getWorkSpace();
		if (!ROOT_PATH.exists()) {
			ROOT_PATH.mkdirs();
		}
	}

	/**
	 * 上传附件
	 * @param request - Http服务请求
	 * @return 已保存的文件属性列表清单
	 */
	public static List<FileBean> upload(HttpServletRequest request) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		// 在根目录下创建年月日子文件夹
		String ymd = DateFormat.format(new Date(), "yyyyMMdd");
		File path = new File(ROOT_PATH, ymd);
		if (!path.exists()) {
			path.mkdirs();
		}
		UUID uuid = null;
		String fileName = null;
		List<FileBean> list = new ArrayList<>();
		try {
			for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
				uuid = UUID.randomUUID();
				MultipartFile mf = entity.getValue();
				fileName = mf.getOriginalFilename();// 获取原文件名
				// 文件名是时分秒
				String name = uuid + fileName.substring(fileName.lastIndexOf("."));
				name = DateFormat.format(new Date(), "HHmmss") + '_' + name;
				File uploadFile = new File(path, name);
				FileCopyUtils.copy(mf.getBytes(), uploadFile);
				FileBean bean = new FileBean();
				bean.setName(fileName);
				bean.setUuid(uuid.toString());
				bean.setPath(uploadFile.getPath());
				list.add(bean);
			}
		} catch (IOException e) {
			LOG.error("上传附件失败，" + e.getMessage(), e);
		}
		return list;
	}

	/**
	 * 获取图片文件base64位编码
	 * @param path - 图片文件全路径
	 * @return
	 */
	public static String getImage(String path) {
		try {
			// 读取图片
			File file = new File(path);
			byte[] img_data = null;
			// 读取图片字节数组
			FileInputStream in = new FileInputStream(file);
			img_data = new byte[in.available()];
			in.read(img_data);
			in.close();
			// 对字节数组Base64编码
			BASE64Encoder encoder = new BASE64Encoder();
			return encoder.encode(img_data);
		} catch (IOException e) {
			LOG.error("读取文件失败！\n" + e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 获取音频文件
	 * @param request
	 * @param response
	 * @param path
	 */
	public static void getAudio(HttpServletRequest request, HttpServletResponse response, String path) {
		File server_file = new File(path);
		FileInputStream fis = null;
		OutputStream os = null;
		try {
			long length = server_file.length();
			String range = request.getHeader("Range");
			if (range != null) {
				String[] rs = range.split("\\=");
				range = rs[1].split("\\-")[0];
			} else {
				range = "0";
			}
			response.reset();
			response.addHeader("Accept-Ranges", "bytes");
			response.addHeader("Content-Length", length + "");
			response.addHeader("Content-Range", "bytes " + range + "-" + length + "/" + length);
			response.addHeader("Content-Type", "audio/mpeg;charset=UTF-8");
			fis = new FileInputStream(server_file);
			byte[] readByte = new byte[1024];
			os = response.getOutputStream();
			while (fis.read(readByte) > 0) {
				os.write(readByte);
			}
			os.flush();
		} catch (Exception e) {
			LOG.error("音频文件输出流错误", e);
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					LOG.error(e);
				}
			}
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					LOG.error(e);
				}
			}
		}
	}

	/**
	 * 下载文件
	 * @param response - Http呼应服务
	 * @param bean - 文件属性对象
	 */
	public static void download(HttpServletResponse response, FileBean bean) {
		File file = new File(bean.getPath());
		try {
			InputStream in = new FileInputStream(file);
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				bout.write(buf, 0, len);
			}
			in.close();
			response.reset();
			// String fileName = URLEncoder.encode(filename, "UTF-8");
			String fileName = new String(bean.getName().getBytes(),"ISO8859-1");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream; charset=UTF-8");
			toClient.write(bout.toByteArray());
			toClient.flush();
			toClient.close();
		} catch (Exception e) {
			LOG.error("下载文件失败！", e);
		}
	}

	/**
	 * 下载文件
	 * @param response - Http呼应服务
	 * @param path - 文件路径
	 */
	public static void download(HttpServletResponse response, String path) {
		File file = new File(path);
		try {
			InputStream in = new FileInputStream(file);
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				bout.write(buf, 0, len);
			}
			in.close();
			response.reset();
			// String fileName = URLEncoder.encode(filename, "UTF-8");
			String fileName = new String(file.getName().getBytes(),"ISO8859-1");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream; charset=UTF-8");
			toClient.write(bout.toByteArray());
			toClient.flush();
			toClient.close();
		} catch (Exception e) {
			LOG.error("下载文件失败！", e);
		}
	}
}
