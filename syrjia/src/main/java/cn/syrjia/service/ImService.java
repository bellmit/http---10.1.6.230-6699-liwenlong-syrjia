package cn.syrjia.service;


import cn.syrjia.common.BaseServiceInterface;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * service接口
 *
 * @pdOid 44bfffdf-f890-423b-876e-c5b217f57951
 */
public interface ImService extends BaseServiceInterface {

    /**
     * 查询患者列表
     *
     * @param request
     * @param doctorId
     * @return
     */
    Map<String, Object> queryPatient(String doctorId, Integer pageNo);

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
    Map<String, Object> queryIms(String date, String doctorId, String patientId, Integer page, Integer row);

    /**
     * 根据日期查询聊天记录
     *
     * @param request
     * @param doctorId
     * @param patientId
     * @return
     */
    Map<String, Object> queryImsDate(String doctorId, String patientId);

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
    Map<String, Object> queryHistoryIms(String doctorId, String patientId, Integer page, Integer row);

    /**
     * 根据医生id查询医生信息
     *
     * @param request
     * @param doctorId
     * @return
     */
    Map<String, Object> queryDoctorById(String doctorId);

    /**
     * 根据患者id查询患者信息
     *
     * @param request
     * @param patientId
     * @return
     */
    Map<String, Object> queryPatientById(String patientId);

    /**
     * 根据mediaId下载音频信息
     *
     * @param request
     * @param mediaId
     * @return
     */
    Map<String, Object> queryMp3(HttpServletRequest request, String mediaId);

    /**
     * 根据信息id查询音频播放路径
     *
     * @param request
     * @param id
     * @return
     */
    Map<String, Object> queryMp3BySqe(String id);

    /**
     * 根据订单号查询症状描述
     *
     * @param request
     * @param orderNo
     * @return
     */
    Map<String, Object> querySymptom(String orderNo);

    /**
     * 获取最后一次订单信息
     *
     * @param request
     * @param patientId
     * @param doctorId
     * @param testId
     * @return
     */
    Map<String, Object> queryLastOrderNo(String patientId, String doctorId, String testId);

    /**
     * 查询历史问诊单/复诊单
     *
     * @param request
     * @param testId
     * @return
     */
    Map<String, Object> querySpecialTestDetailHistory(String testId);

    /**
     * 赠送次数
     *
     * @param request
     * @param patientId
     * @param doctorId
     * @param count
     * @return
     */
    Map<String, Object> addSyZxCount(String patientId, String doctorId, Integer count);

    /**
     * 消息撤回
     *
     * @param request
     * @param toAccount
     * @param fromAccount
     * @param msgId
     * @return
     */
    Map<String, Object> withdrawIm(HttpServletRequest request, String toAccount, String fromAccount, String msgId);

    /**
     * 删除消息
     *
     * @param request
     * @param msgId
     * @param doctorId
     * @return
     */
    Map<String, Object> deleteMsg(String msgId, String doctorId);

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
    Map<String, Object> forwardingMsg(HttpServletRequest request, String msgId, String patientId, String fromAccount, String toAccount);

    /**
     * 删除患者
     *
     * @param request
     * @param doctorId
     * @param patientId
     * @return
     */
    Map<String, Object> deletePatient(HttpServletRequest request, String deleteId, String beDeleted);

    /**
     * 删除医生
     *
     * @param request
     * @param doctorId
     * @param srId
     * @return
     */
    Map<String, Object> deleteDoctor(HttpServletRequest request, String deleteId, String beDeleted);

    /**
     * 发送纯文字消息
     *
     * @param request
     * @param fromAccount
     * @param toAccount
     * @param data
     * @return
     */
    Map<String, Object> sendMsg(HttpServletRequest request, String fromAccount, String toAccount, String data);

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
    Map<String, Object> addSpecialTestHistory(HttpServletRequest request, String test, String otherUrl, String tongueUrl, String surfaceUrl);

    /**
     * 生成签名 腾讯im登录时用
     *
     * @param request
     * @param identifier
     * @return
     */
    public abstract Map<String, Object> queryImSig(HttpServletRequest request, String identifier);
}