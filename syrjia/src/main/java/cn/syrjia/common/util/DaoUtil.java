package cn.syrjia.common.util;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Date;
import cn.syrjia.common.annotation.Disable;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Join;
import cn.syrjia.common.annotation.OneToMany;
import cn.syrjia.common.annotation.Table;

/**
 * dao反射转实体
 * 
 * @author Administrator
 * 
 */
public class DaoUtil {

	/**
	 * 根据泛型实例化实体并注入参数
	 * 
	 * @param t
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T setEntity(T t, ResultSet rs) {

		if (null == rs || null == t) {
			return null;
		}

		Class<?> c = t.getClass();
		Object obj = null;
		try {
			obj = c.newInstance();// 实例化

			Field[] fs = c.getDeclaredFields();// 获取所有属性

			for (Field f : fs) {// 遍历属性
				/**
				 * 如果设置禁用此属性
				 */
				boolean b = getDisableAnnotationName(f);
				if (b) {
					continue;
				}
				// 获得注解名称
				String column = getAnnotationName(f,
						cn.syrjia.common.annotation.Column.class);
				if (!f.getType().isPrimitive()
						&& !f.getType().isAssignableFrom(String.class)
						&& !f.getType().isAssignableFrom(Integer.class)&& !f.getType().isAssignableFrom(Double.class)
						&& !f.getType().isAssignableFrom(BigDecimal.class)
						&& !f.getType().isAssignableFrom(java.sql.Timestamp.class)
						&& !f.getType().isAssignableFrom(java.util.Date.class)) {
					continue;
				}
				Object value = null;
				try {
					value = rs.getObject(column, f.getType());
				} catch (Exception e) {
					continue;
				}
				f.setAccessible(true); // 设置些属性是可以访问的
				f.set(obj, value);
				/*
				 * if (f.getType().isAssignableFrom(String.class)) {//
				 * 如果是String类型的 f.set(obj, rs.getObject(column,
				 * f.getType()).toString()); } else if
				 * (f.getType().isAssignableFrom(Integer.class) ||
				 * f.getType().isAssignableFrom(int.class)) {// 如果为int类型
				 * f.set(obj, Integer.parseInt(rs.getObject(column,
				 * f.getType()).toString())); } else if
				 * (f.getType().isAssignableFrom(BigDecimal.class)) {//
				 * 如果为bigdecimal f.set(obj, new BigDecimal(rs.getObject(column,
				 * f.getType()) .toString())); }
				 */
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (T) obj;
	}

	/**
	 * 根据泛型实例化实体并注入参数
	 * 
	 * @param t
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T setEntity(Class<T> c, ResultSet rs) {

		if (null == rs) {
			return null;
		}

		Object obj = null;
		try {
			obj = c.newInstance();// 实例化

			Field[] fs = c.getDeclaredFields();// 获取所有属性

			for (Field f : fs) {// 遍历属性
				/**
				 * 如果设置禁用此属性
				 */
				boolean b = getDisableAnnotationName(f);
				if (b) {
					continue;
				}
				String column = getAnnotationName(f,
						cn.syrjia.common.annotation.Column.class);
				Class<?> columnType = f.getType();
				if (!columnType.isPrimitive()
						&& !columnType.isAssignableFrom(String.class)
						&& !columnType.isAssignableFrom(Integer.class)
						&& !columnType.isAssignableFrom(Long.class)
						&& !columnType.isAssignableFrom(Double.class)
						&& !columnType.isAssignableFrom(BigDecimal.class)
						&& !columnType.isAssignableFrom(Boolean.class)
						&& !f.getType().isAssignableFrom(java.sql.Timestamp.class)
						&& !f.getType().isAssignableFrom(java.util.Date.class)) {
					continue;
				}
				Object value = null;
				try {
					value = rs.getObject(column, f.getType());
				} catch (Exception e) {
					continue;
				}
				// 获得注解名称
				f.setAccessible(true); // 设置些属性是可以访问的
				f.set(obj, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (T) obj;
	}

	/**
	 * 获得表名
	 * 
	 * @param t
	 * @return
	 */
	public static <T> String getTableName(T t) {
		if (null == t) {
			return null;
		}
		Class<?> c = t.getClass();
		Table table = c.getAnnotation(Table.class);
		if (null != table) {
			if (table.name().equals("classname")) {
				return null;
			} else {
				return table.name();
			}
		}
		return null;
	}

	/**
	 * set属性
	 * 
	 * @param t
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	public static <T> void setEntityDeclared(T t, Object obj, String name)
			throws Exception {
		if (null == t) {
			return;
		}
		Field f = t.getClass().getDeclaredField(name);
		if (f == null) {
			return;
		}
		try {
			f.setAccessible(true); // 设置些属性是可以访问的
			f.set(t, obj);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * get属性
	 * 
	 * @param t
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	public static <T> Object getEntityDeclared(T t, String name)
			throws Exception {
		if (null == t) {
			return null;
		}
		PropertyDescriptor pd = null;
		try {
			pd = new PropertyDescriptor(name, t.getClass());
		} catch (Exception e) {
			return null;
		}
		Method getMethod = pd.getReadMethod();// 获得get方法
		Object o = getMethod.invoke(t);// 执行get方法返回一个Object
		return o;
	}

	/**
	 * 获得表名
	 * 
	 * @param t
	 * @return
	 */
	public static <T> String getTableName(Class<T> c) {
		if (null == c) {
			return null;
		}
		Table table = c.getAnnotation(Table.class);
		if (null != table) {
			if (table.name().equals("classname")) {
				return null;
			} else {
				return table.name();
			}
		}
		return null;
	}

	/**
	 * 获得列属性
	 * 
	 * @param t
	 * @return
	 */
	public static <T> Map<String, Object> getColumnProperty(Field f) {
		Column column = f.getAnnotation(Column.class);
		if (null == column) {
			return null;
		}
		if (null == column.value()||"".equals(column.value()[0])) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("value", column.value());
		map.put("symbol", column.symbol());
		map.put("type", column.type());
		return map;
	}
	
	/**
	 * 获得排序属性
	 * 
	 * @param t
	 * @return
	 */
	public static <T> String getColumnSort(Field f) {
		Column column = f.getAnnotation(Column.class);
		if (null == column) {
			return null;
		}
		if (!column.isSort()) {
			return null;
		}
		return 	column.sortType();
	}
	
	/**
	 * 获得排序属性
	 * 
	 * @param t
	 * @return
	 */
	public static <T> List<Map<String,Object>> getColumnSort(Class<T> c) {
		Field[] fs = c.getDeclaredFields();// 获取所有属性
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		for (Field f : fs) {
			String sort= getColumnSort(f);
			if(null!=sort&&!"".equals(sort.trim())){
				Map<String,Object> map=new HashMap<String, Object>();
				map.put("sort", sort);
				map.put("column",getAnnotationName(f,Column.class));
				list.add(map);
			}
		}
		return list;
	}

	/**
	 * 获取注解属性
	 * 
	 * @param f
	 * @param c
	 * @return
	 */
	public static String getAnnotationName(Field f,
			Class<? extends Annotation> c) {
		Object annotation = f.getAnnotation(c);
		Column column = null;
		Id id = null;
		if (annotation instanceof Column) {
			column = (Column) annotation;
			if (null != annotation) {// 如果没有注解 默认不禁用且名字为属性名
				if (column.name().equals("attrName")) {
					return f.getName();
				} else {
					return column.name();
				}
			}
		}
		if (annotation instanceof Id) {
			id = (Id) annotation;
			if (null != annotation) {// 如果没有注解 默认不禁用且名字为属性名
				if (id.name().equals("id")) {
					return f.getName();
				} else {
					return id.name();
				}
			}
		}
		return f.getName();
	}

	/**
	 * 是否主键
	 * 
	 * @param f
	 * @param c
	 * @return
	 */
	public static String getAnnotationId(Field f, Class<? extends Annotation> c) {
		Object annotation = f.getAnnotation(c);
		Id id = null;
		if (annotation instanceof Id) {
			id = (Id) annotation;
			if (null != annotation) {// 如果没有注解 默认不禁用且名字为属性名
				if (id.name().equals("id")) {
					return f.getName();
				} else {
					return id.name();
				}
			}
		}
		return null;
	}

	/**
	 * 是否主键自增
	 * 
	 * @param f
	 * @param c
	 * @return
	 */
	public static String getAnnotationIdIncrement(Field f,
			Class<? extends Annotation> c) {
		Object annotation = f.getAnnotation(c);
		Id id = null;
		if (annotation instanceof Id) {
			id = (Id) annotation;
			if (null != annotation) {// 如果没有注解 默认不禁用且名字为属性名
				if (!id.increment()||id.UUID()) {
					return null;
				}
				if (id.name().equals("id")) {
					return f.getName();
				} else {
					return id.name();
				}
			}
		}
		return null;
	}

	/**
	 * 获取主键
	 * 
	 * @param <T>
	 * 
	 * @param f
	 * @param c
	 * @return
	 */
	public static <T> String getAnnotationId(Class<T> c) {
		Field[] fs = c.getDeclaredFields();// 获取所有属性
		Id id = null;
		for (Field f : fs) {
			Object annotation = f.getAnnotation(Id.class);
			if (null == annotation) {
				continue;
			}
			id = (Id) annotation;
			if (id.name().equals("id")) {
				return f.getName();
			} else {
				return id.name();
			}
		}
		return null;
	}

	/**
	 * 获取disaple注解属性
	 * 
	 * @param f
	 * @param c
	 * @return
	 */
	public static boolean getDisableAnnotationName(Field f) {
		Disable annotation = f.getAnnotation(Disable.class);
		if (null != annotation) {
			if (annotation.disable()) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
	
	/**
	 * 获取disaple注解属性
	 * 
	 * @param f
	 * @param c
	 * @return
	 */
	public static boolean getColumnAnnotationIsNull(Field f) {
		Column annotation = f.getAnnotation(Column.class);
		if (null != annotation) {
			if (annotation.isNull()) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}


	/**
	 * 获取一对多注解属性
	 * 
	 * @param f
	 * @param c
	 * @return
	 */
	public static Map<String, Object> getOneToManyAnnotationName(Field f) {
		Map<String, Object> map = new HashMap<String, Object>();
		OneToMany annotation = f.getAnnotation(OneToMany.class);
		if (null != annotation) {
			Type type = null;
			if (f.getClass().isPrimitive()
					|| f.getType().isAssignableFrom(String.class)) {// 是否基本数据类型
				return null;
			}
			if (f.getType().isAssignableFrom(List.class)) {// 如果为list类型
				type = f.getGenericType();// 获得list里的泛型
				if (null == type) {
					return null;
				}
				if (!(type instanceof ParameterizedType)) {// 如果是泛型参数的类型
					return null;
				}
			}
			ParameterizedType pt = (ParameterizedType) type;
			Class<?> genericClazz = (Class<?>) pt.getActualTypeArguments()[0]; // 得到泛型里的class类型对象。
			if (genericClazz.isPrimitive()
					|| f.getType().isAssignableFrom(String.class)) {
				return null;
			}
			map.put("fieldName", f.getName());
			map.put("className", genericClazz);
			map.put("contentTable", annotation.contentTable());
			map.put("correlationField", annotation.correlationField());
			map.put("correlationOtherField", annotation.correlationOtherField());
			map.put("correlationTableField", annotation.correlationTableField());
			map.put("correlationTableOtherField",
					annotation.correlationTableOtherField());
			return map;
		}
		return null;
	}

	/**
	 * 获取外键注解属性
	 * 
	 * @param <T>
	 * 
	 * @param f
	 * @param c
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> Map<String, Object> getJoinAnnotationName(Field f,List<Map<String, Object>> list) {
		Map<String, Object> map = null;
		Join join = f.getAnnotation(Join.class);
		if (null != join) {
			boolean flag=false;
			for(Map<String,Object> m : list){
				if(m.get("table").toString().equals(join.table())){
					List<String> l1=(List<String>) m.get("columnName");
					List<String> l2=(List<String>) m.get("otherName");
					l1.add(f.getName());
					l2.add("default".equals(join.otherName())?f.getName():join.otherName());
					flag=true;
				}
			}
			List<String> columnNames=new ArrayList<String>();
			List<String> otherNames=new ArrayList<String>();
			columnNames.add(f.getName());
			map = new HashMap<String, Object>();
			map.put("table", join.table());
			map.put("id", join.id());
			map.put("otherId",join.otherId());
			if ("default".equals(join.otherName())) {
				otherNames.add(f.getName());
			} else {
				otherNames.add(join.otherName());
			}
			map.put("otherName",otherNames);
			map.put("joinType", join.joinType());
			map.put("columnName",columnNames);
			if(!flag){
				list.add(map);
			}
			return map;
		}
		return map;
	}

	/**
	 * 获取date注解属性
	 * 
	 * @param <T>
	 * 
	 * @param f
	 * @param c
	 * @return
	 */
	public static <T> Map<String, Object> getDateAnnotationName(Field f) {
		Map<String, Object> map = null;
		Date date = f.getAnnotation(Date.class);
		if (null != date) {
			map = new HashMap<String, Object>();
			map.put("symbol", date.symbol());
			map.put("field", date.field());
		}
		return map;
	}

	/**
	 * 根据实体拼接sql
	 * 
	 * @param entity
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	public static <T> Map<String, Object> jointSql(T entity, T... t) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> correlationTableList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> joinTableList = new ArrayList<Map<String, Object>>();
		StringBuffer sb = new StringBuffer();
		List<Map<String,String>> annotationSort=new ArrayList<>();
		Object[] obj = null;
		Map<String, List<String>> mapList = null;
		List<String> sortList = null;
		List<String> notEquals = null;
		List<String> stringLike = null;
		String sort = "desc";
		try {
			if (null != t) {
				mapList = (Map<String, List<String>>) t[2];
				sortList = mapList.get("sortList");
				notEquals = mapList.get("notEquals");
				stringLike = mapList.get("stringLike");
				sort = t[3].toString();
			}
		} catch (Exception e1) {

		}
		try {
			Class<?> c = entity.getClass();
			Field[] fs = c.getDeclaredFields();// 获取所有属性
			obj = new Object[fs.length];
			int i = 0;
			String tableName = DaoUtil.getTableName(c);
			for (Field f : fs) {
				/**
				 * 如果设置禁用此属性
				 */
				boolean b = getDisableAnnotationName(f);
				if (b) {
					continue;
				}
				Map<String, Object> correlationTable = getOneToManyAnnotationName(f);// 关联表名
				if (null != correlationTable && correlationTable.size() > 0) {
					correlationTableList.add(correlationTable);
					continue;
				}

				Map<String, Object> joinTable = getJoinAnnotationName(f,joinTableList);

				boolean b1 = false;

				if (null != joinTable && joinTable.size() > 0) {
					//joinTableList.add(joinTable);
					b1 = true;
					// continue;
				}
				
				PropertyDescriptor pd = null;
				try {
					pd = new PropertyDescriptor(f.getName(), c);
				} catch (Exception e) {
					continue;
				}
				String columnName = getAnnotationName(f, Column.class);
				

				if(null==sortList||sortList.size()==0){
					Map<String,String> m=new HashMap<String, String>();
					String type=getColumnSort(f);
					if(null!=type&&!"".equals(type)){
						m.put("type",getColumnSort(f));
						m.put("columnName",columnName);
						annotationSort.add(m);
					}
				}
				
				Method getMethod = pd.getReadMethod();// 获得get方法
				Object o = getMethod.invoke(entity);// 执行get方法返回一个Object
				if (null == o || "".equals(o.toString().trim())) {
					Map<String, Object> columnPropertyMap = getColumnProperty(f);
					if (null == columnPropertyMap) {
						continue;
					}
					Object[] value=(Object[])columnPropertyMap.get("value");
					String symbol=columnPropertyMap.get("symbol").toString();
					String type=columnPropertyMap.get("type").toString();
					if(type.equals("and")){
						for(Object v:value){
							sb.append(" and " + tableName + "_a." + columnName +" "+symbol+" "
									+"?");
							obj[i++] = v;
						}
					}else{
						sb.append(" and (");
						for(int q=0;q<value.length;q++){
							sb.append(tableName + "_a." + columnName + symbol
									+"?");
							if(q!=value.length-1){
								sb.append(" or ");
							}
							obj[i++] = value[q];
						}
						sb.append(" )");
					}
					continue;
				}
				Map<String, Object> dateMap = getDateAnnotationName(f);

				if (null != dateMap && dateMap.size() > 0) {
					String symbol = dateMap.get("symbol").toString();
					String field = dateMap.get("field").toString();
					sb.append(" and " + tableName + "_a." + field + symbol
							+ " ?");
					obj[i++] = o;
				} else if (null != notEquals && notEquals.contains(columnName)) {
					String val[]=o.toString().split(",");
					for(String s:val){
						sb.append(" and " + tableName + "_a." + columnName
								+ "<> ?");
						obj[i++] = s;
					}
					
				} else if (null != stringLike
						&& stringLike.contains(columnName)) {
					if (b1) {
						sb.append(" and " + joinTable.get("table").toString()
								+ "_a." + columnName + " like '"
								+ o.toString() + "%'");
					} else {
						sb.append(" and " + tableName + "_a." +columnName
								+ " like '" + o.toString() + "%'");
					}
				} else {
					sb.append(" and " + tableName + "_a." + columnName + "= ?");
					obj[i++] = o;
				}
			}
			if (null != sortList) {
				sb.append(" order by ");
				for (String s : sortList) {
					if(sortList.size()-1!=sortList.lastIndexOf(s))
					sb.append( tableName + "_a." + s + ", " );
					else sb.append( tableName + "_a." + s + " " );
				}
				sb.append(sort);
			}else if(null!=annotationSort&&annotationSort.size()>0){
				sb.append(" order by ");
				for (Map<String,String> s : annotationSort) {
					if(annotationSort.size()-1!=annotationSort.lastIndexOf(s))
					sb.append( tableName + "_a." + s.get("columnName").toString() + ", " );
					else sb.append( tableName + "_a." + s.get("columnName").toString() + " " );
					sb.append(s.get("type").toString());
				}
			}
			map.put("sql", sb);
			map.put("obj", arrayRemoveNull(obj));
			map.put("correlationTableList", correlationTableList);
			map.put("joinTableList", joinTableList);
		} catch (Exception e) {
			return map;
		}
		return map;
	}

	/**
	 * 根据实体拼接sql
	 * 
	 * @param entity
	 * @return
	 */
	public static <T> List<Map<String, Object>> queryJoin(Class<T> c) {
		List<Map<String, Object>> joinTableList = new ArrayList<Map<String, Object>>();
		try {
			Field[] fs = c.getDeclaredFields();// 获取所有属性
			for (Field f : fs) {
				/*Map<String, Object> joinTable = getJoinAnnotationName(f,);
				if (null != joinTable && joinTable.size() > 0) {
					joinTableList.add(joinTable);
					continue;
				}*/
				 getJoinAnnotationName(f,joinTableList);
			}
		} catch (Exception e) {
			return joinTableList;
		}
		return joinTableList;
	}

	/**
	 * 根据实体拼接sql
	 * 
	 * @param entity
	 * @return
	 */
	public static <T> List<Map<String, Object>> queryOneToMony(Class<T> c) {
		List<Map<String, Object>> correlationTableList = new ArrayList<Map<String, Object>>();
		try {
			Field[] fs = c.getDeclaredFields();// 获取所有属性
			for (Field f : fs) {
				Map<String, Object> correlationTable = getOneToManyAnnotationName(f);// 关联表名
				if (null != correlationTable && correlationTable.size() > 0) {
					correlationTableList.add(correlationTable);
					continue;
				}
			}
		} catch (Exception e) {
			return correlationTableList;
		}
		return correlationTableList;
	}

	/**
	 * 根据参数拼接sql
	 * 
	 * @param entity
	 * @return
	 */
	public static <T> Map<String, Object> jointSql(Class<T> entity,
			Map<String, Object> map) {
		StringBuffer sb = new StringBuffer();
		Object[] obj = null;
		if(null!=map&&map.size()>0){
			obj = new Object[map.size()];
		}else{
			return null;
		}
		try {
			String tableName = DaoUtil.getTableName(entity);
			// 遍历map
			int i = 0;
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				if (entry.getKey().equals("page")
						|| entry.getKey().equals("row")) {
					continue;
				}
				sb.append(" and " + entry.getKey() + "=?");
				obj[i++] = entry.getValue();
			}
			List<Map<String, Object>> list=getColumnSort(entity);
			for(Map<String, Object> m:list){
					if(0==list.lastIndexOf(m))
					sb.append(" order by ");
					if(list.size()-1!=list.lastIndexOf(m))
					sb.append(" cast(" +tableName + "_a." + m.get("column").toString() + " as unsigned), " );
					else 	sb.append(" cast(" +tableName + "_a." + m.get("column").toString() + " as unsigned) " );
					sb.append(m.get("sort").toString());
			}
			map = new HashMap<String, Object>();
			map.put("sql", sb);
			map.put("obj", arrayRemoveNull(obj));
		} catch (Exception e) {
			return map;
		}
		return map;
	}

	/**
	 * 根据参数拼接sql 最后一个参数为不等
	 * 
	 * @param entity
	 * @return
	 */
	public static <T> Map<String, Object> jointSqlLastNotEquals(
			Class<T> entity, Map<String, Object> map) {
		StringBuffer sb = new StringBuffer();
		Object[] obj = new Object[map.size()];
		try {
			// 遍历map
			int i = 0;
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				if (entry.getKey().equals("page")
						|| entry.getKey().equals("row")) {
					continue;
				}
				if (i == map.size() - 1) {
					sb.append(" and " + entry.getKey() + "<>?");
				} else {
					sb.append(" and " + entry.getKey() + "=?");
				}
				obj[i++] = entry.getValue();
			}
			map = new HashMap<String, Object>();
			map.put("sql", sb);
			map.put("obj", arrayRemoveNull(obj));
		} catch (Exception e) {
			return map;
		}
		return map;
	}

	/**
	 * 根据主键拼接sql
	 * 
	 * @param entity
	 * @return
	 */
	public static <T> Map<String, Object> jointIdSql(Class<T> entity, Object id,Boolean... b) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		Object[] obj = new Object[1];
		String tableName=getTableName(entity);
		try {
			String idName = getAnnotationId(entity);
			if (null == idName)
				return map;
			if(null!=b&&b.length>0&&b[0]){
				sb.append(" and "+ getAnnotationId(entity) + "=?");
			}else{
				sb.append(" and "+tableName+"_a." + getAnnotationId(entity) + "=?");
			}
			obj[0] = id;
			map.put("sql", sb);
			map.put("obj", obj);
		} catch (Exception e) {
			return map;
		}
		return map;
	}

	/**
	 * 根据主键拼接sql
	 * 
	 * @param entity
	 * @return
	 */
	public static <T> String jointIdsSql(Class<T> entity) {
		StringBuffer sb = new StringBuffer();
		try {
			String idName = getAnnotationId(entity);
			if (null == idName)
				return null;
			sb.append(" and " + getAnnotationId(entity) + "=?");
		} catch (Exception e) {
			return sb.toString();
		}
		return sb.toString();
	}

	/**
	 * 根据实体拼接sql
	 * 
	 * @param entity
	 * @return
	 */
	public static <T> Map<String, Object> jointInsertSql(T entity) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		StringBuffer sbValue = new StringBuffer();
		sb.append(" (");
		sbValue.append(" values(");
		Object[] obj = null;
		try {
			Class<?> c = entity.getClass();
			Field[] fs = c.getDeclaredFields();// 获取所有属性
			obj = new Object[fs.length];
			int i = 0;
			for (Field f : fs) {
				/**
				 * 如果设置禁用此属性
				 */
				boolean b = getDisableAnnotationName(f);
				if (b) {
					continue;
				}
				
				if(!f.isAccessible()&&!f.getType().isAssignableFrom(Integer.class)
						&&!f.getType().isAssignableFrom(String.class)
						&&!f.getType().isAssignableFrom(Double.class)
						&&!f.getType().isAssignableFrom(BigDecimal.class)
						&&!f.getType().isAssignableFrom(Long.class)
						&&!f.getType().isAssignableFrom(Boolean.class)
						&&!f.getType().isAssignableFrom(java.util.Date.class))
					continue;
				PropertyDescriptor pd = null;
				try {
					pd = new PropertyDescriptor(f.getName(), c);
				} catch (Exception e) {
					continue;
				}
				String name = getAnnotationIdIncrement(f, Id.class);
				if (null != name) {
					continue;
				}
				Method getMethod = pd.getReadMethod();// 获得get方法
				Object o = getMethod.invoke(entity);// 执行get方法返回一个Object
				if (null == o || "".equals(o.toString().trim())) {
					continue;
				}
				String columnName = getAnnotationName(f, Column.class);
				sb.append(columnName + ",");
				sbValue.append("?,");
				obj[i++] = o;
			}
			sb = new StringBuffer(sb.substring(0, sb.length() - 1));
			sbValue = new StringBuffer(sbValue.substring(0,
					sbValue.length() - 1));
			map.put("sql", sb.append(")").append(sbValue).append(")"));
			map.put("obj", arrayRemoveNull(obj));
		} catch (Exception e) {
			return map;
		}
		return map;
	}

	/**
	 * 根据实体拼接sql
	 * 
	 * @param entity
	 * @return
	 */
	public static <T> Map<String, Object> jointSetSql(T entity) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		StringBuffer sbWhere = new StringBuffer();
		sb.append(" set");
		sbWhere.append(" where 1=1 ");
		Object[] obj = null;
		Object[] whereObj = null;
		try {
			Class<?> c = entity.getClass();
			Field[] fs = c.getDeclaredFields();// 获取所有属性
			obj = new Object[fs.length];
			whereObj = new Object[fs.length];
			int i = 0;
			int whereI = 0;
			for (Field f : fs) {
				/**
				 * 如果设置禁用此属性
				 */
				boolean b = getDisableAnnotationName(f);
				if (b) {
					continue;
				}
				
				if(!f.isAccessible()
						&&!f.getType().isAssignableFrom(Integer.class)
						&&!f.getType().isAssignableFrom(String.class)
						&&!f.getType().isAssignableFrom(Double.class)
						&&!f.getType().isAssignableFrom(boolean.class)
						&&!f.getType().isAssignableFrom(Boolean.class)
						&&!f.getType().isAssignableFrom(java.util.Date.class)
						)
					continue;
				
				PropertyDescriptor pd = null;
				try {
					pd = new PropertyDescriptor(f.getName(), c);
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
				Method getMethod = pd.getReadMethod();// 获得get方法
				Object o = getMethod.invoke(entity);// 执行get方法返回一个Object
				if (null == o || "".equals(o.toString().trim())) {
					if(f.toString().indexOf("wzPrice")>-1||f.toString().indexOf("visitTime")>-1||f.toString().indexOf("otherDia")>-1||f.toString().indexOf("remarks")>-1){
						
					}else{
						boolean isNull = getColumnAnnotationIsNull(f);
						if (!isNull)
							continue;
					}
				}
				String name = getAnnotationId(f, Id.class);
				if (null != name) {
					sbWhere.append(" and " + name + " =?");
					whereObj[whereI++] = o;
					continue;
				}
				String columnName = getAnnotationName(f, Column.class);
				if (sb.toString().trim().equals("set"))
					sb.append(" " + columnName + "=?");
				else
					sb.append(" , " + columnName + "=?");
				obj[i++] = null==o?"":o;
			}
			map.put("sql", sb.append(sbWhere));
			map.put("obj", concat(obj, whereObj));
		} catch (Exception e) {
			return null;
		}
		return map;
	}

	/**
	 * 数组去除空字符串
	 * 
	 * @param obj
	 * @return
	 */
	public static Object[] arrayRemoveNull(Object obj) {
		Object[] o = (Object[]) obj;
		List<Object> list = new ArrayList<Object>();
		for (Object o1 : o) {
			if (null != o1) {
				list.add("".equals(o1.toString())?null:o1);
			}
		}
		return 0 == list.size() ? new Object[0] : list.toArray();
	}

	/**
	 * 解析参数
	 * 
	 * @param str
	 * @param t
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> Integer queryT(String str, T... t) {
		Integer i = null;
		switch (str) {
		case "row":
			try {
				i = Integer.parseInt(t[1].toString());
			} catch (Exception e) {
				i = 10;
			}
			break;
		case "page":
			try {
				int page = Integer.parseInt(t[0].toString());
				if(page>0){
					i = (page-1)*Integer.parseInt(t[1].toString());
				}
			} catch (Exception e) {
				i = 0;
			}
			break;
		default:
			break;
		}
		return i;
	}

	/**
	 * 数组合并
	 * 
	 * @param obj
	 * @param obj1
	 * @return
	 */
	public static Object[] concat(Object[] obj, Object[] obj1) {
		Object[] o = new Object[obj.length + obj1.length];
		System.arraycopy(obj, 0, o, 0, obj.length);
		System.arraycopy(obj1, 0, o, obj.length, obj.length);
		return arrayRemoveNull(o);
	}

	public static <T> String dateToString(String str, T date) {
		String time = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(str);
			time = sdf.format(date);
		} catch (Exception e) {
			return time;
		}
		return time;
	}
}
