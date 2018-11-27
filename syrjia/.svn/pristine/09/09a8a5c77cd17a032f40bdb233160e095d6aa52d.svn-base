package cn.syrjia.dao.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import cn.syrjia.common.impl.BaseDaoImpl;
import cn.syrjia.dao.MemberDao;

@Repository("memberDao")
public class MemberDaoImpl extends BaseDaoImpl implements MemberDao {
	// 日志
	private Log logger = LogFactory.getLog(MemberDaoImpl.class);

	/**
	 * 注销 清空token
	 */
	@Override
	public Integer logout(String userid) {
		Integer i = 0;
		try {
			String sql = "update t_member set token = '',openid = '' where id = ?";
			//执行更新
			if(!StringUtils.isEmpty(userid)){
				i = super.update(sql, new Object[]{userid});
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return i;
	}

	/**
	 * 根据微信openid获取微信用户
	 */
	@Override
	public Map<String, Object> getWeixinByOpenid(String openid) {
		String sql = "select * from t_weixin_user where openid = '" + openid
				+ "'";
		//执行查询
		return this.queryBysqlMap(sql, null);
	}

	/**
	 * 根据登录名查找用户
	 */
	@Override
	public Map<String, Object> queryByLoginname(String loginname,String memberId) {
		Map<String, Object> memberMap = new HashMap<String, Object>();
		//拼接sql
		String sql = "select u.* from t_member u where u.state=1 ";
		if(!StringUtils.isEmpty(memberId)||!StringUtils.isEmpty(loginname)){
			//登录名
			if(!StringUtils.isEmpty(loginname)){
				sql +=" and loginname= '"+loginname+"'";
			}
			//用户id
			if(!StringUtils.isEmpty(memberId)){
				sql +=" and id= '"+memberId+"'";
			}
			memberMap = super.queryBysqlMap(sql, null);
		}
		return memberMap;
	}

	/**
	 * 更新用户生日
	 */
	@Override
	public Integer updateUserBirthday(String userId, String birthday) {
		Integer i = 0;
		try {
			String sql = " update t_member set birthday=? where id =? ";
			if (userId != null && !StringUtils.isEmpty(birthday)) {
				//执行更新
				i = super.update(sql, new Object[] { birthday, userId });
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return i;
	}

	/**
	 * 用户开关
	 */
	@Override
	public Map<String, Object> userClockOn(HttpServletRequest request,
			String userid, Integer state) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String sql = "SELECT u.* from t_user_clock_record u where u.userid=? and curdate()=FROM_UNIXTIME(u.clockTime,'%Y-%m-%d') ";
			if (userid != null) {
				//执行查询
				map = super.queryBysqlMap(sql, new Object[] { userid });
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return map;
	}

	/**
	 * 更新密码
	 */
	@Override
	public Integer updatePassWord(String password, String loginName) {
		Integer i = 0;
		try {
			String sql = "UPDATE t_member set `password`=? where loginname=?  ";
			if(!StringUtils.isEmpty(loginName)&&!StringUtils.isEmpty(password)){
				//执行更新
				i = super.update(sql, new Object[]{password,loginName});
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return i;
	}

	/**
	 * 通过openid查询
	 */
	@Override
	public Integer clearByOpenid(String openid) {
		Integer i = 0;
		try {
			String sql = "UPDATE t_member set openid = '' WHERE openid=?";
			if(!StringUtils.isEmpty(openid)){
				//执行更新
				i = super.update(sql, new Object[]{openid});
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return i;
	}

	/**
	 * 通过openid获取用户
	 */
	@Override
	public Map<String, Object> getMemberByOpenid(String openid) {
		Map<String, Object> map = null;
		try {
			String sql = "SELECT * from t_member WHERE id =? ORDER BY loginTime DESC LIMIT 0,1";
			if(!StringUtils.isEmpty(openid)){
				//执行查询
				map = super.queryBysqlMap(sql, new Object[]{openid});
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return map;
	}
	
	/**
	 * 更新电话号码
	 */
	@Override
	public Integer updatePhone(String phone, String loginName,String memberId) {
		Integer i = 0;
		try {
			String sql = "UPDATE t_member set `phone`=? where id=?  ";
			if(!StringUtils.isEmpty(memberId)&&!StringUtils.isEmpty(phone)){
				if(!StringUtils.isEmpty(loginName)){
					sql +=" and loginname='"+loginName+"' ";
				}
				//执行更新
				i = super.update(sql, new Object[]{phone,memberId});
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return i;
	}

	/**
	 * 根据用户ID查询用户信息
	 */
	@Override
	public Map<String, Object> queryMemberById(String memberId) {
		Map<String, Object> memberMap = new HashMap<String, Object>();
		String sql = "select u.* from t_member u where u.state=1 and u.id=? ";
		if(!StringUtils.isEmpty(memberId)){
			//执行查询
			memberMap = super.queryBysqlMap(sql, new Object[]{memberId});
		}
		return memberMap;
	}

	/**
	 * 检验手机号是否存在
	 */
	@Override
	public Integer checkMemberPhone(String phone) {
		Integer i = 0;
		try{
			if(!StringUtils.isEmpty(phone)){
				String sql = "select count(0) from t_member u where u.state<>3 and u.phone='"+phone+"' ";
				//执行查询
				i = super.queryBysqlCount(sql, null);
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		return i;
	}

	/**
	 * 检验用户是否绑定安全手机号
	 */
	@Override
	public Integer checkMemberIsBindPhone(String memberId) {
		Integer count = 0;
		try {
			String sql = "SELECT count(0) from t_member where id=? and phone is NOT NULL  and phone <> ''";
			if(!StringUtils.isEmpty(memberId)){
				//执行查询
				count = jdbcTemplate.queryForObject(sql, new Object[]{memberId},Integer.class);
			}
		} catch (Exception e) {
			logger.info(e);
		}
		return count;
	}

	/**
	 * 通过id查询member
	 */
	@Override
	public Map<String, Object> queryMemberOne(String id) {
		Map<String, Object> map = null;
		try {
			String sql = "SELECT id,state,phone from t_member WHERE id =? ORDER BY loginTime DESC LIMIT 0,1";
			if(!StringUtils.isEmpty(id)){
				//执行查询
				map = super.queryBysqlMap(sql, new Object[]{id});
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return map;
	}

}
