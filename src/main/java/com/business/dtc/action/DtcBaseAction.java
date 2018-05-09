package com.business.dtc.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.rose.jdbc.DBUtils;
import net.sf.rose.jdbc.service.Service;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.business.dtc.bean.DtcAgeGroupBean;
import com.business.dtc.bean.DtcCenterBean;
import com.business.dtc.util.DBTools;

/**
 * @author: xudy
 * @date: 2018/03/17 21:49
 * @description:基础数据的维护
 */
@RestController
@RequestMapping("base")
public class DtcBaseAction extends  BaseAction{

    /**
     * 年龄分组列表
     * @param service
     * @param request
     */
    @RequestMapping("group/list.do")
    public List<DtcAgeGroupBean> ageGroupList(Service service, HttpServletRequest request){
        List<DtcAgeGroupBean> list = DBUtils.getBeanList(service,DtcAgeGroupBean.class);
        return list;
    }


    /**
     * 检查年龄段在数据库中是否已经存在
     * @param service
     * @param group
     */
    @RequestMapping("group/checkAge.do")
    public boolean checkAge(Service service, @ModelAttribute DtcAgeGroupBean group){
        String sql = "select * from dtc_age_group where MIN_AGE<=? and MAX_AGE>=?";
        DtcAgeGroupBean g = DBTools.getBean(service,DtcAgeGroupBean.class,sql,group.getMinAge(),group.getMaxAge());
        return g!=null;
    }

    /**
     * 保存分组年龄
     * @param service
     * @param group
     */
    @RequestMapping("group/save.do")
    public int saveGroup(Service service, @ModelAttribute DtcAgeGroupBean group){
        int i = DBUtils.update(service,group);
        return i;
    }

    /**
     * 中心列表
     * @param service
     * @param request
     */
    @RequestMapping("center/list.do")
    public List<DtcCenterBean> centerList(Service service, HttpServletRequest request){
        List<DtcCenterBean> list = DBTools.getBeanList(service,DtcCenterBean.class,"select * from DTC_CENTER WHERE IS_DELETED=1");
        return list;
    }

    /**
     * 保存中心
     * @param service
     * @param center
     */
    @RequestMapping("center/save.do")
    public int saveCenter(Service service,@ModelAttribute DtcCenterBean center){
        String userName = center.getUserName();

        DtcCenterBean user = DBTools.getBean(service,DtcCenterBean.class,"select * from DTC_CENTER where USER_NAME=?" ,userName);

        if(user==null){
            int i = DBUtils.update(service,center);
            return i;
        }else if(user.getId().equals(center.getId())){
            int i = DBUtils.update(service,center);
            return i;
        }
        return -1;
    }

    /**
     * 启用/禁用
     * @param service
     * @param id
     */
    @RequestMapping("center/enable.do")
    public int enableCenter(Service service,String id,int state){
        int i = DBTools.updateSql(service,"update DTC_CENTER set USER_STATE=? where ID=?",state,id);
        return i;
    }

    /**
     * 删除中心
     * @param service
     * @param id
     */
    @RequestMapping("center/delete.do")
    public int deleteCenter(Service service,String id){
        int i = DBTools.updateSql(service,"update DTC_CENTER set IS_DELETED=0 where ID=?",id);
        return i;
    }


}