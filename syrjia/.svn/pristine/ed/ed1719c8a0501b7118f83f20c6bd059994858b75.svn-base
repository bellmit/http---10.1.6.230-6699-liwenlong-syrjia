package cn.syrjia.common.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.syrjia.common.BaseServiceInterface;
import cn.syrjia.common.LogServiceInterface;
import cn.syrjia.config.Config;
import cn.syrjia.config.configCode;
import cn.syrjia.dao.OrderDao;
import cn.syrjia.entity.Log;
import cn.syrjia.entity.Member;
import cn.syrjia.hospital.dao.AppDoctorDao;
import cn.syrjia.service.MemberService;
import cn.syrjia.util.GetOpenId;
import cn.syrjia.util.Pager;
import cn.syrjia.util.RedisUtil;
import cn.syrjia.util.ResultUtil;
import cn.syrjia.util.StringUtil;
import cn.syrjia.util.UploadImages;
import cn.syrjia.util.Util;
import cn.syrjia.util.WxScan;
import cn.syrjia.util.sendModelMsgUtil;
import cn.syrjia.wxPay.wxPay.util.http.HttpRequest;

@Controller
@RequestMapping("/")
public class BaseController{
	@SuppressWarnings("unused")
	private HttpRequest requests;
	protected HttpServletRequest request; 
    protected HttpServletResponse response; 
    protected HttpSession session;
	protected Pager  pager = new Pager();
	
	@Resource(name = "logService")
	LogServiceInterface logService;
	@Resource(name = "config")
	
	Config config;
	
	@Resource(name = "memberService")
	MemberService memberService;
	
	@Resource(name = "baseService")
	BaseServiceInterface baseService;
	
	@Resource(name = "orderDao")
	OrderDao orderDao;
	
	@Resource(name = "appDoctorDao")
	AppDoctorDao appDoctorDao;
	
	@RequestMapping("/login")
	public String login() {
		return "login/login";
	}
	
	@RequestMapping("log/log")
	public String log() {
		return "log/log";
	}
	
	@RequestMapping("code/qrCode")
	public String qrCode() {
		return "qrCode/qrCode";
	}
	
	@RequestMapping("/index")
	public String index() {
		return "index/index";
	}
	
	@SuppressWarnings({ "unchecked" })
	@RequestMapping("/log/queryLog")
	@ResponseBody
	public Map<String, Object> queryUser(HttpServletRequest request, Log log) {
		// 获得map
		Map<String, Object> req = request.getParameterMap();
		// 转json
		JSONObject json = JSONObject.fromMap(req);

		// 页数
		Integer page = ResultUtil.getPage(json);

		// 行数
		Integer row = ResultUtil.getRow(json);

		String sEcho = json.getString("sEcho");

		Map<String, List<String>> setMap = new HashMap<String, List<String>>();

		// 需要排序的字段
		List<String> sortList = new ArrayList<String>();
		sortList.add("creatTime");
		setMap.put("sortList", sortList);
		// 需要like的字段
		List<String> stringLike = new ArrayList<String>();
		stringLike.add("userName");
		stringLike.add("phone");
		stringLike.add("loginName");
		setMap.put("stringLike", stringLike);
		Map<String, Object> map = logService.searchLog(log, page, row, setMap);
		map = ResultUtil.resultMap(sEcho, map);
		return map;
	}
	
	@RequestMapping("queryAreaList")
	@ResponseBody
	public Map<String,Object> queryAreaList() {
		List<Map<String,Object>> provinces=baseService.queryProvinces();
		List<Map<String,Object>> citys=baseService.queryCitys(null);
		List<Map<String,Object>> areas=baseService.queryAreas(null);
		
		List<Map<String,Object>> c=new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> a=new ArrayList<Map<String,Object>>();
		
		for(Map<String,Object> province:provinces){
			c=new ArrayList<Map<String,Object>>();
			for(Map<String,Object> city:citys){
				if(province.get("value").toString().equals(city.get("pid").toString())){
					a=new ArrayList<Map<String,Object>>();
					for(Map<String,Object> area:areas){
						if(area.get("pid").toString().equals(city.get("value").toString())){
							a.add(area);
						}
					}
					city.put("children",a);
					c.add(city);
				}
			}
			province.put("children",c);
		}
		return Util.resultMap(configCode.code_1001, provinces);
	}
	
	@RequestMapping("queryArea")
	@ResponseBody
	public Map<String,Object> queryArea() {
		List<Map<String,Object>> provinces=baseService.queryProvinces();
		List<Map<String,Object>> citys=baseService.queryCitys(null);
		List<Map<String,Object>> areas=baseService.queryAreas(null);
		
		List<Map<String,Object>> p=new ArrayList<Map<String,Object>>();
		
		for(Map<String,Object> province:provinces){
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("n",province.get("text"));
			List<Map<String,Object>> c=new ArrayList<Map<String,Object>>();
			for(Map<String,Object> city:citys){
				if(province.get("value").toString().equals(city.get("pid").toString())){
					Map<String,Object> mapC=new HashMap<String,Object>();
					mapC.put("n",city.get("text"));
					List<Object> a=new ArrayList<Object>();
					for(Map<String,Object> area:areas){
						if(area.get("pid").toString().equals(city.get("value").toString())){
							a.add(area.get("text"));
						}
					}
					mapC.put("a",a);
					c.add(mapC);
					map.put("c",c);
				}
			}
			p.add(map);
		}
		return Util.resultMap(configCode.code_1001, p);
	}
	
	@RequestMapping("queryAreas")
	@ResponseBody
	public Map<String,Object> queryAreas() {
		List<Map<String,Object>> provinces=baseService.queryProvinces();
		List<Map<String,Object>> citys=baseService.queryCitys(null);
		List<Map<String,Object>> areas=baseService.queryAreas(null);
		
		List<Map<String,Object>> p=new ArrayList<Map<String,Object>>();
		
		for(Map<String,Object> province:provinces){
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("name",province.get("text"));
			List<Map<String,Object>> c=new ArrayList<Map<String,Object>>();
			for(Map<String,Object> city:citys){
				if(province.get("value").toString().equals(city.get("pid").toString())){
					Map<String,Object> mapC=new HashMap<String,Object>();
					mapC.put("name",city.get("text"));
					List<String> a=new ArrayList<String>();
					for(Map<String,Object> area:areas){
						if(area.get("pid").toString().equals(city.get("value").toString())){
							a.add(area.get("text").toString());
						}
					}
					mapC.put("area",a);
					c.add(mapC);
					map.put("city",c);
				}
			}
			p.add(map);
		}
		return Util.resultMap(configCode.code_1001, p);
	}
	
	@RequestMapping("queryMember")
	@ResponseBody
	public Map<String,Object> queryMember(HttpServletRequest request) {
		Object memberId = GetOpenId.getMemberId(request);
		if(StringUtil.isEmpty(memberId)){
			return null;
		}
		Map<String,Object> member=baseService.queryMembersById(memberId.toString());
		if(null==member){
			return null;
		}
		return member;
	}

	@RequestMapping("/thumUpload")
	@ResponseBody
	public Map<String, Object> thumUpload(HttpServletRequest request,
			HttpServletResponse response, String id, String dirName,
			String _sign) throws Exception {
		System.out.println(request);
		String[] imgFiles = request.getParameterValues("imgFiles[]");
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<Map<String, Object>> urllist = new ArrayList<Map<String, Object>>();
		dirName = request.getParameter("dirName");
		if (StringUtil.isEmpty(dirName)) {
			dirName = "medical";
		}
		if (StringUtil.isEmpty(id)) {
			id = GetOpenId.getMemberId(request);
		}
		try {
			response.setContentType("text/html; charset=UTF-8");

			Integer fileLength = request.getParameter("fileLength") == null
					|| StringUtil.isEmpty(request.getParameter("fileLength")
							.toString()) ? 0 : Integer.valueOf(request
					.getParameter("fileLength"));
			Map<String,Object> map = new HashMap<String, Object>();
			for(int i=0;i<imgFiles.length;i++){
				fileLength = imgFiles[i].toString().length();
				System.out.println(imgFiles[i].toString().length());
				map = UploadImages.uploadThumbnailImg(request, response,
						dirName, config.getImgIp(), imgFiles[i].toString(), fileLength,
						id);
				urllist.add(map);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		returnMap.put("urllist", urllist);
		return returnMap;
	}
	
	/**
	 * 上传图片
	 * 
	 * @param multipartFile
	 */
	@RequestMapping(value = "uploadImages")
	@ResponseBody
	public Map<String, Object> uploadImages(
			HttpServletResponse response,
			HttpServletRequest request,
			@RequestParam(value = "multipartFile") MultipartFile[] multipartFile,
			String id, String dirName, String _sign) throws Exception {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<Map<String, Object>> urllist = new ArrayList<Map<String, Object>>();
		if (StringUtil.isEmpty(dirName)) {
			dirName = "images";
		}
		if (StringUtil.isEmpty(id)) {
			id = GetOpenId.getMemberId(request);
		}
		try {
			urllist = UploadImages.uploadImages(multipartFile, request, id,
					dirName, config.getImgIp());
		} catch (Exception e) {
			System.out.println(e);
		}
		returnMap.put("urllist", urllist);
		return returnMap;
	}

/**
 * 以下内容为王昭阳新加方法
 */
	@ModelAttribute  
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response){  
        this.request = request;  
        this.response = response;  
        this.session = request.getSession();  
    } 
	  public void getPage(Pager pager){
	    	String page=request.getParameter("page");
			if (StringUtils.isBlank(page)){
				pager.setPage(1);
			}else{
				pager.setPage(Integer.parseInt(page));
			}
			String row=request.getParameter("row");
			if(StringUtils.isBlank(row)){
				pager.setRow(10);
			}else{
				pager.setRow(Integer.parseInt(row));
			}
	    }
	    
	    public Pager getPager() {
			return pager;
		}

		public void setPager(Pager pager) {
			this.pager = pager;
		}
	
	/**
	 * 获取微信配置
	 * @param request
	 * @return
	 */
	@RequestMapping(value="queryWeiXinConfig")
	@ResponseBody
	public Map<String,Object> queryWeiXinConfig(HttpServletRequest request,String url){
		System.out.println(url);
		Map<String,Object> map = WxScan.getSign(url, memberService);
		return map;
	}
	
	@RequestMapping(value="querySysSet")
	@ResponseBody
	public Map<String,Object> querySysSet(){
		return baseService.querySysSet();
	}
	
	/**
	 * 发送模板消息，临时用
	 * @return 
	 */
	@RequestMapping(value="sendModelMsgTem")
	@ResponseBody
	public Map<String, Object> sendModelMsgTem(String orderNo,String doctorId){
		try {
			//根据ID查询订单详情
			Map<String, Object> recordOrder = appDoctorDao.queyrDetailByOrderNo(orderNo, doctorId);
			if(recordOrder!=null){
				String mainOrderNo = "";
				if(StringUtil.isEmpty(recordOrder.get("mainOrderNo"))){
					mainOrderNo = recordOrder.get("orderNo").toString();
				}else{
					mainOrderNo = recordOrder.get("mainOrderNo").toString();
				}
				//发送固定模板消息
				sendModelMsgUtil.sendAddRecipeOrder(recordOrder.get("patientId").toString(), doctorId,appDoctorDao, "hospital/look_scheme.html?orderNo="+mainOrderNo);
			}
		} catch (Exception e) {
			// TODO: handle exception
			return Util.resultMap(configCode.code_1015, null);
		}
		return Util.resultMap(configCode.code_1001, 1);
	}
	
	@ResponseBody
	@RequestMapping(value = "/registerRedisOpenId")
	public Integer registerRedisOpenId(){
		List<Map<String,Object>> list = memberService.queryBysqlList("SELECT id from t_member ORDER BY createtime DESC limit 72800,200000", null);
		for(int i=0;i<list.size();i++){
			String openid = list.get(i).get("id").toString();
			Object obj = RedisUtil.getVal(openid);
			Object memberId = null;
			if (!StringUtil.isEmpty(obj)) {//如果已有toeken则把旧token返回
				JSONObject json = JSONObject.fromObject(obj);
				memberId = json.has("memberId")?json.get("memberId"):null;
			}
			if(StringUtil.isEmpty(memberId)){
				try {
					JSONObject json = new JSONObject();
					json.set("memberId", openid);
					RedisUtil.setVal(openid, 60 * 60 * 24*365,json.toString());
				} catch (Exception e) {
					System.out.println(e);
				}
			}
			System.out.println("当前为第"+i+"条");
		}
		return 1;
	}
	
}
