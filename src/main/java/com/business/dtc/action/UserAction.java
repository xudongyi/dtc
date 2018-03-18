package com.business.dtc.action;

import javax.servlet.http.HttpServletRequest;

import com.business.dtc.bean.DtcCenterBean;
import com.business.dtc.util.DBTools;
import com.business.dtc.util.ResponseData;
import net.sf.rose.jdbc.DBUtils;
import net.sf.rose.jdbc.UserBean;
import net.sf.rose.jdbc.service.Service;
import net.sf.rose.util.ConstantCode;
import net.sf.rose.util.DateFormat;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: xudy
 * @date: 2018/03/14 16:02
 * @description:
 */
@Controller
@RequestMapping("user")
public class UserAction {

    /**
     * 登录操作
     * @param service
     * @param request
     */
    @RequestMapping("/login.do")
    @ResponseBody
    public ResponseData login(Service service, HttpServletRequest request, String username, String password){
        //1.根据用户名和密码进行校验
        DtcCenterBean bean = DBTools.getBean(service,DtcCenterBean.class,"select * from DTC_CENTER where USER_NAME=? AND PASSWORD=? AND IS_DELETED=1",username,password);
        if(bean==null){
            return ResponseData.buildResponseData(1,"用户名或密码不正确!",null);
        }else{
            if(bean.getUserState()==2){
                return ResponseData.buildResponseData(2,"用户被禁用!",null);
            }
        }
        ResponseData data =  ResponseData.buildResponseData(200,"正常登录",bean);
        UserBean user = new UserBean();
        user.setUserName(bean.getRealName());
        user.setUserID(bean.getRealName());
        user.setLastLoginTime(DateFormat.getTimestamp());
        request.getSession().setAttribute(ConstantCode.USER_BEAN_NAME,user);
        request.getSession().setAttribute("user",bean);
        return data;
    }

    @RequestMapping("/loginOut.do")
    @ResponseBody
    public boolean loginOut(Service service, HttpServletRequest request){
        request.getSession().removeAttribute(ConstantCode.USER_BEAN_NAME);
        request.getSession().removeAttribute("user");
        return true;
    }

    /**
     * 修改密码
     * @param service
     * @param request
     * @param userName
     * @param password
     * @param newpassword
     * @return
     */
    @RequestMapping("/changePwd.do")
    @ResponseBody
    public boolean changePwd(Service service,HttpServletRequest request,String userName,String password,String newpassword){
        DtcCenterBean bean = DBTools.getBean(service,DtcCenterBean.class,"select * from DTC_CENTER where USER_NAME=? AND PASSWORD=? AND IS_DELETED=1",userName,password);
        if(bean==null){
            return false;
        }
        bean.setPassword(newpassword);
        int i = DBUtils.update(service,bean);
        return i>0;
    }
}