package cn.syrjia.service.impl;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.syrjia.common.impl.BaseServiceImpl;
import cn.syrjia.dao.AnswerDao;
import cn.syrjia.entity.Answer;
import cn.syrjia.service.AnswerService;

/** 疑难答疑service实现
 * 
 * @pdOid 86eb07f9-104c-4114-b5d9-5d8b4f804f6e */
@Service("answerService")
public class AnswerServiceImpl extends BaseServiceImpl implements AnswerService {
   /** @pdOid b5c5df3b-7979-4835-94ab-fef0ff008142 */
   @Resource(name="answerDao")
   public AnswerDao answerDao;
   
   /** 查询疑难答疑列表
    * 
    * @param answer 疑难答疑实体
    * @param row 行数
    * @param page 页数
    * @pdOid c443b07e-24da-4528-a7c5-0b8b59978fdd */
   public List<Map<String,Object>> queryAnswerList(String answerType, Integer row, Integer page) {
      return answerDao.queryAnswerList(answerType, row, page);
   }
   
   /** 根据疑难答疑Id查询
    * 
    * @param answerId 疑难答疑Id
    * @pdOid b5f43eef-0eb9-4e59-9ceb-1ab366abf94a */
   public Answer queryAnswerById(String answerId) {
      return answerDao.queryById(Answer.class,answerId);
   }
   
   	/**
   	 * 疑难答疑类型列表
   	 */
	@Override
	public List<Map<String, Object>> queryAnswerTypeList(Integer answerClass) {
		List<Map<String, Object>> list=answerDao.queryAnswerTypeList(answerClass);
		for(Map<String, Object> map:list){
			List<Map<String,Object>> answers=answerDao.queryAnswerList(map.get("id").toString(),null,null);
			map.put("answers", answers);
		}
		return list;
	}

}