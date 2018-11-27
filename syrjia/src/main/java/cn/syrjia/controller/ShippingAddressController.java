package cn.syrjia.controller;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.syrjia.config.configCode;
import cn.syrjia.entity.ShippingAddress;
import cn.syrjia.service.ShippingAddressService;
import cn.syrjia.util.GetOpenId;
import cn.syrjia.util.Util;
import cn.syrjia.wxPay.wxPay.util.StringUtil;

/** 收货地址控制器
 * 
 * @pdOid 4e0e6a25-0352-4677-95ec-55bb221ac6d8 */
@Controller 
@RequestMapping("/shippingAddress")
public class ShippingAddressController {
   /** @pdOid 421c9986-1caf-4c90-badb-792560e7d628 */
   @Resource(name = "shippingAddressService")
   private ShippingAddressService shippingAddressService;
   
   /** 查询地址信息
    * 
    * @param request 
    * @param shippingAddress
    * @pdOid 5c1841a7-498c-4133-a390-028588d9ebcf */
   @RequestMapping("/queryShippingAddressList")
   @ResponseBody
   public Map<String,Object> queryShippingAddressList(HttpServletRequest request, ShippingAddress shippingAddress,String memberId) {
	   if(StringUtil.isEmpty(memberId)){
			memberId = GetOpenId.getMemberId(request);
	   }
	   shippingAddress.setMemberId(memberId);
	   //查询地址信息
	   List<Map<String,Object>> list=shippingAddressService.queryShippingAddressList(shippingAddress);
	   if(null==list){
			return Util.resultMap(configCode.code_1015, list);
		}
		return Util.resultMap(configCode.code_1001, list);
   }
   
   /** 根据ID查询地址信息
    * 
    * @param request 
    * @param shippingAddress 地址实体
    * @pdOid 0002bdb0-8764-45b2-a73a-828a27f65cd3 */
   @RequestMapping("/queryShippingAddressById")
   @ResponseBody
   public Map<String,Object> queryShippingAddressById(HttpServletRequest request, String shippingAddressId) {
	   String memberId = GetOpenId.getMemberId(request);
	   //非空判断
	   if(StringUtil.isEmpty(memberId)||StringUtil.isEmpty(shippingAddressId)){
		   return Util.resultMap(configCode.code_1029, null);
	   }
	   //通过id查询地址
	   Map<String,Object> shippingAddress=shippingAddressService.queryAddressById(shippingAddressId);
	   if(null==shippingAddress||!shippingAddress.get("memberId").equals(memberId)){
			return Util.resultMap(configCode.code_1015, shippingAddress);
		}
		return Util.resultMap(configCode.code_1001, shippingAddress);
   }
   
   /** 查询默认地址信息
    * 
    * @param request 
    * @param shippingAddress 地址实体
    * @pdOid 0002bdb0-8764-45b2-a73a-828a27f65cd3 */
   @RequestMapping("/queryShippingAddressByDefault")
   @ResponseBody
   public Map<String,Object> queryShippingAddressByDefault(HttpServletRequest request,String memberId) {
	   if(StringUtil.isEmpty(memberId)){
			memberId = GetOpenId.getMemberId(request);
	   }
	   //查询默认地址
	   Map<String,Object> map=shippingAddressService.queryShippingAddressByDefault(memberId);
	   if(null==map){
			return Util.resultMap(configCode.code_1015, map);
		}
		return Util.resultMap(configCode.code_1001, map);
   }
   
   /** 添加地址信息
    * 
    * @param request 
    * @param shippingAddress 地址实体
    * @pdOid 0002bdb0-8764-45b2-a73a-828a27f65cd3 */
   @RequestMapping("/addShippingAddress")
   @ResponseBody
   public Map<String,Object> addShippingAddress(HttpServletRequest request, ShippingAddress shippingAddress,String memberId) {
	   if(StringUtil.isEmpty(memberId)){
			memberId = GetOpenId.getMemberId(request);
	   }
	   shippingAddress.setMemberId(memberId);
	   //添加地址信息
	   return shippingAddressService.addShippingAddress(shippingAddress);
   }
   
   /** 
   * @param request 
   * @param shippingAddress 地址实体
   * @pdOid 0002bdb0-8764-45b2-a73a-828a27f65cd3 */
   @RequestMapping("/queryShippingAddressNum")
   @ResponseBody
   public Map<String,Object> queryShippingAddressNum(HttpServletRequest request,String memberId) {
	   if(StringUtil.isEmpty(memberId)){
			memberId = GetOpenId.getMemberId(request);
	   }
	   
	   return shippingAddressService.queryShippingAddressNum(memberId);
   }
   
   /** 修改地址信息
    * 
    * @param request 
    * @param shippingAddress
    * @pdOid e5f6f9bb-6449-4724-90ef-8ba960f8359f */
   @RequestMapping("/updateShippingAddress")
   @ResponseBody
   public Map<String,Object> updateShippingAddress(HttpServletRequest request, ShippingAddress shippingAddress,String memberId) {
	   if(StringUtil.isEmpty(memberId)){
			memberId = GetOpenId.getMemberId(request);
	   }
	   shippingAddress.setMemberId(memberId);
	   //非空判断
	   if(StringUtil.isEmpty(shippingAddress.getId())||StringUtil.isEmpty(shippingAddress.getMemberId())){
		   return Util.resultMap(configCode.code_1029,null);
		}
	   //修改地址信息
	   Integer i=shippingAddressService.updateShippingAddress(shippingAddress);
	   if(null==i){
			return Util.resultMap(configCode.code_1015, i);
		}
		return Util.resultMap(configCode.code_1001, i);
   }
   
   /** 删除地址信息（假删）
    * 
    * @param request 
    * @param shippingAddressId 地址ID
    * @pdOid 5b727557-b791-4fc7-8c1b-de1f7e482640 */
   @RequestMapping("/deleteShippingAddress")
   @ResponseBody
   public Map<String,Object> deleteShippingAddress(HttpServletRequest request, String shippingAddressId) {
	   //删除地址信息（假删）
	   Integer i=shippingAddressService.deleteShippingAddress(shippingAddressId);
	   if(null==i){
			return Util.resultMap(configCode.code_1015, i);
		}
		return Util.resultMap(configCode.code_1001, i);
   }

}