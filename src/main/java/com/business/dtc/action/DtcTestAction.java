package com.business.dtc.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.rose.jdbc.DBUtils;
import net.sf.rose.jdbc.KeyGenerator;
import net.sf.rose.jdbc.service.Service;
import net.sf.rose.util.StringUtil;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.business.dtc.bean.DtcTestBean;
import com.business.dtc.bean.DtcTestCenterBean;
import com.business.dtc.bean.DtcTestCenterGroupBean;
import com.business.dtc.util.DBTools;


/**
 * @author: xudy
 * @date: 2018/03/14 16:46
 * @description:
 */
@RestController
@RequestMapping("test")
public class DtcTestAction extends BaseAction{

    /**
     * 获取当前正在进行的测试
     * @param service
     * @param request
     * @return
     */
    @RequestMapping("/currentTest.do")
    public DtcTestBean currentTest(Service service, HttpServletRequest request){


        return null;
    }

    /**
     * 没有进行的测试,可以创建新的测试
     * @param service
     * @param request
     * @return
     */
    @RequestMapping("/createTest.do")
    public boolean createTest(Service service, HttpServletRequest request, String json , @RequestParam String[] groupIds, @RequestParam String[] centerIds, @RequestParam List<Integer> min, int centerMax){
        DtcTestBean bean = StringUtil.parse(json,DtcTestBean.class);
        DtcTestBean current = dtcTestService.getCurrentTest(service);
        //1.检查是否存在对应正在进行的实验
        if(current==null){
            //1.新增任务
            int i = DBUtils.update(service,bean);
            //2.产生随机号
            boolean createFlag = dtcTestService.createTestNumbers(service,bean.getId(),bean.getTestCount());
            if(i>0 && createFlag){
                //2.不存在新增DtcTestCenterBean
                List<DtcTestCenterBean> testCenterBeanList = new ArrayList<>();
                int result = 0;
                for(int j = 0;j<centerIds.length;j++){
                    DtcTestCenterBean testCenterBean = new DtcTestCenterBean();
                    testCenterBean.setId(KeyGenerator.getSystemUUID());
                    testCenterBean.setCenterMax(centerMax);
                    testCenterBean.setCenterLeft(centerMax);
                    testCenterBean.setCenterId(centerIds[j]);
                    testCenterBean.setTestId(bean.getId());
                    testCenterBeanList.add(testCenterBean);
                    result+=DBTools.insertBean(service,testCenterBean);
                }
                if(result==centerIds.length){
                    //3.根据新增的DtcTestCenterBean新增group
                    List<DtcTestCenterGroupBean> groupBeanList = new ArrayList<>();
                    for(int k = 0;k<groupIds.length;k++){
                        for( DtcTestCenterBean testCenterBean : testCenterBeanList){
                            DtcTestCenterGroupBean groupBean = new DtcTestCenterGroupBean();
                            groupBean.setMinCount(min.get(k));
                            groupBean.setGroupId(groupIds[k]);
                            groupBean.setTestCenterId(testCenterBean.getId());
                            groupBeanList.add(groupBean);
                        }
                    }

                    int [] re = DBTools.bacthInsertBean(service,DtcTestCenterGroupBean.class,groupBeanList);
                    if(re.length==groupBeanList.size()){
                        System.out.println("新增成功");

                    }else{
                        System.out.println("新增中心年龄表失败");
                    }

                }else{
                    System.out.println("新增中心测试表失败");
                }


            }else{
                System.out.println("新增测试失败");
            }

        }

        return false;
    }
}