package cn.syrjia.sales.dao;

import java.util.List;
import java.util.Map;

import cn.syrjia.common.BaseDaoInterface;
import cn.syrjia.sales.entity.SalesRepresent;
import cn.syrjia.sales.entity.SalesSet;
public interface SalesDao extends BaseDaoInterface {
	
	/**
	 * 根据助理ID查询医生
	 * @param srId
	 * @return
	 */
	public abstract List<Map<String,Object>> queryDoctors(String srId);

	/**
	 * 根据助理ID获取医生个人信息
	 * @param srId
	 * @return
	 */
	public abstract Map<String,Object> queryOneSales(String srId,Integer state,String phone);
	
	/**
	 * 修改助理头像、手机号、修改密码
	 * @return
	 */
	public abstract Integer updateSales(SalesRepresent sales);
	
	/**
	 * 检验助理手机号是否存在（修改手机号验证用）
	 * @return
	 */
	public abstract Integer checkSalesPhone(String phone,String srId);
	
	/**
	 * 通过手机号查询助理信息
	 * @param phone
	 * @return
	 */
	public abstract String querySalesByPhone(String phone);
	
	/**
	 * 验证手势密码、数字密码是否正确
	 * @param doctorSet
	 * @return
	 */
	public abstract Integer checkSalesPassword(SalesSet salesSet);
	
	/**
	 * 检验助理自己设置是否存在
	 * @param doctorId
	 * @return
	 */
	public abstract String checkSalesSetById(String srId);
	
	/**
	 * 查询登录接口
	 * @param loginName
	 * @param password
	 * @return
	 */
	public abstract Map<String,Object> login(String loginName,String password);
	
	/**
	 * 根据医生ID查询医生详情信息
	 * @param doctorId
	 * @return
	 */
	public abstract Map<String,Object> queryDoctorById(String doctorId);
	
	/**
	 * 根据医生查询医生专业擅长
	 * @return
	 */
	public abstract List<Map<String,Object>> queryDoctorIlls(String doctorId);
	
	/**
	 * 查询医生开着的线上调理信息
	 * @param doctorId
	 * @return
	 */
	public abstract List<Map<String,Object>> queryDoctorOnline(String doctorId);
	
	/**
	 * 查询一周日期
	 * @return
	 */
	public abstract List<Map<String, Object>> queryWeekData();
	
	/**
	 * 查询从今天起7天内坐诊状态
	 * @param doctorId
	 * @return
	 */
	public abstract List<Map<String,Object>> querySevenZzStatus(String doctorId);
	
	/**
	 * 根据医生ID查询医生14天出诊状态
	 * @param doctorId
	 * @return
	 */
	public abstract List<Map<String, Object>> queryFourTeenZzStatus(String doctorId);

	/**
	 * 查询医生14天是否有出诊
	 * @param doctorId
	 * @return
	 */
	public abstract Integer queryFourTeenCzCount(String doctorId);
	
	/**
	 * 根据医生ID查询医生文章信息
	 * @param doctorId
	 * @return
	 */
	public abstract List<Map<String,Object>> queryDoctorArticleList(String srId,String doctorId,Integer row,Integer page);
	
	/**
	 * 根据医生ID查询医生简介及公告信息
	 * @param doctorId
	 * @return
	 */
	public abstract Map<String,Object> queryDoctorAbstractAndDesc(String doctorId);
	
	/**
	 * 根据医生ID查询医生评价信息（详情页面与评价列表共用）
	 * @param doctorId
	 * @return
	 */
	public abstract List<Map<String,Object>> queryDoctorEvaList(String doctorId,Integer row,Integer page,String _sign);

	/**
	 * 助理查询我的医生列表
	 * @param srId
	 * @param type 默认医生注册的时间倒序 0-按照日接诊量降序排序 1-按照日成单额降序排序 2-按照总接诊量降序排序 3-按照总成单额降序排序 4-导入患者数量倒序排列 5-未认证 6- 医院简拼升序排列
	 * @param page
	 * @param row
	 * @param searchKey
	 * @return
	 */
	public abstract List<Map<String,Object>> queryMyDoctors(String srId,Integer type,Integer page,Integer row,String searchKey);
	
	/**
	 * 获取助理安全设置
	 * @param srId
	 * @return
	 */
	public abstract Map<String,Object> querySalesSetById(String srId);
	
	/**
	 *查询反馈部门列表
	 * @return
	 */
	public abstract List<Map<String,Object>> queryFeedDeparts();
	
	/**
	 * 查询订单号列表
	 * @param srId
	 * @param doctorId
	 * @param page
	 * @param row
	 * @param type 1-待付款 5-已完成  0-全部
	 * @return
	 */
	public abstract List<Map<String,Object>> queryOrderNos(String srId,String doctorId,Integer page,Integer row,Integer type,String startTime,String endTime);
	
	/**
	 * 查询订单列表
	 * @param srId
	 * @param doctorId
	 * @return
	 */
	public abstract List<Map<String,Object>> queryOrders(String orderNo,String srId,String doctorId,String mainOrderNo);

	/**
	 * 查询订单列表
	 * @param srId
	 * @param doctorId
	 * @return
	 */
	public abstract List<Map<String,Object>> querySalesOrders(String orderNo,String srId,String doctorId,String mainOrderNo,Integer page,Integer row,Integer type,String startTime,String endTime);
	/**
	 * 订单列表查询我的医生列表
	 * @param srId
	 * @param page
	 * @param row
	 * @return
	 */
	public abstract List<Map<String,Object>> queryOrderDoctors(String srId,Integer page,Integer row);

	/**
	 * 根据助理ID
	 * @param srId
	 * @param doctorId
	 * @return
	 */
	public abstract Map<String,Object> queryPhoneById(String srId,String doctorId);
	
	/**
	 * 修改助理是否已下载app
	 * @param doctorId
	 * @return
	 */
	public abstract Integer updateSaleDownloadState(String srId);
	
	/**
	 * 查询医生主页统计
	 * @param doctorId
	 * @return
	 */
	public abstract Map<String,Object> queryDoctorCenvs(String doctorId);
	
	/**
	 * 查询助理安全设置
	 * @param id
	 * @return
	 */
	public abstract Map<String,Object> querySalesSetByid(String id);
	
}
