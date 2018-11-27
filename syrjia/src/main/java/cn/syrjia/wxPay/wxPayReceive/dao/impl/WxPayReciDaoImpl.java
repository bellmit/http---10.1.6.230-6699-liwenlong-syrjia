package cn.syrjia.wxPay.wxPayReceive.dao.impl;

import org.springframework.stereotype.Repository;

import cn.syrjia.common.impl.BaseDaoImpl;
import cn.syrjia.util.StringUtil;
import cn.syrjia.wxPay.wxPayReceive.dao.WxPayReciDao;

@Repository("wxPayReciDao")
public class WxPayReciDaoImpl extends BaseDaoImpl implements WxPayReciDao {

	@Override
	public Integer queryCount(String orderNo) {
		Integer count = 0;
		try {
			String sql = "SELECT count(1) from t_wxpay_reci where outTradeNo=? ";
			if(!StringUtil.isEmpty(orderNo)){
				count = super.queryBysqlCount(sql, new Object[]{orderNo});
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return count;
	}
	
}
