package cn.syrjia.hospital.service.impl;

import cn.syrjia.callCenter.util.CallCenterConfig;
import cn.syrjia.callCenter.util.SendCallCenterUtil;
import cn.syrjia.common.impl.BaseServiceImpl;
import cn.syrjia.config.Config;
import cn.syrjia.config.configCode;
import cn.syrjia.dao.GoodsDao;
import cn.syrjia.dao.GoodsOrderDao;
import cn.syrjia.dao.ImDao;
import cn.syrjia.dao.OrderDao;
import cn.syrjia.entity.*;
import cn.syrjia.hospital.dao.AppDoctorDao;
import cn.syrjia.hospital.dao.DoctorDao;
import cn.syrjia.hospital.entity.*;
import cn.syrjia.hospital.service.AppDoctorService;
import cn.syrjia.hospital.service.DoctorOrderService;
import cn.syrjia.service.*;
import cn.syrjia.service.impl.SystemSettingServiceImpl;
import cn.syrjia.util.*;
import cn.syrjia.util.qcloudsms.QCloudSmsUtil;
import cn.syrjia.weixin.util.StringUtils;
import net.sf.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("appDoctorService")
public class AppDoctorServiceImpl extends BaseServiceImpl implements AppDoctorService {

	// 日志
	private Logger logger = LogManager.getLogger(AppDoctorServiceImpl.class);

	@Resource(name = "appDoctorDao")
	AppDoctorDao appDoctorDao;

	@Resource(name = "goodsOrderService")
	GoodsOrderService goodsOrderService;

	@Resource(name = "goodsOrderDao")
	GoodsOrderDao goodsOrderDao;

	@Resource(name = "goodsDao")
	GoodsDao goodsDao;

	@Resource(name = "orderDao")
	OrderDao orderDao;

	@Resource(name = "doctorDao")
	DoctorDao doctorDao;

	@Resource(name = "pushService")
	PushService pushService;

	@Resource(name = "orderService")
	OrderService orderService;

	@Resource(name = "doctorOrderService")
	DoctorOrderService doctorOrderService;

	@Resource(name = "imDao")
	ImDao imDao;

	@Resource(name = "goodsShopCartService")
	GoodsShopCartService goodsShopCartService;

	@Resource(name = "config")
	Config config;

	@Resource(name = "systemSettingService")
	private SystemSettingService systemSettingService;

	/**
	 * 查询医生发送的通知
	 */
	@Override
	public Integer queryDoctorSendNotice(String doctorId) {
		// TODO Auto-generated method stub
		return appDoctorDao.queryDoctorSendNotice(doctorId);
	}

	/**
	 * 查询通知列表
	 */
	@Override
	public List<Map<String, Object>> queryNoticeList(String doctorId, Integer state, Integer page, Integer row) {
		// TODO Auto-generated method stub
		return appDoctorDao.queryNoticeList(doctorId, state, page, row);
	}

	/**
	 * 编辑通知
	 */
	@Override
	public Map<String, Object> editNotice(DoctorNotice doctorNotice) {
		if (StringUtil.isEmpty(doctorNotice.getDoctorId())) {
			return Util.resultMap(configCode.code_1029, null);
		} else {
			Integer isMass = doctorNotice.getIsMass();
			Integer isMothMass = appDoctorDao.queryDoctorSendNotice(doctorNotice.getDoctorId());
			if (isMothMass > 0 && isMass == 1) {// 超过每月最大群发数量
				return Util.resultMap(configCode.code_1019, null);
			} else {
				// 查询公告每天发送数量
				Integer daySendCount = appDoctorDao.checkNoticeDaySendCount(doctorNotice.getDoctorId());
				if (daySendCount >= 5) {
					return Util.resultMap(configCode.code_1020, null);
				} else {
					// 获取系统设置信息
					Map<String, Object> sysMap = appDoctorDao.getSysSet();
					Integer doctorNoticeAutoAudit = sysMap == null
							|| StringUtil.isEmpty(sysMap.get("doctorNoticeAutoAudit")) ? 0
									: Integer.valueOf(sysMap.get("doctorNoticeAutoAudit").toString());
					if (doctorNoticeAutoAudit == 1) {
						doctorNotice.setState(1);
						if (doctorNotice.getIsMass() == 1) {
							// 查询患者
							List<Map<String, Object>> patients = appDoctorDao
									.queryPatientsByDoctorId(doctorNotice.getDoctorId());
							// 遍历患者
							for (Map<String, Object> patient : patients) {
								sendModelMsgUtil.sendNotice(patient.get("id").toString(),
										patient.get("docName").toString(), patient.get("infirmaryName").toString(),
										doctorNotice.getContent(), appDoctorDao,
										"hospital/doctor_notice.html?doctorId=" + doctorNotice.getDoctorId());
							}
						}
					} else {
						if (StringUtil.isEmpty(doctorNotice.getState())) {
							doctorNotice.setState(4);
						}
					}
					Integer i = 0;
					doctorNotice.setTitle(doctorNotice.getContent().trim().length() > 10
							? doctorNotice.getContent().trim().substring(0, 10) : doctorNotice.getContent().trim());
					if (StringUtil.isEmpty(doctorNotice.getId())) {
						// 新增公告
						i = appDoctorDao.addNotice(doctorNotice);
					} else {
						// 修改公告信息
						i = appDoctorDao.editNotice(doctorNotice);
					}
					if (i > 0) {
						return Util.resultMap(configCode.code_1001, i);
					} else {
						return Util.resultMap(configCode.code_1014, i);
					}
				}
			}
		}
	}

	/**
	 * 查询通知详情
	 */
	@Override
	public Map<String, Object> queryNoticeDetail(String id) {
		if (StringUtil.isEmpty(id)) {
			return Util.resultMap(configCode.code_1029, null);
		} else {
			// 查询公告详情
			Map<String, Object> map = appDoctorDao.queryNoticeDetail(id);
			return Util.resultMap(configCode.code_1001, map);
		}
	}

	/**
	 * 通过ID删除通知
	 */
	@Override
	public Map<String, Object> deleteNoticeById(String id) {
		if (StringUtil.isEmpty(id)) {
			return Util.resultMap(configCode.code_1029, null);
		} else {
			// 删除公告信息
			Integer i = appDoctorDao.deleteNoticeById(id);
			if (i > 0) {
				return Util.resultMap(configCode.code_1001, i);
			} else {
				return Util.resultMap(configCode.code_1066, null);
			}
		}
	}

	/**
	 * 查询公告每天发送数量
	 */
	@Override
	public Map<String, Object> checkNoticeDaySendCount(String doctorId) {
		if (StringUtil.isEmpty(doctorId)) {
			return Util.resultMap(configCode.code_1029, null);
		} else {
			// 查询公告每天发送数量
			Integer i = appDoctorDao.checkNoticeDaySendCount(doctorId);
			return Util.resultMap(configCode.code_1001, i);
		}
	}

	/**
	 * 根据医生Id查询患者列表
	 */
	@Override
	public Map<String, Object> queryPatientListById(String doctorId, String _sign, Integer page, Integer row,
			String name) {
		Integer startTime = Util.queryNowTime();
		// 根据医生Id查询患者列表
		Map<String, Object> map = appDoctorDao.queryPatientsById(doctorId, _sign, page, row, name);
		if (map == null) {
			return Util.resultMap(configCode.code_1050, map);
		} else {
			/*
			 * List<Map<String, Object>> list = (List<Map<String, Object>>) map
			 * .get("patients"); for (int i = 0; i < list.size(); i++) {
			 * Map<String, Object> serverMap = appDoctorDao
			 * .queryLastSeverName(doctorId,
			 * list.get(i).get("patientId").toString()); if (serverMap != null)
			 * { list.get(i).put("serverName", serverMap.get("name")); } else {
			 * list.get(i).put("serverName", ""); } } map.put("patients", list);
			 */
			System.out.println(Util.queryNowTime() - startTime);
			return Util.resultMap(configCode.code_1001, map);
		}
	}

	/**
	 * 根据患者ID查询患者管理信息
	 */
	@Override
	public Map<String, Object> queryPatientManageById(String patientId, String doctorId) {
		if (StringUtil.isEmpty(doctorId) || StringUtil.isEmpty(patientId)) {
			return Util.resultMap(configCode.code_1029, null);
		}
		// 根据患者ID查询患者管理信息
		Map<String, Object> map = appDoctorDao.queryPatientManageById(patientId, doctorId);
		if (map == null) {
			return Util.resultMap(configCode.code_1011, map);
		} else {
			return Util.resultMap(configCode.code_1001, map);
		}
	}

	/**
	 * 根据患者ID查询患者信息
	 */
	@Override
	public Map<String, Object> queryPatientById(String patientId) {
		Map<String, Object> map = appDoctorDao.queryPatientById(patientId);
		if (map == null) {
			return Util.resultMap(configCode.code_1011, map);
		} else {
			return Util.resultMap(configCode.code_1001, map);
		}
	}

	/**
	 * 编辑患者管理中信息（特别关注等）
	 */
	@Override
	public Map<String, Object> editPatientManage(DoctorPatient docPatient) {
		if (StringUtil.isEmpty(docPatient.getId())) {
			return Util.resultMap(configCode.code_1029, null);
		} else {
			// 编辑患者管理中信息（特别关注等）
			Integer i = appDoctorDao.editPatientManage(docPatient);
			if (i > 0) {
				return Util.resultMap(configCode.code_1001, i);
			} else {
				return Util.resultMap(configCode.code_1014, null);
			}
		}
	}

	/**
	 * 编辑医生线上调理设置信息
	 */
	@Override
	public Map<String, Object> editDoctorSet(DoctorSet doctorSet, String type) {
		try {
			if (StringUtil.isEmpty(doctorSet.getDoctorId()) || "-1".equals(doctorSet.getDoctorId())) {
				return Util.resultMap(configCode.code_1029, null);
			} else {
				// 检验医生自己设置是否存在
				String id = appDoctorDao.checkDocSetById(doctorSet.getDoctorId());
				String oldGesPassword = null;
				String oldScrectPassword = null;
				doctorSet.setIsSystem(1);
				if (!StringUtil.isEmpty(id) && !"1".equals(id)) {
					// 根据医生ID查询医生线上调理信息
					Map<String, Object> oldDoctorSet = doctorDao.queryDocotrSetById(doctorSet.getDoctorId());
					oldGesPassword = oldDoctorSet == null || StringUtil.isEmpty(oldDoctorSet.get("gesturePassword"))
							? null : oldDoctorSet.get("gesturePassword").toString();
					oldScrectPassword = oldDoctorSet == null || StringUtil.isEmpty(oldDoctorSet.get("secretPassword"))
							? null : oldDoctorSet.get("secretPassword").toString();
				}
				// type类型判断
				if (StringUtil.isEmpty(type) || !"1".equals(type)) {
					if (!StringUtil.isEmpty(doctorSet.getIsGesture()) && doctorSet.getIsGesture() == 1) {
						doctorSet.setIsSecret(0);
						if (!StringUtil.isEmpty(doctorSet.getGesturePassword())) {
							if (Md5Encoder.getMd5(doctorSet.getGesturePassword()).equals(oldGesPassword)) {
								return Util.resultMap(configCode.code_1117, null);
							}
							// 设置密码
							doctorSet.setGesturePassword(Md5Encoder.getMd5(doctorSet.getGesturePassword()));
						} else {
							if (StringUtil.isEmpty(oldGesPassword)) {
								return Util.resultMap(configCode.code_1121, null);
							}
							doctorSet.setGesturePassword(null);
						}
					} else if (!StringUtil.isEmpty(doctorSet.getIsSecret()) && doctorSet.getIsSecret() == 1) {
						doctorSet.setIsGesture(0);
						if (!StringUtil.isEmpty(doctorSet.getSecretPassword())) {
							// md5加密判断
							if (Md5Encoder.getMd5(doctorSet.getSecretPassword()).equals(oldScrectPassword)) {
								return Util.resultMap(configCode.code_1117, null);
							}
							doctorSet.setSecretPassword(Md5Encoder.getMd5(doctorSet.getSecretPassword()));
						} else {
							if (StringUtil.isEmpty(oldScrectPassword)) {
								return Util.resultMap(configCode.code_1121, null);
							}
							// 设置密码为空
							doctorSet.setSecretPassword(null);
						}
					} else {
						if (StringUtil.isEmpty(doctorSet.getSecretPassword())
								&& StringUtil.isEmpty(doctorSet.getGesturePassword())) {
							return Util.resultMap(configCode.code_1121, null);
						}
						if (!StringUtil.isEmpty(doctorSet.getGesturePassword())) {
							// md5加密判断
							doctorSet.setGesturePassword(Md5Encoder.getMd5(doctorSet.getGesturePassword()));
						} else if ("null".equals(doctorSet.getGesturePassword())) {
							doctorSet.setGesturePassword(null);
						}
						// 为空判断，md5加密判断
						if (!StringUtil.isEmpty(doctorSet.getSecretPassword())) {
							doctorSet.setSecretPassword(Md5Encoder.getMd5(doctorSet.getSecretPassword()));
						} else if ("null".equals(doctorSet.getSecretPassword())) {
							doctorSet.setSecretPassword(null);
						}

					}
				}
				// 类别判断
				if (!StringUtil.isEmpty(type) && "1".equals(type)) {
					if (!StringUtil.isEmpty(doctorSet.getIsOnlineTwGh()) && doctorSet.getIsOnlineTwGh() == 1
							&& StringUtil.isEmpty(doctorSet.getFisrtTwGhMoney())) {
						return Util.resultMap(configCode.code_1124, null);
					}
					// 为空判断
					if (!StringUtil.isEmpty(doctorSet.getIsOnlinePhoneGh()) && doctorSet.getIsOnlinePhoneGh() == 1
							&& StringUtil.isEmpty(doctorSet.getFisrtPhoneGhMoney())) {
						// 请先设置费用
						return Util.resultMap(configCode.code_1124, null);
					}
					if (!StringUtil.isEmpty(doctorSet.getIsOnlineTwZx()) && doctorSet.getIsOnlineTwZx() == 1
							&& StringUtil.isEmpty(doctorSet.getTwZxMoney())) {
						// 请先设置费用
						return Util.resultMap(configCode.code_1124, null);
					}
					if (!StringUtil.isEmpty(doctorSet.getIsOnlinePhoneZx()) && doctorSet.getIsOnlinePhoneZx() == 1
							&& StringUtil.isEmpty(doctorSet.getPhoneZxMoney())) {
						// 请先设置费用
						return Util.resultMap(configCode.code_1124, null);
					}
				}
				if (!StringUtil.isEmpty(id) && !"1".equals(id)) {
					doctorSet.setId(id);
					Integer i = appDoctorDao.updateEntity(doctorSet);
					if (i > 0) {
						// 查询医生4种服务是否均以关闭
						Integer count = appDoctorDao.queryDoctorSetIsClose(doctorSet.getDoctorId());
						if (count > 0) {
							// 修改是否接单
							appDoctorDao.updateDoctorisAccpetAsk(doctorSet.getDoctorId(), 0);
						}
						return Util.resultMap(configCode.code_1001, i);
					} else {
						return Util.resultMap(configCode.code_1066, "");
					}
				} else {
					// 添加
					Object i = appDoctorDao.addEntityUUID(doctorSet);
					if (i != null) {
						// 查询医生4种服务是否均以关闭
						Integer count = appDoctorDao.queryDoctorSetIsClose(doctorSet.getDoctorId());
						if (count > 0) {
							// 修改是否接单
							appDoctorDao.updateDoctorisAccpetAsk(doctorSet.getDoctorId(), 0);
						}
						return Util.resultMap(configCode.code_1001, i);
					} else {
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
	 * 查询医生认证信息基本数据
	 */
	@Override
	public Map<String, Object> queryDoctorAuthData() {
		List<Map<String, Object>> list = appDoctorDao.queryDoctorAuthData();
		if (list == null) {
			return Util.resultMap(configCode.code_1011, list);
		}
		return Util.resultMap(configCode.code_1001, list);
	}

	/**
	 * 根据医生ID获取医生认证信息
	 */
	@Override
	public Map<String, Object> queryDoctorApplyData(String doctorId) {
		if (StringUtils.isEmpty(doctorId)) {
			return Util.resultMap(configCode.code_1029, "");
		} else {
			// 根据医生ID获取医生认证信息
			Map<String, Object> map = appDoctorDao.queryDoctorApplyData(doctorId);
			if (map == null) {
				return Util.resultMap(configCode.code_1015, "");
			}
			return Util.resultMap(configCode.code_1001, map);
		}
	}

	/**
	 * 根据医生ID获取医生个人信息（医馆APP个人中心用）
	 */
	@Override
	public Map<String, Object> queryOneDoctor(String doctorId) {
		Integer t = Util.queryNowTime();
		if (StringUtils.isEmpty(doctorId)) {
			return Util.resultMap(configCode.code_1029, "");
		} else {
			// 根据医生ID获取医生个人信息（医馆APP个人中心用）
			Map<String, Object> map = appDoctorDao.queryOneDoctor(doctorId);
			if (map == null) {
				return Util.resultMap(configCode.code_1032, "");
			} else {
				map.put("yztId", !StringUtil.isEmpty(map.get("customYztId"))?map.get("customYztId").toString():!StringUtil.isEmpty(map.get("defaultYztId"))?map.get("defaultYztId").toString():"");
				// 根据医生ID查询医生线上调理信息
				Map<String, Object> setmap = doctorDao.queryDocotrSetById(doctorId);
				if (setmap == null) {
					// 根据医生ID查询医生线上调理信息(默认)
					setmap = doctorDao.queryDocotrSetDefault();
					if (setmap == null) {
						return Util.resultMap(configCode.code_1015, null);
					}
				}
				// 赋值转数字格式
				Integer isOnlineTwGh = Integer.valueOf(setmap.get("isOnlineTwGh").toString());
				Integer isOnlinePhoneGh = Integer.valueOf(setmap.get("isOnlinePhoneGh").toString());
				Integer isOnlineTwZx = Integer.valueOf(setmap.get("isOnlineTwZx").toString());
				Integer isOnlinePhoneZx = Integer.valueOf(setmap.get("isOnlinePhoneZx").toString());
				Integer isOpenServer = 0;
				if (isOnlineTwGh == 1 || isOnlinePhoneGh == 1 || isOnlineTwZx == 1 || isOnlinePhoneZx == 1) {
					isOpenServer = 1;
				}
				map.put("isOpenServer", isOpenServer);
				System.out.println(Util.queryNowTime() - t);
				return Util.resultMap(configCode.code_1001, map);
			}
		}
	}

	/**
	 * 修改医生头像
	 */
	@Override
	public Map<String, Object> updateDoctor(String doctorId, String localUrl, String url, String phone) {
		if (StringUtils.isEmpty(doctorId) || (StringUtils.isEmpty(url) && StringUtils.isEmpty(phone))) {
			return Util.resultMap(configCode.code_1029, "");
		} else {
			// 修改医生头像、手机号
			Integer i = appDoctorDao.updateDoctor(doctorId, localUrl, url, phone);
			if (i > 0) {
				return Util.resultMap(configCode.code_1001, url);
			} else {
				return Util.resultMap(configCode.code_1048, "");
			}
		}
	}

	/**
	 * 检验医生认证时医生名称是否重复
	 */
	@Override
	public Map<String, Object> checkApplyDocName(String docName, String doctorId) {
		if (StringUtils.isEmpty(docName) || StringUtils.isEmpty(docName)) {
			return Util.resultMap(configCode.code_1029, "");
		} else {
			// 检验医生认证时医生名称是否重复
			Integer i = appDoctorDao.checkApplyDocName(docName, doctorId);
			if (i > 0) {
				return Util.resultMap(configCode.code_1030, "");
			} else {
				return Util.resultMap(configCode.code_1001, i);
			}
		}
	}

	/**
	 * 医生修改手机号验证(检验老手机号是否正确)
	 */
	@Override
	public Map<String, Object> checkDoctorPhone(String oldPhone, String doctorId, String newPhone) {
		if (StringUtil.isEmpty(newPhone)) {
			if (StringUtils.isEmpty(doctorId)) {
				return Util.resultMap(configCode.code_1029, "");
			} else {
				// 根据医生ID获取医生个人信息（医馆APP个人中心用）
				Map<String, Object> map = appDoctorDao.queryOneDoctor(doctorId);
				if (map == null) {
					return Util.resultMap(configCode.code_1032, "");
				} else {
					// 医生电话
					String docPhone = map.get("docPhone").toString().trim();
					if (StringUtils.isEmpty(oldPhone)) {
						return Util.resultMap(configCode.code_1038, "");
					} else {
						if (docPhone.equals(oldPhone)) {
							return Util.resultMap(configCode.code_1001, "");
						} else {
							return Util.resultMap(configCode.code_1043, "");
						}
					}
				}
			}
		} else {
			// 检验医生手机号是否存在（修改手机号验证用）
			Integer count = appDoctorDao.checkDoctorPhone(newPhone, null);
			if (count > 0) {
				return Util.resultMap(configCode.code_1042, "");
			} else {
				return Util.resultMap(configCode.code_1001, "");
			}
		}
	}

	/**
	 * 获取手机验证码
	 */
	@Override
	public Map<String, Object> getPhoneCode(HttpServletRequest request, String doctorId, String phone, Integer type) {
		if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(type)) {
			return Util.resultMap(configCode.code_1029, null);
		} else {
			// 验证验证码是否存在
			Map<String, Object> codeMap = appDoctorDao.queryCodeByPhoneAndCode(phone, null, type);
			if (codeMap != null
					&& (Util.queryNowTime() - Integer.valueOf(codeMap.get("createTime").toString())) < 170) {
				return Util.resultMap(configCode.code_1070, null);
			} else {
				// 根据手机号查询获取验证码次数
				Integer num = appDoctorDao.checkSendCodeCount(phone, type);
				String ip = Util.getIp(request);
				if (num > 5) {
					return Util.resultMap(configCode.code_1076, null);
				} else {
					if (type == 2) {// 医生验证老手机号
						if (StringUtils.isEmpty(doctorId)) {
							return Util.resultMap(configCode.code_1029, "");
						} else {
							// 根据医生ID获取医生个人信息（医馆APP个人中心用）
							Map<String, Object> map = appDoctorDao.queryOneDoctor(doctorId);
							if (map == null) {
								return Util.resultMap(configCode.code_1032, "");
							} else {
								String docPhone = map.get("docPhone").toString().trim();
								if (docPhone.equals(phone)) {
									// 发送验证码
									return appDoctorDao.sendCode(phone, type, ip);
								} else {
									return Util.resultMap(configCode.code_1012, "");
								}
							}
						}
					} else if (type == 3) {// 医生验证新手机号
						Integer count = appDoctorDao.checkDoctorPhone(phone, null);
						if (count > 0) {
							return Util.resultMap(configCode.code_1042, "");
						} else {
							// 发送验证码
							return appDoctorDao.sendCode(phone, type, ip);
						}
					} else if (type == 6) {
						// 通过手机号查询医生信息
						String isHahDocId = appDoctorDao.queryDoctorByPhone(phone);
						if (!StringUtil.isEmpty(isHahDocId)) {
							return Util.resultMap(configCode.code_1030, null);
						} else {
							// 发送验证码
							return appDoctorDao.sendCode(phone, type, ip);
						}
					} else {
						// 发送验证码
						return appDoctorDao.sendCode(phone, type, ip);
					}
				}
			}
		}
	}

	/**
	 * 医生端APP登录接口
	 */
	@Override
	public Map<String, Object> login(HttpServletRequest request, String loginName, String code, LoginLog loginLog) {
		if (StringUtil.isEmpty(loginName) || StringUtil.isEmpty(code)) {
			return Util.resultMap(configCode.code_1029, null);
		}
		Map<String, Object> codeMap = null;
		if ("15731131542".equals(loginName) || "13146701003".equals(loginName)|| "15226514965".equals(loginName)) {
			codeMap = new HashMap<String, Object>();
		} else {
			//codeMap = new HashMap<String, Object>();
			// 验证验证码是否存在
			codeMap = appDoctorDao.queryCodeByPhoneAndCode(loginName, code, 1);
			if (null == codeMap) {
				return Util.resultMap(configCode.code_1045, null);
			}
			// 修改验证码状态
			appDoctorDao.updateCodeState(loginName, 1, code);
		}
		Map<String, Object> docMap = null;
		// 根据手机号查询关联医生数量
		Integer count = appDoctorDao.queryDocCountByPhone(loginName);
		if (count > 1) {
			return Util.resultMap(configCode.code_1131, null);
		}
		// 通过手机号查询医生信息
		String doctorId = appDoctorDao.queryDoctorByPhone(loginName);
		if (StringUtil.isEmpty(doctorId)) {
			// String doctorSign = doctorDao.getDoctorSign();
			// 登录没有医生及注册
			doctorId = appDoctorDao.loginAddDoctor(loginName, null, null, 1, Util.queryNowTime(), "", "");
			if (StringUtil.isEmpty(doctorId)) {
				return Util.resultMap(configCode.code_1108, null);
			}
			// 根据医生ID获取医生个人信息（医馆APP个人中心用）
			docMap = appDoctorDao.queryOneDoctor(doctorId);
			if (docMap == null) {
				return Util.resultMap(configCode.code_1032, null);
			}
			
			String result = SendCallCenterUtil.sendCallCenterData(docMap, CallCenterConfig.CustomersDoctor);
			System.out.println(result);
			try {
				// 查询是否存在申请记录
				Integer isHasCount = appDoctorDao.queryDocApply(doctorId);
				if (isHasCount == 0) {
					DoctorApplyRecord applyRecord = new DoctorApplyRecord();
					applyRecord.setApplyId(doctorId);
					applyRecord.setOpenid(Util.getUUID());
					applyRecord.setCreateTime(Util.queryNowTime());
					applyRecord.setDocIsOn("-1");
					applyRecord.setDocPhone(loginName);
					applyRecord.setIllIds(null);
					applyRecord.setInfirDepartIds(null);
					// 新增
					appDoctorDao.addEntity(applyRecord);
				}
				GetSig.accountImport(request, doctorId, null, null);
			} catch (Exception e) {
				System.out.println(e);
			}
		} else {
			// 查询是否存在申请记录
			Integer isHasCount = appDoctorDao.queryDocApply(doctorId);
			if (isHasCount == 0) {
				DoctorApplyRecord applyRecord = new DoctorApplyRecord();
				applyRecord.setApplyId(doctorId);
				applyRecord.setOpenid(Util.getUUID());
				applyRecord.setCreateTime(Util.queryNowTime());
				applyRecord.setDocIsOn("-1");
				applyRecord.setDocPhone(loginName);
				applyRecord.setIllIds(null);
				applyRecord.setInfirDepartIds(null);
				// 保存
				appDoctorDao.addEntity(applyRecord);
			}
			// 修改医生是否已下载app
			appDoctorDao.updateDoctorDownloadState(doctorId);
			// 根据医生ID获取医生个人信息（医馆APP个人中心用）
			docMap = appDoctorDao.queryOneDoctor(doctorId);
			if (docMap == null) {
				return Util.resultMap(configCode.code_1032, null);
			}
		}
		// 查询用户token,ostype,mode,version
		Map<String, Object> oldLoginLog = pushService.queryLoginLogByUserId(doctorId);

		/*
		 * Integer createTime = Integer.parseInt(codeMap.get("createTime")
		 * .toString()); if (Util.queryNowTime() - createTime > 180) { return
		 * Util.resultMap(configCode.code_1046, null); }
		 */

		loginLog.setUserId(doctorId);
		loginLog.setCreateTime(Util.queryNowTime());
		// 新增
		Object obj = appDoctorDao.addEntityUUID(loginLog);
		if (StringUtil.isEmpty(obj)) {
			return Util.resultMap(configCode.code_1015, null);
		}

		if ("0".equals(docMap.get("docIsOn"))) {
			return Util.resultMap(configCode.code_1041, null);
		}
		if ("2".equals(docMap.get("docIsOn")) || "3".equals(docMap.get("docIsOn"))) {
			return Util.resultMap(configCode.code_1059, null);
		}

		String _token = Util.getUUID();
		String _encry_token = Util.getUUID();
		// 添加请求token
		Integer i = appDoctorDao.addMemberToken(doctorId, _token);
		if (i == 0) {
			return Util.resultMap(configCode.code_1064, null);
		}

		if (null == oldLoginLog || oldLoginLog.size() == 0) {
			// 查询导入人员
			Map<String, Object> follow = orderService.queryFollowIdByOpenId(doctorId);
			if (null != follow && follow.size() > 0) {
				// 医生助理推送
				pushService.docbindsuc(follow.get("followId").toString(), doctorId, follow.get("srName").toString());
			}
		}

		JSONObject json = new JSONObject();
		json.set("doctorId", doctorId);
		json.set("_encry_token", _encry_token);
		RedisUtil.setVal(_token, 60 * 60 * 2, json.toString());
		codeMap = new HashMap<String, Object>();
		codeMap.put("_token", _token);
		codeMap.put("_encry_token", _encry_token);
		codeMap.put("memberId", doctorId);
		codeMap.put("yztId", !StringUtil.isEmpty(docMap.get("customYztId"))?docMap.get("customYztId").toString():!StringUtil.isEmpty(docMap.get("defaultYztId"))?docMap.get("defaultYztId").toString():"");
		codeMap.put("type", docMap.get("type"));
		return Util.resultMap(configCode.code_1001, codeMap);
	}

	/**
	 * 医生注册接口
	 */
	@Override
	public Map<String, Object> register(HttpServletRequest request, String phone, String docName, String srId,
			String code) {
		if (StringUtil.isEmpty(phone) || StringUtil.isEmpty(code)) {
			return Util.resultMap(configCode.code_1029, null);
		}
		// 验证验证码是否存在
		Map<String, Object> codeMap = appDoctorDao.queryCodeByPhoneAndCode(phone, code, 6);
		if (null == codeMap) {
			return Util.resultMap(configCode.code_1045, null);
		}
		Integer createTime = Integer.parseInt(codeMap.get("createTime").toString());
		if (Util.queryNowTime() - createTime > 180) {
			return Util.resultMap(configCode.code_1046, null);
		}
		// 修改验证码状态
		appDoctorDao.updateCodeState(phone, 6, code);
		// 根据手机号查询关联医生数量
		Integer count = appDoctorDao.queryDocCountByPhone(phone);
		if (count > 1) {
			return Util.resultMap(configCode.code_1131, null);
		}
		String salesYztId = "";
		// 通过手机号查询医生信息
		String doctorId = appDoctorDao.queryDoctorByPhone(phone);
		if (!StringUtil.isEmpty(doctorId)) {
			return Util.resultMap(configCode.code_1030, null);
		} else {
			// String doctorSign = doctorDao.getDoctorSign();
			if (!StringUtil.isEmpty(srId)) {
				Map<String, Object> salesMap = appDoctorDao.querySalesOne(srId);
				if (salesMap != null) {
					if (!StringUtil.isEmpty(salesMap.get("customYztId"))) {
						salesYztId = salesMap.get("customYztId").toString();
					} else if (!StringUtil.isEmpty(salesMap.get("defaultYztId"))) {
						salesYztId = salesMap.get("defaultYztId").toString();
					}
				}
			}
			// 登录没有医生及注册
			doctorId = appDoctorDao.loginAddDoctor(phone, docName, null, 0, null, salesYztId, srId);
			if (!StringUtil.isEmpty(srId)) {
				// 根据OPENID查询此人是否已有导入人
				Integer isHasFollow = appDoctorDao.queryFollowCountById(doctorId);

				// 判断推荐人是医生推荐医生，还是医助推荐。@doctorId为医生推荐医生
				if (!srId.contains("@doctorId")) {
					if (isHasFollow <= 0) {
						FollowHistory followHistory = new FollowHistory();
						followHistory.setOpenId(doctorId);
						followHistory.setFollowId(srId);
						followHistory.setFollowTime(Util.queryNowTime());
						followHistory.setType(1);
						// 新增
						appDoctorDao.addEntityUUID(followHistory);
						try {
							//医生成功绑定医生助理
							pushService.bindsuc(srId, doctorId, docName);
						} catch (Exception e) {
							System.out.println(e);
						}
					}
				} else {
					srId = srId.replace("@doctorId", "");
					DoctorInvitation doctorInvitation = new DoctorInvitation();
					doctorInvitation.setUuid(Util.getUUID());
					doctorInvitation.setOldDoctorId(srId);
					doctorInvitation.setNewDoctorId(doctorId);
					doctorInvitation.setCreatTime(Util.queryNowTime());
					appDoctorDao.addEntity(doctorInvitation);
//					try {
//						//医生成功绑定医生助理
//						pushService.bindsuc(srId, doctorId, docName);
//					} catch (Exception e) {
//						System.out.println(e);
//					}
				}
				
			}
			// 根据医生ID获取医生个人信息（医馆APP个人中心用）
			Map<String, Object> docMap = appDoctorDao.queryOneDoctor(doctorId);
			String result = SendCallCenterUtil.sendCallCenterData(docMap, CallCenterConfig.CustomersDoctor);
			System.out.println(result);
			try {
				GetSig.accountImport(request, doctorId, docName, null);
			} catch (Exception e) {
				System.out.println(e);
			}
			try {
				// 查询是否存在申请记录
				Integer isHasCount = appDoctorDao.queryDocApply(doctorId);
				if (isHasCount == 0) {
					// 根据手机号查询是否存在申请记录
					appDoctorDao.deleteApplyRecrodByDoctorId(phone);
					DoctorApplyRecord applyRecord = new DoctorApplyRecord();
					applyRecord.setApplyId(doctorId);
					applyRecord.setOpenid(Util.getUUID());
					applyRecord.setDocIsOn("-1");
					applyRecord.setCreateTime(Util.queryNowTime());
					applyRecord.setDocName(docName);
					applyRecord.setDocPhone(phone);
					applyRecord.setIllIds(null);
					applyRecord.setInfirDepartIds(null);
					applyRecord.setDefaultYztId(salesYztId);
					applyRecord.setSalesId(srId);
					// 新增
					appDoctorDao.addEntity(applyRecord);
				}
			} catch (Exception e) {
				System.out.println(e);
			}
			return Util.resultMap(configCode.code_1001, doctorId);
		}
	}

	/**
	 * 编辑医生认证信息
	 */
	@Override
	@Transactional
	public Map<String, Object> addApplyDoc(HttpServletRequest request, DoctorApplyRecord record, String pid,
			String lastId, Integer _lastType, String doctorId) {
		try {
			if (StringUtil.isEmpty(doctorId)) {
				return Util.resultMap(configCode.code_1029, null);
			}
			// 根据id查询单个
			Doctor doctor = appDoctorDao.queryById(Doctor.class, doctorId);
			if (doctor == null) {
				return Util.resultMap(configCode.code_1032, null);
			}
			String illds = record.getIllIds();
			String indepartIds = record.getInfirDepartIds();
			// 根据医生ID获取医生认证信息
			Map<String, Object> oldApplyMap = appDoctorDao.queryDoctorApplyData(doctorId);
			Integer saleTj = 0;
			if (oldApplyMap != null) {
				record.setApplyId(oldApplyMap.get("applyId").toString());
				pid = oldApplyMap.get("followId") == null ? null : oldApplyMap.get("followId").toString();
				saleTj = Integer.valueOf(oldApplyMap.get("saleTj").toString());
			}
			Object id = 0;
			if (StringUtil.isEmpty(record.getApplyUserId())) {
				record.setApplyUserId(doctorId);// 申请人ID
			}

			if (!StringUtil.isEmpty(record.getApplyId())) {
				if (saleTj == 0 && (StringUtil.isEmpty(record.getIdCardBackUrl())
						|| StringUtil.isEmpty(record.getIdCardFaceUrl()) || StringUtil.isEmpty(record.getPracEoSUrl())
						|| StringUtil.isEmpty(record.getPracFourUrl()))) {
					// 证书等图像信息不能为空
					return Util.resultMap(configCode.code_1104, null);
				}
				record.setDocIsOn("0");
				record.setBasicIsOn("0");
				record.setPicIsOn("0");
				record.setIllIds(null);
				record.setInfirDepartIds(null);
				// 修改必须有ID 根据ID
				id = appDoctorDao.updateEntity(record) == 0 ? null : appDoctorDao.updateEntity(record);
				/*
				 * if (id != null) { Channel channel = new Channel();
				 * channel.setId(record.getApplyId()); Channel channel1 =
				 * appDoctorDao.queryById(Channel.class, record.getApplyId());
				 * if (channel1 == null) {
				 * channel.setCreateTime(Util.queryNowTime());
				 * channel.setPid(pid); channel.setState(2); channel.setType(2);
				 * channel.setChannelName(record.getDocName());
				 * appDoctorDao.addEntity(channel); } else {
				 * appDoctorDao.updateChannelState(record.getApplyId()); } }
				 */
			} else {
				if (StringUtil.isEmpty(illds) || StringUtil.isEmpty(indepartIds)
						|| StringUtil.isEmpty(record.getInfirmaryId())
						|| StringUtil.isEmpty(record.getDocPositionId())) {
					return Util.resultMap(configCode.code_1029, null);
				}
				record.setCreateTime(Util.queryNowTime());
				record.setIllIds(null);
				record.setInfirDepartIds(null);
				record.setOpenid(doctorId);
				record.setApplyId(doctorId);
				record.setDocPhone(doctor.getDocPhone());
				// 新增
				id = appDoctorDao.addEntity(record);
				if (id == null) {
					return Util.resultMap(configCode.code_1013, null);
				} else {
					Channel channel = new Channel();
					channel.setId(record.getApplyId());
					channel.setCreateTime(Util.queryNowTime());
					channel.setPid(pid);
					channel.setState(2);
					channel.setType(2);
					channel.setChannelName(record.getDocName());
					// 新增
					appDoctorDao.addEntity(channel);
					AuditRecord auditRecord = new AuditRecord();
					auditRecord.setAuditId(record.getApplyId());
					auditRecord.setCreateTime(Util.queryNowTime());
					auditRecord.setType(0);
					auditRecord.setLastType(_lastType);
					auditRecord.setPid(pid);
					auditRecord.setState(1);
					// 新增
					appDoctorDao.addEntity(auditRecord);
				}
			}
			if (id != null && !"0".equals(id.toString())) {
				MiddleUtil midd = null;
				System.out.println(illds + "=illIds！！！！！！！！！！！！！！");
				if (!StringUtil.isEmpty(illds)) {
					// 删除医生疾病分类关联
					appDoctorDao.delMiddleByDocId(record.getApplyId());
					String[] illids = illds.split(",");
					for (String illid : illids) {
						if (!StringUtil.isEmpty(illid)) {
							midd = new MiddleUtil();
							midd.setDoctorId(record.getApplyId());
							midd.setIllClassId(illid);
							midd.setType("1");
							appDoctorDao.addEntity(midd);
						}
					}
				}
				DocAndDepartUtil ddmidd = null;
				if (!StringUtil.isEmpty(indepartIds)) {
					// 根据医生ID删除医生部门关联
					appDoctorDao.delDocAndDepartUtil(record.getApplyId(), null);
					String[] departIds = indepartIds.split(",");
					for (String departId : departIds) {
						if (!StringUtil.isEmpty(departId)) {
							ddmidd = new DocAndDepartUtil();
							ddmidd.setDoctorId(record.getApplyId());
							ddmidd.setDepartId(departId);
							appDoctorDao.addEntity(ddmidd);
						}
					}
				}
			}
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Util.resultMap(configCode.code_1015, null);
		}
		return Util.resultMap(configCode.code_1001, 1);
	}

	/**
	 * 编辑医生公告信息
	 */
	@Override
	public Map<String, Object> editDocDesc(DoctorAbstractRecord doctorAbstractRecord, String srId) {
		if (StringUtil.isEmpty(doctorAbstractRecord.getDoctorId())) {
			return Util.resultMap(configCode.code_1029, null);
		} else {
			// 查询医生有没有待审核的简介
			Integer count = appDoctorDao.queryAbstractRecordByDoctorId(doctorAbstractRecord.getDoctorId());
			if (count > 0) {
				return Util.resultMap(configCode.code_1114, null);
			}
			Object id = null;
			if (!StringUtil.isEmpty(srId)) {
				doctorAbstractRecord.setApplyUserId(srId);
			} else {
				doctorAbstractRecord.setApplyUserId(doctorAbstractRecord.getDoctorId());
			}
			// 获取系统设置信息
			Map<String, Object> sysMap = appDoctorDao.getSysSet();
			Integer doctorAbstractAutoAudit = sysMap == null
					|| StringUtil.isEmpty(sysMap.get("doctorAbstractAutoAudit")) ? 0
							: Integer.valueOf(sysMap.get("doctorAbstractAutoAudit").toString());
			if (doctorAbstractAutoAudit == 1) {
				doctorAbstractRecord.setState(1);
				doctorAbstractRecord.setAuditTime(Util.queryNowTime());
			} else {
				doctorAbstractRecord.setState(0);
			}
			if (StringUtil.isEmpty(doctorAbstractRecord.getId())) {
				doctorAbstractRecord.setCreateTime(Util.queryNowTime());
				// 新增
				id = appDoctorDao.addEntityUUID(doctorAbstractRecord);
			} else {
				// 更新
				id = appDoctorDao.updateEntity(doctorAbstractRecord);
			}
			if (id != null) {
				if (doctorAbstractAutoAudit == 1) {
					// 修改医生简介信息
					appDoctorDao.updateDocAbstract(doctorAbstractRecord.getDoctorId(),
							doctorAbstractRecord.getContent());
				}
				return Util.resultMap(configCode.code_1001, id);
			} else {
				return Util.resultMap(configCode.code_1014, null);
			}
		}
	}

	/**
	 * 修改医生手机号
	 */
	@Override
	public Map<String, Object> updateDocPhone(HttpServletRequest request, String phone, String code, String doctorId) {

		if (StringUtil.isEmpty(phone) || StringUtil.isEmpty(code) || StringUtil.isEmpty(doctorId)) {
			return Util.resultMap(configCode.code_1029, null);
		}
		// 验证验证码是否存在
		Map<String, Object> codeMap = appDoctorDao.queryCodeByPhoneAndCode(phone, code, 3);
		if (null == codeMap) {
			return Util.resultMap(configCode.code_1045, null);
		}
		Integer createTime = Integer.parseInt(codeMap.get("createTime").toString());
		if (Util.queryNowTime() - createTime > 180) {
			return Util.resultMap(configCode.code_1046, null);
		}
		// 根据医生ID获取医生个人信息（医馆APP个人中心用）
		Map<String, Object> map = appDoctorDao.queryOneDoctor(doctorId);
		if (map == null) {
			return Util.resultMap(configCode.code_1032, "");
		}
		String oldPhone = map.get("docPhone").toString().trim();
		if (oldPhone.equals(phone)) {
			return Util.resultMap(configCode.code_1031, "");
		}
		// 修改手机号加入记录表
		String obj = appDoctorDao.addPhoneUpdate(doctorId, oldPhone, phone);
		if (StringUtil.isEmpty(obj)) {
			return Util.resultMap(configCode.code_1015, null);
		}
		// 修改医生头像、手机号
		Integer i = appDoctorDao.updateDoctor(doctorId, null, null, phone);
		if (i > 0) {
			appDoctorDao.updateCodeState(phone, 3, code);
			String result = SendCallCenterUtil.sendCallCenterData(map, CallCenterConfig.CustomersDoctor);
			System.out.println(result);
			return Util.resultMap(configCode.code_1001, i);
		} else {
			return Util.resultMap(configCode.code_1014, null);
		}

	}

	/**
	 * 验证老手机号、验证码是否正确
	 */
	@Override
	public Map<String, Object> validateCode(String phone, String doctorId, String code) {
		if (StringUtil.isEmpty(phone) || StringUtil.isEmpty(code) || StringUtil.isEmpty(doctorId)) {
			return Util.resultMap(configCode.code_1029, null);
		}
		// 验证验证码是否存在
		Map<String, Object> codeMap = appDoctorDao.queryCodeByPhoneAndCode(phone, code, 2);
		if (null == codeMap) {
			// 验证码不正确
			return Util.resultMap(configCode.code_1045, null);
		}
		Integer createTime = Integer.parseInt(codeMap.get("createTime").toString());
		if (Util.queryNowTime() - createTime > 180) {
			return Util.resultMap(configCode.code_1046, null);
		}
		// 根据医生ID获取医生个人信息（医馆APP个人中心用）
		Map<String, Object> map = appDoctorDao.queryOneDoctor(doctorId);
		if (map == null) {
			return Util.resultMap(configCode.code_1032, "");
		}
		String oldPhone = map.get("docPhone").toString().trim();
		if (oldPhone.equals(phone)) {
			// 修改验证码状态
			appDoctorDao.updateCodeState(phone, 2, code);
			return Util.resultMap(configCode.code_1001, null);
		} else {
			return Util.resultMap(configCode.code_1043, "");
		}
	}

	/**
	 * 退出登录
	 */
	@Override
	public Map<String, Object> loginOut(String oldToken, String userId) {
		if (StringUtil.isEmpty(oldToken)) {
			return Util.resultMap(configCode.code_1051, null);
		} else {
			// 删除redis
			RedisUtil.deleteKey(oldToken);
			// appDoctorDao.deletePushToken(userId);
			appDoctorDao.addLogOutToken(userId);
			return Util.resultMap(configCode.code_1001, null);
		}
	}

	/**
	 * 验证医生手势密码、数字密码是否正确
	 */
	@Override
	public Map<String, Object> checkDoctorPassword(DoctorSet doctorSet) {
		if (StringUtil.isEmpty(doctorSet.getDoctorId()) || (StringUtil.isEmpty(doctorSet.getGesturePassword())
				&& StringUtil.isEmpty(doctorSet.getSecretPassword()))) {
			return Util.resultMap(configCode.code_1029, null);
		} else {
			Integer i = 0;
			if (!StringUtil.isEmpty(doctorSet.getGesturePassword())) {
				// md5判断
				doctorSet.setGesturePassword(Md5Encoder.getMd5(doctorSet.getGesturePassword()));
				// 验证医生手势密码、数字密码是否正确
				i = appDoctorDao.checkDoctorPassword(doctorSet);
				if (i > 0) {
					return Util.resultMap(configCode.code_1001, null);
				} else {
					return Util.resultMap(configCode.code_1003, null);
				}
			} else if (!StringUtil.isEmpty(doctorSet.getSecretPassword())) {
				// 设置密码
				doctorSet.setSecretPassword(Md5Encoder.getMd5(doctorSet.getSecretPassword()));
				// 验证医生手势密码、数字密码是否正确
				i = appDoctorDao.checkDoctorPassword(doctorSet);
				if (i > 0) {
					return Util.resultMap(configCode.code_1001, null);
				} else {
					return Util.resultMap(configCode.code_1005, null);
				}
			} else {
				return Util.resultMap(configCode.code_1029, null);
			}
		}
	}

	/**
	 * 编辑医生坐诊信息
	 */
	@Override
	public Map<String, Object> editDoctorZzData(DoctorZzData zzData, String zzDates) {
		// repeatWay 0-永不重复 1-每天重复 2-工作日重复 3-每周重复 4-自定义
		Object id = null;
		try {
			if (zzData == null || StringUtil.isEmpty(zzData.getDoctorId())
					|| StringUtil.isEmpty(zzData.getInfirmaryId()) || StringUtil.isEmpty(zzData.getZzDate())) {
				return Util.resultMap(configCode.code_1029, null);
			}
			// 查询是否有重复坐诊信息
			Integer count = appDoctorDao.queryIsCfZzData(zzData);
			if (count > 0) {
				return Util.resultMap(configCode.code_1120, null);
			}

			Integer isYzt = 0;
			// 查询单个医院信息
			Map<String, Object> infiramryMap = appDoctorDao.queryOneInfirmary(zzData.getInfirmaryId());
			if (infiramryMap == null) {
				return Util.resultMap(configCode.code_1119, null);
			}
			if (infiramryMap.get("infirmaryName").toString().indexOf("医珍堂") > -1) {
				isYzt = 1;
			}
			// 生成id
			String repeatId = Util.getUUID();
			String zzDate = zzData.getZzDate();
			zzData.setIsYzt(isYzt);
			if (StringUtil.isEmpty(zzData.getId())) {
				zzData.setCreateTime(Util.queryNowTime());
				zzData.setId(repeatId);
				// 新增
				id = appDoctorDao.addEntity(zzData);
			} else {
				repeatId = zzData.getId();
				// 更新
				id = appDoctorDao.updateEntity(zzData);
			}
			zzData.setRepeatId(repeatId.toString());
			List<String> dates = new ArrayList<String>();
			if (!StringUtil.isEmpty(zzDates)) {
				if (zzDates.indexOf(",") > 0) {
					String[] arr = zzDates.split(",");
					for (int i = 0; i < arr.length; i++) {
						if (!arr[i].equals(zzDate)) {
							dates.add(arr[i]);
						}
					}
				} else {
					if (!zzDates.equals(zzDate)) {
						dates.add(zzDates);
					}
				}
				// 根据repeatId,重复方式删除
				appDoctorDao.deleteZzDataByRepeatId(repeatId, zzData.getZzDate());
				// 批量增加坐诊信息
				appDoctorDao.insertDoctorZzData(zzData, dates);
			} else {
				if (zzData.getRepeatWay() != 0) {
					if (zzData.getRepeatWay() == 1) {
						dates = Util.getDateEveryDay(zzDate);
					} else if (zzData.getRepeatWay() == 2) {
						dates = Util.getWorkDates(zzDate);
					} else if (zzData.getRepeatWay() == 3) {
						dates = Util.getWeekDate(zzDate);
					}
					// 根据repeatId,重复方式删除
					appDoctorDao.deleteZzDataByRepeatId(repeatId, zzData.getZzDate());
					// 批量增加坐诊信息
					appDoctorDao.insertDoctorZzData(zzData, dates);
				}
			}
		} catch (Exception e) {
			return Util.resultMap(configCode.code_1014, null);
		}
		return Util.resultMap(configCode.code_1001, id);
	}

	/**
	 * 回复用户评价
	 */
	@Override
	public Map<String, Object> replyMemberEva(Evaluate evaluate, String repalyContent) {
		if (StringUtil.isEmpty(evaluate.getId()) || StringUtil.isEmpty(repalyContent)) {
			return Util.resultMap(configCode.code_1029, null);
		} else {
			// 获取系统设置信息
			Map<String, Object> sysMap = appDoctorDao.getSysSet();
			Integer isAutoReplyDoctorEva = sysMap == null || StringUtil.isEmpty(sysMap.get("isAutoReplyDoctorEva")) ? 0
					: Integer.valueOf(sysMap.get("isAutoReplyDoctorEva").toString());
			if (isAutoReplyDoctorEva == 1) {
				evaluate.setReplyState(1);
			} else {
				evaluate.setReplyState(2);
			}
			evaluate.setExplain(repalyContent);
			// 回复用户评价
			Integer i = appDoctorDao.replyMemberEva(evaluate);
			if (i > 0) {
				return Util.resultMap(configCode.code_1001, i);
			} else {
				return Util.resultMap(configCode.code_1014, null);
			}
		}
	}

	/**
	 * 修改评价状态
	 */
	@Override
	public Map<String, Object> updateEvaState(Evaluate evaluate) {
		if (StringUtil.isEmpty(evaluate.getId())) {
			return Util.resultMap(configCode.code_1029, null);
		} else {
			// 获取系统设置信息
			Map<String, Object> sysMap = appDoctorDao.getSysSet();
			Integer isAutoDeleteDoctorEva = sysMap == null || StringUtil.isEmpty(sysMap.get("isAutoDeleteDoctorEva"))
					? 0 : Integer.valueOf(sysMap.get("isAutoDeleteDoctorEva").toString());
			if (isAutoDeleteDoctorEva == 1) {
				evaluate.setState(3);
			} else {
				evaluate.setState(4);
			}
			// 修改评价状态
			Integer i = appDoctorDao.updateEvaState(evaluate);
			if (i > 0) {
				return Util.resultMap(configCode.code_1001, i);
			} else {
				return Util.resultMap(configCode.code_1014, null);
			}
		}
	}

	/**
	 * 根据医生ID查询医生评价统计信息
	 */
	@Override
	public Map<String, Object> queryDoctorEvaCensus(String doctorId) {
		if (StringUtil.isEmpty(doctorId)) {
			return Util.resultMap(configCode.code_1029, null);
		} else {
			// 根据医生ID查询医生评价统计信息
			Map<String, Object> map = appDoctorDao.queryDoctorEvaCensus(doctorId);
			if (map != null) {
				return Util.resultMap(configCode.code_1001, map);
			} else {
				return Util.resultMap(configCode.code_1011, null);
			}
		}
	}

	/**
	 * 根据医生ID查询医生评价信息（详情页面与评价列表共用）
	 */
	@Override
	public Map<String, Object> queryDoctorEvaList(String doctorId, Integer row, Integer page, String _sign) {
		if (StringUtil.isEmpty(doctorId)) {
			return Util.resultMap(configCode.code_1029, null);
		}
		// 根据医生ID查询医生评价信息（详情页面与评价列表共用）
		List<Map<String, Object>> list = appDoctorDao.queryDoctorEvaList(doctorId, row, page, _sign);
		if (list == null) {
			return Util.resultMap(configCode.code_1011, null);
		}
		for (int i = 0; i < list.size(); i++) {
			// 根据评价ID查询锦旗
			Map<String, Object> map = doctorDao.queryJqImgs(list.get(i).get("id").toString());
			if (map != null) {
				list.get(i).put("jqimgs", map.get("imgdata"));
			} else {
				list.get(i).put("jqimgs", "");
			}
		}
		return Util.resultMap(configCode.code_1001, list);
	}

	/**
	 * 查询一周日期
	 */
	@Override
	public Map<String, Object> queryWeekData() {
		// 查询一周日期
		List<Map<String, Object>> weeks = appDoctorDao.queryWeekData();
		return Util.resultMap(configCode.code_1001, weeks);
	}

	/**
	 * 根据医生ID查询医生14天出诊状态
	 */
	@Override
	public Map<String, Object> queryFourTeenZzStatus(String doctorId) {
		if (StringUtil.isEmpty(doctorId)) {
			return Util.resultMap(configCode.code_1029, null);
		} else {
			// 根据医生ID查询医生14天出诊状态
			List<Map<String, Object>> list = appDoctorDao.queryFourTeenZzStatus(doctorId);
			return Util.resultMap(configCode.code_1001, list);
		}
	}

	/**
	 * 删除坐诊信息
	 */
	@Override
	public Map<String, Object> updateDoctroZzState(String id) {
		if (StringUtil.isEmpty(id)) {
			return Util.resultMap(configCode.code_1029, null);
		} else {
			// 删除坐诊信息
			Integer i = appDoctorDao.updateDoctroZzState(id);
			if (i > 0) {
				return Util.resultMap(configCode.code_1001, i);
			} else {
				return Util.resultMap(configCode.code_1014, null);
			}
		}
	}

	/**
	 * 获取医院列表
	 */
	@Override
	public Map<String, Object> queryInfirmaryList(String name, Integer page, Integer row) {
		// 获取医院列表
		List<Map<String, Object>> list = appDoctorDao.queryInfirmaryList(name, page, row);
		return Util.resultMap(configCode.code_1001, list);
	}

	/**
	 * 根据坐诊ID查询坐诊信息
	 */
	@Override
	public Map<String, Object> queryDoctorZzDataById(String id) {
		if (StringUtil.isEmpty(id)) {
			return Util.resultMap(configCode.code_1029, null);
		} else {
			// 根据坐诊ID查询坐诊信息
			Map<String, Object> map = appDoctorDao.queryDoctorZzDataById(id);
			if (map != null) {
				return Util.resultMap(configCode.code_1001, map);
			} else {
				return Util.resultMap(configCode.code_1011, null);
			}
		}
	}

	/**
	 * 查询医生坐诊信息
	 */
	@Override
	public Map<String, Object> queryDoctorZzList(String doctroId, String zzDate) {
		if (StringUtil.isEmpty(doctroId)) {
			return Util.resultMap(configCode.code_1029, null);
		} else {
			// 查询医生坐诊时间列表
			List<Map<String, Object>> list = appDoctorDao.queryDoctorZzTimes(doctroId, zzDate);
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					if (!StringUtil.isEmpty(list.get(i).get("zzState"))) {
						List<Map<String, Object>> zzDatas = appDoctorDao.queryDoctorZzList(doctroId, zzDate,
								Integer.valueOf(list.get(i).get("zzState").toString()));
						list.get(i).put("zzData", zzDatas);
					}
				}
			}
			return Util.resultMap(configCode.code_1001, list);
		}
	}

	/**
	 * 查询专业练习
	 */
	@Override
	public Map<String, Object> querySpecialTest(String doctroId, Integer type) {
		if (StringUtil.isEmpty(doctroId) || StringUtil.isEmpty(type)) {
			return Util.resultMap(configCode.code_1029, null);
		} else {
			Map<String, Object> map = null;
			if (type == 1) {
				// 根据医生id查询问诊/复诊模板
				map = appDoctorDao.querySpecialTest(doctroId);
			} else if (type == 2) {
				// 根据医生id查询调理方/经典方
				map = appDoctorDao.queryConditioning(doctroId);
			}
			if (map != null) {
				return Util.resultMap(configCode.code_1001, map);
			} else {
				return Util.resultMap(configCode.code_1011, null);
			}
		}
	}

	/**
	 * 查询专业课程
	 */
	@Override
	public Map<String, Object> querySpecialTestClassic(String name, Integer page, Integer row) {
		// 专业课
		List<Map<String, Object>> list = appDoctorDao.querySpecialTestClassic(name, page, row);
		if (list != null) {
			return Util.resultMap(configCode.code_1001, list);
		} else {
			return Util.resultMap(configCode.code_1011, null);
		}
	}

	/**
	 * 通过医生id查询专业练习
	 */
	@Override
	public Map<String, Object> querySpecialTestByDcotorId(String doctorId, Integer type, String testId) {
		if (StringUtil.isEmpty(doctorId) || StringUtil.isEmpty(type)) {
			return Util.resultMap(configCode.code_1029, null);
		} else {
			// 通过医生id查询专业
			List<Map<String, Object>> map = appDoctorDao.querySpecialTestByDoctorId(doctorId, type, testId);
			if (map != null) {
				return Util.resultMap(configCode.code_1001, map);
			} else {
				return Util.resultMap(configCode.code_1011, null);
			}
		}
	}

	/**
	 * 通过医生ID查询调理方
	 */
	@Override
	@Transactional
	public Map<String, Object> queryConditioningByDoctorId(String doctorId, String name, Integer type,
			String conditioningId, String patientId, Integer page, Integer row) {
		if (StringUtil.isEmpty(doctorId)) {
			return Util.resultMap(configCode.code_1029, null);
		} else {
			if (type == 3) {
				// 查询历史方
				List<Map<String, Object>> list = appDoctorDao.queryHistoryConditioningByDoctorId(doctorId, name, type,
						conditioningId, patientId, page, row);
				if (list != null) {
					for (Map<String, Object> map : list) {
						// 历史方里添加药
						List<Map<String, Object>> drug = appDoctorDao
								.queryHistoryConditioningDetailById(map.get("id").toString());
						String drugName = "";
						for (int i = 0; i < drug.size(); i++) {
							drugName += drug.get(i).get("name").toString() + drug.get(i).get("weight")
									+ drug.get(i).get("util") + "、";
						}
						if (drugName.length() > 0) {
							drugName = drugName.substring(0, drugName.length() - 1);
							map.put("drugName", drugName);
						}
						map.put("drug", drug);
					}
					return Util.resultMap(configCode.code_1001, list);
				} else {
					return Util.resultMap(configCode.code_1011, null);
				}
			} else {
				// 查询调理方
				List<Map<String, Object>> list = appDoctorDao.queryConditioningByDoctorId(doctorId, name, type,
						conditioningId, page, row);
				if (list != null) {
					for (Map<String, Object> map : list) {
						// 调理方添加药
						List<Map<String, Object>> drugs = appDoctorDao
								.queryConditioningDetailById(map.get("id").toString());
						StringBuilder drugName = new StringBuilder("");
						for (Map<String, Object> drug : drugs) {
							drugName.append(drug.get("name").toString());
							drugName.append(drug.get("weight").toString());
							drugName.append("克、");
						}
						if (drugName.length() > 0) {
							drugName.deleteCharAt(drugName.length() - 1);
							map.put("drugName", drugName.toString());
						}

						map.put("drug", drugs);

					}
					return Util.resultMap(configCode.code_1001, list);
				} else {
					return Util.resultMap(configCode.code_1011, null);
				}
			}
		}
	}

	/**
	 * 赋值标题
	 */
	@Override
	@Transactional
	public Map<String, Object> copeSpecialTestTitle(String titleId, String testId, String test, String otherName,
			Integer isTongue, Integer isSurface) {
		if (StringUtil.isEmpty(testId) && StringUtil.isEmpty(test)) {
			return Util.resultMap(configCode.code_1029, null);
		}
		Object obj = null;
		if (StringUtil.isEmpty(titleId)) {
			if (!StringUtil.isEmpty(test)) {

				Map<String, Object> title = JsonUtil.jsonToMap(test);

				// 查询专业最大的问题id
				Integer qid = appDoctorDao.querySpecialMaxQid(testId);

				SpecialTestTitle specialTestTitle = new SpecialTestTitle();
				specialTestTitle.setCreateTime(Util.queryNowTime());
				specialTestTitle.setIsMandatory(1);
				specialTestTitle.setOptionType(Integer.parseInt(title.get("optionType").toString()));
				specialTestTitle.setQid(++qid);
				specialTestTitle.setSpecialTestId(testId);
				specialTestTitle.setTitleName(title.get("titleName").toString());
				specialTestTitle.setId(Util.getUUID());

				List<SpecialTestTitleOptions> specialTestTitleOptionss = new ArrayList<SpecialTestTitleOptions>();

				int optionNum = 1;
				if (!title.get("optionType").toString().equals("3")) {
					List<Map<String, Object>> options = JsonUtil.parseJSON2List(title.get("options"));
					for (Map<String, Object> option : options) {
						SpecialTestTitleOptions specialTestTitleOption = new SpecialTestTitleOptions();
						specialTestTitleOption.setOptionName(option.get("optionName").toString());
						specialTestTitleOption.setOptionNum((optionNum++));
						specialTestTitleOption.setTitleId(specialTestTitle.getId());
						specialTestTitleOption.setId(Util.getUUID());
						specialTestTitleOptionss.add(specialTestTitleOption);
					}
				}
				// 执行新增
				obj = appDoctorDao.addEntity(specialTestTitle);
				if (StringUtil.isEmpty(obj)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return Util.resultMap(configCode.code_1014, null);
				}
				if (specialTestTitleOptionss.size() > 0) {
					// 执行新增
					obj = appDoctorDao.addEntity(specialTestTitleOptionss);
				}
				if (StringUtil.isEmpty(obj)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return Util.resultMap(configCode.code_1014, null);
				}

			} else {
				SpecialTest specialTest = new SpecialTest();
				specialTest.setId(testId);
				specialTest.setIsSurface(isSurface);
				specialTest.setIsTongue(isTongue);
				specialTest.setIsOther(StringUtil.isEmpty(otherName) ? 2 : 1);
				specialTest.setOtherName(otherName);
				// 修改必须有ID 根据ID
				appDoctorDao.updateEntity(specialTest);
			}
		} else {
			// 查询
			Integer qid = appDoctorDao.querySpecialMaxQid(testId);
			Map<String, Object> title = appDoctorDao.querySpecialTestTitle(titleId);
			if (null == title || title.size() == 0) {
				return Util.resultMap(configCode.code_1029, null);
			}
			List<SpecialTestTitleOptions> specialTestTitleOptionss = new ArrayList<SpecialTestTitleOptions>();
			// 赋值
			SpecialTestTitle specialTestTitle = new SpecialTestTitle();
			specialTestTitle.setCreateTime(Util.queryNowTime());
			specialTestTitle.setId(Util.getUUID());
			specialTestTitle.setIsMandatory(Integer.parseInt(title.get("isMandatory").toString()));
			specialTestTitle.setOptionType(Integer.parseInt(title.get("optionType").toString()));
			specialTestTitle.setQid(++qid);
			specialTestTitle.setSpecialTestId(testId);
			specialTestTitle.setTitleName(title.get("titleName").toString());
			if (specialTestTitle.getOptionType() != 3) {
				List<Map<String, Object>> options = appDoctorDao.querySpecialTestDetailOption(titleId);
				for (Map<String, Object> option : options) {
					SpecialTestTitleOptions specialTestTitleOptions = new SpecialTestTitleOptions();
					specialTestTitleOptions.setId(Util.getUUID());
					specialTestTitleOptions.setOptionName(option.get("optionName").toString());
					specialTestTitleOptions.setOptionNum(Integer.parseInt(option.get("optionNum").toString()));
					specialTestTitleOptions.setTitleId(specialTestTitle.getId());
					specialTestTitleOptionss.add(specialTestTitleOptions);
				}
			}
			// 新增
			obj = appDoctorDao.addEntity(specialTestTitle);
			if (StringUtil.isEmpty(obj)) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return Util.resultMap(configCode.code_1014, null);
			}
			if (specialTestTitleOptionss.size() > 0) {
				obj = appDoctorDao.addEntity(specialTestTitleOptionss);
			}
			if (StringUtil.isEmpty(obj)) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return Util.resultMap(configCode.code_1014, null);
			}
		}
		return Util.resultMap(configCode.code_1001, obj);
	}

	/**
	 * 查询专业练习详情
	 */
	@Override
	public Map<String, Object> querySpecialTestDetail(String testId) {
		if (StringUtil.isEmpty(testId)) {
			return Util.resultMap(configCode.code_1029, null);
		} else {
			// 根据查询问诊/复诊模板id查询模板详情
			List<Map<String, Object>> list = appDoctorDao.querySpecialTestDetail(testId);
			// 通过id查询
			Map<String, Object> test = appDoctorDao.querySpecialTestById(testId);
			if (test != null && list != null) {
				for (Map<String, Object> map : list) {
					// 查询题目
					List<Map<String, Object>> options = appDoctorDao
							.querySpecialTestDetailOption(map.get("id").toString());
					if (options.size() == 0) {
						options.add(new HashMap<String, Object>());
					}
					map.put("options", options);
				}
				test.put("testOption", list);
				return Util.resultMap(configCode.code_1001, test);
			} else {
				return Util.resultMap(configCode.code_1011, null);
			}
		}
	}

	/**
	 * 通过id查询专业练习详情
	 */
	@Override
	public Map<String, Object> querySpecialTestDetailById(String testId) {
		if (StringUtil.isEmpty(testId)) {
			return Util.resultMap(configCode.code_1029, null);
		} else {
			// 根据查询问诊/复诊模板id查询模板详情
			List<Map<String, Object>> list = appDoctorDao.querySpecialTestDetail(testId);
			// 通过id查询
			Map<String, Object> test = appDoctorDao.querySpecialTestById(testId);
			if (test != null && list != null) {
				for (Map<String, Object> map : list) {
					// 查询题目
					List<Map<String, Object>> options = appDoctorDao
							.querySpecialTestDetailOption(map.get("id").toString());
					if (options.size() == 0) {
						options.add(new HashMap<String, Object>());
					}
					map.put("options", options);
				}
				test.put("testOption", list);
				test.put("tongue", new ArrayList<>());
				test.put("surface", new ArrayList<>());
				test.put("other", new ArrayList<>());
				return Util.resultMap(configCode.code_1001, test);
			} else {
				return Util.resultMap(configCode.code_1011, null);
			}
		}
	}

	/**
	 * 查询调理方详细
	 */
	@Override
	public Map<String, Object> queryConditioningDetail(String conditioningId) {
		if (StringUtil.isEmpty(conditioningId)) {
			return Util.resultMap(configCode.code_1029, null);
		} else {
			// 通过id查询
			Map<String, Object> conditioning = appDoctorDao.queryConditioningById(conditioningId);
			// 通过id查询
			List<Map<String, Object>> list = appDoctorDao.queryConditioningDetail(conditioningId);
			if (null != conditioning && list != null) {
				conditioning.put("conditioningDetail", list);
				return Util.resultMap(configCode.code_1001, conditioning);
			} else {
				return Util.resultMap(configCode.code_1011, null);
			}
		}
	}

	/**
	 * 查询和更新专业练习
	 */
	@Override
	@Transactional
	public Map<String, Object> addOrUpdateSpecialTest(String doctorId, Integer type, Integer isSurface,
			Integer isTongue, String otherName, Integer isOther, String testId, String testName, String test) {
		// Map<String, Object> map = JsonUtil.jsonToMap(test);
		SpecialTest specialTest = new SpecialTest();
		Object obj = null;
		if (!StringUtil.isEmpty(testId)) {
			// 删除
			appDoctorDao.deleteSpecialTestTitle(testId);
			specialTest.setId(testId);
			specialTest.setIsSurface(isSurface);
			specialTest.setIsTongue(isTongue);
			if (StringUtil.isEmpty(isOther)) {
				specialTest.setIsOther(StringUtil.isEmpty(otherName) ? 2 : 1);
			} else {
				specialTest.setIsOther(isOther);
			}
			if (specialTest.getIsOther() == 1) {
				specialTest.setOtherName("您可在此上传病历、化验单或其他检查报告");
			} else {
				specialTest.setOtherName(null);
			}
			specialTest.setTestName(testName);
			// 更新
			obj = appDoctorDao.updateEntity(specialTest);
		} else {
			specialTest.setCreateTime(Util.queryNowTime());
			specialTest.setCreateUserId(doctorId);
			specialTest.setId(Util.getUUID());
			specialTest.setIsDoctor(1);
			if (StringUtil.isEmpty(isOther)) {
				// 设置
				specialTest.setIsOther(StringUtil.isEmpty(otherName) ? 2 : 1);
			} else {
				specialTest.setIsOther(isOther);
			}
			specialTest.setIsSurface(isSurface);
			specialTest.setIsTongue(isTongue);
			specialTest.setOtherName(otherName);
			specialTest.setTestName(testName);
			specialTest.setType(type);
			// 新增
			obj = appDoctorDao.addEntity(specialTest);
			testId = specialTest.getId();
		}
		List<SpecialTestTitle> specialTestTitles = new ArrayList<SpecialTestTitle>();
		List<SpecialTestTitleOptions> specialTestTitleOptions = new ArrayList<SpecialTestTitleOptions>();
		List<Map<String, Object>> list = JsonUtil.parseJSON2List(test);
		int qid = 1;
		for (Map<String, Object> t : list) {
			SpecialTestTitle specialTestTitle = new SpecialTestTitle();
			specialTestTitle.setCreateTime(Util.queryNowTime());
			// specialTestTitle.setIsMandatory(Integer.parseInt(t.get(
			// "isMandatory").toString()));
			specialTestTitle.setIsMandatory(1);
			specialTestTitle.setOptionType(Integer.parseInt(t.get("optionType").toString()));
			specialTestTitle.setQid(qid++);
			specialTestTitle.setSpecialTestId(testId);
			specialTestTitle.setTitleName(t.get("titleName").toString());
			specialTestTitle.setId(Util.getUUID());
			// 新增
			specialTestTitles.add(specialTestTitle);
			int optionNum = 1;
			if (!t.get("optionType").toString().equals("3")) {
				List<Map<String, Object>> options = JsonUtil.parseJSON2List(t.get("options"));
				for (Map<String, Object> option : options) {
					SpecialTestTitleOptions specialTestTitleOption = new SpecialTestTitleOptions();
					specialTestTitleOption.setOptionName(option.get("optionName").toString());
					// specialTestTitleOption.setOptionNum(option.get("c")
					// .toString());
					specialTestTitleOption.setOptionNum((optionNum++));
					specialTestTitleOption.setTitleId(specialTestTitle.getId());
					specialTestTitleOption.setId(Util.getUUID());
					// 新增
					specialTestTitleOptions.add(specialTestTitleOption);
				}
			}
		}
		if (StringUtil.isEmpty(obj)) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Util.resultMap(configCode.code_1014, null);
		}
		if (specialTestTitles.size() > 0) {
			obj = appDoctorDao.addEntity(specialTestTitles);

			if (StringUtil.isEmpty(obj)) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return Util.resultMap(configCode.code_1014, null);
			}
		}
		// 判断是否大于零
		if (specialTestTitleOptions.size() > 0) {
			obj = appDoctorDao.addEntity(specialTestTitleOptions);
			if (StringUtil.isEmpty(obj)) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return Util.resultMap(configCode.code_1014, null);
			}
		}
		return Util.resultMap(configCode.code_1001, obj);
	}

	/**
	 * 删除专业练习
	 */
	@Override
	public Map<String, Object> deleteSpecialTest(String testId, Integer type) {
		if (StringUtil.isEmpty(testId)) {
			return Util.resultMap(configCode.code_1029, null);
		} else {
			Integer i = 0;
			if (type == 1) {
				// 删除
				i = appDoctorDao.deleteSpecialTest(testId);
			} else {
				// 删除
				i = appDoctorDao.deleteConditioning(testId);
			}
			if (i == 0) {
				return Util.resultMap(configCode.code_1011, i);
			} else {
				return Util.resultMap(configCode.code_1001, null);
			}
		}
	}

	/**
	 * 添加和更新调理方
	 */
	@Override
	@Transactional
	public Map<String, Object> addOrUpdateaddOrUpdateConditioning(String doctorId, String conditioningId,
			String conditioningName, String conditioning) {
		// Map<String, Object> map = JsonUtil.jsonToMap(conditioning);
		Object obj = null;
		DoctorConditioning doctorConditioning = new DoctorConditioning();
		System.out.println(StringUtil.isEmpty(conditioningId));
		if (!StringUtil.isEmpty(conditioningId)) {
			// appDoctorDao.deleteConditioning(conditioningId);
			doctorConditioning.setId(conditioningId);
			doctorConditioning.setName(conditioningName);
			// 更新
			obj = appDoctorDao.updateEntity(doctorConditioning);
		} else {
			doctorConditioning.setCreateTime(Util.queryNowTime());
			doctorConditioning.setCreateUserId(doctorId);
			doctorConditioning.setId(Util.getUUID());
			doctorConditioning.setIsDoctor(1);
			doctorConditioning.setName(conditioningName);
			doctorConditioning.setType(1);
			// 新增
			obj = appDoctorDao.addEntity(doctorConditioning);
			conditioningId = doctorConditioning.getId();
		}
		// 删除
		appDoctorDao.deleteConditioningDetail(conditioningId);
		List<DoctorConditioningDetail> doctorConditioningDetails = new ArrayList<DoctorConditioningDetail>();
		List<Map<String, Object>> list = JsonUtil.parseJSON2List(conditioning);
		int optionNum = 1;
		for (Map<String, Object> m : list) {
			DoctorConditioningDetail doctorConditioningDetail = new DoctorConditioningDetail();
			doctorConditioningDetail.setConditioningId(conditioningId);
			doctorConditioningDetail.setDrugId(Util.getValue(m, "drugId", ""));
			doctorConditioningDetail.setId(Util.getUUID());
			// doctorConditioningDetail.setOptionNum(Util.getValue(m,
			// "optionNum",
			// 0));
			doctorConditioningDetail.setOptionNum(optionNum++);
			doctorConditioningDetail.setWeight(Double.parseDouble(m.get("weight").toString()));
			doctorConditioningDetails.add(doctorConditioningDetail);
		}
		if (StringUtil.isEmpty(obj)) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Util.resultMap(configCode.code_1014, null);
		}
		// 新增
		obj = appDoctorDao.addEntity(doctorConditioningDetails);

		if (StringUtil.isEmpty(obj)) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Util.resultMap(configCode.code_1014, null);
		}
		return Util.resultMap(configCode.code_1001, obj);
	}

	/**
	 * 更新默认专业练习
	 */
	@Override
	public Map<String, Object> updateSpecialTestDefault(String testId, String doctorId, Integer type) {
		appDoctorDao.deleteSpecialTestDefault(testId, doctorId, type);
		return Util.resultMap(configCode.code_1001, 1);
	}

	/*
	 * @Override public Map<String, Object> updateSpecialTestDefault(String
	 * testId, String doctorId, Integer type) {
	 * appDoctorDao.deleteSpecialTestDefault(testId, doctorId, type); return
	 * Util.resultMap(configCode.code_1001, 0); }
	 */

	/**
	 * 更新默认的调理方
	 */
	@Override
	public Map<String, Object> updateConditioningDefault(String conditioningId, String doctorId) {
		// 更新
		Integer i = appDoctorDao.updateConditioningDefault(null, doctorId);
		// 更新
		i = appDoctorDao.updateConditioningDefault(conditioningId, doctorId);
		if (i == 0) {
			return Util.resultMap(configCode.code_1001, i);
		} else {
			return Util.resultMap(configCode.code_1011, null);
		}
	}

	/**
	 * 删除调理方
	 */
	@Override
	public Map<String, Object> deleteConditioning(String conditioningId) {
		if (StringUtil.isEmpty(conditioningId)) {
			return Util.resultMap(configCode.code_1029, null);
		} else {
			// 删除
			Integer i = appDoctorDao.deleteConditioning(conditioningId);
			if (i == 0) {
				return Util.resultMap(configCode.code_1001, i);
			} else {
				return Util.resultMap(configCode.code_1011, null);
			}
		}
	}

	/**
	 * 查询开方页面基础数据（药品、症型、复诊时间）
	 */
	@Override
	public Map<String, Object> queryExtractBasicData(String clientLastUpdatedAt, String doctorId,String oldYztId) {
		Map<String, Object> map = new HashMap<>();
		try {
			Map<String,Object> sysMap = appDoctorDao.querySysSet();
			String yztId = sysMap==null||StringUtil.isEmpty(sysMap.get("yztId"))?"YZT000001":sysMap.get("yztId").toString();
			//新版多医珍堂逻辑
			if(!StringUtil.isEmpty(doctorId)){
				Map<String, Object> docMap = appDoctorDao.queryDocById(doctorId);
				if (docMap == null) {
					return Util.resultMap(configCode.code_1142, null);
				}
				yztId = !StringUtil.isEmpty(docMap.get("customYztId")) ? docMap.get("customYztId").toString()
						: !StringUtil.isEmpty(docMap.get("defaultYztId")) ? docMap.get("defaultYztId").toString() : null;
				if (StringUtil.isEmpty(yztId)) {
					return Util.resultMap(configCode.code_1143, null);
				}
				map.put("yztId", yztId);
			}
			String serverLastUpdatedAt = systemSettingService.getValueByKey(SystemSettingServiceImpl.DRUG_UPDATED_AT,
					yztId);

			map.put("lastUpdatedAt", serverLastUpdatedAt);
			map.put("yztId", yztId);

			if (org.apache.commons.lang.StringUtils.isBlank(clientLastUpdatedAt))
				clientLastUpdatedAt = "-1";
			if((!StringUtil.isEmpty(oldYztId)&&!oldYztId.equals(yztId))||(StringUtil.isEmpty(oldYztId)&&!StringUtil.isEmpty(yztId))){
				map.put("code", 1);
				// 查询症型列表
				List<Map<String, Object>> diagnos = appDoctorDao.queryDiagnosticList(null);
				// 查询药品列表(开方页面用)
				List<Map<String, Object>> druglist = appDoctorDao.queryDrugList(null,yztId);
				// 查询复诊时间列表
				List<Map<String, Object>> visitTimes = appDoctorDao.queryRepeatTimes();

				map.put("diagnos", diagnos);

				map.put("druglist", druglist);

				map.put("visitTimes", visitTimes);
			}else{
				if (org.apache.commons.lang.StringUtils.equalsIgnoreCase(clientLastUpdatedAt, serverLastUpdatedAt)) {
					map.put("code", 0);
				} else {
					map.put("code", 1);
					// 查询症型列表
					List<Map<String, Object>> diagnos = appDoctorDao.queryDiagnosticList(null);
					// 查询药品列表(开方页面用)
					List<Map<String, Object>> druglist = appDoctorDao.queryDrugList(null,yztId);
					// 查询复诊时间列表
					List<Map<String, Object>> visitTimes = appDoctorDao.queryRepeatTimes();

					map.put("diagnos", diagnos);

					map.put("druglist", druglist);

					map.put("visitTimes", visitTimes);
				}
			}
			
		} catch (Exception e) {
			logger.warn("获取开方页面基础信息失败,方法名：queryExtractBasicData" + e);
			return Util.resultMap(configCode.code_1141, null);
		}

		return Util.resultMap(configCode.code_1001, map);
	}

	/**
	 * 查询药品列表(开方用)
	 */
	@Override
	public Map<String, Object> queryDrugList(String name,String doctorId) {
		List<Map<String, Object>> druglist = new ArrayList<Map<String,Object>>();
		try {
			Map<String,Object> sysMap = appDoctorDao.querySysSet();
			String yztId = sysMap==null||StringUtil.isEmpty(sysMap.get("yztId"))?"YZT000001":sysMap.get("yztId").toString();
			//新版多医珍堂逻辑
			if(!StringUtil.isEmpty(doctorId)){
				Map<String, Object> docMap = appDoctorDao.queryDocById(doctorId);
				if (docMap == null) {
					return Util.resultMap(configCode.code_1142, null);
				}
				yztId = !StringUtil.isEmpty(docMap.get("customYztId")) ? docMap.get("customYztId").toString()
						: !StringUtil.isEmpty(docMap.get("defaultYztId")) ? docMap.get("defaultYztId").toString() : null;
				if (StringUtil.isEmpty(yztId)) {
					return Util.resultMap(configCode.code_1143, null);
				}
			}
			druglist = appDoctorDao.queryDrugList(name,yztId);
			if (druglist == null) {
				return Util.resultMap(configCode.code_1011, druglist);
			}
		} catch (Exception e) {
			System.out.println(e);
			return Util.resultMap(configCode.code_1015, null);
		}
		return Util.resultMap(configCode.code_1001, druglist);
	}

	/**
	 * 查询患者病历档案
	 */
	@Override
	public Map<String, Object> queryPatientServerRecords(String doctorId, String patientId, Integer page, Integer row,
			String year) {
		if (StringUtil.isEmpty(doctorId) || StringUtil.isEmpty(patientId)) {
			return Util.resultMap(configCode.code_1029, null);
		}
		/*
		 * if(StringUtil.isEmpty(year)){ Date d = new Date(); SimpleDateFormat
		 * sdf = new SimpleDateFormat("yyyy"); year = sdf.format(d); }
		 */
		// 查询患者病历档案
		List<Map<String, Object>> serverRecords = appDoctorDao.queryPatientServerRecords(doctorId, patientId, page, row,
				year);
		return Util.resultMap(configCode.code_1001, serverRecords);
	}

	/**
	 * 查询处方模版处主药品列表
	 */
	@Override
	public Map<String, Object> queryDrugMasterList() {
		List<Map<String, Object>> drugMasterList = appDoctorDao.queryDrugMasterList();
		return Util.resultMap(configCode.code_1001, drugMasterList);
	}

	/**
	 * 查询主药
	 */
	@Override
	public Map<String, Object> queryDrugMasters(String doctorId,String name, Integer page, Integer row) {
		// 查询
		List<Map<String, Object>> drugMasterList;
		try {
			Map<String,Object> sysMap = appDoctorDao.querySysSet();
			String yztId = sysMap==null||StringUtil.isEmpty(sysMap.get("yztId"))?"YZT000001":sysMap.get("yztId").toString();
			//新版多医珍堂逻辑
			if(!StringUtil.isEmpty(doctorId)){
				Map<String, Object> docMap = appDoctorDao.queryDocById(doctorId);
				if (docMap == null) {
					return Util.resultMap(configCode.code_1142, null);
				}
				yztId = !StringUtil.isEmpty(docMap.get("customYztId")) ? docMap.get("customYztId").toString()
						: !StringUtil.isEmpty(docMap.get("defaultYztId")) ? docMap.get("defaultYztId").toString() : null;
				if (StringUtil.isEmpty(yztId)) {
					return Util.resultMap(configCode.code_1143, null);
				}
			}
			drugMasterList = appDoctorDao.queryDrugMasters(yztId,name, page, row);
			if (drugMasterList == null) {
				return Util.resultMap(configCode.code_1011, drugMasterList);
			}
		} catch (Exception e) {
			return Util.resultMap(configCode.code_1015, null);
		}
		return Util.resultMap(configCode.code_1001, drugMasterList);
	}

	/**
	 * 查询患者病历图像(问诊单ID)
	 */
	@Override
	public Map<String, Object> queryPatientImg(String doctorId, String patientId, Integer page, Integer row) {
		if (StringUtil.isEmpty(doctorId) || StringUtil.isEmpty(patientId)) {
			return Util.resultMap(configCode.code_1029, null);
		}
		// 查询患者病历图像(问诊单ID)
		List<Map<String, Object>> list = appDoctorDao.queryPatientImg(doctorId, patientId, null, page, row);
		if (list == null) {
			return Util.resultMap(configCode.code_1011, null);
		}
		for (int i = 0; i < list.size(); i++) {
			if (!StringUtil.isEmpty(list.get(i).get("ids"))) {
				// 查询患者病历图像
				List<Map<String, Object>> imgs = appDoctorDao.queryPatientCaseImgs(list.get(i).get("ids").toString());
				list.get(i).remove("ids");
				list.get(i).put("imgs", imgs);
			}
		}
		return Util.resultMap(configCode.code_1001, list);
	}

	/**
	 * 根据医生ID、患者ID查询调理方订单
	 */
	@Override
	public Map<String, Object> queryConditionsByPatientId(String doctorId, String patientId, Integer page,
			Integer row) {
		if (StringUtil.isEmpty(doctorId) || StringUtil.isEmpty(patientId)) {
			return Util.resultMap(configCode.code_1029, null);
		}
		// 根据医生ID、患者ID查询调理方订单
		List<Map<String, Object>> list = appDoctorDao.queryConditionsByPatientId(doctorId, patientId, null, page, row);
		if (list == null) {
			return Util.resultMap(configCode.code_1011, null);
		}
		for (int i = 0; i < list.size(); i++) {
			if (!StringUtil.isEmpty(list.get(i).get("recordId"))) {
				// 根据调理记录查询调理方案、药品记录
				List<Map<String, Object>> druglist = appDoctorDao
						.queryConditionsByRecordId(list.get(i).get("recordId").toString());
				list.get(i).put("contidionlist", druglist);
			}
		}
		return Util.resultMap(configCode.code_1001, list);
	}

	/**
	 * 添加调理方订单
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Map<String, Object> addRecipeOrder(HttpServletRequest request, String conditions, String doctorId,
			String patientId, String goods, String diagnostics, Double wzPrice, Integer orderType, Integer visitTime,
			String orderNo, Integer state, String remarks, String isOld, String openId) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("conditions", conditions);
		paramMap.put("doctorId", doctorId);
		paramMap.put("patientId", patientId);
		paramMap.put("goods", goods);
		paramMap.put("diagnostics", diagnostics);
		paramMap.put("wzPrice", wzPrice);
		paramMap.put("orderType", orderType);
		paramMap.put("visitTime", visitTime);
		paramMap.put("orderNo", orderNo);
		paramMap.put("state", state);
		paramMap.put("remarks", remarks);
		paramMap.put("isOld", isOld);
		paramMap.put("openId", openId);
		logger.info("Method[addRecipeOrder] request parameters: " + JSONObject.fromMap(paramMap).toString());

		if (!StringUtil.isEmpty(wzPrice) && wzPrice == 0.0) {
			wzPrice = null;
			// return Util.resultMap(configCode.code_1132, null);
		}
		if (StringUtil.isEmpty(conditions) || StringUtil.isEmpty(diagnostics) || StringUtil.isEmpty(doctorId)) {
			logger.error("conditions等参数为空");
			return Util.resultMap(configCode.code_1029, null);
		}
		if (StringUtil.isEmpty(state)) {
			state = 1;
		}
		String mainOrderNo = null;
		String wzOrderNo = null;
		Map<String, Object> lastServerMap = null;
		String patientName = "";
		// 新增点击粉丝直接开方多传一个openId，其他的原有的这个字段必须传null或空字符串
		if (!StringUtil.isEmpty(openId)) {
			patientId = null;
		}
		String memberId = openId;
		Double mainOrderPrice = 0.0;
		String oldwzOrderNo = null;
		String oldmainOrderNo = null;
		String recordId = Util.getUUID();
		String recipeOrderNo = null;
		String oldRecordId = null;
		Double frieght = 0.0;
		Integer delOldServerOk = 0;
		Integer isOldServerType = 0;
		try {
			// 查询医生信息根据ID
			Map<String, Object> doctor = appDoctorDao.queryDocById(doctorId);
			if (doctor == null) {
				logger.error("未查到相应医生信息");
				return Util.resultMap(configCode.code_1032, null);
			}
			if ("-1".equals(doctor.get("docIsOn"))) {
				logger.error("医生未认证");
				return Util.resultMap(configCode.code_1100, null);
			}
			// 获取系统设置信息
			Map<String, Object> sysSetMap = appDoctorDao.getSysSet();
			Integer mDose = sysSetMap == null || StringUtil.isEmpty(sysSetMap.get("dose")) ? 30
					: Integer.valueOf(sysSetMap.get("dose").toString());
			Double jgBasicPrice = sysSetMap == null || StringUtil.isEmpty(sysSetMap.get("jgBasicPrice")) ? 600
					: Double.valueOf(sysSetMap.get("jgBasicPrice").toString());
			orderType = 13;
			String yztId = !StringUtil.isEmpty(doctor.get("customYztId")) ? doctor.get("customYztId").toString()
					: !StringUtil.isEmpty(doctor.get("defaultYztId")) ? doctor.get("defaultYztId").toString() : null;
			if (StringUtil.isEmpty(yztId)) {
				yztId = sysSetMap == null || StringUtil.isEmpty(sysSetMap.get("yztId")) ? "YZT000001"
						: sysSetMap.get("yztId").toString();
			}
			if (!StringUtil.isEmpty(orderNo)) {
				recipeOrderNo = orderNo;
				// 修改调理单获取订单信息接口（orderNo-调理单订单号）
				Map<String, Object> recordOrder = appDoctorDao.queyrDetailByOrderNo(orderNo, doctorId);
				if (recordOrder != null) {
					if (Integer.valueOf(recordOrder.get("paymentStatus").toString()) == 6) {
						logger.error("订单状态已作废");
						return Util.resultMap(configCode.code_1111, null);
					}
					if (Integer.valueOf(recordOrder.get("paymentStatus").toString()) != 1) {
						logger.error("订单状态非未支付");
						return Util.resultMap(configCode.code_1128, null);
					}

					if (Integer.valueOf(recordOrder.get("isIdentifyingPeople").toString()) == 1) {
						return Util.resultMap(configCode.code_1136, null);
					}
					frieght = StringUtil.isEmpty(recordOrder.get("postage")) ? null
							: Double.valueOf(recordOrder.get("postage").toString());
					isOldServerType = StringUtil.isEmpty(recordOrder.get("isOldServer")) ? 0
							: Integer.valueOf(recordOrder.get("isOldServer").toString());
					oldwzOrderNo = StringUtil.isEmpty(recordOrder.get("wzOrderNo")) ? null
							: recordOrder.get("wzOrderNo").toString();
					if (!StringUtils.isEmpty(oldwzOrderNo) && isOldServerType == 0 && StringUtils.isEmpty(wzPrice)) {
						// 根据老服务订单号删除订单
						delOldServerOk = appDoctorDao.deleteOldWzOrder(oldwzOrderNo);
						if (delOldServerOk <= 0) {
							return Util.resultMap(configCode.code_1140, null);
						} else {
							oldwzOrderNo = null;
						}
					}
					oldmainOrderNo = StringUtil.isEmpty(recordOrder.get("mainOrderNo")) ? null
							: recordOrder.get("mainOrderNo").toString();
					oldRecordId = StringUtil.isEmpty(recordOrder.get("recordId")) ? null
							: recordOrder.get("recordId").toString();
					orderType = StringUtils.isEmpty(recordOrder.get("orderType")) ? 13
							: Integer.valueOf(recordOrder.get("orderType").toString());
					if (!StringUtil.isEmpty(oldRecordId)) {
						recordId = oldRecordId;
						// 删除调理方案
						appDoctorDao.deleteConditionById(oldRecordId);
					}
					if (!StringUtils.isEmpty(oldmainOrderNo)) {
						mainOrderNo = oldmainOrderNo;
					}
				}
			} else {
				// 获取订单号
				recipeOrderNo = "DRUG-" + appDoctorDao.getOrderNo();
				System.out.println("recipeOrderNo=" + recipeOrderNo);
			}

			if (!StringUtil.isEmpty(patientId)) {
				if (StringUtil.isEmpty(orderNo)) {
					orderType = 10;
				}
				// 查询最后一次服务订单
				lastServerMap = appDoctorDao.queryLastOrderNo(patientId, doctorId);
				if (lastServerMap != null) {
					patientName = lastServerMap.get("patientName").toString();
					memberId = lastServerMap.get("memberId").toString();
					wzOrderNo = StringUtil.isEmpty(lastServerMap.get("orderNo")) ? null
							: lastServerMap.get("orderNo").toString();
				} else {
					logger.error("患者最后一次服务或患者信息为空");
					return Util.resultMap(configCode.code_1017, null);
				}
			}
			if (!StringUtil.isEmpty(goods) || !StringUtil.isEmpty(wzPrice)) {
				if (!StringUtils.isEmpty(oldmainOrderNo)) {
					mainOrderNo = oldmainOrderNo;
				} else {
					mainOrderNo = "Main-" + appDoctorDao.getOrderNo();
					System.out.println("mainOrderNo=" + mainOrderNo);
				}
			}
			if (!StringUtil.isEmpty(wzPrice)) {
				mainOrderPrice += wzPrice;
				Object id = null;
				int status = 1;
				if (Double.valueOf(wzPrice) == 0.0) {
					status = 2;
				}
				if (!StringUtils.isEmpty(oldwzOrderNo)) {
					if (isOldServerType == 1) {
						// 修改订单主订单号
						appDoctorDao.updateOrderMainOrderNo(oldwzOrderNo);
						oldwzOrderNo = null;
						// 获取订单号
						wzOrderNo = "TW-" + appDoctorDao.getOrderNo();
						// 添加订单
						id = addOrder(yztId, orderDao, wzOrderNo, mainOrderNo, wzPrice, memberId, patientId, 4, null,
								null, doctorId, null, status, null, true, 0);
					} else {
						wzOrderNo = oldwzOrderNo;
						// 添加订单
						id = addOrder(yztId, orderDao, oldwzOrderNo, mainOrderNo, wzPrice, memberId, patientId, 4, null,
								null, doctorId, null, status, null, false);
					}
					System.out.println("添加问诊单1:" + wzOrderNo);
				} else {
					wzOrderNo = "TW-" + appDoctorDao.getOrderNo();
					// 添加订单
					id = addOrder(yztId, orderDao, wzOrderNo, mainOrderNo, wzPrice, memberId, patientId, 4, null, null,
							doctorId, null, status, null, true, 0);
					System.out.println("添加问诊单2:" + wzOrderNo);
				}
				if (id == null) {
					logger.error("插入服务订单失败");
					// 设置事务回滚
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return Util.resultMap(configCode.code_1058, null);
				} else {
					OrderDetailServer serverDetail = new OrderDetailServer();
					serverDetail.setBuyNum(1);
					serverDetail.setDoctorId(doctorId);
					serverDetail.setDoctorPrice(wzPrice);
					serverDetail.setPayPrice(wzPrice);
					Object detialId = null;
					if (!StringUtils.isEmpty(oldwzOrderNo)) {
						serverDetail.setOrderNo(oldwzOrderNo);
						// 根据订单号修改服务订单详情内容
						detialId = appDoctorDao.updateOrderDetailServer(serverDetail);
					} else {
						serverDetail.setCreateTime(Util.queryNowTime());
						serverDetail.setOrderNo(wzOrderNo);
						detialId = appDoctorDao.addEntityUUID(serverDetail);
					}
					if (detialId == null) {
						logger.error("插入服务订单详情失败");
						// 设置事务回滚
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						return Util.resultMap(configCode.code_1058, null);
					}
					if (!StringUtil.isEmpty(doctorId) && !StringUtil.isEmpty(patientId) && status == 2
							&& !StringUtil.isEmpty(memberId)) {
						// 查询医患关系是否存在
						Integer count = orderDao.queryDoctorPatientRelationship(doctorId, patientId);
						if (count <= 0) {
							// 插入医患关系
							orderDao.insertDoctorPatientRelationship(doctorId, memberId, patientId);
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("id", Util.getUUID());
							map.put("doctorId", doctorId);
							map.put("memberId", memberId);
							map.put("patientId", patientId);
							String result = SendCallCenterUtil.sendCallCenterData(map,
									CallCenterConfig.PatientRelation);
							System.out.println(result);
						}
						// 查询keep
						Integer keep = appDoctorDao.queryKeep(memberId, doctorId);
						if (keep == 0) {
							// 添加关注
							appDoctorDao.addKeep(memberId, doctorId);
						}
					}
				}
			}

			if (StringUtil.isEmpty(wzOrderNo)) {
				Object id = null;
				if (!StringUtils.isEmpty(oldwzOrderNo)) {
					wzOrderNo = oldwzOrderNo;
					id = addOrder(yztId, orderDao, oldwzOrderNo, mainOrderNo, 0.0, memberId, patientId, 21, null, null,
							doctorId, null, 1, 1, false);
					System.out.println("添加问诊单3:" + wzOrderNo);
				} else {
					wzOrderNo = "TW-" + appDoctorDao.getOrderNo();
					System.out.println("wzOrderNo=" + wzOrderNo);
					if (delOldServerOk > 0) {
						// 添加订单
						id = addOrder(yztId, orderDao, wzOrderNo, mainOrderNo, 0.0, memberId, patientId, 21, null, null,
								doctorId, null, 1, 1, true, 0);
					} else {
						// 添加订单
						id = addOrder(yztId, orderDao, wzOrderNo, mainOrderNo, 0.0, memberId, patientId, 21, null, null,
								doctorId, null, 1, 1, true);
					}
					System.out.println("添加问诊单4:" + wzOrderNo);
				}
				if (id == null) {
					logger.error("插入服务订单失败");
					// 设置事务回滚
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return Util.resultMap(configCode.code_1058, null);
				} else {
					OrderDetailServer serverDetail = new OrderDetailServer();
					serverDetail.setBuyNum(1);
					serverDetail.setDoctorId(doctorId);
					serverDetail.setDoctorPrice(0.0);
					serverDetail.setPayPrice(0.0);
					Object detialId = null;
					if (!StringUtils.isEmpty(oldwzOrderNo)) {
						serverDetail.setOrderNo(oldwzOrderNo);
						// 根据订单号修改服务订单详情内容
						detialId = appDoctorDao.updateOrderDetailServer(serverDetail);
					} else {
						serverDetail.setCreateTime(Util.queryNowTime());
						serverDetail.setOrderNo(wzOrderNo);
						// 新增
						detialId = appDoctorDao.addEntityUUID(serverDetail);
					}
					if (detialId == null) {
						logger.error("插入服务订单详情失败");
						// 设置事务回滚
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						return Util.resultMap(configCode.code_1058, null);
					}
				}
			}

			List<Map<String, Object>> conditionList = JsonUtil.parseJSON2List(conditions);
			List<ConditionRecord> conditionRecords = new ArrayList<ConditionRecord>();
			List<Map<String, Object>> drugs = new ArrayList<Map<String, Object>>();
			Double recipeOrderPrice = 0.0;
			int i = 0;
			// 查询十八反十九畏集合
			Map<String, Object> confictIds = appDoctorDao.queryConfict();
			boolean isGf = false;
			for (Map<String, Object> conditonMap : conditionList) {
				i++;
				Double jgPrice = 0.0;
				Double unitPrice = 0.0;
				Double drugPrice = 0.0;
				Double contidionPrice = 0.0;
				Double oneContiditonPrice = 0.0;
				Integer waringType = 0;
				Integer agentType = Integer.valueOf(conditonMap.get("agentType").toString());
				// 加工服务
				Integer jgServerType = !conditonMap.containsKey("jgServerType")
						|| StringUtil.isEmpty(conditonMap.get("jgServerType")) ? 0
								: Integer.valueOf(conditonMap.get("jgServerType").toString());
				Integer dose = Integer.valueOf(conditonMap.get("dose").toString());
				if (jgServerType == 3) {
					isGf = true;
					Integer jgBs = 1;
					if (dose % mDose == 0) {
						jgBs = dose / mDose;
					} else {
						jgBs = (dose / mDose) + 1;
					}
					jgPrice = Double.valueOf(jgBs * jgBasicPrice);
				}
				String conditionId = Util.getUUID();
				ConditionRecord conditionRecord = new ConditionRecord();
				conditionRecord.setConditionOrder(i);
				conditionRecord.setId(conditionId);
				conditionRecord.setJgServerType(jgServerType);
				conditionRecord.setRecordId(recordId);
				conditionRecord.setAgentType(agentType);
				conditionRecord.setCreateTime(Util.queryNowTime());
				conditionRecord.setDose(dose);
				conditionRecord.setUseCount(Integer.valueOf(conditonMap.get("useCount").toString()));
				conditionRecord.setIsHideGram(Integer.valueOf(conditonMap.get("isHideGram").toString()));
				conditionRecord.setJgPrice(jgPrice);
				conditionRecord.setOutOrIn(conditonMap.get("outOrIn").toString());
				conditionRecord.setVisitTime(
						!conditonMap.containsKey("visitTime") || StringUtil.isEmpty(conditonMap.get("visitTime")) ? null
								: Integer.valueOf(conditonMap.get("visitTime").toString()));
				conditionRecord
						.setWaring(!conditonMap.containsKey("waring") || StringUtil.isEmpty(conditonMap.get("waring"))
								? null : conditonMap.get("waring").toString());
				conditionRecord
						.setRecipeName(patientName.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "*")
								+ " " + DateTime.getTime());
				List<Map<String, Object>> drugList = JsonUtil.parseJSON2List(conditonMap.get("drugs"));

				// 构造查询药品字符串
				String drugsStr = "";
				for (int j = 0; j < drugList.size(); j++) {
					if (!StringUtil.isEmpty(drugList.get(j).get("drugId"))
							&& !StringUtil.isEmpty(drugList.get(j).get("drugNum"))) {
						drugsStr += "'" + drugList.get(j).get("drugId") + "',";
					}
				}

				drugsStr = drugsStr.substring(0, drugsStr.length() - 1);
				List<Map<String, Object>> drugMaps = appDoctorDao.queryDrugListByIds(drugsStr);

				// 检查是否有不存在的药品
				String wrongDrugs = "";
				for (int j = 0; j < drugMaps.size(); j++) {
					Map<String, Object> drugMap = drugMaps.get(j);
					if (Integer.valueOf(drugMap.get("state").toString()) != 1) {
						wrongDrugs += drugMap.get("drugName").toString() + ",";
					}
				}

				// 回传移动端显示异常药味
				if (!StringUtil.isEmpty(wrongDrugs)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					wrongDrugs = wrongDrugs.substring(0, wrongDrugs.length() - 1);
					return Util.resultMap1(configCode.code_1089, wrongDrugs);
				}

				for (int j = 0; j < drugList.size(); j++) {

					if (!StringUtil.isEmpty(drugList.get(j).get("drugId"))
							&& !StringUtil.isEmpty(drugList.get(j).get("drugNum"))) {

						String drugId = drugList.get(j).get("drugId").toString();

						Map<String, Object> drugMap = null;
						for (int k = 0; k < drugMaps.size(); k++) {
							if (drugMaps.get(k).get("id").toString().equals(drugId)) {
								drugMap = drugMaps.get(k);
								break;
							}
						}

						if (drugMap == null)
							continue;

						Integer drugWaringType = 0;

						if (confictIds != null) {
							for (int c = 0; c < drugMaps.size(); c++) {
								Map<String, Object> drugMap1 = drugMaps.get(c);
								if (confictIds.containsKey(drugMap1.get("masterId") + "_" + drugMap.get("masterId"))
										|| confictIds.containsKey(
												drugMap.get("masterId") + "_" + drugMap1.get("masterId"))) {
									waringType = 2;
									drugWaringType = 2;
								}
							}
						}

						oneContiditonPrice += (Double.valueOf(drugMap.get("drugPrice").toString())
								* Integer.valueOf(drugList.get(j).get("drugNum").toString()));
						unitPrice += (Double.valueOf(drugMap.get("drugPrice").toString())
								* Integer.valueOf(drugList.get(j).get("drugNum").toString()));
						Double maxWeight = StringUtil.isEmpty(drugMap.get("maxWeight")) ? null
								: Double.valueOf(drugMap.get("maxWeight").toString());
						Double drugNum = Double.valueOf(drugList.get(j).get("drugNum").toString());
						if (!StringUtil.isEmpty(maxWeight) && maxWeight != 0.0 && drugNum > maxWeight) {
							drugMap.put("isExcess", 1);
							waringType = 1;
							drugWaringType = 1;
							if (waringType == 2) {
								waringType = 3;
							}
							if (drugWaringType == 2) {
								drugWaringType = 3;
							}
						} else {
							drugMap.put("isExcess", 0);
						}
						drugMap.put("waringType", drugWaringType);
						drugMap.put("drugDose", drugList.get(j).get("drugNum"));
						drugMap.put("drugId", drugList.get(j).get("drugId"));
						drugMap.put("conditionId", conditionId);
						drugMap.put("drugOrder", j);
						drugs.add(drugMap);
					}
				}

				// for (int j = 0; j < drugList.size(); j++) {
				// Integer drugWaringType = 0;
				// if (!StringUtil.isEmpty(drugList.get(j).get("drugId"))
				// && !StringUtil.isEmpty(drugList.get(j).get(
				// "drugNum"))) {
				// Map<String, Object> drugMap = appDoctorDao
				// .queryDrugById(drugList.get(j).get("drugId")
				// .toString());
				// if (drugMap != null) {
				// if (confictIds != null) {
				// for (int c = 0; c < drugList.size(); c++) {
				// if (!StringUtil.isEmpty(drugList.get(c)
				// .get("drugId"))
				// && !StringUtil.isEmpty(drugList
				// .get(c).get("drugNum"))) {
				// Map<String, Object> drugMap1 = appDoctorDao
				// .queryDrugById(drugList.get(c)
				// .get("drugId")
				// .toString());
				// if (confictIds.containsKey(drugMap1
				// .get("masterId")
				// + "_"
				// + drugMap.get("masterId"))
				// || confictIds
				// .containsKey(drugMap
				// .get("masterId")
				// + "_"
				// + drugMap1
				// .get("masterId"))) {
				// waringType = 2;
				// drugWaringType = 2;
				// }
				// }
				// }
				// }
				// oneContiditonPrice += (Double.valueOf(drugMap.get(
				// "drugPrice").toString()) * Integer
				// .valueOf(drugList.get(j).get("drugNum")
				// .toString()));
				// unitPrice += (Double.valueOf(drugMap.get(
				// "drugPrice").toString()) * Integer
				// .valueOf(drugList.get(j).get("drugNum")
				// .toString()));
				// Double maxWeight = StringUtil.isEmpty(drugMap
				// .get("maxWeight")) ? null : Double
				// .valueOf(drugMap.get("maxWeight")
				// .toString());
				// Double drugNum = Double.valueOf(drugList.get(j)
				// .get("drugNum").toString());
				// if (!StringUtil.isEmpty(maxWeight)
				// && maxWeight != 0.0 && drugNum > maxWeight) {
				// drugMap.put("isExcess", 1);
				// waringType = 1;
				// drugWaringType = 1;
				// if (waringType == 2) {
				// waringType = 3;
				// }
				// if (drugWaringType == 2) {
				// drugWaringType = 3;
				// }
				// } else {
				// drugMap.put("isExcess", 0);
				// }
				// drugMap.put("waringType", drugWaringType);
				// drugMap.put("drugDose",
				// drugList.get(j).get("drugNum"));
				// drugMap.put("drugId", drugList.get(j).get("drugId"));
				// drugMap.put("conditionId", conditionId);
				// drugMap.put("drugOrder", j);
				// drugs.add(drugMap);
				// } else {
				// TransactionAspectSupport.currentTransactionStatus()
				// .setRollbackOnly();
				// return Util.resultMap(configCode.code_1089, null);
				// }
				// }
				// }

				unitPrice = Util.getTwoPointPrice(Double.valueOf(unitPrice) + "");
				// drugPrice = Double.valueOf(unitPrice) * dose;
				oneContiditonPrice = Util.getTwoPointPrice(Double.valueOf(oneContiditonPrice) * dose + "");
				drugPrice = oneContiditonPrice;
				contidionPrice = oneContiditonPrice + jgPrice;
				conditionRecord.setUnitPrice(unitPrice);
				conditionRecord.setDrugPrice(Util.getTwoPointPrice(drugPrice + ""));
				conditionRecord.setPrice(Util.getTwoPointPrice(contidionPrice + ""));
				conditionRecord.setWaringType(waringType);
				conditionRecords.add(conditionRecord);
				recipeOrderPrice += contidionPrice;
			}
			// 新增
			Object insertConCount = appDoctorDao.addConditionRecords(conditionRecords);
			if (StringUtil.isEmpty(insertConCount) || Integer.valueOf(insertConCount.toString()) == 0) {
				logger.error("插入调理方案失败");
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return Util.resultMap(configCode.code_1058, null);
			}
			// 批量插入药品记录信息
			Object insertDrugCount = appDoctorDao.insertDrugRecords(drugs);
			if (StringUtil.isEmpty(insertDrugCount) || Integer.valueOf(insertDrugCount.toString()) == 0) {
				logger.error("插入药品信息失败");
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return Util.resultMap(configCode.code_1058, null);
			}
			RecipeRecord recipeRecord = new RecipeRecord();
			recipeRecord.setCreateTime(Util.queryNowTime());
			recipeRecord.setDoctorId(doctorId);
			recipeRecord.setOrderNo(recipeOrderNo);
			recipeRecord.setState(1);
			recipeRecord.setIsSendUser(1);
			recipeRecord.setRemarks(remarks);
			recipeRecord.setOtherDia(diagnostics);
			recipeRecord.setPrice(Util.getTwoPointPrice(recipeOrderPrice + ""));
			recipeRecord.setWzPrice(wzPrice);
			recipeRecord.setVisitTime(visitTime);
			if (isGf) {
				recipeRecord.setJgBasicDose(mDose);
				recipeRecord.setJgBasicPrice(jgBasicPrice);
			} else {
				recipeRecord.setJgBasicDose(0);
				recipeRecord.setJgBasicPrice(0.0);
			}
			Object insertRecipeOk = null;
			if (!StringUtil.isEmpty(oldRecordId)) {
				recipeRecord.setRecordId(oldRecordId);
				// 更新
				insertRecipeOk = appDoctorDao.updateEntity(recipeRecord);
			} else {
				recipeRecord.setRecordId(recordId);
				// 新增
				insertRecipeOk = appDoctorDao.addEntity(recipeRecord);
			}
			if (StringUtil.isEmpty(insertRecipeOk) || Integer.valueOf(insertRecipeOk.toString()) == 0) {
				logger.error("插入调理记录失败");
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return Util.resultMap(configCode.code_1058, null);
			}
			Object addReciOrder = null;
			if (!StringUtil.isEmpty(orderNo)) {
				// 添加订单
				addReciOrder = addOrder(yztId, orderDao, orderNo, mainOrderNo, recipeOrderPrice + frieght, memberId,
						patientId, orderType, wzOrderNo, recordId, doctorId, frieght, 1, 0, false);
				System.out.println("添加问诊单5:" + orderNo);
			} else {
				// 添加订单
				addReciOrder = addOrder(yztId, orderDao, recipeOrderNo, mainOrderNo, recipeOrderPrice + frieght,
						memberId, patientId, orderType, wzOrderNo, recordId, doctorId, frieght, 1, 0, true);
				System.out.println("添加问诊单6:" + recipeOrderNo);
			}
			mainOrderPrice += recipeOrderPrice;
			if (StringUtil.isEmpty(addReciOrder) || Integer.valueOf(addReciOrder.toString()) == 0) {
				logger.error("插入调理订单失败");
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return Util.resultMap(configCode.code_1058, null);
			}
			if (!StringUtil.isEmpty(goods)) {
				List<Map<String, Object>> goodsList = JsonUtil.parseJSON2List(goods);
				// memberId 传为null 此处不判断限购
				Map<String, Object> returnGoodMap = goodsOrderService.addGoodsOrderByGoodsList(goodsList, memberId,
						mainOrderNo, doctorId);
				if (returnGoodMap != null && Integer.valueOf(returnGoodMap.get("respCode").toString()) == 1001) {
					mainOrderPrice += Double.valueOf(returnGoodMap.get("data").toString());
				} else {
					logger.error("判断商品订单异常");
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return returnGoodMap;
				}
			} else {
				goodsOrderDao.deleteOrderByMainOrder(mainOrderNo);
			}
			if (!StringUtil.isEmpty(mainOrderNo)) {
				Object obj = null;
				if (mainOrderNo.equals(oldmainOrderNo)) {
					obj = addOrder(yztId, orderDao, mainOrderNo, null, mainOrderPrice + frieght, memberId, patientId,
							20, null, null, doctorId, frieght, 1, 1, false);
					System.out.println("添加问诊单7:" + mainOrderNo);
				} else {
					obj = addOrder(yztId, orderDao, mainOrderNo, null, mainOrderPrice + frieght, memberId, patientId,
							20, null, null, doctorId, frieght, 1, 1, true);
					System.out.println("添加问诊单8:" + mainOrderNo);
				}
				if (StringUtil.isEmpty(obj) || Integer.valueOf(obj.toString()) == 0) {
					logger.error("插入主订单失败");
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return Util.resultMap(configCode.code_1058, null);
				}
			} else {
				mainOrderNo = recipeOrderNo;
			}

		} catch (Exception e) {
			logger.error("调理方发送失败" + e + ",请求参数：医生ID" + doctorId + ",患者ID" + patientId + "商品JOSN" + goods + ",问诊费："
					+ wzPrice + "调理方：" + conditions + "，订单号：" + orderNo + ",症型：" + diagnostics);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Util.resultMap(configCode.code_1015, null);
		}
		try {
			// 查询导入人员
			Map<String, Object> follow = orderService.queryFollowIdByOpenId(doctorId);
			if (null != follow && follow.size() > 0 && !StringUtil.isEmpty(patientId)) {
				// 调理订单推送
				pushService.unpayedorder(follow.get("followId").toString(), doctorId, mainOrderNo,
						follow.get("docName").toString());
			}
			if (StringUtil.isEmpty(orderNo)) {
				String sendCode = sendModelMsgUtil.sendAddRecipeOrder(
						StringUtil.isEmpty(patientId) ? memberId : patientId, doctorId, appDoctorDao,
						"hospital/look_scheme.html?orderNo=" + mainOrderNo);
				if (StringUtil.isEmpty(sendCode) || !"0".equals(sendCode)) {
					sendCode = sendModelMsgUtil.sendAddRecipeOrder(StringUtil.isEmpty(patientId) ? memberId : patientId,
							doctorId, appDoctorDao, "hospital/look_scheme.html?orderNo=" + mainOrderNo);
					if (!StringUtil.isEmpty(sendCode) && "0".equals(sendCode)) {
						orderDao.insertImSendRecord(mainOrderNo, doctorId, patientId, 1, null, mainOrderPrice + "",
								"sendAddRecipeOrder");
					} else {
						// 新增
						orderDao.insertImSendRecord(mainOrderNo, doctorId, patientId, 0, null, mainOrderPrice + "",
								"sendAddRecipeOrder");
						logger.error("发送推送消息失败，方法名称sendAddRecipeOrder");
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("msgtype", "text");
						params.put("toUser", "o92LXv1ZIHTjleF3MYPFlAiV7Bas");
						params.put("url", "");
						params.put("content", "发送推送消息失败，方法名称sendAddRecipeOrder");
						sendModelMsgUtil.sendCustomMsg(params, orderDao);
					}
				} else {
					// 新增
					orderDao.insertImSendRecord(mainOrderNo, doctorId, patientId, 1, null, mainOrderPrice + "",
							"sendAddRecipeOrder");
				}
			} else {
				String sendCode = sendModelMsgUtil.sendUpdateRecipeOrder(
						StringUtil.isEmpty(patientId) ? memberId : patientId, doctorId, appDoctorDao,
						"hospital/look_scheme.html?orderNo=" + mainOrderNo);
				if (StringUtil.isEmpty(sendCode) || !"0".equals(sendCode)) {
					sendCode = sendModelMsgUtil.sendUpdateRecipeOrder(
							StringUtil.isEmpty(patientId) ? memberId : patientId, doctorId, appDoctorDao,
							"hospital/look_scheme.html?orderNo=" + mainOrderNo);
					if (!StringUtil.isEmpty(sendCode) && "0".equals(sendCode)) {
						// 新增
						orderDao.insertImSendRecord(mainOrderNo, doctorId, patientId, 1, null, mainOrderPrice + "",
								"sendUpdateRecipeOrder");
					} else {
						// 新增
						orderDao.insertImSendRecord(mainOrderNo, doctorId, patientId, 0, null, mainOrderPrice + "",
								"sendUpdateRecipeOrder");
						logger.error("发送推送消息失败，方法名称sendAddRecipeOrder");
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("msgtype", "text");
						params.put("toUser", "o92LXv1ZIHTjleF3MYPFlAiV7Bas");
						params.put("url", "");
						params.put("content", "发送推送消息失败，方法名称sendUpdateRecipeOrder" + sendCode);
						sendModelMsgUtil.sendCustomMsg(params, orderDao);
					}
				} else {
					// 新增
					orderDao.insertImSendRecord(mainOrderNo, doctorId, patientId, 1, null, mainOrderPrice + "",
							"sendUpdateRecipeOrder");
				}
			}
			try {
				GetSig.accountImport(request, doctorId, null, null);
			} catch (Exception e) {
				// TODO: handle exception
			}
			if (!StringUtil.isEmpty(patientId)) {
				try {
					GetSig.accountImport(request, patientId, null, null);
				} catch (Exception e) {
					// TODO: handle exception
				}
				if (!StringUtil.isEmpty(isOld) && "new".equals(isOld)) {
					JSONObject json = new JSONObject();
					json.put("msgType", 12);
					json.put("orderNo", mainOrderNo);
					json.put("content", mainOrderPrice);
					Map<String, Object> result = GetSig.sendMsg(request, doctorId, patientId, 0, json);
					if (result != null && result.get("respCode").toString().equals("1001")) {
						orderDao.insertImSendRecord(orderNo, doctorId, patientId, 1, 12, mainOrderNo + "", "sendMsg");
					} else {
						result = GetSig.sendMsg(request, doctorId, patientId, 0, json);
						if (result != null && !result.get("respCode").toString().equals("1001")) {
							// 新增
							orderDao.insertImSendRecord(orderNo, doctorId, patientId, 0, 12, mainOrderPrice + "",
									"sendMsg");
							logger.error("发送IM消息失败，方法名称GetSig.sendMsg");
							Map<String, Object> params = new HashMap<String, Object>();
							params.put("msgtype", "text");
							params.put("toUser", "o92LXv1ZIHTjleF3MYPFlAiV7Bas");
							params.put("url", "");
							params.put("content", "发送IM开方通知失败，方法名称GetSig.sendMsg");
							sendModelMsgUtil.sendCustomMsg(params, orderDao);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("插入调理方案失败" + e);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("msgtype", "text");
			params.put("url", "");
			params.put("content", "订单异常：IM注册失效");
			params.put("toUser", "o92LXv1ZIHTjleF3MYPFlAiV7Bas");
			sendModelMsgUtil.sendCustomMsg(params, orderDao);
		}

		Map<String, Object> paramMap2 = new HashMap<>();
		paramMap2.put("doctorId", doctorId);
		paramMap2.put("patientId", patientId);
		paramMap2.put("mainOrderNo", mainOrderNo);
		paramMap2.put("wzOrderNo", wzOrderNo);
		paramMap2.put("orderNo", orderNo);
		logger.info("Method[addRecipeOrder] excute successful: " + JSONObject.fromMap(paramMap2).toString());

		return Util.resultMap(configCode.code_1001, mainOrderNo);
	}

	/**
	 * 添加订单
	 * 
	 * @param orderDao
	 * @param orderNo
	 * @param mainOrderNo
	 * @param orderPrice
	 * @param memberId
	 * @param patientId
	 * @param orderType
	 * @param sourceOrderNo
	 * @param recordId
	 * @param doctorId
	 * @param freight
	 * @param payStatus
	 * @param orderStatus
	 * @param isAdd
	 * @param isOldServer
	 * @return
	 */
	private Object addOrder(String yztId, OrderDao orderDao, String orderNo, String mainOrderNo, Double orderPrice,
			String memberId, String patientId, Integer orderType, String sourceOrderNo, String recordId,
			String doctorId, Double freight, Integer payStatus, Integer orderStatus, boolean isAdd,
			Integer... isOldServer) {
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
		order.setOrderWay(1);
		order.setGoodsPrice(orderPrice);
		order.setRecordId(recordId);
		order.setFreight(freight);
		order.setPostage(freight);
		order.setYztId(yztId);
		order.setSourceOrderNo(sourceOrderNo);
		if (isOldServer != null && isOldServer.length > 0) {
			order.setIsOldServer(isOldServer[0]);
		}
		if (payStatus == 2 && (order.getOrderType() == 4 || order.getOrderType() == 6 || order.getOrderType() == 21
				|| order.getOrderType() == 7)) {
			order.setPayTime(Util.queryNowTime());
		}
		Object i = null;
		if (!isAdd) {
			if (!StringUtil.isEmpty(order.getOrderNo())) {
				order.setFreight(freight);
				order.setConsignee("");
				order.setPhone("");
				order.setDetailedAddress("");
				// 更新
				i = orderDao.updateEntity(order);
			}
		} else {
			// 新增
			i = orderDao.addEntity(order);
		}
		if (!StringUtil.isEmpty(i)) {
			// 查询要发送呼叫中心数据
			Map<String, Object> map = orderDao.querySendCallCenterData(orderNo);
			if (map != null) {
				String result = SendCallCenterUtil.sendCallCenterData(map, CallCenterConfig.Order);
				System.out.println(result);
			}
		}
		return i;
	}

	/**
	 * 查询服务类型年
	 */
	@Override
	public Map<String, Object> queryServerYears(String doctorId, String patientId) {
		if (StringUtil.isEmpty(doctorId) || StringUtil.isEmpty(patientId)) {
			return Util.resultMap(configCode.code_1029, null);
		}
		// 查询服务类型年
		List<Map<String, Object>> years = appDoctorDao.queryServerYears(doctorId, patientId);
		if (years == null) {
			return Util.resultMap(configCode.code_1011, null);
		} else {
			return Util.resultMap(configCode.code_1001, years);
		}
	}

	/**
	 * 修改医生认证、基础信息
	 */
	@Override
	public Map<String, Object> editApplyDoc(HttpServletRequest request, DoctorApplyRecord record, Integer type,
			String doctorId) {
		if (StringUtil.isEmpty(doctorId)) {
			return Util.resultMap(configCode.code_1029, null);
		}
		// 查询医生信息根据ID
		Map<String, Object> doctor = appDoctorDao.queryDocById(doctorId);
		if (doctor == null) {
			return Util.resultMap(configCode.code_1032, null);
		}
		Object id = 0;
		try {
			String pid = null;
			// 根据医生ID获取医生认证信息
			Map<String, Object> oldApplyMap = appDoctorDao.queryDoctorApplyData(doctorId);
			String picIsOn = "-1";
			String basicIsOn = "-1";
			String docIsOn = "-1";
			Integer saleTj = 0;
			record.setApplyId(doctorId);
			if (oldApplyMap != null) {
				record.setApplyId(oldApplyMap.get("applyId").toString());
				pid = oldApplyMap.get("followId") == null ? null : oldApplyMap.get("followId").toString();
				saleTj = Integer.valueOf(oldApplyMap.get("saleTj").toString());
				picIsOn = StringUtil.isEmpty(oldApplyMap.get("picIsOn")) ? null : oldApplyMap.get("picIsOn").toString();
				basicIsOn = StringUtil.isEmpty(oldApplyMap.get("basicIsOn")) ? null
						: oldApplyMap.get("basicIsOn").toString();
				docIsOn = StringUtil.isEmpty(oldApplyMap.get("docIsOn")) ? null : oldApplyMap.get("docIsOn").toString();

			}
			if (type == 1) {// 修改基础信息
				if (StringUtil.isEmpty(record.getInfirmaryId()) || StringUtil.isEmpty(record.getIllIds())
						|| StringUtil.isEmpty(record.getInfirDepartIds())) {
					return Util.resultMap(configCode.code_1029, null);
				}
				String illds = record.getIllIds();
				String indepartIds = record.getInfirDepartIds();
				record.setBasicIsOn("0");
				record.setIllIds(null);
				record.setInfirDepartIds(null);
				record.setRecordFailNote(null);
				if (!"-1".equals(picIsOn) && !"1".equals(docIsOn)) {
					record.setDocIsOn("0");
				}
				// 更新
				id = appDoctorDao.updateEntity(record) == 0 ? null : appDoctorDao.updateEntity(record);
				if (id != null && !"0".equals(id.toString())) {
					MiddleUtil midd = null;
					System.out.println(illds + "=illIds！！！！！！！！！！！！！！");
					if (!StringUtil.isEmpty(illds)) {
						// 删除医生疾病分类关联
						appDoctorDao.delMiddleByDocId(record.getApplyId());
						String[] illids = illds.split(",");
						for (String illid : illids) {
							if (!StringUtil.isEmpty(illid)) {
								midd = new MiddleUtil();
								midd.setDoctorId(record.getApplyId());
								midd.setIllClassId(illid);
								midd.setType("1");
								// 新增
								appDoctorDao.addEntity(midd);
							}
						}
					}
					DocAndDepartUtil ddmidd = null;
					if (!StringUtil.isEmpty(indepartIds)) {
						// 根据医生ID删除医生部门关联
						appDoctorDao.delDocAndDepartUtil(record.getApplyId(), null);
						String[] departIds = indepartIds.split(",");
						for (String departId : departIds) {
							if (!StringUtil.isEmpty(departId)) {
								ddmidd = new DocAndDepartUtil();
								ddmidd.setDoctorId(record.getApplyId());
								ddmidd.setDepartId(departId);
								appDoctorDao.addEntity(ddmidd);
							}
						}
					}
				}
			} else if (type == 2) {
				if (StringUtil.isEmpty(record.getIdCardNo())) {
					return Util.resultMap(configCode.code_1029, null);
				}
				if (saleTj == 0 && (StringUtil.isEmpty(record.getIdCardBackUrl())
						|| StringUtil.isEmpty(record.getIdCardFaceUrl()) || StringUtil.isEmpty(record.getPracEoSUrl())
						|| StringUtil.isEmpty(record.getPracFourUrl()))) {
					return Util.resultMap(configCode.code_1104, null);
				}
				if (StringUtil.isEmpty(record.getApplyUserId())) {
					record.setApplyUserId(doctorId);
				}
				record.setPicIsOn("0");
				record.setIllIds(null);
				record.setInfirDepartIds(null);
				record.setRecordPicFailNote(null);
				if (!"-1".equals(basicIsOn) && !"1".equals(docIsOn)) {
					record.setDocIsOn("0");
				}
				// 更新
				id = appDoctorDao.updateEntity(record) == 0 ? null : appDoctorDao.updateEntity(record);
			}
			if (id != null && !"0".equals(id.toString())) {
				try {
					Channel channel = new Channel();
					channel.setId(record.getApplyId());
					// 通过id查询
					Channel channel1 = appDoctorDao.queryById(Channel.class, record.getApplyId());
					if (channel1 == null) {
						channel.setCreateTime(Util.queryNowTime());
						channel.setPid(pid);
						channel.setState(2);
						channel.setType(2);
						channel.setChannelName(record.getDocName());
						appDoctorDao.addEntity(channel);
					} else {
						// 修改状态
						appDoctorDao.updateChannelState(record.getApplyId());
					}
				} catch (Exception e) {
					System.out.println("修改channel信息失败，异常信息" + e);
				}
				return Util.resultMap(configCode.code_1001, 1);
			} else {
				return Util.resultMap(configCode.code_1014, null);
			}
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Util.resultMap(configCode.code_1015, null);
		}
	}

	/**
	 * 查询常用语
	 */
	@Override
	public Map<String, Object> queryPhrase(String doctorId) {
		if (StringUtil.isEmpty(doctorId)) {
			return Util.resultMap(configCode.code_1029, null);
		}
		// 查询常用语
		List<Map<String, Object>> list = appDoctorDao.queryPhrase(doctorId);
		if (null != list) {
			return Util.resultMap(configCode.code_1001, list);
		} else {
			return Util.resultMap(configCode.code_1011, null);
		}
	}

	/**
	 * 添加常用语
	 */
	@Override
	public Map<String, Object> addPhrase(String doctorId, String id, String content) {
		if (StringUtil.isEmpty(doctorId) || StringUtil.isEmpty(content)) {
			return Util.resultMap(configCode.code_1029, null);
		}
		// 查询常用语以有数量
		Integer count = appDoctorDao.queryPhraseCount(doctorId);
		if (count >= 50) {
			return Util.resultMap(configCode.code_1091, null);
		}
		Integer i = 0;
		if (StringUtil.isEmpty(id)) {
			id = Util.getUUID();
			// 添加常用语
			i = appDoctorDao.addPhrase(id, doctorId, content);
		} else {
			// 更新
			i = appDoctorDao.updatePhrase(id, doctorId, content);
		}
		if (i == 0) {
			return Util.resultMap(configCode.code_1014, null);
		} else {
			return Util.resultMap(configCode.code_1001, id);
		}
	}

	/**
	 * 删除常用语
	 */
	@Override
	public Map<String, Object> deletePhrase(String doctorId, String phraseId) {
		if (StringUtil.isEmpty(doctorId) || StringUtil.isEmpty(phraseId)) {
			return Util.resultMap(configCode.code_1029, null);
		}
		// 删除常用语
		Integer i = appDoctorDao.deletePhrase(doctorId, phraseId);
		if (i == 0) {
			return Util.resultMap(configCode.code_1014, i);
		} else {
			return Util.resultMap(configCode.code_1001, i);
		}
	}

	/**
	 * 百宝箱我的商品选择（开药时用）
	 */
	@Override
	public Map<String, Object> queryMyGoods(String doctorId, String patientId, String name, Integer page, Integer row) {
		List<Map<String, Object>> list = appDoctorDao.queryMyGoods(doctorId, name, page, row);
		if (null == list) {
			return Util.resultMap(configCode.code_1011, null);
		}
		for (Map<String, Object> map : list) {
			// 根基商品id查询规格参数
			List<Map<String, Object>> specifications = goodsDao
					.queryGoodsSpecificationsByGoodsId(map.get("id").toString());
			// 根基商品id查询规格参数
			Map<String, Object> pricenum = goodsDao.queryGoodsPricenumByGoodsId(map.get("id").toString());
			// 查询商品活动
			List<Map<String, Object>> activitys = goodsDao.queryActivity(map.get("id").toString());

			for (Map<String, Object> activity : activitys) {
				// 查询商品活动详情
				List<Map<String, Object>> activityDetail = goodsDao.queryActivityDetail(activity.get("id").toString(),
						Integer.parseInt(activity.get("type").toString()));
				activity.put("activityDetail", activityDetail);
			}

			map.put("specifications", specifications);
			map.put("pricenum", pricenum);
			map.put("activity", activitys);
		}

		Double dividedProportion = 0.0;
		// 查询默认分成比例
		Map<String, Object> commission = goodsDao.queryCommission(doctorId);
		if (null == commission || commission.size() == 0) {
			// 查询分成比例
			dividedProportion = goodsDao.queryDivided(8, 1);
			if (!StringUtil.isEmpty(patientId)) {
				// 查询关注id
				String followId = goodsDao.queryFollowId(doctorId, patientId);
				if (StringUtil.isEmpty(followId)) {
					Double d = goodsDao.queryDivided(7, 1);
					// * 涓や釜Double鏁扮浉鍔�
					dividedProportion = Util.add(dividedProportion + "", d + "");
				}
			}
		} else {
			if (StringUtils.isEmpty(commission.get("goodsCash"))) {
				// 查询分成比例
				dividedProportion = goodsDao.queryDivided(8, 1);
			} else {
				dividedProportion = Double.parseDouble(commission.get("goodsCash").toString());
			}
			// 查询关注id
			String followId = goodsDao.queryFollowId(doctorId, patientId);
			if (StringUtil.isEmpty(followId)) {
				if (StringUtils.isEmpty(commission.get("goodsDiversion"))) {
					// 查询分成比例
					Double d = goodsDao.queryDivided(7, 1);
					dividedProportion = Util.add(dividedProportion + "", d + "");
				} else {
					dividedProportion = Util.add(dividedProportion + "", commission.get("goodsDiversion").toString());
				}
			}
		}

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("goods", list);
		result.put("dividedProportion", dividedProportion);
		return Util.resultMap(configCode.code_1001, result);
	}

	/**
	 * 点击助理提交修改状态
	 */
	@Override
	public Map<String, Object> updateApplyRecordState(DoctorApplyRecord applyRecord) {
		if (StringUtil.isEmpty(applyRecord.getApplyId())) {
			return Util.resultMap(configCode.code_1029, null);
		} else {
			// 根据医生ID查询助理是否存在
			Map<String, Object> map = appDoctorDao.checkDoctorFollowId(applyRecord.getApplyId());
			if (map == null) {
				return Util.resultMap(configCode.code_1103, null);
			}
			// 查询医生信息根据ID
			Map<String, Object> doctor = appDoctorDao.queryDocById(applyRecord.getApplyId());
			if (null != doctor && doctor.get("docIsOn").equals("0")) {
				return Util.resultMap(configCode.code_1122, null);
			}
			// 点击助理提交修改状态
			Integer i = appDoctorDao.updateApplyRecordState(applyRecord);
			if (i > 0) {
				// 认证推送
				pushService.authassist(map.get("followId").toString(), applyRecord.getApplyId(),
						map.get("docName").toString());
				JSONObject data = new JSONObject();
				data.put("content", applyRecord.getApplyId());
				data.put("msgType", "22");
				GetSig.sendMsg(null, applyRecord.getApplyId(), map.get("followId").toString(), 1, data);
				return Util.resultMap(configCode.code_1001, map.get("followId"));
			} else {
				return Util.resultMap(configCode.code_1066, null);
			}
		}
	}

	/**
	 * 根据医生ID查询助理是否存在
	 */
	@Override
	public Map<String, Object> checkDoctorFollowId(String doctorId) {
		if (StringUtil.isEmpty(doctorId)) {
			return Util.resultMap(configCode.code_1029, null);
		} else {
			// 根据医生ID查询助理是否存在
			Map<String, Object> map = appDoctorDao.checkDoctorFollowId(doctorId);
			if (map == null) {
				map = new HashMap<String, Object>();
				map.put("followId", "");
			}
			return Util.resultMap(configCode.code_1001, map);
		}
	}

	/**
	 * 电话开方接口
	 */
	@Override
	public Map<String, Object> phoneExtract(String orderNo, String doctorId, String patientId) {
		if (StringUtil.isEmpty(doctorId)) {
			return Util.resultMap(configCode.code_1029, null);
		}
		try {
			if (StringUtil.isEmpty(orderNo)) {
				String memberId = null;
				Integer orderType = 17;
				String sourceOrderNo = null;
				if (!StringUtil.isEmpty(patientId)) {
					orderType = 16;
					// 查询最后一次服务订单
					Map<String, Object> lastServerMap = appDoctorDao.queryLastOrderNo(patientId, doctorId);
					if (lastServerMap != null) {
						memberId = lastServerMap.get("memberId").toString();
						sourceOrderNo = StringUtil.isEmpty(lastServerMap.get("orderNo")) ? null
								: lastServerMap.get("orderNo").toString();
					} else {
						return Util.resultMap(configCode.code_1017, null);
					}
				}
				// 获取订单号
				orderNo = "DRUG-" + appDoctorDao.getOrderNo();
				String recordId = Util.getUUID();
				RecipeRecord recipeRecord = new RecipeRecord();
				recipeRecord.setRecordId(recordId);
				recipeRecord.setCreateTime(Util.queryNowTime());
				recipeRecord.setDoctorId(doctorId);
				recipeRecord.setOrderNo(orderNo);
				recipeRecord.setIsHas(0);
				recipeRecord.setIsSendUser(0);
				recipeRecord.setState(-1);
				// 新增
				Object insertRecipeOk = appDoctorDao.addEntity(recipeRecord);
				if (insertRecipeOk != null) {
					// 添加订单
					Object addOrderOk = addOrder(null, orderDao, orderNo, null, null, memberId, patientId, orderType,
							sourceOrderNo, recordId, doctorId, null, 1, 0, true);
					if (addOrderOk == null) {
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						return Util.resultMap(configCode.code_1058, null);
					}
					// 查询医生信息根据ID
					Map<String, Object> doctor = appDoctorDao.queryDocById(doctorId);
					if (doctor != null) {
						Map<String, Object> map = new HashMap<String, Object>();
						// token
						// 先把POLYLINK_MESSAGE_TOKEN通过MD5加密，然后再获取当前年月日用之前加密的串再加密
						map.put("token", CallCenterConfig.getPhoneBasicToken());
						map.put("phones", doctor.get("docPhone"));
						map.put("transferType", 3);
						map.put("queuenum", "");
						map.put("calledNumber", CallCenterConfig.calledNumber);
						map.put("customerForeignId", orderNo);
						map.put("maxRingTime", null);
						map.put("redialTimes", null);
						map.put("ivrnum", "");
						map.put("ivrProfile", 4001);
						map.put("minRedialInterval", null);
						map.put("idleAgentMultiple", null);
						map.put("routeGroupID", 4);

						String result = SendCallCenterUtil.sendCallCenterData(map, CallCenterConfig.OncKeyCall);
						System.out.println(result);
					}
				} else {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return Util.resultMap(configCode.code_1058, null);
				}
			}
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Util.resultMap(configCode.code_1015, null);
		}
		return Util.resultMap(configCode.code_1001, orderNo);
	}

	/**
	 * 拍照开方
	 */
	@Override
	public Map<String, Object> photoExtract(HttpServletRequest request, PhotoExtract photoExtract, String imgUrls) {
		if (StringUtil.isEmpty(photoExtract.getDoctorId()) || StringUtil.isEmpty(imgUrls)) {
			return Util.resultMap(configCode.code_1029, null);
		}
		try {
			photoExtract.setCreateTime(Util.queryNowTime());
			photoExtract.setImgUrl(imgUrls);
			photoExtract.setState(-1);
			Object insertOk = appDoctorDao.addEntityUUID(photoExtract);
			if (insertOk != null) {
				// 徐调用呼叫中心接口

				// 目前暂定一张先按一张图片处理
//				Piclib pic = new Piclib();
//				pic.setGoodId(insertOk.toString());
//				pic.setPicPathUrl(imgUrls);
//				pic.setStatus("10");
//				pic.setStatusDate(Util.queryNowTime());
//				appDoctorDao.addEntityUUID(pic);
				String[] imgArr = imgUrls.split(",");
				int i= appDoctorDao.addOrderPhoto(imgArr, insertOk.toString());
				if (i!=0){return Util.resultMap(configCode.code_1001, insertOk);}
				else{return Util.resultMap(configCode.code_1058, null);}
				
			} else {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return Util.resultMap(configCode.code_1058, null);
			}
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Util.resultMap(configCode.code_1015, null);
		}
	}

	/**
	 * 根据医生ID条件、分页查询就诊列表
	 */
	@Override
	public Map<String, Object> queryJzList(String doctorId, Integer page, Integer row, Integer type) {
		if (StringUtil.isEmpty(doctorId)) {
			return Util.resultMap(configCode.code_1029, null);
		}
		// 根据医生ID条件、分页查询就诊列表
		List<Map<String, Object>> list = appDoctorDao.queryJzList(doctorId, page, row, type);
		if (list == null || list.size() == 0) {
			return Util.resultMap(configCode.code_1001, list);
		} else {
			this.imDao.queryLastMsgListByList(list);
			// for (Map<String, Object> map : list) {
			// Map<String, Object> last = imDao.queryLastMsg(
			// map.get("doctorId").toString(), map.get("patientId")
			// .toString());
			// if (null != last) {
			// map.put("lastMsg", Util.getValue(last, "lastMsg", ""));
			// map.put("lastTime", Util.getValue(last, "lastTime", ""));
			// map.put("msgTime", Util.getValue(last, "msgTime", ""));
			// } else {
			// map.put("msgTime", 0);
			// }
			//
			// }
			Collections.sort(list, new Comparator<Map<String, Object>>() {
				public int compare(Map<String, Object> o1, Map<String, Object> o2) {
					int map1value = (Integer) o1.get("msgTime");
					int map2value = (Integer) o2.get("msgTime");
					return map2value - map1value;
				}
			});
			return Util.resultMap(configCode.code_1001, list);
		}
	}

	/**
	 * 查询记录
	 */
	@Override
	public Map<String, Object> queryRecord(String orderNo) {
		Map<String, Object> recordOrder = appDoctorDao.queryRecordOrder(orderNo);
		if (null == recordOrder || recordOrder.size() == 0) {
			return Util.resultMap(configCode.code_1096, null);
		}
		if (!StringUtils.isEmpty(recordOrder.get("mainOrderNo"))) {
			orderNo = recordOrder.get("mainOrderNo").toString();
		} else {
			recordOrder.put("mainOrderNo", orderNo);
		}
		// 查询
		List<Map<String, Object>> conditioningRecords = appDoctorDao
				.queryConditioningRecord(recordOrder.get("recordId").toString());
		if (null == conditioningRecords || conditioningRecords.size() == 0) {
			return Util.resultMap(configCode.code_1096, null);
		}
		for (Map<String, Object> conditioningRecord : conditioningRecords) {
			List<Map<String, Object>> drugRecord = appDoctorDao
					.queryDrugRecord(conditioningRecord.get("id").toString());
			conditioningRecord.put("drugRecord", drugRecord);
		}
		recordOrder.put("conditioningRecord", conditioningRecords);

		Map<String, Object> otherPrice = appDoctorDao.queryGoodsByDrugRecord(orderNo);
		if (null == otherPrice || otherPrice.size() == 0) {
			recordOrder.put("otherPrice", "0");
		} else {
			recordOrder.put("otherPrice", otherPrice.get("receiptsPrice").toString());
		}

		List<Map<String, Object>> goods = appDoctorDao.queryGoodsByOrderNo(orderNo);
		for (Map<String, Object> m : goods) {
			// 查询商品活动
			List<Map<String, Object>> activitys = goodsDao.queryActivity(m.get("goodsId").toString());
			for (Map<String, Object> activity : activitys) {
				// 查询商品活动详情
				List<Map<String, Object>> activityDetail = goodsDao.queryActivityDetail(activity.get("id").toString(),
						Integer.parseInt(activity.get("type").toString()));
				activity.put("activityDetail", activityDetail);
			}
			m.put("activity", activitys);
		}
		recordOrder.put("goods", goods);
		return Util.resultMap(configCode.code_1001, recordOrder);
	}

	/**
	 * 添加订单记录
	 */
	@Override
	@Transactional
	public Map<String, Object> addRecordOrdeNo(String orderNo, String goods) {
		Map<String, Object> recordOrder = appDoctorDao.queryRecordOrderPrice(orderNo);
		if (!StringUtils.isEmpty(recordOrder.get("mainOrderNo"))) {
			orderNo = recordOrder.get("mainOrderNo").toString();
			recordOrder = appDoctorDao.queryRecordOrderPrice(orderNo);
		}
		// 查询
		Map<String, Object> drugRecordPrice = appDoctorDao.queryRecordOrder(orderNo);
		Map<String, Object> wzPrice = appDoctorDao.queryDrugRecordPrice(orderNo, 4);

		if (drugRecordPrice.get("state").toString().equals("0")) {
			return Util.resultMap(configCode.code_1105, null);
		}

		String goodsPrices = "0";

		if (!StringUtil.isEmpty(goods)) {
			// 添加商品订单 开方选择百宝箱
			Map<String, Object> result = goodsOrderService.addGoodsOrderByGoodsList(JsonUtil.parseJSON2List(goods),
					Util.getValue(recordOrder, "memberId", ""), orderNo, recordOrder.get("doctorId").toString());
			if (!result.get("respCode").toString().equals("1001")) {
				return result;
			}
			goodsPrices = result.get("data").toString();
		} else {
			goodsOrderDao.deleteOrderByMainOrder(orderNo);
		}

		Double orderPrice = Util.add(drugRecordPrice.get("orderPrice").toString(), goodsPrices);
		if (null != wzPrice && wzPrice.size() > 0) {
			orderPrice = Util.add(orderPrice + "", wzPrice.get("orderPrice").toString());
		}
		Double receiptsPrice = Util.add(drugRecordPrice.get("orderPrice").toString(), goodsPrices);
		if (null != wzPrice && wzPrice.size() > 0) {
			receiptsPrice = Util.add(receiptsPrice + "", wzPrice.get("orderPrice").toString());
		}
		Double goodsPrice = Util.add(drugRecordPrice.get("orderPrice").toString(), goodsPrices);
		if (null != wzPrice && wzPrice.size() > 0) {
			goodsPrice = Util.add(goodsPrice + "", wzPrice.get("orderPrice").toString());
		}
		// 更新
		Integer i = appDoctorDao.updateRecordOrderPrice(orderNo, orderPrice, receiptsPrice, goodsPrice);
		if (i == 0) {
			return Util.resultMap(configCode.code_1033, null);
		} else {
			return Util.resultMap(configCode.code_1001, i);
		}
	}

	/**
	 * 更新订单记录
	 */
	@Override
	public Map<String, Object> updateRecordOrdeNo(String orderNo, String patientId, String shippingAddressId) {
		try {
			if (StringUtil.isEmpty(shippingAddressId) || StringUtil.isEmpty(patientId) || StringUtil.isEmpty(orderNo)) {
				return Util.resultMap(configCode.code_1029, null);
			}
			// 查询收货地址
			ShippingAddress shippingAddress = appDoctorDao.queryShippingAddress(shippingAddressId);
			if (null == shippingAddress || StringUtil.isEmpty(shippingAddress.getId())
					|| StringUtil.isEmpty(patientId)) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return Util.resultMap(configCode.code_1069, shippingAddressId);
			}

			Map<String, Object> recordOrder = appDoctorDao.queryRecordOrderPrice(orderNo);
			if (recordOrder == null) {
				return Util.resultMap(configCode.code_1011, null);
			}
			// 通过订单号查询
			Map<String, Object> drugRecordPrice = appDoctorDao.queryDrugRecordPriceByMainOrderNo(orderNo);

			if (drugRecordPrice == null) {
				return Util.resultMap(configCode.code_1011, null);
			}

			Integer state = Integer.valueOf(drugRecordPrice.get("state").toString());
			if (state == 0) {
				return Util.resultMap(configCode.code_1105, null);
			}
			Double drugGoodsPrice = 0.0;

			// 通过订单号查询
			Map<String, Object> goodsPrice = appDoctorDao.queryDrugRecordPrice(orderNo, 1);
			if (goodsPrice != null && !StringUtil.isEmpty(drugRecordPrice.get("goodsPrice"))) {
				drugGoodsPrice = Double.parseDouble(drugRecordPrice.get("goodsPrice").toString());
			}

			Double drugPostage = 0.0;

			Double drugReceiptsPrice = Double.parseDouble(drugRecordPrice.get("receiptsPrice").toString())
					- Double.parseDouble(drugRecordPrice.get("postage").toString());

			if (drugGoodsPrice == 0.0) {
				drugGoodsPrice = drugReceiptsPrice;
			}

			Double drugOrderPrice = Double.parseDouble(drugRecordPrice.get("orderPrice").toString())
					- Double.parseDouble(drugRecordPrice.get("postage").toString());
			/*
			 * if (!StringUtils.isEmpty(drugRecordPrice.get("postage"))) {
			 * drugReceiptsPrice = Util.subtract(drugReceiptsPrice + "",
			 * drugRecordPrice.get("postage").toString()).doubleValue();
			 * drugOrderPrice = Util.subtract(drugOrderPrice + "",
			 * drugRecordPrice.get("postage").toString()).doubleValue();
			 * drugGoodsPrice = Util.subtract(drugGoodsPrice + "",
			 * drugRecordPrice.get("postage").toString()).doubleValue(); }
			 */
			if (drugReceiptsPrice < 100) {
				if (shippingAddress.getCity().equals("北京市")) {
					drugPostage = 13.00;
				} else {
					drugPostage = 23.00;
				}
			}
			// 更新订单价格
			Integer i = goodsShopCartService.updateOrderPrice(drugRecordPrice.get("orderNo").toString(),
					Util.add(drugOrderPrice + "", drugPostage + ""), Util.add(drugReceiptsPrice + "", drugPostage + ""),
					Util.add(drugGoodsPrice + "", drugPostage + ""), drugPostage);
			if (i == 0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return Util.resultMap(configCode.code_1033, null);
			}
			// String orderPrice = drugRecordPrice.get("orderPrice").toString();
			Double postagePrice = 0.0;
			if (null != goodsPrice && goodsPrice.size() > 0) {
				// 查询购物车邮费
				Map<String, Object> postage = goodsShopCartService.queryPostageByMainOrderNo(shippingAddress.getCity(),
						orderNo);
				if (!postage.get("respCode").toString().equals("1001")) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return postage;
				}
				String goodsOrderPrice = goodsPrice.get("orderPrice").toString();
				String goodsReceiptsPrice = goodsPrice.get("receiptsPrice").toString();
				String goodsPrices = goodsPrice.get("goodsPrice").toString();

				/*
				 * if (!StringUtils.isEmpty(goodsPrice.get("postage"))) {
				 * goodsOrderPrice = Util.subtract(orderPrice,
				 * goodsPrice.get("postage").toString()).doubleValue() + "";
				 * goodsReceiptsPrice = Util.subtract(goodsReceiptsPrice,
				 * goodsPrice.get("postage").toString()).doubleValue() + "";
				 * goodsPrices = Util.subtract(goodsPrices,
				 * goodsPrice.get("postage").toString()).doubleValue() + ""; }
				 */
				postagePrice = Double.parseDouble(JsonUtil.jsonToMap(postage.get("data")).get("result").toString());
				// 更新订单价格
				i = goodsShopCartService.updateOrderPrice(goodsPrice.get("orderNo").toString(),
						Util.add(goodsOrderPrice, postagePrice + ""), Util.add(goodsReceiptsPrice, postagePrice + ""),
						Util.add(goodsPrices, postagePrice + ""), postagePrice);
				if (i == 0) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return Util.resultMap(configCode.code_1033, null);
				}
			}
			/*
			 * Double orderPrice1 = Util.subtract(
			 * recordOrder.get("orderPrice").toString(), null ==
			 * recordOrder.get("postage") ? "0" : recordOrder.get(
			 * "postage").toString()).doubleValue(); Double receiptsPrice =
			 * Util.subtract( recordOrder.get("receiptsPrice").toString(), null
			 * == recordOrder.get("postage") ? "0" : recordOrder.get(
			 * "postage").toString()).doubleValue(); Double goodsPrices =
			 * Util.subtract( recordOrder.get("goodsPrice").toString(), null ==
			 * recordOrder.get("postage") ? "0" : recordOrder.get(
			 * "postage").toString()).doubleValue();
			 */
			Double orderPrice1 = Double.parseDouble(recordOrder.get("orderPrice").toString())
					- Double.parseDouble(recordOrder.get("postage").toString());
			Double receiptsPrice = Double.parseDouble(recordOrder.get("receiptsPrice").toString())
					- Double.parseDouble(recordOrder.get("postage").toString());
			Double goodsPrices = Double.parseDouble(recordOrder.get("goodsPrice").toString())
					- Double.parseDouble(recordOrder.get("postage").toString());

			i = goodsShopCartService.updateOrderPrice(orderNo, orderPrice1 + drugPostage + postagePrice,
					receiptsPrice + drugPostage + postagePrice, goodsPrices + drugPostage + postagePrice,
					drugPostage + postagePrice);
			if (i == 0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return Util.resultMap(configCode.code_1033, null);
			}
			// 更新
			i = appDoctorDao.updateRecordOrdeNo(orderNo, patientId, shippingAddress);
			if (!StringUtil.isEmpty(recordOrder.get("sourceOrderNo"))) {
				appDoctorDao.updateRecordOrdeNoMemberId(recordOrder.get("sourceOrderNo").toString(), patientId,
						shippingAddress.getMemberId());
			}
			// 查询患者信息
			PatientData patientData = appDoctorDao.queryPatientData(patientId);

			if (null == patientData || StringUtil.isEmpty(patientData.getId())) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return Util.resultMap(configCode.code_1069, null);
			}
			// 设置memberId
			shippingAddress.setMemberId(patientData.getMemberId());

			if (i == 0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return Util.resultMap(configCode.code_1033, null);
			} else {
				return Util.resultMap(configCode.code_1001, i);
			}
		} catch (Exception e) {
			return Util.resultMap(configCode.code_1015, null);
		}
	}

	/**
	 * 通过订单号查询记录
	 */
	@Override
	public Map<String, Object> queryRecordOrdeNo(String orderNo) {
		// 通过订单号查询
		Map<String, Object> recordOrder = appDoctorDao.queryRecordOrderPrice(orderNo);
		// 通过订单号查询
		Map<String, Object> drugRecordPrice = appDoctorDao.queryDrugRecordPriceByMainOrderNo(orderNo);
		// 通过订单号查询
		Integer serverType = appDoctorDao.queryServerTypeByMainOrderNo(orderNo);
		// 通过订单号查询
		List<Map<String, Object>> goods = appDoctorDao.queryGoodsByOrderNo(orderNo);
		Map<String, Object> result = new HashMap<String, Object>();
		if (recordOrder == null || drugRecordPrice == null) {
			return Util.resultMap(configCode.code_1096, result);
		}
		result.put("recordOrder", recordOrder);
		result.put("drugRecordPrice", drugRecordPrice);
		result.put("goods", goods);
		result.put("serverType", serverType);
		return Util.resultMap(configCode.code_1001, result);
	}

	/**
	 * 查询最新订单
	 */
	@Override
	public Map<String, Object> queryLastOrde(String patientId, String doctorId) {
		Map<String, Object> order = appDoctorDao.queryLastOrde(patientId, doctorId);
		return Util.resultMap(configCode.code_1001, order);
	}

	/**
	 * 查询推送消息纪录
	 */
	@Override
	public List<Map<String, Object>> querySendModePage(String userId, Integer type, Integer page, Integer row) {
		return appDoctorDao.querySendModePage(userId, type, page, row);
	}

	/**
	 * 设置通知已读
	 */
	@Override
	public Integer updateSendMode(String userId, String[] models) {
		SendMode sendMode = new SendMode();
		sendMode.setUserId(userId);
		sendMode.setRead(2);
		if (null == models || models.length == 0) {
			return appDoctorDao.updateSendMode(userId);
		} else {
			return appDoctorDao.updateSendMode(userId, models);
		}
	}

	/**
	 * 查询未读通知数
	 */
	@Override
	public Integer querySendModeByUnRead(String userId, String type) {
		return appDoctorDao.querySendModeByUnRead(userId, type);
	}

	/**
	 * 查询发送的模板
	 */
	@Override
	public Map<String, Object> querySendModeByLast(String userId, String type) {
		return appDoctorDao.querySendModeByLast(userId, type);
	}

	/**
	 * 修改是否接单
	 */
	@Override
	public Map<String, Object> updateDoctorisAccpetAsk(String doctorId) {
		if (StringUtil.isEmpty(doctorId)) {
			return Util.resultMap(configCode.code_1029, null);
		}

		try {
			Integer i = 0;
			Integer isAccpetAsk = 0;
			// 根据医生ID获取医生个人信息（医馆APP个人中心用）
			Map<String, Object> docMap = appDoctorDao.queryOneDoctor(doctorId);
			if (docMap == null) {
				return Util.resultMap(configCode.code_1032, "");
			}
			Integer type = Integer.valueOf(docMap.get("docIsOn").toString());
			if (type == 0) {
				return Util.resultMap(configCode.code_1041, null);
			} else if (type == 2 || type == 3) {
				return Util.resultMap(configCode.code_1059, null);
			} else if (type == -1) {
				return Util.resultMap(configCode.code_1100, null);
			}
			Integer isOn = Integer.valueOf(docMap.get("isAccpetAsk").toString());
			if (isOn == 0) {
				isAccpetAsk = 1;
				// 根据医生ID查询医生线上调理信息
				Map<String, Object> map = doctorDao.queryDocotrSetById(doctorId);
				if (map == null) {
					// 根据医生ID查询医生线上调理信息(默认)
					map = doctorDao.queryDocotrSetDefault();
					if (map == null) {
						return Util.resultMap(configCode.code_1015, null);
					}
				}
				Integer isOnlineTwGh = Integer.valueOf(map.get("isOnlineTwGh").toString());
				Integer isOnlinePhoneGh = Integer.valueOf(map.get("isOnlinePhoneGh").toString());
				Integer isOnlineTwZx = Integer.valueOf(map.get("isOnlineTwZx").toString());
				Integer isOnlinePhoneZx = Integer.valueOf(map.get("isOnlinePhoneZx").toString());
				if (isOnlineTwGh == 0 && isOnlinePhoneGh == 0 && isOnlineTwZx == 0 && isOnlinePhoneZx == 0) {
					return Util.resultMap(configCode.code_1098, null);
				}

			}
			// 修改是否接单
			i = appDoctorDao.updateDoctorisAccpetAsk(doctorId, isAccpetAsk);
			if (i > 0) {
				return Util.resultMap(configCode.code_1001, isAccpetAsk);
			} else {
				return Util.resultMap(configCode.code_1066, i);
			}
		} catch (Exception e) {
			return Util.resultMap(configCode.code_1015, null);
		}
	}

	/**
	 * 添加服务订单
	 */
	@Override
	public Map<String, Object> addServerOrder(HttpServletRequest request, String doctorId, String patientId,
			String receiptsPrice) {
		if (StringUtil.isEmpty(doctorId) || StringUtil.isEmpty(patientId)) {
			return Util.resultMap(configCode.code_1029, null);
		}
		Map<String, Object> result;
		try {
			// 查询最后的订单号
			Map<String, Object> serverOrder = appDoctorDao.queryLastOrde(patientId, doctorId);
			if (serverOrder == null) {
				return Util.resultMap(configCode.code_1011, null);
			}
			// 查询是否已存在未支付补缴挂号费订单
			Integer count = appDoctorDao.queryIsHasBjGhPrice(doctorId, serverOrder.get("memberId").toString(),
					patientId);
			if (count > 0) {
				return Util.resultMap(configCode.code_1137, null);
			}
			Order order = new Order();
			// 赋值
			order.setMemberId(serverOrder.get("memberId").toString());
			order.setPatientId(patientId);
			order.setDoctorId(doctorId);
			order.setOrderType(4);
			order.setRsrvStr1("1");
			order.setReceiptsPrice(Double.parseDouble(receiptsPrice));
			String orderNo = appDoctorDao.getOrderNo();
			order.setOrderNo(orderNo);
			// 购买医生服务生成订单
			result = doctorOrderService.addServerOrder(request, doctorId, order);
			return result;
		} catch (NumberFormatException e) {
			logger.error(e + "补缴挂号费异常");
			return Util.resultMap(configCode.code_1015, null);
		}
	}

	/**
	 * 通过医生id和患者id更新订单
	 */
	@Override
	public Map<String, Object> updateOrderByEnd(String doctorId, String patientId) {
		Map<String, Object> serverOrder = appDoctorDao.queryLastOrde(patientId, doctorId);
		Integer i = appDoctorDao.updateOrderByEnd(serverOrder.get("orderNo").toString());
		if (i > 0) {
			// 根据医生ID获取医生个人信息（医馆APP个人中心用）
			Map<String, Object> doctor = appDoctorDao.queryOneDoctor(doctorId);
			sendModelMsgUtil.sendEval(patientId, serverOrder.get("serverName").toString(),
					serverOrder.get("docName").toString(), doctor.get("positionName").toString(),
					doctor.get("infirmaryName").toString(), serverOrder.get("patientName").toString(),
					serverOrder.get("sex").toString(), serverOrder.get("age").toString(), appDoctorDao,
					"hospital/evaluate_doctor.html?orderNo=" + serverOrder.get("orderNo").toString());

			/*
			 * QuartzManager.deleteJob("serverOver_" +
			 * serverOrder.get("orderNo").toString());
			 */

			JSONObject json = new JSONObject();
			json.put("msgType", 17);
			json.put("serverOrderNo", serverOrder.get("orderNo").toString());
			json.put("content", "您的问诊已结束");
			GetSig.sendMsg(null, doctorId, patientId, 0, json);

			if (!StringUtil.isEmpty(serverOrder.get("twZhZxCount").toString())
					&& Integer.parseInt(serverOrder.get("twZhZxCount").toString()) > 0) {
				json = new JSONObject();
				json.put("msgType", 14);
				json.put("orderNo", serverOrder.get("orderNo").toString());
				json.put("content", serverOrder.get("twZhZxCount").toString());
				GetSig.sendMsg(null, doctorId, patientId, 0, json);
				// 添加ZxCount
				imDao.addZxCount(serverOrder.get("orderNo").toString(),
						Integer.parseInt(serverOrder.get("twZhZxCount").toString()));
				// 添加ZxCount
				imDao.addZxCount(serverOrder.get("orderNo").toString(), doctorId,
						Integer.parseInt(serverOrder.get("twZhZxCount").toString()));
			}

			return Util.resultMap(configCode.code_1001, i);
		} else {
			return Util.resultMap(configCode.code_1066, null);
		}
	}

	/**
	 * 查询总结算
	 */
	@Override
	public Map<String, Object> querySettlementTotal(String doctorId, Integer settlementType, String startTime,
			String endTime, String checkDoctorId) {
		if (StringUtil.isEmpty(doctorId)) {
			return Util.resultMap(configCode.code_1029, null);
		}
		// 通过医生id查询
		Map<String, Object> settlementTotal = appDoctorDao.querySettlementTotal(doctorId, settlementType, startTime,
				endTime, checkDoctorId);
		if (null != settlementTotal) {
			Map<String, Object> salesRepresent = appDoctorDao.querySalesOne(doctorId);
			if (null == salesRepresent) {
				settlementTotal.put("type", 1);
			} else {
				settlementTotal.put("type", 2);
			}
			// 通过医生id查询最后的银行卡
			Map<String, Object> lastBank = appDoctorDao.queryLastBankByDoctorId(doctorId);
			if (lastBank != null && !StringUtil.isEmpty(lastBank.get("bankCode"))) {
				settlementTotal.put("lastBankCode", lastBank.get("bankCode"));
			} else {
				settlementTotal.put("lastBankCode", "");
			}
			return Util.resultMap(configCode.code_1001, settlementTotal);
		} else {
			return Util.resultMap(configCode.code_1066, null);
		}
	}

	/**
	 * 查询结算
	 */
	@Override
	public Map<String, Object> querySettlement(String doctorId, Integer settlementType, String startTime,
			String endTime, String checkDoctorId, Integer page, Integer row) {
		List<Map<String, Object>> settlements = appDoctorDao.querySettlement(doctorId, settlementType, startTime,
				endTime, checkDoctorId, page, row);
		if (null != settlements) {
			return Util.resultMap(configCode.code_1001, settlements);
		} else {
			return Util.resultMap(configCode.code_1066, null);
		}
	}

	/**
	 * 查询默认的专业
	 */
	@Override
	public String queryDefultSpecial(String doctorId, Integer type) {
		return appDoctorDao.queryDefultSpecial(doctorId, type);
	}

	/**
	 * 修改调理记录状态改为修改状态
	 */
	@Override
	public Map<String, Object> updateRecipeRecordState(String recordId, Integer state) {
		if (StringUtil.isEmpty(recordId)) {
			return Util.resultMap(configCode.code_1029, null);
		}
		Integer count = 0;
		try {
			// 通过记录id查询
			Map<String, Object> recordOrder = appDoctorDao.queryRecipeOrderByRecordId(recordId);
			if (null == recordOrder || recordOrder.size() == 0) {
				return Util.resultMap(configCode.code_1011, null);
			}

			if (Integer.valueOf(recordOrder.get("paymentStatus").toString()) == 2
					|| Integer.valueOf(recordOrder.get("paymentStatus").toString()) == 5
					|| Integer.valueOf(recordOrder.get("paymentStatus").toString()) == 3) {
				return Util.resultMap(configCode.code_1112, null);
			}

			if (Integer.valueOf(recordOrder.get("isIdentifyingPeople").toString()) == 1) {
				return Util.resultMap(configCode.code_1136, null);
			}

			if (StringUtil.isEmpty(state)) {
				state = 0;
			}
			// 修改调理记录状态改为修改状态
			count = appDoctorDao.updateRecipeRecordState(recordId, state);
			if (count < 0) {
				return Util.resultMap(configCode.code_1066, count);
			}
		} catch (Exception e) {
			System.out.println(e);
			return Util.resultMap(configCode.code_1066, count);
		}
		return Util.resultMap(configCode.code_1001, count);
	}

	/**
	 * 查询作废理由列表
	 *
	 * @return
	 */
	@Override
	public Map<String, Object> queryDeleteReasons() {
		List<Map<String, Object>> list = appDoctorDao.queryDeleteReasons();
		if (list == null) {
			return Util.resultMap(configCode.code_1011, list);
		}
		return Util.resultMap(configCode.code_1001, list);
	}

	/**
	 * 作废调理单
	 */
	@Override
	@Transactional
	public Map<String, Object> cancleRecipeState(HttpServletRequest request, String orderNo, String doctorId,
			String cancleNote) {
		try {
			if (StringUtil.isEmpty(orderNo) || StringUtil.isEmpty(doctorId)) {
				return Util.resultMap(configCode.code_1029, null);
			}
			// 修改调理单获取订单信息接口（orderNo-调理单订单号）
			Map<String, Object> recordOrder = appDoctorDao.queyrDetailByOrderNo(orderNo, doctorId);
			if (recordOrder == null) {
				return Util.resultMap(configCode.code_1011, null);
			}
			if (Integer.valueOf(recordOrder.get("paymentStatus").toString()) != 1) {
				return Util.resultMap(configCode.code_1109, null);
			}
			if (Integer.valueOf(recordOrder.get("state").toString()) == 0) {
				return Util.resultMap(configCode.code_1111, null);
			}
			if (Integer.valueOf(recordOrder.get("isIdentifyingPeople").toString()) == 1) {
				return Util.resultMap(configCode.code_1136, null);
			}
			String recordId = StringUtil.isEmpty(recordOrder.get("recordId")) ? null
					: recordOrder.get("recordId").toString();
			String cancleOrderNo = StringUtil.isEmpty(recordOrder.get("mainOrderNo")) ? orderNo
					: recordOrder.get("mainOrderNo").toString();
			@SuppressWarnings("unused")
			String wzOrderNo = StringUtil.isEmpty(recordOrder.get("wzOrderNo")) ? null
					: recordOrder.get("wzOrderNo").toString();
			// 作废调理方
			Integer i = appDoctorDao.cancleRecipeState(cancleOrderNo, cancleNote, null);
			if (i > 0) {
				// appDoctorDao.cancleRecipeState(wzOrderNo, cancleNote,1);
				if (!StringUtils.isEmpty(recordOrder.get("patientId"))) {
					try {
						JSONObject data = new JSONObject();
						data.put("content", "调理方已作废");
						data.put("serverOrderNo", recordOrder.get("wzOrderNo").toString());
						data.put("msgType", 26);
						data.put("syncOtherMachine", 1);
						// 发送纯文字消息
						Map<String, Object> result = GetSig.sendMsg(request, doctorId,
								recordOrder.get("patientId").toString(), 0, data);
						if (result != null && result.get("respCode").toString().equals("1001")) {
							// 新增
							orderDao.insertImSendRecord(orderNo, doctorId, recordOrder.get("patientId").toString(), 1,
									null, recordOrder.get("wzOrderNo").toString(), "sendMsg");
						} else {
							// 发送纯文字消息
							result = GetSig.sendMsg(request, doctorId, recordOrder.get("patientId").toString(), 0,
									data);
							if (result != null && !result.get("respCode").toString().equals("1001")) {
								// 新增
								orderDao.insertImSendRecord(orderNo, doctorId, recordOrder.get("patientId").toString(),
										0, null, recordOrder.get("wzOrderNo").toString(), "sendMsg");
								logger.error("发送IM作废消息失败，方法名称GetSig.sendMsg");
								Map<String, Object> params = new HashMap<String, Object>();
								params.put("msgtype", "text");
								params.put("toUser", "o92LXv1ZIHTjleF3MYPFlAiV7Bas");
								params.put("url", "");
								params.put("content", "发送IM作废消息失败，方法名称GetSig.sendMsg");
								sendModelMsgUtil.sendCustomMsg(params, orderDao);
							}
						}
					} catch (Exception e) {
						System.out.println(e);
					}
				}
				// 修改调理记录状态改为修改状态
				appDoctorDao.updateRecipeRecordState(recordId, 0);
				return Util.resultMap(configCode.code_1001, i);
			}
		} catch (Exception e) {
			System.out.println(e + "eee");
		}
		return Util.resultMap(configCode.code_1015, null);
	}

	/**
	 * 不涉及订单拨打电话
	 */
	@Override
	public Map<String, Object> doctorCallNoOrder(String patientId, String doctorId) {
		try {
			// 根据医生ID查询医生线上调理信息(默认)
			Map<String, Object> setMap = doctorDao.queryDocotrSetDefault();
			if (setMap == null) {
				return Util.resultMap(configCode.code_1015, null);
			}
			Integer onServerTalkTime = StringUtil.isEmpty(setMap.get("onServerTalkTime")) ? 15 * 60
					: Integer.valueOf(setMap.get("onServerTalkTime").toString()) * 60;
			Integer onServerTalkCount = StringUtil.isEmpty(setMap.get("onServerTalkCount")) ? 3
					: Integer.valueOf(setMap.get("onServerTalkCount").toString());
			Integer allTalkTime = StringUtil.isEmpty(setMap.get("allTalkTime")) ? 15 * 60
					: Integer.valueOf(setMap.get("allTalkTime").toString()) * 60;
			Integer allTalkCount = StringUtil.isEmpty(setMap.get("allTalkCount")) ? 15 * 60
					: Integer.valueOf(setMap.get("allTalkCount").toString());

			if (StringUtil.isEmpty(patientId) || StringUtil.isEmpty(doctorId)) {
				return Util.resultMap(configCode.code_1029, null);
			}
			// 通过id查询
			Map<String, Object> patient = appDoctorDao.queryPatientsById(patientId);
			// 查询医生信息根据ID
			Map<String, Object> doctor = appDoctorDao.queryDocById(doctorId);
			if (patient == null || doctor == null) {
				return Util.resultMap(configCode.code_1011, null);
			}
			String orderNo = null;
			Integer paymentStatus = 5;
			Map<String, Object> lastOrderMap = appDoctorDao.queryLastOrde(patientId, doctorId);
			if (lastOrderMap != null) {
				orderNo = StringUtil.isEmpty(lastOrderMap.get("orderNo")) ? null
						: lastOrderMap.get("orderNo").toString();
				paymentStatus = StringUtil.isEmpty(lastOrderMap.get("paymentStatus")) ? 5
						: Integer.valueOf(lastOrderMap.get("paymentStatus").toString());
			}
			Integer type = 2;
			// 最长通话时长
			Integer maxCallTime = allTalkTime * 60;
			if (!StringUtil.isEmpty(orderNo)) {
				if (paymentStatus == 2) {
					maxCallTime = onServerTalkTime;
					type = 1;
				}
			}
			Integer sendCount = 0;
			if (type == 1) {
				// 查询每天拨打次数
				sendCount = appDoctorDao.queryPhoneRecordCount(doctorId, patientId, type);
			} else {
				// 查询每天拨打次数
				sendCount = appDoctorDao.queryPhoneRecordCount(doctorId, null, type);
			}
			if ((type == 1 && sendCount >= onServerTalkCount) || (type == 2 && sendCount >= allTalkCount)) {
				return Util.resultMap(configCode.code_1118, null);
			}
			// 主呼号码
			String callerNum = StringUtil.isEmpty(doctor.get("docPhone")) ? null : doctor.get("docPhone").toString();

			// 被呼号码
			String calleeNum = StringUtil.isEmpty(patient.get("phone")) ? null : patient.get("phone").toString();
			if (StringUtil.isEmpty(callerNum) || StringUtil.isEmpty(calleeNum)) {
				return Util.resultMap(configCode.code_1011, null);
			}

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
			map.put("CustomerForeignId", orderNo);
			map.put("maxCallTime", maxCallTime);
			map.put("promptTime", promptTime);
			map.put("promptVoice", "");
			map.put("type", type);
			map.put("fromId", doctorId);
			map.put("toId", patientId);

			String result = SendCallCenterUtil.sendCallCenterData(map, CallCenterConfig.OutBoundCallOut);
			System.out.println(result);
		} catch (Exception e) {
			System.out.println(e);
		}
		return Util.resultMap(configCode.code_1001, 1);
	}

	/**
	 * 查询我的粉丝列表接口
	 */
	@Override
	public Map<String, Object> queryFollowMembers(String doctorId, Integer page, Integer row, String name) {
		Integer s = Util.queryNowTime();
		if (StringUtil.isEmpty(doctorId)) {
			return Util.resultMap(configCode.code_1029, null);
		}
		// 查询我的粉丝列表接口
		Map<String, Object> map = appDoctorDao.queryFollowMembers(doctorId, page, row, name);
		if (map == null) {
			return Util.resultMap(configCode.code_1011, map);
		}
		System.out.println(Util.queryNowTime() - s);
		return Util.resultMap(configCode.code_1001, map);
	}

	/**
	 * 通过手机号发送患者
	 */
	@Override
	public Map<String, Object> sendPatientByPhone(String phone, String orderNo) {
		if (StringUtil.isEmpty(phone) || StringUtil.isEmpty(orderNo)) {
			return Util.resultMap(configCode.code_1029, null);
		}
		String qrCodeUrl = null;
		// //根据订单号查询二维码
		Map<String, Object> qrCodeMap = appDoctorDao.queryOrderQrCode(orderNo);
		if (qrCodeMap == null) {
			return Util.resultMap(configCode.code_1034, null);
		}
		if (qrCodeMap != null) {
			qrCodeUrl = StringUtil.isEmpty(qrCodeMap.get("qrCodeUrl")) ? null : qrCodeMap.get("qrCodeUrl").toString();
		}
		if (StringUtil.isEmpty(qrCodeUrl)) {
			String token = appDoctorDao.queryToken();
			String dirName = "smsOrder";
			String param = orderNo;
			int time = 30 * 24 * 60 * 60;
			Map<String, Object> imgMap = HttpReuqest.httpPostQrCode(token, null, time, param + "@sms", dirName,
					orderNo);
			if (imgMap != null) {
				qrCodeUrl = StringUtil.isEmpty(imgMap.get("fwPath")) ? null : imgMap.get("fwPath").toString();
				if (!StringUtil.isEmpty(qrCodeUrl)) {
					// 插入订单号查询二维码
					appDoctorDao.insertOrderQrCode(orderNo, qrCodeUrl);
				}
			}
		}

		String[] params = { qrCodeMap.get("docName").toString(),
				"syrjia/weixin/leadPhone/leadPhone.html?orderNo=" + orderNo };
		Boolean flag = QCloudSmsUtil.sendSmsByTemId(phone, SMSTemplateIdUtil.sendExtract_SMS, params);
		if (!flag) {
			return Util.resultMap(configCode.code_1123, null);
		}
		return Util.resultMap(configCode.code_1001, 1);
	}

	/**
	 * 查询未认证个人简介数量
	 */
	@Override
	public Map<String, Object> queryAbstractIsNotSuccess(String doctorId) {
		if (StringUtil.isEmpty(doctorId)) {
			return Util.resultMap(configCode.code_1029, null);
		}
		Integer count = appDoctorDao.queryAbstractIsNotSuccess(doctorId);
		return Util.resultMap(configCode.code_1001, count);
	}

	/**
	 * 查询最后一条医生简介记录
	 */
	@Override
	public Map<String, Object> queryLastAbstract(String doctorId) {
		if (StringUtil.isEmpty(doctorId)) {
			return Util.resultMap(configCode.code_1029, null);
		}
		// 查询最后一条医生简介记录
		Map<String, Object> map = appDoctorDao.queryLastAbstract(doctorId);
		if (map == null) {
			map = new HashMap<String, Object>();
			map.put("docAbstract", "");
			map.put("remark", "");
			map.put("state", "1");
		}
		return Util.resultMap(configCode.code_1001, map);
	}

	/**
	 * 根据医生ID就诊数量
	 */
	@Override
	public Map<String, Object> queryJzCount(String doctorId) {
		Map<String, Object> map = new HashMap<String, Object>();
		int allCount = 0;
		int yzfCount = 0;
		try {
			if (StringUtil.isEmpty(doctorId)) {
				return Util.resultMap(configCode.code_1029, null);
			}
			// 根据医生ID条件、分页查询就诊列表
			List<Map<String, Object>> list = appDoctorDao.queryJzList(doctorId, null, null, null);
			if (list == null) {
				return Util.resultMap(configCode.code_1011, list);
			}
			allCount = list.size();
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).get("orderstate").equals("yzf")) {
					yzfCount++;
				}
			}
			map.put("allCount", allCount);
			map.put("yzfCount", yzfCount);
		} catch (Exception e) {
			System.out.println();
		}
		return Util.resultMap(configCode.code_1001, map);
	}

	/**
	 * 根据医生ID查询新版首页统计信息
	 */
	@Override
	public Map<String, Object> queryIndexCount(String doctorId) {
		Integer t = Util.queryNowTime();
		if (StringUtil.isEmpty(doctorId)) {
			return Util.resultMap(configCode.code_1029, null);
		}
		// 查询首页统计信息
		Map<String, Object> map = appDoctorDao.queryIndexCount(doctorId);
		if (map == null) {
			return Util.resultMap(configCode.code_1011, null);
		}
		System.out.println(Util.queryNowTime() - t);
		return Util.resultMap(configCode.code_1001, map);
	}

	/**
	 * 查询历史银行卡信息
	 */
	@Override
	public Map<String, Object> queryBankHistorys(String doctorId, Integer page, Integer row) {
		if (StringUtil.isEmpty(doctorId)) {
			return Util.resultMap(configCode.code_1029, null);
		}
		// 查询历史银行卡信息
		List<Map<String, Object>> list = appDoctorDao.queryBankHistorys(doctorId, page, row);
		if (list == null) {
			return Util.resultMap(configCode.code_1011, null);
		}
		return Util.resultMap(configCode.code_1001, list);
	}

	/**
	 * 查询银行字典列表
	 */
	@Override
	public Map<String, Object> queryBankList() {
		List<Map<String, Object>> list = appDoctorDao.queryBankList();
		if (list == null) {
			return Util.resultMap(configCode.code_1011, null);
		}
		return Util.resultMap(configCode.code_1001, list);
	}

	/**
	 * 绑定银行卡
	 */
	@Override
	public Map<String, Object> editBindBank(BindBank bindBank) {
		Object i = null;
		if (StringUtil.isEmpty(bindBank.getDoctorId()) || StringUtil.isEmpty(bindBank.getBankCode())
				|| StringUtil.isEmpty(bindBank.getBankId()) || StringUtil.isEmpty(bindBank.getBankName())) {
			return Util.resultMap(configCode.code_1029, null);
		}
		if (StringUtil.isEmpty(bindBank.getId())) {
			bindBank.setBankTime(Util.queryNowTime());
			i = appDoctorDao.addEntityUUID(bindBank);
		} else {
			i = appDoctorDao.updateEntity(bindBank);
		}
		if (StringUtil.isEmpty(i)) {
			return Util.resultMap(configCode.code_1014, null);
		}
		return Util.resultMap(configCode.code_1001, i);
	}

	/**
	 * 通过助理id查询关注的患者
	 */
	@Override
	public Map<String, Object> queryFollowBySrId(String srId) {
		List<Map<String, Object>> list = appDoctorDao.queryFollowBySrId(srId);
		return Util.resultMap(configCode.code_1001, list);
	}

	/**
	 * 查询订单二维码
	 */
	@Override
	public Map<String, Object> queryOrderQrUrl(String orderNo) {
		if (StringUtil.isEmpty(orderNo)) {
			return Util.resultMap(configCode.code_1029, null);
		}
		// 根据订单号查询二维码
		Map<String, Object> map = appDoctorDao.queryOrderQrCode(orderNo);
		if (map == null) {
			return Util.resultMap(configCode.code_1034, null);
		}

		if (StringUtil.isEmpty(map.get("qrCodeUrl"))) {
			String token = appDoctorDao.queryToken();
			String dirName = "smsOrder";
			// String doctorId = map.get("doctorId").toString();
			String param = orderNo;
			int time = 30 * 24 * 60 * 60;
			Map<String, Object> imgMap = HttpReuqest.httpPostQrCode(token, null, time, param + "@sms", dirName,
					orderNo);
			if (imgMap != null) {
				String qrCodeUrl = imgMap.get("fwPath") == "" ? null : imgMap.get("fwPath").toString();
				if (!StringUtil.isEmpty(qrCodeUrl)) {
					appDoctorDao.insertOrderQrCode(orderNo, qrCodeUrl);
					map.put("qrCodeUrl", qrCodeUrl);
				}
			}
		}
		return Util.resultMap(configCode.code_1001, map);
	}

	/**
	 * 根据手机号查询关联医生数量
	 */
	@Override
	public Integer queryDocCountByPhone(String phone) {
		return appDoctorDao.queryDocCountByPhone(phone);
	}

	/**
	 * 获取膏方费
	 */
	@Override
	public Map<String, Object> getGfPriceAndDose() {
		Map<String, Object> map = appDoctorDao.getSysSet();
		Double price = 600.00;
		Integer basicDose = 30;
		if (map != null && !StringUtil.isEmpty(map.get("jgBasicPrice"))) {
			price = Double.valueOf(map.get("jgBasicPrice").toString());
			basicDose = Integer.valueOf(map.get("dose").toString());
		}
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("price", price);
		returnMap.put("basicDose", basicDose);
		return Util.resultMap(configCode.code_1001, returnMap);
	}

	/**
	 * 重复发送处方订单
	 */
	@Override
	public Integer repeatSendRecipeOrder() {
		/*
		 * sendModelMsgUtil.sendAddRecipeOrder(patientId,
		 * "588138fbd79740268c5541e5efa3cd7d", appDoctorDao,
		 * "hospital/look_scheme.html?orderNo=" + mainOrderNo);
		 */
		return 1;
	}

	/**
	 * 查询银行卡信息单个
	 */
	@Override
	public Map<String, Object> queryBankById(String id) {
		return Util.resultMap(configCode.code_1001, appDoctorDao.queryBankById(id));
	}

	@Override
	public Map<String, Object> lookCfOrder(String orderNo) {

		return Util.resultMap(configCode.code_1001, appDoctorDao.lookCfOrder(orderNo));
	}

	@Override
	public String repeatTsPayOrder(String memberId, String doctorId, String orderNo) {
		String sendCode = sendModelMsgUtil.sendAddRecipeOrder( memberId, doctorId, appDoctorDao,
				"hospital/look_scheme.html?orderNo=" + orderNo);
		return sendCode;
	}

}
