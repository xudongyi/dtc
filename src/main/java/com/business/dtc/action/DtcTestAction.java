package com.business.dtc.action;

import javax.servlet.http.HttpServletRequest;

import net.sf.rose.jdbc.service.Service;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.business.dtc.bean.DtcTestBean;


/**
 * @author: xudy
 * @date: 2018/03/14 16:46
 * @description:
 */
@RestController
@RequestMapping("/dtc/test")
public class DtcTestAction extends BaseAction{

    /**
     * 获取当前正在进行的测试
     * @param service
     * @param request
     * @return
     */
    @RequestMapping("/currentTest.do")
    public DtcTestBean currentTest(Service service, HttpServletRequest request){


        return null;
    }

    /**
     * 没有进行的测试,可以创建新的测试
     * @param service
     * @param request
     * @return
     */
    @RequestMapping("/createTest.do")
    public boolean createTest(Service service,HttpServletRequest request){
        return false;
    }
}