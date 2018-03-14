package com.business.dtc.bean;

import net.sf.rose.jdbc.bean.ModelBean;
import net.sf.rose.jdbc.bean.annotation.Column;
import net.sf.rose.jdbc.bean.annotation.Table;
import net.sf.rose.jdbc.bean.annotation.Type;

/**
 * @author: xudy
 * @date: 2018/03/13 19:20
 * @description:用于产生一次检验试验活动(默认只能有一个正在进行)
 */
@Table(name = "DTC_TEST")
public class DtcTestBean extends ModelBean {

	@Column(pk = true, name = "ID", type = Type.定长文本, description = "主键", canNull = false, size = 32, policy = "UUID")
	private String id;

	@Column(name = "NAME", type = Type.变长文本, canNull = false, size = 50)
	private String name;

	@Column(name = "TEST_COUNT", type = Type.数字整型, canNull = false)
	private int testCount;

	@Column(name = "DESCRIPTION", type = Type.变长文本, size = 200)
	private String description;

	@Column(name = "STATE", type = Type.数字整型, canNull = false, description = "1-正在进行 2-已结束")
	private int state;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTestCount() {
		return testCount;
	}

	public void setTestCount(int testCount) {
		this.testCount = testCount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
}