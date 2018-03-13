package net.sf.rose.web.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import net.sf.rose.jdbc.PageBean;
import net.sf.rose.jdbc.dao.BeanDAO;
import net.sf.rose.jdbc.dao.GenericDAO;
import net.sf.rose.jdbc.dao.MapDAO;
import net.sf.rose.jdbc.query.BeanSQL;
import net.sf.rose.jdbc.query.MapSQL;
import net.sf.rose.jdbc.query.QuerySQL;
import net.sf.rose.jdbc.service.Service;
import net.sf.rose.web.utils.WebUtils;

/** 
 * @author fengjian E-mail: 9110530@qq.com 
 * @version 创建时间：2016年12月01日
 * 类说明：通用Web控制器类
 */
@Controller
@RequestMapping("/com")
public class CommonAction {

	/**
	 * 执行数据库查询操作
	 * @param request     - Http请求
	 * @param response    - Http返回
	 * @param service     - 数据库连接服务类
	 * @param entityClass - 查询返回的数据类
	 */
	@RequestMapping("/get.html")
	@ResponseBody
	public Object get(HttpServletRequest request, HttpServletResponse response, Service service, 
			@RequestParam(value = "entityClass", required = false) String entityClass, 
			@RequestParam(value = "tableName", required = false) String tableName) throws Exception {
		Map<String, Object> map = WebUtils.getRequestData(request);
		GenericDAO dao = null;
		QuerySQL query = null;
		if (entityClass != null && entityClass.equals("")) {
			dao = new BeanDAO(service);
			query = dao.getQuerySQL();
			query.setEntityClass(Class.forName(entityClass));
		}
		else if (tableName != null && !tableName.equals("")) {
			dao = new MapDAO(service);
			query = dao.getQuerySQL();
			query.setTableName(tableName);
		}
		else {
			throw new RuntimeException("list方法参数缺失！，entityClass或tableName不能为空。");
		}
		query.createSql(map);
		return dao.get();
	}

	/**
	 * 执行数据库查询操作
	 * @param request     - Http请求
	 * @param response    - Http返回
	 * @param service     - 数据库连接服务类
	 * @param entityClass - 查询返回的数据类
	 * @param pageIndex   - 分页查询用的第N页
	 * @param pageSize	  - 分页查询用的每页页数
	 */
	@RequestMapping("/list.html")
	public void list(HttpServletRequest request, HttpServletResponse response, Service service, 
			@RequestParam(value = "entityClass", required = false) String entityClass, 
			@RequestParam(value = "tableName", required = false) String tableName, 
			@RequestParam(value = "orderby", required = false) String orderby, 
			@RequestParam(value = "pageIndex", required = false) int pageIndex, 
			@RequestParam(value = "pageSize", required = false) int pageSize) throws Exception {
		Map<String, Object> map = WebUtils.getRequestData(request);
		GenericDAO dao = null;
		QuerySQL query = null;
		if (entityClass != null && entityClass.equals("")) {
			dao = new BeanDAO(service);
			query = dao.getQuerySQL();
			query.setEntityClass(Class.forName(entityClass));
		}
		else if (tableName != null && !tableName.equals("")) {
			dao = new MapDAO(service);
			query = dao.getQuerySQL();
			query.setTableName(tableName);
		}
		else {
			throw new RuntimeException("list方法参数缺失！，entityClass或tableName不能为空。");
		}
		// 设置排序
		if (!(orderby == null || orderby.equals(""))) {
			query.addOrderby(orderby);
		}
		// 如果不分页
		if (pageSize == 0) {
			query.createSql(map);
			WebUtils.renderJson(response, dao.list());
		}
		// 如果分页
		else {
			PageBean page = new PageBean();
			page.setPageIndex(pageIndex);
			page.setpageSize(pageSize);
			query.setPage(page);
			query.createSql(map);
			dao.list();
			WebUtils.renderJson(response, page);
		}
	}

	/**
	 * 执行数据库更新操作
	 * @param request     - Http请求
	 * @param response    - Http返回
	 * @param service     - 数据库连接服务类
	 * @param entityClass - 查询返回的数据类
	 * @param tableName   - 表名
	 */
	@RequestMapping("/update.html")
	@ResponseBody
	public String update(HttpServletRequest request, HttpServletResponse response, Service service, 
			@RequestParam(value = "entityClass", required = false) String entityClass, 
			@RequestParam(value = "tableName", required = false) String tableName) throws Exception {
		if (entityClass != null && entityClass.equals("")) {
			BeanDAO dao = new BeanDAO(service);
			BeanSQL query = dao.getQuerySQL();
			// 将要处理的类名称转化成数组
			String[] clazzs = null;
			if (entityClass.indexOf(',') != -1) {
				clazzs = StringUtils.split(entityClass);
			} else {
				clazzs = new String[] { entityClass };
			}
			for (String className : clazzs) {
				// 根据类名称从Http请求中查找Json数据
				String json = request.getParameter(className);
				if (json != null) {
					Class<?> clazz = Class.forName(className);
					Object bean = JSON.parseObject(json, clazz);
					query.setEntityClass(clazz);
					json = request.getParameter(className + "_action");
					// 如果指定执行删除操作，则生成删除SQL文
					if (json != null && json.equals("delete")) {
						query.createDeleteSql(bean);
					}
					// 否则生成保存SQL文
					else {
						query.createSaveSql(bean);
					}
					dao.update();
				}
			}
		} else if (tableName != null && !tableName.equals("")) {
			MapDAO dao = new MapDAO(service);
			MapSQL query = dao.getQuerySQL();
			// 将要处理的类名称转化成数组
			String[] tableNames = null;
			if (entityClass.indexOf(',') != -1) {
				tableNames = StringUtils.split(tableName);
			} else {
				tableNames = new String[] { tableName };
			}
			String json = null;
			Map<String, Object> map = WebUtils.getRequestData(request);
			for (String name : tableNames) {
				query.setTableName(name);
				json = request.getParameter(name + "_action");
				if (json == null) {
					throw new RuntimeException("表" + name + "的操作方法参数缺失！，" + name + "_action不能为空。");
				}
				// 如果是插入操作
				if (json.equals("insert")) {
					query.createInsertSql(map);
				}
				// 如果是更新或删除操作
				else {
					// 取更新条件参数
					json = request.getParameter(name + "_condition");
					if (json != null && !json.equals("")) {
						// 将条件Json格式的数据转化成Map参数
						JSONObject jo = JSON.parseObject(json);
						Map<String, Object> condition = new HashMap<String, Object>();
						for (String str : jo.keySet()) {
							condition.put(str, jo.get(str));
						}
						// 如果是更新操作
						if (json.equals("update")) {
							query.createUpdateSql(map);
						}
						// 如果是删除操作
						else if (json.equals("delete")) {
							query.createDeleteSql(condition);
						}
					}
				}
				dao.update();
			}
		} else {
			throw new RuntimeException("list方法参数缺失！，entityClass或tableName不能为空。");
		}
		return "succeed";
	}

}
