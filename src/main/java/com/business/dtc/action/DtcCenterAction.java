package com.business.dtc.action;

import com.business.dtc.bean.DtcCenterBean;
import com.business.dtc.bean.DtcTestBean;
import com.business.dtc.bean.DtcTestCenterBean;
import com.business.dtc.bean.DtcTestCenterPatientBean;
import com.business.dtc.util.AgeUtil;
import com.business.dtc.util.DBTools;
import net.sf.rose.jdbc.service.Service;
import net.sf.rose.util.DateFormat;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: xudy
 * @date: 2018/03/15 14:29
 * @description:中心端业务接口
 */
@RestController
@RequestMapping("center")
public class DtcCenterAction extends  BaseAction {


    /**
     * 获取测试的基本信息
     * 获取该测试中心的每个年龄组的最少实验人数和已经进入的人数
     * @param service
     * @param request
     */
    @RequestMapping("/getCenterInfo.do")
    public Map<String,Object> getCenterInfo(Service service, HttpServletRequest request){
        Map<String,Object> result = new HashMap<>();
        //1.获取左侧该实验,该中心的目前进展情况
        DtcTestBean testBean = dtcTestService.getCurrentTest(service);
        DtcCenterBean user = dtcTestService.getCurrentUser(request);
        if(testBean!=null && user!=null){
            List<Map<String,Object>> info = dtcTestService.getCenterGroupDetail(service,testBean.getId(),user.getId());
            result.put("info",info);
            //汇总
            int[] total = new int[2];
            for(Map<String,Object> map : info){
                total[0]+=Integer.parseInt(map.get("MIN_COUNT").toString());
                total[1]+=Integer.parseInt(map.get("counts").toString());
            }
            result.put("total",total);
            //中心允许的最多人数
            DtcTestCenterBean bean = DBTools.getBean(service,DtcTestCenterBean.class,
            "select t1.* from dtc_test_center t1 left join dtc_center t2" +
                    " on t1.CENTER_ID = t2.ID" +
                    " where t1.TEST_ID=?" +
                    " and t1.CENTER_ID=?",testBean.getId(),user.getId());
            result.put("centerInfo",bean);

        }
        return result;
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
    public int addPatient(Service service, @ModelAttribute DtcTestCenterPatientBean patient){
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
                    int randomNum = dtcTestService.getTestNumberCacheAndAssign(service,testId,testCenterBean.getId(),patient);
                    return randomNum;
                }else{
                    System.out.println("不能参加该测试!");
                }

            }
        }
        return 0;
    }

    /**
     * 获取对应测试,对应中心的被测试人列表
     * @param service
     */
    @RequestMapping("listPatient.do")
    public void listPatient(Service service){

    }
}