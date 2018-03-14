package com.business.dtc.bean;

import net.sf.rose.jdbc.bean.ModelBean;
import net.sf.rose.jdbc.bean.annotation.Column;
import net.sf.rose.jdbc.bean.annotation.Table;
import net.sf.rose.jdbc.bean.annotation.Type;

/**
 * @author: xudy
 * @date: 2018/03/14 10:20
 * @description:测试中心
 */
@Table(name = "DTC_CENTER")
public class DtcCenterBean extends ModelBean {

	@Column(pk = true, name = "ID", type = Type.定长文本, description = "主键", canNull = false, size = 32, policy = "UUID")
	private String id;

	@Column(name = "CENTER_NO", type = Type.变长文本, canNull = false, size = 10)
	private String centerNo;

	@Column(name = "CENTER_NAME", type = Type.变长文本, size = 50)
	private String centerName;

	@Column(name = "ADDRESS", type = Type.变长文本, size = 50)
	private String address;

	@Column(name = "USER_NAME", type = Type.变长文本, canNull = false, size = 50)
	private String userName;

	@Column(name = "PASSWORD", type = Type.变长文本, canNull = false, size = 50)
	private String password;

	@Column(name = "ROLE", type = Type.数字整型, canNull = false)
	private int role;

	@Column(name = "REAL_NAME", type = Type.变长文本, size = 50)
	private String realName;

	@Column(name = "MOBILE", type = Type.变长文本, size = 50)
	private String mobile;

	@Column(name = "USER_STATE", type = Type.数字整型, canNull = false)
	private int userState;

	@Column(name = "IS_DELETED", type = Type.数字整型, canNull = false)
	private int isDeleted;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCenterNo() {
		return centerNo;
	}

	public void setCenterNo(String centerNo) {
		this.centerNo = centerNo;
	}

	public String getCenterName() {
		return centerName;
	}

	public void setCenterName(String centerName) {
		this.centerName = centerName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public int getUserState() {
		return userState;
	}

	public void setUserState(int userState) {
		this.userState = userState;
	}

	public int getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}
}