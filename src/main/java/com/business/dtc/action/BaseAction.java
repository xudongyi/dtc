package com.business.dtc.action;

import com.business.dtc.service.DtcTestService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: xudy
 * @date: 2018/03/15 14:25
 * @description:
 */
public class BaseAction {

    @Autowired
    DtcTestService dtcTestService;
}