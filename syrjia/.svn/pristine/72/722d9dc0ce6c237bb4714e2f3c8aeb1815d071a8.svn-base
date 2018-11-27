package cn.syrjia.hospital.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import cn.syrjia.common.impl.BaseServiceImpl;
import cn.syrjia.config.configCode;
import cn.syrjia.hospital.dao.AppDoctorDao;
import cn.syrjia.hospital.dao.DoctorDao;
import cn.syrjia.hospital.dao.DoctorOrderDao;
import cn.syrjia.hospital.dao.KnowledgeDao;
import cn.syrjia.hospital.entity.Department;
import cn.syrjia.hospital.entity.Doctor;
import cn.syrjia.hospital.service.DoctorService;
import cn.syrjia.util.GetOpenId;
import cn.syrjia.util.StringUtil;
import cn.syrjia.util.Util;
import cn.syrjia.weixin.util.StringUtils;

@Service("doctorService")
public class DoctorServiceImpl extends BaseServiceImpl implements DoctorService {

	@Resource(name = "doctorDao")
	DoctorDao doctorDao;
	
	@Resource(name = "doctorOrderDao")
	DoctorOrderDao doctorOrderDao;

	@Resource(name = "appDoctorDao")
	AppDoctorDao appDoctorDao;
	
	@Resource(name = "knowledgeCircleDao")
	KnowledgeDao knowledgeCircleDao;
	
	@Override
	public Map<String, Object> queryDoctorList(HttpServletRequest request,
			Doctor doctor, Integer page, Integer row, String memberId,
			String searchSort, String illClassId, String area, String city,String province) {
		if (StringUtils.isEmpty(memberId)) {
		    memberId = GetOpenId.getMemberId(request);
			if (StringUtils.isEmpty(memberId)) {
				return Util.resultMap(configCode.code_1029, "");
			}
		}
		String lockDoctorIds = null;
		if (StringUtil.isEmpty(doctor.getIsRecommended())) {
			Map<String,Object> map = doctorDao.queryLockPatientDoctor(memberId);
			if(map!=null&&!StringUtil.isEmpty(map.get("lockDoctorId"))){
				lockDoctorIds = map.get("lockDoctorId").toString();
			}
		}
		List<Map<String,Object>> ids = new ArrayList<Map<String,Object>>();
		if(!StringUtil.isEmpty(lockDoctorIds)){
			ids = doctorDao.queryScanAndJsIds(memberId,null);
		}
		
		List<Map<String, Object>> doctorList = doctorDao.queryDoctorList(
				request, doctor, page, row, memberId, searchSort, illClassId,
				area,ids,city,province);
		return Util.resultMap(configCode.code_1001, doctorList);
	}

	@Override
	public Map<String, Object> queryDoctorSet(String doctorId) {
		Map<String, Object> map = doctorDao.queryDocotrSetById(doctorId);
		if(map==null){
			map = doctorDao.queryDocotrSetDefault();
		}
		if(map!=null){
			if("null".equals(map.get("gesturePassword"))){
				map.put("gesturePassword","");
			}
			if("null".equals(map.get("secretPassword"))){
				map.put("secretPassword","");
			}
		}
		return map;
	}

	@Override
	public List<Map<String, Object>> queryPositions() {
		return doctorDao.queryPositions();
	}

	@Override
	public List<Map<String, Object>> queryDeparts() {
		return doctorDao.queryDeparts();
	}

	

	@Override
	public Map<String, Object> queryDocotrById(HttpServletRequest request,
			String doctorId, String memberId) {
		Integer s = Util.queryNowTime();
		if (StringUtils.isEmpty(memberId)) {
			memberId = GetOpenId.getMemberId(request);
			if (StringUtils.isEmpty(memberId)) {
				return Util.resultMap(configCode.code_1029, "");
			}
		}
		Map<String, Object> docMap = doctorDao.queryDocotrById(doctorId,
				memberId);
		if(docMap==null){
			return Util.resultMap(configCode.code_1032, null);
		}
		if (docMap!=null&&"0".equals(docMap.get("isRecommended"))) {
			Map<String,Object> map = doctorDao.queryLockPatientDoctor(memberId);
			if(map!=null&&!StringUtil.isEmpty(map.get("lockDoctorId"))){
				List<Map<String,Object>> ids = doctorDao.queryScanAndJsIds(memberId,doctorId);
				if(ids==null||ids.size()<=0){
					return Util.resultMap(configCode.code_1032, null);
				}
			}
		}
		System.out.println(Util.queryNowTime()-s);
		return Util.resultMap(configCode.code_1001, docMap);
	}
	
	@Override
	public Map<String, Object> queryDoctorByCard(String doctorId) {
		Map<String, Object> docMap = doctorDao.queryDoctorByCard(doctorId);
		if(docMap==null){
			return Util.resultMap(configCode.code_1032, null);
		}
		return Util.resultMap(configCode.code_1001, docMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> queryMyDoctorList(HttpServletRequest request,
			String memberId, Integer page, Integer row) {
		Integer s = Util.queryNowTime();
		if (StringUtils.isEmpty(memberId)) {
			memberId = GetOpenId.getMemberId(request);
			if (StringUtils.isEmpty(memberId)) {
				return Util.resultMap(configCode.code_1029, "");
			}
		}
		Map<String, Object> map = doctorDao.queryMyDoctorListWx(memberId, page,
				row);
		if(map==null){
			return Util.resultMap(configCode.code_1011, map);
		}
		System.out.println(Util.queryNowTime()-s);
		List<Map<String,Object>> list = (List<Map<String, Object>>) map.get("data");
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				if(!StringUtil.isEmpty(list.get(i).get("type"))&&Integer.valueOf(list.get(i).get("type").toString())==1){
					List<Map<String,Object>> jzList = doctorDao.queryJsRecords(list.get(i).get("orderNo").toString(), list.get(i).get("doctorId").toString(), memberId);
					list.get(i).put("jzLists", jzList);
				}
			}
		}
		map.put("data", list);
		System.out.println(Util.queryNowTime()-s);
		return Util.resultMap(configCode.code_1001, map);
	}

	@Override
	public List<Map<String, Object>> queryDoctorByKey(String searchkey) {
		return doctorDao.queryDoctorByKey(searchkey);
	}


	@Override
	public Map<String, Object> queryDoctorZzList(String doctroId, String zzDate) {
		List<Map<String, Object>> list = doctorDao.queryDoctorZzList(doctroId,
				zzDate);
		return Util.resultMap(configCode.code_1001, list);
	}

	@Override
	public Map<String, Object> queryZzDataCountByDocId(String doctorId) {
		List<Map<String, Object>> list = doctorDao
				.queryZzDataCountByDocId(doctorId);
		return Util.resultMap(configCode.code_1001, list);
	}

	@Override
	public Map<String, Object> queryDoctorEvaCensus(HttpServletRequest request,String doctorId,String memberId) {
		if(StringUtil.isEmpty(memberId)){
			memberId = GetOpenId.getMemberId(request);
		}
		if(StringUtil.isEmpty(memberId)||StringUtil.isEmpty(doctorId)){
			return Util.resultMap(configCode.code_1029, null);
		}
		Map<String, Object> map = doctorDao.queryDoctorEvaCensus(doctorId,memberId);
		return Util.resultMap(configCode.code_1001, map);
	}

	@Override
	public Map<String, Object> queryDoctorEvaList(HttpServletRequest request,String doctorId, Integer row,
			Integer page, String _sign,String memberId) {
		if(StringUtil.isEmpty(memberId)){
			memberId = GetOpenId.getMemberId(request);
		}
		if(StringUtil.isEmpty(memberId)||StringUtil.isEmpty(doctorId)){
			return Util.resultMap(configCode.code_1029, null);
		}
		List<Map<String, Object>> list = doctorDao.queryDoctorEvaList(doctorId,
				row, page, _sign,memberId);
		if(list==null){
			return Util.resultMap(configCode.code_1011, null);
		}
		for(int i=0;i<list.size();i++){
			Map<String,Object> map = doctorDao.queryJqImgs(list.get(i).get("id").toString());
			if(map!=null){
				list.get(i).put("jqimgs", map.get("imgdata"));
			}else{
				list.get(i).put("jqimgs", "");
			}
		}
		return Util.resultMap(configCode.code_1001, list);
	}

	@Override
	public Map<String, Object> queryDoctorArticleList(HttpServletRequest request,String doctorId,
			Integer row, Integer page) {
		String memberId = GetOpenId.getMemberId(request);
		if(StringUtil.isEmpty(memberId)||StringUtil.isEmpty(doctorId)){
			return Util.resultMap(configCode.code_1029, null);
		}
		List<Map<String, Object>> list = doctorDao.queryDoctorArticleList(memberId,
				doctorId, row, page);
		if(list==null){
			return Util.resultMap(configCode.code_1015, null);
		}else{
			 for(Map<String,Object> map:list){
					String labelNames=null;
					if(!StringUtil.isEmpty(map.get("labelIds"))){
						labelNames=knowledgeCircleDao.queryLabelNames(map.get("labelIds").toString());
					}
					/*Map<String,Object> info=knowledgeCircleDao.queryKnowledgeInfo(map.get("id").toString(),StringUtil.isEmpty(map.get("labelIds"))?"":map.get("labelIds").toString(), memberId);
					
					map.put("collectNum",info.get("collectNum").toString());
					if(!StringUtil.isEmpty(info.get("labelNames"))){
						map.put("labelNames",info.get("labelNames").toString());
					}else{
						map.put("labelNames",null);
					}
					map.put("replyNum",info.get("replyNum").toString());
					map.put("shareNum",info.get("shareNum").toString());
					map.put("plNum",info.get("plNum").toString());
					map.put("pointNum",info.get("pointNum").toString());
					map.put("followId",info.get("follow").toString());
					map.put("praiseId",info.get("praise").toString());
					map.put("collectId",info.get("collect").toString());
					
					
					if(info.get("follow").toString().equals(0)){
						map.put("follow",1);
					}else{
						map.put("follow",2);
					}
					
					if(info.get("praise").toString().equals(0)){
						map.put("praise",1);
					}else{
						map.put("praise",2);
					}
					
					if(info.get("collect").toString().equals(0)){
						map.put("collect",1);
					}else{
						map.put("collect",2);
					}*/
					//Integer collectNum=knowledgeCircleDao.queryCollectNum(map.get("id").toString());
					Integer replyNum=knowledgeCircleDao.queryReplyNum(map.get("id").toString(),doctorId);
					//Integer shareNum=knowledgeCircleDao.queryShareNum(map.get("id").toString());
					Integer plNum=knowledgeCircleDao.queryPlNum(map.get("id").toString(),doctorId);
					//Integer pointNum=knowledgeCircleDao.queryPointNum(map.get("id").toString());
					Integer follow=knowledgeCircleDao.queryFollow(map.get("doctorId").toString(),doctorId);
					Integer praiseId=knowledgeCircleDao.querypPraiseId(map.get("id").toString(),doctorId);
					Map<String,Object> collect=knowledgeCircleDao.querypCollectId(map.get("id").toString(),doctorId);
					//map.put("collectNum",collectNum);
					map.put("labelNames",labelNames);
					map.put("replyNum",replyNum);
					//map.put("shareNum",shareNum);
					map.put("plNum",plNum);
					//map.put("pointNum",pointNum);
					map.put("followId",follow);
					map.put("praiseId",praiseId);
					map.put("collectId",collect.get("num"));
					map.put("collectTime",collect.get("createTime"));
					if(follow==0){
						map.put("follow",1);
					}else{
						map.put("follow",2);
					}
					if(praiseId==0){
						map.put("praise",1);
					}else{
						map.put("praise",2);
					}
					if(collect.size()==0||StringUtil.isEmpty(collect.get("num"))||Integer.parseInt(collect.get("num").toString())==0){
						map.put("collect",1);
					}else{
						map.put("collect",2);
					}
				}
			return Util.resultMap(configCode.code_1001, list);
		}
	}

	@Override
	public Map<String, Object> querySevenZzStatus(String doctorId) {
		List<Map<String, Object>> list = doctorDao.querySevenZzStatus(doctorId);
		return Util.resultMap(configCode.code_1001, list);
	}


	/**
	 * 查询疾病及不时分类
	 * 
	 * @return
	 */
	@Override
	public List<Map<String,Object>> queryIllClass(Integer num) {
		return doctorDao.queryIllClass(num);
	}
	@Override
	public List<Department> queryDepartments() {
		return doctorDao.queryDepartments();
	}

	@Override
	public Map<String, Object> queryMedicalRecords(HttpServletRequest request,Integer orderType,
			String patientId, String memberId, Integer page, Integer row) {
		if(StringUtils.isEmpty(memberId)){
			memberId = GetOpenId.getMemberId(request);
			if(StringUtils.isEmpty(memberId)){
				return Util.resultMap(configCode.code_1029, "");
			}
		}
		List<Map<String,Object>> records = doctorDao.queryMedicalRecords(orderType, patientId, memberId, page, row);
		if(records==null){
			return Util.resultMap(configCode.code_1011, "");
		}
		return Util.resultMap(configCode.code_1001, records);
	}


	@Override
	public Map<String, Object> queryMedicalRecordsCons(HttpServletRequest request,String memberId) {
		Map<String,Object> map = new HashMap<String, Object>();
		List<Map<String,Object>> serverTypes = doctorDao.queryServerTypes();
		if(StringUtils.isEmpty(memberId)){
			memberId = GetOpenId.getMemberId(request);
			if(StringUtils.isEmpty(memberId)){
				return Util.resultMap(configCode.code_1029, "");
			}
		}
		List<Map<String,Object>> patients = doctorDao.queryPatients(memberId);
		map.put("serverTypes", serverTypes);
		map.put("patients", patients);
		return Util.resultMap(configCode.code_1001, map);
	}

	@Override
	public Map<String, Object> checkDoctorIsAccpetAsk(HttpServletRequest request,String memberId,String doctorId,
			Integer orderType) {
		if(StringUtil.isEmpty(doctorId)||StringUtil.isEmpty(orderType)){
			return Util.resultMap(configCode.code_1029, "");
		}
		if(StringUtils.isEmpty(memberId)){
			memberId = GetOpenId.getMemberId(request);
			if(StringUtils.isEmpty(memberId)){
				return Util.resultMap(configCode.code_1029, "");
			}
		}
		Map<String, Object> map = doctorOrderDao.getDoctorOrderDayCount(doctorId, orderType);
		Integer isAccpetAsk = appDoctorDao.queryDocIsAccOn(doctorId);
		if(isAccpetAsk==0){
			return Util.resultMap(configCode.code_1099, "");
		}
		Map<String, Object> docMap = doctorDao.queryDocotrById(
				doctorId, memberId);
		if (docMap == null) {
			return Util.resultMap(configCode.code_1032, null);
		}
		Integer dayOrderCount = 0;

		if(map!=null){
			dayOrderCount = Integer.valueOf(map.get("dayOrderCount").toString());
		}
		Integer isOnlineTwGh = Integer.valueOf(docMap.get("isOnlineTwGh")
				.toString());
		Integer isOnlinePhoneGh = Integer.valueOf(docMap.get("isOnlinePhoneGh")
				.toString());
		Integer isOnlineTwZx = Integer.valueOf(docMap.get("isOnlineTwZx")
				.toString());
		Integer isOnlinePhoneZx = Integer.valueOf(docMap.get("isOnlinePhoneZx")
				.toString());
		if (orderType == 4 || orderType == 5) {
			if (isOnlineTwGh==0) {
				return Util.resultMap(configCode.code_1057, null);
			}else {
				if(!StringUtil.isEmpty(docMap.get("acceptTwOrderCount"))){
					Integer acceptTwOrderCount = Integer.valueOf(docMap.get("acceptTwOrderCount").toString());
					if(dayOrderCount-acceptTwOrderCount>=0){
						return Util.resultMap(configCode.code_1116, null);
					}
				}
			}
		} else if (orderType == 6) {
			if (isOnlineTwZx==0) {
				return Util.resultMap(configCode.code_1054, null);
			} else {
				if(!StringUtil.isEmpty(docMap.get("acceptTwZxOrderCount"))){
					Integer acceptTwZxOrderCount = Integer.valueOf(docMap.get("acceptTwZxOrderCount").toString());
					if(dayOrderCount-acceptTwZxOrderCount>=0){
						return Util.resultMap(configCode.code_1116, null);
					}
				}
			}
		} else if (orderType == 7
				|| orderType == 9) {
			if (isOnlinePhoneGh==0) {
				return Util.resultMap(configCode.code_1055, null);
			}else{
				if(!StringUtil.isEmpty(docMap.get("acceptPhoneOrderCount"))){
					Integer acceptPhoneOrderCount = Integer.valueOf(docMap.get("acceptPhoneOrderCount").toString());
					if(dayOrderCount-acceptPhoneOrderCount>=0){
						return Util.resultMap(configCode.code_1116, null);
					}
				}
			}
		} else if (orderType== 8) {
			if (isOnlinePhoneZx==0) {
				return Util.resultMap(configCode.code_1056, null);
			}else {
				if(!StringUtil.isEmpty(docMap.get("acceptPhoneZxOrderCount"))){
					Integer acceptPhoneZxOrderCount = Integer.valueOf(docMap.get("acceptPhoneZxOrderCount").toString());
					if(dayOrderCount-acceptPhoneZxOrderCount>=0){
						return Util.resultMap(configCode.code_1116, null);
					}
				}
			}
		}
		return Util.resultMap(configCode.code_1001, null);
	}

	@Override
	public Map<String, Object> queryIllClassById(String id) {
		Map<String,Object> map = doctorDao.queryIllClassById(id);
		return Util.resultMap(configCode.code_1001, map);
	}

}
