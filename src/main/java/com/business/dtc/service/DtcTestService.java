package com.business.dtc.service;

import net.sf.rose.jdbc.service.Service;

import com.business.dtc.bean.DtcCenterBean;
import com.business.dtc.bean.DtcTestBean;
import com.business.dtc.bean.DtcTestCenterBean;
import com.business.dtc.bean.DtcTestCenterPatient;

/**
 * @author: xudy
 * @date: 2018/3/14 0014
 * @description:
 */
public interface DtcTestService {

	/**
	 * 根据centerId获取中心的基本信息
	 * 
	 * @param service
	 * @param centerId
	 * @return
	 */
	DtcCenterBean getCenter(Service service, String centerId);

	/**
	 * 检查该人员能够进入测试系统中
	 * 
	 * @param service
	 * @param patient
	 *            病人
	 * @param testCenterBean
	 *            测试中心信息
	 * @return
	 */
	boolean checkPermission(Service service, DtcTestCenterPatient patient, DtcTestCenterBean testCenterBean);

	/**
	 * 获取当前正在进行的测试
	 * 
	 * @param service
	 * @return
	 */
	DtcTestBean getCurrentTest(Service service);

	/**
	 * 获取当前正在进行的测试
	 * 
	 * @param service
	 * @param testId
	 * @param centerId
	 * @return
	 */
	DtcTestCenterBean getCurrentTestCenter(Service service, String testId, String centerId);

	/**
	 * 获取中心其他年龄段的人数之和
	 * 
	 * @param service
	 * @param testCenterId
	 * @param patient
	 * @return
	 */
	int getCenterOtherGroupCount(Service service, String testCenterId, DtcTestCenterPatient patient);

	/**
	 * 获取中心对应年龄段的缓存信息并同时将缓存中的号分配给该病人
	 * 
	 * @param service
	 * @param testCenterId
	 * @param patient
	 * @return
	 */
	boolean getTestNumberCacheAndAssign(Service service, String testId, String testCenterId, DtcTestCenterPatient patient);

    /**
     * 生成随机号
     * @param service
     * @param testId
     * @return
     */
	boolean createTestNumbers(Service service,String testId,int counts);

}
