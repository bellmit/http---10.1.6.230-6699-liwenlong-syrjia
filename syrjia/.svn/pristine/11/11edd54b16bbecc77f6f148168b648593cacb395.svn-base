package cn.syrjia.hospital.dao;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import cn.syrjia.common.BaseDaoInterface;
import cn.syrjia.hospital.entity.MyEvaBanner;

public interface DoctorOrderDao extends BaseDaoInterface {
	
	/**
	 * 获取订单号
	 * @return
	 */
	public abstract String orderNo();
	
	/**
	 * 查询用户购买服务确认页面信息
	 * @param orderType
	 * @param doctorId
	 * @param memberId
	 * @return
	 */
	public abstract Map<String,Object> queryConfirmDocServerData(Integer orderType,String doctorId,String memberId);

	/**
	 * 根据服务类型、就诊人ID查询是否存在未完成订单
	 * @param patientId
	 * @param orderType
	 * @return
	 */
	public abstract Integer checkNoFinishOrderByPatientId(String patientId,String doctorId,Integer orderType);
	
	/**
	 * 根据订单号查询症状描述
	 * @param request
	 * @param orderNo
	 * @return
	 */
	public abstract Map<String,Object> queryOrderSymptomByOrderNo(HttpServletRequest request,String orderNo);

	/**
	 * 查询同一用户是否有未处理拍方抓药
	 * @param memberId
	 * @return
	 */
	public abstract Integer checkPhotoMedicalCount(String memberId);
	
	/**
	 * 评价页面获取信息
	 * @param orderNo
	 * @param memberId
	 * @return
	 */
	public abstract Map<String,Object> queryDoctorOrderDetail(String orderNo,String memberId);
	
	/**
	 * 查询我的锦旗列表
	 * @param memberId
	 * @return
	 */
	public abstract List<Map<String, Object>> queryMyEvaBanners(String memberId);
	
	/**
	 * 根据锦旗ID查询锦旗信息
	 * @param id
	 * @return
	 */
	public abstract Map<String,Object> queryEvaBannerById(String id);
	
	/**
	 * 批量增加我的锦旗
	 * @param myEvaBanner
	 * @param list
	 * @return
	 */
	public abstract Object insertMyEvaBanners(MyEvaBanner myEvaBanner,List<Map<String,Object>> list);
	
	/**
	 * 根据订单号查询已付款购买锦旗列表
	 * @param orderNo
	 * @return
	 */
	public abstract List<Map<String,Object>> queryEvaBannerByOrderNo(String orderNo);
	
	/**
	 * 修改锦旗状态
	 * @param evaId
	 * @param bannerId
	 * @return
	 */
	public abstract Integer updateMyEvaBanState(String memberId,String evaId,String bannerId,Integer count);
	
	/**
	 * 通过订单号查询评论
	 * @param orderNo
	 * @return
	 */
	public abstract Integer queryEvaByOrderNo(String orderNo);
	
	/**
	 * 批量添加评价标签
	 * @param list
	 * @param evaId
	 * @return
	 */
	public abstract Object insertEvaLables(JSONArray checkLabels,String evaId);

	/**
	 * 查询最新调查
	 * @param memberId
	 * @param page
	 * @param row
	 * @return
	 */
	public abstract List<Map<String,Object>> queryNowInquiry(String memberId,
			Integer page, Integer row);
	
	/**
	 *查询医生每天已接单数量
	 * @param doctorId
	 * @return
	 */
	public abstract Map<String,Object> getDoctorOrderDayCount(String doctorId,Integer orderType);

	/**
	 * 查询患者是否有未完成抄方订单
	 * @param doctorId
	 * @param patientId
	 * @return
	 */
	public abstract Integer queryCfOrderByIds(String doctorId,String patientId);
	/**
	 * 新增抄方订单
	 * @param doctorId
	 * @param patientId
	 * @return
	 */
	public abstract Integer addCopySquare(String wzOrderNo, String img1, String img2, String img3, String snote, int agentType,
			int anagraphCount,String patientId);
	/**
	 * 查询抄方申请
	 * @param doctorId
	 * @param patientId
	 * @return
	 */
	public abstract	Map<String, Object> lookCfOrder(String orderId);
	
	/**
	 * 查询是否添加症状描述
	 * @param orderNo
	 * @return
	 */
	public abstract Integer querySypCountByOrderNo(String orderNo);
	
	/**
	 * 提交评价锦旗订单与医生挂钩
	 * @param orderNos
	 * @param doctorId
	 * @return
	 */
	public abstract Integer updateJqOrderDoctorId(List<Map<String, Object>> orderNos,String doctorId);
	/**
	 * 提交评价锦旗订单与医生挂钩
	 * @param orderNos
	 * @param doctorId
	 * @return
	 */
	public abstract Integer updateJqOrderDoctorIdPatientId(List<Map<String, Object>> orderNos,String doctorId, String patientId);




}
