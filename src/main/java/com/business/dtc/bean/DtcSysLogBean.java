package com.business.dtc.bean;

import net.sf.rose.jdbc.bean.ModelBean;
import net.sf.rose.jdbc.bean.annotation.Column;
import net.sf.rose.jdbc.bean.annotation.Table;
import net.sf.rose.jdbc.bean.annotation.Type;

/**
 * @author: xudy
 * @date: 2018/03/13 19:20
 * @description:系统日志
 */
@Table(name = "DTC_SYS_LOG")
public class DtcSysLogBean extends ModelBean {

	@Column(pk = true, name = "ID", type = Type.定长文本, description = "主键", canNull = false, size = 32, policy = "UUID")
	private String id;

	@Column(name = "CONTENT", type = Type.变长文本, size = 500)
	private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}