package com.business.dtc.action;

import javax.servlet.http.HttpServletRequest;

import com.business.dtc.bean.DtcAgeGroupBean;
import com.business.dtc.bean.DtcCenterBean;
import com.business.dtc.util.DBTools;
import com.business.dtc.util.PageUtil;
import net.sf.rose.jdbc.DBUtils;
import net.sf.rose.jdbc.PageBean;
import net.sf.rose.jdbc.service.Service;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: xudy
 * @date: 2018/03/17 21:49
 * @description:基础数据的维护
 */
@RestController
@RequestMapping("base")
public class DtcBaseAction extends  BaseAction{

    /**
     * 年龄列表
     * @param service
     * @param request
     */
    @RequestMapping("group/list.do")
    public void ageGroupList(Service service, HttpServletRequest request){

    }

    /**
     * 保存年龄
     * @param service
     * @param group
     */
    @RequestMapping("group/save.do")
    public void saveGroup(Service service, @ModelAttribute DtcAgeGroupBean group){

    }

    /**
     * 中心列表
     * @param service
     * @param request
     */
    @RequestMapping("center/list.do")
    public List<DtcCenterBean> centerList(Service service, HttpServletRequest request){
        List<DtcCenterBean> list = DBTools.getBeanList(service,DtcCenterBean.class,"select * from DTC_CENTER");
        return list;
    }

    /**
     * 保存中心
     * @param service
     * @param center
     */
    public void saveCenter(Service service,@ModelAttribute DtcCenterBean center){

    }


}