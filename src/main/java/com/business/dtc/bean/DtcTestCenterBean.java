package com.business.dtc.bean;

import net.sf.rose.jdbc.bean.ModelBean;
import net.sf.rose.jdbc.bean.annotation.Column;
import net.sf.rose.jdbc.bean.annotation.Table;
import net.sf.rose.jdbc.bean.annotation.Type;

/**
 * @author: xudy
 * @date: 2018/03/13 19:20
 * @description:测试检验中心关联表
 */
@Table(name = "DTC_TEST_CENTER")
public class DtcTestCenterBean extends ModelBean {

	@Column(pk = true, name = "ID", type = Type.定长文本, description = "主键", canNull = false, size = 32, policy = "UUID")
	private String id;

	@Column(name = "TEST_ID", type = Type.变长文本, canNull = false, size = 32)
	private String testId;

	@Column(name = "CENTER_ID", type = Type.变长文本, canNull = false, size = 32)
	private String centerId;

	@Column(name = "CENTER_MAX", type = Type.数字整型)
	private int centerMax;

	@Column(name = "CENTER_LEFT", type = Type.数字整型)
	private int centerLeft;

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

	public String getCenterId() {
		return centerId;
	}

	public void setCenterId(String centerId) {
		this.centerId = centerId;
	}

	public int getCenterMax() {
		return centerMax;
	}

	public void setCenterMax(int centerMax) {
		this.centerMax = centerMax;
	}

	public int getCenterLeft() {
		return centerLeft;
	}

	public void setCenterLeft(int centerLeft) {
		this.centerLeft = centerLeft;
	}
}