package com.business.dtc.util;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.rose.jdbc.PageBean;

/**
 * 项目名称: platform
 * 创建人: xudy
 * 创建时间: 2016/6/15 10:14
 * 类描述: 一句话描述该类的功能 修改备注: 说明本次修改内容
 */
public class PageUtil {
	/**
	 * 分割list实现分页
	 * 
	 * @param list
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public static List<Object> splitList(List<Object> list, int pageIndex, int pageSize) {
		List<Object> newArrayList = new ArrayList<>();
		int totalCount = list.size();// 总数据
		int pageCount = 0;
		if (totalCount <= pageSize) {
			return list;
		}
		/* 计算出总共能分成多少页 */
		if (totalCount % pageSize > 0) // 数据总数和每页显示的总数不能整除的情况
		{
			pageCount = totalCount / pageSize + 1;
		} else // 数据总数和每页显示的总数能整除的情况
		{
			pageCount = totalCount / pageSize;
		}
		if (totalCount > 0) {
			if (pageIndex <= pageCount) {
				if (pageIndex == 1) // 当前页数为第一页
				{
					if (totalCount <= pageSize) // 数据总数小于每页显示的数据条数
					{
						// 截止到总的数据条数(当前数据不足一页，按一页显示)，这样才不会出现数组越界异常
						newArrayList = list.subList(0, totalCount);
					} else {
						newArrayList = list.subList(0, pageSize);
					}
				} else {
					// 截取起始下标
					int fromIndex = (pageIndex - 1) * pageSize;
					// 截取截止下标
					int toIndex = pageIndex * pageSize;
					/* 计算截取截止下标 */
					if ((totalCount - toIndex) % pageSize >= 0) {
						toIndex = pageIndex * pageSize;
					} else {
						toIndex = (pageIndex - 1) * pageSize + (totalCount % pageSize);
					}
					if (totalCount >= toIndex) {
						newArrayList = list.subList(fromIndex, toIndex);
					}
				}
			}
		}
		return newArrayList;
	}

	/**
	 * @Title: page
	 * @Description: 获取分页对象
	 * @param request
	 * @return PageBean
	 * @throws
	 * @author luys
	 * @date 2016年12月21日 下午4:00:25
	 */
	public static PageBean page(HttpServletRequest request) {
		PageBean page = new PageBean();
		int pageIndex = Integer.parseInt(request.getParameter("pageIndex") == null ? "0" : request.getParameter("pageIndex"));
		int pageSize = Integer.parseInt(request.getParameter("pageSize") == null ? "10" : request.getParameter("pageSize"));
		page.setPageIndex(pageIndex);
		page.setpageSize(pageSize);
		return page;
	}
}
