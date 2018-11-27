package cn.syrjia.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.syrjia.config.configCode;
import cn.syrjia.entity.Evaluate;
import cn.syrjia.entity.Goods;
import cn.syrjia.entity.GoodsType;
import cn.syrjia.entity.Member;
import cn.syrjia.service.GoodsService;
import cn.syrjia.util.GetOpenId;
import cn.syrjia.util.Util;
import cn.syrjia.wxPay.wxPay.util.StringUtil;

@Controller 
@RequestMapping("/goods")
public class GoodsController {
	
	@Resource(name = "goodsService")
	GoodsService goodsService;

	/**
	 * 根据id查询商品
	 * @param goodsIds
	 * @return
	 */
	@RequestMapping("/queryGoodsById")
	@ResponseBody
	public Map<String,Object> queryGoodsById(HttpServletRequest request,String goodsIds,String memberId){
		//判断goodsIds是否为空
		if(StringUtil.isEmpty(goodsIds)){
			return Util.resultMap(configCode.code_1003,null);
		}
		//判断memberId是否为空
		if(StringUtil.isEmpty(memberId)){
			memberId = GetOpenId.getMemberId(request);
		}
		//根据id查询商品
		Map<String,Object> goods=goodsService.queryGoodsById(goodsIds,memberId);
		//判断查询
		if(null==goods){
			return Util.resultMap(configCode.code_1015, goods);
		}
		return Util.resultMap(configCode.code_1001, goods);
	}
	
	/**
	 * 根据id查询商品 立即购买时使用
	 * @param goodsIds
	 * @return
	 */
	@RequestMapping("/queryGoodsForList")
	@ResponseBody
	public Map<String,Object> queryGoodsForList(String goodsId){
		//判断goodId是否为空
		if(StringUtil.isEmpty(goodsId)){
			return Util.resultMap(configCode.code_1003,null);
		}
		//根基商品id查询规格参数
		List<Map<String,Object>> goods=goodsService.queryGoodsForList(goodsId);
		//判断返回结果是否为空
		if(null==goods){
			return Util.resultMap(configCode.code_1015, goods);
		}
		return Util.resultMap(configCode.code_1001, goods);
	}
	
	/**
	 * 商品列表
	 * @param goods
	 * @param page
	 * @param row
	 * @return
	 */
	@RequestMapping("/queryGoods")
	@ResponseBody
	public Map<String,Object> queryGoods(HttpServletRequest request,Goods goods,String memberId,String serviceId,String brandId,String lowPrice,String highPrice,String order,Integer page,Integer row){
		//判断memberId是否为空
		if(StringUtil.isEmpty(memberId)){
			memberId = GetOpenId.getMemberId(request);
		}
		//查询商品信息
		Map<String, Object> goodss=goodsService.queryGoods(goods,memberId,serviceId,brandId,lowPrice,highPrice,order,page,row);
		//判断返回结果是否为空
		if(null==goodss){
			return Util.resultMap(configCode.code_1015, goodss);
		}
		return Util.resultMap(configCode.code_1001, goodss);
	}
	
	/**
	 * 商品首页展示图
	 * @param goods
	 * @param page
	 * @param row
	 * @return
	 */
	@RequestMapping("/queryGoodsIndexPic")
	@ResponseBody
	public Map<String,Object> queryGoodsIndexPic(){
		return goodsService.queryGoodsIndexImg();
	}
	
	/**
	 * 查询库存
	 * @param goodsId
	 * @return
	 */
	@RequestMapping("/queryGoodsStock")
	@ResponseBody
	public Map<String,Object> queryGoodsStock(String goodsId){
		//查询商品库存
		Integer stock=goodsService.queryGoodsStock(goodsId);
		//判断查询结果是否为空
		if(null==stock){
			return Util.resultMap(configCode.code_1015, stock);
		}
		return Util.resultMap(configCode.code_1001, stock);
	}
	
	/**
	 * 查询商品类型
	 * @param goodsType
	 * @param page
	 * @param row
	 * @return
	 */
	@RequestMapping("/queryGoodsType")
	@ResponseBody
	public Map<String,Object> queryGoodsType(GoodsType goodsType,Integer page,Integer row){
		//查询商品类型
		List<Map<String,Object>> goodsTypes=goodsService.queryGoodsType(goodsType, page, row);
		//查询商城首页菜单
		List<Map<String,Object>> menu=goodsService.queryMallMenu(page, row);
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("menu",menu);
		map.put("goodsTypes",goodsTypes);
		//判断goodsTypes是否为空
		if(null==goodsTypes){
			return Util.resultMap(configCode.code_1015,null);
		}
		return Util.resultMap(configCode.code_1001, map);
	}
	
	/**
	 * 查询商品类型和商品
	 * @param goodsType
	 * @param page
	 * @param row
	 * @return
	 */
	@RequestMapping("/queryGoodsTypeAndGoods")
	@ResponseBody
	public Map<String,Object> queryGoodsTypeAndGoods(GoodsType goodsType,Integer page,Integer row){
		//查询商品类型和商品
		List<Map<String,Object>> goodsTypes=goodsService.queryGoodsTypeAndGoods(goodsType, page, row);
		//判断goodsTypes是否为空
		if(null==goodsTypes){
			return Util.resultMap(configCode.code_1015,null);
		}
		return Util.resultMap(configCode.code_1001,goodsTypes);
	}
	
	/**
	 * 查询商品评价
	 * @param evaluate
	 * @param page
	 * @param row
	 * @return
	 */
	@RequestMapping("/queryGoodsEvaluate")
	@ResponseBody
	public Map<String,Object> queryGoodsEvaluate(Evaluate evaluate,Integer page,Integer row){
		Map<String,Object> evaluates=goodsService.queryGoodsEvaluate(evaluate, page, row);
		if(null==evaluates){
			return Util.resultMap(configCode.code_1015, evaluates);
		}
		return Util.resultMap(configCode.code_1001, evaluates);
	}
	
	/**
	 * 商品图集
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping("/queryGoodsImg")
	@ResponseBody
	public Map<String, Object> queryGoodsImg(HttpServletRequest request,String goodsId) {
		return goodsService.queryGoodsImg(goodsId);
	}
	
	/**
	 * 查询当前登陆人手机号
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping("/queryMemberPhone")
	@ResponseBody
	public Map<String, Object> queryMemberPhone(HttpServletRequest request) {
		//获取memberId
		String memberId = GetOpenId.getMemberId(request);
		//通过memberId查询member实体
		Member member=goodsService.queryById(Member.class,memberId);
		return Util.resultMap(configCode.code_1001,null==member?"":member.getLoginname());
	}
	
	/**
	 * 查询分类下子分类信息
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping("/queryGoodsTypeByTypeId")
	@ResponseBody
	public Map<String, Object> queryGoodsTypeByTypeId(HttpServletRequest request,String typeId) {
		//根据类型id查询商品
		List<Map<String, Object>> goodsType=goodsService.queryGoodsTypeByTypeId(typeId);
		return Util.resultMap(configCode.code_1001,goodsType);
	}
	
	/**
	 * 查询分类下子分类信息
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping("/queryPostage")
	@ResponseBody
	public Map<String, Object> queryPostage(HttpServletRequest request,String city,String goodsId) {
		String postagePrice=goodsService.queryPostage(city,goodsId);
		return Util.resultMap(configCode.code_1001,postagePrice);
	}
	
	/**
	 * 查询服务信息
	 * 
	 * @return
	 */
	@RequestMapping("/queryService")
	@ResponseBody
	public Map<String, Object> queryService(HttpServletRequest request) {
		//查询服务类型
		List<Map<String, Object>> services=goodsService.queryService();
		//查询品牌
		List<Map<String, Object>> brands=goodsService.queryBrand();
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("services", services);
		map.put("brands", brands);
		return Util.resultMap(configCode.code_1001,map);
	}
	
	/**
	 * 查询品牌信息
	 * 
	 * @return
	 */
	@RequestMapping("/queryBrand")
	@ResponseBody
	public Map<String, Object> queryBrand(HttpServletRequest request) {
		List<Map<String, Object>> services=goodsService.queryBrand();
		return Util.resultMap(configCode.code_1001,services);
	}
	
	/**
	 * 根据名称获取商品信息
	 * 
	 * @return
	 */
	@RequestMapping("/queryGoodsByName")
	@ResponseBody
	public Map<String, Object> queryGoodsByName(HttpServletRequest request,String name) {
		//判断name是否为空
		if(StringUtil.isEmpty(name)){
			return Util.resultMap(configCode.code_1001,null);
		}
		//根据名称查询商品
		List<Map<String, Object>> services=goodsService.queryGoodsByName(name);
		return Util.resultMap(configCode.code_1001,services);
	}
	
	/**
	 * 获取常用搜索信息
	 * 
	 * @return
	 */
	@RequestMapping("/queryCommonSearch")
	@ResponseBody
	public Map<String, Object> queryCommonSearch(HttpServletRequest request) {
		List<Map<String, Object>> services=goodsService.queryCommonSearch();
		return Util.resultMap(configCode.code_1001,services);
	}
	
	/**
	 * 获取历史搜索信息
	 * 
	 * @return
	 */
	@RequestMapping("/queryHistorySearch")
	@ResponseBody
	public Map<String, Object> queryHistorySearch(HttpServletRequest request,String memberId,Integer type) {
		//判断memberId是否为空
		if(StringUtil.isEmpty(memberId)){
			memberId = GetOpenId.getMemberId(request);
		}
		//查询历史搜索
		List<Map<String, Object>> services=goodsService.queryHistorySearch(memberId,type);
		return Util.resultMap(configCode.code_1001,services);
	}
	
	/**
	 * 添加历史搜索信息
	 * 
	 * @return
	 */
	@RequestMapping("/addHistorySearch")
	@ResponseBody
	public Map<String, Object> addHistorySearch(HttpServletRequest request,String memberId,String name,Integer type) {
		//判断memberId是否为空
		if(StringUtil.isEmpty(memberId)){
			memberId = GetOpenId.getMemberId(request);
		}
		//添加历史搜索
		Integer i=goodsService.addHistorySearch(memberId,name,type);
		return Util.resultMap(configCode.code_1001,i);
	}
	
	/**
	 * 删除历史搜索信息
	 * 
	 * @return
	 */
	@RequestMapping("/deleteHistorySearch")
	@ResponseBody
	public Map<String, Object> deleteHistorySearch(HttpServletRequest request,String memberId,Integer type) {
		//判断memberId是否为空
		if(StringUtil.isEmpty(memberId)){
			memberId = GetOpenId.getMemberId(request);
		}
		//情况历史搜索
		Integer i=goodsService.deleteHistorySearch(memberId,type);
		return Util.resultMap(configCode.code_1001,i);
	}
	
	/**
	 * 获取常用搜索信息
	 * 
	 * @return
	 */
	@RequestMapping("/queryMallActivity")
	@ResponseBody
	public Map<String, Object> queryMallActivity(HttpServletRequest request) {
		List<Map<String, Object>> services=goodsService.queryMallActivity();
		return Util.resultMap(configCode.code_1001,services);
	}
	
	/**
	 * 获取常用搜索信息
	 * 
	 * @return
	 */
	@RequestMapping("/queryMallActivityDetail")
	@ResponseBody
	public Map<String, Object> queryMallActivityDetail(HttpServletRequest request,String id) {
		return goodsService.queryMallActivityDetail(id);
	}
}
