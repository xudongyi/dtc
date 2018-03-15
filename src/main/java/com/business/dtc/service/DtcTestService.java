package com.business.dtc.service;

import com.business.dtc.bean.DtcAgeGroupBean;
import com.business.dtc.bean.DtcTestBean;
import com.business.dtc.bean.DtcTestCenterPatient;
import net.sf.rose.jdbc.service.Service;

/**
 * @author: xudy
 * @date: 2018/3/14 0014
 * @description:
 */
public interface DtcTestService {

    /**
     * 检查该人员能够进入测试系统中
     * @param service
     * @param patient
     * @return
     */
    boolean checkPermission(Service service,DtcTestCenterPatient patient);

    /**
     * 根据病人从对应测试中心,对应年龄的缓存中进行分配号牌
     * @param service
     * @param patient
     */
    boolean assignNumberToPatientByCache(Service service,DtcTestCenterPatient patient);

    /**
     * 初始化对应年龄段的缓存
     * @param service
     * @param patient
     */
    boolean initAgeNumberCache(Service service,DtcTestCenterPatient patient);

    /**
     * 根据年龄获取对应分组
     * @param service
     * @param age
     * @return
     */
    DtcAgeGroupBean getAgeGroupByAge(Service service,int age);

    /**
     * 获取当前正在进行的测试
     * @param service
     * @return
     */
    DtcTestBean getCurrentTest(Service service);

}
