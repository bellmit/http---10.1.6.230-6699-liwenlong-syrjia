package cn.syrjia.hospital.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import cn.syrjia.common.impl.BaseDaoImpl;
import cn.syrjia.common.util.DaoUtil;
import cn.syrjia.hospital.dao.CouponDao;
import cn.syrjia.hospital.entity.WeiXinUserCoupon;
import cn.syrjia.util.Util;

@Repository("couponDao")
public class CouponDaoImpl extends BaseDaoImpl implements CouponDao {

	// 日志
	private Logger logger = LogManager.getLogger(CouponDaoImpl.class);

	/**
	 * 根据购物价格 添加购物券
	 */
	@Override
	public String queryCoupon(Object total) {
		String sql = "select id from h_coupon where state=0 and startMoney >= ? and IFNULL(endMoney,9999999) < ?  and if(startTime is NULL,TO_DAYS(NOW()),TO_DAYS(startTime)) <= TO_DAYS(NOW())  and if(endTime is NULL,TO_DAYS(NOW())+1,TO_DAYS(endTime)) > TO_DAYS(NOW()) ORDER BY startMoney desc LIMIT 0,1";
		String id = null;
		try {
			id = jdbcTemplate.queryForObject(sql,
					new Object[] { total, total }, String.class);
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return id;
	}

	/**
	 * 根据购物券ID添加购物券
	 */
	@Override
	public Integer addCoupon(String openId, String couponId) {
		String sql = "insert into h_weixinuser_coupon(id,openId,couponId) values(?,?,?)";
		Integer i = 0;
		try {
			i = jdbcTemplate.update(sql, new Object[] { Util.getUUID(), openId,
					couponId });
		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
		return i;
	}

	/**
	 * 查询可用优惠券
	 */
	@Override
	public List<WeiXinUserCoupon> queryUsableCoupon(String openId,
			String total, Integer page, Integer row, Integer type) {
		String sql = "select * from  h_weixinuser_coupon where openId=? and useMoney <= ? and isOverdue=0 and state=0 and type=?";
		Integer start = row * (page - 1);
		sql += " limit " + start + "," + row;
		final List<WeiXinUserCoupon> weiXinUserCoupons = new ArrayList<WeiXinUserCoupon>();
		try {
			jdbcTemplate.query(sql, new Object[] { openId, total, type },
					new RowCallbackHandler() {
						@Override
						public void processRow(ResultSet rs)
								throws SQLException {
							WeiXinUserCoupon weiXinUserCoupon = DaoUtil
									.setEntity(WeiXinUserCoupon.class, rs);
							weiXinUserCoupons.add(weiXinUserCoupon);
						}
					});
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return weiXinUserCoupons;
	}

	/**
	 * 使用优惠券
	 */
	@Override
	public Integer useCoupon(String openId, String couponId) {
		String sql = "update h_weixinuser_coupon set state=1 where openId=? and id=? and state=0 and isOverdue=0";
		Integer i = 0;
		try {
			i = jdbcTemplate.update(sql, new Object[] { openId, couponId });
		} catch (DataAccessException e) {
			throw e;
		}
		return i;
	}

	/**
	 * 查询不可用优惠券
	 */
	@Override
	public List<WeiXinUserCoupon> queryCoupon(String openId, Integer page,
			Integer row) {
		String sql = "select * from h_weixinuser_coupon where openId=? and (isOverdue=1 or state = 1)";
		Integer start = row * (page - 1);
		sql += " limit " + start + "," + row;
		final List<WeiXinUserCoupon> weiXinUserCoupons = new ArrayList<WeiXinUserCoupon>();
		try {
			jdbcTemplate.query(sql, new Object[] { openId },
					new RowCallbackHandler() {
						@Override
						public void processRow(ResultSet rs)
								throws SQLException {
							WeiXinUserCoupon weiXinUserCoupon = DaoUtil
									.setEntity(WeiXinUserCoupon.class, rs);
							weiXinUserCoupons.add(weiXinUserCoupon);
						}
					});
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return weiXinUserCoupons;
	}

	/**
	 * 优惠券是否可用
	 */
	@Override
	public String isUseCoupon(String total, String id,Integer type) {
		String sql="select couponPrice from  h_weixinuser_coupon where useMoney <= ? and isOverdue=0 and state=0 and type=? and id=?";
		String couponPrice="0";
		try {
			couponPrice = jdbcTemplate.queryForObject(sql,new Object[]{total,type,id},String.class);
		} catch (DataAccessException e) {
			couponPrice="0";
		}
		return null==couponPrice||"".equals(couponPrice.trim())?"0":couponPrice;
	}

}
