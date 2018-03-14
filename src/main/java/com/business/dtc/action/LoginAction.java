package com.business.dtc.action;

import javax.servlet.http.HttpServletRequest;

import net.sf.rose.jdbc.UserBean;
import net.sf.rose.jdbc.service.Service;
import net.sf.rose.util.ConstantCode;
import net.sf.rose.util.DateFormat;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: xudy
 * @date: 2018/03/14 16:02
 * @description:
 */
@Controller
@RequestMapping("user")
public class LoginAction {

    /**
     * 登录操作
     * @param service
     * @param request
     */
    @RequestMapping("/login.do")
    @ResponseBody
    public void login(Service service, HttpServletRequest request){
        UserBean user = new UserBean();
        user.setUserName("admin"+",张三");
        user.setLastLoginTime(DateFormat.getTimestamp());
        request.getSession().setAttribute(ConstantCode.USER_BEAN_NAME,user);
    }
}