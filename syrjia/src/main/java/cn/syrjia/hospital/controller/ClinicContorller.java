package cn.syrjia.hospital.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.syrjia.entity.Piclib;
import cn.syrjia.hospital.entity.Hospital;
import cn.syrjia.hospital.service.ClinicService;

@Controller
@RequestMapping("clinic")
public class ClinicContorller {

	/**
	 * 挑转到就诊
	 * @param request
	 * @param hId
	 * @return
	 */
	@RequestMapping(value="toClinic")
	public String toClinic(HttpServletRequest request,String hId){
		Hospital h = clinicService.queryHosById(hId);
		request.setAttribute("h",h);
		return "hospital/clinic";
	}
	
	/**
	 * 进入地图
	 * @param request
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	@RequestMapping(value="toMap")
	public String toMap(HttpServletRequest request,String latitude,String longitude){
		request.setAttribute("latitude", latitude);
		request.setAttribute("longitude", longitude);
		return "hospital/map";
	}
	
	@Resource(name="clinicService")
	ClinicService clinicService;
	
	/**
	 * 查询医馆头信息 包括科室数量和医生数量 以及标签
	 * @param hosId
	 * @return
	 */
	@RequestMapping(value="queryTopMsg")
	@ResponseBody
	public Map<String,Object> queryTopMsg(String hosId){
		return clinicService.queryTopMsg(hosId);
	}
	/**
	 * 查询医馆下包含的科室列表
	 * @param hosId
	 * 
	 * 
	 * @return
	 */
	@RequestMapping(value="queryDepList")
	@ResponseBody
	public List<Map<String,Object>> queryDepList(String hosId){
		return clinicService.queryDepList(hosId);
	}
	/**
	 * 查询医馆介绍详情
	 * @param request
	 * @param hosId
	 * @return
	 */
	@RequestMapping(value="toRemark")
	public String toRemark(HttpServletRequest request,String hosId){
		Hospital h = clinicService.queryById(Hospital.class, hosId, true);
		request.setAttribute("h",h);
		return "hospital/clinicDetails";
	}
	/**
	 * 查询医馆介绍详情
	 * @param request
	 * @param hosId
	 * @return
	 */
	@RequestMapping(value="toImages")
	public String toImages(HttpServletRequest request,String hosId){
		request.setAttribute("hosId",hosId);
		return "hospital/clinicImages";
	}
	
	/**
	 * 通过医院id查询图片
	 * @param hosId
	 * @return
	 */
	@RequestMapping(value="queryImages")
	@ResponseBody
	public List<Piclib> queryImages(String hosId){
		Hospital h = clinicService.queryById(Hospital.class, hosId, true);
		List<Piclib> list = h.getImgList();
		return list;
	}
}
