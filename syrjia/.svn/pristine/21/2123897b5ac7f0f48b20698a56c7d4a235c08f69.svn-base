package cn.syrjia.sales.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.syrjia.common.BaseServiceInterface;
import cn.syrjia.entity.LoginLog;
import cn.syrjia.sales.entity.FeedBack;
import cn.syrjia.sales.entity.SalesRepresent;
import cn.syrjia.sales.entity.SalesSet;

public interface SalesService extends BaseServiceInterface {
	
	/**
	 * 修改助理手机号
	 * 
	 * @param request
	 * @param phone
	 * @param code
	 * @param srId
	 * @return
	 */
	public abstract Map<String, Object> updateSalesPhone(
			HttpServletRequest request, String phone, String code,
			String srId);
	
	/**
	 * 退出登录
	 * 
	 * @param oldToken
	 * @return
	 */
	public abstract Map<String, Object> loginOut(String oldToken);
	
	/**
	 * 编辑安全设置信息
	 * 
	 * @param salesSet
	 * @return
	 */
	public abstract Map<String, Object> editSalesSet(SalesSet salesSet);
	
	/**
	 * 验证手势密码、数字密码是否正确
	 * @param salesSet
	 * @return
	 */
	public abstract Map<String,Object> checkSalesPassword(SalesSet salesSet);
	
	/**
	 * 根据ID获取助理个人信息
	 * 
	 * @param id
	 * @return
	 */
	public abstract Map<String, Object> queryOneSales(String srId);
	
	/**
	 * 修改助理信息
	 * @param sales
	 * @return
	 */
	public abstract Map<String, Object> updateSales(SalesRepresent sales);
	
	/**
	 * 获取手机验证码
	 * 
	 * @param request
	 * @param doctorId
	 * @param phone
	 * @param type
	 * @return
	 */
	public abstract Map<String, Object> getPhoneCode(
			HttpServletRequest request, String srId, String phone,
			Integer type);

	/**
	 * 助理端APP登录接口
	 * 
	 * @param request
	 * @param loginName
	 * @param code
	 * @param loginLog
	 * @return
	 */
	public abstract Map<String, Object> login(HttpServletRequest request,
			String loginName, String password, LoginLog loginLog);
	
	/**
	 * 验证老手机号、验证码是否正确
	 * 
	 * @param phone
	 * @param doctorId
	 * @param code
	 * @return
	 */
	public abstract Map<String, Object> validateOldPhoneAndCode(String phone,
			String srId, String code);
	
	/**
	 * 根据医生ID获取医生详情
	 * @param doctorId
	 * @return
	 */
	public abstract Map<String,Object> queryDoctorById(String doctorId);
	
	/**
	 * 根据医生ID获取医生详情下方展示内容
	 * @param doctorId
	 * @return
	 */
	public abstract Map<String,Object> queryDoctorOtherById(String doctorId);
	
	/**
	 * 根据医生ID查询医生文章信息
	 * @param doctorId
	 * @return
	 */
	public abstract Map<String,Object> queryDoctorArticleList(String srId,String doctorId,Integer row,Integer page);

	/**
	 * 根据医生ID查询医生评价信息（详情页面与评价列表共用）
	 * @param doctorId
	 * @return
	 */
	public abstract Map<String,Object> queryDoctorEvaList(String doctorId,Integer row,Integer page,String _sign);
	
	/**
	 * 助理查询我的医生列表
	 * @param srId
	 * @param type 默认医生注册的时间倒序 0-按照日接诊量降序排序 1-按照日成单额降序排序 2-按照总接诊量降序排序 3-按照总成单额降序排序 4-导入患者数量倒序排列 5-未认证 6- 医院简拼升序排列
	 * @param page
	 * @param row
	 * @param searchKey
	 * @return
	 */
	public abstract Map<String,Object> queryMyDoctors(String srId,Integer type,Integer page,Integer row,String searchKey);
	
	/**
	 * 获取助理安全设置
	 * @param srId
	 * @return
	 */
	public abstract Map<String,Object> querySalesSetById(String srId);
	
	/**
	 * 修改密码接口
	 * @param srId
	 * @param phone
	 * @param oldPassword
	 * @param newPassrod
	 * @return
	 */
	public abstract Map<String,Object> updatePassword(String srId,String phone,String oldPassword,String newPassrod,String code);
	
	/**
	 * 根据助理ID查询医生
	 * @param srId
	 * @return
	 */
	public abstract Map<String,Object> queryDoctors(String srId);
	
	/**
	 * 查询一周日期
	 * @return
	 */
	public abstract Map<String, Object> queryWeekData();
	
	/**
	 * 根据医生ID查询医生14天出诊状态
	 * @param doctorId
	 * @return
	 */
	public abstract Map<String, Object> queryFourTeenZzStatus(String doctorId);
	
	/**
	 *查询反馈部门列表
	 * @return
	 */
	public abstract Map<String,Object> queryFeedDeparts();
	
	/**
	 * 新增反馈信息
	 * @param feedBack
	 * @return
	 */
	public abstract Map<String,Object> addFeedBack(FeedBack feedBack);
	
	/**
	 * 助理端查询订单列表
	 * @param srId
	 * @param doctorId
	 * @return
	 */
	public abstract Map<String,Object> queryOrders(String srId,String doctorId,Integer page,Integer row,Integer type,String startTime,String endTime);
	
	/**
	 * 助理端查询订单列表（新）
	 * @param srId
	 * @param doctorId
	 * @return
	 */
	public abstract Map<String,Object> querySalesOrders(String srId,String doctorId,Integer page,Integer row,Integer type,String startTime,String endTime);
	
	/**
	 * 订单列表查询我的医生列表
	 * @param srId
	 * @param page
	 * @param row
	 * @return
	 */
	public abstract Map<String,Object> queryOrderDoctors(String srId,Integer page,Integer row);

	/**
	 * 不涉及订单拨打电话
	 * @param srId
	 * @param doctorId
	 * @return
	 */
	public abstract Map<String,Object> salesCallNoOrder(String srId,String doctorId);
}
