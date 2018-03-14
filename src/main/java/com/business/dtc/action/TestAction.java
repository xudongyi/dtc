package com.business.dtc.action;

import net.sf.rose.jdbc.service.Service;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * @author: xudy
 * @date: 2018/03/13 17:50
 * @desction:
 */
@Controller
@RequestMapping("test")
public class TestAction {
    @RequestMapping("test.do")
    @ResponseBody
    public Date test(Service service){
        System.out.println(service.getDataSourceName());
        return new Date();
    }
}