package com.business.dtc.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.business.dtc.bean.*;
import net.sf.rose.jdbc.DBUtils;
import net.sf.rose.jdbc.KeyGenerator;
import net.sf.rose.jdbc.PageBean;
import net.sf.rose.jdbc.service.Service;
import net.sf.rose.util.StringUtil;

import net.sf.rose.web.utils.WebUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.business.dtc.util.DBTools;


/**
 * @author: xudy
 * @date: 2018/03/14 16:46
 * @description:
 */
@RestController
@RequestMapping("test")
public class DtcTestAction extends BaseAction{



    @RequestMapping("/getCenterList.do")
    public List<DtcCenterBean> getCenterList(Service service,HttpServletRequest request){
        List<DtcCenterBean> list = DBTools.getBeanList(service,DtcCenterBean.class,"SELECT * from DTC_CENTER where role=2");
        return list;
    }

    @RequestMapping("/getAgeGroupList.do")
    public List<DtcAgeGroupBean> getAgeGroupList(Service service,HttpServletRequest request){
        List<DtcAgeGroupBean> list = DBUtils.getBeanList(service,DtcAgeGroupBean.class);
        return list;
    }

    /**
     * 获取当前正在进行的测试
     * @param service
     * @param request
     * @return
     */
    @RequestMapping("/currentTest.do")
    public Map<String,Object> currentTest(Service service, HttpServletRequest request){
        DtcTestBean current = dtcTestService.getCurrentTest(service);
        if(current!=null){
            //当前全部年龄组
            List<DtcAgeGroupBean> groups = DBUtils.getBeanList(service,DtcAgeGroupBean.class);
            Map<String,Object> condition = new HashMap<>();
            condition.put("testId",current.getId());
            List<DtcTestCenterBean> testCenters = DBUtils.getBeanList(service,DtcTestCenterBean.class,condition);
            String queryId = "";
            String centerId = "";

            List<String> groupR = new ArrayList<>() ;
            List<Integer> minR = new ArrayList<>();
            List<String> centerR = new ArrayList<>();
            for(DtcTestCenterBean bean : testCenters){
                centerId+="'"+bean.getCenterId()+"',";
                centerR.add(bean.getCenterId());
            }
            centerId = centerId.substring(0,centerId.length()-1);
            List<DtcCenterBean> centers = DBTools.getBeanList(service,DtcCenterBean.class,
            "select * from DTC_CENTER where ID in("+centerId+")");
            String sql = "select * from DTC_TEST_CENTER_GROUP where TEST_CENTER_ID in('"+testCenters.get(0).getId()+"')";
            List<DtcTestCenterGroupBean> groupBeanList = DBTools.getBeanList(service,DtcTestCenterGroupBean.class,sql);
            List<DtcAgeGroupBean> resultGroups = new ArrayList<>();

            for(DtcTestCenterGroupBean group : groupBeanList){
                for(DtcAgeGroupBean bean : groups){
                    if(group.getGroupId().equals(bean.getId())){
                        resultGroups.add(bean);
                        break;
                    }
                }
                groupR.add(group.getGroupId());
                minR.add(group.getMinCount());

            }

            Map<String,Object> result = new HashMap<>();
            result.put("name",current.getName());
            result.put("testCount",current.getTestCount());
            result.put("centerMax",testCenters.get(0).getCenterMax());
            result.put("centers",centers);
            result.put("groups",resultGroups);
            result.put("group",groupR);
            result.put("center",centerR);
            result.put("min",minR);
            return result;
        }

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
            //正在进行中
            bean.setState(DtcTestBean.DOING);
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
                        return true;

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

    @RequestMapping("groupDetail.do")
    public PageBean groupDetail (Service service, HttpServletRequest request){
        PageBean page = WebUtils.getPageBean(request);
        Map<String,Object> params = WebUtils.getRequestData(request);
        DtcTestBean test = dtcTestService.getCurrentTest(service);
        StringBuilder sql = new StringBuilder();
        sql.append("select t1.*,t2.NUMBER,t2.GROUP_NAME,t2.ASSIGN_TIME,t4.CENTER_NAME from dtc_test_center_patient t1")
            .append(" left join dtc_test_number t2 on t1.ID = t2.PATIENT_ID ")
            .append(" left join dtc_test_center t3 on t1.TEST_CENTER_ID = t3.ID ")
            .append(" left join dtc_center t4 on t3.CENTER_ID = t4.ID ")
            .append(" where t2.TEST_ID=?");
        List<Object> condition = new ArrayList<>();
        condition.add(test.getId());
        if(params.containsKey("beginTime")){
            sql.append(" AND t2.ASSIGN_TIME>=?");
            condition.add(params.get("beginTime"));
        }
        if(params.containsKey("endTime")){
            sql.append(" AND t2.ASSIGN_TIME<=?");
            condition.add(params.get("endTime"));
        }
        if(params.containsKey("centerId")){
            sql.append(" AND t4.ID=?");
            condition.add(params.get("centerId"));
        }
        page = DBTools.getDataList(service,sql.toString(),page,condition);
        return page;
    }
}