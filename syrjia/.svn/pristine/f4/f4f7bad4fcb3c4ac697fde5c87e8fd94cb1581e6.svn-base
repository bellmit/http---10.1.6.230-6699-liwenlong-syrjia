package cn.syrjia.dao;

import java.util.List;
import java.util.Map;

import cn.syrjia.common.BaseDaoInterface;
import cn.syrjia.entity.ShippingAddress;

/**
 * 地址信息dao接口
 * 
 * @pdOid b602396f-9ec1-4be2-aea3-8a116e9833af
 */
public interface ShippingAddressDao extends BaseDaoInterface {

	/**
	 * 查询地址信息
	 * 
	 * @param shippingAddress
	 * @pdOid cea6acb6-3c53-4f88-b923-9814bdf72c14
	 */
	List<Map<String, Object>> queryShippingAddressList(
			ShippingAddress shippingAddress);

	/**
	 * 删除地址信息（假删）
	 * 
	 * @param shippingAddressId
	 *            地址ID
	 * @pdOid dccd7760-8dbd-4faf-99e2-3e603b1de947
	 */
	Integer deleteShippingAddress(String shippingAddressId);

	/**
	 * 更新默认地址
	 * @param openId
	 * @return
	 */
	Integer updateShippingAddressIsDefault(String openId);

	/**
	 * 查询地址数
	 * @param memberId
	 * @return
	 */
	Integer queryShippingAddressNum(String memberId);

	/**
	 * 通过openid查询地址
	 * @param openId
	 * @return
	 */
	Map<String, Object> queryShippingAddressByDefault(String openId);

	/**
	 * 查询同步呼叫中心数据
	 * 
	 * @param id
	 * @return
	 */
	public abstract Map<String, Object> querySendCallCenterData(String id);

	/**
	 * 通过id查询地址
	 * @param id
	 * @return
	 */
	public abstract Map<String, Object> queryAddressById(String id);
}