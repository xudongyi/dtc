package com.business.dtc.service.impl;

import java.util.*;

import net.sf.rose.jdbc.DBUtils;
import net.sf.rose.jdbc.dao.BeanDAO;
import net.sf.rose.jdbc.query.BeanSQL;
import net.sf.rose.jdbc.service.Service;
import net.sf.rose.util.DateFormat;

import org.apache.log4j.Logger;

import com.business.dtc.bean.*;
import com.business.dtc.service.DtcTestService;
import com.business.dtc.util.DBTools;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: xudy
 * @date: 2018/03/14 16:54
 * @description:
 */
@org.springframework.stereotype.Service
public class DtcTestServiceImpl implements DtcTestService {

	Logger LOG = Logger.getLogger(DtcTestServiceImpl.class);

	@Override
	public DtcCenterBean getCenter(Service service, String centerId) {
		DtcCenterBean bean = DBUtils.getBean(service, DtcCenterBean.class, centerId);
		return bean;
	}

    @Override
    public DtcCenterBean getCurrentUser(HttpServletRequest request) {
        DtcCenterBean bean = (DtcCenterBean)request.getSession().getAttribute("user");
        return bean;
    }

    @Override
    public List<Map<String, Object>> getCenterGroupDetail(Service service, String testId, String centerId) {
        StringBuilder sql = new StringBuilder();
	    sql.append("select j3.GROUP_NAME,j2.* from (")
                .append(" select count(1) as counts,j1.group_id,MIN_COUNT from (")
                .append(" select t1.*,t3.MIN_COUNT from dtc_test_center_patient t1")
                .append(" left join dtc_test_center t2 on t1.TEST_CENTER_ID = t2.ID")
                .append(" left join dtc_test_center_group t3 on t3.GROUP_ID = t1.GROUP_ID")
                .append(" left join dtc_test_center t4 on t3.TEST_CENTER_ID = t4.ID")
                .append(" where t2.TEST_ID=?")
                .append(" and t2.CENTER_ID=?")
                .append(" and t4.CENTER_ID=?) as j1 ")
                .append(" group by j1.group_id) as j2 ")
                .append(" left join dtc_age_group j3 on j2.group_id = j3.ID")
                .append(" order by j3.min_age ");
	    List<Map<String,Object>> result = DBTools.getDataList(service,sql.toString(),testId,centerId,centerId);
	    return result;
    }

    @Override
    public List<DtcAgeGroupBean> getTestAgeGroups(Service service,String testId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select DISTINCT(t2.ID) ,t2.*  from dtc_test_center_group t1")
                .append(" left join dtc_age_group t2 on t1.GROUP_ID = t2.ID")
                .append(" left join dtc_test_center t3 on t1.TEST_CENTER_ID = t3.ID")
                .append(" left join dtc_test t4 on t3.TEST_ID = t4.ID")
                .append(" where t4.ID=? order by t2.MIN_AGE asc");
        List<DtcAgeGroupBean> list = DBTools.getBeanList(service,DtcAgeGroupBean.class,sql.toString(),testId);
        return list;
    }

    @Override
    public List<DtcCenterBean> getTestCenters(Service service,String testId) {
	    StringBuilder sql = new StringBuilder();
        sql.append("select t2.* from dtc_test_center t1 ")
        .append(" left join dtc_center t2 on t1.CENTER_ID = t2.ID")
        .append(" left join dtc_test t3 on t1.TEST_ID = t3.ID")
        .append(" where t1.TEST_ID=? order by t2.CREATE_TIME asc");
        List<DtcCenterBean> list = DBTools.getBeanList(service,DtcCenterBean.class,sql.toString(),testId);
        return list;
	}

    @Override
    public boolean createTestNumbers(Service service, String testId,int counts) {
	    List<Integer> result = new ArrayList<>();
	    for(int i=1;i<=counts;i++){
            result.add(i);
        }
        Collections.shuffle(result);
        List<DtcTestNumberBean> numberBeanList = new ArrayList<>();
        for(Integer number : result){
            DtcTestNumberBean numberBean = new DtcTestNumberBean();
            numberBean.setNumber(number);
            numberBean.setNumberState(0);
            numberBean.setTestId(testId);
            numberBeanList.add(numberBean);
        }
        int[] bacth = DBTools.bacthInsertBean(service,DtcTestNumberBean.class,numberBeanList);
        if(bacth.length==numberBeanList.size()){
            return true;
        }
        return false;
    }

    @Override
	public boolean checkPermission(Service service, DtcTestCenterPatientBean patient, DtcTestCenterBean testCenterBean) {
		// 1.中心最多人数>0
		// 2.中心最多人数-（中心其它年龄最少人数总和）>0
		int count = getCenterOtherGroupCount(service, testCenterBean.getId(), patient);
		if (testCenterBean.getCenterLeft() > 0 && count > 0 &&  testCenterBean.getCenterMax() > count) {
			return true;
		}
		// 3.获取中心其他年龄的最少人数之和
		return false;
	}

	@Override
	public DtcTestBean getCurrentTest(Service service) {
		String sql = "select * from DTC_TEST where state=?";
		DtcTestBean test = DBTools.getBean(service, DtcTestBean.class, sql, DtcTestBean.DOING);
		return test;
	}

	@Override
	public DtcTestCenterBean getCurrentTestCenter(Service service, String testId, String centerId) {
		String sql = "select * from DTC_TEST_CENTER where TEST_ID=? AND CENTER_ID=?";
		DtcTestCenterBean testCenter = DBTools.getBean(service, DtcTestCenterBean.class, sql, testId, centerId);
		return testCenter;
	}

	@Override
	public int getCenterOtherGroupCount(Service service, String testCenterId, DtcTestCenterPatientBean patient) {
		int age = patient.getAge();
		// 1.首先查询年龄是属于哪一组
		String sql = "select * from dtc_age_group where MIN_AGE<=? and MAX_AGE>=?";
		DtcAgeGroupBean group = DBTools.getBean(service, DtcAgeGroupBean.class, sql, age, age);
		if (group != null) {
			// 2.根据组号,查询对应其他所有组的成员总和
			Map<String, Object> result = DBTools.getData(service, "select SUM(min_count) as sums from dtc_test_center_group where group_id<>? and test_center_id=?", group.getId(),testCenterId);
			if (result.size() > 0 && Integer.parseInt(result.get("sums").toString()) > 0) {
				return Integer.parseInt(result.get("sums").toString());
			} else {
				LOG.error("dtc_age_group无数据");
				return -1;
			}
		}
		return 0;
	}

    @Override
    public DtcAgeGroupBean getDtcTestGroup(Service service, int age, String testId, String centerId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select  t2.* from dtc_test_center_group t1 ")
        .append(" left join  dtc_age_group t2 on t1.GROUP_ID = t2.ID")
        .append(" left join dtc_test_center t3 on t1.TEST_CENTER_ID = t3.ID ")
        .append(" where t2.MIN_AGE<=? and t2.MAX_AGE>=? and t3.TEST_ID =?")
        .append(" and t3.CENTER_ID=?");
        DtcAgeGroupBean ageGroupBean = DBTools.getBean(service,DtcAgeGroupBean.class,sql.toString(),age,age,testId,centerId);
	    return ageGroupBean;
    }

    @Override
	public int getTestNumberCacheAndAssign(Service service, String testId, String testCenterId, DtcTestCenterPatientBean patient) {
		int ramdomNum = 0;
	    int age = patient.getAge();
		// 1.首先查询年龄是属于这次试验的哪一组
		DtcAgeGroupBean group = getDtcTestGroup(service,age,testId,patient.getCenterId());
		// 2.获取对应年龄段的缓存信息
		if (group != null) {
			List<DtcTestNumberCacheBean> cacheBeanList = DBTools.getBeanList(service, DtcTestNumberCacheBean.class,
					"select * from DTC_TEST_NUMBER_CACHE where test_center_id=? AND group_id=?", testCenterId, group.getId());
			if (cacheBeanList == null || cacheBeanList.size() == 0) {
				// 3.1从试验中心获取对应的4条随机号,插入对应中心缓存中
				boolean initFlag = initAgeNumberCache(service, patient.getCenterId(), testId, testCenterId, group);
				if (initFlag) {
					// 3.2初始化完成后,继续获取
					cacheBeanList = DBTools.getBeanList(service, DtcTestNumberCacheBean.class,
							"select * from DTC_TEST_NUMBER_CACHE where test_center_id=? AND group_id=?", testCenterId, group.getId());
				}

			}
			// 4.获取到缓存中的号码
			if (cacheBeanList != null) {
				DtcTestNumberCacheBean cacheBean = cacheBeanList.get(0);
				// 5.分配号码(首先将缓存中的号码取出来,然后将号码分配给该人员,同时将必要的信息回写给号码分配中心)
				patient.setNumberId(cacheBean.getNumberId());
				DtcCenterBean center = getCenter(service, patient.getCenterId());
				patient.setPatientNumber(center.getCenterNo() + DateFormat.format(new Date(), DateFormat.DATE_FORMAT_4));
				//对应分组
                patient.setGroupId(group.getId());
				int i = DBUtils.update(service, patient);
				if (i > 0) {
					// 1)删除缓存中对应记录
					DBTools.deleteBean(service, cacheBean);
					// 2)回写dtc_test_number中的number_state,assign_time,center_id,patient_id
					DtcTestNumberBean number = DBUtils.getBean(service, DtcTestNumberBean.class, cacheBean.getNumberId());
					if (number != null) {
						number.setAssignTime(new Date());
						number.setNumberState(DtcTestNumberBean.NUMBER_STATE_ISASSIGN);
						number.setCenterId(patient.getCenterId());
						number.setPatientId(patient.getId());
						int j = DBUtils.update(service, number);
						if (j > 0) {
							// 6.分配完成后,需要将dtc_test_center中的center_left减少一个
							DtcTestCenterBean testCenterBean = DBUtils.getBean(service, DtcTestCenterBean.class, testCenterId);
							testCenterBean.setCenterLeft(testCenterBean.getCenterLeft() - 1);
							int k = DBUtils.update(service, testCenterBean);
							if (k <= 0) {
								LOG.error("更新dtc_test_center失败");
							} else {
                                ramdomNum = number.getNumber();
								LOG.warn("分配成功!");
							}
						} else {
							LOG.error("更新dtc_test_number失败");
						}
					} else {
						LOG.error("获取dtc_test_number失败");
					}
				} else {
					LOG.error("更新dtc_test_center_patient失败");
				}

			}

		}else{
            LOG.error("该年龄不在测试年龄段的范围之内,请输入正确的病人!");
        }

		return ramdomNum;
	}

	/**
	 * 初始化缓存
	 * 
	 * @param service
	 * @param centerId
	 * @param testCenterId
	 * @return
	 */
	private boolean initAgeNumberCache(Service service, String centerId, String testId, String testCenterId, DtcAgeGroupBean group) {
		// 1.从试验总部获取对应的随机数
		List<DtcTestNumberBean> testNumbers = getTestNumber(service, testId);
		if (testNumbers != null && testNumbers.size() > 0) {
			// 2.根据获取到的数据,进行下一步操作
			List<DtcTestNumberCacheBean> cacheBeans = new ArrayList<>();
			for (DtcTestNumberBean testNumber : testNumbers) {
				DtcTestNumberCacheBean dtcCache = new DtcTestNumberCacheBean();
				dtcCache.setGroup_name(testNumber.getGroupName());
				dtcCache.setGroupId(group.getId());
				dtcCache.setNumberId(testNumber.getId());
				dtcCache.setTestCenterId(testCenterId);
				cacheBeans.add(dtcCache);
			}
			BeanDAO dao = new BeanDAO(service);
			BeanSQL query = dao.getQuerySQL();
			query.setEntityClass(DtcTestNumberCacheBean.class);
			query.createBatchInsertSql(cacheBeans);
			int[] result = dao.batchUpdate();
			if (result.length == testNumbers.size()) {
				return true;
			} else {
				LOG.error("插入的缓存数量和获取的缓存数量不一致!");
				service.rollback();
			}

		} else {
			LOG.error("已经没有随机号提供!试验即将结束");
		}
		return false;
	}

	private String[] randomAABB(String randomNum, int listSize) {
		char[] charArr = randomNum.toCharArray();
		List<String> list = new ArrayList<>();
		for (char chars : charArr) {
			list.add(chars + "");
		}
		Collections.shuffle(list);
		String[] result = new String[listSize];
		for (int i = 0; i < listSize; i++) {
			result[i] = list.get(i);
		}
		return result;
	}

	/**
	 * 比较当前AB的试验人数谁多
	 * 
	 * @param service
	 * @param testId
	 * @return 0 相等,>0 A大 <0 B大
	 */
	private int compareABFromCenter(Service service, String testId) {
		StringBuilder sql = new StringBuilder();
		// 已分配的
		sql.append("select count(1) AS counts from  dtc_test_number t1").append(" where t1.number_state=1 or t1.number_state=2 ").append(" and t1.test_id=?")
				.append(" and t1.group_name=?");
		Map<String, Object> resultA = DBTools.getData(service, sql.toString(), testId, "A");
		Map<String, Object> resultB = DBTools.getData(service, sql.toString(), testId, "B");
		int countA = Integer.parseInt(resultA.get("counts").toString());
		int countB = Integer.parseInt(resultB.get("counts").toString());
		return countA - countB;
	}

	/**
	 * 获取对应试验未被分配的号码(获取4个)
	 * 
	 * @param service
	 * @param testId
	 * @return
	 */
	private List<DtcTestNumberBean> getTestNumber(Service service, String testId) {
		// 只需要比较test_number表中AB谁多
		int compare = compareABFromCenter(service, testId);
		// 保证A,B间隔排列不能出现同时AABB或者BBAA的情况
		// 1.先检查该中心实际上的A,B组的相差人数
		// 如果A比B多则,AABBB中取四个
		// 如果B比A多则,AAABB中随机取四个
		// 如果A和B相等则,AABB中随机取四个
		String AABB = "AABB";
		String sql = "select * from dtc_test_number where number_state=? and test_id = ? limit 4";
		List<DtcTestNumberBean> list = DBTools.getBeanList(service, DtcTestNumberBean.class, sql, DtcTestNumberBean.NUMBER_STATE_NOTASSIGN, testId);
		if (list != null && list.size() > 0) {
			// 从中心获取对应的4条数据
			// 如果中心<4条数据,则全部拿过来
			if (list.size() == 3) {
				if (compare > 0) {
					AABB = "ABB";
				} else if (compare < 0) {
					AABB = "AAB";
				}
			} else if (list.size() == 2) {
				AABB = "AB";
			} else if (list.size() == 1) {
				if (compare > 0) {
					AABB = "B";
				} else if (compare < 0) {
					AABB = "A";
				}
			} else {
				if (compare > 0) {
					AABB = "AABBB";
				} else if (compare < 0) {
					AABB = "AAABB";
				}
			}
			String[] randomAABB = randomAABB(AABB, list.size());
			for (int i = 0; i < list.size(); i++) {
				DtcTestNumberBean bean = list.get(i);
				bean.setNumberState(DtcTestNumberBean.NUMBER_STATE_ISCACHE);
				// 从缓存中缓存的时间
				bean.setCacheTime(new Date());
				bean.setGroupName(randomAABB[i]);
			}
			BeanDAO dao = new BeanDAO(service);
			BeanSQL query = dao.getQuerySQL();
			query.setEntityClass(DtcTestNumberBean.class);
			query.createBatchSql("update dtc_test_number set number_state=#numberState#,cache_time=#cacheTime#,group_name=#groupName# where ID=#id#", list);
			int[] result = dao.batchUpdate();
			if (result.length == list.size()) {
				return list;
			}
		}
		return null;
	}

	public static void main(String[] args) {
		String ramdomNum = "AABBB";
		int length = ramdomNum.length() >= 4 ? 4 : ramdomNum.length();
		char[] charArr = ramdomNum.toCharArray();
		List<String> list = new ArrayList<>();
		for (char chars : charArr) {
			list.add(chars + "");
		}
		Collections.shuffle(list);
		String[] result = new String[length];
		for (int i = 0; i < length; i++) {
			result[i] = list.get(i);
		}
	}

}