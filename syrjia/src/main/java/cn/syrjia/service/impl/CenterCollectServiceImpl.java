package cn.syrjia.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import cn.syrjia.common.impl.BaseServiceImpl;
import cn.syrjia.config.configCode;
import cn.syrjia.dao.CenterCollectDao;
import cn.syrjia.dao.OrderDao;
import cn.syrjia.entity.Order;
import cn.syrjia.hospital.entity.UserKeep;
import cn.syrjia.service.CenterCollectService;
import cn.syrjia.util.Util;

@Service("centerCollectService")
public class CenterCollectServiceImpl extends BaseServiceImpl implements
		CenterCollectService {
	
	@Resource(name = "centerCollectDao")
	CenterCollectDao centerCollectDao;
	
	@Resource(name = "orderDao")
	OrderDao orderDao;
	
	/**
	 * 关注的医生
	 */
	@Override
	public List<Map<String, Object>> queryKeepDoctor(String searchSort,
			String memberId, Integer page, Integer row,String openId) {
		return centerCollectDao.queryKeepDoctor(searchSort, memberId, page, row, openId);
	}
	
	/**
	 * 我收藏的文章
	 */
	@Override
	public List<Map<String, Object>> queryKeepKonwledge(String searchSort,
			String memberId, Integer page, Integer row,String openId) {
		//我收藏的文章
		List<Map<String, Object>> liMaps = centerCollectDao.queryKeepKonwledge(searchSort, memberId, page, row, openId);
		if(liMaps!=null&&liMaps.size()>0){
			//遍历liMaps
			for (Map<String, Object> map : liMaps) {
				if(map.get("labelNames")!=null){
					//不为空赋值
					if(map.get("labelNames").toString().indexOf(",")>0){
						String[] labelNameList = map.get("labelNames").toString().split(",");
						map.put("labelNameList", labelNameList);
					}else{
						String[] labelNameList = new String[]{map.get("labelNames").toString()};
						map.put("labelNameList", labelNameList);
					}
				}
			}
		}
		return liMaps;
	}
	
	/**
	 * 收藏文章中关注医生
	 */
	@Override
	public Map<String, Object> addKeepDoc(String memberId, String doctorId) {
		
		//为空判断
		if(!StringUtils.isEmpty(memberId)&&!StringUtils.isEmpty(doctorId)){
			Object result = "";
			UserKeep userKeep = new UserKeep();
			//赋值
			userKeep.setType("3");
			userKeep.setMemberId(memberId);
			userKeep.setGoodsId(doctorId);
			userKeep.setCreateTime((int) (System.currentTimeMillis()/1000));
			//添加
			result =  centerCollectDao.addEntityUUID(userKeep);
			if(result!=null){
				return Util.resultMap(configCode.code_1001, result);
			}else{
				return Util.resultMap(configCode.code_1001, "");
			}
		}else{
			return Util.resultMap(configCode.code_1029, "");
		}
		
	}

	/**
	 * 查询医生的gz
	 */
	@Override
	public List<Map<String, Object>> queryDoctorGZ(String name,String memberId, Integer page, Integer row,String openId) {
		return centerCollectDao.queryDoctorGZ(name,memberId, page, row, openId);
	}
	
	/**
	 * 查询
	 */
	@Override
	public List<Map<String, Object>> queryDoctorJZ(String name,String memberId, Integer page, Integer row,String openId) {
		return centerCollectDao.queryDoctorJZ(name,memberId, page, row, openId);
	}

	/**
	 * 查询收藏的文章
	 */
	@Override
	public List<Map<String, Object>> queryCollectArticle(String name,String memberId,
			Integer page, Integer row) {
		// TODO Auto-generated method stub
		return centerCollectDao.queryCollectArticle(name,memberId, page, row);
	}

	/**
	 * 查询收藏的商品
	 */
	@Override
	public List<Map<String, Object>> queryCollectGoods(String name,
			String memberId, Integer page, Integer row) {
		// TODO Auto-generated method stub
		return centerCollectDao.queryCollectGoods(name,memberId, page, row);
	}
	
	/**
	 * 通过id查询用户
	 */
	@Override
	public Map<String, Object> queryMemberById(String memberId) {
		//创建order对象
		Order order=new Order();
		//赋值
		order.setPaymentStatus(2);
		order.setOrderStatus(120);
		//查询订单数
		Integer dsh=orderDao.queryAllOrderListNum(memberId,order);
		order=new Order();
		order.setPaymentStatus(1);
		//order.setOrderStatus(1);
		//查询订单数
		Integer dfk=orderDao.queryAllOrderListNum(memberId,order);
		order=new Order();
		order.setPaymentStatus(5);
		//order.setOrderStatus(5);
		//查询订单数
		Integer dpj=orderDao.queryAllOrderListNum(memberId,order);
		//通过id查询member
		Map<String,Object> member = centerCollectDao.queryMembersById(memberId);
		Map<String,Object> result=new HashMap<String, Object>();
		//拼接返回结果
		result.put("dsh",dsh);
		result.put("dfk",dfk);
		result.put("dpj",dpj);
		result.put("member",member);
		return result;
	}
}
