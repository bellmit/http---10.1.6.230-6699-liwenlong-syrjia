package cn.syrjia.dao;

import java.util.List;
import java.util.Map;

import cn.syrjia.common.BaseDaoInterface;
import cn.syrjia.entity.Evaluate;

public interface EvaluateDao  extends BaseDaoInterface{

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
	abstract List<Map<String, Object>> queryEvaluateList(Evaluate evaluate,Integer level,Integer page,Integer row);
	
	/**
	 * 查询好评率
	 * @param evaluate
	 * @param evaluateLevel
	 * @return
	 */
	abstract String queryEvaluateRate(Evaluate evaluate,Integer evaluateLevel);
	
	/**
	 * 查询差评 中评 好评数
	 * @param evaluate
	 * @return
	 */
	abstract Map<String, Object> queryEvaluateNum(Evaluate evaluate);
	
	/**
	 * 查询订单状态
	 * @param orderNo
	 * @return
	 */
	abstract Map<String, Object> queryOrderState(String orderNo);
	
	/**
	 * 查询是否已评价
	 * @param orderNo
	 * @return
	 */
	abstract Integer queryEvaluateByOrderNo(String orderNo);
	
	
	/**
	 * 添加频率图片
	 * @param evaluateId
	 * @param picIds
	 * @return
	 */
	public abstract Integer updateEvaluatePic(String evaluateId,String[] picIds);
	
	/**
	 * 查询评价标签
	 */
	abstract List<Map<String, Object>> queryEvalabels(Integer type);

	/**
	 * 改為好評
	 * @param eva
	 * @return
	 */
	public abstract Object updateHigh(Evaluate eva);
}
