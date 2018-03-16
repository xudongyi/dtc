package com.business.dtc.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.rose.jdbc.dao.BeanDAO;
import net.sf.rose.jdbc.query.BeanSQL;
import net.sf.rose.jdbc.service.Service;

import com.business.dtc.bean.*;
import com.business.dtc.service.DtcTestService;
import com.business.dtc.util.DBTools;

/**
 * @author: xudy
 * @date: 2018/03/14 16:54
 * @description:
 */
@org.springframework.stereotype.Service
public class DtcTestServiceImpl implements DtcTestService {

	@Override
	public boolean checkPermission(Service service, DtcTestCenterPatient patient, DtcTestCenterBean testCenterBean) {
		// 1.中心最多人数>0
		// 2.中心最多人数-（中心其它年龄最少人数总和）>0
		int count = getCenterOtherGroupCount(service, testCenterBean.getId(), patient);
		if (count > 0 && testCenterBean.getCenterLeft() > 0 && testCenterBean.getCenterMax() > count) {
			return true;
		}
		// 3.获取中心其他年龄的最少人数之和
		return false;
	}

	@Override
	public DtcAgeGroupBean getAgeGroupByAge(Service service, int age) {
		return null;
	}

	@Override
	public DtcTestBean getCurrentTest(Service service) {
		return null;
	}

	@Override
	public DtcTestCenterBean getCurrentTestCenter(Service service, String testId, String centerId) {
		return null;
	}

	@Override
	public int getCenterOtherGroupCount(Service service, String testCenterId, DtcTestCenterPatient patient) {
		int age = patient.getAge();
		// 1.首先查询年龄是属于哪一组
		String sql = "select * from dtc_age_group where MIN_AGE<=? and MAX_AGE>=?";
		DtcAgeGroupBean group = DBTools.getBean(service, DtcAgeGroupBean.class, sql, age, age);
		if (group != null) {
			// 2.根据组号,查询对应其他所有组的成员总和
			Map<String, Object> result = DBTools.getData(service, "select SUM(min_count) as sums from dtc_test_center_group  where id<>?", group.getId());
			if (result.size() > 0 && Integer.parseInt(result.get("sums").toString()) > 0) {
				return Integer.parseInt(result.get("sums").toString());
			} else {
				System.out.println("无数据");
				return -1;
			}
		}
		return 0;
	}

	@Override
	public boolean getTestNumberCacheAndAssign(Service service, String testId, String testCenterId, DtcTestCenterPatient patient) {
		int age = patient.getAge();
		// 1.首先查询年龄是属于哪一组
		String sql = "select * from dtc_age_group where MIN_AGE<=? and MAX_AGE>=?";
		DtcAgeGroupBean group = DBTools.getBean(service, DtcAgeGroupBean.class, sql, age, age);
		// 2.获取对应年龄段的缓存信息
		if (group != null) {
			List<DtcTestNumberCacheBean> cacheBeanList = DBTools.getBeanList(service, DtcTestNumberCacheBean.class,
					"select * from DTC_TEST_NUMBER_CACHE where test_center_id=? AND group_id=?", testCenterId, group.getId());
			if (cacheBeanList == null || cacheBeanList.size() == 0) {
				// 3.1从试验中心获取对应的4条随机号,插入对应中心缓存中
				boolean initFlag = initAgeNumberCache(service, patient.getCenterId(), testId, testCenterId);
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

				// 6.分配完成后,需要将dtc_test_center中的center_left减少一个
			}

		}

		return false;
	}

	/**
	 * 初始化缓存
	 * 
	 * @param service
	 * @param centerId
	 * @param testCenterId
	 * @return
	 */
	private boolean initAgeNumberCache(Service service, String centerId, String testId, String testCenterId) {
		// 保证A,B间隔排列不能出现同时AABB或者BBAA的情况
		// 1.先检查该中心实际上的A,B组的相差人数
		// 如果A比B多则,AABBB中取四个
		// 如果B比A多则,AAABB中随机取四个
		// 如果A和B相等则,AABB中随机取四个
		String AABB = "AABB";
		// 从中心获取对应的4条数据
		// 如果中心<4条数据,则全部拿过来
		List<DtcTestNumberBean> testNumbers = getTestNumber(service, testId);
		if (testNumbers != null && testNumbers.size()>0) {
            int compare = compareABFromCenter(service, centerId, testCenterId);
            if(testNumbers.size()==3){
                if (compare > 0) {
                    AABB = "ABB";
                } else if (compare < 0) {
                    AABB = "AAB";
                }
            }else if(testNumbers.size()==2){
                if (compare > 0) {
                    AABB = "AABBB";
                } else if (compare < 0) {
                    AABB = "AAABB";
                }
            }else if(testNumbers.size()==1){
                if (compare > 0) {
                    AABB = "B";
                } else if (compare < 0) {
                    AABB = "A";
                }
            }else{
                if (compare > 0) {
                    AABB = "AABBB";
                } else if (compare < 0) {
                    AABB = "AAABB";
                }
            }

            String[] randomAABB = randomAABB(AABB);
		} else {
			System.out.println("已经没有随机号提供!试验即将结束");
		}
		return false;
	}

	private String[] randomAABB(String ramdomNum) {
		String[] result = new String[4];

		return result;
	}

	/**
	 * 比较当前AB的试验人数谁多
	 * 
	 * @param service
	 * @param centerId
	 * @param testCenterId
	 * @return 0 相等,>0 A大 <0 B大
	 */
	private int compareABFromCenter(Service service, String centerId, String testCenterId) {
		StringBuilder sql = new StringBuilder();
		// 已分配的
		sql.append("select count(*) AS counts from dtc_test_center_patient t1 left join dtc_test_number t2").append(" on t1.number_id = t2.id ")
				.append(" where t2.number_state=3").append(" and t1.center_id=?").append(" and t1.test_center_id=?").append(" and group_name=?");
		Map<String, Object> resultA = DBTools.getData(service, sql.toString(), centerId, testCenterId, "A");
		Map<String, Object> resultB = DBTools.getData(service, sql.toString(), centerId, testCenterId, "B");
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
		String sql = "select * from dtc_test_number where number_state=? and test_id = ? limit 4";
		List<DtcTestNumberBean> list = DBTools.getBeanList(service, DtcTestNumberBean.class, sql, DtcTestNumberBean.NUMBER_STATE_NOTASSIGN, testId);
		if (list != null && list.size() > 0) {
			for (DtcTestNumberBean bean : list) {
				bean.setNumberState(DtcTestNumberBean.NUMBER_STATE_ISCACHE);
				// 缓存的时间
				bean.setCacheTime(new Date());
			}
			BeanDAO dao = new BeanDAO(service);
			BeanSQL query = dao.getQuerySQL();
			query.setEntityClass(DtcTestNumberBean.class);
			query.createBatchSql("update dtc_test_number set number_state=#numberState#,cache_time=#cacheTime# where ID=#id#", list);
			int[] result = dao.batchUpdate();
			if (result.length == list.size()) {
				return list;
			}
		}
		return null;
	}

}