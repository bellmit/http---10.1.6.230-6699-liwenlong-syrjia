package cn.syrjia.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.syrjia.config.configCode;
import cn.syrjia.entity.PatientData;
import cn.syrjia.service.PatientDataService;
import cn.syrjia.util.GetOpenId;
import cn.syrjia.util.Util;
import cn.syrjia.wxPay.wxPay.util.StringUtil;

/**
 * 患者资料控制器
 */
@Controller
@RequestMapping("/patientData")
public class PatientDataController {

	@Resource(name = "patientDataService")
	private PatientDataService patientDataService;

	/**
	 * 查询患者信息
	 * 
	 * @param request
	 * @param memberId
	 */
	@RequestMapping("/queryPatientDataList")
	@ResponseBody
	public Map<String, Object> queryPatientDataList(HttpServletRequest request,
			PatientData patientData) {
		String memberId = GetOpenId.getMemberId(request);
		//查询
		return patientDataService.queryPatientList(memberId);
	}

	/**
	 * 根据ID查询患者信息
	 * 
	 * @param request
	 */
	@RequestMapping("/queryPatientDataById")
	@ResponseBody
	public Map<String, Object> queryPatientDataById(HttpServletRequest request,
			String id) {
		String memberId = GetOpenId.getMemberId(request);
		//非空判断
		if (StringUtil.isEmpty(memberId) || StringUtil.isEmpty(id)) {
			return Util.resultMap(configCode.code_1029, null);
		}
		//通过id查询患者信息
		Map<String, Object> patientData = patientDataService
				.queryPatientsById(id);
		if (null == patientData
				|| !patientData.get("memberId").equals(memberId)) {
			return Util.resultMap(configCode.code_1015, patientData);
		}
		return Util.resultMap(configCode.code_1001, patientData);
	}

	/**
	 * 查询默认患者信息
	 * 
	 * @param request
	 */
	@RequestMapping("/queryPatientByDefault")
	@ResponseBody
	public Map<String, Object> queryPatientByDefault(HttpServletRequest request) {
		String memberId = GetOpenId.getMemberId(request);
		return patientDataService.queryPatientByDefault(memberId);
	}

	/**
	 * 添加患者信息
	 * 
	 * @param request
	 * @param patientData
	 *            地址实体
	 */
	@RequestMapping("/addPatientData")
	@ResponseBody
	public Map<String, Object> addPatientData(HttpServletRequest request,
			PatientData patientData) {
		String memberId = GetOpenId.getMemberId(request);

		patientData.setMemberId(memberId);
		//执行添加
		return patientDataService.addPatientData(request, patientData);
	}

	/**
	 * 修改患者信息
	 * 
	 * @param request
	 */
	@RequestMapping("/updatePatientData")
	@ResponseBody
	public Map<String, Object> updatePatientData(HttpServletRequest request,
			PatientData patientData) {
		String memberId = GetOpenId.getMemberId(request);
		patientData.setMemberId(memberId);
		//修改地址信息
		return patientDataService.updatePatientData(patientData);
	}

	/**
	 * 删除患者信息（假删）
	 * 
	 * @param request
	 */
	@RequestMapping("/deletePatientData")
	@ResponseBody
	public Map<String, Object> deletePatientData(HttpServletRequest request,
			String id) {
		return patientDataService.deletePatient(id);
	}

	/**
	 * 查询患者关希列表
	 * 
	 * @param request
	 */
	@RequestMapping("/queryPatientNexus")
	@ResponseBody
	public Map<String, Object> queryPatientNexus(HttpServletRequest request) {
		return patientDataService.queryPatientNexusList();
	}

	/**
	 * 设置默认就诊人
	 * 
	 * @Description: TODO
	 * @param @return
	 * @return Map<String,Object>
	 * @throws
	 * @date 2018-3-26
	 */
	@RequestMapping("/defPatient")
	@ResponseBody
	public Map<String, Object> defPatient(HttpServletRequest request, String id) {
		return patientDataService.defPatient(id);
	}

	/**
	 * 查询已有就诊人个数
	 * 
	 * @param memberId
	 * @return
	 */
	@RequestMapping("/queryPatientCount")
	@ResponseBody
	public Map<String, Object> queryPatientCount(HttpServletRequest request,
			String memberId) {
		return patientDataService.queryPatientCount(request, memberId);
	}

}
