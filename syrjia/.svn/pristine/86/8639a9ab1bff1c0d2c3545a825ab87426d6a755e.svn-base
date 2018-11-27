package cn.syrjia.controller;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.syrjia.service.StatisticsService;
import cn.syrjia.util.GetOpenId;
import cn.syrjia.util.JsonUtil;
import cn.syrjia.util.StringUtil;

@Scope("prototype")
@Controller 
@RequestMapping("/statistics")
public class StatisticsController {
	
	@Resource(name = "statisticsService")
	StatisticsService statisticsService;

	/**
	 * 添加
	 * @param request
	 * @param statistics
	 * @return
	 */
	@RequestMapping("addStatistics")
	@ResponseBody
	public Integer statistics(HttpServletRequest request, String statistics) {
		//json转list
		List<Map<String, Object>> list = JsonUtil.parseJSON2List(statistics);
		Object openId = request.getSession().getAttribute("openid");
		Object memberId = GetOpenId.getMemberId(request);
		//添加统计
		return statisticsService.addStatistics(list,StringUtil.isEmpty(openId)?null:openId.toString(),StringUtil.isEmpty(memberId)?null:memberId.toString());
	}
	
	/**
	 * 查询sessionId
	 * @param request
	 * @return
	 */
	@RequestMapping("querySessionid")
	@ResponseBody
	public String querySessionid(HttpServletRequest request) {
		return request.getSession().getId();
	}
}
