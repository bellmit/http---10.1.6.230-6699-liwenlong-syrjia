package cn.syrjia.hospital.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.syrjia.common.impl.BaseServiceImpl;
import cn.syrjia.hospital.dao.CouponDao;
import cn.syrjia.hospital.entity.WeiXinUserCoupon;
import cn.syrjia.hospital.service.CouponService;

@Service("couponService")
public class CouponServiceImpl extends BaseServiceImpl implements CouponService{

	@Resource(name="couponDao")
	CouponDao couponDao;

	@Override
	public Integer addCoupon(String openId, Object total) {
		String id=couponDao.queryCoupon(total);
		Integer i=0;
		if(null!=id&&!"".equals(id.trim())){
			i=couponDao.addCoupon(openId, id);
		}else{
			i=-1;
		}
		return i;
	}

	@Override
	public Integer addCoupon(String openId, String couponId) {
		return couponDao.addCoupon(openId, couponId);
	}

	@Override
	public List<WeiXinUserCoupon> queryUsableCoupon(String openId, String total,Integer page, Integer row,Integer type) {
		return couponDao.queryUsableCoupon(openId, total,page,row,type);
	}

	@Override
	public Integer useCoupon(String openId, String couponId) {
		return couponDao.useCoupon(openId, couponId);
	}

	@Override
	public List<WeiXinUserCoupon> queryCoupon(String openId,
			Integer page, Integer row) {
		return couponDao.queryCoupon(openId, page, row);
	}

	@Override
	public String isUseCoupon(String total, String id,Integer type) {
		return couponDao.isUseCoupon(total, id,type);
	}
	
}
