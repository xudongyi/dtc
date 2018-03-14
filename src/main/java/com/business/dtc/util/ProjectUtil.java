package com.business.dtc.util;

import javax.servlet.http.HttpServletRequest;

import com.business.dtc.bean.DtcCenterBean;

/**
 * @author: xudy
 * @date: 2018/03/14 14:35
 * @description:
 */
public class ProjectUtil {

	/**
	 * 登录后将信息存放到session当中(设置session的过期时间为1天,这样可以一天不用刷新页面)
	 * 
	 * @param request
	 * @return
	 */
	public static DtcCenterBean getCurrentLoginInfo(HttpServletRequest request) {
		DtcCenterBean info = (DtcCenterBean) request.getSession().getAttribute("user");
		return info;
	}
}