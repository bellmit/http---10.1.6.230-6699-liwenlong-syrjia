package cn.syrjia.dao.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.syrjia.common.impl.BaseDaoImpl;
import cn.syrjia.dao.TcodeDao;
import cn.syrjia.util.StringUtil;

@Repository("tcodeDao")
public class TcodeDaoImpl extends BaseDaoImpl implements TcodeDao {
	private Logger logger = LogManager.getLogger(TcodeDaoImpl.class);
	
	@Resource(name = "jdbcTemplate")
	JdbcTemplate jdbcTemplate;

	/**
	 * 保存验证码信息
	 */
	public int saveCode(String phone,String code,Integer type,String ip) {
		// TODO Auto-generated method stub
		String sql = "insert INTO t_code(phone, code,createTime,type,ip) VALUES (?,?,UNIX_TIMESTAMP(),?,?)";
		int i = 0;
		try {
			//执行更新
			i = jdbcTemplate.update(sql, new Object[]{phone,code,type,ip});
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e);
		}
		return i;
	}

	/**
	 * 根据手机号和类型获取验证码信息
	 */
	public Map<String, Object> getCodeByPhoneAndType(String phone, Integer type) {
		// TODO Auto-generated method stub
		String sql="SELECT * FROM t_code AS c WHERE c.state=1 and c.phone = '"+phone+"' and  c.type="+type
				+" and c.createTime =(SELECT MAX(createTime) FROM t_code AS  cc WHERE cc.phone ='"+phone+"' and  c.type="+type+")";
		Map<String, Object> code = null;
		try {
			//执行更新
			code = jdbcTemplate.queryForMap(sql);
		}catch(Exception e){
			// TODO: handle exception
						logger.error(e);
		}
		return code;
	}
	
	/**
	 * 获取当天的电话数
	 */
	@Override
	public int getPhoneNumberToday(String phone, Integer type, String ip) {
			String sql="SELECT COUNT(1) FROM t_code c WHERE  FROM_UNIXTIME(createTime,'%Y-%m-%d %H')=date_format(now(), '%Y-%m-%d %H') and c.phone=? and c.type=? ";
			int code = 0;
			try {
				if(!StringUtil.isEmpty(phone)&&type!=null){
					if(!StringUtil.isEmpty(ip)){
						sql +=" and c.ip='"+ip+"' ";
					}
					code = jdbcTemplate.queryForObject(sql,new Object[]{phone,type},Integer.class);
				}
			}catch(Exception e){
				logger.error(e);
			}
			return code;
	}

}
