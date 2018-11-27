package cn.syrjia.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import cn.syrjia.common.impl.BaseServiceImpl;
import cn.syrjia.config.Config;
import cn.syrjia.config.configCode;
import cn.syrjia.dao.MemberDao;
import cn.syrjia.entity.Member;
import cn.syrjia.entity.Piclib;
import cn.syrjia.entity.UserAdvise;
import cn.syrjia.entity.UserClockRecord;
import cn.syrjia.service.MemberService;
import cn.syrjia.util.DateTime;
import cn.syrjia.util.GetOpenId;
import cn.syrjia.util.SessionUtil;
import cn.syrjia.util.StringUtil;
import cn.syrjia.util.Util;

@Service("memberService")
public class MemberServiceImpl extends BaseServiceImpl implements MemberService {

	@Resource(name = "config")
	Config config;

	@Resource(name = "memberDao")
	MemberDao memberDao;

	/**
	 * 添加用户
	 */
	@Override
	public Object addMember(Member member) {
		Object a = 0;
		member.setState(1);
		member.setCreatetime(Util.queryNowTime());
		a = memberDao.addEntityUUID(member);
		return a;
	}

	/**
	 * 上传头像
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Map<String, Object> insertUserHeadInfo(MultipartFile image,
			String id, HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			//非空判断
			if (!StringUtils.isEmpty(id)) {
				Member user = memberDao.queryById(Member.class, id);
				// 获取图片并上传
				String filename = DateTime.getDate();
				//上传图片
				Map<String, Object> uploadimage = Util.uploadImage(image,
						request, filename, config.getImgIp());
				Object returnInfo = uploadimage.get("code");
				if (null != returnInfo) {
					setFileError(resultMap, returnInfo);
					return resultMap;
				}
				String fileurl = uploadimage.get("fwPath").toString()
						.replaceAll("\\\\", "/");
				user.setPhoto(fileurl);
				//执行更新
				int obj = memberDao.updateEntity(user);
				if (obj > 0) {
					resultMap.put("respCode", configCode.code_1001);// 成功
					resultMap.put("respMsg",
							configCode.codeDesc(configCode.code_1001));
					resultMap.put("user", user);// 返回logicId
				} else {
					resultMap.put("respCode", configCode.code_1048);// 失败
					resultMap.put("respMsg",
							configCode.codeDesc(configCode.code_1048));
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			// 设置事务回滚
			TransactionAspectSupport.currentTransactionStatus()
					.setRollbackOnly();
			resultMap.put("respCode", configCode.code_1009);// 失败
			resultMap.put("respMsg", configCode.codeDesc(configCode.code_1009));
			resultMap.put("user", "");// 返回logicId
		}
		return resultMap;
	}

	/**
	 * 返回错误信息
	 * @param resultMap
	 * @param returnInfo
	 */
	private void setFileError(Map<String, Object> resultMap, Object returnInfo) {
		if (returnInfo.toString().equals("-1")) {
			resultMap.put("respCode", configCode.code_1012);
			resultMap.put("respMsg", configCode.codeDesc(configCode.code_1012));
		} else if (returnInfo.toString().equals("-2")) {
			resultMap.put("respCode", configCode.code_1013);
			resultMap.put("respMsg", configCode.codeDesc(configCode.code_1013));
		}
		resultMap.put("datas", "error");// 返回logicId
	}

	/**
	 * 注销，清空token信息
	 */
	@Override
	public Integer logout(String userid) {
		return memberDao.logout(userid);
	}

	/**
	 * 根据openid查找微信用户信息
	 */
	@Override
	public Map<String, Object> getWeixinByOpenid(String openid) {
		Map<String, Object> resultMap = new HashMap<>();
		if (StringUtils.isEmpty(openid)) {// 微信用户不存在
			resultMap.put("respCode", configCode.code_1002);
			resultMap.put("respMsg", configCode.codeDesc(configCode.code_1002));
		} else {
			resultMap = memberDao.getWeixinByOpenid(openid);
			if (resultMap == null) {// 微信用户不存在
				resultMap = new HashMap<String, Object>();
				resultMap.put("respCode", configCode.code_1002);
				resultMap.put("respMsg",
						configCode.codeDesc(configCode.code_1002));
			} else {
				resultMap.put("respCode", configCode.code_1001);
				resultMap.put("respMsg",
						configCode.codeDesc(configCode.code_1001));
			}
		}
		return resultMap;
	}

	/**
	 * 根据登录名获取用户信息
	 */
	@Override
	public Map<String, Object> getByLoginname(String loginname,String memberId) {
		return memberDao.queryByLoginname(loginname,memberId);
	}

	/**
	 * 更新用户生日
	 */
	@Override
	public Integer updateUserBirthday(String userId, String birthday) {
		return memberDao.updateUserBirthday(userId, birthday);
	}

	/**
	 * 用户开关
	 */
	@Override
	public Map<String, Object> userClockOn(HttpServletRequest request,
			String userid, Integer state, String _sign) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Object i = 0;
		try {
			if (StringUtils.isEmpty(_sign)
					|| (!StringUtils.isEmpty(_sign) && !"TennisOn"
							.equals(_sign))) {
				resultMap.put("respCode", configCode.code_1059);
				resultMap.put("respMsg",
						configCode.codeDesc(configCode.code_1059));
			} else {
				String openid = SessionUtil.getOpenId(request);
				if (StringUtils.isEmpty(userid)) {// 参数错误
					resultMap.put("respCode", configCode.code_1029);
					resultMap.put("respMsg",
							configCode.codeDesc(configCode.code_1029));
				} else {
					//根据id查询单个
					Member member = memberDao.queryById(Member.class, userid);
					if (member == null || member.getState() == 3) {
						resultMap.put("respCode", configCode.code_1002);
						resultMap.put("respMsg",
								configCode.codeDesc(configCode.code_1002));
					} else {
						String workTime = "7-15";
						if (state == null) {
							state = 0;
						}
						if (state == 0) {
							workTime = "7-15";
						} else {
							workTime = "16-23";
						}
						//查询开关
						Map<String, Object> paramMap = memberDao.userClockOn(
								request, userid, state);
						//创建对象
						UserClockRecord record = new UserClockRecord();
						//赋值
						record.setUserid(userid);
						record.setClockTime((int) (System.currentTimeMillis() / 1000));
						record.setState(state);
						record.setWorkTime(workTime);
						if (paramMap == null) {
							record.setOpenid(StringUtils.isEmpty(openid) ? null
									: openid);
							i = memberDao.addEntity(record);
						} else {
							record.setOpenid(!StringUtils.isEmpty(openid) ? openid
									: paramMap.get("openid") == null ? null
											: paramMap.get("openid").toString());
							i = memberDao.updateEntity(record);
						}
						if (i != null && Integer.valueOf(i.toString()) > 0) {
							resultMap.put("respCode", configCode.code_1001);// 成功
							resultMap.put("respMsg",
									configCode.codeDesc(configCode.code_1001));
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			// 设置事务回滚
			TransactionAspectSupport.currentTransactionStatus()
					.setRollbackOnly();
			resultMap.put("respCode", configCode.code_1015);// 失败
			resultMap.put("respMsg", configCode.codeDesc(configCode.code_1015));
		}
		return resultMap;
	}

	/**
	 * 更新用户的登录数据
	 */
	@Override
	public Map<String, Object> updateUserLoginData(HttpServletRequest request,
			Member umUser) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			if (umUser == null || umUser.getId() == null) {
				resultMap.put("respCode", configCode.code_1029);
				resultMap.put("respMsg",
						configCode.codeDesc(configCode.code_1029));
			} else {
				//修改必须有ID 根据ID
				Integer i = memberDao.updateEntity(umUser);
				if (i > 0) {
					resultMap.put("respCode", configCode.code_1001);// 成功
					resultMap.put("respMsg",
							configCode.codeDesc(configCode.code_1001));
				} else {
					resultMap.put("respCode", configCode.code_1066);// 操作失败
					resultMap.put("respMsg",
							configCode.codeDesc(configCode.code_1066));
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			// 设置事务回滚
			TransactionAspectSupport.currentTransactionStatus()
					.setRollbackOnly();
			resultMap.put("respCode", configCode.code_1015);// 失败
			resultMap.put("respMsg", configCode.codeDesc(configCode.code_1015));

		}
		return resultMap;
	}

	/**
	 * 更新密码
	 */
	@Override
	public Integer updatePassWord(String password, String loginName) {
		return memberDao.updatePassWord(password, loginName);
	}

	/**
	 * 添加用户警告
	 */
	@Override
	public Object addUserAdvise(UserAdvise userAdvise, List<Piclib> piclist,
			HttpServletRequest request) {
		Object i = 0;
		try {
			//查询openId
			String openId = SessionUtil.getOpenId(request);
			if (!StringUtil.isEmpty(openId)) {
				userAdvise
						.setCreateTime((int) (System.currentTimeMillis() / 1000));
				userAdvise.setOpenid(openId);
				userAdvise.setState(0);
				//执行添加
				i = memberDao.addEntity(userAdvise);
				if (i != null && Integer.valueOf(i.toString()) > 0
						&& piclist != null && piclist.size() > 0) {
					//遍历picList
					for (int u = 0; u < piclist.size(); u++) {
						if (!StringUtil.isEmpty(piclist.get(u).getPicPathUrl())) {
							piclist.get(u).setPicId(Util.getUUID());
							piclist.get(u).setStatus("10");
							piclist.get(u).setAdviceId(
									Integer.valueOf(i.toString()));
							piclist.get(u).setStatusDate(
									(int) (System.currentTimeMillis() / 1000));
							memberDao.addEntity(piclist.get(u));
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return i;
	}

	/**
	 * 通过openid清除
	 */
	@Override
	public Integer clearByOpenid(String openid) {
		return memberDao.clearByOpenid(openid);
	}

	/**
	 * 更新手机号
	 */
	@Override
	public Integer updatePhone(HttpServletRequest request,String phone, String loginName,String code,String memberId) {
		if(StringUtil.isEmpty(memberId)){
			memberId = GetOpenId.getMemberId(request);
		}
		//更新
		Integer i = memberDao.updatePhone(phone, loginName,memberId);
		if(i>0){
			memberDao.updateCodeState(phone, 5, code);
		}
		return i;
	}

	/**
	 * 获取验证码
	 */
	@Override
	public Map<String, Object> getPhoneCode(HttpServletRequest request,
			String phone, Integer type, String memberId) {
		// ip
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if(StringUtil.isEmpty(memberId)){
			memberId = GetOpenId.getMemberId(request);
		}
		
		if (StringUtil.isEmpty(phone) || type == null
				|| StringUtils.isEmpty(memberId)) {
			return Util.resultMap(configCode.code_1029, null);
		} else {
			//验证验证码是否存在
			Map<String, Object> codeMap = memberDao.queryCodeByPhoneAndCode(
					phone, null, type);
			if (codeMap != null
					&& (Util.queryNowTime() - Integer.valueOf(codeMap.get(
							"createTime").toString())) < 170) {
				return Util.resultMap(configCode.code_1070, null);
			} else {
				//根据手机号查询获取验证码次数
				Integer num = memberDao.checkSendCodeCount(phone, type);
				if (num > 5) {
					return Util.resultMap(configCode.code_1076, null);
				} else {
					//根据用户ID查询用户信息
					Map<String, Object> map = memberDao
							.queryMemberById(memberId);
					if (map == null) {
						return Util.resultMap(configCode.code_1002, "");
					} else {
						String oldhone = map.get("phone")==null?null:map.get("phone").toString().trim();
						if (type == 4) {// 医生验证老手机号
							if (!StringUtil.isEmpty(oldhone)&&oldhone.equals(phone)) {
								//发送验证码
								return memberDao.sendCode(phone, type, ip);
							} else {
								return Util.resultMap(configCode.code_1043, "");
							}
						} else if (type == 5) {// 医生验证新手机号
							Integer count = memberDao.checkMemberPhone(phone);
							if (count > 0) {
								return Util.resultMap(configCode.code_1042, "");
							} else {
								//发送验证码
								return memberDao.sendCode(phone, type, ip);
							}
						} else {
							return memberDao.sendCode(phone, type, ip);
						}
					}
				}
			}
		}
	}

	/**
	 * 检验用户是否绑定安全手机号
	 */
	@Override
	public Map<String, Object> checkMemberIsBindPhone(HttpServletRequest request,String memberId) {
		if(StringUtil.isEmpty(memberId)){
			memberId = GetOpenId.getMemberId(request);
		}
		//检查
		Integer count = memberDao.checkMemberIsBindPhone(memberId);
		return Util.resultMap(configCode.code_1001, count);
	}

	/**
	 * 根据openId获取member信息
	 */
	@Override
	public Map<String, Object> getMemberByOpenid(String openId) {
		return memberDao.getMemberByOpenid(openId);
	}

	/**
	 * 根据id获取member信息
	 */
	@Override
	public Map<String, Object> queryMemberOne(String id) {
		return memberDao.queryMemberOne(id);
	}
}
