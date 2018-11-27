package cn.syrjia.service;


import java.util.List;
import java.util.Map;

import cn.syrjia.common.BaseServiceInterface;


public interface CenterCollectService extends BaseServiceInterface {
	
	/**
	 * 关注的医生
	 * @Description: TODO
	 * @param @param searchSort
	 * @param @param doc
	 * @param @param page
	 * @param @param row
	 * @param @param openId
	 * @param @return   
	 * @return List<Map<String,Object>>  
	 * @throws
	 * @date 2018-3-26
	 */
	public abstract List<Map<String, Object>> queryKeepDoctor(String searchSort, String memberId,
			Integer page, Integer row,String openId);
	
	/**
	 * 我收藏的文章
	 * @Description: TODO
	 * @param @param searchSort
	 * @param @param doc
	 * @param @param page
	 * @param @param row
	 * @param @param openId
	 * @param @return   
	 * @return List<Map<String,Object>>  
	 * @throws
	 * @date 2018-3-26
	 */
	public abstract List<Map<String, Object>> queryKeepKonwledge(String searchSort, String memberId,
			Integer page, Integer row,String openId);
	
	/**
	 * 收藏文章中关注医生
	 * @Description: TODO
	 * @param @param doctorId
	 * @param @return   
	 * @return Map<String,Object>  
	 * @throws
	 * @date 2018-3-27
	 */
	Map<String, Object> addKeepDoc( String memberId ,String doctorId);

	/**
	 * 查询医生的gz
	 * @param name
	 * @param memberId
	 * @param page
	 * @param row
	 * @param openId
	 * @return
	 */
	List<Map<String, Object>> queryDoctorGZ(String name,String memberId, Integer page,
			Integer row, String openId);

	
	/**
	 * 查询医生的jz
	 * @param name
	 * @param memberId
	 * @param page
	 * @param row
	 * @param openId
	 * @return
	 */
	List<Map<String, Object>> queryDoctorJZ(String name,String memberId, Integer page,
			Integer row, String openId);

	/**
	 * 查询医生的文章
	 * @param name
	 * @param memberId
	 * @param page
	 * @param row
	 * @return
	 */
	List<Map<String, Object>> queryCollectArticle(String name,
			String memberId, Integer page, Integer row);

	/**
	 * 查询收藏的商品
	 * @param name
	 * @param memberId
	 * @param page
	 * @param row
	 * @return
	 */
	List<Map<String, Object>> queryCollectGoods(String name,
			String memberId, Integer page, Integer row);
	
	/**
	 * 通过id查询用户
	 * @param memberId
	 * @return
	 */
	Map<String, Object> queryMemberById(String memberId);

}
