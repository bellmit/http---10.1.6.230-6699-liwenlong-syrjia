package cn.syrjia.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.syrjia.config.configCode;
import cn.syrjia.entity.Answer;
import cn.syrjia.service.AnswerService;
import cn.syrjia.util.Util;

/** 疑难答疑控制器
 * 
 * @pdOid 7fe649bf-75b3-4358-92be-808dbe294e27 */
@Controller 
@RequestMapping("/answer")
public class AnswerController {
   /** @pdOid 2fd92c95-6709-4f41-99d4-fb7102685c21 */
   @Resource(name = "answerService")
   private AnswerService answerService;
   
   /** 查询疑难答疑分类列表
    * 
    * @param answer 疑难答疑实体
    * @param row 行数
    * @param page 页数
    * @pdOid 9553094d-ef4d-4dbf-8b99-0c2605b571c1 */
   @RequestMapping("/queryAnswerTypeList")
   @ResponseBody
   public Map<String,Object> queryAnswerTypeList(Integer answerClass) {
	  List<Map<String,Object>> list=answerService.queryAnswerTypeList(answerClass);
      return Util.resultMap(configCode.code_1001,list);//成功
   }
   
   /** 查询疑难答疑列表
    * 
    * @param answer 疑难答疑实体
    * @param row 行数
    * @param page 页数
    * @pdOid 9553094d-ef4d-4dbf-8b99-0c2605b571c1 */
   @RequestMapping("/queryAnswerList")
   @ResponseBody
   public Map<String,Object> queryAnswerList(String answerType, Integer row, Integer page) {
	   List<Map<String,Object>> list=answerService.queryAnswerList(answerType, row, page);
	   return Util.resultMap(configCode.code_1001,list);//成功 
   }
   
   /** 根据疑难答疑Id查询
    * 
    * @param answerId 疑难答疑Id
    * @pdOid 3643b559-4a78-47b4-a0f9-2da3a09f6fb7 */
   public Map<String,Object> queryAnswerById(String answerId) {
	   Answer Answer= answerService.queryById(Answer.class,answerId);
	   return Util.resultMap(configCode.code_1001,Answer);//成功
   }

}