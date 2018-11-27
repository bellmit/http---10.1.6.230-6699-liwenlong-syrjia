package cn.syrjia.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import cn.syrjia.common.impl.BaseDaoImpl;
import cn.syrjia.dao.ShippingAddressDao;
import cn.syrjia.entity.ShippingAddress;
import cn.syrjia.util.StringUtil;
/** 地址信息dao实现
 * 
 * @pdOid 672fc366-869e-4c91-b4b8-72b9f0e8e2b3 */
@Repository("shippingAddressDao")
public class ShippingAddressDaoImpl extends BaseDaoImpl implements ShippingAddressDao {

	// 日志
	private Logger logger = LogManager.getLogger(ShippingAddressDaoImpl.class);
	
	/**
	 * 查询地址信息
	 */
	@Override
	public List<Map<String, Object>> queryShippingAddressList(
			ShippingAddress shippingAddress) {
		String sql="select * from t_shipping_address where state=1 and memberId=? order by isDefault desc";
		List<Map<String, Object>> addresss=null;
		try {
			//执行查询
			addresss = jdbcTemplate.queryForList(sql,new Object[]{shippingAddress.getMemberId()});
		} catch (DataAccessException e) {
			logger.warn(e);
			throw e;
		}
		return addresss;
	}

	/**
	 * 删除地址信息（假删）
	 */
	@Override
	public Integer deleteShippingAddress(String shippingAddressId) {
		String sql="update t_shipping_address set state=3 where id=?";
		Integer i=0;
		try {
			//执行更新
			i = jdbcTemplate.update(sql,new Object[]{shippingAddressId});
		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
		return i;
	}

	/**
	 * 更新默认地址
	 */
	@Override
	public Integer updateShippingAddressIsDefault(String memberId) {
		String sql="update t_shipping_address set isDefault=0 where memberId=?";
		Integer i=0;
		try {
			//执行更新
			i = jdbcTemplate.update(sql,new Object[]{memberId});
		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
		return i;
	}
	
	/**
	 * 查询地址数
	 */
	@Override
	public Integer queryShippingAddressNum(String memberId) {
		String sql="select count(1) from t_shipping_address where memberId=? and state <> 3";
		Integer i=0;
		try {
			//执行查询
			i = jdbcTemplate.queryForObject(sql,new Object[]{memberId},Integer.class);
		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
		return i;
	}

	/**
	 * 通过openid查询地址
	 */
	@Override
	public Map<String,Object> queryShippingAddressByDefault(String memberId) {
		String sql="select * from t_shipping_address where state=1 and isDefault=1 and memberId=?";
		Map<String,Object> map=new HashMap<String, Object>();
		try {
			//执行查询
			map=jdbcTemplate.queryForMap(sql,new Object[]{memberId});
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return map;
	}
	
	/**
	 * 查询同步呼叫中心数据
	 */
	@Override
	public Map<String, Object> querySendCallCenterData(String id) {
		Map<String, Object> map = null;
		try {
			if(!StringUtils.isEmpty(id)){
				String sql = "SELECT p.id receiverId,p.memberId,p.consignee,p.phone,p.province,p.city,p.area,p.detailedAddress,p.isDefault,p.state,p.createTime from t_shipping_address p where p.id='"+id+"'";
				map = super.queryBysqlMap(sql, null);
			}
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		return map;
	}
	
	/**
	 * 通过id查询地址
	 */
	@Override
	public Map<String, Object> queryAddressById(String id) {
		Map<String,Object> map = null;
		try {
			String sql = "SELECT * from t_shipping_address where id=? ";
			if(!StringUtil.isEmpty(id)){
				map = jdbcTemplate.queryForMap(sql,new Object[]{id});
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return map;
	}
}