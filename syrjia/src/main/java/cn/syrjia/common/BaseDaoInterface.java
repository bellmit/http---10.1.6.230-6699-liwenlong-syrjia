package cn.syrjia.common;

import java.util.List;
import java.util.Map;

/**
 */
public abstract interface BaseDaoInterface {

	/**
	 * 根据实体查询
	 * 
	 * @param entity
	 *            实体
	 * @return
	 */
	public abstract <T> List<T> query(T entity,boolean...isChild);

	/**
	 * 根据类名和条件查询
	 * 
	 * @param t
	 * @return
	 */
	public abstract <T> List<T> query(Class<T> t, Map<String, Object> map,boolean...isChild);

	/**
	 * 根据类名和条件查询
	 * 
	 * @param t
	 * @return
	 */
	public abstract <T> T queryOne(Class<T> t, Map<String, Object> map,boolean...isChild);

	/**
	 * 根据实体查询单个
	 * 
	 * @param entity
	 *            实体
	 * @return
	 */
	public abstract <T> T queryByEntity(T entity,boolean...isChild);

	/**
	 * 根据id查询单个
	 * 
	 * @param id
	 *            id
	 * @return
	 */
	public abstract <T> T queryById(Class<T> t, Object id,boolean...isChild);

	/**
	 * 分页
	 * 
	 * @param entity
	 *            实体 T 参数对应 页数 行数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public abstract <T> Map<String, Object> queryListByPage(T entity, T... i);

	/**
	 * 分页
	 * 
	 * @param entity
	 *            实体 T 参数对应 页数 行数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public abstract <T> Integer queryListByPageNum(T entity, T... i);

	/**
	 * 分页
	 * 
	 * @param entity
	 *            实体 T 参数对应 页数 行数
	 * @return
	 */
	public abstract <T> Map<String, Object> queryListByPage(Class<T> t,
			Map<String, Object> param,boolean...isChild);

	/**
	 * 分页 总条数
	 * 
	 * @param entity
	 * @param param
	 * @return
	 */
	public abstract <T> Integer queryListByPageNum(Class<T> entity,
			Map<String, Object> param);

	/**
	 * 修改必须有ID 根据ID
	 */
	public abstract <T> Integer updateEntity(T entity);

	/**
	 * 修改必须有ID 根据ID
	 */
	public abstract <T> Integer updateOrAdd(T entity);

	/**
	 * 删除 根据Id
	 */
	public abstract <T> Integer deleteEntity(Class<T> entity, Object id);

	/**
	 * 批量删除 根据Id
	 */
	public abstract <T> Integer deleteEntityList(Class<T> entity, List<Object> ids);

	/**
	 * 增加
	 */
	public abstract <T> Object addEntity(T entity);
	
	/**
	 * 增加
	 */
	public abstract <T> Object addEntityUUID(T entity);
	
	
	/**
	 * 批量增加
	 */
	public abstract <T> Object addEntity(List<T> entitys);
	
	/**
	 * 批量增加
	 */
	public abstract <T> Object addEntityUUID(List<T> entitys);

	/**
	 * 是否存在 返回数量
	 * 
	 * @param <T>
	 */
	public abstract <T> Integer exist(Class<T> t, Map<String, Object> map);

	/**
	 * 是否存在不是本id 返回数量
	 * 
	 * @param <T>
	 */
	public abstract <T> Integer existId(Class<T> t, Map<String, Object> map);
	
	
	/**
	 * 批量修改
	 * @param <T>
	 */
	public abstract <T> Integer batchUpdate(List<T> entitys);
	
	
	/**
	 * 自定义sql 查询数量
	 * @param sql
	 * @param obj
	 * @return
	 */
	public abstract Integer queryBysqlCount(String sql,Object[] obj);
	
	/**
	 * 自定义sql 查询列表List<Map<String,Object>>
	 * @param sql
	 * @param obj
	 * @return
	 */
	public abstract List<Map<String,Object>> queryBysqlList(String sql,Object[] obj);
	
	
	/**
	 * 自定义sql 查询列表List<Bean>
	 * @param <T>
	 * @param sql
	 * @param obj
	 * @return
	 */
	public abstract <T> List<T> queryBysqlListBean(Class<T> t,String sql,Object[] obj);
	
	/**
	 * 自定义sql 查询列表Map<String,Object>
	 * @param <T>
	 * @param sql
	 * @param obj
	 * @return
	 */
	public abstract Map<String,Object> queryBysqlMap(String sql,Object[] obj);
	
	
	/**
	 * 自定义sql 修改/增加 返回1成功 0失败
	 * @param <T>
	 * @param sql
	 * @param obj
	 * @return
	 */
	public abstract Integer update(String sql,Object[] obj);
	
	/**
	 * 自定义sql 删除 返回1成功 0失败
	 * @param <T>
	 * @param sql
	 * @param obj
	 * @return
	 */
	public abstract Integer delete(String sql,Object[] obj);
	
	/**
	 * 自定义sql 批量删除 返回成功数
	 * @param <T>
	 * @param sql
	 * @param obj
	 * @return
	 */
	public abstract Integer deleteBatch(String sql,List<Object> list);
	
	public Map<String,Object> getTicket();

	/**
	 * 地区查询
	 * @return
	 */
	public abstract List<Map<String,Object>> queryProvinces();
	
	/**
	 * 地区查询
	 * @return
	 */
	public abstract List<Map<String,Object>> queryCitys(String provinceId);
	
	/**
	 * 地区查询
	 * @return
	 */
	public abstract List<Map<String,Object>> queryAreas(String cityId);
	
	/**
	 * 获取系统设置信息
	 * @return
	 */
	public abstract Map<String,Object> getSysSet();
	
	/**
	 * 根据手机号查询获取验证码次数
	 * @param phone
	 * @param type
	 * @return
	 */
	public abstract Integer checkSendCodeCount(String phone,Integer type);
	
	/**
	 * 修改验证码状态
	 * @param phone
	 * @param type
	 * @return
	 */
	public abstract Integer updateCodeState(String phone,Integer type,String code);
	
	/**
	 * 将验证码存入库中
	 * @param phone
	 * @param type
	 * @param code
	 * @param ip
	 * @return
	 */
	public abstract Integer addCode(String phone,Integer type,String code,String ip);
	
	/**
	 * 验证验证码是否存在
	 * @param phone
	 * @param code
	 * @param type
	 * @return
	 */
	public abstract Map<String,Object> queryCodeByPhoneAndCode(String phone,String code,Integer type);
	
	/**
	 * 添加请求token
	 * @param memberId
	 * @param _token
	 * @return
	 */
	public abstract Integer addMemberToken(String memberId,String _token);
	
	/**
	 * 修改手机号加入记录表
	 * @param docPhone
	 * @return
	 */
	public abstract String addPhoneUpdate(String updateId,String oldPhone,String newPhone);
	
	/**
	 * 发送验证码
	 * @param phone
	 * @param type
	 * @param ip
	 * @return
	 */
	public abstract Map<String, Object> sendCode(String phone, Integer type, String ip);

	/**
	 * 根据订单号查询物流
	 * @param orderNo
	 * @return
	 */
	public abstract Map<String,Object> queryLogisiticsByOrderNo(String orderNo);
	
	/**
	 * 查询用户token,ostype,mode,version
	 * @param userId
	 * @return
	 */
	public abstract Map<String, Object> queryLoginLogByUserId(String userId);
	
	public abstract Map<String, Object> queryOpenIdByPatientId(String patientId);
	
	public abstract Map<String,Object> querySysSet();
	
	public abstract String queryToken();
	
	public abstract Integer queryKeep(String openId,String doctorId);
	
	public abstract Integer addKeep(String openId,String doctorId);
	
	public abstract Integer deletePushToken(String userId);
	
	public abstract Integer addLogOutToken(String userId);
	
	public abstract Map<String,Object> queryWxUserByOpenId(String openId);
	
	public abstract Map<String,Object> queryMembersById(String openId);
	
	public abstract Map<String,Object> queryPatientsById(String id);
	
	public abstract Map<String,Object> queryDoctorOne(String id);
	
	public abstract Object insertImSendRecord(String pushOrderNo,String fromId,String toId,Integer state,Integer msgType,String content,String methodName);

	public abstract Integer insertPhoneVersion(String content,String id);
	
	public abstract Map<String,Object> queryEvaById(String id);
	
	/**
	 * 查询未读通知数
	 * @param userId
	 * @return
	 */
	public Integer querySendModeByUnRead(String userId,String type,String patientId);
	
	public abstract Map<String,Object> querySalesOne(String id);
	
}
