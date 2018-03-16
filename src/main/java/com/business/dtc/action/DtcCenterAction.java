package com.business.dtc.action;

import com.business.dtc.bean.DtcTestBean;
import com.business.dtc.bean.DtcTestCenterBean;
import com.business.dtc.bean.DtcTestCenterPatient;
import com.business.dtc.util.AgeUtil;
import net.sf.rose.jdbc.service.Service;
import net.sf.rose.util.DateFormat;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author: xudy
 * @date: 2018/03/15 14:29
 * @description:中心端业务接口
 */
@RestController
@RequestMapping("dtc/center")
public class DtcCenterAction extends  BaseAction {


    /**
     * 获取测试的基本信息
     * 获取该测试中心的每个年龄组的最少实验人数和已经进入的人数
     * @param service
     * @param request
     */
    public void getCenterInfo(Service service, HttpServletRequest request){

    }

    /**
     * 检验年龄和出生年月
     * @param service
     * @param request
     * @param age
     * @param birthday
     * @return
     */
    @RequestMapping("checkAgeWithBirthday.do")
    public boolean checkAgeWithBirthday(Service service,HttpServletRequest request,int age,String birthday){
        Date birth = DateFormat.format(birthday,"yyyy-MM-dd");
        return AgeUtil.checkAgeWithBirthday(age,birth);
    }

    /**
     * 添加病人
     * @param service
     * @param patient
     */
    @RequestMapping("addPatient.do")
    public void addPatient(Service service, @ModelAttribute DtcTestCenterPatient patient){
        //1.获取正在进行的试验
        DtcTestBean testBean = dtcTestService.getCurrentTest(service);
        if(testBean!=null){
            String testId = testBean.getId();
            String centerId = patient.getCenterId();
            //2.根据testId和centerId获取该中心,该试验的testCenter
            DtcTestCenterBean testCenterBean = dtcTestService.getCurrentTestCenter(service,testId,centerId);
            if(testCenterBean!=null){
                patient.setTestCenterId(testCenterBean.getId());
                //3.检查病人是否能够进行测试
                if(dtcTestService.checkPermission(service,patient,testCenterBean)){
                    System.out.println("能参加该测试!");
                    //4.能够继续参加试验首先从缓存中获取对应年龄段的缓存信息
                    boolean assign = dtcTestService.getTestNumberCacheAndAssign(service,testId,testCenterBean.getId(),patient);
                }else{
                    System.out.println("不能参加该测试!");
                }

            }
        }
    }

    /**
     * 获取对应测试,对应中心的被测试人列表
     * @param service
     */
    @RequestMapping("listPatient.do")
    public void listPatient(Service service){

    }
}