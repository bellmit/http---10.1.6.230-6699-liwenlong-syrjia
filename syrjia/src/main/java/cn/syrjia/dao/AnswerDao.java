package cn.syrjia.dao;

import java.util.List;
import java.util.Map;

import cn.syrjia.common.BaseDaoInterface;
import cn.syrjia.entity.Answer;

/***********************************************************************

/** 疑难答疑dao接口
 * 
 * @pdOid fa3e36a4-983f-4ae8-bd70-778c0badaac6 */
public interface AnswerDao extends BaseDaoInterface {
	
	/**
	 * 查询答案列表
	 * @param answerType
	 * @param row
	 * @param page
	 * @return
	 */
	List<Map<String,Object>> queryAnswerList(String answerType, Integer row, Integer page);
	
	/**
	 * 查询答案类型列表
	 * @param answerClass
	 * @return
	 */
	List<Map<String,Object>> queryAnswerTypeList(Integer answerClass);
}