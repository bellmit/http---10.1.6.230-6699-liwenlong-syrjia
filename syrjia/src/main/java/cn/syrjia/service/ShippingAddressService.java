package cn.syrjia.service;

import java.util.List;
import java.util.Map;

import cn.syrjia.common.BaseServiceInterface;
import cn.syrjia.entity.ShippingAddress;


/** 地址service接口
 * 
 * @pdOid 4d0cd010-cbf5-48fa-98de-0f81c68cfb8b */
public interface ShippingAddressService extends BaseServiceInterface {
   /** 查询地址信息
    * 
    * @param shippingAddress
    * @pdOid cea6acb6-3c53-4f88-b923-9814bdf72c14 */
   List<Map<String,Object>> queryShippingAddressList(ShippingAddress shippingAddress);
   /** 添加地址信息
    * 
    * @param shippingAddress 地址实体
    * @pdOid 0190c684-3cdd-44fe-9a04-50f0ee1d3f73 */
   Map<String,Object> addShippingAddress(ShippingAddress shippingAddress);
   
   Map<String,Object> queryShippingAddressNum(String memberId);
   /** 修改地址信息
    * 
    * @param shippingAddress
    * @pdOid b30a014e-e2c6-49b8-aa34-be4f6573506f */
   Integer updateShippingAddress(ShippingAddress shippingAddress);
   /** 删除地址信息（假删）
    * 
    * @param shippingAddressId 地址ID
    * @pdOid dccd7760-8dbd-4faf-99e2-3e603b1de947 */
   Integer deleteShippingAddress(String shippingAddressId);
   
   /**
    * 查询默认地址
    * @param openId
    * @return
    */
   Map<String,Object> queryShippingAddressByDefault(String openId);
   
   /**
    * 通过id查询地址
    * @param id
    * @return
    */
   public abstract Map<String, Object> queryAddressById(String id);

}