package cn.syrjia.hospital.dao;

import java.util.List;
import java.util.Map;

import cn.syrjia.common.BaseDaoInterface;
import cn.syrjia.entity.Evaluate;
import cn.syrjia.entity.PatientData;
import cn.syrjia.entity.ShippingAddress;
import cn.syrjia.hospital.entity.ConditionRecord;
import cn.syrjia.hospital.entity.Diagnostic;
import cn.syrjia.hospital.entity.DoctorApplyRecord;
import cn.syrjia.hospital.entity.DoctorNotice;
import cn.syrjia.hospital.entity.DoctorPatient;
import cn.syrjia.hospital.entity.DoctorSet;
import cn.syrjia.hospital.entity.DoctorZzData;
import cn.syrjia.hospital.entity.OrderDetailServer;
import cn.syrjia.hospital.entity.SendMsgRecord;

public interface AppDoctorDao extends BaseDaoInterface {
	
	/**
	 * 获取订单号
	 * @return
	 */
	public abstract String getOrderNo();

	/**
	 * 根据医生Id查询患者列表
	 * 
	 * @param doctorId
	 * @param _sign
	 * @param page
	 * @param row
	 * @return
	 */
	public abstract Map<String, Object> queryPatientsById(String doctorId,
			String _sign, Integer page, Integer row, String name);

	/**
	 * 根据患者ID查询患者管理信息
	 * 
	 * @param patientId
	 * @param page
	 * @param row
	 * @return
	 */
	public abstract Map<String, Object> queryPatientManageById(String patientId,String doctorId);

	/**
	 * 根据患者ID查询患者信息
	 * 
	 * @param patientId
	 * @return
	 */
	public abstract Map<String, Object> queryPatientById(String patientId);

	/**
	 * 编辑患者管理中信息（特别关注等）
	 * 
	 * @param patientId
	 * @return
	 */
	public abstract Integer editPatientManage(DoctorPatient docPatient);

	/**
	 * 查询是否发过通知
	 * 
	 * @param smr
	 * @return
	 */
	public abstract Map<String, Object> querySendRecord(SendMsgRecord smr);

	/**
	 * 查询医生当月是否群发
	 * @param doctorId
	 * @return
	 */
	public abstract Integer queryDoctorSendNotice(String doctorId);

	/**
	 * 分页查询医生公告列表
	 * @param doctorId
	 * @param page
	 * @param row
	 * @return
	 */
	public abstract List<Map<String, Object>> queryNoticeList(String doctorId,Integer state,Integer page,
			Integer row);

	/**
	 * 新增公告
	 * @param doctorNotice
	 * @return
	 */
	public abstract Integer addNotice(DoctorNotice doctorNotice);

	/**
	 * 修改公告信息
	 * @param doctorNotice
	 * @return
	 */
	public abstract Integer editNotice(DoctorNotice doctorNotice);

	/**
	 * 查询公告详情
	 * @param id
	 * @return
	 */
	public abstract Map<String, Object> queryNoticeDetail(String id);

	/**
	 * 删除公告信息
	 * @param id
	 * @return
	 */
	public abstract Integer deleteNoticeById(String id);
	

	/**
	 * 查询公告每天发送数量
	 * @param doctorId
	 * @return
	 */
	public abstract Integer checkNoticeDaySendCount(String doctorId);
	
	/**
	 * 回复用户评价
	 * @param evaluate
	 * @return
	 */
	public abstract Integer replyMemberEva(Evaluate evaluate);
	
	/**
	 * 修改评价状态
	 * @param evaluate
	 * @return
	 */
	public abstract Integer updateEvaState(Evaluate evaluate);
	
	/**
	 * 根据repeatId,重复方式删除
	 * @param repeatId
	 * @return
	 */
	public abstract Integer deleteZzDataByRepeatId(String repeatId,String checkDate);
	
	/**
	 * 查询医生认证信息基本数据
	 * @return
	 */
	public abstract List<Map<String,Object>> queryDoctorAuthData();
	
	/**
	 * 根据医生ID获取医生认证信息
	 * @param id
	 * @return
	 */
	public abstract Map<String,Object> queryDoctorApplyData(String doctorId);
	
	/**
	 * 根据医生ID获取医生个人信息（医馆APP个人中心用）
	 * @param id
	 * @return
	 */
	public abstract Map<String,Object> queryOneDoctor(String doctorId);
	
	/**
	 * 修改医生头像、手机号
	 * @param doctor
	 * @return
	 */
	public abstract Integer updateDoctor(String doctorId,String localUrl,String url,String phone);
	
	/**
	 * 检验医生手机号是否存在（修改手机号验证用）
	 * @param doctor
	 * @return
	 */
	public abstract Integer checkDoctorPhone(String docPhone,String doctorId);
	
	/**
	 * 检验医生认证时医生名称是否重复
	 * @param docName
	 * @param doctorId
	 * @return
	 */
	public abstract Integer checkApplyDocName(String docName,String doctorId);
	
	/**
	 * 通过手机号查询医生信息
	 * @param phone
	 * @return
	 */
	public abstract String queryDoctorByPhone(String phone);
	
	/**
	 * 根据医生ID查询医生评价信息（详情页面与评价列表共用）
	 * @param doctorId
	 * @return
	 */
	public abstract List<Map<String,Object>> queryDoctorEvaList(String doctorId,Integer row,Integer page,String _sign);
	
	/**
	 * 根据医生ID查询医生评价统计信息
	 * @param doctorId
	 * @return
	 */
	public abstract Map<String,Object> queryDoctorEvaCensus(String doctorId);
	
	/**
	 * 登录没有医生及注册
	 * @param docPhone
	 * @return
	 */
	public abstract String loginAddDoctor(String docPhone,String docName,String doctorSign,Integer isDownLoadApp,Integer downTime,String defaultYztId,String saleId);
	
	/**
	 * 验证医生手势密码、数字密码是否正确
	 * @param doctorSet
	 * @return
	 */
	public abstract Integer checkDoctorPassword(DoctorSet doctorSet);
	
	/**
	 * 修改状态
	 * @param id
	 * @return
	 */
	public abstract Integer updateChannelState(String id);
	
	/**
	 * 删除医生疾病分类关联
	 * @param id
	 * @return
	 */
	public abstract Integer delMiddleByDocId(String id);
	
	/**
	 * 根据医生ID删除医生部门关联
	 * @param docId
	 * @param departId
	 * @return
	 */
	public abstract Integer delDocAndDepartUtil(String docId,String departId);
	
	/**
	 * 检验医生自己设置是否存在
	 * @param doctorId
	 * @return
	 */
	public abstract String checkDocSetById(String doctorId);
	
	/**
	 * 查询医生有没有待审核的简介
	 * @param doctorId
	 * @return
	 */
	public abstract Integer queryAbstractRecordByDoctorId(String doctorId);
	
	/**
	 * 查询一周日期
	 * @return
	 */
	public abstract List<Map<String, Object>> queryWeekData();
	
	/**
	 * 根据医生ID查询医生14天出诊状态
	 * @param doctorId
	 * @return
	 */
	public abstract List<Map<String, Object>> queryFourTeenZzStatus(String doctorId);
	
	/**
	 * 批量增加坐诊信息
	 * @param zzData
	 * @param date
	 * @return
	 */
	public abstract Object insertDoctorZzData(DoctorZzData zzData,List<String> dates);
	
	/**
	 * 删除坐诊信息
	 * @param doctorId
	 * @param state
	 * @return
	 */
	public abstract Integer updateDoctroZzState(String id);
	
	/**
	 * 获取医院列表
	 * @return
	 */
	public abstract List<Map<String,Object>> queryInfirmaryList(String name,Integer page,Integer row);
	
	/**
	 * 根据坐诊ID查询坐诊信息
	 * @param id
	 * @return
	 */
	public abstract Map<String,Object> queryDoctorZzDataById(String id);
	
	/**
	 * 查询医生坐诊信息
	 * @param doctroId
	 * @return
	 */
	public abstract List<Map<String,Object>> queryDoctorZzList(String doctroId,String zzDate,Integer state);
	
	/**
	 * 查询医生坐诊时间列表
	 * @param doctroId
	 * @return
	 */
	public abstract List<Map<String,Object>> queryDoctorZzTimes(String doctroId,String zzDate);
	
	/**
	 * 根据医生id查询问诊/复诊模板
	 * @param doctroId
	 * @return
	 */
	public abstract Map<String,Object> querySpecialTest(String doctroId);
	
	/**
	 * 查询名著
	 * @param name
	 * @param page
	 * @param row
	 * @return
	 */
	public abstract List<Map<String,Object>> querySpecialTestClassic(String name,Integer page, Integer row);
	
	/**
	 * 通过医生id查询
	 * @param doctroId
	 * @param type
	 * @param testId
	 * @return
	 */
	public abstract List<Map<String,Object>> querySpecialTestByDoctorId(String doctroId,Integer type,String testId);
	
	/**
	 * 通过id查询
	 * @param testId
	 * @return
	 */
	public abstract Map<String,Object> querySpecialTestById(String testId);
	
	/**
	 * 通过id查询调理方
	 * @param conditioningId
	 * @return
	 */
	public abstract Map<String,Object> queryConditioningById(String conditioningId);
	
	/**
	 * 删除
	 * @param testId
	 * @return
	 */
	public abstract Integer deleteSpecialTest(String testId);
	
	/**
	 * 删除调理方
	 * @param conditioningId
	 * @return
	 */
	public abstract Integer deleteConditioning(String conditioningId);
	
	/**
	 * 删除调理方详情
	 * @param conditioningId
	 * @return
	 */
	public abstract Integer deleteConditioningDetail(String conditioningId);
	
	/**
	 * 添加默认的值
	 * @param testId
	 * @param doctorId
	 * @param type
	 * @return
	 */
	public abstract Integer addSpecialTestDefault(String testId,String doctorId,Integer type);

	/**
	 * 删除默认值
	 * @param testId
	 * @param doctorId
	 * @param type
	 * @return
	 */
	public abstract Integer deleteSpecialTestDefault(String testId, String doctorId,Integer type);
	
	/**
	 * 更新调理方默认值
	 * @param conditioningId
	 * @param doctorId
	 * @return
	 */
	public abstract Integer updateConditioningDefault(String conditioningId,String doctorId);
	
	/**
	 * 删除标题
	 * @param testId
	 * @return
	 */
	public abstract Integer deleteSpecialTestTitle(String testId);
	
	/**
	 * 根据医生id查询调理方/经典方
	 * @param doctroId
	 * @return
	 */
	public abstract Map<String,Object> queryConditioning(String doctroId);
	
	/**
	 * 通过医生id查询调理方
	 * @param doctroId
	 * @param name
	 * @param type
	 * @param conditioningId
	 * @param page
	 * @param row
	 * @return
	 */
	public abstract List<Map<String,Object>> queryConditioningByDoctorId(String doctroId,String name,Integer type,String conditioningId,Integer page,Integer row);
	
	/**
	 * 通过医生id查询历史方
	 * @param doctroId
	 * @param name
	 * @param type
	 * @param conditioningId
	 * @param patientId
	 * @param page
	 * @param row
	 * @return
	 */
	public abstract List<Map<String,Object>> queryHistoryConditioningByDoctorId(String doctroId,String name,Integer type,String conditioningId,String patientId,Integer page,Integer row);
	
	/**
	 * 通过id查询调理方详情
	 * @param id
	 * @return
	 */
	public abstract List<Map<String,Object>> queryConditioningDetailById(String id);
	
	/**
	 * 通过id查询历史方
	 * @param id
	 * @return
	 */
	public abstract List<Map<String,Object>> queryHistoryConditioningDetailById(String id);
	
	/**
	 * 根据查询问诊/复诊模板id查询模板详情
	 * @param doctroId
	 * @return
	 */
	public abstract List<Map<String, Object>> querySpecialTestDetail(String testId);
	
	/**
	 * 查询标题
	 * @param titleId
	 * @return
	 */
	public abstract Map<String, Object> querySpecialTestTitle(String titleId);
	
	/**
	 * 查最新的id
	 * @param testId
	 * @return
	 */
	public abstract Integer querySpecialMaxQid(String testId);
	
	/**
	 * 查询调理方详情
	 * @param conditioningId
	 * @return
	 */
	public abstract List<Map<String, Object>> queryConditioningDetail(String conditioningId);
	
	/**
	 * 查询题目
	 * @param detailId
	 * @return
	 */
	public abstract List<Map<String, Object>> querySpecialTestDetailOption(String detailId);

	/**
	 * 查询症型列表
	 * @param diagnostic
	 * @return
	 */
	public abstract List<Map<String,Object>> queryDiagnosticList(Diagnostic diagnostic);
	
	/**
	 * 查询药品列表(开方页面用)
	 * @param name
	 * @return
	 */
	public abstract List<Map<String,Object>> queryDrugList(String name,String yztId);
	
	/**
	 * 查询患者病历档案
	 * @param doctorId
	 * @return
	 */
	public abstract List<Map<String,Object>> queryPatientServerRecords(String doctorId,String patientId,Integer page,Integer row,String year);
	
	/**
	 * 查询复诊时间列表
	 * @return
	 */
	public abstract List<Map<String,Object>> queryRepeatTimes();
	
	/**
	 * 查询处方模版处主药品列表
	 * @return
	 */
	public abstract List<Map<String,Object>> queryDrugMasterList();
	
	/**
	 * 查询主药
	 * @param name
	 * @param page
	 * @param row
	 * @return
	 */
	public abstract List<Map<String,Object>> queryDrugMasters(String yztId,String name,Integer page,Integer row);
	
	/**
	 * 根据医生ID、患者ID查询调理方订单
	 * @param doctorId
	 * @param patientId
	 * @return
	 */
	public abstract List<Map<String,Object>> queryConditionsByPatientId(String doctorId,String patientId,String wzDate,Integer page,Integer row);
	
	/**
	 * 根据调理记录查询调理方案、药品记录
	 * @param doctorId
	 * @param patientId
	 * @return
	 */
	public abstract List<Map<String,Object>> queryConditionsByRecordId(String recordId);

	/**
	 * 查询患者病历图像(问诊单ID)
	 * @param doctorId
	 * @param patientId
	 * @return
	 */
	public abstract List<Map<String,Object>> queryPatientImg(String doctorId,String patientId,String wzDate,Integer page,Integer row);
	
	/**
	 * 查询患者病历图像
	 * @param doctorId
	 * @param patientId
	 * @return
	 */
	public abstract List<Map<String, Object>> queryPatientCaseImgs(String ids);
	/**
	 * 查询所有问诊日期(同一天在一个页面展示)
	 * @param doctorId
	 * @param patientId
	 * @return
	 */
	public abstract List<Map<String,Object>> queryWzDates(String doctorId,String patientId);

	/**
	 * 根据药品ID集合获取药品列表
	 * @param ids
	 * @return
	 */
	public abstract Map<String,Object> queryDrugById(String id);
	
	/**
	 * 根据药品ID集合字符串获取药品列表
	 * @param ids
	 * @return
	 */
	public abstract List<Map<String,Object>> queryDrugListByIds(String ids);
	
	/**
	 * 批量插入药品记录信息
	 * @param list
	 */
	public abstract Object insertDrugRecords(List<Map<String,Object>> list);
	
	/**
	 * 查询服务类型年
	 * @param doctorId
	 * @param patientId
	 * @param page
	 * @param row
	 * @return
	 */
	public abstract List<Map<String,Object>> queryServerYears(String doctorId,String patientId);
	
	/**
	 * 查询常用语
	 * @param doctorId
	 * @return
	 */
	List<Map<String,Object>> queryPhrase(String doctorId);
	
	/**
	 * 添加常用语
	 * @param doctorId
	 * @param content
	 * @return
	 */
	Integer addPhrase(String id,String doctorId,String content);
	
	/**
	 * 更新短语
	 * @param id
	 * @param doctorId
	 * @param content
	 * @return
	 */
	Integer updatePhrase(String id,String doctorId,String content);
	
	
	/**
	 * 删除常用语
	 * @param doctorId
	 * @param phraseId
	 * @return
	 */
	Integer deletePhrase(String doctorId,String phraseId);
	
	/**
	 * 查询常用语以有数量
	 * @param doctorId
	 * @return
	 */
	Integer queryPhraseCount(String doctorId);
	
	/**
	 * 百宝箱我的商品选择（开药时用）
	 * @param doctorId
	 * @return
	 */
	List<Map<String,Object>> queryMyGoods(String doctorId,String name,Integer page,Integer row);

	/**
	 * 点击助理提交修改状态
	 * @param applyRecord
	 * @return
	 */
	public abstract Integer updateApplyRecordState(DoctorApplyRecord applyRecord);
	
	/**
	 * 根据医生ID查询助理是否存在
	 * @param doctorId
	 * @return
	 */
	public abstract Map<String,Object> checkDoctorFollowId(String doctorId);
	
	/**
	 * 查询最后一次服务订单
	 * @param patientId
	 * @param doctorId
	 * @return
	 */
	public abstract Map<String,Object> queryLastOrderNo(String patientId,String doctorId);
	
	/**
	 * 查询订单记录
	 * @param orderNo
	 * @return
	 */
	public abstract Map<String,Object> queryRecordOrder(String orderNo);
	
	/**
	 * 查询订单价格
	 * @param orderNo
	 * @return
	 */
	public abstract Map<String,Object> queryRecordOrderPrice(String orderNo);
	
	/**
	 * 更新订单价格记录
	 * @param orderNo
	 * @param orderPrice
	 * @param receiptsPrice
	 * @param goodsPrice
	 * @return
	 */
	public abstract Integer updateRecordOrderPrice(String orderNo,Double orderPrice,Double receiptsPrice,Double goodsPrice);

	/**
	 * 更新订单号记录
	 * @param orderNo
	 * @param patientId
	 * @param shippingAddress
	 * @return
	 */
	public abstract Integer updateRecordOrdeNo(String orderNo,String patientId,ShippingAddress shippingAddress);
	
	/**
	 * 通过用户id更新订单记录
	 * @param orderNo
	 * @param patientId
	 * @param memberId
	 * @return
	 */
	public abstract Integer updateRecordOrdeNoMemberId(String orderNo,String patientId,String memberId);
	
	/**
	 * 查询药品价格记录
	 * @param orderNo
	 * @param orderType
	 * @return
	 */
	public abstract Map<String, Object> queryDrugRecordPrice(String orderNo,Integer orderType);
	
	/**
	 * 查询药的记录价格通过主订单
	 * @param orderNo
	 * @return
	 */
	public abstract Map<String, Object> queryDrugRecordPriceByMainOrderNo(String orderNo);
	
	/**
	 * 通过主订单查询服务类型
	 * @param orderNo
	 * @return
	 */
	public abstract Integer queryServerTypeByMainOrderNo(String orderNo);
	
	/**
	 * 查询调理方记录
	 * @param recordId
	 * @return
	 */
	public abstract List<Map<String,Object>> queryConditioningRecord(String recordId);
	
	/**
	 * 查询药品记录
	 * @param recordId
	 * @return
	 */
	public abstract List<Map<String,Object>> queryDrugRecord(String recordId);
	
	/**
	 * 通过订单号查询商品
	 * @param orderNo
	 * @return
	 */
	public abstract List<Map<String,Object>> queryGoodsByOrderNo(String orderNo);
	
	/**
	 * 通过药品记录查询商品
	 * @param orderNo
	 * @return
	 */
	public abstract Map<String, Object> queryGoodsByDrugRecord(String orderNo);
	
	/**
	 * 查询最后一个订单
	 * @param patientId
	 * @param doctorId
	 * @return
	 */
	public abstract Map<String,Object> queryLastOrde(String patientId,String doctorId);
	
	/**
	 * 查询总结算
	 * @param doctorId
	 * @param settlementType
	 * @param startTime
	 * @param endTime
	 * @param checkDoctorId
	 * @return
	 */
	public abstract Map<String,Object> querySettlementTotal(String doctorId,Integer settlementType,String startTime,String endTime,String checkDoctorId);
	
	/**
	 * 查询结算
	 * @param doctorId
	 * @param settlementType
	 * @param startTime
	 * @param endTime
	 * @param checkDoctorId
	 * @param page
	 * @param row
	 * @return
	 */
	public abstract List<Map<String,Object>> querySettlement(String doctorId,Integer settlementType,String startTime,String endTime,String checkDoctorId,Integer page,Integer row);
	
	/**
	 * 查询订单结算详情
	 * @param orderNo
	 * @return
	 */
	public abstract List<Map<String,Object>> querySettlementOrderDetail(String orderNo);
	
	/**
	 * 查询默认的特价商品
	 * @param doctorId
	 * @param type
	 * @return
	 */
	public abstract String queryDefultSpecial(String doctorId,Integer type);
	
	/**
	 * 更新最后一个订单
	 * @param orderNo
	 * @return
	 */
	public abstract Integer updateOrderByEnd(String orderNo);
	
	/**
	 * 更新发送的模板
	 * @param userId
	 * @param modes
	 * @return
	 */
	public Integer updateSendMode(String userId, String[] modes);
	
	/**
	 * 更新发送模板
	 * @param userId
	 * @return
	 */
	public Integer updateSendMode(String userId);
	
	/**
	 * 查询未读通知数
	 * @param userId
	 * @return
	 */
	public Integer querySendModeByUnRead(String userId,String type);
	
	/**
	 * 查询最后一个发送的模板
	 * @param userId
	 * @param type
	 * @return
	 */
	public Map<String,Object> querySendModeByLast(String userId,String type);
	
	/**
	 *  查询推送消息纪录
	 * @param userId
	 * @param page
	 * @param row
	 * @return
	 */
	public abstract List<Map<String, Object>> querySendModePage(String userId,Integer type,Integer page,Integer row);
	
	/**
	 * 根据医生ID条件、分页查询就诊列表
	 * @param doctorId
	 * @param page
	 * @param row
	 * @param type 1-全部 2-待付款 3-待发货 4-待收货 5-已完成
	 * @return
	 */
	public abstract List<Map<String,Object>> queryJzList(String doctorId,Integer page,Integer row,Integer type);
	
	/**
	 * 修改是否接单
	 * @param doctorId
	 * @param isAccpetAsk
	 * @return
	 */
	public abstract Integer updateDoctorisAccpetAsk(String doctorId,Integer isAccpetAsk);

	/**
	 * 查询医生接单接口是否开启
	 * @param doctorId
	 * @return
	 */
	public abstract Integer queryDocIsAccOn(String doctorId);
	
	/**
	 * 删除调理方案
	 * @param id
	 * @return
	 */
	public abstract Integer deleteConditionById(String id);
	
	/**
	 * 修改调理单获取订单信息接口（orderNo-调理单订单号）
	 */
	public abstract Map<String,Object> queyrDetailByOrderNo(String orderNo,String doctorId);
	
	/**
	 * 根据订单号修改服务订单详情内容
	 * @param orderNo
	 * @return
	 */
	public abstract Integer updateOrderDetailServer(OrderDetailServer detailServer);
	
	/**
	 * 修改调理记录状态改为修改状态
	 * @param recordId
	 * @return
	 */
	public abstract Integer updateRecipeRecordState(String recordId,Integer state);
	
	/**
	 * 查询作废理由列表
	 * @return
	 */
	public abstract List<Map<String,Object>> queryDeleteReasons();
	
	/**
	 * 作废调理方
	 * @param orderNo
	 * @return
	 */
	public abstract Integer cancleRecipeState(String orderNo,String cannelNote,Integer isServer);
	
	/**
	 * 通过记录id查询处方订单
	 * @param recordId
	 * @return
	 */
	public abstract Map<String,Object> queryRecipeOrderByRecordId(String recordId);
	
	/**
	 * 查询医生4种服务是否均以关闭
	 * @param doctorId
	 * @return
	 */
	public abstract Integer queryDoctorSetIsClose(String doctorId);
	
	/**
	 * 查询我的粉丝列表接口
	 * @param doctorId
	 * @param page
	 * @param row
	 * @return
	 */
	public abstract Map<String,Object> queryFollowMembers(String doctorId,Integer page,Integer row,String name);

	/**
	 * 查询未认证个人简介数量
	 * @param doctorId
	 * @return
	 */
	public abstract Integer queryAbstractIsNotSuccess(String doctorId);
	
	/**
	 * 查询最后一条医生简介记录
	 * @param doctorId
	 * @return
	 */
	public abstract Map<String,Object> queryLastAbstract(String doctorId);
	
	/**
	 * 根据手机号查询是否存在申请记录 
	 * @param docPhone
	 * @return
	 */
	public abstract Integer deleteApplyRecrodByDoctorId(String docPhone);
	
	/**
	 * 修改医生简介信息
	 * @param doctorId
	 * @param content
	 * @return
	 */
	public abstract Integer updateDocAbstract(String doctorId,String content);
	
	/**
	 * 查询每天拨打次数
	 * @param fromId
	 * @param toId
	 * @param type
	 * @return
	 */
	public abstract Integer queryPhoneRecordCount(String fromId,String toId,Integer type);

	/**
	 * 查询单个医院信息
	 * @param infirmaryId
	 * @return
	 */
	public abstract Map<String,Object> queryOneInfirmary(String infirmaryId);
	
	/**
	 * 查询是否有重复坐诊信息
	 * @param zzData
	 * @return
	 */
	public abstract Integer queryIsCfZzData(DoctorZzData zzData);
	
	/**
	 * 根据订单号查询订单
	 * @param orderNo
	 * @return
	 */
	public abstract Map<String,Object> queryOneOrder(String orderNo);
	
	/**
	 * 根据订单号查询二维码
	 * @param orderNo
	 * @return
	 */
	public abstract Map<String,Object> queryOrderQrCode(String orderNo);
	
	/**
	 * 插入订单号查询二维码
	 * @param orderNo
	 * @return
	 */
	public abstract Object insertOrderQrCode(String orderNo,String qrCodeUrl);
	
	/**
	 * 查询医患最后一个交流服务类型
	 * @param doctorId
	 * @param patientId
	 * @return
	 */
	public abstract Map<String,Object> queryLastSeverName(String doctorId,String patientId);

	/**
	 * 查询历史银行卡信息
	 * @param doctorId
	 * @param page
	 * @param row
	 * @return
	 */
	public abstract List<Map<String,Object>> queryBankHistorys(String doctorId,Integer page,Integer row);
	
	/**
	 * 查询医生最新银行卡号
	 * @param doctorId
	 * @return
	 */
	public abstract Map<String,Object> queryLastBankByDoctorId(String doctorId);

	/**
	 * 查询银行字典列表
	 * @return
	 */
	public abstract List<Map<String,Object>> queryBankList();
	
	/**
	 * 查询订单详情服务
	 * @param orderNo
	 * @return
	 */
	public abstract Map<String,Object> queryOrderDetailServer(String orderNo);
	
	/**
	 * 添加调理单记录
	 * @param conditionRecords
	 * @return
	 */
	public abstract Integer addConditionRecords(List<ConditionRecord> conditionRecords);
	
	/**
	 * 查询首页统计信息
	 * @param doctorId
	 * @return
	 */
	public abstract Map<String,Object> queryIndexCount(String doctorId);
	
	/**
	 * 查询患者通过医生id
	 * @param doctorId
	 * @return
	 */
	public abstract List<Map<String,Object>> queryPatientsByDoctorId(String doctorId);
	
	/**
	 * 通过助理id查询关联
	 * @param srId
	 * @return
	 */
	public abstract List<Map<String,Object>> queryFollowBySrId(String srId);
	
	/**
	 * 通过url查询订单
	 * @param orderNo
	 * @return
	 */
	public abstract Map<String,Object> queryOrderQrUrl(String orderNo);
	
	/**
	 * 修改医生是否已下载app
	 * @param doctorId
	 * @return
	 */
	public abstract Integer updateDoctorDownloadState(String doctorId);
	
	/**
	 * 查询十八反十九畏集合
	 * @return
	 */
	public abstract Map<String, Object> queryConfict();
	
	/**
	 * 查询最后一次交流信息
	 * @param doctorId
	 * @param patientId
	 * @return
	 */
	public abstract Map<String,Object> queryLastChatMap(String doctorId,String patientId);
	
	/**
	 * 查询最后一次服务
	 * @param doctorId
	 * @param patientId
	 * @return
	 */
	public abstract Map<String,Object> queryLastServerMap(String doctorId,String patientId);
	
	/**
	 * 根据手机号查询关联医生数量
	 * @param phone
	 * @return
	 */
	public abstract Integer queryDocCountByPhone(String phone);

	/**
	 * 查询是否存在申请记录
	 * @param doctorId
	 * @return
	 */
	public abstract Integer queryDocApply(String doctorId);
	/**
	 * 电话咨询完成
	 * @param doctorId
	 * @return
	 */
	public abstract Integer updatePhoneAdvisory(String orderNo);
	
	/**
	 * 根据OPENID查询此人是否已有导入人
	 * @param openid
	 * @return
	 */
	public abstract Integer queryFollowCountById(String openid);
	
	/**
	 * 查询医生信息根据ID
	 * @param doctorId
	 * @return
	 */
	public abstract Map<String,Object> queryDocById(String doctorId);
	
	/**
	 * 查询单个医生
	 * @param doctorId
	 * @return
	 */
	public abstract Map<String,Object> queryPushDoctor(String doctorId);
	/**
	 * 查询收货地址
	 * @param id
	 * @return
	 */
	public abstract ShippingAddress queryShippingAddress(String id);
	
	/**
	 * 查询患者信息
	 * @param doctorId
	 * @return
	 */
	public abstract PatientData queryPatientData(String id);
	
	/**
	 * 修改订单主订单号
	 * @param orderNo
	 * @return
	 */
	public abstract Integer updateOrderMainOrderNo(String orderNo);
	
	/**
	 * 查询银行卡信息单个
	 * @param id
	 * @return
	 */
	public abstract Map<String,Object> queryBankById(String id);
	
	/**
	 * 查询所有未使用锦旗订单号
	 * @param memberId
	 * @return
	 */
	public abstract List<Map<String,Object>> queryEvaBannerOrders(String memberId);
	
	/**
	 * 查询是否已存在未支付补缴挂号费订单
	 * @param doctorId
	 * @param memberId
	 * @param patientId
	 * @return
	 */
	public abstract Integer queryIsHasBjGhPrice(String doctorId,String memberId,String patientId);

	/**
	 * 根据老服务订单号删除订单
	 * @param orderNo
	 * @return
	 */
	public abstract Integer deleteOldWzOrder(String orderNo);
/**
 * 根据订单号查询抄方申请
 * 
 * */
	public abstract Map<String, Object> lookCfOrder(String orderNo);
	
	/**
	 * 拍照开方插入图片（多张）
	 * @param imgArr
	 * @param id
	 * @return
	 */
	public abstract  Integer addOrderPhoto(String[] imgArr, String id);

}

