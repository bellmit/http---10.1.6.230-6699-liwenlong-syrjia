package cn.syrjia.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import cn.syrjia.callCenter.util.CallCenterConfig;
import cn.syrjia.callCenter.util.SendCallCenterUtil;
import cn.syrjia.common.impl.BaseServiceImpl;
import cn.syrjia.config.configCode;
import cn.syrjia.dao.GoodsDao;
import cn.syrjia.dao.GoodsOrderDao;
import cn.syrjia.dao.GoodsShopCartDao;
import cn.syrjia.entity.Order;
import cn.syrjia.entity.OrderActivity;
import cn.syrjia.entity.OrderActivitySale;
import cn.syrjia.entity.ShippingAddress;
import cn.syrjia.entity.vo.GoodsOrderDetail;
import cn.syrjia.service.GoodsOrderService;
import cn.syrjia.service.GoodsShopCartService;
import cn.syrjia.util.JsonUtil;
import cn.syrjia.util.Util;
import cn.syrjia.weixin.util.StringUtils;
import cn.syrjia.wxPay.wxPay.util.StringUtil;

@Service("goodsOrderService")
public class GoodsOrderServiceImpl extends BaseServiceImpl implements
		GoodsOrderService {

	@Resource(name = "goodsOrderDao")
	GoodsOrderDao goodsOrderDao;

	@Resource(name = "goodsShopCartDao")
	GoodsShopCartDao goodsShopCartDao;
	
	@Resource(name = "goodsShopCartService")
	GoodsShopCartService goodsShopCartService;

	@Resource(name = "goodsDao")
	GoodsDao goodsDao;

	/**
	 * 添加订单 购物车
	 * @param goodsIds
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Map<String, Object> addGoodsOrder(Order order,
			String shippingAddressId, String[] shopCarts) {
		//判断order是否为空
		if (null == order || StringUtil.isEmpty(order.getMemberId())
				|| null == shopCarts || shopCarts.length == 0|| StringUtil.isEmpty(shippingAddressId)) {
			return Util.resultMap(configCode.code_1066, null);
		}
		//初始化数据
		Double total = 0.0;
		Double originalTotal = 0.0;
		List<GoodsOrderDetail> goodsOrderDetails = new ArrayList<GoodsOrderDetail>();
		List<OrderActivity> orderActivitys = new ArrayList<OrderActivity>();
		List<OrderActivitySale> orderActivitySales = new ArrayList<OrderActivitySale>();
		Map<String, Object> platformActivity = new HashMap<String, Object>();
		//生成订单号
		String orderNo = goodsOrderDao.orderNo();
		order.setOrderNo(orderNo);
		for (String shopCart : shopCarts) {//循环购物车商品
			Map<String, Object> goods = goodsShopCartDao
					.queryGoodsByShopCartId(shopCart);//查询购物车商品
			if (null == goods || goods.size() == 0) {//如果商品已不存在
				return Util.resultMap(configCode.code_1067, null);
			}
			if (Integer.parseInt(goods.get("buyCount").toString()) > Integer
					.parseInt(goods.get("stock").toString())) {///查询最大购买量
				return Util.resultMap(configCode.code_1021, null);
			}
			Double originalPrice = Double.parseDouble(goods.get("price")//价格
					.toString());
			Integer buyCount = Integer.parseInt(goods.get("buyCount")//数量
					.toString());
			Double goodsOriginalTotal = originalPrice * buyCount;//总价
			Double goodsPrice = originalPrice * buyCount;//总价
			// Double goodsSale=0.0;
			total = Util.add(total + "", goodsOriginalTotal + "");//累计总价
			String orderDetailId = Util.getUUID();
			List<Map<String, Object>> activitys = goodsDao.queryActivity(goods
					.get("id").toString());//查询商品活动
			for (Map<String, Object> activity : activitys) {//循环活动
				List<Map<String, Object>> activityDetails = goodsDao
						.queryActivityDetail(activity.get("id").toString(),
								Integer.parseInt(activity.get("type")
										.toString()));//查询活动详情
				if (activity.get("type").toString().equals("1")) {//如果是商家活动
					
					Integer buyCounted=goodsOrderDao.queryBuyConnt(order.getMemberId(), activity.get("id").toString(), goods.get("priceNumId").toString(),null);//查询是否限购
					if(buyCounted>=Integer.parseInt(activity.get("activityNum").toString())||buyCount>Integer.parseInt(activity.get("activityNum").toString())){//如果超出限购数量
						TransactionAspectSupport.currentTransactionStatus()
						.setRollbackOnly();
						return Util.resultMap(configCode.code_1079,null);
					}
					for (Map<String, Object> activityDetail : activityDetails) {//循环活动详情
						if (goodsOriginalTotal >= Double
								.parseDouble(activityDetail
										.get("activityPrice").toString())) {//如果超过活动价格
							if (activity.get("activityType").toString()
									.equals("1")) {//如果满减
								goodsPrice = Util.multiply(
										goodsOriginalTotal + "",
										activityDetail.get("activityFold")
												.toString()).doubleValue();
							} else if (activity.get("activityType").toString()
									.equals("2")) {//如果满折
								goodsPrice = Util.subtract(
										goodsOriginalTotal + "",
										activityDetail.get("activityFold")
												.toString()).doubleValue();
							}
							break;
						}
					}
					
				} else {//平台活动
					if (null != platformActivity.get(activity.get("id")
							.toString())) {//如果此活动已放入集合 则累计价格
						
						Map<String, Object> activityDetail = (Map<String, Object>) platformActivity.get(activity.get("id")
								.toString());
						activityDetail.put("price", Util.add(activityDetail
								.get("price").toString(), goodsPrice + ""));
						activityDetail.put("goodsOriginalTotal", Util.add(
								activityDetail.get("goodsOriginalTotal")
										.toString(), goodsPrice + ""));
					} else {//如果此活动没放入集合
						Map<String, Object> activityDetail = new HashMap<String, Object>();
						activityDetail.put("price", goodsPrice);
						activityDetail.put("activityType",
								activity.get("activityType").toString());
						activityDetail.put("goodsOriginalTotal",
								goodsOriginalTotal);
						activityDetail.put("activityDetail", activityDetails);
						platformActivity.put(activity.get("id").toString(),
								activityDetail);

					}
				}
				OrderActivity orderActivity = new OrderActivity();//此商品订单活动
				//订单活动赋值
				orderActivity.setActivityId(activity.get("id").toString());
				orderActivity.setGoodsId(goods.get("id").toString());
				orderActivity.setOrderNo(orderNo);
				orderActivity.setId(Util.getUUID());
				orderActivity.setOrderDetialId(orderDetailId);
				orderActivity.setType(Integer.parseInt(activity.get("type").toString()));
				orderActivity.setActivityType(Integer.parseInt(activity.get("activityType").toString()));
				//保存订单活动
				orderActivitys.add(orderActivity);
				//遍历订单详情
				for(Map<String,Object> activityDetail:activityDetails){
					OrderActivitySale orderActivitySale=new OrderActivitySale();
					orderActivitySale.setId(Util.getUUID());
					orderActivitySale.setActivityId(orderActivity.getId());
					orderActivitySale.setActivityFold(Double.parseDouble(activityDetail.get("activityFold").toString()));
					orderActivitySale.setActivityPrice(Double.parseDouble(activityDetail.get("activityPrice").toString()));
					//保存orderActivitySale
					orderActivitySales.add(orderActivitySale);
				}
			}

			originalTotal = Util.add(originalTotal + "", goodsPrice + "");//累计价格

			GoodsOrderDetail goodsOrderDetail = new GoodsOrderDetail();//商品详情
			goodsOrderDetail.setId(orderDetailId);
			goodsOrderDetail.setCreateTime(Util.queryNowTime());
			goodsOrderDetail.setGoodsId(goods.get("id").toString());
			goodsOrderDetail.setGoodsNum(buyCount);
			goodsOrderDetail.setGoodsPrice(Util.divide(goodsPrice + "",
					buyCount + "").doubleValue());//促销活动单价
			goodsOrderDetail.setGoodsOriginalPrice(originalPrice);//商品单价（原价）
			goodsOrderDetail.setGoodsPriceNumId(goods.get("priceNumId")
					.toString());
			goodsOrderDetail.setGoodsTotal(goodsPrice);//促销活动后价格
			goodsOrderDetail.setOrderNo(orderNo);
			goodsOrderDetail.setPaymentStatus(1);
			goodsOrderDetail.setType(1);
			//保存goodsOrderDetail
			goodsOrderDetails.add(goodsOrderDetail);
			Integer i=goodsOrderDao.restocking(goods.get("priceNumId").toString(), buyCount);//如果超过购买数量
			if(i==0){
				TransactionAspectSupport.currentTransactionStatus()
				.setRollbackOnly();
				return Util.resultMap(configCode.code_1021, orderNo);
			}
		}
		
		order.setGoodsPrice(originalTotal);

		for (Map.Entry<String, Object> entry : platformActivity.entrySet()) {//计算平台活动
			// String key = entry.getKey().toString();
			Map<String, Object> activityDetail = (Map<String, Object>) entry
					.getValue();
			List<Map<String, Object>> activityDetails = (List<Map<String, Object>>) activityDetail
					.get("activityDetail");
			for (Map<String, Object> activity : activityDetails) {
				if (Double.parseDouble(activityDetail.get("goodsOriginalTotal")
						.toString()) >= Double.parseDouble(activity.get(
						"activityPrice").toString())) {
					if (activityDetail.get("activityType").toString()
							.equals("1")) {//如果满减
						System.out.println(Double.parseDouble(activity.get(
								"activityFold").toString()) / 10);
						originalTotal = originalTotal
								- Util.subtract(
										activityDetail.get("price").toString(),
										Util.multiply(
												activityDetail.get("price")
														.toString(),
												Double.parseDouble(activity
														.get("activityFold")
														.toString())
														/ 10 + "").toString())
										.doubleValue();
					} else if (activityDetail.get("activityType").toString()
							.equals("2")) {//满折
						originalTotal = originalTotal
								- Util.subtract(
										activityDetail.get("price").toString(),
										Util.subtract(
												activityDetail.get("price")
														.toString(),
												activity.get("activityFold")
														.toString()).toString())
										.doubleValue();
					}
				}
			}
		}

		if (!StringUtils.isEmpty(shippingAddressId)) {//如果有收货地址
			ShippingAddress shippingAddress = goodsShopCartDao.queryById(
					ShippingAddress.class, shippingAddressId); //查询收货地址信息
			if (null == shippingAddress
					|| StringUtil.isEmpty(shippingAddress.getId())) {
				TransactionAspectSupport.currentTransactionStatus()
						.setRollbackOnly();
				return Util.resultMap(configCode.code_1069, shippingAddressId);
			}
			//订单赋值
			order.setConsignee(shippingAddress.getConsignee());
			order.setDetailedAddress(shippingAddress.getDetailedAddress());
			order.setPhone(shippingAddress.getPhone());
			order.setProvince(shippingAddress.getProvince());
			order.setArea(shippingAddress.getArea());
			order.setCity(shippingAddress.getCity());
		}
		//订单赋值
		order.setCreateTime(Util.queryNowTime());
		order.setOrderPrice(total);
		order.setReceiptsPrice(originalTotal);
		//查询购物车邮费
		Map<String,Object> postage=goodsShopCartService.queryPostageByCartIds(order.getCity(), shopCarts);//计算邮费
		if(!postage.get("respCode").toString().equals("1001")){
			return postage;
		}
		order.setPostage(Double.parseDouble(JsonUtil.jsonToMap(postage.get("data")).get("result").toString()));
		order.setReceiptsPrice(order.getReceiptsPrice()+order.getPostage());
		order.setGoodsPrice(order.getGoodsPrice()+order.getPostage());
		order.setOrderPrice(order.getOrderPrice()+order.getPostage());
		//保存订单
		Object obj = goodsShopCartDao.addEntity(order);
		if (StringUtils.isEmpty(obj)) {
			TransactionAspectSupport.currentTransactionStatus()
					.setRollbackOnly();
			return Util.resultMap(configCode.code_1066, orderNo);
		}
		if(orderActivitys.size()>0){
			//保存orderActivitys
			obj = goodsShopCartDao.addEntity(orderActivitys);
			if (StringUtils.isEmpty(obj)) {
				TransactionAspectSupport.currentTransactionStatus()
						.setRollbackOnly();
				return Util.resultMap(configCode.code_1066, orderNo);
			}
		}
		if(orderActivitySales.size()>0){
			//保存orderActivitySales
			obj = goodsShopCartDao.addEntity(orderActivitySales);
			if (StringUtils.isEmpty(obj)) {
				TransactionAspectSupport.currentTransactionStatus()
				.setRollbackOnly();
				return Util.resultMap(configCode.code_1066, orderNo);
			}
		}
		//保存goodsOrderDetails
		obj = goodsShopCartDao.addEntity(goodsOrderDetails);
		if (StringUtils.isEmpty(obj)) {
			TransactionAspectSupport.currentTransactionStatus()
					.setRollbackOnly();
			return Util.resultMap(configCode.code_1066, orderNo);
		} else {
			//删除购物车
			goodsShopCartDao.deleteGoodsShopCart(shopCarts);
			//查询呼叫中心数据
			Map<String, Object> map = goodsOrderDao.querySendCallCenterData(orderNo);
			if (map != null) {
				String result = SendCallCenterUtil.sendCallCenterData(map,
						CallCenterConfig.Order);
				System.out.println(result);
			}
			return Util.resultMap(configCode.code_1001, orderNo);
		}
	}

	/**
	 * 添加订单 开方百宝箱
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Map<String, Object> addGoodsOrderByGoodsList(List<Map<String,Object>> goods,String memberId,String sourceOrderNo,String doctorId) {
		//根据主订单号删除订单信息
		goodsOrderDao.deleteOrderByMainOrder(sourceOrderNo);
		//判断goods是否为空
		if (null==goods||goods.size()==0||StringUtil.isEmpty(sourceOrderNo)) {
			return Util.resultMap(configCode.code_1066, null);
		}
		//创建订单实体
		Order order = new Order();
		//初始化订单数据
		order.setMainOrderNo(sourceOrderNo);
		Double total = 0.0;
		Double originalTotal = 0.0;
		List<OrderActivity> orderActivitys = new ArrayList<OrderActivity>();
		List<OrderActivitySale> orderActivitySales = new ArrayList<OrderActivitySale>();
		Map<String, Object> platformActivity = new HashMap<String, Object>();
		List<GoodsOrderDetail> goodsOrderDetails = new ArrayList<GoodsOrderDetail>();
		//生成订单号
		String orderNo = goodsOrderDao.orderNo();
		//遍历商品
		for(Map<String,Object> g:goods){
			//根据商品和规格参数查询商品信息
			Map<String, Object> good = goodsShopCartDao
					.queryGoodsByGoodsIdAndPriceNumId(g.get("goodsId").toString(),g.get("priceNumId").toString(), memberId);
			if (null == good || good.size() == 0) {
				return Util.resultMap(configCode.code_1067, null);
			}
			if (Integer.parseInt(g.get("buyCount").toString()) > Integer.parseInt(good.get("stock").toString())) {
				return Util.resultMap(configCode.code_1021, null);
			}
			//价格数据计算
			Double originalPrice = Double
					.parseDouble(good.get("price").toString());
			Double goodsOriginalTotal = originalPrice * Integer.parseInt(g.get("buyCount").toString());
			Double goodsPrice = originalPrice * Integer.parseInt(g.get("buyCount").toString());
			total = Util.add(total + "", goodsOriginalTotal + "");
			// Double goodsSale=0.0;
			String orderDetailId = Util.getUUID();
			//查询商品活动
			List<Map<String, Object>> activitys = goodsDao.queryActivity(good.get("id").toString());
			//遍历activitys
			for (Map<String, Object> activity : activitys) {
				//查询商品活动详情
				List<Map<String, Object>> activityDetails = goodsDao
						.queryActivityDetail(activity.get("id").toString(),
								Integer.parseInt(activity.get("type").toString()));
				if (activity.get("type").toString().equals("1")) {
					if(!StringUtil.isEmpty(memberId)){
						//根据用户id查询已购买数量
						Integer buyCounted=goodsOrderDao.queryBuyConnt(memberId, activity.get("id").toString(),g.get("priceNumId").toString(),null);
						if(buyCounted>=Integer.parseInt(activity.get("activityNum").toString())||Integer.parseInt(g.get("buyCount").toString())>Integer.parseInt(activity.get("activityNum").toString())){
							TransactionAspectSupport.currentTransactionStatus()
							.setRollbackOnly();
							return Util.resultMap(configCode.code_1079,null);
						}
					}
					for (Map<String, Object> activityDetail : activityDetails) {
						if (goodsOriginalTotal >= Double.parseDouble(activityDetail
								.get("activityPrice").toString())) {
							if (activity.get("activityType").toString().equals("1")) {
								goodsPrice = Util.multiply(
										goodsOriginalTotal + "",
										activityDetail.get("activityFold")
												.toString()).doubleValue();
							} else if (activity.get("activityType").toString()
									.equals("2")) {
								goodsPrice = Util.subtract(
										goodsOriginalTotal + "",
										activityDetail.get("activityFold")
												.toString()).doubleValue();
							}
							break;
						}
					}
				} else {
					if (null != platformActivity.get(activity.get("id").toString())) {
						Map<String, Object> activityDetail = (Map<String, Object>) platformActivity.get(activity.get("id")
								.toString());
						activityDetail.put("price", Util.add(activityDetail
								.get("price").toString(), goodsPrice + ""));
						activityDetail.put("goodsOriginalTotal", Util.add(
								activityDetail.get("goodsOriginalTotal")
										.toString(), goodsPrice + ""));
					} else {
						//创建活动详情实体
						Map<String, Object> activityDetail = new HashMap<String, Object>();
						//活动详情赋值
						activityDetail.put("price", goodsPrice);
						activityDetail.put("activityType",
								activity.get("activityType").toString());
						activityDetail
								.put("goodsOriginalTotal", goodsOriginalTotal);
						activityDetail.put("activityDetail", activityDetails);
						platformActivity.put(activity.get("id").toString(),
								activityDetail);

					}
				}
				//创建活动订单
				OrderActivity orderActivity = new OrderActivity();
				//活动订单赋值
				orderActivity.setActivityId(activity.get("id").toString());
				orderActivity.setGoodsId(good.get("id").toString());
				orderActivity.setOrderNo(orderNo);
				orderActivity.setId(Util.getUUID());
				orderActivity.setOrderDetialId(orderDetailId);
				orderActivity.setType(Integer.parseInt(activity.get("type").toString()));
				orderActivity.setActivityType(Integer.parseInt(activity.get("activityType").toString()));
				//保存活动订单
				orderActivitys.add(orderActivity);
				//遍历活动详情
				for(Map<String,Object> activityDetail:activityDetails){
					//创建OrderACtivitySales
					OrderActivitySale orderActivitySale=new OrderActivitySale();
					//赋值
					orderActivitySale.setId(Util.getUUID());
					orderActivitySale.setActivityId(orderActivity.getId());
					orderActivitySale.setActivityFold(Double.parseDouble(activityDetail.get("activityFold").toString()));
					orderActivitySale.setActivityPrice(Double.parseDouble(activityDetail.get("activityPrice").toString()));
					//保存OrderActivitySale
					orderActivitySales.add(orderActivitySale);
				}
			}
			
			originalTotal = Util.add(originalTotal + "", goodsPrice + "");
			//订单赋值
			order.setGoodsPrice(originalTotal);
			//创建商品订单详情
			GoodsOrderDetail goodsOrderDetail = new GoodsOrderDetail();
			//商品订单赋值
			goodsOrderDetail.setId(orderDetailId);
			if(!StringUtil.isEmpty(doctorId)){
				goodsOrderDetail.setDoctorId(doctorId);
			}
			//赋值
			goodsOrderDetail.setCreateTime(Util.queryNowTime());
			goodsOrderDetail.setGoodsId(good.get("id").toString());
			goodsOrderDetail.setGoodsNum(Integer.parseInt(g.get("buyCount").toString()));
			goodsOrderDetail.setGoodsPrice(Util.divide(goodsPrice + "",
					g.get("buyCount").toString() + "").doubleValue());
			goodsOrderDetail.setGoodsOriginalPrice(originalPrice);
			goodsOrderDetail.setGoodsPriceNumId(g.get("priceNumId").toString());
			goodsOrderDetail.setGoodsTotal(goodsPrice);
			goodsOrderDetail.setOrderNo(orderNo);
			goodsOrderDetail.setPaymentStatus(1);
			goodsOrderDetail.setType(1);
			goodsOrderDetails.add(goodsOrderDetail);
			//减去库存
			Integer i=goodsOrderDao.restocking(g.get("priceNumId").toString(), Integer.parseInt(g.get("buyCount").toString()));
			if(i==0){
				TransactionAspectSupport.currentTransactionStatus()
				.setRollbackOnly();
				return Util.resultMap(configCode.code_1021, orderNo);
			}

		}
		
		for (Map.Entry<String, Object> entry : platformActivity.entrySet()) {
			// String key = entry.getKey().toString();
			Map<String, Object> activityDetail = (Map<String, Object>) entry
					.getValue();
			List<Map<String, Object>> activityDetails = (List<Map<String, Object>>) activityDetail
					.get("activityDetail");
			//遍历活动详情
			for (Map<String, Object> activity : activityDetails) {
				if (Double.parseDouble(activityDetail.get("goodsOriginalTotal")
						.toString()) >= Double.parseDouble(activity.get(
						"activityPrice").toString())) {
					if (activityDetail.get("activityType").toString()
							.equals("1")) {
						originalTotal = originalTotal
								- Util.subtract(
										activityDetail.get("price").toString(),
										Util.multiply(
												activityDetail.get("price")
														.toString(),
												Double.parseDouble(activity
														.get("activityFold")
														.toString())
														/ 10 + "").toString())
										.doubleValue();
					} else if (activityDetail.get("activityType").toString()
							.equals("2")) {
						originalTotal = originalTotal
								- Util.subtract(
										activityDetail.get("price").toString(),
										Util.subtract(
												activityDetail.get("price")
														.toString(),
												activity.get("activityFold")
														.toString()).toString())
										.doubleValue();
					}
				}
			}
		}
		//订单赋值
		order.setOrderNo(orderNo);
		order.setCreateTime(Util.queryNowTime());
		order.setOrderPrice(total);
		order.setReceiptsPrice(originalTotal);
		order.setMemberId(memberId);
		order.setDoctorId(doctorId);
		//保存订单
		Object obj = goodsShopCartDao.addEntity(order);
		if (StringUtils.isEmpty(obj)) {
			TransactionAspectSupport.currentTransactionStatus()
					.setRollbackOnly();
			return Util.resultMap(configCode.code_1066, orderNo);
		}
		if (orderActivitys.size() > 0) {
			//保存orderActivitys
			obj = goodsShopCartDao.addEntity(orderActivitys);
			if (StringUtils.isEmpty(obj)) {
				TransactionAspectSupport.currentTransactionStatus()
						.setRollbackOnly();
				return Util.resultMap(configCode.code_1066, orderNo);
			}
		}
		
		if(orderActivitySales.size()>0){
			//保存orderActivitySales
			obj = goodsShopCartDao.addEntity(orderActivitySales);
			if (StringUtils.isEmpty(obj)) {
				TransactionAspectSupport.currentTransactionStatus()
				.setRollbackOnly();
				return Util.resultMap(configCode.code_1066, orderNo);
			}
		}
		//保存goodsOrderDetails
		obj = goodsShopCartDao.addEntity(goodsOrderDetails);
		if (StringUtils.isEmpty(obj)) {
			TransactionAspectSupport.currentTransactionStatus()
					.setRollbackOnly();
			return Util.resultMap(configCode.code_1066, null);
		}
		//查询呼叫中心数据
		Map<String, Object> map = goodsOrderDao.querySendCallCenterData(orderNo);
		if (map != null) {
			String result = SendCallCenterUtil.sendCallCenterData(map,
					CallCenterConfig.Order);
			System.out.println(result);
		}
		return Util.resultMap(configCode.code_1001, originalTotal);
	}
	
	/**
	 * 添加订单 直接购买
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Map<String, Object> addGoodsOrderByGoods(String goodsId,
			String shippingAddressId, String buyCount, String priceNumId,
			String memberId,String doctorId,String patientId) {
		//判断参数是否为空
		if (StringUtil.isEmpty(goodsId)
				|| StringUtil.isEmpty(shippingAddressId)
				|| StringUtil.isEmpty(buyCount) || StringUtil.isEmpty(memberId)
				|| StringUtil.isEmpty(priceNumId)|| StringUtil.isEmpty(shippingAddressId)) {
			return Util.resultMap(configCode.code_1066, null);
		}
		//根据商品和规格参数查询商品信息
		Map<String, Object> goods = goodsShopCartDao
				.queryGoodsByGoodsIdAndPriceNumId(goodsId, priceNumId, memberId);
		//判断goods是否为空
		if (null == goods || goods.size() == 0) {
			return Util.resultMap(configCode.code_1067, null);
		}
		//转换Integer格式
		if (Integer.parseInt(buyCount) > Integer.parseInt(goods.get("stock")
				.toString())) {
			return Util.resultMap(configCode.code_1021, null);
		}

		List<OrderActivity> orderActivitys = new ArrayList<OrderActivity>();
		List<OrderActivitySale> orderActivitySales = new ArrayList<OrderActivitySale>();
		Map<String, Object> platformActivity = new HashMap<String, Object>();
		//生成订单号
		String orderNo = goodsOrderDao.orderNo();
		//获取价格
		Double originalPrice = Double
				.parseDouble(goods.get("price").toString());
		Double goodsOriginalTotal = originalPrice * Integer.parseInt(buyCount);
		Double goodsPrice = originalPrice * Integer.parseInt(buyCount);
		// Double goodsSale=0.0;
		String orderDetailId = Util.getUUID();
		//查询商品活动
		List<Map<String, Object>> activitys = goodsDao.queryActivity(goods.get(
				"id").toString());
		//循环遍历活动
		for (Map<String, Object> activity : activitys) {
			//查询商品活动详情
			List<Map<String, Object>> activityDetails = goodsDao
					.queryActivityDetail(activity.get("id").toString(),
							Integer.parseInt(activity.get("type").toString()));
			if (activity.get("type").toString().equals("1")) {
				//根据用户id查询已购买数量
				Integer buyCounted=goodsOrderDao.queryBuyConnt(memberId, activity.get("id").toString(),priceNumId,null);
				if(buyCounted>=Integer.parseInt(activity.get("activityNum").toString())||Integer.parseInt(buyCount)>Integer.parseInt(activity.get("activityNum").toString())){
					TransactionAspectSupport.currentTransactionStatus()
					.setRollbackOnly();
					return Util.resultMap(configCode.code_1079,null);
				}
				//遍历activityDetails
				for (Map<String, Object> activityDetail : activityDetails) {
					if (goodsOriginalTotal >= Double.parseDouble(activityDetail
							.get("activityPrice").toString())) {
						if (activity.get("activityType").toString().equals("1")) {
							goodsPrice = Util.multiply(
									goodsOriginalTotal + "",
									activityDetail.get("activityFold")
											.toString()).doubleValue();
						} else if (activity.get("activityType").toString()
								.equals("2")) {
							goodsPrice = Util.subtract(
									goodsOriginalTotal + "",
									activityDetail.get("activityFold")
											.toString()).doubleValue();
						}
						break;
					}
				}
			} else {
				if (null != platformActivity.get(activity.get("id").toString())) {
					Map<String, Object> activityDetail = (Map<String, Object>) platformActivity.get(activity.get("id")
							.toString());
					activityDetail.put("price", Util.add(activityDetail
							.get("price").toString(), goodsPrice + ""));
					activityDetail.put("goodsOriginalTotal", Util.add(
							activityDetail.get("goodsOriginalTotal")
									.toString(), goodsPrice + ""));
				} else {
					//初始化活动详情
					Map<String, Object> activityDetail = new HashMap<String, Object>();
					activityDetail.put("price", goodsPrice);
					activityDetail.put("activityType",
							activity.get("activityType").toString());
					activityDetail
							.put("goodsOriginalTotal", goodsOriginalTotal);
					activityDetail.put("activityDetail", activityDetails);
					platformActivity.put(activity.get("id").toString(),
							activityDetail);

				}
			}
			//创建订单活动并赋值
			OrderActivity orderActivity = new OrderActivity();
			orderActivity.setActivityId(activity.get("id").toString());
			orderActivity.setGoodsId(goods.get("id").toString());
			orderActivity.setOrderNo(orderNo);
			orderActivity.setId(Util.getUUID());
			orderActivity.setOrderDetialId(orderDetailId);
			orderActivity.setType(Integer.parseInt(activity.get("type").toString()));
			orderActivity.setActivityType(Integer.parseInt(activity.get("activityType").toString()));
			//保存订单活动
			orderActivitys.add(orderActivity);
			for(Map<String,Object> activityDetail:activityDetails){
				//创建OrderActivitySale对象，并赋值
				OrderActivitySale orderActivitySale=new OrderActivitySale();
				orderActivitySale.setId(Util.getUUID());
				orderActivitySale.setActivityId(orderActivity.getId());
				orderActivitySale.setActivityFold(Double.parseDouble(activityDetail.get("activityFold").toString()));
				orderActivitySale.setActivityPrice(Double.parseDouble(activityDetail.get("activityPrice").toString()));
				//执行保存
				orderActivitySales.add(orderActivitySale);
			}
		}
		//创建订单对象
		Order order = new Order();
		//设置商品价格
		order.setGoodsPrice(goodsPrice);
		
		//遍历平台活动
		for (Map.Entry<String, Object> entry : platformActivity.entrySet()) {
			// String key = entry.getKey().toString();
			Map<String, Object> activityDetail = (Map<String, Object>) entry
					.getValue();
			//获取活动详情
			List<Map<String, Object>> activityDetails = (List<Map<String, Object>>) activityDetail
					.get("activityDetail");
			//遍历活动详情
			for (Map<String, Object> activity : activityDetails) {
				if (Double.parseDouble(activityDetail.get("goodsOriginalTotal")
						.toString()) >= Double.parseDouble(activity.get(
						"activityPrice").toString())) {
					if (activityDetail.get("activityType").toString()
							.equals("1")) {
						//计算商品价格
						goodsPrice = goodsPrice
								- Util.subtract(
										activityDetail.get("price").toString(),
										Util.multiply(
												activityDetail.get("price")
														.toString(),
												Double.parseDouble(activity
														.get("activityFold")
														.toString())
														/ 10 + "").toString())
										.doubleValue();
					} else if (activityDetail.get("activityType").toString()
							.equals("2")) {
						//计算商品价格
						goodsPrice = goodsPrice
								- Util.subtract(
										activityDetail.get("price").toString(),
										Util.subtract(
												activityDetail.get("price")
														.toString(),
												activity.get("activityFold")
														.toString()).toString())
										.doubleValue();
					}
				}
			}
		}
		//创建商品订单详情，并赋值
		GoodsOrderDetail goodsOrderDetail = new GoodsOrderDetail();
		goodsOrderDetail.setId(orderDetailId);
		goodsOrderDetail.setCreateTime(Util.queryNowTime());
		goodsOrderDetail.setGoodsId(goods.get("id").toString());
		goodsOrderDetail.setGoodsNum(Integer.parseInt(buyCount));
		goodsOrderDetail.setGoodsPrice(Util.divide(goodsPrice + "",
				buyCount + "").doubleValue());
		goodsOrderDetail.setGoodsOriginalPrice(originalPrice);
		goodsOrderDetail.setGoodsPriceNumId(priceNumId);
		goodsOrderDetail.setGoodsTotal(goodsPrice);
		goodsOrderDetail.setOrderNo(orderNo);
		goodsOrderDetail.setPaymentStatus(1);
		goodsOrderDetail.setType(1);
		//减去库存
		Integer i=goodsOrderDao.restocking(priceNumId, Integer.parseInt(buyCount));
		if(i==0){
			TransactionAspectSupport.currentTransactionStatus()
			.setRollbackOnly();
			return Util.resultMap(configCode.code_1021, orderNo);
		}
		//设置订单号
		order.setOrderNo(orderNo);
		//根据id查询单个
		ShippingAddress shippingAddress = goodsShopCartDao.queryById(
				ShippingAddress.class, shippingAddressId);
		if (null == shippingAddress
				|| StringUtil.isEmpty(shippingAddress.getId())) {
			TransactionAspectSupport.currentTransactionStatus()
					.setRollbackOnly();
			return Util.resultMap(configCode.code_1069, shippingAddressId);
		}
		//order赋值
		order.setConsignee(shippingAddress.getConsignee());
		order.setDetailedAddress(shippingAddress.getDetailedAddress());
		order.setPhone(shippingAddress.getPhone());
		order.setProvince(shippingAddress.getProvince());
		order.setArea(shippingAddress.getArea());
		order.setCity(shippingAddress.getCity());

		order.setCreateTime(Util.queryNowTime());
		order.setOrderPrice(goodsOriginalTotal);
		order.setReceiptsPrice(goodsPrice);
		order.setMemberId(memberId);
		order.setDoctorId(doctorId);
		order.setPatientId(patientId);
		//查询购物车邮费
		Map<String,Object> postage=goodsShopCartService.queryPostageByCityName(order.getCity(), goodsId,Integer.parseInt(buyCount));
		if(!postage.get("respCode").toString().equals("1001")){
			return postage;
		}
		order.setPostage(Double.parseDouble(JsonUtil.jsonToMap(postage.get("data")).get("result").toString()));
		order.setReceiptsPrice(order.getReceiptsPrice()+order.getPostage());
		order.setGoodsPrice(order.getGoodsPrice()+order.getPostage());
		order.setOrderPrice(order.getOrderPrice()+order.getPostage());
		//保存order
		Object obj = goodsShopCartDao.addEntity(order);
		if (StringUtils.isEmpty(obj)) {
			TransactionAspectSupport.currentTransactionStatus()
					.setRollbackOnly();
			return Util.resultMap(configCode.code_1066, orderNo);
		}
		if (orderActivitys.size() > 0) {
			//保存orderActivitys
			obj = goodsShopCartDao.addEntity(orderActivitys);
			if (StringUtils.isEmpty(obj)) {
				TransactionAspectSupport.currentTransactionStatus()
						.setRollbackOnly();
				return Util.resultMap(configCode.code_1066, orderNo);
			}
		}
		
		if(orderActivitySales.size()>0){
			//保存orderActvitySales
			obj = goodsShopCartDao.addEntity(orderActivitySales);
			if (StringUtils.isEmpty(obj)) {
				TransactionAspectSupport.currentTransactionStatus()
				.setRollbackOnly();
				return Util.resultMap(configCode.code_1066, orderNo);
			}
		}
		//保存goodsOrderDetail
		obj = goodsShopCartDao.addEntity(goodsOrderDetail);
		if (StringUtils.isEmpty(obj)) {
			TransactionAspectSupport.currentTransactionStatus()
					.setRollbackOnly();
			return Util.resultMap(configCode.code_1066, orderNo);
		}
		//查询呼叫中心数据
		Map<String, Object> map = goodsOrderDao.querySendCallCenterData(orderNo);
		if (map != null) {
			String result = SendCallCenterUtil.sendCallCenterData(map,
					CallCenterConfig.Order);
			System.out.println(result);
		}
		return Util.resultMap(configCode.code_1001, orderNo);
	}

	/**
	 * 查询所有订单
	 * 
	 * @param openId
	 *            openId
	 * @param order
	 *            订单实体
	 * @param row
	 *            行数
	 * @param page
	 *            页数
	 * @pdOid b0943165-1c13-44fb-b1df-892a07dca3fc
	 */
	public Map<String, Object> queryOrderList(String memberId, Order order,
			Integer row, Integer page) {
		//查询订单列表
		List<Map<String, Object>> list = goodsOrderDao.queryOrderList(memberId,
				order, row, page);
		if (null == list) {
			return Util.resultMap(configCode.code_1015, list);
		} 
		/*for(Map<String,Object> map:list){
			if(StringUtil.isEmpty(map.get("postage"))){
				String cityId=goodsDao.queryCityIdByCity(map.get("city").toString());
				String postage=goodsDao.queryPostageByGoodsIds(cityId,map.get("orderNo").toString());
				map.put("postage", postage);
			}
		}*/
		return Util.resultMap(configCode.code_1001, list);
	}
	
	/**
	 * 查询所有订单
	 * 
	 * @param openId
	 *            openId
	 * @param order
	 *            订单实体
	 * @param row
	 *            行数
	 * @param page
	 *            页数
	 * @pdOid b0943165-1c13-44fb-b1df-892a07dca3fc
	 */
	public Map<String, Object> queryOrderByOrderNo(String memberId, Order order) {
		//根据订单号查询订单信息
		Map<String, Object> map = goodsOrderDao.queryOrderByOrderNo(memberId,order);
		if (null == map) {
			return Util.resultMap(configCode.code_1015, map);
		}
		//判断postage是否为空
		if(StringUtil.isEmpty(map.get("postage"))&&!StringUtil.isEmpty(map.get("city"))){
			String cityId=goodsDao.queryCityIdByCity(map.get("city").toString());
			String postage=goodsDao.queryPostageByGoodsIds(cityId, map.get("orderNo").toString());
			map.put("postage", postage);
		}
		return Util.resultMap(configCode.code_1001, map);
	}
	
	/**
	 * 查询订单详情
	 * 
	 * @param openId
	 *            openId
	 * @param order
	 *            订单实体
	 * @param row
	 *            行数
	 * @param page
	 *            页数
	 * @pdOid b0943165-1c13-44fb-b1df-892a07dca3fc
	 */
	public Map<String, Object> queryOrderDetailByOrderNo(String memberId, Order order) {
		//根据订单号查询订单信息
		List<Map<String, Object>> list = goodsOrderDao.queryOrderDetailByOrderNo(memberId,order);
		return Util.resultMap(configCode.code_1001, list);
	}

	/**
	 * 订单退款
	 * 
	 * @param openId
	 * @param orderNo
	 *            订单号
	 * @pdOid a7f241e2-bdf1-47ef-9dd6-217152f9643a
	 */
	public Integer refundOrder(String openId, String orderNo, String refundMsg) {
		return goodsOrderDao.refundOrder(openId, orderNo, refundMsg);
	}

	/**
	 * 根据订单信息 查询订单详情
	 */
	@Override
	public Map<String, Object> queryOrderForDetail(Order order) {
		//查询订单详情
		Map<String, Object> map = goodsOrderDao.queryOrderForDetail(order);
		if (null == map || StringUtil.isEmpty(map.get("orderNo"))) {
			return Util.resultMap(configCode.code_1071, map);
		}
		//根据城市id查询城市名
		String cityId=goodsDao.queryCityIdByCity(map.get("city").toString());
		//根据城市id和订单号查询邮费
		String postage=goodsDao.queryPostageByGoodsIds(cityId, map.get("orderNo").toString());
		map.put("price",Util.add(map.get("price").toString(), postage));
		return Util.resultMap(configCode.code_1001, map);
	}

	/**
	 * 根据订单号查询供应商信息
	 */
	@Override
	public List<Map<String, Object>> querySupplierByOrderNo(String orderNo) {
		return goodsOrderDao.querySupplierByOrderNo(orderNo);
	}

	/**
	 * 生成订单号
	 */
	@Override
	public String orderNo() {
		return goodsOrderDao.orderNo();
	}

	/**
	 * 查询服务订单
	 */
	@Override
	public Map<String, Object> queryOrderForService(Order order, Integer page,
			Integer row) {
		//查询服务订单
		List<Map<String, Object>> list = goodsOrderDao.queryOrderForService(
				order, page, row);
		//判断list是否为空
		if (null == list) {
			return Util.resultMap(configCode.code_1015, list);
		}
		return Util.resultMap(configCode.code_1001, list);
	}

	/**
	 * 查询服务订单详情
	 */
	@Override
	public Map<String, Object> queryOrderForServiceById(
			GoodsOrderDetail goodsOrderDetail, String memberId) {
		//查询服务订单详情
		Map<String, Object> map = goodsOrderDao.queryOrderForServiceById(
				goodsOrderDetail, memberId);
		//判断orderNo是否为空
		if (null == map || StringUtil.isEmpty(map.get("orderNo"))) {
			return Util.resultMap(configCode.code_1071, map);
		}
		return Util.resultMap(configCode.code_1001, map);
	}
}
