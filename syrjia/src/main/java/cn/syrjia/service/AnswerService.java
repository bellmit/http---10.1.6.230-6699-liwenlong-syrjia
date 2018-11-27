package cn.syrjia.service;


import java.util.List;
import java.util.Map;

import cn.syrjia.common.BaseServiceInterface;
import cn.syrjia.entity.Answer;

/** 疑难答疑service接口
 * 
 * @pdOid 44bfffdf-f890-423b-876e-c5b217f57951 */
public interface AnswerService extends BaseServiceInterface {
   /** 查询疑难答疑列表
    * 
    * @param answer 疑难答疑实体
    * @param row 行数
    * @param page 页数
    * @pdOid 1c4ce06e-ea1d-463b-bf23-863254921749 */
	List<Map<String,Object>> queryAnswerList(String answerType, Integer row, Integer page);
	
	/**
	 * 查询答案类型列表
	 * @param answerClass
	 * @return
	 */
	List<Map<String,Object>> queryAnswerTypeList(Integer answerClass);
	
   /** 根据疑难答疑Id查询
    * 
    * @param answerId 疑难答疑Id
    * @pdOid 602e28f2-fb61-4644-8e4b-02e47235ff82 */
   Answer queryAnswerById(String answerId);

}