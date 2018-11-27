package cn.syrjia.sales.service.impl;

import cn.syrjia.callCenter.util.CallCenterConfig;
import cn.syrjia.callCenter.util.SendCallCenterUtil;
import cn.syrjia.common.impl.BaseServiceImpl;
import cn.syrjia.config.Config;
import cn.syrjia.config.configCode;
import cn.syrjia.entity.LoginLog;
import cn.syrjia.hospital.dao.DoctorDao;
import cn.syrjia.hospital.dao.KnowledgeDao;
import cn.syrjia.sales.dao.SalesDao;
import cn.syrjia.sales.entity.FeedBack;
import cn.syrjia.sales.entity.SalesRepresent;
import cn.syrjia.sales.entity.SalesSet;
import cn.syrjia.sales.service.SalesService;
import cn.syrjia.util.*;
import cn.syrjia.weixin.util.StringUtils;
import net.sf.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("salesService")
public class SalesServiceImpl extends BaseServiceImpl implements SalesService {

	// 日志
	private Logger logger = LogManager.getLogger(SalesServiceImpl.class);

	@Resource(name = "salesDao")
	SalesDao salesDao;

	@Resource(name = "doctorDao")
	DoctorDao doctorDao;
	
	@Resource(name = "knowledgeCircleDao")
	public KnowledgeDao knowledgeDao;

	@Resource(name = "config")
	Config config;

	/**
	 * 退出登录
	 */
	@Override
	public Map<String, Object> loginOut(String oldToken) {
		if (StringUtil.isEmpty(oldToken)) {
			return Util.resultMap(configCode.code_1051, null);
		} else {
			RedisUtil.deleteKey(oldToken);
			return Util.resultMap(configCode.code_1001, 1);
		}
	}

	/**
	 * 编辑安全设置信息
	 */
	@SuppressWarnings("unused")
	@Override
	public Map<String, Object> editSalesSet(SalesSet salesSet) {
		try {
			//助理id为空判断
			if (StringUtil.isEmpty(salesSet.getSrId())) {
				return Util.resultMap(configCode.code_1029, null);
			} else {
				//检验助理自己设置是否存在
				String id = salesDao.checkSalesSetById(salesSet.getSrId());
				String oldGesPassword = null;
				String oldScrectPassword = null;
				Integer oldIsGesture = 0;
				Integer oldIsScret = 0;
				if (!StringUtil.isEmpty(id)) {
					//查询助理安全设置
					Map<String,Object> oldSalesSet = salesDao.querySalesSetByid(id);
					if(oldSalesSet!=null){
						oldGesPassword = oldSalesSet.get("gesturePassword").toString();
						oldScrectPassword = oldSalesSet.get("secretPassword").toString();
						oldIsGesture = Integer.valueOf(oldSalesSet.get("isGesture").toString());
						oldIsScret = Integer.valueOf(oldSalesSet.get("isSecret").toString());
					}
				}
				salesSet.setIsSystem(1);
				//非空判断
				if (!StringUtil.isEmpty(salesSet.getIsGesture())
						&& salesSet.getIsGesture() == 1) {
					if (!StringUtil.isEmpty(salesSet.getGesturePassword())) {
						//设置密码
						salesSet.setGesturePassword(Md5Encoder.getMd5(salesSet
								.getGesturePassword()));
					} else {
						if (StringUtil.isEmpty(oldGesPassword)) {
							return Util.resultMap(configCode.code_1121, null);
						}
						//设置密码
						salesSet.setGesturePassword(null);
					}
				} else if (!StringUtil.isEmpty(salesSet.getIsSecret())
						&& salesSet.getIsSecret() == 1) {
					if (!StringUtil.isEmpty(salesSet.getSecretPassword())) {
						salesSet.setSecretPassword(Md5Encoder.getMd5(salesSet
								.getSecretPassword()));
					} else {
						if (StringUtil.isEmpty(oldScrectPassword)) {
							//密码未设置，请先设置
							return Util.resultMap(configCode.code_1121, null);
						}
						salesSet.setSecretPassword(null);
					}
				} else {
					if (StringUtil.isEmpty(salesSet.getSecretPassword())
							&& StringUtil
									.isEmpty(salesSet.getGesturePassword())) {
						//密码未设置，请先设置
						return Util.resultMap(configCode.code_1121, null);
					}
					if (!StringUtil.isEmpty(salesSet.getGesturePassword())) {
						salesSet.setGesturePassword(Md5Encoder.getMd5(salesSet
								.getGesturePassword()));
					} else if ("null".equals(salesSet.getGesturePassword())) {
						salesSet.setGesturePassword(null);
					}
					if (!StringUtil.isEmpty(salesSet.getSecretPassword())) {
						salesSet.setSecretPassword(Md5Encoder.getMd5(salesSet
								.getSecretPassword()));
					} else if ("null".equals(salesSet.getSecretPassword())) {
						salesSet.setSecretPassword(null);
					}
				}

				if (!StringUtil.isEmpty(id)) {
					salesSet.setId(id);
					Integer i = salesDao.updateEntity(salesSet);
					if (i > 0) {
						//成功
						return Util.resultMap(configCode.code_1001, i);
					} else {
						//操作失败
						return Util.resultMap(configCode.code_1066, "");
					}
				} else {
					Object i = salesDao.addEntityUUID(salesSet);
					if (i != null) {
						return Util.resultMap(configCode.code_1001, i);
					} else {
						//操作失败
						return Util.resultMap(configCode.code_1066, "");
					}
				}
			}
		} catch (Exception e) {
			logger.info(e);
		}
		return null;
	}

	/**
	 * 验证手势密码、数字密码是否正确
	 */
	@Override
	public Map<String, Object> checkSalesPassword(SalesSet salesSet) {
		try {
			if (StringUtil.isEmpty(salesSet.getSrId())
					|| (StringUtil.isEmpty(salesSet.getGesturePassword()) && StringUtil
							.isEmpty(salesSet.getSecretPassword()))) {
				return Util.resultMap(configCode.code_1029, null);
			} else {
				Integer i = 0;
				if (!StringUtil.isEmpty(salesSet.getGesturePassword())) {
					salesSet.setGesturePassword(Md5Encoder.getMd5(salesSet
							.getGesturePassword()));
					//验证手势密码、数字密码是否正确
					i = salesDao.checkSalesPassword(salesSet);
					if (i > 0) {
						return Util.resultMap(configCode.code_1001, null);
					} else {
						return Util.resultMap(configCode.code_1003, null);
					}
				} else if (!StringUtil.isEmpty(salesSet.getSecretPassword())) {
					salesSet.setSecretPassword(Md5Encoder.getMd5(salesSet
							.getSecretPassword()));
					//验证手势密码、数字密码是否正确
					i = salesDao.checkSalesPassword(salesSet);
					if (i > 0) {
						//成功
						return Util.resultMap(configCode.code_1001, null);
					} else {
						//数字密码不正确！
						return Util.resultMap(configCode.code_1005, null);
					}
				} else {
					//参数不正确
					return Util.resultMap(configCode.code_1029, null);
				}
			}
		} catch (Exception e) {
			logger.info(e);
			return Util.resultMap(configCode.code_1015, null);
		}
	}

	/**
	 * 根据ID获取助理个人信息
	 */
	@Override
	public Map<String, Object> queryOneSales(String srId) {
		try {
			if (StringUtils.isEmpty(srId)) {
				//参数不正确
				return Util.resultMap(configCode.code_1029, "");
			} else {
				//根据助理ID获取医生个人信息
				Map<String, Object> map = salesDao.queryOneSales(srId, null,
						null);
				if (map == null) {
					//医生信息不存在
					return Util.resultMap(configCode.code_1032, "");
				} else {
					return Util.resultMap(configCode.code_1001, map);
				}
			}
		} catch (Exception e) {
			logger.info(e);
			return Util.resultMap(configCode.code_1015, null);
		}
	}

	/**
	 * 修改助理信息
	 */
	@Override
	public Map<String, Object> updateSales(SalesRepresent sales) {
		try {
			if (StringUtils.isEmpty(sales.getSrId())
					|| (StringUtils.isEmpty(sales.getImgUrl())
							&& StringUtils.isEmpty(sales.getPhone()) && StringUtils
								.isEmpty(sales.getSalesPassword()))) {
				return Util.resultMap(configCode.code_1029, "");
			} else {
				//修改助理头像、手机号、修改密码
				Integer i = salesDao.updateSales(sales);
				if (i > 0) {
					return Util.resultMap(configCode.code_1001,
							sales.getImgUrl());
				} else {
					return Util.resultMap(configCode.code_1048, "");
				}
			}
		} catch (Exception e) {
			logger.info(e);
			return Util.resultMap(configCode.code_1015, null);
		}
	}

	/**
	 * 获取手机验证码
	 */
	@Override
	public Map<String, Object> getPhoneCode(HttpServletRequest request,
			String srId, String phone, Integer type) {
		if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(type)) {
			return Util.resultMap(configCode.code_1029, null);
		} else {
			//验证验证码是否存在
			Map<String, Object> codeMap = salesDao.queryCodeByPhoneAndCode(
					phone, null, type);
			if (codeMap != null
					&& (Util.queryNowTime() - Integer.valueOf(codeMap.get(
							"createTime").toString())) < 170) {
				return Util.resultMap(configCode.code_1070, null);
			} else {
				//根据手机号查询获取验证码次数
				Integer num = salesDao.checkSendCodeCount(phone, type);
				String ip = Util.getIp(request);
				if (num > 5) {
					return Util.resultMap(configCode.code_1076, null);
				} else {
					if (type == 7) {// 助理验证老手机号
						if (StringUtils.isEmpty(srId)) {
							return Util.resultMap(configCode.code_1029, "");
						} else {
							//根据助理ID获取医生个人信息
							Map<String, Object> map = salesDao.queryOneSales(
									srId, 0, phone);
							if (map == null) {
								return Util.resultMap(configCode.code_1092, "");
							} else {
								String oldPhone = map.get("phone").toString()
										.trim();
								if (oldPhone.equals(phone)) {
									//发送验证码
									return salesDao.sendCode(phone, type, ip);
								} else {
									return Util.resultMap(configCode.code_1012,
											"");
								}
							}
						}
					} else if (type == 8) {// 助理验证新手机号
						Integer count = salesDao.checkSalesPhone(phone, null);
						if (count > 0) {
							return Util.resultMap(configCode.code_1042, "");
						} else {
							//发送验证码
							return salesDao.sendCode(phone, type, ip);
						}
					} else {
						//根据助理ID获取医生个人信息
						Map<String, Object> map = salesDao.queryOneSales(null,
								0, phone);
						if (map == null) {
							return Util.resultMap(configCode.code_1092, "");
						} else {
							//检验助理手机号是否存在（修改手机号验证用）
							Integer count = salesDao.checkSalesPhone(phone,
									null);
							if (count > 0) {
								//发送验证码
								return salesDao.sendCode(phone, type, ip);
							} else {
								return Util.resultMap(configCode.code_1012, "");
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 助理端APP登录接口
	 */
	@Override
	public Map<String, Object> login(HttpServletRequest request,
			String loginName, String password, LoginLog loginLog) {
		if (StringUtil.isEmpty(loginName) || StringUtil.isEmpty(password)) {
			return Util.resultMap(configCode.code_1029, null);
		}
		//通过手机号查询助理信息
		String srId = salesDao.querySalesByPhone(loginName);
		if (StringUtil.isEmpty(srId)) {
			//该手机号未注册
			return Util.resultMap(configCode.code_1012, null);
		}
		password = Md5Encoder.getMd5(password);
		//查询登录接口
		Map<String, Object> salesMap = salesDao.login(loginName, password);
		if (null == salesMap) {
			return Util.resultMap(configCode.code_1008, null);
		}
		Integer state = Integer.valueOf(salesMap.get("state").toString());
		if (state == 1) {
			return Util.resultMap(configCode.code_1009, null);
		}
		loginLog.setUserId(srId);
		loginLog.setCreateTime(Util.queryNowTime());
		//增加
		Object obj = salesDao.addEntityUUID(loginLog);
		if (StringUtil.isEmpty(obj)) {
			return Util.resultMap(configCode.code_1015, null);
		}

		String _token = Util.getUUID();
		String _encry_token = Util.getUUID();
		//添加请求token
		Integer i = salesDao.addMemberToken(srId, _token);
		if (i == 0) {
			return Util.resultMap(configCode.code_1064, null);
		}
		//修改助理是否已下载app
		salesDao.updateSaleDownloadState(srId);
		JSONObject json = new JSONObject();
		json.set("srId", srId);
		json.set("_encry_token", _encry_token);
		RedisUtil.setVal(_token, 60 * 60 * 2, json.toString());
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("_token", _token);
		returnMap.put("_encry_token", _encry_token);
		returnMap.put("srId", srId);
		returnMap.put("memberId", srId);
		returnMap.put("isSelfSet", salesMap.get("isSelfSet"));
		return Util.resultMap(configCode.code_1001, returnMap);
	}

	/**
	 * 验证老手机号、验证码是否正确
	 */
	@Override
	public Map<String, Object> validateOldPhoneAndCode(String phone,
			String srId, String code) {
		if (StringUtil.isEmpty(phone) || StringUtil.isEmpty(code)
				|| StringUtil.isEmpty(srId)) {
			return Util.resultMap(configCode.code_1029, null);
		}
		Integer type = 7;
		if (StringUtil.isEmpty(srId)) {// 忘记密码
			type = 9;
		}
		//验证验证码是否存在
		Map<String, Object> codeMap = salesDao.queryCodeByPhoneAndCode(phone,
				code, type);
		if (null == codeMap) {
			return Util.resultMap(configCode.code_1045, null);
		}
		Integer createTime = Integer.parseInt(codeMap.get("createTime")
				.toString());
		if (Util.queryNowTime() - createTime > 180) {
			return Util.resultMap(configCode.code_1046, null);
		}

		if (!StringUtil.isEmpty(srId)) {
			//根据助理ID获取医生个人信息
			Map<String, Object> map = salesDao.queryOneSales(srId, 0, null);
			if (map == null) {
				return Util.resultMap(configCode.code_1092, "");
			}
			String oldPhone = map.get("phone").toString().trim();
			if (oldPhone.equals(phone)) {
				//修改验证码状态
				salesDao.updateCodeState(phone, type, code);
				return Util.resultMap(configCode.code_1001, null);
			} else {
				return Util.resultMap(configCode.code_1043, "");
			}
		} else {
			//修改验证码状态
			salesDao.updateCodeState(phone, type, code);
			return Util.resultMap(configCode.code_1001, null);
		}
	}

	/**
	 * 修改助理手机号
	 */
	@Override
	public Map<String, Object> updateSalesPhone(HttpServletRequest request,
			String phone, String code, String srId) {

		if (StringUtil.isEmpty(phone) || StringUtil.isEmpty(code)
				|| StringUtil.isEmpty(srId)) {
			return Util.resultMap(configCode.code_1029, null);
		}
		//验证验证码是否存在
		Map<String, Object> codeMap = salesDao.queryCodeByPhoneAndCode(phone,
				code, 8);
		if (null == codeMap) {
			return Util.resultMap(configCode.code_1045, null);
		}
		Integer createTime = Integer.parseInt(codeMap.get("createTime")
				.toString());
		if (Util.queryNowTime() - createTime > 180) {
			return Util.resultMap(configCode.code_1046, null);
		}
		//根据助理ID获取医生个人信息
		Map<String, Object> map = salesDao.queryOneSales(srId, 0, null);
		if (map == null) {
			return Util.resultMap(configCode.code_1032, "");
		}
		String oldPhone = map.get("phone").toString().trim();
		if (oldPhone.equals(phone)) {
			return Util.resultMap(configCode.code_1031, "");
		}
		//修改手机号加入记录表
		String obj = salesDao.addPhoneUpdate(srId, oldPhone, phone);
		if (StringUtil.isEmpty(obj)) {
			return Util.resultMap(configCode.code_1015, null);
		}

		SalesRepresent sales = new SalesRepresent();
		sales.setSrId(srId);
		sales.setPhone(phone);
		//修改助理头像、手机号、修改密码
		Integer i = salesDao.updateSales(sales);
		if (i > 0) {
			//修改验证码状态
			salesDao.updateCodeState(phone, 8, code);
			return Util.resultMap(configCode.code_1001, i);
		} else {
			return Util.resultMap(configCode.code_1014, null);
		}

	}

	/**
	 * 根据医生ID查询医生文章信息
	 */
	@Override
	public Map<String, Object> queryDoctorArticleList(String srId,
			String doctorId, Integer row, Integer page) {
		if (StringUtil.isEmpty(doctorId) || StringUtil.isEmpty(srId)) {
			return Util.resultMap(configCode.code_1029, null);
		}
		//根据医生ID查询医生文章信息
		List<Map<String, Object>> list = salesDao.queryDoctorArticleList(srId,
				doctorId, row, page);
		if (list == null) {
			return Util.resultMap(configCode.code_1015, null);
		} else {
			for(Map<String,Object> map:list){
				String labelNames=null;
				if(!StringUtil.isEmpty(map.get("labelIds"))){
					//通过ID查询标签名称
					labelNames=knowledgeDao.queryLabelNames(map.get("labelIds").toString());
				}
				/*Map<String,Object> info=knowledgeDao.queryKnowledgeInfo(map.get("id").toString(),StringUtil.isEmpty(map.get("labelIds"))?"":map.get("labelIds").toString(), memberId);
				
				map.put("collectNum",info.get("collectNum").toString());
				if(!StringUtil.isEmpty(info.get("labelNames"))){
					map.put("labelNames",info.get("labelNames").toString());
				}else{
					map.put("labelNames",null);
				}
				map.put("replyNum",info.get("replyNum").toString());
				map.put("shareNum",info.get("shareNum").toString());
				map.put("plNum",info.get("plNum").toString());
				map.put("pointNum",info.get("pointNum").toString());
				map.put("followId",info.get("follow").toString());
				map.put("praiseId",info.get("praise").toString());
				map.put("collectId",info.get("collect").toString());
				
				
				if(info.get("follow").toString().equals(0)){
					map.put("follow",1);
				}else{
					map.put("follow",2);
				}
				
				if(info.get("praise").toString().equals(0)){
					map.put("praise",1);
				}else{
					map.put("praise",2);
				}
				
				if(info.get("collect").toString().equals(0)){
					map.put("collect",1);
				}else{
					map.put("collect",2);
				}*/
				//Integer collectNum=knowledgeDao.queryCollectNum(map.get("id").toString());
				//查询评论数
				Integer replyNum=knowledgeDao.queryReplyNum(map.get("id").toString(),srId);
				//Integer shareNum=knowledgeDao.queryShareNum(map.get("id").toString());
				//查询评论数
				Integer plNum=knowledgeDao.queryPlNum(map.get("id").toString(),srId);
				//Integer pointNum=knowledgeDao.queryPointNum(map.get("id").toString());
				//查询是否关注
				Integer follow=knowledgeDao.queryFollow(map.get("doctorId").toString(),srId);
				//查询是否点赞
				Integer praiseId=knowledgeDao.querypPraiseId(map.get("id").toString(),srId);
				//查询是否收藏
				Map<String,Object> collect=knowledgeDao.querypCollectId(map.get("id").toString(),srId);
				//map.put("collectNum",collectNum);
				map.put("labelNames",labelNames);
				map.put("replyNum",replyNum);
				//map.put("shareNum",shareNum);
				map.put("plNum",plNum);
				//map.put("pointNum",pointNum);
				map.put("followId",follow);
				map.put("praiseId",praiseId);
				map.put("collectId",collect.get("num"));
				map.put("collectTime",collect.get("createTime"));
				if(follow==0){
					map.put("follow",1);
				}else{
					map.put("follow",2);
				}
				if(praiseId==0){
					map.put("praise",1);
				}else{
					map.put("praise",2);
				}
				if(collect.size()==0||StringUtil.isEmpty(collect.get("num"))||Integer.parseInt(collect.get("num").toString())==0){
					map.put("collect",1);
				}else{
					map.put("collect",2);
				}
			}
			
			return Util.resultMap(configCode.code_1001, list);
		}
	}

	/**
	 * 根据医生ID查询医生评价信息（详情页面与评价列表共用）
	 */
	@Override
	public Map<String, Object> queryDoctorEvaList(String doctorId, Integer row,
			Integer page, String _sign) {
		if (StringUtil.isEmpty(doctorId)) {
			return Util.resultMap(configCode.code_1029, null);
		}
		//根据医生ID查询医生评价信息（详情页面与评价列表共用）
		List<Map<String, Object>> list = salesDao.queryDoctorEvaList(doctorId,
				row, page, _sign);
		if (list == null) {
			return Util.resultMap(configCode.code_1015, null);
		} else {
			for (int i = 0; i < list.size(); i++) {
				//根据评价ID查询锦旗
				Map<String, Object> map = doctorDao.queryJqImgs(list.get(i)
						.get("id").toString());
				if (map != null) {
					list.get(i).put("jqimgs", map.get("imgdata"));
				} else {
					list.get(i).put("jqimgs", "");
				}
			}
			return Util.resultMap(configCode.code_1001, list);
		}
	}

	/**
	 * 根据医生ID获取医生详情
	 */
	@Override
	public Map<String, Object> queryDoctorById(String doctorId) {
		if (StringUtil.isEmpty(doctorId)) {
			return Util.resultMap(configCode.code_1029, null);
		}
		//根据医生ID查询医生详情信息
		Map<String, Object> docMap = salesDao.queryDoctorById(doctorId);
		if (docMap == null) {
			return Util.resultMap(configCode.code_1015, null);
		} else {
			Integer buyNum=0;
			Integer evaCnt=0;
			Integer knowledgeCount =0;
			Integer followCount=0;
			Integer dayBuyNum=0;
			Double dayTotalPrice = 0.0;
			String goodEvaRate = "100%";
			//查询医生主页统计
			Map<String,Object> map = salesDao.queryDoctorCenvs(doctorId);
			if(map!=null){
				//获取数据
				buyNum = StringUtil.isEmpty(map.get("buyNum"))?0:Integer.valueOf(map.get("buyNum").toString());
				evaCnt = StringUtil.isEmpty(map.get("evaCnt"))?0:Integer.valueOf(map.get("evaCnt").toString());
				knowledgeCount = StringUtil.isEmpty(map.get("knowledgeCount"))?0:Integer.valueOf(map.get("knowledgeCount").toString());
				followCount = StringUtil.isEmpty(map.get("followCount"))?0:Integer.valueOf(map.get("followCount").toString());
				followCount+=StringUtil.isEmpty(map.get("keepCount"))?0:Integer.valueOf(map.get("keepCount").toString());
				dayBuyNum = StringUtil.isEmpty(map.get("dayBuyNum"))?0:Integer.valueOf(map.get("dayBuyNum").toString());
				dayTotalPrice = StringUtil.isEmpty(map.get("dayTotalPrice"))?0:Double.valueOf(map.get("dayTotalPrice").toString());
				goodEvaRate = StringUtil.isEmpty(map.get("goodEvaRate"))?"100%":map.get("goodEvaRate").toString();
			}
			//拼接返回数据
			docMap.put("buyNum", buyNum);
			docMap.put("evaCnt", evaCnt);
			docMap.put("knowledgeCount", knowledgeCount);
			docMap.put("followCount", followCount);
			docMap.put("dayBuyNum", dayBuyNum);
			docMap.put("dayTotalPrice", dayTotalPrice);
			docMap.put("goodEvaRate", goodEvaRate);
			return Util.resultMap(configCode.code_1001, docMap);
		}
	}

	/**
	 * 根据医生ID获取医生详情下方展示内容
	 */
	@Override
	public Map<String, Object> queryDoctorOtherById(String doctorId) {
		if (StringUtil.isEmpty(doctorId)) {
			return Util.resultMap(configCode.code_1029, null);
		}
		Map<String, Object> returnMap = new HashMap<String, Object>();
		/*
		 * returnMap.put("ill", ""); returnMap.put("online", "");
		 * returnMap.put("zzData", ""); returnMap.put("notice", "");
		 * returnMap.put("desc", ""); returnMap.put("eva", "");
		 */
		//根据医生查询医生专业擅长
		List<Map<String, Object>> illList = salesDao.queryDoctorIlls(doctorId);
		returnMap.put("illList", illList);
		/*
		 * if(illList!=null&&illList.size()>0){ Map<String,Object> illMap = new
		 * HashMap<String,Object>(); illMap.put("name", "专业擅长");
		 * illMap.put("illList", illList); returnMap.put("ill", illMap); }
		 */
		//查询医生开着的线上调理信息
		List<Map<String, Object>> onlineList = salesDao
				.queryDoctorOnline(doctorId);
		returnMap.put("onlineList", onlineList);
		/*
		 * if(onlineList!=null&&onlineList.size()>0){ Map<String,Object>
		 * onLineMap = new HashMap<String,Object>(); onLineMap.put("name",
		 * "线上调理"); onLineMap.put("onlineList", onlineList);
		 * returnMap.put("online", onLineMap); }
		 */
		//查询医生14天是否有出诊
		Integer isHasZz = salesDao.queryFourTeenCzCount(doctorId);
		//查询从今天起7天内坐诊状态
		List<Map<String, Object>> sevenList = salesDao
				.querySevenZzStatus(doctorId);
		returnMap.put("isCz", isHasZz);
		if (isHasZz > 0) {
			returnMap.put("sevenList", sevenList);
		}
		/*
		 * if(isHasZz>0){ List<Map<String,Object>> sevenList =
		 * salesDao.querySevenZzStatus(doctorId);
		 * if(onlineList!=null&&onlineList.size()>0){ Map<String,Object>
		 * zzDataMap = new HashMap<String,Object>(); zzDataMap.put("name",
		 * "线下坐诊"); zzDataMap.put("sevenList", sevenList);
		 * returnMap.put("zzData", zzDataMap); } }
		 */
		//根据医生ID查询医生简介及公告信息
		Map<String, Object> docMap = salesDao
				.queryDoctorAbstractAndDesc(doctorId);
		returnMap.put("notice", docMap.get("noticeContent"));
		returnMap.put("docAbstract", docMap.get("docAbstract"));
		/*
		 * if(docMap!=null){
		 * if(!StringUtil.isEmpty(docMap.get("noticeContent"))){
		 * Map<String,Object> noticeMap = new HashMap<String,Object>();
		 * noticeMap.put("name", "医生公告"); noticeMap.put("notice",
		 * docMap.get("noticeContent")); returnMap.put("notice", noticeMap); }
		 * if(!StringUtil.isEmpty(docMap.get("docAbstract"))){
		 * Map<String,Object> descMap = new HashMap<String,Object>();
		 * descMap.put("name", "医生介绍"); descMap.put("docAbstract",
		 * docMap.get("docAbstract")); returnMap.put("desc", descMap); } }
		 */
		//根据医生ID查询医生评价信息（详情页面与评价列表共用）
		List<Map<String, Object>> evaList = salesDao.queryDoctorEvaList(
				doctorId, null, null, null);
		returnMap.put("evaList", evaList);
		/*
		 * if(evaList!=null&&evaList.size()>0){ Map<String,Object> evaMap = new
		 * HashMap<String,Object>(); evaMap.put("name", "评价");
		 * evaMap.put("evaMap", evaList); returnMap.put("eva", evaMap); }
		 */
		return Util.resultMap(configCode.code_1001, returnMap);
	}

	/**
	 * 助理查询我的医生列表
	 */
	@Override
	public Map<String, Object> queryMyDoctors(String srId, Integer type,
			Integer page, Integer row, String searchKey) {
		Integer t = Util.queryNowTime();
		if (StringUtil.isEmpty(srId)) {
			return Util.resultMap(configCode.code_1029, null);
		}
		//助理查询我的医生列表
		List<Map<String, Object>> list = salesDao.queryMyDoctors(srId, type,
				page, row, searchKey);
		System.out.println(Util.queryNowTime()-t);
		if (list == null) {
			return Util.resultMap(configCode.code_1015, null);
		}
		for (int i = 0; i < list.size(); i++) {
			if (!StringUtil.isEmpty(list.get(i).get("isShow"))) {
				if (list.get(i).get("showType").equals("0")) {
					list.get(i).put("_type", "0");// 审核中
				} else {
					list.get(i).put("_type", "1");// 帮忙认证
				}
			} else {
				list.get(i).put("_type", "");// 帮忙认证
			}
			list.get(i).remove("isShow");
			list.get(i).remove("showType");
		}
		System.out.println(Util.queryNowTime()-t);
		return Util.resultMap(configCode.code_1001, list);
	}

	/**
	 * 获取助理安全设置
	 */
	@Override
	public Map<String, Object> querySalesSetById(String srId) {
		if (StringUtil.isEmpty(srId)) {
			return Util.resultMap(configCode.code_1029, null);
		}
		//获取助理安全设置
		Map<String, Object> map = salesDao.querySalesSetById(srId);
		if (map == null) {
			return Util.resultMap(configCode.code_1015, null);
		}
		if ("-1".equals(map.get("id"))) {
			map.put("id", "");
		}
		return Util.resultMap(configCode.code_1001, map);
	}

	/**
	 * 修改密码接口
	 */
	@Override
	public Map<String, Object> updatePassword(String srId, String phone,
			String oldPassword, String newPassrod, String code) {
		if (StringUtil.isEmpty(newPassrod)) {
			return Util.resultMap(configCode.code_1093, null);
		}
		Integer i = 0;
		try {
			SalesRepresent sales = new SalesRepresent();
			if (!StringUtil.isEmpty(srId) && !StringUtil.isEmpty(oldPassword)) {
				//根据助理ID获取医生个人信息
				Map<String, Object> salesMap = salesDao.queryOneSales(srId, 0,
						null);
				if (salesMap == null) {
					return Util.resultMap(configCode.code_1092, null);
				}
				oldPassword = Md5Encoder.getMd5(oldPassword);
				if (!oldPassword.equals(salesMap.get("salesPassword"))) {
					return Util.resultMap(configCode.code_1022, null);
				}
				newPassrod = Md5Encoder.getMd5(newPassrod);
				if (oldPassword.equals(newPassrod)) {
					return Util.resultMap(configCode.code_1117, null);
				}
				sales.setSrId(srId);
				sales.setSalesPassword(newPassrod);
			} else {
				if (StringUtil.isEmpty(phone)) {
					return Util.resultMap(configCode.code_1029, null);
				}
				//验证验证码是否存在
				Map<String, Object> codeMap = salesDao.queryCodeByPhoneAndCode(
						phone, code, 9);
				if (null == codeMap) {
					return Util.resultMap(configCode.code_1045, null);
				}
				Integer createTime = Integer.parseInt(codeMap.get("createTime")
						.toString());
				if (Util.queryNowTime() - createTime > 180) {
					return Util.resultMap(configCode.code_1046, null);
				}
				newPassrod = Md5Encoder.getMd5(newPassrod);
				//通过手机号查询助理信息
				srId = salesDao.querySalesByPhone(phone);
				if (StringUtil.isEmpty(srId)) {
					return Util.resultMap(configCode.code_1012, null);
				}
				sales.setSrId(srId);
				sales.setSalesPassword(newPassrod);
			}
			//修改助理头像、手机号、修改密码
			i = salesDao.updateSales(sales);
			if (i > 0) {
				//修改验证码状态
				salesDao.updateCodeState(phone, 9, code);
			} else {
				return Util.resultMap(configCode.code_1006, i);
			}
		} catch (Exception e) {
			return Util.resultMap(configCode.code_1015, null);
		}
		return Util.resultMap(configCode.code_1001, i);
	}

	/**
	 * 根据助理ID查询医生
	 */
	@Override
	public Map<String, Object> queryDoctors(String srId) {
		Integer s = Util.queryNowTime();
		if (StringUtil.isEmpty(srId)) {
			//参数不正确
			return Util.resultMap(configCode.code_1029, null);
		}
		//根据助理ID查询医生
		List<Map<String, Object>> list = salesDao.queryDoctors(srId);
		if (null == list) {
			return Util.resultMap(configCode.code_1075, null);
		}
		System.out.println(Util.queryNowTime()-s);
		return Util.resultMap(configCode.code_1001, list);
	}
	
	/**
	 * 查询一周日期
	 */
	@Override
	public Map<String, Object> queryWeekData() {
		//查询一周日期
		List<Map<String, Object>> list = salesDao.queryWeekData();
		if (null == list) {
			return Util.resultMap(configCode.code_1015, null);
		}
		return Util.resultMap(configCode.code_1001, list);
	}

	/**
	 * 根据医生ID查询医生14天出诊状态
	 */
	@Override
	public Map<String, Object> queryFourTeenZzStatus(String doctorId) {
		if (StringUtil.isEmpty(doctorId)) {
			return Util.resultMap(configCode.code_1029, null);
		}
		//根据医生ID查询医生14天出诊状态
		List<Map<String, Object>> list = salesDao
				.queryFourTeenZzStatus(doctorId);
		if (null == list) {
			return Util.resultMap(configCode.code_1015, null);
		}
		return Util.resultMap(configCode.code_1001, list);
	}

	/**
	 * 查询反馈部门列表
	 */
	@Override
	public Map<String, Object> queryFeedDeparts() {
		//查询反馈部门列表
		List<Map<String, Object>> list = salesDao.queryFeedDeparts();
		if (list == null) {
			return Util.resultMap(configCode.code_1015, null);
		}
		return Util.resultMap(configCode.code_1001, list);
	}

	/**
	 * 新增反馈信息
	 */
	@Override
	public Map<String, Object> addFeedBack(FeedBack feedBack) {
		if (feedBack == null || StringUtil.isEmpty(feedBack.getFeedId())
				|| StringUtil.isEmpty(feedBack.getSrId())) {
			return Util.resultMap(configCode.code_1029, null);
		}
		feedBack.setCreateTime(Util.queryNowTime());
		//执行新增
		Object id = salesDao.addEntityUUID(feedBack);
		if (id == null) {
			return Util.resultMap(configCode.code_1015, null);
		}
		return Util.resultMap(configCode.code_1001, id);
	}

	/**
	 * 助理端查询订单列表
	 */
	@Override
	public Map<String, Object> queryOrders(String srId, String doctorId,
			Integer page, Integer row, Integer type, String startTime,
			String endTime) {
		Integer start = Util.queryNowTime();
		if (StringUtil.isEmpty(srId)) {
			return Util.resultMap(configCode.code_1029, null);
		}
		List<Map<String, Object>> mainOrderNos = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> returnOrderNos = new ArrayList<Map<String, Object>>();
		try {
			//查询订单号列表
			mainOrderNos = salesDao.queryOrderNos(srId, doctorId, page, row,
					type, startTime, endTime);
			if (mainOrderNos == null) {
				return Util.resultMap(configCode.code_1011, null);
			}
			for (int i = 0; i < mainOrderNos.size(); i++) {
				Map<String, Object> childMap = new HashMap<String, Object>();
				//查询订单列表
				List<Map<String, Object>> childOrderNos = salesDao.queryOrders(
						null, srId, doctorId, mainOrderNos.get(i)
								.get("orderNo").toString());
				List<Map<String, Object>> childOrderList = new ArrayList<Map<String, Object>>();
				if (childOrderNos == null) {
					return Util.resultMap(configCode.code_1011, null);
				}
				if (childOrderNos.size() == 0) {
					//查询订单列表
					childOrderList = salesDao.queryOrders(mainOrderNos.get(i)
							.get("orderNo").toString(), srId, doctorId, null);
					if (childOrderList == null) {
						return Util.resultMap(configCode.code_1011, null);
					}
					childOrderNos = new ArrayList<Map<String, Object>>();
					childMap.put("childOrderNo", "");
					childMap.put("showState", "");
					childMap.put("childOrderList", childOrderList);
					childOrderNos.add(childMap);
				} else {
					for (int c = 0; c < childOrderNos.size(); c++) {
						//查询订单列表
						childOrderList = salesDao.queryOrders(childOrderNos
								.get(c).get("childOrderNo").toString(), srId,
								doctorId, null);
						if (childOrderList == null) {
							return Util.resultMap(configCode.code_1011, null);
						}
						childOrderNos.get(c).put("childOrderList",
								childOrderList);
						childOrderNos.get(c).remove("createTime");
						childOrderNos.get(c).remove("doctorId");
						childOrderNos.get(c).remove("serverName");
						childOrderNos.get(c).remove("specifications");
						childOrderNos.get(c).remove("imgUrl");
						childOrderNos.get(c).remove("name");
						childOrderNos.get(c).remove("positionName");
						childOrderNos.get(c).remove("orderstate");
						childOrderNos.get(c).remove("buyNum");
						childOrderNos.get(c).remove("type");
					}
				}
				if (childOrderNos != null
						&& childOrderNos.size() > 0
						&& JsonUtil.parseJSON2List(
								childOrderNos.get(0).get("childOrderList"))
								.size() > 0) {
					mainOrderNos.get(i).put("child", childOrderNos);
					returnOrderNos.add(mainOrderNos.get(i));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Util.resultMap(configCode.code_1015, null);
		}
		System.out.println(Util.queryNowTime() - start);
		return Util.resultMap(configCode.code_1001, returnOrderNos);
	}
	
	/**
	 * 助理端查询订单列表（新）
	 */
	@Override
	public Map<String, Object> querySalesOrders(String srId, String doctorId,
			Integer page, Integer row, Integer type, String startTime,
			String endTime) {
		Integer start = Util.queryNowTime();
		if (StringUtil.isEmpty(srId)) {
			return Util.resultMap(configCode.code_1029, null);
		}
		List<Map<String, Object>> childOrderList = new ArrayList<Map<String, Object>>();
		try {
			//查询订单列表
			childOrderList = salesDao.querySalesOrders(null, srId, doctorId, null,page,row,type,startTime,endTime);
			if (childOrderList == null) {
				return Util.resultMap(configCode.code_1011, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Util.resultMap(configCode.code_1015, null);
		}
		System.out.println(Util.queryNowTime() - start);
		return Util.resultMap(configCode.code_1001, childOrderList);
	}

	/**
	 * 订单列表查询我的医生列表
	 */
	@Override
	public Map<String, Object> queryOrderDoctors(String srId, Integer page,
			Integer row) {
		if (StringUtil.isEmpty(srId)) {
			return Util.resultMap(configCode.code_1029, null);
		}
		//订单列表查询我的医生列表
		List<Map<String, Object>> doclist = salesDao.queryOrderDoctors(srId,
				page, row);
		if (doclist == null) {
			return Util.resultMap(configCode.code_1011, null);
		}
		return Util.resultMap(configCode.code_1001, doclist);
	}

	/**
	 * 不涉及订单拨打电话
	 */
	@Override
	public Map<String, Object> salesCallNoOrder(String srId, String doctorId) {
		try {
			if (StringUtil.isEmpty(srId) || StringUtil.isEmpty(doctorId)) {
				return Util.resultMap(configCode.code_1029, null);
			}
			//根据助理ID
			Map<String, Object> salesMap = salesDao.queryPhoneById(srId, null);
			//通过id查询医生
			Map<String,Object> doctor = salesDao.queryDoctorOne(doctorId);
			if (salesMap == null || doctor == null) {
				return Util.resultMap(configCode.code_1011, null);
			}
			// 主呼号码
			String callerNum = StringUtil.isEmpty(salesMap.get("phone")) ? null
					: salesMap.get("phone").toString();

			// 被呼号码
			String calleeNum = doctor.get("docPhone").toString();
			if (StringUtil.isEmpty(callerNum) || StringUtil.isEmpty(calleeNum)) {
				return Util.resultMap(configCode.code_1011, null);
			}

			// 最长通话时长
			Integer maxCallTime = 300;

			// 提醒通话时长间隔
			Integer promptTime = 60;

			Map<String, Object> map = new HashMap<String, Object>();
			// token 先把POLYLINK_MESSAGE_TOKEN通过MD5加密，然后再获取当前年月日用之前加密的串再加密
			map.put("token", CallCenterConfig.getPhoneBasicToken());
			map.put("callerNum", callerNum);
			map.put("calleeNum", calleeNum);
			map.put("callerRouteGroupId", "");
			map.put("calleeRouteGroupId", "");
			map.put("customerDisplayNum", "");
			map.put("CustomerForeignId", "");
			map.put("maxCallTime", maxCallTime);
			map.put("promptTime", promptTime);
			map.put("promptVoice", "");
			/*
			 * map.put("type",type); map.put("fromId", srId); map.put("toId",
			 * doctorId);
			 */
			String result = SendCallCenterUtil.sendCallCenterData(map,
					CallCenterConfig.OutBoundCallOut);
			System.out.println(result);
		} catch (Exception e) {
			System.out.println(e);
		}
		return Util.resultMap(configCode.code_1001, 1);
	}

}
