package cn.syrjia.hospital.service;

import java.util.List;

import cn.syrjia.common.BaseServiceInterface;
import cn.syrjia.hospital.entity.WeiXinUserCoupon;

public interface CouponService extends BaseServiceInterface{

	
	/**
	 * 根据购物价格 添加购物券
	 * @param openId
	 * @param total
	 * @return
	 */
	public abstract Integer addCoupon(String openId,Object total);
	
	/**
	 * 根据购物券ID添加购物券 0失败 1成功 -1无购物券
	 * @param openId
	 * @param total
	 * @return
	 */
	public abstract Integer addCoupon(String openId,String couponId);
	
	
	/**
	 * 查询可用优惠券
	 * @param openId
	 * @param total
	 * @return
	 */
	public abstract List<WeiXinUserCoupon> queryUsableCoupon(String openId,String total,Integer page, Integer row,Integer type);
	
	
	/**
	 * 使用优惠券
	 * @param openId
	 * @param couponId
	 * @return
	 */
	public abstract Integer useCoupon(String openId,String couponId);
	
	
	/**
	 * 查询不可用优惠券
	 * @param openId
	 * @param total
	 * @return
	 */
	public abstract List<WeiXinUserCoupon> queryCoupon(String openId,Integer page, Integer row);
	
	/**
	 * 优惠券是否可用
	 * @param total
	 * @param id
	 * @return
	 */
	public abstract String isUseCoupon(String total,String id,Integer type);
}

