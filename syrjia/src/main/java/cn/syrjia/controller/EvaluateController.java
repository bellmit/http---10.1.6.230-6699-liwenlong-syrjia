package cn.syrjia.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.syrjia.config.configCode;
import cn.syrjia.entity.EvaUtil;
import cn.syrjia.entity.Evaluate;
import cn.syrjia.entity.Piclib;
import cn.syrjia.service.EvaluateService;
import cn.syrjia.util.GetOpenId;
import cn.syrjia.util.JsonUtil;
import cn.syrjia.util.StringUtil;
import cn.syrjia.util.Util;

@Controller
@RequestMapping("/evaluate")
public class EvaluateController {

	@Resource(name = "evaluateService")
	EvaluateService evaluateService;

	/**
	 * 查询评价
	 * @param type
	 * @return
	 */
	@RequestMapping("/queryEvalabels")
	@ResponseBody
	public Map<String, Object> queryEvalabels(Integer type) {
		return evaluateService.queryEvalabels(type);
	}

	/**
	 * 查询评价列表
	 * @param request
	 * @param evaluate
	 * @param memberId
	 * @param page
	 * @param row
	 * @param level
	 * @return
	 */
	@RequestMapping("/queryEvaluateList")
	@ResponseBody
	public Map<String, Object> queryEvaluateList(HttpServletRequest request,Evaluate evaluate,String memberId,
			Integer page, Integer row,Integer level) {
		//非空判断
		if(StringUtil.isEmpty(memberId)){
			memberId = GetOpenId.getMemberId(request);
		}
		evaluate.setMemberId(memberId);
		//查询评价列表
		return evaluateService.queryEvaluateList(evaluate,level,page, row);
	}
	
	/**
	 * 查询好评率
	 * @param evaluate
	 * @param evaluateLevel
	 * @return
	 */
	@RequestMapping("/queryEvaluateRate")
	@ResponseBody
	public Map<String, Object> queryEvaluateRate(Evaluate evaluate,Integer evaluateLevel) {
		return evaluateService.queryEvaluateRate(evaluate,evaluateLevel);
	}
	
	/**
	 * 查询差评 中评 好评数
	 * @param evaluate
	 * @return
	 */
	@RequestMapping("/queryEvaluateNum")
	@ResponseBody
	public Map<String, Object> queryEvaluateNum(Evaluate evaluate) {
		return evaluateService.queryEvaluateNum(evaluate);
	}

	/**
	 * 新增评价
	 * 
	 * @param eva
	 * @return
	 */
	@RequestMapping("/addEvaluate")
	@ResponseBody
	public Map<String,Object> addEvaluate(HttpServletRequest request, String eval,String orderNo,String memberId) {
		//非空判断
		if(StringUtil.isEmpty(memberId)){
			memberId = GetOpenId.getMemberId(request);
		}
		List<Map<String,Object>> list=JsonUtil.parseJSON2List(eval);
		//执行添加
		return evaluateService.addEvaluate(list, orderNo,memberId);
	}
	
	/**
	 * 新增评价
	 * 
	 * @param eva
	 * @return
	 */
	@RequestMapping("/addEvaluates")
	@ResponseBody
	public Object addEvaluates(HttpServletRequest request, EvaUtil eva,
			String picServerIds) {
		return evaluateService.addEvaluate(request, eva.getEvalist(),
				picServerIds);
	}

	/**
	 * 新增评价
	 * 
	 * @param eva
	 * @return
	 */
	@RequestMapping("/addEva")
	@ResponseBody
	public Object addEvaluate(HttpServletRequest request, Evaluate eva) {
		return evaluateService.addEva(request, eva);
	}

	/**
	 * 新增评价
	 * 
	 * @param eva
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "static-access" })
	@RequestMapping("/addEvaluateList")
	@ResponseBody
	public Map<String, Object> addEvaluateList(HttpServletRequest request,
			String evaluates, String picServerIds) {
		//非空判断
		if (StringUtil.isEmpty(evaluates)) {
			return Util.resultMap(configCode.code_1015, null);
		}
		List<Evaluate> evaluate;
		try {
			//转json
			JSONArray objData = JSONArray.fromObject(evaluates);
			//转list集合
			evaluate = (List<Evaluate>) objData.toList(objData, Evaluate.class);
			//新增评论
			Object obj = evaluateService.addEvaluate(request, evaluate,
					picServerIds);
			if (null == obj) {
				return Util.resultMap(configCode.code_1015, null);
			}
		} catch (Exception e) {
			return Util.resultMap(configCode.code_1015, null);
		}

		return Util.resultMap(configCode.code_1001, evaluate);
	}

	/**
	 * 修改评论
	 * @param evaluate
	 * @param state
	 * @return
	 */
	@RequestMapping("/updateEvaluate")
	@ResponseBody
	public Object updateEvaluate(Evaluate evaluate, String state) {
		return evaluateService.updateEvaluate(evaluate, state);
	}

	/**
	 * 上传评价图片
	 * 
	 * @param multipartFile
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping("/uploadEvaPics")
	@ResponseBody
	public Map<String, Object> uploadEvaPics(HttpServletRequest request,
			@RequestParam(value = "multipartFiles") MultipartFile[] multipartFiles,
			 String id, String orderNo) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		//解析上传文件
		for (MultipartFile multipartFile : multipartFiles) {
			//上传
			Map<String, String> imageUrl = Util.uploadFoodImage(multipartFile,
					request, "eval");
			//创建对象
			Piclib piclib = new Piclib();
			//赋值
			piclib.setPicId(Util.getUUID());
			piclib.setPicType(2);
			piclib.setPicPathUrl(imageUrl.get("url"));
			piclib.setPicAddr(imageUrl.get("riskPath"));
			//执行新增
			evaluateService.addEntity(piclib);
			imageUrl.put("picId", piclib.getPicId());
			//返回值
			list.add(imageUrl);
		}
		return Util.resultMap(configCode.code_1001,list);
	}

	/**
	 * 上传评价图片
	 * 
	 * @param multipartFile
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping("/uploadEvaPic")
	@ResponseBody
	public String uploadEvaPic(
			@RequestParam(value = "imageFile") MultipartFile multipartFile,
			HttpServletRequest request) {
		//上传图片
		Map<String, String> map = Util.uploadFoodImage(multipartFile, request,
				multipartFile.getName());
		//创建对象
		Piclib pic = new Piclib();
		//赋值
		pic.setPicId(Util.getUUID());
		pic.setPicAddr(map.get("url").toString());
		pic.setPicPathUrl(map.get("riskPath").toString());
		pic.setStatus("10");
		pic.setStatusDate((int) (System.currentTimeMillis() / 1000));
		//执行新增
		evaluateService.addEntity(pic);
		return pic.getPicId();
	}

	/**
	 * 删除
	 * 
	 * @param pro
	 * @return
	 */
	@RequestMapping("/deleEvaPic")
	@ResponseBody
	public Object deleEvaPic(Piclib pic) {
		String picid = pic.getPicId();
		int picdel = 0;
		if (pic != null) {
			//删除
			picdel = evaluateService.deleteEntity(Piclib.class, picid);
			String bdUrl = pic.getPicAddr();
			File f = new File(bdUrl);
			f.delete();
		}
		return picdel;
	}
}
