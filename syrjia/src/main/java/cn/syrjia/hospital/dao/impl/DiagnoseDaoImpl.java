package cn.syrjia.hospital.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import cn.syrjia.common.impl.BaseDaoImpl;
import cn.syrjia.hospital.dao.DiagnoseDao;
import cn.syrjia.hospital.entity.IllnessOrDiscomfortClass;
import cn.syrjia.util.StringUtil;

@Repository("diagnoseDao")
public class DiagnoseDaoImpl extends BaseDaoImpl implements DiagnoseDao {

	// 日志
	private Logger logger = LogManager.getLogger(DiagnoseDaoImpl.class);

	
	
	/**获取所有省份**/
	@Override
	public List<Map<String, Object>> queryAllProviceNames() {
		List<Map<String, Object>> provincesList = new ArrayList<Map<String, Object>>();
		String sql = " select provinceid,province from provinces order by provinceid  ";
		try {
			provincesList = super.queryBysqlList(sql, null);
		} catch (Exception e) {
			System.out.println(e);
		}
		return provincesList;
	}

	/**根据省份id获取下级城市**/
	@Override
	public List<Map<String, Object>> queryCitiesByProvice(String proviceId) {
		List<Map<String, Object>> citiesList = new ArrayList<Map<String, Object>>();
		String sql = " select cityid,city,provinceid from cities where provinceid=? order by cityid  ";
		try {
			citiesList = super.queryBysqlList(sql, new Object[]{proviceId});
		} catch (Exception e) {
			System.out.println(e);
		}
		return citiesList;
	}
	

	/**查询我的医生**/
	@Override
	public List<Map<String, Object>> queryMyDoctor(Integer page, Integer row, String openId) {
		List<Map<String, Object>> doclist = new ArrayList<Map<String, Object>>();
		try {
			
			String sql="select * from (SELECT d.*,infir.infirmaryName,tt.sort titleSort, de.departName,(select GROUP_CONCAT(ff.illClassName) from (SELECT ill.illClassName,miu.doctorId FROM h_middle_util miu INNER JOIN h_illness_class ill ON ill.illClassId=miu.illClassId WHERE  miu.type=1)ff where ff.doctorId=d.doctorId LIMIT 0,2) illClassName, (SELECT COUNT(1) FROM h_doctor_order o " 
				+" WHERE o.doctorId = d.doctorId AND o. STATUS = '10' AND ( o.paymentStatus = '2' OR o.paymentStatus = '5' )) buyNum,  "
				+" (SELECT COUNT(1) evaCnt FROM h_doctor_evaluate e WHERE e.evaStaus='10' and e.doctorId = d.doctorId ) evaCnt, "
				+" (SELECT COUNT(1) evaGoodCnt FROM h_doctor_evaluate e1 WHERE e1.evaStaus='10' and e1.evaluateLevel>5 and e1.doctorId = d.doctorId ) evaGoodCnt,  "
				+" ( SELECT group_concat(ddd.departName) FROM h_department ddd WHERE ddd.departId in(SELECT ddu.departId from h_doc_depart_util ddu where ddu.doctorId=d.doctorId)) departNames  "
				+" FROM h_doctor d left JOIN h_department de on de.departId =d.infirDepartId  "
				+" inner JOIN h_infirmary infir on infir.infirmaryId =d.infirmaryId and infir.state=0  "
				+" inner JOIN h_technical_title tt on tt.titleId =d.docPositionId  "
				+" INNER JOIN h_user_keep uk ON uk.doctorId=d.doctorId "
				+" INNER JOIN h_weixin_user wu ON wu.openid=uk.openid "
				+" where d.docIsOn='1' and d.docStatus='10' "
				+" AND wu.openid=?"
				+" UNION ALL	 "
				+" SELECT d.*,infir.infirmaryName,tt.sort titleSort, de.departName,(select GROUP_CONCAT(ff.illClassName) from (SELECT ill.illClassName,miu.doctorId FROM h_middle_util miu INNER JOIN h_illness_class ill ON ill.illClassId=miu.illClassId WHERE  miu.type=1)ff where ff.doctorId=d.doctorId LIMIT 0,2) illClassName, (SELECT COUNT(1) FROM h_doctor_order o  "
				+" WHERE o.doctorId = d.doctorId AND o. STATUS = '10' AND ( o.paymentStatus = '2' OR o.paymentStatus = '5' )) buyNum,  "
				+" (SELECT COUNT(1) evaCnt FROM h_doctor_evaluate e WHERE e.evaStaus='10' and e.doctorId = d.doctorId ) evaCnt, "
				+" (SELECT COUNT(1) evaGoodCnt FROM h_doctor_evaluate e1 WHERE e1.evaStaus='10' and e1.evaluateLevel>5 and e1.doctorId = d.doctorId ) evaGoodCnt,  "
				+" ( SELECT group_concat(ddd.departName) FROM h_department ddd WHERE ddd.departId in(SELECT ddu.departId from h_doc_depart_util ddu where ddu.doctorId=d.doctorId)) departNames  "
				+" FROM h_doctor d left JOIN h_department de on de.departId =d.infirDepartId  "
				+" inner JOIN h_infirmary infir on infir.infirmaryId =d.infirmaryId and infir.state=0  "
				+" inner JOIN h_technical_title tt on tt.titleId =d.docPositionId  "
				+" INNER JOIN h_doctor_order dor ON dor.doctorId=d.doctorId "
				+" INNER JOIN h_weixin_user wu ON wu.openid=dor.openid "
				+" where d.docIsOn='1' and d.docStatus='10' and (dor.paymentStatus=2 or dor.paymentStatus=5)"
				+" AND wu.openid=?) f GROUP BY f.doctorId ";
			sql += " LIMIT " + (page - 1) + "," + row;
			System.out.println(sql);
			doclist = super.queryBysqlList(sql, new Object[]{openId,openId});
		}catch(Exception ex){
			logger.info(ex.getMessage()+"\r\n"+ ex.getStackTrace());
		}
		return doclist;
	}
	
	

	/**根据关键字搜索医生**/
	@Override
	public List<Map<String, Object>> searchDoctor(Integer page, Integer row, String keyword) {
		List<Map<String, Object>> doclist = new ArrayList<Map<String, Object>>();
		try {
			//没传关键字直接返回
			if(keyword.trim().length()==0)
				return doclist;
			
			String sql = "SELECT d.doctorId,d.docName,d.docUrl,d.docSignature, infir.infirmaryName, tt.sort titleSort,tt.tecName,"
			    +" (select GROUP_CONCAT(ff.illClassName) "
			    +" 		from (SELECT ill.illClassName,miu.doctorId FROM h_middle_util miu  "
			    +" 		INNER JOIN h_illness_class ill ON ill.illClassId=miu.illClassId WHERE  miu.type=1 )ff  "
			    +" 		where ff.doctorId=d.doctorId LIMIT 0,2) illClassName,  "
			    +" 		(SELECT COUNT(1) FROM h_doctor_order o  WHERE o.doctorId = d.doctorId AND o. STATUS = '10' AND ( o.paymentStatus = '2' OR o.paymentStatus = '5' )) buyNum,   "
			    +" 		(SELECT COUNT(1) evaCnt FROM h_doctor_evaluate e WHERE e.evaStaus='10' and e.doctorId = d.doctorId ) evaCnt,   "
			    +" 		(SELECT COUNT(1) evaGoodCnt FROM h_doctor_evaluate e1 WHERE e1.evaStaus='10' and e1.evaluateLevel>5 and e1.doctorId = d.doctorId ) evaGoodCnt,   "
			    +" 		(SELECT group_concat(ddd.departName) FROM h_department ddd WHERE ddd.departId in(SELECT ddu.departId from h_doc_depart_util ddu where ddu.doctorId=d.doctorId)) departNames  " 
			    +"FROM h_doctor d  "
			    +"	left JOIN h_department de on de.departId =d.infirDepartId   "
			    +"	inner JOIN h_infirmary infir on infir.infirmaryId =d.infirmaryId and infir.state=0   "
			    +"	inner JOIN h_technical_title tt on tt.titleId =d.docPositionId   "
			    +"WHERE d.docIsOn='1' and d.docStatus='10'  "
			    +"	and (d.docName =?  "
			    +"	  or d.doctorId in(select ut.doctorId from h_department de join h_doc_depart_util ut on de.departId=ut.departId where de.departName like ? ) "
			    +"	  or d.doctorId in(select ut.doctorId from h_illness_class ill join h_middle_util ut on ut.type=1 and ill.illClassId=ut.illClassId where ill.illClassName like ? ) " 
			    +"  )"
				+" ";///TODO 需要增加order
			sql += " LIMIT " + (page - 1) + "," + row;
			System.out.println(sql);
			String doctName=keyword; //医生姓名
			String deptName=keyword;  //科室
			String illName=keyword+"%";  //疾病名称
			doclist = super.queryBysqlList(sql, new Object[]{doctName,deptName,illName});
		}catch(Exception ex){
			logger.info(ex.getMessage()+"\r\n"+ ex.getStackTrace());
		}
		return doclist;
	}

	
	
	/**获取常用疾病或不适**/
	@Override
	public List<IllnessOrDiscomfortClass> getTopIllClass() {
		List<IllnessOrDiscomfortClass> topIllList = new ArrayList<IllnessOrDiscomfortClass>();
		try {
			String sql = "SELECT c.* from h_illness_class c where c.illClassIsOn = '1' and c.illClassStatus = '10' ORDER BY c.isSort DESC LIMIT 0,7";
			topIllList = super.queryBysqlListBean(
					IllnessOrDiscomfortClass.class, sql, null);
		} catch (Exception e) {
			logger.info(e);
		}
		return topIllList;
	}
	
	/** 获取 已经使用的科室 **/
	@Override
	public List<Map<String, Object>> queryUsedDepartments() {
		List<Map<String, Object>> depts = new ArrayList<Map<String, Object>>();
		try {
			
			String sql = " select departId,departName from h_department where departId in(select departId from h_doc_depart_util) ";
			depts = super.queryBysqlList(sql, null);
			
		} catch (Exception e) {
			logger.info(e);
		}
		return depts;
	}

	/**筛选医生**/
	@Override
	public List<Map<String, Object>> filterDoctor(Integer page, Integer row, String cityId, String deptId,
			String postionId, String keyword, String sortType) {

		List<Map<String, Object>> doclist = new ArrayList<Map<String, Object>>();
		try {
			String sql = "SELECT d.*,infir.infirmaryName,tt.sort titleSort, de.departName,(select GROUP_CONCAT(ff.illClassName) from (SELECT ill.illClassName,miu.doctorId FROM h_middle_util miu INNER JOIN h_illness_class ill ON ill.illClassId=miu.illClassId WHERE  miu.type=1)ff where ff.doctorId=d.doctorId LIMIT 0,2) illClassName, (SELECT COUNT(1) FROM h_doctor_order o "
					+ " WHERE o.doctorId = d.doctorId AND o. STATUS = '10' AND ( o.paymentStatus = '2' OR o.paymentStatus = '5' )) buyNum, "
					+ " 	(SELECT COUNT(1) evaCnt FROM h_doctor_evaluate e WHERE e.evaStaus='10' and e.doctorId = d.doctorId ) evaCnt, "
					+ " 	(SELECT COUNT(1) evaGoodCnt FROM h_doctor_evaluate e1 WHERE e1.evaStaus='10' and e1.evaluateLevel>5 and e1.doctorId = d.doctorId ) evaGoodCnt, "
					+ " 	( SELECT group_concat(ddd.departName) FROM h_department ddd WHERE ddd.departId in(SELECT ddu.departId from h_doc_depart_util ddu where ddu.doctorId=d.doctorId)) departNames "
					+ " FROM h_doctor d left JOIN h_department de on de.departId =d.infirDepartId "
					+ " 	inner JOIN h_infirmary infir on infir.infirmaryId =d.infirmaryId and infir.state=0 "
					+ " 	LEFT JOIN h_doc_depart_util ddu ON ddu.doctorId=d.doctorId "
					+ " 	inner JOIN h_technical_title tt on tt.titleId =d.docPositionId "
					+ " where d.docIsOn='1' and d.docStatus='10' ";
			
					if(!StringUtil.isEmpty(keyword)){
							sql+=" and (d.docName like '"+keyword.trim()+"%'";
							sql+=" or d.doctorId in(select ut.doctorId from h_department de join h_doc_depart_util ut on de.departId=ut.departId where de.departName like '"+keyword.trim()+"%' ) ";
							sql+=" or d.doctorId in(select ut.doctorId from h_illness_class ill join h_middle_util ut on ut.type=1 and ill.illClassId=ut.illClassId where ill.illClassName like '"+keyword.trim()+"%' ) " ;
							sql+=" ) ";
					}
			/*
			if (setMap != null && setMap.size() > 0) {
				String docId = setMap.get("followId") == null ? "" : setMap.get("followId").toString();
				Integer isLock = setMap.get("isLockUser") == null ? 0 : Integer.parseInt(setMap.get("isLockUser") + "");
				if (isLock == 1) {
					sql += " and (d.isRecommended='1' or d.doctorId='" + docId	+ "'";
					if (!StringUtils.isEmpty(doc.getDoctorId())) {
						sql += " or d.doctorId='" + doc.getDoctorId() + "' ";
					}
					sql += ")";
				}
			} else {
				sql += " and (d.isProtected!=1";
				if (!StringUtils.isEmpty(doc.getDoctorId())) {
					sql += " or d.doctorId='" + doc.getDoctorId() + "'  ";
				}
				sql += ")";
			}
			if (!StringUtil.isEmpty(doc.getIsRecommended())) {
				sql += " and d.isRecommended='" + doc.getIsRecommended() + "'";
			}
			
			if (!StringUtil.isEmpty(doc.getSystemId())) {
				sql += " and d.doctorId in(select mu.doctorId from h_middle_util mu where mu.type='4' and mu.systemId='"
						+ doc.getSystemId() + "')";
			}
			if (!StringUtil.isEmpty(doc.getOpenid())) {
				sql += " and d.doctorId in(SELECT u.doctorId from h_user_keep u where u.type='0' and u.openid='"
						+ doc.getOpenid() + "')";
			}

			*/
			if (!StringUtil.isEmpty(deptId) && !"".equals(deptId)) {
				sql += " and ddu.departId = '" + deptId + "'";
			}
			if (!StringUtil.isEmpty(cityId) && !"".equals(cityId)) {
				sql += " and infir.cityName = '" + cityId + "'";
			}
			if (!StringUtil.isEmpty(postionId) && !"".equals(postionId)) {
				sql += " and tt.titleId = '" + postionId + "'";
			}
			
			if (!StringUtil.isEmpty(sortType)) {
				if ("0".equals(sortType)) {// 好评度
					sql += " order by evaGoodCnt desc ";
				} else if ("1".equals(sortType)) {// 接单数
					sql += " order by buyNum desc ";
				} else if ("2".equals(sortType)) {// 职称
					sql += " order by titleSort desc ";
				} else if ("3".equals(sortType)) {// 收费高低
					sql += " order by d.docPrice desc ";
				} else {
					sql += " order by buyNum desc ";
				}
			} else {
				sql += " order by buyNum desc ";
			}
			sql += " LIMIT " + (page - 1) + "," + row;
			
			doclist = super.queryBysqlList(sql,null);
			
		} catch (NumberFormatException e) {
			logger.info(e+"异常信息");
		}
		return doclist;
		
	}

	
	/** 获取医生信息 **/
	@Override
	public Map<String, Object> getDoctorInfo(String doctorId) {
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = "SELECT d.*,infir.infirmaryName, de.departName, (SELECT COUNT(1) FROM h_doctor_order o "
				+ " WHERE o.doctorId = d.doctorId AND o. STATUS = '10' AND ( o.paymentStatus = '2' OR o.paymentStatus = '5' )) buyNum, "
				+ " ( SELECT COUNT(1) FROM h_doctor_evaluate e WHERE e.doctorId = d.doctorId AND e.evaStaus='10' ) evaCnt, "
				+ " ( SELECT COUNT(1) FROM h_follow_history hf where hf.followId=d.doctorId and hf.type=0 ) as followCnt, "
				+ " ( SELECT group_concat(ddd.departName) FROM h_department ddd WHERE ddd.departId in(SELECT ddu.departId from h_doc_depart_util ddu where ddu.doctorId=d.doctorId)) departNames, "
				+ " (SELECT COUNT(1) from h_doctor_evaluate ev where ev.evaluateLevel>=5 and ev.evaStaus='10' and ev.doctorId=d.doctorId) goodNum"
				+ " FROM h_doctor d left JOIN h_department de on de.departId =d.infirDepartId "
				+ " inner JOIN h_infirmary infir on infir.infirmaryId =d.infirmaryId and infir.state=0 "
				+ " where d.docIsOn='1' and d.docStatus='10' ";
		if (!StringUtil.isEmpty(doctorId)) {
			sql += " and d.doctorId='" + doctorId + "'";
			map = super.queryBysqlMap(sql, null);
		}
		return map;
	}
	
}
