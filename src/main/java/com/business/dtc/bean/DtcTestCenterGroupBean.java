package com.business.dtc.bean;

import net.sf.rose.jdbc.bean.ModelBean;
import net.sf.rose.jdbc.bean.annotation.Column;
import net.sf.rose.jdbc.bean.annotation.Table;
import net.sf.rose.jdbc.bean.annotation.Type;

/**
 * @author: xudy
 * @date: 2018/03/13 19:20
 * @description:检验年龄分组
 */
@Table(name = "DTC_TEST_CENTER_GROUP")
public class DtcTestCenterGroupBean extends ModelBean {

	@Column(pk = true, name = "ID", type = Type.定长文本, description = "主键", canNull = false, size = 32, policy = "UUID")
	private String id;

	@Column(name = "NUMBER_ID", type = Type.变长文本, canNull = false, size = 32)
	private String numberId;

	@Column(name = "TEST_CENTER_ID", type = Type.变长文本, canNull = false, size = 32)
	private String testCenterId;

	@Column(name = "GROUP_NAME", type = Type.变长文本, description = "分组A,B")
	private String groupName;

	@Column(name = "GROUP_ID", type = Type.变长文本, canNull = false, size = 32)
	private String groupId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNumberId() {
		return numberId;
	}

	public void setNumberId(String numberId) {
		this.numberId = numberId;
	}

	public String getTestCenterId() {
		return testCenterId;
	}

	public void setTestCenterId(String testCenterId) {
		this.testCenterId = testCenterId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
}