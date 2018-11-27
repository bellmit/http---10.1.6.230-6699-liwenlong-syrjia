package cn.syrjia.hospital.service.impl;

import cn.syrjia.callCenter.util.CallCenterConfig;
import cn.syrjia.callCenter.util.SendCallCenterUtil;
import cn.syrjia.common.impl.BaseServiceImpl;
import cn.syrjia.config.configCode;
import cn.syrjia.dao.ImDao;
import cn.syrjia.dao.OrderDao;
import cn.syrjia.entity.Evaluate;
import cn.syrjia.entity.Order;
import cn.syrjia.entity.copySquare;
import cn.syrjia.entity.Piclib;
import cn.syrjia.entity.vo.GoodsOrderDetail;
import cn.syrjia.hospital.dao.AppDoctorDao;
import cn.syrjia.hospital.dao.DoctorDao;
import cn.syrjia.hospital.dao.DoctorOrderDao;
import cn.syrjia.hospital.entity.OrderDetailServer;
import cn.syrjia.hospital.entity.OrderSymptom;
import cn.syrjia.hospital.entity.PhotoMedicalRecord;
import cn.syrjia.hospital.service.AppDoctorService;
import cn.syrjia.hospital.service.DoctorOrderService;
import cn.syrjia.service.ImService;
import cn.syrjia.service.OrderService;
import cn.syrjia.service.PushService;
import cn.syrjia.util.*;
import cn.syrjia.util.qcloudsms.QCloudSmsUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

@Service("doctorOrderService")
public class DoctorOrderServiceImpl extends BaseServiceImpl implements
		DoctorOrderService {

	@Resource(name = "doctorOrderDao")
	DoctorOrderDao doctorOrderDao;

	@Resource(name = "appDoctorDao")
	AppDoctorDao appDoctorDao;

	@Resource(name = "doctorDao")
	DoctorDao doctorDao;

	@Resource(name = "orderDao")
	OrderDao orderDao;

	@Resource(name = "pushService")
	PushService pushService;

	@Resource(name = "imService")
	ImService imService;

	@Resource(name = "imDao")
	ImDao imDao;

	@Resource(name = "orderService")
	OrderService orderService;

	@Resource(name = "appDoctorService")
	AppDoctorService appDoctorService;

	@Override
	public Map<String, Object> queryConfirmDocServerData(
			HttpServletRequest request, Integer orderType, String doctorId,
			String memberId) {
		if (StringUtil.isEmpty(memberId)) {
			memberId = GetOpenId.getMemberId(request);
		}
		Map<String, Object> map = doctorOrderDao.queryConfirmDocServerData(
				orderType, doctorId, memberId);
		if (null == map || map.size() == 0) {
			return Util.resultMap(configCode.code_1032, null);
		} else {
			return Util.resultMap(configCode.code_1001, map);
		}
	}

	@Override
	public Map<String, Object> checkNoFinishOrderByPatientId(String patientId,
			String doctorId, Integer orderType) {
		Integer count = 0;
		if (orderType != 6 && orderType != 8) {
			count = doctorOrderDao.checkNoFinishOrderByPatientId(patientId,
					doctorId, orderType);
		}
		return Util.resultMap(configCode.code_1001, count);
	}

	@Override
	public Map<String, Object> addServerOrder(HttpServletRequest request,
			String doctorId, Order order) {
		if (StringUtil.isEmpty(order.getMemberId())) {
			String memberId = GetOpenId.getMemberId(request);

			order.setMemberId(memberId);
		}
		if (StringUtil.isEmpty(order.getPatientId())
				|| StringUtil.isEmpty(doctorId) || order.getOrderType() == null
				|| StringUtil.isEmpty(order.getMemberId())) {
			return Util.resultMap(configCode.code_1029, null);
		} else {
			String orderNo = "";
			String oldOrderNo = order.getOrderNo();
			if (StringUtil.isEmpty(order.getOrderNo())) {
				orderNo = doctorOrderDao.orderNo();
			} else {
				orderNo = order.getOrderNo();
			}
			Integer paystatus = 1;
			try {
				Map<String, Object> docMap = doctorDao.queryDocotrById(
						doctorId, order.getMemberId());
				if (docMap == null) {
					return Util.resultMap(configCode.code_1032, null);
				}
				if (order.getOrderType() == 4 || order.getOrderType() == 5
						|| order.getOrderType() == 6) {
					orderNo = "TW-" + orderNo;
				} else {
					orderNo = "PHONE-" + orderNo;
				}
				Integer zxCount = 0;
				Integer zqZxCount = 0;
				Integer zhZxCount = 0;
				Integer zxTime = 15;
				Integer talkLimitTime = null;
				Double orderPrice = order.getReceiptsPrice();
				if (order.getOrderType() == 4 || order.getOrderType() == 5) {
					if (Integer.valueOf(docMap.get("isOnlineTwGh").toString()) == 0) {
						return Util.resultMap(configCode.code_1057, null);
					} else {
						if (StringUtil.isEmpty(orderPrice)) {
							orderPrice = Double.valueOf(docMap.get(
									"fisrtTwGhMoney").toString());
						}
					}
				} else if (order.getOrderType() == 6) {
					if (Integer.valueOf(docMap.get("isOnlineTwZx").toString()) == 0) {
						return Util.resultMap(configCode.code_1054, null);
					} else {
						orderPrice = Double.valueOf(docMap.get("twZxMoney")
								.toString());
						zxCount = StringUtil.isEmpty(docMap.get("twZxCount")) ? null
								: Integer.valueOf(docMap.get("twZxCount")
										.toString());
						zqZxCount = StringUtil.isEmpty(docMap
								.get("twZqZxCount")) ? null : Integer
								.valueOf(docMap.get("twZqZxCount").toString());
						zhZxCount = StringUtil.isEmpty(docMap
								.get("twZhZxCount")) ? null : Integer
								.valueOf(docMap.get("twZhZxCount").toString());
						zxTime = StringUtil.isEmpty(docMap.get("twZxTime")) ? null
								: Integer.valueOf(docMap.get("twZxTime")
										.toString());
					}
				} else if (order.getOrderType() == 7
						|| order.getOrderType() == 9) {
					if (Integer.valueOf(docMap.get("isOnlinePhoneGh")
							.toString()) == 0) {
						return Util.resultMap(configCode.code_1055, null);
					} else {
						talkLimitTime = Integer.valueOf(docMap.get(
								"phoneGhServerTime").toString()) * 60;
						orderPrice = Double.valueOf(docMap.get(
								"fisrtPhoneGhMoney").toString());
					}
				} else if (order.getOrderType() == 8) {
					if (Integer.valueOf(docMap.get("isOnlinePhoneZx")
							.toString()) == 0) {
						return Util.resultMap(configCode.code_1056, null);
					} else {
						orderPrice = Double.valueOf(docMap.get("phoneZxMoney")
								.toString());
						talkLimitTime = Integer.valueOf(docMap.get(
								"phoneZxTime").toString()) * 60;
					}
				}
				order.setOrderNo(orderNo);
				order.setCreateTime(Util.queryNowTime());
				order.setState(1);
				order.setDoctorId(doctorId);
				order.setOrderPrice(orderPrice);
				order.setReceiptsPrice(orderPrice);
				if (orderPrice == 0.0) {
					order.setPaymentStatus(2);
					order.setPayTime(Util.queryNowTime());
				} else {
					order.setPaymentStatus(1);
				}
				order.setGoodsPrice(orderPrice);
				order.setOrderWay(1);
				Object id = doctorOrderDao.addEntity(order);
				if (id == null) {
					// 设置事务回滚
					TransactionAspectSupport.currentTransactionStatus()
							.setRollbackOnly();
					return Util.resultMap(configCode.code_1058, null);
				} else {
					OrderDetailServer serverDetail = new OrderDetailServer();
					serverDetail.setBuyNum(1);
					serverDetail.setCreateTime(Util.queryNowTime());
					serverDetail.setDoctorId(doctorId);
					serverDetail.setDoctorPrice(orderPrice);
					serverDetail.setOrderNo(orderNo);
					serverDetail.setPayPrice(orderPrice);
					serverDetail.setZxCount(zxCount);
					serverDetail.setZhZxCount(zhZxCount);
					serverDetail.setZqZxCount(zqZxCount);
					serverDetail.setZxTime(zxTime);
					serverDetail.setSyZxCount(zxCount);
					serverDetail.setTalkLimitTime(talkLimitTime);
					if (order.getPaymentStatus() == 2
							&& (order.getOrderType() == 4
									|| order.getOrderType() == 6
									|| order.getOrderType() == 7 || order
									.getOrderType() == 8)) {
						serverDetail.setValidityTime(Util.queryNowTime()
								+ (48 * 60 * 60));
						if (order.getOrderType() == 4
								|| order.getOrderType() == 7
								|| order.getOrderType() == 8) {
							// QuartzManager.addJob("serverOver_"+order.getOrderNo(),new
							// Date(serverDetail.getValidityTime()*1000L),order.getOrderNo());
						}
					}
					Object detialId = doctorOrderDao
							.addEntityUUID(serverDetail);
					if (detialId == null) {
						// 设置事务回滚
						TransactionAspectSupport.currentTransactionStatus()
								.setRollbackOnly();
						return Util.resultMap(configCode.code_1058, null);
					}
					if (!StringUtil.isEmpty(doctorId)
							&& !StringUtil.isEmpty(order.getPatientId())
							&& order.getPaymentStatus() == 2
							&& !StringUtil.isEmpty(order.getMemberId())) {
						Integer count = orderDao
								.queryDoctorPatientRelationship(doctorId,
										order.getPatientId());
						if (count <= 0) {
							orderDao.insertDoctorPatientRelationship(doctorId,
									order.getMemberId(), order.getPatientId());
							try {
								Map<String, Object> map = new HashMap<String, Object>();
								map.put("id", Util.getUUID());
								map.put("doctorId", doctorId);
								map.put("memberId", order.getMemberId());
								map.put("patientId", order.getPatientId());
								String result = SendCallCenterUtil
										.sendCallCenterData(
												map,
												CallCenterConfig.PatientRelation);
								System.out.println(result);
							} catch (Exception e) {
								System.out.println(e);
							}
						}

						Integer keep = appDoctorDao.queryKeep(
								order.getMemberId(), doctorId);
						if (keep == 0) {
							appDoctorDao.addKeep(order.getMemberId(), doctorId);
						}

						orderDao.updateCFOrderNo(order.getPatientId(),
								order.getDoctorId(), order.getOrderNo());

						Producer.producer(order.getOrderNo());// 结算分成
					}
					Map<String, Object> map = orderDao
							.querySendCallCenterData(order.getOrderNo());
					if (map != null) {
						String result = SendCallCenterUtil.sendCallCenterData(
								map, CallCenterConfig.Order);
						System.out.println(result);
					}
					try {
						buyServerPush(request, oldOrderNo, order, doctorId);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				paystatus = order.getPaymentStatus();
			} catch (NumberFormatException e) {
				// 设置事务回滚
				TransactionAspectSupport.currentTransactionStatus()
						.setRollbackOnly();
				return Util.resultMap(configCode.code_1058, null);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("orderNo", orderNo);
			map.put("paystatus", paystatus);
			map.put("doctorId", doctorId);
			map.put("patientId", order.getPatientId());
			return Util.resultMap(configCode.code_1001, map);
		}
	}

	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\order.txt");
		BufferedReader reader = null;
		String temp = null;
		int line = 1;
		try {
			reader = new BufferedReader(new FileReader(file));
			while ((temp = reader.readLine()) != null) {
				Producer.producer(temp);// 结算分成
				line++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	@Transactional
	public Map<String, Object> addOrderSymptom(HttpServletRequest request,
			final OrderSymptom orderSymptom) {
		if (StringUtil.isEmpty(orderSymptom.getOrderNo())) {
			return Util.resultMap(configCode.code_1029, null);
		} else {
			final Map<String, Object> map = doctorOrderDao
					.queryOrderSymptomByOrderNo(request,
							orderSymptom.getOrderNo());
			Integer count = doctorOrderDao.querySypCountByOrderNo(orderSymptom
					.getOrderNo());
			if (map == null) {
				return Util.resultMap(configCode.code_1034, null);
			} else {
				if (StringUtil.isEmpty(map.get("symptomDescribe"))
						&& count <= 0) {
					// && 2 == Integer.valueOf(map.get("paymentStatus")
					// .toString())) {
					orderSymptom.setCreateTime(Util.queryNowTime());
					orderSymptom.setPatientId(map.get("orderPatientId")
							.toString());
					orderSymptom.setState(1);
					Object id = doctorOrderDao.addEntityUUID(orderSymptom);
					if (StringUtil.isEmpty(id)) {
						return Util.resultMap(configCode.code_1014, null);
					} else {
						JSONObject json = new JSONObject();
						json.put("msgType", 7);
						json.put("content", 3);
						json.put("serverOrderNo", orderSymptom.getOrderNo());
						json.put("orderNo", orderSymptom.getOrderNo());
						try {
							GetSig.sendMsg(request,
									orderSymptom.getPatientId(),
									map.get("doctorId").toString(), 1, json);
						} catch (Exception e) {
							System.out.println(e);
						}

						pushService.docfinishzzms(map.get("doctorId")
								.toString(), orderSymptom.getPatientId(), map
								.get("patientName").toString());

						Thread t = new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									Thread.sleep(1000);
									String testId = appDoctorService
											.queryDefultSpecial(
													map.get("doctorId")
															.toString(), 1);

									if (!StringUtil.isEmpty(testId)) {
										Map<String, Object> spect = imService
												.queryLastOrderNo(orderSymptom
														.getPatientId(), map
														.get("doctorId")
														.toString(), testId);
										if (spect.get("respCode").toString()
												.equals("1001")) {
											JSONObject json = new JSONObject();
											json.put("msgType", 8);
											json.put("orderNo",
													orderSymptom.getOrderNo());
											json.put(
													"dataId",
													JsonUtil.jsonToMap(
															spect.get("data"))
															.get("testId"));
											json.put("serverOrderNo",
													orderSymptom.getOrderNo());
											GetSig.sendMsg(
													null,
													map.get("doctorId")
															.toString(),
													orderSymptom.getPatientId(),
													0, json);
										}
									}
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						});
						t.start();
						return Util.resultMap(configCode.code_1001, id);
					}
				} else {
					return Util.resultMap(configCode.code_1086, null);
				}
			}
		}
	}

	@Override
	public Map<String, Object> queryOrderSymptomByOrderNo(
			HttpServletRequest request, String orderNo) {
		if (StringUtil.isEmpty(orderNo)) {
			return Util.resultMap(configCode.code_1029, null);
		} else {
			Map<String, Object> map = doctorOrderDao
					.queryOrderSymptomByOrderNo(request, orderNo);
			return Util.resultMap(configCode.code_1001, map);
		}
	}

	@Override
	public Map<String, Object> checkPhotoMedicalCount(
			HttpServletRequest request, String memberId) {
		if (StringUtil.isEmpty(memberId)) {
			memberId = GetOpenId.getMemberId(request);

		}
		if (StringUtil.isEmpty(memberId)) {
			return Util.resultMap(configCode.code_1029, null);
		} else {
			Integer count = doctorOrderDao.checkPhotoMedicalCount(memberId);
			if (count > 0) {
				return Util.resultMap(configCode.code_1001, count);
			} else {
				return Util.resultMap(configCode.code_1065, null);
			}
		}
	}

	@Override
	public Map<String, Object> addPhotoMedical(HttpServletRequest request,
			PhotoMedicalRecord photoMedical, String imgUrls) {
		if (StringUtil.isEmpty(photoMedical.getMemberId())) {
			String memberId = GetOpenId.getMemberId(request);

			photoMedical.setMemberId(memberId);
		}
		if (StringUtil.isEmpty(photoMedical.getMemberId())
				|| StringUtil.isEmpty(imgUrls)) {
			return Util.resultMap(configCode.code_1029, null);
		} else {
			Integer count = doctorOrderDao.checkPhotoMedicalCount(photoMedical
					.getMemberId());
			if (count > 0) {
				return Util.resultMap(configCode.code_1065, null);
			} else {
				photoMedical.setCreateTime(Util.queryNowTime());
				photoMedical.setState(-1);
				photoMedical.setImgUrl(imgUrls);
				Object id = doctorOrderDao.addEntityUUID(photoMedical);
				if (id != null) {
					// 目前暂定一张先按一张图片处理
					Piclib pic = new Piclib();
					pic.setGoodId(id.toString());
					pic.setPicPathUrl(imgUrls);
					pic.setStatus("10");
					pic.setStatusDate(Util.queryNowTime());
					doctorOrderDao.addEntityUUID(pic);
					return Util.resultMap(configCode.code_1001, id);
				} else {
					return Util.resultMap(configCode.code_1014, null);
				}
			}
		}
	}

	@Override
	public Map<String, Object> queryDoctorOrderDetail(
			HttpServletRequest request, String orderNo, String memberId) {
		if (StringUtil.isEmpty(memberId)) {
			memberId = GetOpenId.getMemberId(request);

		}
		if (StringUtil.isEmpty(memberId) || StringUtil.isEmpty(orderNo)) {
			return Util.resultMap(configCode.code_1029, null);
		} else {
			Map<String, Object> map = doctorOrderDao.queryDoctorOrderDetail(
					orderNo, memberId);
			if (map == null) {
				return Util.resultMap(configCode.code_1034, null);
			} else {
				return Util.resultMap(configCode.code_1001, map);
			}
		}
	}

	@Override
	public Map<String, Object> queryMyEvaBanners(HttpServletRequest request,
			String memberId) {
		if (StringUtil.isEmpty(memberId)) {
			memberId = GetOpenId.getMemberId(request);

		}
		if (StringUtil.isEmpty(memberId)) {
			return Util.resultMap(configCode.code_1029, null);
		} else {
			List<Map<String, Object>> list = doctorOrderDao
					.queryMyEvaBanners(memberId);
			return Util.resultMap(configCode.code_1001, list);
		}
	}

	@Override
	public Map<String, Object> addJqOrder(HttpServletRequest request,
			String paramarr, String memberId) {
		if (StringUtil.isEmpty(memberId)) {
			memberId = GetOpenId.getMemberId(request);

		}
		if (StringUtil.isEmpty(memberId)) {
			return Util.resultMap(configCode.code_1029, null);
		} else {
			List<Map<String, Object>> list = JsonUtil.parseJSON2List(paramarr);
			String orderNo = "JQ_" + doctorOrderDao.orderNo();
			Double orderPrice = 0.0;
			Object id = null;
			for (int i = 0; i < list.size(); i++) {
				if (!StringUtil.isEmpty(list.get(i).get("id"))) {
					Map<String, Object> map = doctorOrderDao
							.queryEvaBannerById(list.get(i).get("id")
									.toString());
					if (map != null) {
						orderPrice += Math.round(Double.valueOf(map
								.get("price").toString())
								* Integer.valueOf(list.get(i).get("buyCount")
										.toString()) * 100) / 100;
						GoodsOrderDetail goodsOrderDetail = new GoodsOrderDetail();
						goodsOrderDetail.setId(Util.getUUID());
						goodsOrderDetail.setCreateTime(Util.queryNowTime());
						goodsOrderDetail.setGoodsId(map.get("id").toString());
						goodsOrderDetail.setGoodsNum(Integer.valueOf(list
								.get(i).get("buyCount").toString()));
						goodsOrderDetail.setGoodsPrice(Double.valueOf(map.get(
								"price").toString()));
						goodsOrderDetail.setGoodsOriginalPrice(Double
								.valueOf(map.get("price").toString()));
						goodsOrderDetail.setGoodsTotal(Double.valueOf(map.get(
								"price").toString()));
						goodsOrderDetail.setOrderNo(orderNo);
						goodsOrderDetail.setPaymentStatus(1);
						id = doctorOrderDao.addEntity(goodsOrderDetail);
						if (id == null) {
							TransactionAspectSupport.currentTransactionStatus()
									.setRollbackOnly();
							return Util
									.resultMap(configCode.code_1033, orderNo);
						}
					}
				}
			}
			if (orderPrice != 0.0) {
				Order order = new Order();
				order.setOrderNo(orderNo);
				order.setCreateTime(Util.queryNowTime());
				order.setState(1);
				order.setOrderPrice(orderPrice);
				order.setReceiptsPrice(orderPrice);
				order.setPaymentStatus(1);
				order.setGoodsPrice(orderPrice);
				order.setMemberId(memberId);
				order.setOrderType(12);
				id = doctorOrderDao.addEntity(order);
				if (id == null) {
					TransactionAspectSupport.currentTransactionStatus()
							.setRollbackOnly();
					return Util.resultMap(configCode.code_1033, orderNo);
				}
			}
			return Util.resultMap(configCode.code_1001, orderNo);
		}
	}

	@Override
	public Map<String, Object> addEvaluate(HttpServletRequest request,
			Evaluate evaluate, String labels, String jqArr,
			String myEvaBannersOrders) {
		if (StringUtil.isEmpty(evaluate.getMemberId())) {
			String memberId = GetOpenId.getMemberId(request);
			evaluate.setMemberId(memberId);
		}
		if (StringUtil.isEmpty(evaluate.getMemberId())
				|| StringUtil.isEmpty(evaluate.getGoodsId())) {
			return Util.resultMap(configCode.code_1029, null);
		}
		Integer count = doctorOrderDao.queryEvaByOrderNo(evaluate.getOrderNo());

		if (count > 0) {
			return Util.resultMap(configCode.code_1081, null);
		}
		Object obj = null;
		try {
			Map<String, Object> sysMap = doctorOrderDao.getSysSet();
			Integer isAutoDoctorEva = sysMap == null
					|| StringUtil.isEmpty(sysMap.get("isAutoDoctorEva")) ? 0
					: Integer.valueOf(sysMap.get("isAutoDoctorEva").toString());
			if (isAutoDoctorEva == 1) {
				if (!StringUtil.isEmpty(jqArr)) {
					pushService.docreceivepj(evaluate.getGoodsId(),
							evaluate.getOrderNo());
				}
				evaluate.setState(1);
			} else {
				evaluate.setState(2);
			}
			evaluate.setType(2);
			evaluate.setCreateTime(Util.queryNowTime());
			obj = doctorOrderDao.addEntityUUID(evaluate);
			if (obj == null) {
				TransactionAspectSupport.currentTransactionStatus()
						.setRollbackOnly();
				return Util.resultMap(configCode.code_1087, null);
			} else {
				if (!StringUtil.isEmpty(labels)) {
					JSONArray checkLabels = JSONArray.fromString(labels);
					doctorOrderDao.insertEvaLables(checkLabels, obj.toString());
				}
				List<Map<String, Object>> jqOrderNolist = null;
				if (!StringUtil.isEmpty(jqArr)) {
					jqOrderNolist = appDoctorDao.queryEvaBannerOrders(evaluate
							.getMemberId());
					List<Map<String, Object>> jqlist = JsonUtil
							.parseJSON2List(jqArr);
					for (Map<String, Object> map : jqlist) {
						doctorOrderDao.updateMyEvaBanState(evaluate
								.getMemberId(), obj.toString(), map.get("id")
								.toString(), Integer.valueOf(map.get("count")
								.toString()));
					}
				}
				if (jqOrderNolist != null && jqOrderNolist.size() > 0) {
					// 区分 红旗订单 如果是红旗订单 则修改patientId
					Map<String, Object> order = orderDao
							.queryOrderByOrderNo(evaluate.getOrderNo());
					String patientId = "";
					if (null != order) {
						patientId = (String) order.get("patientId");
					}

					doctorOrderDao.updateJqOrderDoctorIdPatientId(
							jqOrderNolist, evaluate.getGoodsId(), patientId);

					for (Map<String, Object> orderItem : jqOrderNolist) {
						Producer.producer(orderItem.get("orderNo").toString()); // 结算分成
					}
				}
			}
		} catch (Exception e) {
			return Util.resultMap(configCode.code_1087, obj);
		}
		return Util.resultMap(configCode.code_1001, obj);
	}

	@Override
	public List<Map<String, Object>> queryNowInquiry(String memberId,
			Integer page, Integer row) {
		List<Map<String, Object>> list = doctorOrderDao.queryNowInquiry(
				memberId, page, row);
		if (null != list) {
			// for(Map<String,Object> map:list){
			// Map<String,Object>
			// last=imDao.queryLastMsg(map.get("doctorId").toString(),
			// map.get("patientId").toString());
			// if(null!=last) {
			// map.put("lastMsg",Util.getValue(last,"lastMsg",""));
			// map.put("lastMsgId",Util.getValue(last,"id",""));
			// map.put("lastTime",Util.getValue(last,"lastTime",""));
			// map.put("lastPeople",Util.getValue(last,"lastPeople",""));
			// map.put("lastMsgTime",Util.getValue(last,"msgTime",""));
			// }else {
			// map.put("lastMsgTime",0);
			// }
			// }

			StringBuffer sbDoctorId = new StringBuffer();
			StringBuffer sbPatientId = new StringBuffer();
			for (Map<String, Object> map : list) {
				sbDoctorId.append("'").append(map.get("doctorId")).append("',");
				sbPatientId.append("'").append(map.get("patientId"))
						.append("',");
			}
			if (sbDoctorId.lastIndexOf(",") == sbDoctorId.length() - 1) {
				sbDoctorId.deleteCharAt(sbDoctorId.length() - 1);
			}
			if (sbPatientId.lastIndexOf(",") == sbPatientId.length() - 1) {
				sbPatientId.deleteCharAt(sbPatientId.length() - 1);
			}
			List<Map<String, Object>> listMap = this.imDao.queryLastMsgList(
					sbDoctorId.toString(), sbPatientId.toString());
			for (Map<String, Object> map : list) {
				String doctorIdStr = (String) map.get("doctorId");
				String patientIdStr = (String) map.get("patientId");
				for (Map<String, Object> map1 : listMap) {
					String fromAccountStr = (String) map1.get("fromAccount");
					String toAccountStr = (String) map1.get("toAccount");
					if ((doctorIdStr.equals(fromAccountStr) && patientIdStr
							.equals(toAccountStr))
							|| (doctorIdStr.equals(toAccountStr) && patientIdStr
									.equals(fromAccountStr))) {

						map.put("lastMsg", Util.getValue(map1, "lastMsg", ""));
						map.put("lastMsgId", Util.getValue(map1, "id", ""));
						map.put("lastTime", Util.getValue(map1, "lastTime", ""));
						map.put("lastPeople",
								Util.getValue(map1, "lastPeople", ""));
						map.put("lastMsgTime",
								Util.getValue(map1, "msgTime", ""));
						break;
					}
				}
				if (null == map.get("lastMsgTime")) {
					map.put("lastMsgTime", 0);
				}
			}
		}
		Collections.sort(list, new Comparator<Map<String, Object>>() {
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				int map1value = (Integer) o1.get("lastMsgTime");
				int map2value = (Integer) o2.get("lastMsgTime");
				return map2value - map1value;
			}
		});
		return list;
	}

	@Override
	public Map<String, Object> addCfOrder(HttpServletRequest request,copySquare copySquare,
			String doctorId, String patientId) {
		String memberId = GetOpenId.getMemberId(request);//"oo_4L1a-ldB9n3YwoJfpgUcIqrp0";
		if (StringUtil.isEmpty(patientId) || StringUtil.isEmpty(doctorId)
				|| StringUtil.isEmpty(memberId)) {
			return Util.resultMap(configCode.code_1029, null);
		}
		String wzOrderNo = null;
		// Map<String, Object> lastServerMap = null;
		try {
			Map<String, Object> doctor = appDoctorDao.queryDocById(doctorId);
			if (doctor == null) {
				return Util.resultMap(configCode.code_1032, null);
			}
			if ("-1".equals(doctor.get("docIsOn"))) {
				return Util.resultMap(configCode.code_1100, null);
			}
			Integer orderType = 22;
			Integer isHasCfCount = doctorOrderDao.queryCfOrderByIds(doctorId,
					patientId);//查询有没有未完成的抄方订单
			if (isHasCfCount > 0) {
				return Util.resultMap(configCode.code_1127, null);
			}
			/*
			 * if (!StringUtil.isEmpty(patientId)) { lastServerMap =
			 * appDoctorDao.queryLastOrderNo(patientId, doctorId); if
			 * (lastServerMap != null) { memberId =
			 * lastServerMap.get("memberId").toString(); wzOrderNo = StringUtil
			 * .isEmpty(lastServerMap.get("orderNo")) ? null :
			 * lastServerMap.get("orderNo").toString(); } else { return
			 * Util.resultMap(configCode.code_1017, null); } }
			 */

			if (StringUtil.isEmpty(wzOrderNo)) {
				Object id = null;
				wzOrderNo = "CF-" + appDoctorDao.getOrderNo();
				System.out.println("wzOrderNo=" + wzOrderNo);
				id = addOrder(orderDao, wzOrderNo, null, 0.0, memberId,
						patientId, orderType, null, null, doctorId, null, 2, 0,
						true);
				System.out.println("添加问诊单4:" + wzOrderNo);
				if (id == null) {
					// 设置事务回滚
					TransactionAspectSupport.currentTransactionStatus()
							.setRollbackOnly();
					return Util.resultMap(configCode.code_1058, null);
				} else {
					OrderDetailServer serverDetail = new OrderDetailServer();
					serverDetail.setBuyNum(1);
					serverDetail.setDoctorId(doctorId);
					serverDetail.setDoctorPrice(0.0);
					serverDetail.setPayPrice(0.0);
					serverDetail.setValidityTime(Util.queryNowTime()
							+ (48 * 60 * 60));
					Object detialId = null;
					serverDetail.setCreateTime(Util.queryNowTime());
					serverDetail.setOrderNo(wzOrderNo);
					detialId = appDoctorDao.addEntityUUID(serverDetail);
					if (detialId == null) {
						// 设置事务回滚
						TransactionAspectSupport.currentTransactionStatus()
								.setRollbackOnly();
						return Util.resultMap(configCode.code_1058, null);
					}

					// QuartzManager.addJob("serverOver_"+wzOrderNo,new
					// Date(serverDetail.getValidityTime()*1000L),wzOrderNo);
				}
			}
			Integer count = orderDao.queryDoctorPatientRelationship(doctorId,
					patientId);
			if (count <= 0) {
				orderDao.insertDoctorPatientRelationship(doctorId, memberId,
						patientId);
			}

			Integer keep = appDoctorDao.queryKeep(memberId, doctorId);
			if (keep == 0) {
				appDoctorDao.addKeep(memberId, doctorId);
			}
			//新建表存储新版抄方页面页面数据，图片，剂型、付数、备注
			
			//新建表 主键ID（UUID），抄方订单号（varchar 32），剂型（int）,付数（int）,备注（varchar 300）,创建时间（） 

           /* String img1 =    copySquare.getImg1()!=null?copySquare.getImg1().toString():"";
            String img2 =    copySquare.getImg2()!=null?copySquare.getImg2().toString():"";
            String img3 =   copySquare.getImg3()!=null?copySquare.getImg3().toString():"";
			String snote =  copySquare.getSnote()!=null?copySquare.getSnote().toString():"";
			int agentType = copySquare.getAgentType();
			int anagraphCount = copySquare.getAnagraphCount();*/
			int i = 0;
            /*if(!StringUtil.isEmpty(wzOrderNo)&&!StringUtil.isEmpty(agentType)&&(!StringUtil.isEmpty(img1)||!StringUtil.isEmpty(img2)||!StringUtil.isEmpty(img3))&&anagraphCount!=0&&agentType!=0&&snote.length()<=300){
				doctorOrderDao.addCopySquare( wzOrderNo,img1, img2, img3, snote , agentType, anagraphCount,patientId);
             }else{
            	 return Util.resultMap(configCode.code_1058, null);
             }*/
			//分割线----------------
			if (i==0){
			try {
				pushService.docCf(doctorId, patientId);
				if (!StringUtil.isEmpty(doctor.get("docPhone"))) {
					Map<String, Object> patient = appDoctorDao
							.queryPatientById(patientId);
					if (patient != null) {
						String[] params = { patient.get("name").toString() };
						QCloudSmsUtil.sendSmsByTemId(doctor.get("docPhone")
								.toString(), SMSTemplateIdUtil.cfServer_SMS,
								params);
					}
				}
				JSONObject data = new JSONObject();
				data.put("patientId", patientId);
				data.put("serverOrderNo", wzOrderNo);
				data.put("content", "您收到新的抄方订单，请尽快进行处理");
				data.put("msgType", 30);//
				//data.put("agentType", agentType);//剂型
				//data.put("anagraphCount", anagraphCount);//付数
				 /*if (StringUtil.isEmpty(snote)){
					 data.put("snote", "无");//备注：无
				 }
				 else{
					 if (snote.length()>7){
						 data.put("snote", snote.toString().substring(0,7)+"...");//备注：少于7个字
						 }
					 
					 else{ 
						 data.put("snote", snote);//备注：少于7个字
					 }
					 }*/
				 
				Map<String, Object> result = GetSig.sendMsg(null, patientId,
						doctorId, 1, data);
				if (result != null
						&& result.get("respCode").toString().equals("1001")) {
					orderDao.insertImSendRecord(wzOrderNo, doctorId, patientId,
							1, 30, 0 + "", "sendMsg");
				} else {
					result = GetSig.sendMsg(null, patientId, doctorId, 1, data);
					if (result != null
							&& !result.get("respCode").toString()
									.equals("1001")) {
						orderDao.insertImSendRecord(wzOrderNo, doctorId,
								patientId, 0, 30, 0 + "", "sendMsg");
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("msgtype", "text");
						params.put("toUser", "o92LXv1ZIHTjleF3MYPFlAiV7Bas");
						params.put("url", "");
						params.put("content", "发送IM抄方消息失败，方法名称GetSig.sendMsg");
						sendModelMsgUtil.sendCustomMsg(params, orderDao);
					}
				}
			} catch (Exception e) {
				System.out.println(e);
			}
			}
			else{
				// 设置事务回滚
				TransactionAspectSupport.currentTransactionStatus()
						.setRollbackOnly();
				return Util.resultMap(configCode.code_1058, null);
			}

		} catch (NumberFormatException e) {
			TransactionAspectSupport.currentTransactionStatus()
					.setRollbackOnly();
			return Util.resultMap(configCode.code_1015, null);
		}

		return Util.resultMap(configCode.code_1001, wzOrderNo);
	}
	@Override
	public Map<String, Object> lookCfOrder(HttpServletRequest request,String orderNo){

		return Util.resultMap(configCode.code_1001,doctorOrderDao.lookCfOrder(orderNo));
	}
	@Override
	public Map<String, Object> checkCfOrderCount(String doctorId,String patientId){
		Integer count = doctorOrderDao.queryCfOrderByIds( doctorId, patientId);
        if (count>0){
        	return Util.resultMap(configCode.code_1127, null);
        }else{
        	return Util.resultMap(configCode.code_1001,null);
        }
		
	}

	private Object addOrder(OrderDao orderDao, String orderNo,
			String mainOrderNo, Double orderPrice, String memberId,
			String patientId, Integer orderType, String sourceOrderNo,
			String recordId, String doctorId, Double freight,
			Integer payStatus, Integer orderStatus, boolean isAdd) {
		Order order = new Order();
		order.setOrderNo(orderNo);
		order.setOrderPrice(orderPrice);
		order.setCreateTime(Util.queryNowTime());
		order.setState(1);
		order.setMainOrderNo(mainOrderNo);
		order.setMemberId(memberId);
		order.setPatientId(patientId);
		order.setDoctorId(doctorId);
		order.setOrderType(orderType);
		order.setReceiptsPrice(orderPrice);
		order.setPaymentStatus(payStatus);
		order.setOrderStatus(orderStatus);
		order.setGoodsPrice(orderPrice);
		order.setRecordId(recordId);
		order.setFreight(freight);
		order.setOrderWay(1);
		order.setSourceOrderNo(sourceOrderNo);
		if (payStatus == 2) {
			order.setPayTime(Util.queryNowTime());
		} else if (payStatus == 5) {
			order.setEndTime(Util.queryNowTime());
		}
		if (!isAdd) {
			return orderDao.updateEntity(order);
		}
		return orderDao.addEntity(order);
	}

	private void buyServerPush(HttpServletRequest request, String oldOrderNo,
			Order order, String doctorId) {
		if (!StringUtil.isEmpty(oldOrderNo)) {
			JSONObject json = new JSONObject();
			json.put("msgType", 15);
			json.put("orderNo", order.getOrderNo());
			json.put("content", order.getReceiptsPrice());
			Map<String, Object> result = GetSig.sendMsg(request,
					order.getDoctorId(), order.getPatientId(), 0, json);
			if (result != null
					&& result.get("respCode").toString().equals("1001")) {
				orderDao.insertImSendRecord(order.getOrderNo(), doctorId,
						order.getPatientId(), 1, 15, order.getReceiptsPrice()
								+ "", "sendMsg");
			} else {
				result = GetSig.sendMsg(request, order.getDoctorId(),
						order.getPatientId(), 0, json);
				if (result != null
						&& !result.get("respCode").toString().equals("1001")) {
					orderDao.insertImSendRecord(order.getOrderNo(), doctorId,
							order.getPatientId(), 0, 15,
							order.getReceiptsPrice() + "", "sendMsg");
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("msgtype", "text");
					params.put("toUser", "o92LXv1ZIHTjleF3MYPFlAiV7Bas");
					params.put("url", "");
					params.put("content", "发送IM消息失败，方法名称GetSig.sendMsg");
					sendModelMsgUtil.sendCustomMsg(params, orderDao);
				}
			}
		}

		if (!StringUtil.isEmpty(doctorId)
				&& !StringUtil.isEmpty(order.getPatientId())
				&& order.getPaymentStatus() == 2
				&& !StringUtil.isEmpty(order.getMemberId())) {
			Map<String, Object> patientData = doctorDao.queryPatientsById(order
					.getPatientId());

			if (order.getOrderType() == 4) {
				if (!StringUtil.isEmpty(order.getRsrvStr1())
						&& order.getRsrvStr1().equals("1")) {
					JSONObject json = new JSONObject();
					json.put("msgType", 16);
					json.put("orderNo", order.getOrderNo());
					json.put("content", order.getReceiptsPrice());
					Map<String, Object> result = GetSig.sendMsg(request,
							order.getPatientId(), order.getDoctorId(), 0, json);
					if (result != null
							&& result.get("respCode").toString().equals("1001")) {
						orderDao.insertImSendRecord(order.getOrderNo(),
								doctorId, order.getPatientId(), 1, 16,
								order.getReceiptsPrice() + "", "sendMsg");
					} else {
						result = GetSig.sendMsg(request, order.getPatientId(),
								order.getDoctorId(), 0, json);
						if (result != null
								&& !result.get("respCode").toString()
										.equals("1001")) {
							orderDao.insertImSendRecord(order.getOrderNo(),
									doctorId, order.getPatientId(), 0, 16,
									order.getReceiptsPrice() + "", "sendMsg");
							Map<String, Object> params = new HashMap<String, Object>();
							params.put("msgtype", "text");
							params.put("toUser", "o92LXv1ZIHTjleF3MYPFlAiV7Bas");
							params.put("url", "");
							params.put("content", "发送IM消息失败，方法名称GetSig.sendMsg");
							sendModelMsgUtil.sendCustomMsg(params, orderDao);
						}
					}
				} else {
					String testId = appDoctorDao.queryDefultSpecial(
							order.getDoctorId(), 1);

					try {
						if (patientData != null) {
							Map<String, Object> result = GetSig.sendMsg(
									request, order.getPatientId(),
									order.getDoctorId(), order.getOrderNo(),
									patientData.get("name").toString(),
									order.getOrderType());
							if (result != null
									&& result.get("respCode").toString()
											.equals("1001")) {
								orderDao.insertImSendRecord(order.getOrderNo(),
										doctorId, order.getPatientId(), 1,
										null, order.getReceiptsPrice() + "",
										"sendMsg");
							} else {
								result = GetSig.sendMsg(request,
										order.getPatientId(),
										order.getDoctorId(),
										order.getOrderNo(),
										patientData.get("name").toString(),
										order.getOrderType());
								if (result != null
										&& !result.get("respCode").toString()
												.equals("1001")) {
									orderDao.insertImSendRecord(
											order.getOrderNo(), doctorId,
											order.getPatientId(), 0, null,
											order.getReceiptsPrice() + "",
											"sendMsg");
									Map<String, Object> params = new HashMap<String, Object>();
									params.put("msgtype", "text");
									params.put("toUser",
											"o92LXv1ZIHTjleF3MYPFlAiV7Bas");
									params.put("url", "");
									params.put("content",
											"发送IM消息失败，方法名称GetSig.sendMsg");
									sendModelMsgUtil.sendCustomMsg(params,
											orderDao);
								}
							}
						}
					} catch (Exception e) {
						System.out.println("发送IM推送异常：" + e);
					}

					if (!StringUtil.isEmpty(testId)) {
						Map<String, Object> spect = imService.queryLastOrderNo(
								order.getPatientId(), doctorId, testId);
						if (spect.get("respCode").toString().equals("1001")) {
							JSONObject json = new JSONObject();
							json.put("msgType", 8);
							json.put("orderNo", order.getOrderNo());
							json.put(
									"dataId",
									JsonUtil.jsonToMap(spect.get("data")).get(
											"testId"));
							json.put("serverOrderNo", order.getOrderNo());
							Map<String, Object> result = GetSig.sendMsg(
									request, order.getDoctorId(),
									order.getPatientId(), 1, json);
							if (result != null
									&& result.get("respCode").toString()
											.equals("1001")) {
								orderDao.insertImSendRecord(order.getOrderNo(),
										doctorId, order.getPatientId(), 1,
										null, order.getReceiptsPrice() + "",
										"sendMsg");
							} else {
								result = GetSig.sendMsg(request,
										order.getDoctorId(),
										order.getPatientId(), 1, json);
								if (result != null
										&& !result.get("respCode").toString()
												.equals("1001")) {
									orderDao.insertImSendRecord(
											order.getOrderNo(), doctorId,
											order.getPatientId(), 0, null,
											order.getReceiptsPrice() + "",
											"sendMsg");
									Map<String, Object> params = new HashMap<String, Object>();
									params.put("msgtype", "text");
									params.put("toUser",
											"o92LXv1ZIHTjleF3MYPFlAiV7Bas");
									params.put("url", "");
									params.put("content",
											"发送IM消息失败，方法名称GetSig.sendMsg");
									sendModelMsgUtil.sendCustomMsg(params,
											orderDao);
								}
							}
						}
					}
				}
			} else {
				if (patientData != null) {
					Map<String, Object> result = GetSig.sendMsg(request, order
							.getPatientId(), order.getDoctorId(), order
							.getOrderNo(), patientData.get("name").toString(),
							order.getOrderType());
					if (result != null
							&& result.get("respCode").toString().equals("1001")) {
						orderDao.insertImSendRecord(order.getOrderNo(),
								doctorId, order.getPatientId(), 1, null,
								order.getReceiptsPrice() + "", "sendMsg");
					} else {
						result = GetSig.sendMsg(request, order.getPatientId(),
								order.getDoctorId(), order.getOrderNo(),
								patientData.get("name").toString(),
								order.getOrderType());
						if (result != null
								&& !result.get("respCode").toString()
										.equals("1001")) {
							orderDao.insertImSendRecord(order.getOrderNo(),
									doctorId, order.getPatientId(), 0, null,
									order.getReceiptsPrice() + "", "sendMsg");
							Map<String, Object> params = new HashMap<String, Object>();
							params.put("msgtype", "text");
							params.put("toUser", "o92LXv1ZIHTjleF3MYPFlAiV7Bas");
							params.put("url", "");
							params.put("content", "发送IM消息失败，方法名称GetSig.sendMsg");
							sendModelMsgUtil.sendCustomMsg(params, orderDao);
						}
					}
				}
			}
			Map<String, Object> follow = orderDao.queryFollowIdByOpenId(order
					.getDoctorId());
			if (null != follow && follow.size() > 0) {

				if (order.getOrderType() == 4) {
					pushService.paysuczxtl(follow.get("followId").toString(),
							order.getDoctorId(), follow.get("docName")
									.toString());
					if (patientData != null) {
						pushService.doctwtlservice(follow.get("followId")
								.toString(),
								patientData.get("name").toString(),
								follow.get("docName").toString(), order
										.getOrderNo(), order.getDoctorId());
					}

				} else if (order.getOrderType() == 7) {
					pushService.paysucdhtl(follow.get("followId").toString(),
							order.getDoctorId(), follow.get("docName")
									.toString());
				} else if (order.getOrderType() == 6) {
					pushService.paysuctwzx(follow.get("followId").toString(),
							order.getDoctorId(), follow.get("docName")
									.toString());
					if (patientData != null) {
						pushService.doctwzxservice(follow.get("followId")
								.toString(),
								patientData.get("name").toString(),
								follow.get("docName").toString(), order
										.getOrderNo(), order.getDoctorId());
					}

				} else if (order.getOrderType() == 8) {
					pushService.paysucdhzx(follow.get("followId").toString(),
							order.getDoctorId(), follow.get("docName")
									.toString());
				}
			}

			if (order.getOrderType() == 4) {
				pushService.docpaysuczxtl(order.getDoctorId(),
						order.getPatientId(), null);
				Map<String, Object> doctor = appDoctorDao.queryPushDoctor(order
						.getDoctorId());
				if (null != doctor && doctor.size() > 0 && patientData != null) {
					String[] params = { "图文调理",
							patientData.get("name").toString() };
					QCloudSmsUtil
							.sendSmsByTemId(doctor.get("docPhone").toString(),
									SMSTemplateIdUtil.twServer_SMS, params);
					sendModelMsgUtil
							.sendBuySuccess(
									order.getPatientId(),
									"图文调理",
									doctor.get("docName").toString(),
									doctor.get("infirmaryName").toString(),
									patientData.get("name").toString(),
									Integer.valueOf(patientData.get("sex")
											.toString()) == 0 ? "男" : "女",
									appDoctorDao,
									"im/inquiry.html?identifier="
											+ order.getPatientId()
											+ "&selToID=" + order.getDoctorId());
				}
			} else if (order.getOrderType() == 6) {
				pushService.docpaysuctwzx(order.getDoctorId(),
						order.getPatientId(), null);
				Map<String, Object> doctor = appDoctorDao.queryPushDoctor(order
						.getDoctorId());
				if (null != doctor && doctor.size() > 0 && patientData != null) {
					String[] params = { "图文咨询",
							patientData.get("name").toString() };
					QCloudSmsUtil
							.sendSmsByTemId(doctor.get("docPhone").toString(),
									SMSTemplateIdUtil.twServer_SMS, params);
					sendModelMsgUtil
							.sendBuySuccess(
									order.getPatientId(),
									"图文咨询",
									doctor.get("docName").toString(),
									doctor.get("infirmaryName").toString(),
									patientData.get("name").toString(),
									Integer.valueOf(patientData.get("sex")
											.toString()) == 0 ? "男" : "女",
									appDoctorDao,
									"im/inquiry.html?identifier="
											+ order.getPatientId()
											+ "&selToID=" + order.getDoctorId());
				}
			} else if (order.getOrderType() == 7) {
				pushService.docpaysucdhtl(order.getDoctorId(),
						order.getPatientId(), null);
				Map<String, Object> doctor = appDoctorDao.queryPushDoctor(order
						.getDoctorId());
				if (null != doctor && doctor.size() > 0 && patientData != null) {
					String[] params = { "电话调理",
							patientData.get("name").toString() };
					QCloudSmsUtil.sendSmsByTemId(doctor.get("docPhone")
							.toString(), SMSTemplateIdUtil.phoneServer_SMS,
							params);
					sendModelMsgUtil
							.sendBuySuccess(
									order.getPatientId(),
									"电话调理",
									doctor.get("docName").toString(),
									doctor.get("infirmaryName").toString(),
									patientData.get("name").toString(),
									Integer.valueOf(patientData.get("sex")
											.toString()) == 0 ? "男" : "女",
									appDoctorDao,
									"im/inquiry.html?identifier="
											+ order.getPatientId()
											+ "&selToID=" + order.getDoctorId());
				}
			} else if (order.getOrderType() == 8) {
				pushService.docpaysucdhzx(order.getDoctorId(),
						order.getPatientId(), null);
				Map<String, Object> doctor = appDoctorDao.queryPushDoctor(order
						.getDoctorId());
				if (null != doctor && doctor.size() > 0 && patientData != null) {
					String[] params = { "电话咨询",
							patientData.get("name").toString() };
					QCloudSmsUtil.sendSmsByTemId(doctor.get("docPhone")
							.toString(), SMSTemplateIdUtil.phoneServer_SMS,
							params);
					sendModelMsgUtil
							.sendBuySuccess(
									order.getPatientId(),
									"电话咨询",
									doctor.get("docName").toString(),
									doctor.get("infirmaryName").toString(),
									patientData.get("name").toString(),
									Integer.valueOf(patientData.get("sex")
											.toString()) == 0 ? "男" : "女",
									appDoctorDao,
									"im/inquiry.html?identifier="
											+ order.getPatientId()
											+ "&selToID=" + order.getDoctorId());
				}
			}
		}
	}
}
