package cn.syrjia.controller;

import cn.syrjia.config.Config;
import cn.syrjia.config.configCode;
import cn.syrjia.service.ImService;
import cn.syrjia.util.Mpeg4ToMp3;
import cn.syrjia.util.Util;
import cn.syrjia.util.WxScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * im控制器
 *
 * @pdOid 63136139-b477-4074-a29b-efca813f1e9d
 */
@Controller
@RequestMapping("/im")
public class ImController {

    @Resource(name = "imService")
    ImService imService;
    @Resource(name = "config")
    Config config;

    /**
     * 生成签名 转换MP3
     *
     * @param url 需要转换的URL
     * @return
     */
    @RequestMapping("/changeToMp3")
    @ResponseBody
    public Map<String, Object> changeToMp3(String url, String fileName) {
        return Mpeg4ToMp3.changeToMp3(url, fileName, config.getFileService());
    }

    /**
     * 生成签名 腾讯im登录时用
     *
     * @param request
     * @param identifier
     * @return
     */
    @RequestMapping("/querySig")
    @ResponseBody
    public Map<String, Object> querySig(HttpServletRequest request, String identifier) {
		/*Map<String,Object> sig=GetSig.getSig(request, identifier);
		if(null==sig){
			return Util.resultMap(configCode.code_1075, sig);
		}
		return Util.resultMap(configCode.code_1001, sig);*/
        return imService.queryImSig(request, identifier);
    }

    /**
     * 查询患者列表
     *
     * @param request
     * @param doctorId
     * @return
     */
    @RequestMapping("/queryPatients")
    @ResponseBody
    public Map<String, Object> queryPatients(HttpServletRequest request, String doctorId,
                                             @RequestParam(defaultValue = "1") Integer pageNo) {
        return imService.queryPatient(doctorId, pageNo);
    }

    /**
     * 查询历史聊天记录
     *
     * @param request
     * @param doctorId
     * @param patientId
     * @param page
     * @param row
     * @return
     */
    @RequestMapping("/queryHistoryIms")
    @ResponseBody
    public Map<String, Object> queryHistoryIms(HttpServletRequest request, String doctorId, String patientId, Integer page, Integer row) {
        return imService.queryHistoryIms(doctorId, patientId, page, row);
    }

    /**
     * 查询聊天记录
     *
     * @param request
     * @param date
     * @param doctorId
     * @param patientId
     * @param page
     * @param row
     * @return
     */
    @RequestMapping("/queryIms")
    @ResponseBody
    public Map<String, Object> queryIms(HttpServletRequest request, String date, String doctorId, String patientId, Integer page, Integer row) {
        return imService.queryIms(date, doctorId, patientId, page, row);
    }

    /**
     * 根据日期查询聊天记录
     *
     * @param request
     * @param doctorId
     * @param patientId
     * @return
     */
    @RequestMapping("/queryImsDate")
    @ResponseBody
    public Map<String, Object> queryImsDate(HttpServletRequest request, String doctorId, String patientId) {
        return imService.queryImsDate(doctorId, patientId);
    }

    /**
     * 根据医生id查询医生信息
     *
     * @param request
     * @param doctorId
     * @return
     */
    @RequestMapping("/queryDoctorById")
    @ResponseBody
    public Map<String, Object> queryDoctorById(HttpServletRequest request, String doctorId) {
        return imService.queryDoctorById(doctorId);
    }

    /**
     * 根据患者id查询患者信息
     *
     * @param request
     * @param patientId
     * @return
     */
    @RequestMapping("/queryPatientById")
    @ResponseBody
    public Map<String, Object> queryPatientById(HttpServletRequest request, String patientId) {
        return imService.queryPatientById(patientId);
    }

    /**
     * 调取微信分享时加密
     *
     * @param request
     * @param url
     * @return
     */
    @RequestMapping("/queryScan")
    @ResponseBody
    public Map<String, Object> queryScan(HttpServletRequest request, String url) {
        Map<String, Object> map = WxScan.getSign(url, imService);
        return Util.resultMap(configCode.code_1001, map);
    }

    /**
     * 根据mediaId下载音频信息
     *
     * @param request
     * @param mediaId
     * @return
     */
    @RequestMapping("/queryMp3")
    @ResponseBody
    public Map<String, Object> queryMp3(HttpServletRequest request, String mediaId) {
        return imService.queryMp3(request, mediaId);
    }

    /**
     * 根据信息id查询音频播放路径
     *
     * @param request
     * @param id
     * @return
     */
    @RequestMapping("/queryMp3BySqe")
    @ResponseBody
    public Map<String, Object> queryMp3BySqe(HttpServletRequest request, String id) {
        return imService.queryMp3BySqe(id);
    }

    /**
     * 根据订单号查询症状描述
     *
     * @param request
     * @param orderNo
     * @return
     */
    @RequestMapping("/querySymptom")
    @ResponseBody
    public Map<String, Object> querySymptom(HttpServletRequest request, String orderNo) {
        return imService.querySymptom(orderNo);
    }

    /**
     * 发送历史问诊单/复诊单
     *
     * @param request
     * @param test
     * @param tongueUrl
     * @param surfaceUrl
     * @param otherUrl
     * @param otherFile
     * @param tongueFile
     * @param surfaceFile
     * @return
     */
    @RequestMapping("/addSpecialTestHistory")
    @ResponseBody
    public Map<String, Object> addSpecialTestHistory(HttpServletRequest request, String test, String tongueUrl, String surfaceUrl, String otherUrl, @RequestParam(required = false) MultipartFile[] otherFile, @RequestParam(required = false) MultipartFile[] tongueFile, @RequestParam(required = false) MultipartFile[] surfaceFile) {
        return imService.addSpecialTestHistory(request, test, otherUrl, tongueUrl, surfaceUrl);
    }

    /**
     * 发送纯文字消息
     *
     * @param request
     * @param fromAccount
     * @param toAccount
     * @param data
     * @return
     */
    @RequestMapping("/sendMsg")
    @ResponseBody
    public Map<String, Object> sendMsg(HttpServletRequest request, String fromAccount, String toAccount, String data) {
        return imService.sendMsg(request, fromAccount, toAccount, data);
    }

    /**
     * 获取最后一次订单信息
     *
     * @param request
     * @param patientId
     * @param doctorId
     * @param testId
     * @return
     */
    @RequestMapping("/queryLastOrderNo")
    @ResponseBody
    public Map<String, Object> queryLastOrderNo(HttpServletRequest request, String patientId, String doctorId, String testId) {
        return imService.queryLastOrderNo(patientId, doctorId, testId);
    }

    /**
     * 查询历史问诊单/复诊单
     *
     * @param request
     * @param testId
     * @return
     */
    @RequestMapping("/querySpecialTestDetailHistory")
    @ResponseBody
    public Map<String, Object> querySpecialTestDetailHistory(HttpServletRequest request, String testId) {
        return imService.querySpecialTestDetailHistory(testId);
    }

    /**
     * 赠送次数
     *
     * @param request
     * @param patientId
     * @param doctorId
     * @param count
     * @return
     */
    @RequestMapping("/addSyZxCount")
    @ResponseBody
    public Map<String, Object> addSyZxCount(HttpServletRequest request, String patientId, String doctorId, Integer count) {
        return imService.addSyZxCount(patientId, doctorId, count);
    }

    /**
     * 消息撤回
     *
     * @param request
     * @param toAccount
     * @param fromAccount
     * @param msgId
     * @return
     */
    @RequestMapping("/withdrawIm")
    @ResponseBody
    public Map<String, Object> withdrawIm(HttpServletRequest request, String toAccount, String fromAccount, String msgId) {
        return imService.withdrawIm(request, toAccount, fromAccount, msgId);
    }

    /**
     * 删除消息
     *
     * @param request
     * @param msgId
     * @param doctorId
     * @return
     */
    @RequestMapping("/deleteMsg")
    @ResponseBody
    public Map<String, Object> deleteMsg(HttpServletRequest request, String msgId, String doctorId) {
        return imService.deleteMsg(msgId, doctorId);
    }

    /**
     * 转发消息
     *
     * @param request
     * @param msgId
     * @param patientId
     * @param fromAccount
     * @param toAccount
     * @return
     */
    @RequestMapping("/forwardingMsg")
    @ResponseBody
    public Map<String, Object> forwardingMsg(HttpServletRequest request, String msgId, String patientId, String fromAccount, String toAccount) {
        return imService.forwardingMsg(request, msgId, patientId, fromAccount, toAccount);
    }

    /**
     * 删除患者
     *
     * @param request
     * @param doctorId
     * @param patientId
     * @return
     */
    @RequestMapping("/deletePatient")
    @ResponseBody
    public Map<String, Object> deletePatient(HttpServletRequest request, String doctorId, String patientId) {
        return imService.deletePatient(request, doctorId, patientId);
    }

    /**
     * 删除医生
     *
     * @param request
     * @param doctorId
     * @param srId
     * @return
     */
    @RequestMapping("/deleteDoctor")
    @ResponseBody
    public Map<String, Object> deleteDoctor(HttpServletRequest request, String doctorId, String srId) {
        return imService.deleteDoctor(request, srId, doctorId);
    }
}