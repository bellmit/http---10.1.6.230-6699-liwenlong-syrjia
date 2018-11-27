package cn.syrjia.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.syrjia.config.configCode;
import cn.syrjia.entity.ShopCart;
import cn.syrjia.service.GoodsShopCartService;
import cn.syrjia.util.GetOpenId;
import cn.syrjia.util.Util;
import cn.syrjia.wxPay.wxPay.util.StringUtil;

@Controller
@RequestMapping("/goodsShopCart")
public class GoodsShopCartController {

	@Resource(name = "goodsShopCartService")
	GoodsShopCartService goodsShopCartService;

	/**
	 * 添加购物车
	 * 
	 * @param goodsIds
	 * @return
	 */
	@RequestMapping("/addGoodsShopCart")
	@ResponseBody
	public Map<String, Object> addGoodsShopCart(HttpServletRequest request,
			ShopCart shopCart, String memberId) {
		//判断memberId是否为空
		if (StringUtil.isEmpty(memberId)) {
			memberId = GetOpenId.getMemberId(request);
		}
		//获取不到返回错误信息
		if (StringUtil.isEmpty(memberId)) {
			return Util.resultMap(configCode.code_1074, null);
		}
		//添加memberId
		shopCart.setMemberId(memberId.toString());
		//添加购物车
		return goodsShopCartService.addGoodsShopCart(shopCart);
	}

	/**
	 * 添加购物车 立即支付时使用
	 * 
	 * @param goodsIds
	 * @return
	 */
	@RequestMapping("/addGoodsShopCartForOrder")
	@ResponseBody
	public Map<String, Object> addGoodsShopCartForOrder(
			HttpServletRequest request, ShopCart shopCart, String memberId) {
		//判断memberId是否为空
		if (StringUtil.isEmpty(memberId)) {
			memberId = GetOpenId.getMemberId(request);
		}
		//memberId添加实体
		shopCart.setMemberId(memberId);
		//添加购物车 立即支付时使用
		return goodsShopCartService.addGoodsShopCartForOrder(shopCart);
	}

	/**
	 * 删除购物车
	 * 
	 * @param goodsIds
	 * @return
	 */
	@RequestMapping("/deleteGoodsShopCart")
	@ResponseBody
	public Map<String, Object> deleteGoodsShopCart(HttpServletRequest request,
			String shopCartIds, String memberId) {
		//判断memberId是否为空
		if (StringUtil.isEmpty(memberId)) {
			memberId = GetOpenId.getMemberId(request);
		}
		//删除购物车
		return goodsShopCartService.deleteGoodsShopCart(shopCartIds.split(","));
	}

	/**
	 * 更新购物车商品数量
	 * 
	 * @param goodsIds
	 * @return
	 */
	@RequestMapping("/updateGoodsShopCartBuyCount")
	@ResponseBody
	public Map<String, Object> updateGoodsShopCartBuyCount(
			HttpServletRequest request, ShopCart shopCart, String memberId) {
		//判断memberId是否为空
		if (StringUtil.isEmpty(memberId)) {
			memberId = GetOpenId.getMemberId(request);
		}
		//实体添加memberId
		shopCart.setMemberId(memberId);
		//更新购物车商品数量
		return goodsShopCartService.updateGoodsShopCartBuyCount(shopCart);
	}

	/**
	 * 查询购物车商品
	 * 
	 * @param goodsIds
	 * @return
	 */
	@RequestMapping("/queryShopCart")
	@ResponseBody
	public Map<String, Object> queryShopCart(HttpServletRequest request,
			ShopCart shopCart, String memberId) {
		//判断memberId是否为空
		if (StringUtil.isEmpty(memberId)) {
			memberId = GetOpenId.getMemberId(request);
		}
		//实体添加memberId
		shopCart.setMemberId(memberId);
		//查询购物车商品
		return goodsShopCartService.queryShopCart(shopCart);
	}

	/**
	 * 查询购物车商品
	 * 
	 * @param goodsIds
	 * @return
	 */
	@RequestMapping("/queryShopCartById")
	@ResponseBody
	public Map<String, Object> queryShopCartById(HttpServletRequest request,
			ShopCart shopCart, String memberId) {
		//判断memberId是否为空
		if (StringUtil.isEmpty(memberId)) {
			memberId = GetOpenId.getMemberId(request);
		}
		//实体添加memberId
		shopCart.setMemberId(memberId);
		//查询购物车商品
		return goodsShopCartService.queryShopCartById(shopCart);
	}

	/**
	 * 查询购物车商品
	 * 
	 * @param goodsIds
	 * @return
	 */
	@RequestMapping("/queryGoodsById")
	@ResponseBody
	public Map<String, Object> queryGoodsById(HttpServletRequest request,
			String goodsId, String priceNumId, String buyCount) {
		return goodsShopCartService.queryGoodsById(goodsId, priceNumId,
				buyCount);
	}

	/**
	 * 查询购物车商品数量
	 * 
	 * @param goodsIds
	 * @return
	 */
	@RequestMapping("/queryShopCartNum")
	@ResponseBody
	public Map<String, Object> queryShopCartNum(HttpServletRequest request,
			ShopCart shopCart, String memberId) {
		//判断memberId是否为空
		if (StringUtil.isEmpty(memberId)) {
			memberId = GetOpenId.getMemberId(request);
		}
		if (StringUtil.isEmpty(memberId)) {
			return Util.resultMap(configCode.code_1001, 0);
		}
		//实体添加memberId
		shopCart.setMemberId(memberId.toString());
		//查询购物车商品数量
		return goodsShopCartService.queryShopCartNum(shopCart);
	}

	/**
	 * 查询购物车商品数量
	 * 
	 * @param goodsIds
	 * @return
	 */
	@RequestMapping("/queryPostageByCartIds")
	@ResponseBody
	public Map<String, Object> queryPostageByCartIds(
			HttpServletRequest request, String city, String ids) {
		return goodsShopCartService.queryPostageByCartIds(city, ids.split(","));
	}

	/**
	 * 查询购物车商品数量
	 * 
	 * @param goodsIds
	 * @return
	 */
	@RequestMapping("/queryPostageByCityName")
	@ResponseBody
	public Map<String, Object> queryPostageByCityName(
			HttpServletRequest request, String city, String goodsId,
			Integer buyCount) {
		return goodsShopCartService.queryPostageByCityName(city, goodsId,
				buyCount);
	}

	/**
	 * 查询购物车商品数量
	 * 
	 * @param goodsIds
	 * @return
	 */
	@RequestMapping("/queryPostageByMainOrderNo")
	@ResponseBody
	public Map<String, Object> queryPostageByMainOrderNo(
			HttpServletRequest request, String city, String orderNo) {
		return goodsShopCartService.queryPostageByMainOrderNo(city, orderNo);
	}
}
