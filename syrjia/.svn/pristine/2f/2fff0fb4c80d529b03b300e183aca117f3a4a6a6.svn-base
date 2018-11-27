package cn.syrjia.hospital.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.syrjia.hospital.entity.Department;
import cn.syrjia.hospital.entity.DoctorApplyRecord;
import cn.syrjia.hospital.entity.DoctorReply;
import cn.syrjia.hospital.entity.IllnessOrDiscomfortClass;
import cn.syrjia.hospital.entity.Symptom;
import cn.syrjia.hospital.entity.TechnicalTitle;
import cn.syrjia.hospital.service.HospitalService;
import cn.syrjia.util.RedisUtil;
import cn.syrjia.util.SessionUtil;

/**
 * @author liwenlong
 */
@Controller
@RequestMapping("/hospital")
public class HospitalController {

	@Resource(name = "hospitalService")
	HospitalService hospitalService;

	/**
	 * 获取疾病分类
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/getDocIllList")
	@ResponseBody
	public Map<String, Object> getDocApplyIllList(DoctorApplyRecord doctor) {
		System.out.println(doctor.getApplyId());
		// 获取疾病分类
		List<IllnessOrDiscomfortClass> allIllList = new ArrayList<IllnessOrDiscomfortClass>();
		Object obj = RedisUtil.getVal("illClass");
		if (null != obj) {
			allIllList = (List<IllnessOrDiscomfortClass>) obj;
		} else {
			//疾病或不适分类
			IllnessOrDiscomfortClass ill = new IllnessOrDiscomfortClass();
			ill.setIllClassIsOn("1");
			ill.setIllClassStatus("10");
			//根据实体查询
			allIllList = hospitalService.query(ill);
			RedisUtil.setVal("illClass", allIllList);
		}

		Map<String, Object> map = new HashMap<String, Object>();
		// 获取该医生擅长疾病
		/*
		 * List<IllnessOrDiscomfortClass> docIllList = new
		 * ArrayList<IllnessOrDiscomfortClass>(); if
		 * (!StringUtil.isEmpty(doctor.getApplyId())) { DoctorApplyRecord doc =
		 * hospitalService.queryById( DoctorApplyRecord.class,
		 * doctor.getApplyId(), true); if (doc != null) { docIllList =
		 * doc.getIllList(); } map.put("docIllList", docIllList); }
		 */
		map.put("allIllList", allIllList);
		return map;
	}

	/**
	 * 查询
	 * @param orderNo
	 * @return
	 */
	@RequestMapping("/queryHasSym")
	@ResponseBody
	public Object queryHasSym(String orderNo) {
		Symptom s = new Symptom();
		s.setOrderNo(orderNo);
		List<Symptom> sym = hospitalService.query(s, false);
		return sym;
	}

	/**
	 * 查询症状描述
	 * 
	 * @param symptom
	 * @return
	 */
	@RequestMapping("/querySymptom")
	@ResponseBody
	public List<Symptom> querySymptom(Symptom symptom) {
		List<Symptom> list = hospitalService.query(symptom, true);
		return list;
	}


	/**
	 * 查询留言
	 * 
	 * @return
	 */
	@RequestMapping("/queryDoctorReply")
	@ResponseBody
	public List<DoctorReply> queryDoctorReply(DoctorReply doctorReply,
			Integer page, Integer row) {
		Map<String, List<String>> setMap = new HashMap<String, List<String>>();
		// 需要排序的字段
		List<String> sortList = new ArrayList<String>();
		sortList.add("createTime");
		setMap.put("sortList", sortList);
		doctorReply.setState(0);
		/*
		 * List<DoctorReply> doctorReplys = hospitalService.queryListByPages(
		 * doctorReply, page, row, setMap, "desc", true); return doctorReplys;
		 */
		return null;
	}

	/**
	 * 添加留言
	 * 
	 * @param doctorReply
	 * @return
	 */
	@RequestMapping("/addDoctorReply")
	@ResponseBody
	public DoctorReply addDoctorReply(HttpServletRequest request,
			DoctorReply doctorReply) {
		try {
			String openId = SessionUtil.getOpenId(request);
			doctorReply.setOpenId(openId);
			/*
			 * String doctorId = SessionUtil.isDoctor(request);
			 * doctorReply.setDoctorId(doctorId); doctorReply .setType(null ==
			 * doctorId || "".equals(doctorId.trim()) ? 1 : 0);
			 */
			doctorReply
					.setCreateTime((int) (System.currentTimeMillis() / 1000));
			//添加回复
			return hospitalService.addDoctorReply(doctorReply);
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 会员向医生发送请求 推送给医生模板消息
	 * 
	 * @param openId
	 * @return
	 */
	/*
	 * @RequestMapping(value="sendMsgToDoc")
	 * 
	 * @ResponseBody public Map<String,Object> sendMsgToDoc(String docId,String
	 * openId,HttpServletRequest request){ return
	 * hospitalService.sendMsgToDoc(request,docId,openId,0); }
	 */

	/**
	 * 会员向医生发送请求 推送给医生模板消息
	 * @param request
	 * @param serverIds
	 * @return
	 */
	@RequestMapping(value = "returnMap")
	@ResponseBody
	public List<Map<String, String>> returnMap(HttpServletRequest request,
			@RequestParam(value = "serverIds") String[] serverIds) {
		return hospitalService.returnMap(request, serverIds);
	}

	/**
	 * 挑转到订单任务
	 * @param request
	 * @param docId
	 * @param buyNum
	 * @return
	 */
	@RequestMapping(value = "toOrderTask")
	public String toOrderTask(HttpServletRequest request, String docId,
			Integer buyNum) {
		request.setAttribute("docId", docId);
		request.setAttribute("buyNum", buyNum);
		return "hospital/orderTask";
	}

	/**
	 * 通过id查询订单任务
	 * @param docId
	 * @param page
	 * @param row
	 * @return
	 */
	@RequestMapping(value = "queryOrderTaskById")
	@ResponseBody
	public List<Map<String, Object>> queryOrderTaskById(String docId,
			Integer page, Integer row) {
		return hospitalService.queryOrderTaskByDocId(docId, page, row);
	}

	/**
	 * 获取部门列表
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/getDepartList")
	@ResponseBody
	public Map<String, Object> getDepartList(DoctorApplyRecord doctor) {
		// 获取医院列表
		List<Department> departList = new ArrayList<Department>();
		Object obj = RedisUtil.getVal("depart");
		if (null != obj) {
			departList = (List<Department>) obj;
		} else {
			Department depart = new Department();
			departList = hospitalService.query(depart);
			RedisUtil.setVal("depart", departList);
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("departList", departList);
		map.put("docDepartList", null);
		return map;
	}

	/**
	 * 获取部门列表
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/getTechnicalTitleList")
	@ResponseBody
	public Map<String, Object> getTechnicalTitleList(DoctorApplyRecord doctor) {
		// 获取医院列表
		List<TechnicalTitle> titleList = new ArrayList<TechnicalTitle>();
		Object obj = RedisUtil.getVal("technicalTitle");
		if (null != obj) {
			titleList = (List<TechnicalTitle>) obj;
		} else {
			TechnicalTitle title = new TechnicalTitle();
			titleList = hospitalService.query(title);
			RedisUtil.setVal("technicalTitle", titleList);
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("titleList", titleList);
		map.put("doc", null);
		return map;
	}


}