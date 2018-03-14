package com.business.dtc.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author: xudy
 * @date: 2018/03/14 13:49
 * @description:
 */
public class LoginFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		String projectName = request.getContextPath();
		// 获得用户请求的URI
		String path = request.getRequestURI();
		// 登陆页面过滤 css,js,images
		if (path.indexOf("/login.jsp") > -1 || path.contains("/css/") || path.contains("/js/") ||path.contains("/images/")) {
			filterChain.doFilter(servletRequest, servletResponse);
			return;
		}
		HttpSession session = request.getSession();
        response.sendRedirect(projectName+"/page/login.jsp");
	}

	@Override
	public void destroy() {

	}
}