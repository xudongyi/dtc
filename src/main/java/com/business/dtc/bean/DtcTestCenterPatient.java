package com.business.dtc.bean;

import java.util.Date;

import net.sf.rose.jdbc.bean.ModelBean;
import net.sf.rose.jdbc.bean.annotation.Column;
import net.sf.rose.jdbc.bean.annotation.Table;
import net.sf.rose.jdbc.bean.annotation.Type;

/**
 * @author: xudy
 * @date: 2018/03/13 19:20
 * @description:检验测试病人
 */
@Table(name = "DTC_TEST_CENTER_PATIENT")
public class DtcTestCenterPatient extends ModelBean {

	@Column(pk = true, name = "ID", type = Type.定长文本, description = "主键", canNull = false, size = 32, policy = "UUID")
	private String id;

	@Column(name = "patient_number", type = Type.变长文本, canNull = false, size = 10)
	private String patientNumber;

	@Column(name = "CENTER_ID", type = Type.变长文本, canNull = false, size = 32)
	private String centerId;

	@Column(name = "TEST_CENTER_ID", type = Type.变长文本, canNull = false, size = 32)
	private String testCenterId;

	@Column(name = "NUMBER_ID", type = Type.变长文本, canNull = false, size = 32)
	private String numberId;

	@Column(name = "NAME", type = Type.变长文本, canNull = false, size = 50)
	private String name;

	@Column(name = "SEX", type = Type.数字整型)
	private int sex;

	@Column(name = "AGE", canNull = false, type = Type.数字整型)
	private int age;

	@Column(name = "BIRTHDAY", canNull = false, type = Type.日期时间)
	private Date birthday;

	@Column(name = "IS_MARRYED", type = Type.数字整型)
	private int isMarry;

	@Column(name = "MOBILE", type = Type.变长文本, size = 50)
	private String mobile;

	@Column(name = "ADDRESS", type = Type.变长文本, size = 100)
	private String address;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPatientNumber() {
		return patientNumber;
	}

	public void setPatientNumber(String patientNumber) {
		this.patientNumber = patientNumber;
	}

	public String getCenterId() {
		return centerId;
	}

	public void setCenterId(String centerId) {
		this.centerId = centerId;
	}

	public String getTestCenterId() {
		return testCenterId;
	}

	public void setTestCenterId(String testCenterId) {
		this.testCenterId = testCenterId;
	}

	public String getNumberId() {
		return numberId;
	}

	public void setNumberId(String numberId) {
		this.numberId = numberId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public int getIsMarry() {
		return isMarry;
	}

	public void setIsMarry(int isMarry) {
		this.isMarry = isMarry;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}