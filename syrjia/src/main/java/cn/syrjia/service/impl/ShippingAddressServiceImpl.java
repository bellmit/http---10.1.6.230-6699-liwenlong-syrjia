package cn.syrjia.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.syrjia.callCenter.util.CallCenterConfig;
import cn.syrjia.callCenter.util.SendCallCenterUtil;
import cn.syrjia.common.impl.BaseServiceImpl;
import cn.syrjia.config.configCode;
import cn.syrjia.dao.ShippingAddressDao;
import cn.syrjia.entity.ShippingAddress;
import cn.syrjia.service.ShippingAddressService;
import cn.syrjia.util.Util;

/**
 * 地址信息service实现
 * 
 * @pdOid 9413aca7-26a8-485b-90f9-1967ec5d2698
 */
@Service("shippingAddressService")
public class ShippingAddressServiceImpl extends BaseServiceImpl implements
		ShippingAddressService {
	/** @pdOid 215b5e86-bb8b-4c94-b03f-397fa2f0c9c2 */
	@Resource(name = "shippingAddressDao")
	public ShippingAddressDao shippingAddressDao;

	/**
	 * 查询地址信息
	 * 
	 * @param shippingAddress
	 * @pdOid 88ab5658-0b0c-4b05-8500-a751f3702807
	 */
	public List<Map<String, Object>> queryShippingAddressList(
			ShippingAddress shippingAddress) {
		return shippingAddressDao.queryShippingAddressList(shippingAddress);
	}

	/**
	 * 添加地址信息
	 * 
	 * @param shippingAddress
	 *            地址实体
	 * @pdOid 09ded8c2-ce44-469f-b0ce-ddecf0f1ab26
	 */
	public Map<String,Object> addShippingAddress(ShippingAddress shippingAddress) {
		//查询地址树
		Integer count=shippingAddressDao.queryShippingAddressNum(shippingAddress.getMemberId());
		if(count>99){
			return Util.resultMap(configCode.code_1115,null);
		}
		//非空判断
		if (null!=shippingAddress.getIsDefault()&&shippingAddress.getIsDefault()) {
			shippingAddressDao.updateShippingAddressIsDefault(shippingAddress.getMemberId());
		}
		shippingAddress
				.setCreateTime((int) (System.currentTimeMillis() / 1000));
		//实体添加
		Object obj=shippingAddressDao.addEntityUUID(shippingAddress);
		if(null==obj){
			return Util.resultMap(configCode.code_1015, obj);
		}
		//查询同步呼叫中心数据
		Map<String,Object> map = shippingAddressDao.querySendCallCenterData(obj.toString());
		if(map!=null){
			if(map.get("isDefault").equals(false)){
				map.put("isDefault",0);
			}else{
				map.put("isDefault",1);
			}
			String result = SendCallCenterUtil.sendCallCenterData(map,CallCenterConfig.CustomersReceiver);
			System.out.println(result);
		}
		return Util.resultMap(configCode.code_1001, obj);
	}
	
	/**
	 * 查询地址数
	 */
	@Override
	public Map<String, Object> queryShippingAddressNum(String memberId) {
		Integer count=shippingAddressDao.queryShippingAddressNum(memberId);
		return Util.resultMap(configCode.code_1001,count);
	}

	/**
	 * 修改地址信息
	 * 
	 * @param shippingAddress
	 * @pdOid bc6e7193-c003-4e27-a100-562e93b8b670
	 */
	public Integer updateShippingAddress(ShippingAddress shippingAddress) {
		if (null!=shippingAddress.getIsDefault()&&shippingAddress.getIsDefault()) {
			shippingAddressDao.updateShippingAddressIsDefault(shippingAddress.getMemberId());
		}
		/**
		 * 实体更新
		 */
		Integer i = shippingAddressDao.updateEntity(shippingAddress);
		if(i>0){
			//查询同步呼叫中心数据
			Map<String,Object> map = shippingAddressDao.querySendCallCenterData(shippingAddress.getId());
			if(map!=null){
				if(map.get("isDefault").equals(false)){
					map.put("isDefault",0);
				}else{
					map.put("isDefault",1);
				}
				String result = SendCallCenterUtil.sendCallCenterData(map,CallCenterConfig.CustomersReceiver);
				System.out.println(result);
			}
		}
		return i;
	}

	/**
	 * 删除地址信息（假删）
	 * 
	 * @param shippingAddressId
	 *            地址ID
	 * @pdOid 80d5744b-c822-4735-9b7e-b8c94d8dc5cb
	 */
	public Integer deleteShippingAddress(String shippingAddressId) {
		Integer i = shippingAddressDao.deleteShippingAddress(shippingAddressId);
		if(i>0){
			//查询同步呼叫中心数据
			Map<String,Object> map = shippingAddressDao.querySendCallCenterData(shippingAddressId);
			if(map!=null){
				if(map.get("isDefault").equals(false)){
					map.put("isDefault",0);
				}else{
					map.put("isDefault",1);
				}
				String result = SendCallCenterUtil.sendCallCenterData(map,CallCenterConfig.CustomersReceiver);
				System.out.println(result);
			}
		}
		return i;
	}

	/**
	 * 查询默认地址
	 */
	@Override
	public Map<String,Object> queryShippingAddressByDefault(String openId) {
		return shippingAddressDao.queryShippingAddressByDefault(openId);
	}

	/**
	 * 通过id查询地址
	 */
	@Override
	public Map<String, Object> queryAddressById(String id) {
		return shippingAddressDao.queryAddressById(id);
	}

}