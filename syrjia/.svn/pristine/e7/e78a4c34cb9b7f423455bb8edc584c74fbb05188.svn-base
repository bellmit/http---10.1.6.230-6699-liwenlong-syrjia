package cn.syrjia.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;

import cn.syrjia.common.impl.BaseDaoImpl;
import cn.syrjia.dao.StatisticsDao;
import cn.syrjia.util.StringUtil;
import cn.syrjia.util.Util;

@Repository("statisticsDao")
public class StatisticsDaoImpl extends BaseDaoImpl implements StatisticsDao{

	// 日志
	private Logger logger = LogManager.getLogger(StatisticsDaoImpl.class);

	/**
	 * 添加
	 */
	@Override
	public Integer addStatistics(final List<Map<String, Object>> list,final String openId,final String memberId) {
		String sql="insert into t_statistics(id,openId,memberId,url,title,startTime,endTime,duration,isDown,ip,country,province,city,isp,accessName,accessId,author,version,screen,clickNum,sessionId,type,createTime) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		int i[]={0};
		try {
			i=jdbcTemplate.batchUpdate(sql,new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ps.setString(1,Util.getUUID());
					ps.setString(2,openId);
					ps.setString(3,memberId);
					//地址
					ps.setString(4,StringUtil.isEmpty(list.get(i).get("url"))?null:list.get(i).get("url").toString());
					//标题
					ps.setString(5,StringUtil.isEmpty(list.get(i).get("title"))?null:list.get(i).get("title").toString());
					//开始时间
					ps.setString(6,StringUtil.isEmpty(list.get(i).get("startTime"))?null:list.get(i).get("startTime").toString());
					//结束时间
					ps.setString(7,StringUtil.isEmpty(list.get(i).get("endTime"))?null:list.get(i).get("endTime").toString());
					ps.setString(8,StringUtil.isEmpty(list.get(i).get("duration"))?null:list.get(i).get("duration").toString());
					ps.setString(9,StringUtil.isEmpty(list.get(i).get("isDown"))?null:list.get(i).get("isDown").toString());
					//ip地址
					ps.setString(10,StringUtil.isEmpty(list.get(i).get("ip"))?null:list.get(i).get("ip").toString());
					ps.setString(11,StringUtil.isEmpty(list.get(i).get("country"))?null:list.get(i).get("country").toString());
					ps.setString(12,StringUtil.isEmpty(list.get(i).get("province"))?null:list.get(i).get("province").toString());
					ps.setString(13,StringUtil.isEmpty(list.get(i).get("city"))?null:list.get(i).get("city").toString());
					ps.setString(14,StringUtil.isEmpty(list.get(i).get("isp"))?null:list.get(i).get("isp").toString());
					ps.setString(15,StringUtil.isEmpty(list.get(i).get("accessName"))?null:list.get(i).get("accessName").toString());
					ps.setString(16,StringUtil.isEmpty(list.get(i).get("accessId"))?null:list.get(i).get("accessId").toString());
					ps.setString(17,StringUtil.isEmpty(list.get(i).get("author"))?null:list.get(i).get("author").toString());
					//版本
					ps.setString(18,StringUtil.isEmpty(list.get(i).get("version"))?null:list.get(i).get("version").toString());
					ps.setString(19,StringUtil.isEmpty(list.get(i).get("screen"))?null:list.get(i).get("screen").toString());
					//点击数
					ps.setString(20,StringUtil.isEmpty(list.get(i).get("clickNum"))?null:list.get(i).get("clickNum").toString());
					//sessionis
					ps.setString(21,StringUtil.isEmpty(list.get(i).get("sessionId"))?null:list.get(i).get("sessionId").toString());
					ps.setString(22,StringUtil.isEmpty(list.get(i).get("type"))?null:list.get(i).get("type").toString());
					//创建时间
					ps.setInt(23,StringUtil.isEmpty(list.get(i).get("createTime"))?null:new Double(Double.parseDouble(list.get(i).get("createTime").toString())).intValue());
				}
				@Override
				public int getBatchSize() {
					return list.size();
				}
			});
		} catch (DataAccessException e) {
			logger.error(e);
		}
		return Util.toInt(i);
	}
}
