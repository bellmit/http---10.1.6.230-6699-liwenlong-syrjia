package cn.syrjia.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import cn.syrjia.common.BaseServiceInterface;
import cn.syrjia.entity.Evaluate;

public interface EvaluateService extends BaseServiceInterface{

	/**
	 * 查询评价列表
	 * @param request
	 * @param evaluate
	 * @param memberId
	 * @param page
	 * @param row
	 * @param level
	 * @return
	 */
	abstract Map<String,Object> queryEvaluateList(Evaluate evaluate,Integer level,Integer page,Integer row);
	
	/**
	 * 查询好评率
	 * @param evaluate
	 * @param evaluateLevel
	 * @return
	 */
	abstract Map<String,Object> queryEvaluateRate(Evaluate evaluate,Integer evaluateLevel);
	
	/**
	 * 查询差评 中评 好评数
	 * @param evaluate
	 * @return
	 */
	abstract Map<String,Object> queryEvaluateNum(Evaluate evaluate);
	
	/**
	 * 新增评价
	 * 
	 * @param eva
	 * @return
	 */
	abstract Map<String,Object> addEvaluate(List<Map<String,Object>> list,String orderNo,String memberId);
	
	/**
	 * 查询评价
	 * @param type
	 * @return
	 */
	abstract Map<String,Object> queryEvalabels(Integer type);

	/**
	 * 新增评论
	 * @param evaluate
	 * @return
	 */
	public abstract Object addEvaluate(HttpServletRequest request,List<Evaluate> evaluate,String picServerIds);
	
	/**
	 * 修改评论
	 * @param evaluate
	 * @return
	 */
	public abstract Object updateEvaluate(Evaluate evaluate,String state);
	
	/**
	 * 更新评价图片
	 * @param evaluateId
	 * @param picIds
	 * @return
	 */
	public abstract Integer updateEvaluatePic(String evaluateId,String[] picIds);
	
	/**
	 * 上传图片
	 * @param multipartFile
	 * @param request
	 * @param id
	 * @param dirName
	 * @return
	 */
	public abstract List<Map<String,Object>> uploadEvaluatePic(MultipartFile[] multipartFile, HttpServletRequest request,
			String id,String orderNo);
	/**
	 * 新增评论
	 * @Description: TODO
	 * @param @param request
	 * @param @param evaluate
	 * @param @param picServerIds
	 * @param @return   
	 * @return Object  
	 * @throws
	 * @date 2017-12-16
	 */
	public abstract Object addEva(HttpServletRequest request,Evaluate evaluate);

}
