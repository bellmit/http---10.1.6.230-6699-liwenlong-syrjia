package cn.syrjia.dao.impl;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.beust.jcommander.Strings;

import cn.syrjia.common.impl.BaseDaoImpl;
import cn.syrjia.dao.AppDao;
import cn.syrjia.entity.Advertising;

@Repository("appDao")
public class AppDaoImpl extends BaseDaoImpl implements AppDao{

	// 日志
	private Logger logger = LogManager.getLogger(AppDaoImpl.class);

	/**
	 * 查询广告
	 * @param request
	 * @param advertising
	 * @return
	 */
	@Override
	public Map<String, Object> queryAdvertising(Advertising advertising) {
		String sql="select name,url,imageUrl,seconds,skipSeconds,linkType,data from t_advertising where state=1 and port=? and  startTime<=UNIX_TIMESTAMP() and endTime>=UNIX_TIMESTAMP() order by createTime desc limit 0,1";
		Map<String, Object> map=null;
		try {
			//执行查询
			map = jdbcTemplate.queryForMap(sql,new Object[]{advertising.getPort()});
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return map;
	}

	/**
	 * 根据手机号查询医生信息
	 * @param loginName
	 * @return
	 */
	@Override
	public Integer queryUserByLoginName(String loginName) {
		String sql="select count(1) from t_doctor where  docStatus=10 and docPhone=?";
		Integer i=0;
		try {
			//执行查询
			i = jdbcTemplate.queryForObject(sql,new Object[]{loginName},Integer.class);
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return i;
	}

	/**
	 * 根据手机号查询医生信息
	 * @param loginName
	 * @return
	 */
	@Override
	public String queryDoctorByLoginName(String loginName) {
		String sql="select doctorId from t_doctor where docPhone=? and docStatus=10";
		String doctorId=null;
		try {
			//执行查询
			doctorId = jdbcTemplate.queryForObject(sql,new Object[]{loginName},String.class);
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return doctorId;
	}

	/**
	 * 根据医生id查询医生信息
	 * @param loginName
	 * @return
	 */
	@Override
	public Map<String, Object> queryDoctorById(String userId) {
		String sql="select * from t_doctor where doctorId=? and docStatus=10";
		Map<String, Object> doctor=null;
		try {
			//执行查询
			doctor = jdbcTemplate.queryForMap(sql,new Object[]{userId});
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return doctor;
	}
	
	/**
	 * 查询app最新安装包
	 * @param port
	 * @return
	 */
	@Override
	public Map<String, Object> queryAppByLast(Integer port,Integer phoneType,String version) {
		String sql="select srcPath src,version,type,remarks,appName from t_app where state = 1 and port=? ";
		//判断移动端类型，0-ios、1-android
		if("0".equals(String.valueOf(phoneType))){
			sql += " and phoneType='0' ";
		}else{
			sql += " and phoneType='1' ";
		}
		sql += " order by createTime desc LIMIT 0,1 ";
		Map<String, Object> result=null;
		try {
			//执行查询
			result = jdbcTemplate.queryForMap(sql,new Object[]{port});
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		//如果历史版本不为空，判断历史版本时候有强制更新的版本,
		if(result!=null && !Strings.isStringEmpty(version)){
			String typeSql = " select type from t_app where createTime >=  if ((select createTime from t_app where version ='"+version.trim()+"' and phoneTYpe ='"+phoneType+"') is null , (select createTime from t_app where version ='"+version.trim()+"' and phoneTYpe ='"+phoneType+"'),(select createTime from t_app where phoneTYpe ='"+phoneType+"' order by createTime LIMIT 1))"
					+ " and createTime <= (select createTime from t_app where state = 1 and port='"+String.valueOf(port)+"'  and phoneTYpe='"+phoneType+"'  ORDER BY createTime desc LIMIT 0,1 ) "
					+ " and port='"+String.valueOf(port)+"'  and phoneTYpe='"+phoneType+"'  ORDER BY type asc LIMIT 0,1 ";
			Map<String, Object> typeMap = jdbcTemplate.queryForMap(typeSql,new Object[]{});
			if(result!=null && typeMap!=null){
				if("1".equals(String.valueOf(typeMap.get("type")))){
					result.put("type", "1");
				}
			}
		}
		return result;
	}
	
	/**
	 * 查询app最新物理地址
	 * @param port
	 * @return
	 */
	@Override
	public Map<String, Object> queryAppByLastAddr(Integer port) {
		String sql="select riskPath from t_app where state = 1 and phoneType='1' and port=? order by createTime desc LIMIT 0,1";
		Map<String, Object> doctor=null;
		try {
			//执行查询
			doctor = jdbcTemplate.queryForMap(sql,new Object[]{port});
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return doctor;
	}

	/**
	 * 获取token
	 * @param request
	 * @param phone
	 * @param memberId
	 * @param oldToken
	 * @return
	 */
	@Override
	public Map<String,Object> queryMemberToken(String _token) {
		String sql="select memberId,createTime from t_member_token where _token=? order by createTime desc limit 0,1";
		Map<String,Object> map = null;
		try {
			//执行查询
			map = jdbcTemplate.queryForMap(sql,new Object[]{_token});
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return map;
	}
	
	/**
	 * 根据用户id查询token信息
	 * @param memberId
	 * @return
	 */
	@Override
	public String queryTokenByMemberId(String memberId) {
		String sql="select _token from t_member_token where memberId=? and UNIX_TIMESTAMP()-createTime<60*60*2-100 order by createTime desc limit 0,1";
		String token=null;
		try {
			//执行查询
			token = jdbcTemplate.queryForObject(sql,new Object[]{memberId},String.class);
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return token;
	}
}
