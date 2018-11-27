package cn.syrjia.sales.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.syrjia.config.configCode;
import cn.syrjia.entity.Channel;
import cn.syrjia.entity.LoginLog;
import cn.syrjia.sales.entity.FeedBack;
import cn.syrjia.sales.entity.SalesRepresent;
import cn.syrjia.sales.entity.SalesSet;
import cn.syrjia.sales.service.SalesService;
import cn.syrjia.util.HttpReuqest;
import cn.syrjia.util.StringUtil;
import cn.syrjia.util.Util;

@Controller
@RequestMapping("appSales")
public class SalesController {

	@Resource(name = "salesService")
	SalesService salesService;

	/**
	 * 修改助理手机号
	 * 
	 * @param request
	 * @param phone
	 * @param code
	 * @param srId
	 * @return
	 */
	@RequestMapping("/updateSalesPhone")
	@ResponseBody
	public Map<String, Object> updateSalesPhone(HttpServletRequest request,
			String phone, String code, String srId) {
		return salesService.updateSalesPhone(request, phone, code, srId);
	}

	/**
	 * 退出登录
	 * 
	 * @param oldToken
	 * @return
	 */
	@RequestMapping("/loginOut")
	@ResponseBody
	public Map<String, Object> loginOut(String oldToken) {
		return salesService.loginOut(oldToken);
	}

	/**
	 * 编辑安全设置信息
	 * 
	 * @param salesSet
	 * @return
	 */
	@RequestMapping("/editSalesSet")
	@ResponseBody
	public Map<String, Object> editSalesSet(SalesSet salesSet) {
		return salesService.editSalesSet(salesSet);
	}

	/**
	 * 验证手势密码、数字密码是否正确
	 * 
	 * @param salesSet
	 * @return
	 */
	@RequestMapping("/checkSalesPassword")
	@ResponseBody
	public Map<String, Object> checkSalesPassword(SalesSet salesSet) {
		return salesService.checkSalesPassword(salesSet);
	}

	/**
	 * 根据ID获取助理个人信息
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryOneSales")
	@ResponseBody
	public Map<String, Object> queryOneSales(String srId) {
		return salesService.queryOneSales(srId);
	}

	/**
	 * 修改助理信息
	 * 
	 * @param sales
	 * @return
	 */
	@RequestMapping("/updateSales")
	@ResponseBody
	public Map<String, Object> updateSales(SalesRepresent sales) {
		return salesService.updateSales(sales);
	}

	/**
	 * 获取手机验证码
	 * 
	 * @param request
	 * @param doctorId
	 * @param phone
	 * @param type
	 * @return
	 */
	@RequestMapping("/getPhoneCode")
	@ResponseBody
	public Map<String, Object> getPhoneCode(HttpServletRequest request,
			String srId, String phone, Integer type) {
		return salesService.getPhoneCode(request, srId, phone, type);
	}

	/**
	 * 助理端APP登录接口
	 * 
	 * @param request
	 * @param loginName
	 * @param code
	 * @param loginLog
	 * @return
	 */
	@RequestMapping("/login")
	@ResponseBody
	public Map<String, Object> login(HttpServletRequest request,
			String loginName, String password, LoginLog loginLog) {
		return salesService.login(request, loginName, password, loginLog);
	}

	/**
	 * 验证老手机号、验证码是否正确
	 * 
	 * @param phone
	 * @param doctorId
	 * @param code
	 * @return
	 */
	@RequestMapping("/validateCode")
	@ResponseBody
	public Map<String, Object> validateCode(String phone, String srId,
			String code) {
		return salesService.validateOldPhoneAndCode(phone, srId, code);
	}

	/**
	 * 根据医生ID获取医生详情
	 * 
	 * @param doctorId
	 * @return
	 */
	@RequestMapping("/queryDoctorById")
	@ResponseBody
	public Map<String, Object> queryDoctorById(String doctorId) {
		return salesService.queryDoctorById(doctorId);
	}

	/**
	 * 根据医生ID获取医生详情下方展示内容
	 * 
	 * @param doctorId
	 * @return
	 */
	@RequestMapping("/queryDoctorOtherById")
	@ResponseBody
	public Map<String, Object> queryDoctorOtherById(String doctorId) {
		return salesService.queryDoctorOtherById(doctorId);
	}

	/**
	 * 根据医生ID查询医生文章信息
	 * 
	 * @param doctorId
	 * @return
	 */
	@RequestMapping("/queryDoctorArticleList")
	@ResponseBody
	public Map<String, Object> queryDoctorArticleList(String srId,
			String doctorId, Integer row, Integer page) {
		return salesService.queryDoctorArticleList(srId, doctorId, row, page);
	}

	/**
	 * 根据医生ID查询医生评价信息（详情页面与评价列表共用）
	 * 
	 * @param doctorId
	 * @return
	 */
	@RequestMapping("/queryDoctorEvaList")
	@ResponseBody
	public Map<String, Object> queryDoctorEvaList(String doctorId, Integer row,
			Integer page, String _sign) {
		return salesService.queryDoctorEvaList(doctorId, row, page, _sign);
	}

	/**
	 * 助理查询我的医生列表
	 * 
	 * @param srId
	 * @param type
	 *            默认医生注册的时间倒序 0-按照日接诊量降序排序 1-按照日成单额降序排序 2-按照总接诊量降序排序
	 *            3-按照总成单额降序排序 4-导入患者数量倒序排列 5-未认证 6- 医院简拼升序排列
	 * @param page
	 * @param row
	 * @param searchKey
	 * @return
	 */
	@RequestMapping("/queryMyDoctors")
	@ResponseBody
	public Map<String, Object> queryMyDoctors(String srId, Integer type,
			Integer page, Integer row, String name) {
		return salesService.queryMyDoctors(srId, type, page, row, name);
	}

	/**
	 * 上传评价图片
	 * 
	 * @param multipartFile
	 * @param request
	 * @param doctorId
	 * @return
	 */
	@RequestMapping("/uploadImgs")
	@ResponseBody
	public Map<String, Object> uploadImgs(
			HttpServletRequest request,
			@RequestParam(value = "multipartFiles") MultipartFile multipartFiles,
			SalesRepresent sales, String dirName) {
		//上传图片
		Map<String, String> imageMap = Util.uploadFoodImage(multipartFiles,
				request, dirName);
		if (imageMap == null) {
			return Util.resultMap(configCode.code_1048, null);
		}
		if (!StringUtil.isEmpty(dirName)) {
			if ("salesUrl".equals(dirName)) {
				if (StringUtil.isEmpty(sales.getSrId())) {
					return Util.resultMap(configCode.code_1029, "");
				} else {
					//通过id查询助理
					Map<String,Object> oldsales = salesService.querySalesOne(sales.getSrId());
					sales.setImgUrl(imageMap.get("url").toString());
					sales.setLocalImgUrl(imageMap.get("riskPath").toString());
					Map<String, Object> map = HttpReuqest.httpPostCoutosQrCode(
							sales.getLocalImgUrl(), "sales/" + sales.getSrId(),
							sales.getSrId(), oldsales.get("localQrUrl").toString());
					if (map != null && !StringUtil.isEmpty(map.get("fwPath"))) {
						sales.setQrCodeUrl(map.get("fwPath").toString());
						sales.setLocalQrUrl(map.get("localPath").toString());
						//根据id查询助理
						Channel channel = salesService.queryById(Channel.class,
								sales.getSrId());
						if (channel != null) {
							channel.setQrUrl(map.get("fwPath").toString());
							channel.setLocalQrUrl(map.get("localPath")
									.toString());
							//修改必须有ID 根据ID
							salesService.updateEntity(channel);
						}
					}
					//修改助理信息
					return salesService.updateSales(sales);
				}
			} else if ("applyDoctor".equals(dirName)) {
				return Util
						.resultMap(configCode.code_1001, imageMap.get("url"));
			}
		}
		return Util.resultMap(configCode.code_1048, null);
	}

	/**
	 * 获取助理安全设置
	 * 
	 * @param srId
	 * @return
	 */
	@RequestMapping("/querySalesSetById")
	@ResponseBody
	public Map<String, Object> querySalesSetById(String srId) {
		return salesService.querySalesSetById(srId);
	}

	/**
	 * 修改密码接口
	 * 
	 * @param srId
	 * @return
	 */
	@RequestMapping("/updatePassword")
	@ResponseBody
	public Map<String, Object> updatePassword(String srId, String phone,
			String oldPassword, String newPassrod, String code) {
		return salesService.updatePassword(srId, phone, oldPassword,
				newPassrod, code);
	}

	/**
	 * 根据助理ID查询医生（首页）
	 * 
	 * @param srId
	 * @return
	 */
	@RequestMapping("/queryDoctors")
	@ResponseBody
	public Map<String, Object> queryDoctors(String srId) {
		return salesService.queryDoctors(srId);
	}

	/**
	 * 查询一周日期
	 * 
	 * @return
	 */
	@RequestMapping("/queryWeekData")
	@ResponseBody
	public Map<String, Object> queryWeekData() {
		return salesService.queryWeekData();
	}

	/**
	 * 根据医生ID查询医生14天出诊状态
	 * 
	 * @param doctorId
	 * @return
	 */
	@RequestMapping("/queryFourTeenZzStatus")
	@ResponseBody
	public Map<String, Object> queryFourTeenZzStatus(String doctorId) {
		return salesService.queryFourTeenZzStatus(doctorId);
	}

	/**
	 * 查询反馈部门列表
	 * 
	 * @return
	 */
	@RequestMapping("/queryFeedDeparts")
	@ResponseBody
	public Map<String, Object> queryFeedDeparts() {
		return salesService.queryFeedDeparts();
	}

	/**
	 * 新增反馈信息
	 * 
	 * @param feedBack
	 * @return
	 */
	@RequestMapping("/addFeedBack")
	@ResponseBody
	public Map<String, Object> addFeedBack(FeedBack feedBack) {
		return salesService.addFeedBack(feedBack);
	}

	/**
	 * 助理端查询订单列表
	 * 
	 * @param srId
	 * @param doctorId
	 * @param page
	 * @param row
	 * @param type
	 * @return
	 */
	@RequestMapping("/queryOrders")
	@ResponseBody
	public Map<String, Object> queryOrders(String srId, String doctorId,
			Integer page, Integer row, Integer type, String startTime,
			String endTime) {
		return salesService.queryOrders(srId, doctorId, page, row, type,
				startTime, endTime);
	}
	
	/**
	 * 助理端查询订单列表(新)
	 * 
	 * @param srId
	 * @param doctorId
	 * @param page
	 * @param row
	 * @param type
	 * @return
	 */
	@RequestMapping("/querySalesOrders")
	@ResponseBody
	public Map<String, Object> querySalesOrders(String srId, String doctorId,
			Integer page, Integer row, Integer type, String startTime,
			String endTime) {
		return salesService.querySalesOrders(srId, doctorId, page, row, type,
				startTime, endTime);
	}

	/**
	 * 订单列表查询我的医生列表
	 * 
	 * @param srId
	 * @param page
	 * @param row
	 * @return
	 */
	@RequestMapping("/queryOrderDoctors")
	@ResponseBody
	public Map<String, Object> queryOrderDoctors(String srId, Integer page,
			Integer row) {
		return salesService.queryOrderDoctors(srId, page, row);
	}

	/**
	 * 不涉及订单拨打电话
	 * 
	 * @param srId
	 * @param doctorId
	 * @return
	 */
	@RequestMapping("/salesCallNoOrder")
	@ResponseBody
	public Map<String, Object> salesCallNoOrder(String srId, String doctorId) {
		return salesService.salesCallNoOrder(srId, doctorId);
	}

}
