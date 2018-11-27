package cn.syrjia.common.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.apache.commons.lang.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import cn.syrjia.common.BaseDaoInterface;
import cn.syrjia.common.util.DaoUtil;
import cn.syrjia.config.configCode;
import cn.syrjia.util.SMSTemplateIdUtil;
import cn.syrjia.util.StringUtil;
import cn.syrjia.util.Util;
import cn.syrjia.util.qcloudsms.QCloudSmsUtil;

/**
 */
@Repository("baseDao")
public class BaseDaoImpl implements BaseDaoInterface {

	// 日志
	private Logger logger = LogManager.getLogger(BaseDaoImpl.class);

	@Resource(name = "jdbcTemplate")
	protected JdbcTemplate jdbcTemplate;

	/**
	 * 根据实体查询
	 * 
	 * @param entity
	 *            实体
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> query(final T entity, boolean... isChild) {
		String tableName = DaoUtil.getTableName(entity);// 表名

		StringBuilder sql = new StringBuilder();// sql

		StringBuilder joinSqlQuery = new StringBuilder();// 链接要查询的字段

		StringBuilder joinSql = new StringBuilder();// 链接sql

		Map<String, Object> map = DaoUtil.jointSql(entity);

		List<Map<String, Object>> joinTableList = null == map
				.get("joinTableList") ? null : (List<Map<String, Object>>) map
				.get("joinTableList");

		if (null != joinTableList) {
			for (Map<String, Object> joinMap : joinTableList) {
				String joinTable = joinMap.get("table").toString();
				String id = joinMap.get("id").toString();
				String otherId = joinMap.get("otherId").toString();
				List<String> otherName = (List<String>) joinMap
						.get("otherName");
				String joinType = joinMap.get("joinType").toString();
				List<String> columnName = (List<String>) joinMap
						.get("columnName");
				for (int i = 0; i < otherName.size(); i++) {
					joinSqlQuery.append(",").append(joinTable).append("_a.")
							.append(otherName.get(i).toString()).append(" as ")
							.append(columnName.get(i).toString());
				}
				String jointable = joinType + " join " + joinTable + " as "
						+ joinTable + "_a" + " on " + tableName + "_a." + id
						+ "=" + joinTable + "_a." + otherId + " ";
				joinSql.append(jointable);
			}
		}
		sql.append("select ").append(tableName).append("_a.*")
				.append(joinSqlQuery).append(" from ").append(tableName)
				.append(" as ").append(tableName).append("_a ").append(joinSql)
				.append(" where 1=1");

		sql.append(null == map.get("sql") ? "" : map.get("sql"));

		Object[] obj = null == map.get("obj") ? new Object[0] : (Object[]) map
				.get("obj");

		final boolean isChilds = (null != isChild && 0 != isChild.length && isChild[0]) ? true
				: false;// 是否查询关联表

		final List<Map<String, Object>> correlationTableList = isChilds ? (null == map
				.get("correlationTableList") ? null
				: (List<Map<String, Object>>) map.get("correlationTableList"))
				: null;
		final List<T> entitys = new ArrayList<T>();
		try {
			jdbcTemplate.query(sql.toString(), obj, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					T t = DaoUtil.setEntity(entity, rs);
					if (isChilds && null != correlationTableList
							&& correlationTableList.size() > 0) {
						try {
							setChlidTable(correlationTableList, t);
						} catch (Exception e) {
						}
					}
					entitys.add(t);
				}
			});
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return entitys;
	}

	public <T> void setChlidTable(
			List<Map<String, Object>> correlationTableList, T t)
			throws Exception {
		if (null != correlationTableList && correlationTableList.size() > 0) {// 如果有关联表
			for (Map<String, Object> entry : correlationTableList) {
				String fieldName = entry.get("fieldName").toString();// 字段名
				Class<?> className = (Class<?>) entry.get("className");// 关联实体
				String contentTable = entry.get("contentTable").toString();// 中间表
				String correlationTableField = entry.get(
						"correlationTableField").toString();// 中间表关联字段
				String correlationTableOtherField = entry.get(
						"correlationTableOtherField").toString();// 中间表被关联字段
				String correlationField = entry.get("correlationField")
						.toString();// 关联字段
				String correlationOtherField = entry.get(
						"correlationOtherField").toString();// 被关联字段
				if (null != contentTable && !"".equals(contentTable.trim())) {
					String contentsql = "select " + correlationTableOtherField
							+ " from " + contentTable + " where "
							+ correlationTableField + " =?";
					Object obj = DaoUtil.getEntityDeclared(t, correlationField);
					List<Object> listObj = jdbcTemplate.query(contentsql,
							new Object[] { obj }, new RowMapper<Object>() {
								@Override
								public Object mapRow(ResultSet rs, int i)
										throws SQLException {
									Object obj = rs.getObject(1);
									return obj;
								}
							});
					List<?> resultList = this.query(className,
							correlationOtherField, listObj);
					DaoUtil.setEntityDeclared(t, resultList, fieldName);
				} else {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put(correlationOtherField,
							DaoUtil.getEntityDeclared(t, correlationField));
					List<?> resultList = this.queryOneToMony(className, map);
					DaoUtil.setEntityDeclared(t, resultList, fieldName);
				}
			}
		}
	}

	/**
	 * 根据实体查询单个
	 * 
	 * @param entity
	 *            实体
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T queryByEntity(final T entity, boolean... isChild) {

		String tableName = DaoUtil.getTableName(entity);

		StringBuilder sql = new StringBuilder();

		StringBuilder joinSqlQuery = new StringBuilder();

		StringBuilder joinSql = new StringBuilder();

		Map<String, Object> map = DaoUtil.jointSql(entity);// 拼接字符串

		List<Map<String, Object>> joinTableList = null == map
				.get("joinTableList") ? null : (List<Map<String, Object>>) map
				.get("joinTableList");

		if (null != joinTableList) {
			for (Map<String, Object> joinMap : joinTableList) {
				String joinTable = joinMap.get("table").toString();
				String id = joinMap.get("id").toString();
				String otherId = joinMap.get("otherId").toString();
				List<String> otherName = (List<String>) joinMap
						.get("otherName");
				String joinType = joinMap.get("joinType").toString();
				List<String> columnName = (List<String>) joinMap
						.get("columnName");
				for (int i = 0; i < otherName.size(); i++) {
					joinSqlQuery.append(",").append(joinTable).append("_a.")
							.append(otherName.get(i).toString()).append(" as ")
							.append(columnName.get(i).toString());
				}
				String jointable = " " + joinType + " join " + joinTable
						+ " as " + joinTable + "_a" + " on " + tableName
						+ "_a." + id + "=" + joinTable + "_a." + otherId;
				joinSql.append(jointable);
			}
		}
		sql.append("select ").append(tableName).append("_a.*")
				.append(joinSqlQuery).append(" from ").append(tableName)
				.append(" as ").append(tableName).append("_a ").append(joinSql)
				.append(" where 1=1");

		sql.append(null == map.get("sql") ? "" : map.get("sql"));
		Object[] obj = null == map.get("obj") ? new Object[0] : (Object[]) map
				.get("obj");
		final boolean isChilds = (null != isChild && 0 != isChild.length && isChild[0]) ? true
				: false;// 是否查询关联表

		final List<Map<String, Object>> correlationTableList = isChilds ? (null == map
				.get("correlationTableList") ? null
				: (List<Map<String, Object>>) map.get("correlationTableList"))
				: null;
		final List<T> entitys = new ArrayList<T>();
		try {
			jdbcTemplate.query(sql.toString(), obj, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					T t = DaoUtil.setEntity(entity, rs);
					if (isChilds && null != correlationTableList
							&& correlationTableList.size() > 0) {
						try {
							setChlidTable(correlationTableList, t);
						} catch (Exception e) {
						}
					}
					entitys.add(t);
				}
			});
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return null == entitys || 0 == entitys.size() || entitys.size() > 1 ? null
				: entitys.get(0);
	}

	/**
	 * 分页
	 * 
	 * @param entity
	 *            实体 T 参数对应 页数 行数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> Map<String, Object> queryListByPage(final T entity, T... i) {

		String tableName = DaoUtil.getTableName(entity);

		StringBuilder sql = new StringBuilder();

		StringBuilder joinSqlQuery = new StringBuilder();

		StringBuilder joinSql = new StringBuilder();

		Map<String, Object> map = DaoUtil.jointSql(entity, i);// 拼接字符串

		List<Map<String, Object>> joinTableList = null == map
				.get("joinTableList") ? null : (List<Map<String, Object>>) map
				.get("joinTableList");

		if (null != joinTableList) {
			for (Map<String, Object> joinMap : joinTableList) {
				String joinTable = joinMap.get("table").toString();
				String id = joinMap.get("id").toString();
				String otherId = joinMap.get("otherId").toString();
				List<String> otherName = (List<String>) joinMap
						.get("otherName");
				String joinType = joinMap.get("joinType").toString();
				List<String> columnName = (List<String>) joinMap
						.get("columnName");
				for (int q = 0; q < otherName.size(); q++) {
					joinSqlQuery.append(",").append(joinTable).append("_a.")
							.append(otherName.get(q).toString()).append(" as ")
							.append(columnName.get(q).toString()).append(" ");
				}
				String jointable = joinType + " join " + joinTable + " as "
						+ joinTable + "_a" + " on " + tableName + "_a." + id
						+ "=" + joinTable + "_a." + otherId + " ";
				joinSql.append(jointable);
			}
		}
		sql.append("select ").append(tableName).append("_a.*")
				.append(joinSqlQuery).append(" from ").append(tableName)
				.append(" as ").append(tableName).append("_a ").append(joinSql)
				.append(" where 1=1");

		sql.append(null == map.get("sql") ? "" : map.get("sql"));

		Object[] obj = null == map.get("obj") ? new Object[0] : (Object[]) map
				.get("obj");

		Integer total = queryListByPageNumSize(sql.toString(), obj);

		Integer row = DaoUtil.queryT("row", i);// 获得行数

		Integer page = DaoUtil.queryT("page", i);// 获得页数

		sql.append(" limit " + page + "," + row);
		Object isChild = null;
		try {
			isChild = i[i.length - 1];
		} catch (Exception e1) {
		}

		final boolean isChilds = (isChild instanceof Boolean && Boolean
				.parseBoolean(isChild.toString())) ? true : false;

		final List<Map<String, Object>> correlationTableList = isChilds ? (null == map
				.get("correlationTableList") ? null
				: (List<Map<String, Object>>) map.get("correlationTableList"))
				: null;

		final List<T> entitys = new ArrayList<T>();
		try {
			jdbcTemplate.query(sql.toString(), obj, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					T t = DaoUtil.setEntity(entity, rs);
					if (isChilds && null != correlationTableList
							&& correlationTableList.size() > 0) {
						try {
							setChlidTable(correlationTableList, t);
						} catch (Exception e) {
						}
					}
					entitys.add(t);
				}
			});
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		map = new HashMap<String, Object>();
		map.put("data", entitys);
		map.put("total", total);
		return map;
	}

	public <T> Integer queryListByPageNumSize(String sql, Object obj[]) {
		return this
				.queryBysqlCount("select count(1) from (" + sql + ") f", obj);
	}

	@SuppressWarnings("unchecked")
	public <T> Integer queryListByPageNum(final T entity, T... i) {

		String tableName = DaoUtil.getTableName(entity);

		StringBuilder sql = new StringBuilder();

		StringBuilder joinSqlQuery = new StringBuilder();

		StringBuilder joinSql = new StringBuilder();

		Map<String, Object> map = DaoUtil.jointSql(entity, i);// 拼接字符串

		List<Map<String, Object>> joinTableList = null == map
				.get("joinTableList") ? null : (List<Map<String, Object>>) map
				.get("joinTableList");

		if (null != joinTableList) {
			for (Map<String, Object> joinMap : joinTableList) {
				String joinTable = joinMap.get("table").toString();
				String id = joinMap.get("id").toString();
				String otherId = joinMap.get("otherId").toString();
				List<String> otherName = (List<String>) joinMap
						.get("otherName");
				String joinType = joinMap.get("joinType").toString();
				List<String> columnName = (List<String>) joinMap
						.get("columnName");
				for (int q = 0; q < otherName.size(); q++) {
					joinSqlQuery.append(",").append(joinTable).append("_a.")
							.append(otherName.get(q).toString()).append(" as ")
							.append(columnName.get(q).toString());
				}
				String jointable = joinType + " join " + joinTable + " as "
						+ joinTable + "_a" + " on " + tableName + "_a." + id
						+ "=" + joinTable + "_a." + otherId;
				joinSql.append(jointable);
			}
		}
		sql.append("select count(1) ").append(" from ").append(tableName)
				.append(" as ").append(tableName).append("_a ").append(joinSql)
				.append(" where 1=1");

		sql.append(null == map.get("sql") ? "" : map.get("sql"));

		Object[] obj = null == map.get("obj") ? new Object[0] : (Object[]) map
				.get("obj");

		Integer total = 0;
		try {
			total = jdbcTemplate.queryForObject(sql.toString(), obj,
					Integer.class);
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return total;
	}

	/**
	 * 修改必须有ID 根据ID
	 */
	public <T> Integer updateEntity(T entity) {
		String sql = "update " + DaoUtil.getTableName(entity);
		Map<String, Object> map = DaoUtil.jointSetSql(entity);
		if (null == map.get("sql")) {
			return 0;
		}
		sql += null == map.get("sql") ? "" : map.get("sql").toString();
		Object[] obj = null == map.get("obj") ? new Object[0] : (Object[]) map
				.get("obj");
		Integer i = 0;
		try {
			System.out.println("\n\n" + sql);
			i = jdbcTemplate.update(sql, obj);
		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
		return i;
	}

	/**
	 * 批量修改
	 */
	@Override
	public <T> Integer batchUpdate(final List<T> entitys) {
		String sql = "update " + DaoUtil.getTableName(entitys.get(0));
		Map<String, Object> map = DaoUtil.jointSetSql(entitys.get(0));
		if (null == map.get("sql")) {
			return 0;
		}
		sql += null == map.get("sql") ? "" : map.get("sql").toString();
		// Object[] obj =null==map.get("obj")?new Object[0]:(Object[])
		// map.get("obj");
		int[] i = { 0 };
		try {
			i = jdbcTemplate.batchUpdate(sql,
					new BatchPreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps, int q)
								throws SQLException {
							Map<String, Object> map = DaoUtil
									.jointSetSql(entitys.get(q));
							Object[] obj = (Object[]) map.get("obj");
							for (int j = 1; j <= obj.length; j++) {
								ps.setObject(j, obj[j - 1]);
							}
						}

						@Override
						public int getBatchSize() {
							return entitys.size();
						}
					});
		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
		return Util.toInt(i);
	}

	/**
	 * 删除 根据属性
	 */
	@SuppressWarnings("unchecked")
	public <T> Integer deleteEntity(T entity) {
		String sql = "delete from " + DaoUtil.getTableName(entity)
				+ " where 1=1";
		Map<String, Object> map = DaoUtil.jointSql(entity);// 拼接字符串
		sql += null == map.get("sql") ? "" : map.get("sql");
		Object[] obj = null == map.get("obj") ? new Object[0] : (Object[]) map
				.get("obj");
		int i = 0;
		try {
			i = jdbcTemplate.update(sql, obj);
		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
		return i;
	}

	/**
	 * 增加
	 */
	public <T> Object addEntity(T entity) {
		String sql = "insert into " + DaoUtil.getTableName(entity);
		Map<String, Object> map = DaoUtil.jointInsertSql(entity);// 拼接字符串
		sql += null == map.get("sql") ? "" : map.get("sql");
		final Object[] obj = null == map.get("obj") ? new Object[0]
				: (Object[]) map.get("obj");
		KeyHolder keyHolder = new GeneratedKeyHolder();
		try {
			final String s = sql;
			System.out.println(s);
			jdbcTemplate.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection conn)
						throws SQLException {
					PreparedStatement ps = conn.prepareStatement(s,
							PreparedStatement.RETURN_GENERATED_KEYS);
					int i = 1;
					for (Object o : obj) {
						ps.setObject(i++, o);
					}
					return ps;
				}
			}, keyHolder);
		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
		return null == keyHolder.getKey() ? 1 : keyHolder.getKey();
	}

	/**
	 * 增加
	 */
	public <T> Object addEntityUUID(T entity) {
		String uuid = Util.getUUID();
		String id = DaoUtil.getAnnotationId(entity.getClass());
		try {
			DaoUtil.setEntityDeclared(entity, uuid, id);
		} catch (Exception e1) {
		}
		String sql = "insert into " + DaoUtil.getTableName(entity);
		Map<String, Object> map = DaoUtil.jointInsertSql(entity);// 拼接字符串
		sql += null == map.get("sql") ? "" : map.get("sql");
		final Object[] obj = null == map.get("obj") ? new Object[0]
				: (Object[]) map.get("obj");
		try {
			jdbcTemplate.update(sql, obj);
		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
		return uuid;
	}

	/**
	 * 增加
	 */
	public <T> Object addEntity(List<T> entity) {
		String sql = "insert into " + DaoUtil.getTableName(entity.get(0));
		int i = 0;
		int count = 0;
		Object[] obj = new Object[0];
		for (T t : entity) {
			Map<String, Object> map = DaoUtil.jointInsertSql(t);// 拼接字符串
			if (count++ == 0) {
				sql += null == map.get("sql") ? "" : (map.get("sql") + ",");
			} else {
				sql += map
						.get("sql")
						.toString()
						.substring(
								map.get("sql").toString().indexOf("values") + 6)
						+ ",";
			}
			obj = null == map.get("obj") ? new Object[0] : ArrayUtils.addAll(
					obj, (Object[]) map.get("obj"));
		}
		try {
			i = jdbcTemplate.update(sql.substring(0, sql.length() - 1), obj);
		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}

		return i;
	}

	/**
	 * 增加
	 */
	public <T> Object addEntityUUID(List<T> entity) {
		int i = 0;
		for (T t : entity) {
			String sql = "insert into " + DaoUtil.getTableName(entity.get(0));
			String uuid = Util.getUUID();
			String id = DaoUtil.getAnnotationId(entity.get(0).getClass());
			try {
				DaoUtil.setEntityDeclared(t, uuid, id);
			} catch (Exception e1) {
			}
			Map<String, Object> map = DaoUtil.jointInsertSql(t);// 拼接字符串
			sql += null == map.get("sql") ? "" : map.get("sql");
			Object[] obj = null == map.get("obj") ? new Object[0]
					: (Object[]) map.get("obj");
			try {
				i = jdbcTemplate.update(sql, obj);
			} catch (DataAccessException e) {
				logger.error(e);
				throw e;
			}
		}
		return i;
	}

	/**
	 * 修改或增加
	 */
	public <T> Integer updateOrAdd(T entity) {
		String sql = "replace into " + DaoUtil.getTableName(entity);
		Map<String, Object> map = DaoUtil.jointInsertSql(entity);// 拼接字符串
		sql += null == map.get("sql") ? "" : map.get("sql").toString();
		Object[] obj = null == map.get("obj") ? new Object[0] : (Object[]) map
				.get("obj");
		int i = 0;
		try {
			i = jdbcTemplate.update(sql, obj);
		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
		return i;
	}

	/**
	 * 根据类和参数查询
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> query(final Class<T> t, Map<String, Object> map,
			boolean... isChild) {

		String tableName = DaoUtil.getTableName(t);// 表名

		StringBuilder sql = new StringBuilder();// sql

		StringBuilder joinSqlQuery = new StringBuilder();// 链接要查询的字段

		StringBuilder joinSql = new StringBuilder();// 链接sql

		Map<String, Object> query = DaoUtil.jointSql(t, map);

		List<Map<String, Object>> joinTableList = DaoUtil.queryJoin(t);

		if (null != joinTableList) {
			for (Map<String, Object> joinMap : joinTableList) {
				String joinTable = joinMap.get("table").toString();
				String id = joinMap.get("id").toString();
				String otherId = joinMap.get("otherId").toString();
				List<String> otherName = (List<String>) joinMap
						.get("otherName");
				String joinType = joinMap.get("joinType").toString();
				List<String> columnName = (List<String>) joinMap
						.get("columnName");
				for (int i = 0; i < otherName.size(); i++) {
					joinSqlQuery.append(",").append(joinTable).append("_a.")
							.append(otherName.get(i).toString()).append(" as ")
							.append(columnName.get(i).toString());
				}
				String jointable = joinType + " join " + joinTable + " as "
						+ joinTable + "_a" + " on " + tableName + "_a." + id
						+ "=" + joinTable + "_a." + otherId;
				joinSql.append(jointable);
			}
		}
		sql.append("select ").append(tableName).append("_a.*")
				.append(joinSqlQuery).append(" from ").append(tableName)
				.append(" as ").append(tableName).append("_a ").append(joinSql)
				.append(" where 1=1");

		sql.append(null == query ? "" : query.get("sql"));
		;

		Object[] obj = null == query ? new Object[0] : (Object[]) query
				.get("obj");
		final boolean isChilds = (null != isChild && 0 != isChild.length && isChild[0]) ? true
				: false;// 是否查询关联表

		final List<Map<String, Object>> oneToMony = isChilds ? DaoUtil
				.queryOneToMony(t) : null;
		List<T> entitys = new ArrayList<T>();
		try {
			/*
			 * jdbcTemplate.query(sql.toString(), obj,new RowCallbackHandler() {
			 * 
			 * @Override public void processRow(ResultSet rs) throws
			 * SQLException { T t1 = DaoUtil.setEntity(t, rs); if (isChilds &&
			 * null != oneToMony && oneToMony.size() > 0) { try {
			 * setChlidTable(oneToMony, t1); } catch (Exception e) { } }
			 * entitys.add(t1); } });
			 */
			entitys = jdbcTemplate.query(sql.toString(), obj,
					new RowMapper<T>() {
						@Override
						public T mapRow(ResultSet rs, int i)
								throws SQLException {
							T t1 = DaoUtil.setEntity(t, rs);
							if (isChilds && null != oneToMony
									&& oneToMony.size() > 0) {
								try {
									setChlidTable(oneToMony, t1);
								} catch (Exception e) {
								}
							}
							return t1;
						}
					});
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return entitys;
	}

	/**
	 * 根据类和参数查询 递归查询 无接口实现 不允许集成
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> queryOneToMony(final Class<T> t, Map<String, Object> map) {

		String tableName = DaoUtil.getTableName(t);// 表名

		StringBuilder sql = new StringBuilder();// sql

		StringBuilder joinSqlQuery = new StringBuilder();// 链接要查询的字段

		StringBuilder joinSql = new StringBuilder();// 链接sql

		Map<String, Object> query = DaoUtil.jointSql(t, map);

		List<Map<String, Object>> joinTableList = DaoUtil.queryJoin(t);

		if (null != joinTableList) {
			for (Map<String, Object> joinMap : joinTableList) {
				String joinTable = joinMap.get("table").toString();
				String id = joinMap.get("id").toString();
				String otherId = joinMap.get("otherId").toString();
				List<String> otherName = (List<String>) joinMap
						.get("otherName");
				String joinType = joinMap.get("joinType").toString();
				List<String> columnName = (List<String>) joinMap
						.get("columnName");
				for (int i = 0; i < otherName.size(); i++) {
					joinSqlQuery.append(",").append(joinTable).append("_a.")
							.append(otherName.get(i).toString()).append(" as ")
							.append(columnName.get(i).toString());
				}
				String jointable = joinType + " join " + joinTable + " as "
						+ joinTable + "_a" + " on " + tableName + "_a." + id
						+ "=" + joinTable + "_a." + otherId + " ";
				joinSql.append(jointable);
			}
		}
		sql.append("select ").append(tableName).append("_a.*")
				.append(joinSqlQuery).append(" from ").append(tableName)
				.append(" as ").append(tableName).append("_a ").append(joinSql)
				.append(" where 1=1");

		sql.append(null == query.get("sql") ? "" : query.get("sql"));
		;

		Object[] obj = null == query.get("obj") ? new Object[0]
				: (Object[]) query.get("obj");
		final List<Map<String, Object>> oneToMony = DaoUtil.queryOneToMony(t);

		List<T> entitys = new ArrayList<T>();
		try {
			/*
			 * jdbcTemplate.query(sql.toString(), obj,new RowCallbackHandler() {
			 * 
			 * @Override public void processRow(ResultSet rs) throws
			 * SQLException { T t1 = DaoUtil.setEntity(t, rs); if (null !=
			 * oneToMony && oneToMony.size() > 0) { try {
			 * setChlidTable(oneToMony, t1); } catch (Exception e) { } }
			 * entitys.add(t1); } });
			 */
			entitys = jdbcTemplate.query(sql.toString(), obj,
					new RowMapper<T>() {
						@Override
						public T mapRow(ResultSet rs, int i)
								throws SQLException {
							T t1 = DaoUtil.setEntity(t, rs);
							if (null != oneToMony && oneToMony.size() > 0) {
								try {
									setChlidTable(oneToMony, t1);
								} catch (Exception e) {
								}
							}
							return t1;
						}
					});
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return entitys;
	}

	/**
	 * 根据类和批量查询 递归查询 无接口实现 不允许继承
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> query(final Class<T> t, String name, List<Object> lists) {

		String tableName = DaoUtil.getTableName(t);// 表名

		StringBuilder sql = new StringBuilder();// sql

		StringBuilder joinSqlQuery = new StringBuilder();// 链接要查询的字段

		StringBuilder joinSql = new StringBuilder();// 链接sql

		List<Map<String, Object>> joinTableList = DaoUtil.queryJoin(t);

		if (null != joinTableList) {
			for (Map<String, Object> joinMap : joinTableList) {
				String joinTable = joinMap.get("table").toString();
				String id = joinMap.get("id").toString();
				String otherId = joinMap.get("otherId").toString();
				List<String> otherName = (List<String>) joinMap
						.get("otherName");
				String joinType = joinMap.get("joinType").toString();
				List<String> columnName = (List<String>) joinMap
						.get("columnName");
				for (int i = 0; i < otherName.size(); i++) {
					joinSqlQuery.append(",").append(joinTable).append("_a.")
							.append(otherName.get(i).toString()).append(" as ")
							.append(columnName.get(i).toString());
				}
				String jointable = joinType + " join " + joinTable + " as "
						+ joinTable + "_a" + " on " + tableName + "_a." + id
						+ "=" + joinTable + "_a." + otherId;
				joinSql.append(jointable);
			}
		}
		sql.append("select ").append(tableName).append("_a.*")
				.append(joinSqlQuery).append(" from ").append(tableName)
				.append(" as ").append(tableName).append("_a ").append(joinSql)
				.append(" where 1=1 and ").append(name).append("=?");

		final List<T> entitys = new ArrayList<T>();

		final List<Map<String, Object>> oneToMony = DaoUtil.queryOneToMony(t);
		try {
			for (Object list : lists) {
				jdbcTemplate.query(sql.toString(), new Object[] { list },
						new RowCallbackHandler() {
							@Override
							public void processRow(ResultSet rs)
									throws SQLException {
								T t1 = DaoUtil.setEntity(t, rs);
								if (null != oneToMony && oneToMony.size() > 0) {
									try {
										setChlidTable(oneToMony, t1);
									} catch (Exception e) {
									}
								}
								entitys.add(t1);
							}
						});
			}
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return entitys;
	}

	/**
	 * 根据类和参数查询单个
	 */
	@SuppressWarnings("unchecked")
	public <T> T queryOne(final Class<T> t, Map<String, Object> map,
			boolean... isChild) {

		String tableName = DaoUtil.getTableName(t);// 表名

		StringBuilder sql = new StringBuilder();// sql

		StringBuilder joinSqlQuery = new StringBuilder();// 链接要查询的字段

		StringBuilder joinSql = new StringBuilder();// 链接sql

		Map<String, Object> querys = DaoUtil.jointSql(t, map);

		List<Map<String, Object>> joinTableList = DaoUtil.queryJoin(t);
		logger.debug("你的sql");
		if (null != joinTableList) {
			for (Map<String, Object> joinMap : joinTableList) {
				String joinTable = joinMap.get("table").toString();
				String id = joinMap.get("id").toString();
				String otherId = joinMap.get("otherId").toString();
				List<String> otherName = (List<String>) joinMap
						.get("otherName");
				String joinType = joinMap.get("joinType").toString();
				List<String> columnName = (List<String>) joinMap
						.get("columnName");
				for (int i = 0; i < otherName.size(); i++) {
					joinSqlQuery.append(",").append(joinTable).append("_a.")
							.append(otherName.get(i).toString()).append(" as ")
							.append(columnName.get(i).toString());
				}
				String jointable = joinType + " join " + joinTable + " as "
						+ joinTable + "_a" + " on " + tableName + "_a." + id
						+ "=" + joinTable + "_a." + otherId;
				joinSql.append(jointable);
			}
		}
		sql.append("select ").append(tableName).append("_a.*")
				.append(joinSqlQuery).append(" from ").append(tableName)
				.append(" as ").append(tableName).append("_a ").append(joinSql)
				.append(" where 1=1");

		sql.append(null == querys ? "" : querys.get("sql"));
		logger.debug(sql.toString() + "你的sql");
		Object[] obj = null == querys ? new Object[0] : (Object[]) querys
				.get("obj");
		final boolean isChilds = (null != isChild && 0 != isChild.length && isChild[0]) ? true
				: false;// 是否查询关联表

		final List<Map<String, Object>> oneToMony = isChilds ? DaoUtil
				.queryOneToMony(t) : null;

		final List<T> entitys = new ArrayList<T>();
		logger.debug(sql.toString() + "你的sql");
		try {
			jdbcTemplate.query(sql.toString(), obj, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					T t1 = DaoUtil.setEntity(t, rs);
					if (isChilds && null != oneToMony && oneToMony.size() > 0) {
						try {
							setChlidTable(oneToMony, t1);
						} catch (Exception e) {
						}
					}
					entitys.add(t1);
				}
			});
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return null == entitys || 0 == entitys.size() || entitys.size() > 1 ? null
				: entitys.get(0);
	}

	/**
	 * 根据类和id查询
	 */
	@SuppressWarnings("unchecked")
	public <T> T queryById(final Class<T> t, Object id, boolean... isChild) {

		String tableName = DaoUtil.getTableName(t);// 表名

		StringBuilder sql = new StringBuilder();// sql

		StringBuilder joinSqlQuery = new StringBuilder();// 链接要查询的字段

		StringBuilder joinSql = new StringBuilder();// 链接sql

		Map<String, Object> map = DaoUtil.jointIdSql(t, id);

		List<Map<String, Object>> joinTableList = DaoUtil.queryJoin(t);

		if (null != joinTableList) {
			for (Map<String, Object> joinMap : joinTableList) {
				String joinTable = joinMap.get("table").toString();
				String ids = joinMap.get("id").toString();
				String otherId = joinMap.get("otherId").toString();
				List<String> otherName = (List<String>) joinMap
						.get("otherName");
				String joinType = joinMap.get("joinType").toString();
				List<String> columnName = (List<String>) joinMap
						.get("columnName");
				for (int i = 0; i < otherName.size(); i++) {
					joinSqlQuery.append(",").append(joinTable).append("_a.")
							.append(otherName.get(i).toString()).append(" as ")
							.append(columnName.get(i).toString());
				}
				String jointable = joinType + " join " + joinTable + " as "
						+ joinTable + "_a" + " on " + tableName + "_a." + ids
						+ "=" + joinTable + "_a." + otherId + " ";
				joinSql.append(jointable);
			}
		}
		sql.append("select ").append(tableName).append("_a.*")
				.append(joinSqlQuery).append(" from ").append(tableName)
				.append(" as ").append(tableName).append("_a ").append(joinSql)
				.append(" where 1=1");

		sql.append(null == map.get("sql") ? "" : map.get("sql"));

		Object[] obj = null == map.get("obj") ? new Object[0] : (Object[]) map
				.get("obj");
		final boolean isChilds = (null != isChild && 0 != isChild.length && isChild[0]) ? true
				: false;// 是否查询关联表

		final List<Map<String, Object>> oneToMony = isChilds ? DaoUtil
				.queryOneToMony(t) : null;

		final List<T> entitys = new ArrayList<T>();
		try {
			System.out.println("\n\nsql:" + sql.toString());
			jdbcTemplate.query(sql.toString(), obj, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					T t1 = DaoUtil.setEntity(t, rs);
					if (isChilds && null != oneToMony && oneToMony.size() > 0) {
						try {
							setChlidTable(oneToMony, t1);
						} catch (Exception e) {
						}
					}
					entitys.add(t1);
				}
			});
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return null == entitys || 0 == entitys.size() || entitys.size() > 1 ? null
				: entitys.get(0);
	}

	/**
	 * 根据类和参数查询分页
	 */
	@SuppressWarnings("unchecked")
	public <T> Map<String, Object> queryListByPage(final Class<T> entity,
			Map<String, Object> param, boolean... isChild) {

		String tableName = DaoUtil.getTableName(entity);// 表名

		StringBuilder sql = new StringBuilder();// sql

		StringBuilder joinSqlQuery = new StringBuilder();// 链接要查询的字段

		StringBuilder joinSql = new StringBuilder();// 链接sql

		Map<String, Object> map = DaoUtil.jointSql(entity, param);// 拼接字符串

		List<Map<String, Object>> joinTableList = DaoUtil.queryJoin(entity);

		if (null != joinTableList) {
			for (Map<String, Object> joinMap : joinTableList) {
				String joinTable = joinMap.get("table").toString();
				String id = joinMap.get("id").toString();
				String otherId = joinMap.get("otherId").toString();
				List<String> otherName = (List<String>) joinMap
						.get("otherName");
				String joinType = joinMap.get("joinType").toString();
				List<String> columnName = (List<String>) joinMap
						.get("columnName");
				for (int i = 0; i < otherName.size(); i++) {
					joinSqlQuery.append(",").append(joinTable).append("_a.")
							.append(otherName.get(i).toString()).append(" as ")
							.append(columnName.get(i).toString());
				}
				String jointable = joinType + " join " + joinTable + " as "
						+ joinTable + "_a" + " on " + tableName + "_a." + id
						+ "=" + joinTable + "_a." + otherId;
				joinSql.append(jointable);
			}
		}
		sql.append("select ").append(tableName).append("_a.*")
				.append(joinSqlQuery).append(" from ").append(tableName)
				.append(" as ").append(tableName).append("_a ").append(joinSql)
				.append(" where 1=1");

		sql.append(null == map.get("sql") ? "" : map.get("sql"));

		Integer row = DaoUtil
				.queryT("row", param.get("page"), param.get("row"));// 获得行数

		Integer page = DaoUtil.queryT("page", param.get("page"));// 获得页数

		sql.append(" limit " + page + "," + row);

		Object[] obj = null == map.get("obj") ? new Object[0] : (Object[]) map
				.get("obj");

		final boolean isChilds = (null != isChild && 0 != isChild.length && isChild[0]) ? true
				: false;// 是否查询关联表

		final List<Map<String, Object>> oneToMony = isChilds ? DaoUtil
				.queryOneToMony(entity) : null;

		final List<T> entitys = new ArrayList<T>();

		try {
			jdbcTemplate.query(sql.toString(), obj, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					T t = DaoUtil.setEntity(entity, rs);
					if (isChilds && null != oneToMony && oneToMony.size() > 0) {
						try {
							setChlidTable(oneToMony, t);
						} catch (Exception e) {
						}
					}
					entitys.add(t);
				}
			});
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		map = new HashMap<String, Object>();
		Integer total = queryListByPageNum(entity, param);
		map.put("data", entitys);
		map.put("total", total);
		return map;
	}

	@SuppressWarnings("unchecked")
	public <T> Integer queryListByPageNum(final Class<T> entity,
			Map<String, Object> param) {

		String tableName = DaoUtil.getTableName(entity);// 表名

		StringBuilder sql = new StringBuilder();// sql

		StringBuilder joinSqlQuery = new StringBuilder();// 链接要查询的字段

		StringBuilder joinSql = new StringBuilder();// 链接sql

		Map<String, Object> map = DaoUtil.jointSql(entity, param);// 拼接字符串

		List<Map<String, Object>> joinTableList = DaoUtil.queryJoin(entity);

		if (null != joinTableList) {
			for (Map<String, Object> joinMap : joinTableList) {
				String joinTable = joinMap.get("table").toString();
				String id = joinMap.get("id").toString();
				String otherId = joinMap.get("otherId").toString();
				List<String> otherName = (List<String>) joinMap
						.get("otherName");
				String joinType = joinMap.get("joinType").toString();
				List<String> columnName = (List<String>) joinMap
						.get("columnName");
				for (int i = 0; i < otherName.size(); i++) {
					joinSqlQuery.append(",").append(joinTable).append("_a.")
							.append(otherName.get(i).toString()).append(" as ")
							.append(columnName.get(i).toString());
				}
				String jointable = joinType + " join " + joinTable + " as "
						+ joinTable + "_a" + " on " + tableName + "_a." + id
						+ "=" + joinTable + "_a." + otherId;
				joinSql.append(jointable);
			}
		}
		sql.append("select count(1) ").append(" from ").append(tableName)
				.append(" as ").append(tableName).append("_a ").append(joinSql)
				.append(" where 1=1");

		sql.append(null == map.get("sql") ? "" : map.get("sql"));

		Object[] obj = null == map.get("obj") ? new Object[0] : (Object[]) map
				.get("obj");

		Integer total = 0;

		try {
			total = jdbcTemplate.queryForObject(sql.toString(), obj,
					Integer.class);
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return total;
	}

	@Override
	public <T> Integer deleteEntity(Class<T> entity, Object id) {
		String tableName = DaoUtil.getTableName(entity);
		String sql = "delete from " + tableName + " where 1=1";
		Map<String, Object> map = DaoUtil.jointIdSql(entity, id, true);
		sql += null == map.get("sql") ? "" : map.get("sql");
		Object[] obj = null == map.get("obj") ? new Object[0] : (Object[]) map
				.get("obj");
		Integer i = 0;
		try {
			i = jdbcTemplate.update(sql, obj);
		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
		return i;
	}

	/**
	 * 批量删除
	 */
	@Override
	public <T> Integer deleteEntityList(Class<T> entity, final List<Object> ids) {
		String sql = "delete from " + DaoUtil.getTableName(entity)
				+ " where 1=1";
		String sb = DaoUtil.jointIdsSql(entity);
		sql += null == sb || "".equals(sb) ? "" : sb.toString();
		int[] i = { 0 };
		try {
			i = jdbcTemplate.batchUpdate(sql,
					new BatchPreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							ps.setObject(1, ids.get(i));
						}

						@Override
						public int getBatchSize() {
							return ids.size();
						}
					});
		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
		return Util.toInt(i);
	}

	@Override
	public <T> Integer exist(Class<T> entity, Map<String, Object> param) {
		String sql = "select count(1) from " + DaoUtil.getTableName(entity)
				+ " where 1=1";

		Map<String, Object> map = DaoUtil.jointSql(entity, param);// 拼接字符串

		sql += null == map.get("sql") ? "" : map.get("sql");

		Object[] obj = null == map.get("obj") ? new Object[0] : (Object[]) map
				.get("obj");

		Integer i = 0;
		try {
			i = jdbcTemplate.queryForObject(sql, obj, Integer.class);
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return i;
	}

	@Override
	public <T> Integer existId(Class<T> entity, Map<String, Object> param) {
		String sql = "select count(1) from " + DaoUtil.getTableName(entity)
				+ " where 1=1";

		Map<String, Object> map = DaoUtil.jointSqlLastNotEquals(entity, param);// 拼接字符串

		sql += null == map.get("sql") ? "" : map.get("sql");

		Object[] obj = null == map.get("obj") ? new Object[0] : (Object[]) map
				.get("obj");

		Integer i = 0;
		try {
			i = jdbcTemplate.queryForObject(sql, obj, Integer.class);
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return i;
	}

	@Override
	public Integer delete(String sql, Object[] obj) {
		int i = 0;
		try {
			i = jdbcTemplate.update(sql, obj);
		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
		return i;
	}

	@Override
	public Integer deleteBatch(String sql, final List<Object> list) {
		int[] i = { 0 };
		try {
			i = jdbcTemplate.batchUpdate(sql,
					new BatchPreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							ps.setObject(1, list.get(i));
						}

						@Override
						public int getBatchSize() {
							return list.size();
						}
					});
		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
		return Util.toInt(i);
	}

	@Override
	public Integer queryBysqlCount(String sql, Object[] obj) {
		Integer i = 0;
		try {
			i = jdbcTemplate.queryForObject(sql, obj, Integer.class);
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return i;
	}

	@Override
	public List<Map<String, Object>> queryBysqlList(String sql, Object[] obj) {
		List<Map<String, Object>> list = null;
		try {
			list = jdbcTemplate.queryForList(sql, obj);
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return list;
	}

	@Override
	public <T> List<T> queryBysqlListBean(final Class<T> t, String sql,
			Object[] obj) {
		List<T> entity = jdbcTemplate.query(sql, obj, new RowMapper<T>() {
			@Override
			public T mapRow(ResultSet rs, int i) throws SQLException {
				return DaoUtil.setEntity(t, rs);
			}
		});
		return entity;
	}

	@Override
	public Map<String, Object> queryBysqlMap(String sql, Object[] obj) {
		Map<String, Object> map = null;
		try {
			map = jdbcTemplate.queryForMap(sql, obj);
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return map;
	}

	@Override
	public Integer update(String sql, Object[] obj) {
		Integer i = 0;
		try {
			i = jdbcTemplate.update(sql, obj);
		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
		return i;
	}

	@Override
	public Map<String, Object> getTicket() {
		String sql = "SELECT jsapiTicket from t_weixin_accesstoken WHERE UNIX_TIMESTAMP(now())-addTime <=expires_in";
		final Map<String, Object> map = new HashMap<String, Object>();
		try {
			jdbcTemplate.query(sql, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					map.put("ticket", rs.getString("jsapiTicket"));
				}
			});
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return map;
	}

	@Override
	public List<Map<String, Object>> queryProvinces() {
		String sql = "select provinceid value,province text from t_provinces order by provinceid";
		try {
			return jdbcTemplate.queryForList(sql);
		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
	}

	@Override
	public List<Map<String, Object>> queryCitys(String provinceId) {
		String sql = "select cityid value,city text,provinceid pid from t_cities order by cityid";
		try {
			return jdbcTemplate.queryForList(sql);
		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
	}

	@Override
	public List<Map<String, Object>> queryAreas(String cityId) {
		String sql = "select areaid value,area text,cityid pid from t_areas order by areaid asc";
		try {
			return jdbcTemplate.queryForList(sql);
		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
	}

	@Override
	public Map<String, Object> getSysSet() {
		Map<String, Object> map = null;
		try {
			// map = (Map<String, Object>) RedisUtil.getVal("sysSet");
			if (map == null) {
				String sql = "select * from t_sys_set";
				map = jdbcTemplate.queryForMap(sql);
				//RedisUtil.setVal("sysSet", map);
			}
		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
		return map;
	}

	@Override
	public Integer checkSendCodeCount(String phone, Integer type) {
		int i = 0;
		try {
			/*String sql = "select count(1) from t_code where phone=? and type=? and TO_DAYS(FROM_UNIXTIME(createTime))=TO_DAYS(NOW())";
			if (!StringUtil.isEmpty(phone) && type != null) {
				i = jdbcTemplate.queryForObject(sql,
						new Object[] { phone, type }, Integer.class);
			}*/
		} catch (Exception e) {
			logger.error(e);
		}
		return i;
	}

	@Override
	public Integer updateCodeState(String phone, Integer type, String code) {
		int i = 0;
		try {
			String sql = "update t_code set state=2 where phone=? and type=?";
			if (!StringUtil.isEmpty(code)) {
				sql += " and code='" + code + "' ";
			}
			if (!StringUtil.isEmpty(phone) && type != null)
				i = jdbcTemplate.update(sql, new Object[] { phone, type });
		} catch (Exception e) {
			logger.error(e);
		}
		return i;
	}

	@Override
	public Integer addCode(String phone, Integer type, String code, String ip) {
		String sql = "insert INTO t_code(phone, code,createTime,type,ip) VALUES (?,?,UNIX_TIMESTAMP(),?,?)";
		int i = 0;
		try {
			i = jdbcTemplate
					.update(sql, new Object[] { phone, code, type, ip });
		} catch (Exception e) {
			logger.error(e);
		}
		return i;
	}

	@Override
	public Map<String, Object> queryCodeByPhoneAndCode(String phone,
			String code, Integer type) {
		Map<String, Object> map = null;
		try {
			String sql = "select * from t_code where phone=? and type=? and state=1";
			if (!StringUtil.isEmpty(code)) {
				sql += " and code='" + code + "' ";
			}
			sql +=" order by createTime desc limit 0,1";
			map = jdbcTemplate.queryForMap(sql, new Object[] { phone, type });
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return map;
	}

	@Override
	public Integer addMemberToken(String memberId, String _token) {
		/*String sql = "delete from t_member_token where memberId='"
				+ memberId
				+ "';INSERT INTO t_member_token(memberId,_token,createTime) values(?,?,?)";*/
		String sql = "INSERT INTO t_member_token(memberId,_token,createTime) values(?,?,?)";
		Integer i = 1;
		try {
			if (!StringUtil.isEmpty(memberId)) {
				jdbcTemplate.update(sql, new Object[] { memberId, _token,
						Util.queryNowTime() });
			}
		} catch (DataAccessException e) {
			logger.warn(e);
			i = 0;
		}
		return i;
	}

	@Override
	public String addPhoneUpdate(String updateId, String oldPhone,
			String newPhone) {
		String sql = "insert into t_update_phone_record(id,updateId,oldPhone,newPhone,createTime) values(?,?,?,?,?)";
		String id = Util.getUUID();
		try {
			jdbcTemplate.update(sql, new Object[] { id, updateId, oldPhone,
					newPhone, Util.queryNowTime() });
		} catch (DataAccessException e) {
			logger.error(e);
			return null;
		}
		return id;
	}

	public Map<String, Object> sendCode(String phone, Integer type, String ip) {
		int sendCode = (int) ((Math.random() * 9 + 1) * 1000);
		try {
			String code = sendCode + "";
			String[] params = {code};
			Boolean flag = QCloudSmsUtil.sendSmsByTemId(phone, SMSTemplateIdUtil.sendCode_SMS, params);
			/*Boolean flag = BatchPublishSMSMessageDemo.opera(phone,
					code = "{\"authcode\":\"" + code + "\"}",
					SMSTemplateIdUtil.sendCode_SMS);*/
			if (flag) {
				this.updateCodeState(phone, type, null);
				Integer i = this.addCode(phone, type, sendCode + "", ip);
				if (i > 0) {
					return Util.resultMap(configCode.code_1001, null);// 成功
				} else {
					TransactionAspectSupport.currentTransactionStatus()
							.setRollbackOnly();
					return Util.resultMap(configCode.code_1029, null);
				}
			} else {
				return Util.resultMap(configCode.code_1044, null);// 发送失败
			}
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus()
					.setRollbackOnly();
			return Util.resultMap(configCode.code_1015, null);// 发送失败
		}
	}

	@Override
	public Map<String, Object> queryLogisiticsByOrderNo(String orderNo) {
		Map<String, Object> map = null;
		try {
			if (!StringUtil.isEmpty(orderNo)) {
				String sql = "SELECT orderNo,logisticsId logisticsCode,express logisticsName,logisticsNumber logisticsNo,receiver,deliveryAddress,receiverMobile from t_logistics where orderNo='"
						+ orderNo + "' ";
				map = jdbcTemplate.queryForMap(sql);
			}
		} catch (Exception e) {
			logger.info(e);
		}
		return map;
	}
	
	@Override
	// 查询用户token
	public Map<String, Object> queryLoginLogByUserId(String userId) {
		String sql = "SELECT log.id,log.userId,log.token,log.ostype,log.model,log.version,log.createTime,log.type FROM t_login_log log WHERE log.userId=? ORDER BY log.createTime DESC LIMIT 0,1";
		Map<String, Object> userLoginLog = null;
		try {
			userLoginLog = jdbcTemplate.queryForMap(sql,
					new Object[] { userId });
		} catch (Exception e) {
			logger.error(e);
		}
		return userLoginLog;
	}

	@Override
	public Map<String, Object> queryOpenIdByPatientId(String patientId) {
		String sql="select memberId as openId,name from t_patient_data where id=?";
		Map<String, Object> map = null;
		try {
			map = jdbcTemplate.queryForMap(sql,
					new Object[] { patientId });
		} catch (Exception e) {
			logger.error(e);
		}
		return map;
	}

	@Override
	public Map<String, Object> querySysSet() {
		String sql="select * from t_sys_set";
		Map<String, Object> map = null;
		try {
			map = jdbcTemplate.queryForMap(sql);
		} catch (Exception e) {
			logger.error(e);
		}
		return map;
	}
	
	@Override
	public String queryToken() {
		String sql="select access_token from t_weixin_accesstoken where account_id=1 and UNIX_TIMESTAMP()-addTime<2*60*60";
		try {
			return jdbcTemplate.queryForObject(sql,String.class);
		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
	}
	
	@Override
	public Integer queryKeep(String openId,String doctorId) {
		Integer count = 0;
		try {
				String sql = "select count(1) from t_user_keep where memberId=? and goodsId=?";
				count = jdbcTemplate.queryForObject(sql,new Object[]{openId,doctorId},Integer.class);
		} catch (Exception e) {
			logger.error(e);
		}
		return count;
	}
	
	@Override
	public Integer addKeep(String openId, String doctorId) {
		String sql="insert into  t_user_keep(id,memberId,goodsId,createTime,type) values(?,?,?,?,?)";
		Integer i=0;
		try {
			i = jdbcTemplate.update(sql,new Object[]{Util.getUUID(),openId,doctorId,Util.queryNowTime(),3});
		} catch (DataAccessException e) {
			logger.error(e);
		}
		return i;
	}

	@Override
	public Integer deletePushToken(String userId) {
		Integer i = 0;
		try {
			if(!StringUtil.isEmpty(userId)){
				String sql = "delete from t_login_log where userId='"+userId+"' ";
				i = jdbcTemplate.update(sql);
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return i;
	}
	
	@Override
	public Integer addLogOutToken(String userId) {
		Integer i = 0;
		try {
			if(!StringUtil.isEmpty(userId)){
				String sql = "insert into t_login_log(id,userId,createTime,type) values (REPLACE(UUID(),'-',''),?,?,2)";
				i = jdbcTemplate.update(sql,new Object[]{userId,Util.queryNowTime()});
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return i;
	}

	@Override
	public Map<String, Object> queryWxUserByOpenId(String openId) {
		Map<String,Object> map = null;
		try {
			String sql = "SELECT * from t_weixin_user where openid=? ";
			if(!StringUtil.isEmpty(openId)){
				map = jdbcTemplate.queryForMap(sql,new Object[]{openId});
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return map;
	}

	@Override
	public Map<String, Object> queryMembersById(String openId) {
		Map<String,Object> map = null;
		try {
			String sql = "SELECT *,realname name from t_member where id=? ";
			if(!StringUtil.isEmpty(openId)){
				map = jdbcTemplate.queryForMap(sql,new Object[]{openId});
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return map;
	}

	@Override
	public Map<String, Object> queryPatientsById(String id) {
		Map<String,Object> map = null;
		try {
			String sql = "SELECT * from t_patient_data where id=? ";
			if(!StringUtil.isEmpty(id)){
				map = jdbcTemplate.queryForMap(sql,new Object[]{id});
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return map;
	}

	@Override
	public Map<String, Object> queryDoctorOne(String id) {
		Map<String,Object> map = null;
		try {
			String sql = "SELECT * from t_doctor where doctorId=? ";
			if(!StringUtil.isEmpty(id)){
				map = jdbcTemplate.queryForMap(sql,new Object[]{id});
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return map;
	}

	@Override
	public Object insertImSendRecord(String pushOrderNo, String fromId,
			String toId, Integer state, Integer msgType, String content,String methodName) {
		Object i = 0;
		try {
			String sql = "INSERT INTO t_im_send_record(pushOrderNo,fromId,toId,state,msgType,content,methodName) VALUES('"+pushOrderNo+"','"+fromId+"','"+toId+"',"+state+","+msgType+",'"+content+"','"+methodName+"') ";
			i = jdbcTemplate.update(sql);
		} catch (Exception e) {
			System.out.println(e);
		}
		return i;
	}

	@Override
	public Integer insertPhoneVersion(String content, String id) {
		Integer i = 0;
		try {
			if(!StringUtil.isEmpty(id)&&!StringUtil.isEmpty(content)){
				String delsql = "delete from t_phone_version_record where id='"+id+"'";
				i = jdbcTemplate.update(delsql);
				String sql = "INSERT INTO t_phone_version_record(id,content) VALUES('"+id+"','"+content+"') ";
				i = jdbcTemplate.update(sql);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return i;
	}

	@Override
	public Map<String, Object> queryEvaById(String id) {
		Map<String, Object> map = null;
		try {
			String sql = "SELECT id,name,imgUrl,remark,price,sort,type,createTime from t_eva_banner where id=? ";
			if(!StringUtil.isEmpty(id)){
				map = jdbcTemplate.queryForMap(sql,new Object[]{id});
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return map;
	}

	@Override
	public Integer querySendModeByUnRead(String userId, String type,
			String patientId) {
		String sql = "select count(1) from t_send_model where userId=? and `read`=1 and isShow=1";
		String sql1 = "select count(1) from t_send_model where userId=? and `read`=1 and isShow=1";
		Integer i = 0;
		try {
			i = jdbcTemplate.queryForObject(sql, new Object[] { userId },
					Integer.class);
		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
		return i;
	}

	@Override
	public Map<String, Object> querySalesOne(String id) {
		Map<String, Object> map = null;
		try {
			String sql = "SELECT * from t_sales_represent where srId=? ";
			if(!StringUtil.isEmpty(id)){
				map = jdbcTemplate.queryForMap(sql,new Object[]{id});
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return map;
	}

}