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

	@Column(name = "GROUP_ID", type = Type.变长文本, canNull = false, size = 32)
	private String groupId;

	@Column(name = "TEST_CENTER_ID", type = Type.变长文本, canNull = false, size = 32)
	private String testCenterId;

	@Column(name = "MIN_COUNT", type = Type.数字整型, description = "每个年龄段的最少人数")
	private int minCount;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getTestCenterId() {
		return testCenterId;
	}

	public void setTestCenterId(String testCenterId) {
		this.testCenterId = testCenterId;
	}

	public int getMinCount() {
		return minCount;
	}

	public void setMinCount(int minCount) {
		this.minCount = minCount;
	}
}