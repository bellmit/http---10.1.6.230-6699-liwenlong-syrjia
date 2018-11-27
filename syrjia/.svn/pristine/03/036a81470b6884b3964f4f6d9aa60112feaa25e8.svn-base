package cn.syrjia.hospital.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.syrjia.entity.WeiXinUser;
import cn.syrjia.hospital.entity.IllnessOrDiscomfortClass;
import cn.syrjia.hospital.entity.UserKeep;
import cn.syrjia.hospital.service.DiagnoseService;
import cn.syrjia.hospital.service.HospitalService;
import cn.syrjia.util.GetOpenId;
import cn.syrjia.util.SessionUtil;
import cn.syrjia.util.http.HttpRequest;

@Controller
@RequestMapping(value="/diagnose")
public class DiagnoseController {

//	@Resource
//	DocCenterService docCenterService;
	
	@Resource
	DiagnoseService diagnoseService;
	
	@Resource(name = "hospitalService")
	HospitalService hospitalService;
	
	private HttpRequest requests;
	
	/** 我的医生 **/
	@RequestMapping(value="tomydoctors")
	public String toMyDoctors(HttpServletRequest request,
			HttpServletResponse response,Integer isRead) {
		String code = request.getParameter("code");
		try {
			if (code != null && !"".equals(code)) {
				/*String openId = GetOpenId.getOpenId(requests, request,
						response, code);
				GetOpenId.setpPivateService(requests, request, openId,
						docCenterService);// 是否购买了服务
				GetOpenId.setDoctorService(requests, request, openId,
						docCenterService);// 是否医馆医生
				GetOpenId.setpPivateDoctor(requests, request, openId,
						docCenterService);// 是否私人医生
				GetOpenId.setSales(requests, request, openId,
						docCenterService);// 是否医药代表
				GetOpenId.setDistri(requests, request, openId,
						docCenterService);// 是否分销商
*/			} else {
				// request.getSession().setAttribute("privateService", 0);//
				// 没有openId
			}
		} catch (Exception e) {
		}
		
		String openId = SessionUtil.getOpenId(request);
		//是否阅读了会员协议 ///TODO 应该不需要这段
//		ReadAgree ra=docCenterService.queryById(ReadAgree.class, openId, false);
//		if(ra!=null){
//			isRead=ra.getIsRead();
//		}
		request.setAttribute("isRead", isRead);
		return "diagnose/mydoctors";
	}
	
	/**找医生(在线诊疗首页)*/
	@RequestMapping(value="toindex")
	public String toConditioningIndex(HttpServletRequest request) {
		
		return "diagnose/index";
	}
	
	/** 搜索医生 **/
	@RequestMapping(value="tosearchdoctors")
	public String toSearchDoctors(HttpServletRequest request) {
		
		return "diagnose/searchdoctors";
	}
	
	/**更多医生*/
	@RequestMapping("tomoredoclist")
	public String toMoreDocList(HttpServletRequest request,String cityId,String city,String depId,String dep,String postionId,String postion) {
		request.setAttribute("cityId", cityId);
		request.setAttribute("city", city);
		request.setAttribute("depId", depId);
		request.setAttribute("dep", dep);
		request.setAttribute("postionId", postionId);
		request.setAttribute("postion", postion);
		return "diagnose/moredoclist";
	}
	/**主页医生*/
	@RequestMapping("todoctor")
	public String toDoctor(HttpServletRequest request, String doctorId) {
		doctorId= "9f7c19af76b94a78a7dcee737b0106c6";
		int i = 0;
		try {
			//获取医生信息 
			Map<String, Object> docMap = diagnoseService.getDoctorInfo(doctorId);
			request.setAttribute("doc", docMap);
			String openid = SessionUtil.getOpenId(request);
			UserKeep keep = new UserKeep();
			keep.setOpenid(openid);
			keep.setDoctorId(doctorId);
			//根据实体查询单个
			keep = diagnoseService.queryByEntity(keep);
			if (keep != null) {
				i = 1;
			}
			System.out.println("toDoctorDetail openId" + openid);
			//根据id查询单个
			WeiXinUser wxUser = diagnoseService.queryById(WeiXinUser.class,	openid);
			request.setAttribute("isKeep", i);
			request.setAttribute("wxUser", wxUser);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "diagnose/doctor";
	}
	
	

	
	

	/**获取前几位常用不适**/
	@RequestMapping("/getTopIllClass")
	@ResponseBody
	public List<IllnessOrDiscomfortClass> getTopIllClass() {
		// 获取不适及疾病分类
		List<IllnessOrDiscomfortClass> topIllClsslist = diagnoseService.getTopIllClass();
		return topIllClsslist;
	}

	/**查询我的医生**/
	@RequestMapping("/queryMyDoctors")
	@ResponseBody
	public List<Map<String, Object>> queryMyDoctors(HttpServletRequest request,	Integer page, Integer row) {
		String openId = SessionUtil.getOpenId(request);
		//我的医生
		List<Map<String, Object>> myDocList = diagnoseService.queryMyDoctor(page, row, openId);
		return myDocList;
	}

	/**搜索医生**/
	@RequestMapping("/searchDoctors")
	@ResponseBody
	public List<Map<String, Object>> searchDoctors(HttpServletRequest request,	Integer page, Integer row) {
		String keyword = request.getParameter("keyword");
		//搜索医生
		List<Map<String, Object>> doctList = diagnoseService.searchDoctor(page, row, keyword);
		//System.out.println(System.currentTimeMillis());
		return doctList;
	}
	
	/**获取所有省份**/
	@RequestMapping(value = "queryAllProviceNames")
	@ResponseBody
	public List<Map<String, Object>> queryAllProviceNames() {
		return diagnoseService.queryAllProviceNames();
	}
	
	/**获取省份下级城市**/
	@RequestMapping(value = "queryCitiesByProvice")
	@ResponseBody
	public List<Map<String, Object>> queryCitiesByProvice(String proviceId) {
		return diagnoseService.queryCitiesByProvice(proviceId);
	}
	
	/**医生科室(用于过滤条件)**/
	@RequestMapping(value = "queryUsedDepartments")
	@ResponseBody
	public List<Map<String, Object>> queryUsedDepartments() {
		return diagnoseService.queryUsedDepartments();
	}
	
	
	/**筛选医生
	 * @page
	 * @row
	 * @cityId 城市Id (为空,不参与过滤)
	 * @deptId 科室Id (为空,不参与过滤)
	 * @postionId 职称Id (为空,不参与过滤)
	 * @keyword 关键字
	 * @sortType 排序方式 (为空,默认排序方式)
	 * **/
	@RequestMapping("/filterDoctors")
	@ResponseBody
	public List<Map<String, Object>> filterDoctors(HttpServletRequest request,	
			Integer page, Integer row,
			String cityId,String deptId,String postionId,String keyword, String sortType) {
		
		//筛选医生
		List<Map<String, Object>> doctList = diagnoseService.filterDoctor(page, row,cityId,deptId, postionId,keyword,sortType);
		
		return doctList;
	}
	
	
}
