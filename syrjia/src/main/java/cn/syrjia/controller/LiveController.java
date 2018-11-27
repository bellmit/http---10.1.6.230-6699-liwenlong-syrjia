package cn.syrjia.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.syrjia.config.configCode;
import cn.syrjia.entity.Live;
import cn.syrjia.service.LiveService;
import cn.syrjia.util.Util;
import cn.syrjia.wxPay.wxPay.util.StringUtil;

@Controller 
@RequestMapping("/live")
public class LiveController {

	@Resource(name = "liveService")
	LiveService liveService;
	
	/**
	 * 根据id查询知识明细
	 * @param knowledgeId
	 * @return
	 */
	@RequestMapping("/queryLiveByInviteCode")
	@ResponseBody
	public Map<String,Object> queryLiveByInviteCode(Live live){
		//非空判断
		if(StringUtil.isEmpty(live.getInviteCode())){
			return Util.resultMap(configCode.code_1003,null);
		}
		//通过实体查询
		live=liveService.queryByEntity(live);
		if(null==live){
			return Util.resultMap(configCode.code_1015, live);
		}
		return Util.resultMap(configCode.code_1001, live);
	}
}
