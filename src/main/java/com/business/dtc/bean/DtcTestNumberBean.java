package com.business.dtc.bean;

import java.util.Date;

import net.sf.rose.jdbc.bean.ModelBean;
import net.sf.rose.jdbc.bean.annotation.Column;
import net.sf.rose.jdbc.bean.annotation.Table;
import net.sf.rose.jdbc.bean.annotation.Type;

/**
 * @author: xudy
 * @date: 2018/03/13 19:20
 * @description:检验序号码
 */
@Table(name = "DTC_TEST_NUMBER")
public class DtcTestNumberBean extends ModelBean {

	@Column(pk = true, name = "ID", type = Type.定长文本, description = "主键", canNull = false, size = 32, policy = "UUID")
	private String id;

	@Column(name = "TEST_ID", type = Type.变长文本, canNull = false, size = 32)
	private String testId;

	@Column(name = "GROUP_NAME", type = Type.数字整型, description = "A/B(只有当number_state=2时才会将A/B的值赋值)")
	private String groupName;

	/**
	 * (当数据被中心拿走时此时标示该记录已经被拿走,无法再次取出)
	 */
	public static int NUMBER_STATE_NOTASSIGN = 0;
	public static int NUMBER_STATE_ISCACHE = 1;
	public static int NUMBER_STATE_ISASSIGN = 2;
	public static int NUMBER_STATE_WASTE = 3;
	@Column(name = "NUMBER_STATE", type = Type.数字整型, description = "0-未分配 1-已缓存 2-已分配 3-已废弃")
	private int numberState;

	@Column(name = "CACHE_TIME", type = Type.日期时间)
	private Date cacheTime;

	@Column(name = "ASSIGN_TIME", type = Type.日期时间)
	private Date assignTime;

	@Column(name = "CENTER_ID", type = Type.变长文本, canNull = false, size = 32)
	private String centerId;

	@Column(name = "PATIENT_ID", type = Type.变长文本, canNull = false, size = 32)
	private String patientId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTestId() {
		return testId;
	}

	public void setTestId(String testId) {
		this.testId = testId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public int getNumberState() {
		return numberState;
	}

	public void setNumberState(int numberState) {
		this.numberState = numberState;
	}

	public Date getCacheTime() {
		return cacheTime;
	}

	public void setCacheTime(Date cacheTime) {
		this.cacheTime = cacheTime;
	}

	public Date getAssignTime() {
		return assignTime;
	}

	public void setAssignTime(Date assignTime) {
		this.assignTime = assignTime;
	}

	public String getCenterId() {
		return centerId;
	}

	public void setCenterId(String centerId) {
		this.centerId = centerId;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
}