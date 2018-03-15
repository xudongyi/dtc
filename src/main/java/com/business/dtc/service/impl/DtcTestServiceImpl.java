package com.business.dtc.service.impl;

import com.business.dtc.bean.DtcAgeGroupBean;
import com.business.dtc.bean.DtcTestBean;
import com.business.dtc.bean.DtcTestCenterPatient;
import com.business.dtc.service.DtcTestService;
import net.sf.rose.jdbc.service.Service;

/**
 * @author: xudy
 * @date: 2018/03/14 16:54
 * @description:
 */
@org.springframework.stereotype.Service
public class DtcTestServiceImpl implements DtcTestService {

    @Override
    public boolean checkPermission(Service service, DtcTestCenterPatient patient) {
        //1.中心最多人数>0
        //2.中心最多人数-（中心其它年龄最少人数总和）>0
        return false;
    }

    @Override
    public boolean assignNumberToPatientByCache(Service service, DtcTestCenterPatient patient) {
        //1.根据年龄从缓存中获取对应的数据,进行分配
        //2.如果缓存中没有对应年龄的数据,则初始化4条记录进入缓存
        return false;
    }

    @Override
    public boolean initAgeNumberCache(Service service, DtcTestCenterPatient patient) {
        //初始化4条记录进入缓存
        return false;
    }

    @Override
    public DtcAgeGroupBean getAgeGroupByAge(Service service, int age) {
        return null;
    }

    @Override
    public DtcTestBean getCurrentTest(Service service) {
        return null;
    }

}