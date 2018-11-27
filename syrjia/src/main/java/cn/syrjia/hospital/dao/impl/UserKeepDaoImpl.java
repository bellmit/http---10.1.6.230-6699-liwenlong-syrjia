package cn.syrjia.hospital.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import cn.syrjia.common.impl.BaseDaoImpl;
import cn.syrjia.hospital.dao.UserKeepDao;
import cn.syrjia.hospital.entity.UserKeep;
import cn.syrjia.util.StringUtil;

@Repository("userKeepDao")
public class UserKeepDaoImpl extends BaseDaoImpl implements UserKeepDao {

	/**
	 * 根据openid 查询该医生是否已收藏
	 */
	@Override
	public Map<String, Object> queryDoctorId(UserKeep keep) {
		final Map<String,Object> map=new HashMap<String, Object>();
		if(!StringUtil.isEmpty(keep.getOpenid())){
			String sql="select doctorId from h_user_keep where type='"+keep.getType()+"' and openid='"+keep.getOpenid()+"'";
			super.jdbcTemplate.query(sql,new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					map.put(rs.getString(1),"");
				}
			});
		}
		return map;
	}

	/**
	 * 根据条件删除收藏
	 */
	@Override
	public Object deleteUserKeep(UserKeep keep) {
		Object i = 0;
		String sql = "delete u.* from h_user_keep u where type=? and u.doctorId=? and u.openid=? ";
		if(!StringUtil.isEmpty(keep.getDoctorId())&&!StringUtil.isEmpty(keep.getOpenid())){
			i = super.delete(sql, new Object[]{keep.getType(),keep.getDoctorId(),keep.getOpenid()});
		}
		return i;
	}

}
