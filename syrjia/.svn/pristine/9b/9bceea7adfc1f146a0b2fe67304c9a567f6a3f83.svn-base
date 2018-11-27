package cn.syrjia.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

import cn.syrjia.common.impl.BaseServiceImpl;
import cn.syrjia.config.configCode;
import cn.syrjia.dao.ScoreGoodsDao;
import cn.syrjia.entity.Member;
import cn.syrjia.entity.Order;
import cn.syrjia.entity.ScoreConsume;
import cn.syrjia.entity.ScoreGoods;
import cn.syrjia.entity.ShippingAddress;
import cn.syrjia.entity.vo.GoodsOrderDetail;
import cn.syrjia.service.ScoreGoodsService;
import cn.syrjia.util.DateTime;
import cn.syrjia.util.QRCodeUtil;
import cn.syrjia.util.Util;
import cn.syrjia.wxPay.wxPay.util.StringUtil;

@Service("scoreGoodsService")
public class ScoreGoodsServiceImpl extends BaseServiceImpl implements
		ScoreGoodsService {

	@Resource(name = "scoreGoodsDao")
	ScoreGoodsDao scoreGoodsDao;

	/**
	 * 查询所有的记录商品
	 */
	@Override
	public Map<String, Object> queryAllScoreGoods(ScoreGoods scoreGoods,
			Integer page, Integer row) {
		//查询
		List<Map<String, Object>> list = scoreGoodsDao.queryAllScoreGoods(
				scoreGoods, page, row);
		if (null == list) {
			return Util.resultMap(configCode.code_1015, list);
		}
		return Util.resultMap(configCode.code_1001, list);
	}

	/**
	 * 查询商品记录
	 */
	@Override
	public Map<String, Object> queryScoreGoodsById(ScoreGoods scoreGoods) {
		Map<String, Object> map = scoreGoodsDao.queryScoreGoodsById(scoreGoods
				.getId());
		if (null == map) {
			return Util.resultMap(configCode.code_1015, map);
		}
		return Util.resultMap(configCode.code_1001, map);
	}
	
	/**
	 * 通过用户id查询
	 */
	@Override
	public Map<String, Object> queryScoreByUserId(ScoreConsume scoreConsume) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		String score = scoreGoodsDao.queryScoreByUserId(scoreConsume
				.getUserid());
		if (null == score) {
			return Util.resultMap(configCode.code_1015, score);
		}
		returnMap.put("integral", score);
		//根据id查询单个
		Member userMember = scoreGoodsDao.queryById(Member.class,
				scoreConsume.getUserid());
		if (StringUtils.isEmpty(userMember)) {
			return Util.resultMap(configCode.code_1002, null);
		}
		returnMap.put("user", userMember);
		return Util.resultMap(configCode.code_1001, returnMap);
	}
	
	/**
	 * 查询记录
	 */
	@Override
	public Map<String, Object> queryScoreRecord(ScoreConsume scoreConsume,
			Integer page, Integer row) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		//查询
		List<Map<String, Object>> scores = scoreGoodsDao.queryScoreRecord(
				scoreConsume, page, row);
		if (null == scores) {
			return Util.resultMap(configCode.code_1015, scores);
		}
		returnMap.put("intgralList", scores);
		//通过id查询
		String score = scoreGoodsDao.queryScoreByUserId(scoreConsume
				.getUserid());
		if (null == score) {
			return Util.resultMap(configCode.code_1015, score);
		}
		returnMap.put("intgral", score);
		return Util.resultMap(configCode.code_1001, returnMap);
	}

	/**
	 * 查询列表
	 */
	@Override
	public Map<String, Object> queryScoreOrderList(String memberId,
			Integer page, Integer row) {
		List<Map<String, Object>> scoreOrderList = scoreGoodsDao
				.queryScoreOrderList(memberId, page, row);
		if (StringUtils.isEmpty(scoreOrderList)) {
			return Util.resultMap(configCode.code_1015, null);
		}
		return Util.resultMap(configCode.code_1001, scoreOrderList);
	}

	/**
	 * 添加
	 */
	@Override
	public Map<String, Object> addScoreOrder(HttpServletRequest request,Order order, String scoreGoodsId,String shippingAddressId) {
		
		String orderNo = scoreGoodsDao.orderNo();
		// 获取积分数，根据userid
		String integralPrice = scoreGoodsDao.queryScoreByUserId(order
				.getMemberId());
		// 获取兑换礼物，所需积分数 SELECT FORMAT(price-newPrice,2) FROM t_gift WHERE id=
		Map<String, Object> map = scoreGoodsDao
				.queryScoreGoodsById(scoreGoodsId);
		if (null == map) {
			return Util.resultMap(configCode.code_1015, map);
		}
		double price = Double.parseDouble(StringUtil.isEmpty(map
				.get("activityId")) ? map.get("price").toString() : map.get(
				"activityPrice").toString());
		// 比较积分是否足够支付
		if (Double.parseDouble(integralPrice) >= price) {
			Object addIntegral=null;
			// 生成订单
			order.setOrderNo(orderNo);
			order.setOrderPrice(Double.parseDouble(map.get("price").toString()));
			order.setReceiptsPrice(price);
			order.setCreateTime(Util.queryNowTime());
			order.setState(1);
			order.setOrderType(2);
			order.setOrderStatus(1);// 1-未发货 2-已发货 5-交易成功
			order.setPaymentStatus(2);// 1-等待付款 2-已付款 3取消订单5-交易成功
			
			if(map.get("isShipping").toString().equals("1")){
				
				if(StringUtil.isEmpty(shippingAddressId)){
					return Util.resultMap(configCode.code_1069,null);
				}
				//创建实体
				ShippingAddress shippingAddress=new ShippingAddress();
				//赋值
				shippingAddress.setId(shippingAddressId);
				shippingAddress.setMemberId(order.getMemberId());
				//根据实体查询单个
				shippingAddress=scoreGoodsDao.queryByEntity(shippingAddress);
				
				if(null==shippingAddress||StringUtil.isEmpty(shippingAddress.getId())){
					return Util.resultMap(configCode.code_1069,null);
				}
				//赋值
				order.setConsignee(shippingAddress.getConsignee());
				order.setArea(shippingAddress.getArea());
				order.setCity(shippingAddress.getCity());
				order.setProvince(shippingAddress.getProvince());
				order.setPhone(shippingAddress.getPhone());
				order.setDetailedAddress(shippingAddress.getDetailedAddress());
			}
			
			//订单详情
			GoodsOrderDetail goodsOrderDetail=new GoodsOrderDetail();
			//赋值
			goodsOrderDetail.setOrderNo(orderNo);
			goodsOrderDetail.setCreateTime(Util.queryNowTime());
			goodsOrderDetail.setGoodsId(scoreGoodsId);
			goodsOrderDetail.setGoodsNum(1);
			goodsOrderDetail.setGoodsOriginalPrice(Double.parseDouble(map.get("price").toString()));
			goodsOrderDetail.setGoodsPrice(price);
			goodsOrderDetail.setGoodsTotal(price);
			goodsOrderDetail.setId(scoreGoodsDao.orderNo());
			goodsOrderDetail.setPaymentStatus(2);
			
			if(map.get("identificationId").toString().equals("1")){
				Map<String, String> resultMap;
				try {
					//生成一维码
					resultMap = QRCodeUtil.enbarcode(request,goodsOrderDetail.getId(), "scoreOrder");
					goodsOrderDetail.setType(1);
				} catch (Exception e) {
					TransactionAspectSupport.currentTransactionStatus()
					.setRollbackOnly();
					return Util.resultMap(configCode.code_1015, null);
				}
			}else{
				goodsOrderDetail.setType(2);
			}
			
			
			// 积分，减去相应积分数量
			ScoreConsume scoreConsume = new ScoreConsume();
			scoreConsume.setConsumeScore(Double.parseDouble("-" + price));
			scoreConsume.setCreatetime(Util.queryNowTime());
			scoreConsume.setOrderNo(orderNo);
			scoreConsume.setUserid(order.getMemberId());
			scoreConsume.setSurplusScore(Double.parseDouble(integralPrice)-price);
			addIntegral = scoreGoodsDao.addEntity(scoreConsume);
			if (!StringUtils.isEmpty(addIntegral)) {
				// 更新状态，paymentStatus 2
				addIntegral = scoreGoodsDao.addEntity(order);
				if (null==addIntegral) {
					TransactionAspectSupport.currentTransactionStatus()
					.setRollbackOnly();
					return Util.resultMap(configCode.code_1015, null);
				}else{
					//新增
					addIntegral = scoreGoodsDao.addEntity(goodsOrderDetail);
					if (null==addIntegral) {
						TransactionAspectSupport.currentTransactionStatus()
						.setRollbackOnly();
						return Util.resultMap(configCode.code_1015, null);
					}
				}
			}else{
				return Util.resultMap(configCode.code_1015,null);
			}
		} else {
			return Util.resultMap(configCode.code_1070, null);
		}
		return Util.resultMap(configCode.code_1001, orderNo);
	}

	/**
	 * 通过订单查询
	 */
	@Override
	public Map<String, Object> queryScoreOrderByOrderNo(String orderNo,String memberId,
			String scoreGoodsId) {
		//通过订单查询
		Map<String, Object> map=scoreGoodsDao.queryScoreOrderByOrderNo(orderNo, memberId, scoreGoodsId);
		if(null==map){
			return Util.resultMap(configCode.code_1015,null);
		}
		return Util.resultMap(configCode.code_1001, map);
	}

	/**
	 * 查询订单记录
	 */
	@Override
	public Map<String, Object> queryScoreOrder(Order order,Integer page,Integer row) {
		List<Map<String, Object>> list=scoreGoodsDao.queryScoreOrder(order, page, row);
		if(null==list){
			return Util.resultMap(configCode.code_1015,null);
		}
		return Util.resultMap(configCode.code_1001, list);
	}

	/**
	 * 查询商品的图片
	 */
	@Override
	public Map<String, Object> queryScoreGoodsImg(String scoreGoodsId) {
		List<Map<String, Object>> list=scoreGoodsDao.queryScoreGoodsImg(scoreGoodsId);
		if(null==list){
			return Util.resultMap(configCode.code_1015,null);
		}
		return Util.resultMap(configCode.code_1001, list);
	}
}
