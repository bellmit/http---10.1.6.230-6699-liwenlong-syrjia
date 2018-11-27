package cn.syrjia.hospital.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.syrjia.hospital.service.HealthProductService;

@Controller
@RequestMapping("/drug")
public class HealthProductsCenterContorller {

	
	@Resource(name = "healthProductService")
	HealthProductService healthProductService;
	
	/**
	 * 查询药品名录
	 * @param request
	 * @param type(1 颗粒 2 饮片)
	 * @param page
	 * @param row
	 * @param memberId
	 * @return
	 */
	@RequestMapping("/queryHealthProducts")
	@ResponseBody
	public Map<String,Object> queryHealthProducts(HttpServletRequest request,String doctorId,String name,String type,Integer page,Integer row){
		return healthProductService.queryHealthProducts(doctorId,name,type,page,row);
	}
	
	/**
	 * 申请增加药味
	 * @param doctorId
	 * @param content
	 * @return
	 */
	@RequestMapping("/applyAddDrug")
	@ResponseBody
	public Map<String,Object> applyAddDrug(String doctorId, String content,HttpServletRequest request){
		return healthProductService.applyAddDrug(doctorId, content);
	}
	
}
