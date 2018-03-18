package com.business.dtc.util;

import java.util.List;
import java.util.Map;

import net.sf.rose.jdbc.PageBean;
import net.sf.rose.jdbc.dao.BeanDAO;
import net.sf.rose.jdbc.dao.MapDAO;
import net.sf.rose.jdbc.query.BeanSQL;
import net.sf.rose.jdbc.query.MapSQL;
import net.sf.rose.jdbc.service.Service;
import org.apache.poi.ss.formula.functions.T;

/**
 * @创建人: xudy
 * @创建日期: 2017/08/16 16:44
 * @类描述: 自定义封装工具类
 */
public class DBTools {

	public static Map<String, Object> getData(Service service, String sql, Object... args) {
		MapDAO dao = new MapDAO(service);
		MapSQL query = dao.getQuerySQL();
		query.setSQL(sql);
		for (Object o : args) {
			query.addParameter(o);
		}
		Map<String, Object> data = dao.get();
		return data;
	}

	public static List<Map<String, Object>> getDataList(Service service, String sql, List<Object> condition) {
		MapDAO dao = new MapDAO(service);
		MapSQL query = dao.getQuerySQL();
		query.setSQL(sql);
		query.setParameters(condition);
		List<Map<String, Object>> data = dao.list();
		return data;
	}

	public static List<Map<String, Object>> getDataList(Service service, String sql, Object... args) {
		MapDAO dao = new MapDAO(service);
		MapSQL query = dao.getQuerySQL();
		query.setSQL(sql);
		for (Object o : args) {
			query.addParameter(o);
		}
		List<Map<String, Object>> data = dao.list();
		return data;
	}

	public static PageBean getDataList(Service service, String sql, PageBean page, List<Object> condition) {
		MapDAO dao = new MapDAO(service);
		MapSQL query = dao.getQuerySQL();
		query.setSQL(sql);
		query.setParameters(condition);
		query.setPage(page);
		dao.list();
		return page;
	}

	public static PageBean getDataList(Service service, String sql, PageBean page, Object... args) {
		MapDAO dao = new MapDAO(service);
		MapSQL query = dao.getQuerySQL();
		query.setSQL(sql);
		for (Object o : args) {
			query.addParameter(o);
		}
		query.setPage(page);
		dao.list();
		return page;
	}

	public static <T> T getBean(Service service, Class<?> entity, String sql, List<Object> condition) {
		BeanDAO dao = new BeanDAO(service);
		BeanSQL query = dao.getQuerySQL();
		query.setEntityClass(entity);
		query.setSQL(sql);
		query.setParameters(condition);
		return dao.get();
	}

	public static <T> T getBean(Service service, Class<?> entity, String sql, Object... args) {
		BeanDAO dao = new BeanDAO(service);
		BeanSQL query = dao.getQuerySQL();
		query.setEntityClass(entity);
		query.setSQL(sql);
		for (Object o : args) {
			query.addParameter(o);
		}
		return dao.get();
	}

	public static <T> List<T> getBeanList(Service service, Class<?> entity, String sql) {
		BeanDAO dao = new BeanDAO(service);
		BeanSQL query = dao.getQuerySQL();
		query.setEntityClass(entity);
		query.setSQL(sql);
		return dao.list();
	}

	public static <T> List<T> getBeanList(Service service, Class<?> entity, String sql, List<Object> condition) {
		BeanDAO dao = new BeanDAO(service);
		BeanSQL query = dao.getQuerySQL();
		query.setEntityClass(entity);
		query.setSQL(sql);
		query.setParameters(condition);
		return dao.list();
	}

	public static <T> List<T> getBeanList(Service service, Class<?> entity, String sql, Object... args) {
		BeanDAO dao = new BeanDAO(service);
		BeanSQL query = dao.getQuerySQL();
		query.setEntityClass(entity);
		query.setSQL(sql);
		for (Object o : args) {
			query.addParameter(o);
		}
		return dao.list();
	}

	public static PageBean getPageList(Service service, Class<?> entity, String sql, PageBean page, List<Object> condition) {
		BeanDAO dao = new BeanDAO(service);
		BeanSQL query = dao.getQuerySQL();
		query.setEntityClass(entity);
		query.setSQL(sql);
		query.setParameters(condition);
		query.setPage(page);
		dao.list();
		return page;
	}

	public static PageBean getPageList(Service service, Class<?> entity, String sql, PageBean page, Object... args) {
		BeanDAO dao = new BeanDAO(service);
		BeanSQL query = dao.getQuerySQL();
		query.setEntityClass(entity);
		query.setSQL(sql);
		for (Object o : args) {
			query.addParameter(o);
		}
		query.setPage(page);
		dao.list();
		return page;
	}

	public static int updateSql(Service service, String sql, Object... args) {
		BeanDAO dao = new BeanDAO(service);
		BeanSQL query = dao.getQuerySQL();
		query.setSQL(sql);
		for (Object o : args) {
			query.addParameter(o);
		}
		return dao.update();
	}

	public static int insertBean(Service service, Object bean) {
		BeanDAO dao = new BeanDAO(service);
		BeanSQL query = dao.getQuerySQL();
		query.createInsertSql(bean);
		return dao.update();
	}

    public static int[] bacthInsertBean(Service service, Class<?> entity,List<?> list) {
        BeanDAO dao = new BeanDAO(service);
        BeanSQL query = dao.getQuerySQL();
        query.setEntityClass(entity);
        query.createBatchInsertSql(list);
        return dao.batchUpdate();
    }

	public static int deleteBean(Service service, Object bean) {
		BeanDAO dao = new BeanDAO(service);
		BeanSQL query = dao.getQuerySQL();
		query.createDeleteSql(bean);
		return dao.update();
	}

	public static int deleteBean(Service service, Class<?> entity, String id) {
		BeanDAO dao = new BeanDAO(service);
		BeanSQL query = dao.getQuerySQL();
		query.createDeleteSql(entity, id);
		return dao.update();
	}
}