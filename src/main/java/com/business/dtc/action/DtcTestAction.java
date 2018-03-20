package com.business.dtc.action;

import java.util.*;

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


    @RequestMapping("/current.do")
    public  DtcTestBean getCurrentTest(Service service){
        return dtcTestService.getCurrentTest(service);
    }

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

    /**
     * 试验分组详情
     * @param service
     * @param request
     * @return
     */
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
        sql.append(" order by t2.ASSIGN_TIME desc");
        page = DBTools.getDataList(service,sql.toString(),page,condition);
        return page;
    }

    @RequestMapping("testProcess.do")
    public Map<String,Object> testProcess(Service service,HttpServletRequest request){
        DtcTestBean test = dtcTestService.getCurrentTest(service);
        if(test!=null){
            Map<String,Object> result = new HashMap<>();
            Map<String,Object> params = WebUtils.getRequestData(request);
            //表头
            List<Object> header = new ArrayList<>();
            //内容
            List<List<Object>> body = new ArrayList<>();
            //内容汇总
            List<Object> bodyCollect = new ArrayList<>();
            //底部
            List<List<Object>> groupBottom = new ArrayList<>();

            List<DtcCenterBean> centers = dtcTestService.getCenters(service);
            List<DtcAgeGroupBean> ageGroups =dtcTestService.getAgeGroups(service);
            header.add("统计项");
            for(DtcCenterBean bean : centers){
                header.add(bean.getCenterName());
            }
            for(int i =0;i<ageGroups.size();i++){
                List<Object> singleData = new ArrayList<>();
                DtcAgeGroupBean ageGroupBean = ageGroups.get(i);
                int total  = 0;
                singleData.add(ageGroupBean.getGroupName());
                for(int j =0;j<centers.size();j++){
                    DtcCenterBean centerBean = centers.get(j);
                    //统计每个中心和年龄分组对应的进展情况
                    StringBuilder sql= new StringBuilder();
                    sql.append("select count(1) AS counts from dtc_test_center_patient t1")
                    .append(" left join dtc_test_center t2 on t1.TEST_CENTER_ID = t2.ID")
                    .append(" left join dtc_test t3 on t2.TEST_ID = t3.ID")
                    .append(" left join dtc_age_group t4 on t1.GROUP_ID = t4.ID")
                    .append(" where t2.CENTER_ID=?")
                    .append(" and t3.ID=?")
                    .append(" and  t4.ID=?");
                    Map<String,Object> counts = DBTools.getData(service,sql.toString(),centerBean.getId(),test.getId(),ageGroupBean.getId());
                    singleData.add(counts.get("counts"));
                    total+=Integer.parseInt(counts.get("counts").toString());
                }
                singleData.add(total);
                body.add(singleData);
            }
            header.add("总计");
            int [] total = new int[body.get(0).size()-1];
            for(int i =0;i<body.size() ;i++){
                List<Object> collect = body.get(i);
                for(int j =1;j<collect.size();j++){
                   total[j-1]+=Integer.parseInt(collect.get(j).toString());
                }
            }
            List<Object> re =new ArrayList<>();
            re.add(0,"总计");
            for(Object o : total){
                re.add(o);
            }
            body.add(re);

            //AB组统计
            for(String groupName : new String[]{"A","B"}){
                List<Object> singleData = new ArrayList<>();
                singleData.add(groupName);
                int totalCount = 0;
                for(DtcCenterBean centerBean : centers){
                    StringBuilder sql= new StringBuilder();
                    sql.append("select count(1) as counts from dtc_test_center_patient t1 ")
                            .append(" left join dtc_test_center t2 on t1.TEST_CENTER_ID = t2.ID")
                            .append(" left join dtc_test t3 on t2.TEST_ID = t3.ID ")
                            .append(" left join dtc_test_number t4 on t1.NUMBER_ID = t4.ID")
                            .append(" where t2.CENTER_ID=?")
                            .append(" and t3.ID=?")
                            .append(" and t4.GROUP_NAME=?");
                    Map<String,Object> counts = DBTools.getData(service,sql.toString(),centerBean.getId(),test.getId(),groupName);
                    singleData.add(counts.get("counts"));
                    totalCount+=Integer.parseInt(counts.get("counts").toString());
                }
                singleData.add(totalCount);
                body.add(singleData);

            }
            result.put("header",header);
            result.put("body",body);
            return result;
        }
        return null;
    }
}