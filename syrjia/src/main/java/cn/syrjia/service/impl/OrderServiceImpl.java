package cn.syrjia.service.impl;

import cn.syrjia.callCenter.util.CallCenterConfig;
import cn.syrjia.callCenter.util.SendCallCenterUtil;
import cn.syrjia.common.impl.BaseServiceImpl;
import cn.syrjia.config.configCode;
import cn.syrjia.dao.OrderDao;
import cn.syrjia.entity.Order;
import cn.syrjia.entity.RefundApplyRecord;
import cn.syrjia.entity.RefundAuditRecord;
import cn.syrjia.entity.SysSet;
import cn.syrjia.hospital.dao.AppDoctorDao;
import cn.syrjia.hospital.entity.MyEvaBanner;
import cn.syrjia.hospital.entity.OrderDetailServer;
import cn.syrjia.service.OrderService;
import cn.syrjia.service.PushService;
import cn.syrjia.util.*;
import cn.syrjia.util.qcloudsms.QCloudSmsUtil;
import cn.syrjia.wxPay.wxPay.util.StringUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("orderService")
public class OrderServiceImpl extends BaseServiceImpl implements OrderService {

    // 日志
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Resource(name = "orderDao")
    private OrderDao orderDao;

    @Resource(name = "appDoctorDao")
    private AppDoctorDao appDoctorDao;

    @Resource(name = "pushService")
    private PushService pushService;

    /**
     * 生成订单号
     *
     * @return 订单号
     */
    @Override
    public String orderNo() {
        return orderDao.orderNo();
    }

    /**
     * 更新订单状态
     *
     * @param request     request
     * @param orderStatus orderStatus
     * @param paystatus   paystatus
     * @param orderNo     orderNo
     * @param ordertype   ordertype
     * @param refundId    refundId
     * @param tradeNo     tradeNo
     * @param orderWay    orderWay
     * @param payWay      payWay
     * @return Integer
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateState(HttpServletRequest request, Integer orderStatus, Integer paystatus, String orderNo,
                               Integer ordertype, Integer refundId, String tradeNo, Integer orderWay, Integer... payWay) {
        Integer i = 0;
        try {
            logger.info("支付回调传回订单号：{}", orderNo);
            if (!StringUtils.isEmpty(orderNo)) {
                Map<String, Object> order = orderDao.queryOrderByOrderNo(orderNo);
                logger.info("订单【{}】状态变更为 {} 第一步调用", orderNo, paystatus);
                if (order != null) {
                    logger.info("获取医生ID开始");
                    String doctorId = StringUtil.isEmpty(order.get("doctorId")) ? null : order.get("doctorId").toString();
                    logger.info("获取医生ID结束：{}", doctorId);
                    logger.info("获取患者ID开始");
                    String patientId = StringUtil.isEmpty(order.get("patientId")) ? null : order.get("patientId").toString();
                    logger.info("获取患者ID结束：{}", patientId);
                    int orderType = Integer.valueOf(order.get("orderType").toString());
                    String followId = "";
                    String yztId = "";
                    if (paystatus == 2) {
                        Integer payway = null;
                        if (payWay != null && payWay.length > 0) {
                            payway = payWay[0];
                        }
                        if (!StringUtil.isEmpty(doctorId)) {
                            logger.info("查询医生导流人开始");
                            Map<String, Object> followMap = orderDao.queryFollowIdByOpenId(doctorId);
                            followId = (followMap == null || StringUtil.isEmpty(followMap.get("followId"))) ?
                                    null : followMap.get("followId").toString();
                           /* yztId = (followMap == null || StringUtil.isEmpty(followMap.get("yztId"))) ?
                                    null : followMap.get("yztId").toString();*/
                            logger.info("查询医生导流人结束，医生导流人ID：{}", followId);
                        }
                        if (ordertype == 12) {
                            logger.info("锦旗订单修改状态开始");
                            i = orderDao.updateOrderStatus(5, 5, tradeNo, payway, orderNo,
                                    null, null, orderWay,yztId);
                            logger.info("锦旗订单修改状态结束，修改结果返回：{}", i);
                        } else {
                            if (ordertype == 10 || ordertype == 13 || ordertype == 14 || ordertype == 15
                                    || ordertype == 16 || ordertype == 17 || ordertype == 18 || ordertype == 19) {
                                logger.info("药品订单修改状态开始");
                                i = orderDao.updateOrderStatus(paystatus, 0, tradeNo, payway, orderNo,
                                        null, followId, orderWay,yztId);
                                logger.info("药品订单修改状态结束，结果为：{}", i);
                            } else {
                                logger.info("服务、商品订单修改状态开始");
                                i = orderDao.updateOrderStatus(paystatus, orderStatus, tradeNo, payway, orderNo,
                                        null, followId, orderWay,yztId);
                                logger.info("服务、商品订单修改状态结束，结果为：{}", i);
                            }
                        }

                        if (i > 0) {
                            String serverOrderNo = null;
                            try {
                                if (ordertype == 10 || ordertype == 13 || ordertype == 14 || ordertype == 15 ||
                                        ordertype == 16 || ordertype == 17 || ordertype == 18 || ordertype == 19 ||
                                        ordertype == 20) {
                                    logger.info("付款成功修改调理记录状态为待审核开始");
                                    orderDao.updateRecordStatus(orderNo);
                                    logger.info("付款成功修改调理记录状态为待审核结束");
                                }
                                logger.info("付款订单类型为：{}", ordertype);
                                if (orderType == 20) {
                                    logger.info("付款订单为主订单，查询子订单数据并修改状态开始");
                                    List<Map<String, Object>> childOrders = orderDao.queryChildOrderNos(orderNo);
                                    logger.info("付款订单为主订单，查询子订单数据数据结果：{}", childOrders);
                                    if (childOrders != null && childOrders.size() > 0) {
                                        for (Map<String, Object> childOrder : childOrders) {
                                            int ordertype1;
                                            if (!StringUtil.isEmpty(childOrder.get("orderType"))) {
                                                ordertype1 = Integer.valueOf(childOrder.get("orderType").toString());
                                                logger.info("子订单订单类型：{}", ordertype1);
                                                logger.info("修改子订单订单状态为 {} 开始", paystatus);
                                                orderDao.updateOrderStatus(paystatus, orderStatus, tradeNo, payway,
                                                        childOrder.get("orderNo").toString(), null,
                                                        followId, orderWay,yztId);
                                                logger.info("修改子订单订单状态为 {} 完成", paystatus);
                                                if (ordertype1 == 4 || ordertype1 == 5 || ordertype1 == 6 ||
                                                        ordertype1 == 7 || ordertype1 == 8 || ordertype1 == 9 ||
                                                        ordertype1 == 21 || ordertype1 == 22) {
                                                    serverOrderNo = StringUtil.isEmpty(childOrder.get("orderNo")) ?
                                                            null : childOrder.get("orderNo").toString();
                                                    logger.info("子订单服务订单号：{}", serverOrderNo);
                                                    if (!StringUtil.isEmpty(serverOrderNo)) {
                                                        logger.info("修改之前所有服务订单类型为已完成开始");
                                                        orderDao.updateCFOrderNo(patientId, doctorId, serverOrderNo);
                                                        logger.info("修改之前所有服务订单类型为已完成结束");
                                                    }

                                                    Integer validityTime = Util.queryNowTime() + (48 * 60 * 60);
                                                    if (ordertype1 == 4 || ordertype1 == 7 || ordertype1 == 6 ||
                                                            ordertype1 == 8 || ordertype1 == 22) {
                                                        try {
                                                            if (ordertype1 == 4 || ordertype1 == 7) {
                                                                logger.info("同步挂号费至HIS开始：{}",
                                                                        childOrder.get("orderNo").toString());
                                                                Util.addHisServer(childOrder.get("orderNo").toString(),
                                                                        orderDao);
                                                                logger.info("同步挂号费至HIS结束");
                                                            }
                                                        } catch (Exception e) {
                                                            logger.error("订单【{}】服务同步his失败：{}",
                                                                    childOrder.get("orderNo").toString(), e);
                                                        }
                                                        OrderDetailServer serverDetail = new OrderDetailServer();
                                                        serverDetail.setOrderNo(childOrder.get("orderNo").toString());
                                                        serverDetail.setValidityTime(validityTime);
                                                        orderDao.updateOrderDetailServer(serverDetail);
                                                        logger.info("修改服务订单有效时长，订单号：{}，有效期至：{}结束",
                                                                childOrder.get("orderNo").toString(), validityTime);
                                                    }
                                                }
                                                if (ordertype1 == 10 || ordertype1 == 13 || ordertype1 == 14 ||
                                                        ordertype1 == 15 || ordertype1 == 16 || ordertype1 == 17 ||
                                                        ordertype1 == 18 || ordertype1 == 19) {
                                                    orderStatus = 0;
                                                    logger.info("获取医患最后一次正在调理服务订单开始");
                                                    Map<String, Object> lastOrder = appDoctorDao.queryLastOrderNo(patientId, doctorId);
                                                    logger.info("获取医患最后一次服务订单结束");
                                                    if (null != lastOrder) {
                                                        if (StringUtil.isEmpty(serverOrderNo)) {
                                                            serverOrderNo = StringUtil.isEmpty(lastOrder.get("orderNo")) ?
                                                                    null : lastOrder.get("orderNo").toString();
                                                        }
                                                        if (!StringUtil.isEmpty(serverOrderNo)) {
                                                            logger.info("推送用药说明开始");
                                                            JSONObject json = new JSONObject();
                                                            json.put("msgType", 27);
                                                            json.put("serverOrderNo", serverOrderNo);
                                                            Map<String, Object> result = GetSig.sendMsgSync(request,
                                                                    doctorId, patientId, json);
                                                            if (result != null &&
                                                                    result.get("respCode").toString().equals("1001")) {
                                                                orderDao.insertImSendRecord(null, doctorId, patientId,
                                                                        1, 27, null, "sendMsgSync");
                                                            } else {
                                                                result = GetSig.sendMsgSync(request, doctorId, patientId, json);
                                                                if (result != null
                                                                        && !result.get("respCode").toString().equals("1001")) {
                                                                    orderDao.insertImSendRecord(null, doctorId, patientId,
                                                                            0, 27, null, "sendMsgSync");
                                                                }
                                                            }
                                                            logger.info("推送用药说明结束");
                                                            logger.info("查询复诊时间推送开始");
                                                            Map<String, Object> visitTime = orderDao.queryVisitTimeyOrderNo(childOrder.get("orderNo").toString());
                                                            logger.info("查询复诊时间推送结束，数据为：{}", visitTime);
                                                            try {
                                                                if (null != visitTime &&
                                                                        !StringUtil.isEmpty(visitTime.get("visitTime")) &&
                                                                        !visitTime.get("visitTime").toString().equals("0")) {
                                                                    logger.info("推送建议复诊调理日期开始");
                                                                    result = GetSig.sendMsgTest(request, doctorId, patientId,
                                                                            "建议复诊调理日期：" + DateTime.getDate(Integer.parseInt(visitTime.get("visitTime").toString())),
                                                                            serverOrderNo);
                                                                    if (result != null &&
                                                                            result.get("respCode").toString().equals("1001")) {
                                                                        orderDao.insertImSendRecord(serverOrderNo,
                                                                                doctorId, patientId, 1, null,
                                                                                "建议复诊调理日期：" + DateTime.getDate(Integer.parseInt(visitTime.get("visitTime").toString())),
                                                                                "sendMsgTest");
                                                                    } else {
                                                                        result = GetSig.sendMsgTest(request, doctorId,
                                                                                patientId,
                                                                                "建议复诊调理日期：" + DateTime.getDate(Integer.parseInt(visitTime.get("visitTime").toString())),
                                                                                serverOrderNo);
                                                                        if (result != null &&
                                                                                !result.get("respCode").toString().equals("1001")) {
                                                                            orderDao.insertImSendRecord(serverOrderNo,
                                                                                    doctorId, patientId, 0, null,
                                                                                    "建议复诊调理日期：" + DateTime.getDate(Integer.parseInt(visitTime.get("visitTime").toString())),
                                                                                    "sendMsgTest");
                                                                        }
                                                                    }
                                                                    logger.info("推送建议复诊调理日期结束");
                                                                }
                                                            } catch (Exception e) {
                                                                logger.error("订单【{}】状态变更为：{},异常信息：{}", orderNo, paystatus, e);
                                                                orderDao.addCreateLog(null, "支付模块",
                                                                        "订单【" + orderNo + "】扣款成功，状态变更为" + paystatus + "操作失败",
                                                                        orderNo, 8, "用户");
                                                                Map<String, Object> params = new HashMap<>();
                                                                params.put("msgtype", "text");
                                                                params.put("toUser", "o92LXv1ZIHTjleF3MYPFlAiV7Bas");
                                                                params.put("url", "");
                                                                params.put("content", "发送IM复诊时间失败");
                                                                sendModelMsgUtil.sendCustomMsg(params, orderDao);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                logger.error("订单【{}】状态变更为：{}，异常信息：", orderNo, paystatus, e);
                                orderDao.addCreateLog(null, "支付模块",
                                        "订单【" + orderNo + "】扣款成功，状态变更为" + paystatus + "操作失败",
                                        orderNo, 8, "用户");
                                Map<String, Object> params = new HashMap<>();
                                params.put("msgtype", "text");
                                params.put("toUser", "o92LXv1ZIHTjleF3MYPFlAiV7Bas");
                                params.put("url", "");
                                params.put("content", "订单：" + orderNo + "扣款成功，状态变更失败");
                                sendModelMsgUtil.sendCustomMsg(params, orderDao);

                                params.put("content", "订单：" + orderNo + "扣款成功，状态变更失败2");
                                sendModelMsgUtil.sendCustomMsg(params, orderDao);
                            }
                            String sourOrderNo = StringUtil.isEmpty(order.get("sourceOrderNo")) ?
                                    null : order.get("sourceOrderNo").toString();
                            if (!StringUtil.isEmpty(sourOrderNo)) {
                                try {
                                    logger.info("修改药品关联服务订单状态，sourOrderNo：{} 开始", sourOrderNo);
                                    orderDao.updateOrderStatus(paystatus, orderStatus, tradeNo, payway, sourOrderNo,
                                            null, followId, orderWay,yztId);
                                    logger.info("修改药品关联服务订单状态，sourOrderNo：{} 结束", sourOrderNo);
                                    Map<String, Object> serverOrder = orderDao.queryOrderByOrderNo(sourOrderNo);
                                    logger.info("药品关联服务订单信息：{}", serverOrder);
                                    if (serverOrder != null &&
                                            !StringUtil.isEmpty(serverOrder.get("orderType")) &&
                                            Integer.valueOf(serverOrder.get("orderType").toString()) == 4) {
                                        logger.info("药品关联服务订单信息为图文调理，修改订单过期时间开始");
                                        OrderDetailServer serverDetail = new OrderDetailServer();
                                        serverDetail.setOrderNo(sourOrderNo);
                                        serverDetail.setValidityTime(Util.queryNowTime() + (48 * 60 * 60));
                                        orderDao.updateOrderDetailServer(serverDetail);
                                        logger.info("药品关联服务订单信息为图文调理，修改订单过期时间结束");
                                    }
                                } catch (Exception e) {
                                    logger.info("订单【{}】状态变更为：{}，异常信息：{}", orderNo, paystatus, e);
                                    orderDao.addCreateLog(null, "支付模块",
                                            "订单【" + orderNo + "】扣款成功，状态变更为" + paystatus + "操作失败",
                                            orderNo, 8, "用户");
                                    Map<String, Object> params = new HashMap<>();
                                    params.put("msgtype", "text");
                                    params.put("toUser", "o92LXv1ZIHTjleF3MYPFlAiV7Bas");
                                    params.put("url", "");
                                    params.put("content", "订单：" + orderNo + "扣款成功，状态变更失败");
                                    sendModelMsgUtil.sendCustomMsg(params, orderDao);
                                }
                            }

                            try {
                                if (ordertype == 4 || ordertype == 5 || ordertype == 6 || ordertype == 7 ||
                                        ordertype == 8 || ordertype == 9 || ordertype == 21 || ordertype == 22) {
                                    logger.info("支付订单为服务订单，订单号：{}，修改之前所有的服务订单为已完成 开始", orderNo);
                                    orderDao.updateCFOrderNo(patientId, doctorId, orderNo);
                                    logger.info("支付订单为服务订单，订单号：{}，修改之前所有的服务订单为已完成 结束", orderNo);
                                    try {
                                        if (orderType == 4 || orderType == 7) {
                                            logger.info("同步挂号费到HIS系统,订单号：{}", orderNo);
                                            Util.addHisServer(orderNo, orderDao);
                                        }
                                    } catch (Exception e) {
                                        logger.error("订单【{}】服务同步his失败：{}", orderNo, e);
                                    }
                                    if (ordertype == 4 || ordertype == 7 || ordertype == 6 || ordertype == 8 || ordertype == 22) {
                                        Integer validityTime = Util.queryNowTime() + (48 * 60 * 60);
                                        OrderDetailServer orderDetailServer = new OrderDetailServer();
                                        orderDetailServer.setOrderNo(orderNo);
                                        orderDetailServer.setValidityTime(validityTime);
                                        orderDao.updateOrderDetailServer(orderDetailServer);
                                        logger.info("订单【{}】状态变更为：{}", orderNo, paystatus);
                                    }
                                }
                            } catch (Exception e) {
                                logger.error("订单【{}】状态变更为：{}，异常信息：{}", orderNo, paystatus, e);
                                orderDao.addCreateLog(null, "支付模块",
                                        "订单【" + orderNo + "】扣款成功，状态变更为" + paystatus + "操作失败",
                                        orderNo, 8, "用户");
                                Map<String, Object> params = new HashMap<>();
                                params.put("msgtype", "text");
                                params.put("toUser", "o92LXv1ZIHTjleF3MYPFlAiV7Bas");
                                params.put("url", "");
                                params.put("content", "订单：" + orderNo + "扣款成功，状态变更失败");
                                sendModelMsgUtil.sendCustomMsg(params, orderDao);
                            }

                            try {
                                if (!StringUtil.isEmpty(doctorId) && !StringUtil.isEmpty(patientId)) {
                                    Integer count = orderDao.queryDoctorPatientRelationship(doctorId, patientId);
                                    logger.info("医患关系是否存在：doctorId={}；patientId={}，是否存在{}", doctorId, patientId, count);
                                    if (count <= 0) {
                                        logger.info("医患关系不存在，插入医患关系doctorId={}，patientId={} 开始", doctorId, patientId);
                                        orderDao.insertDoctorPatientRelationship(doctorId, order.get("memberId").toString(), patientId);
                                        logger.info("医患关系不存在，插入医患关系doctorId={}，patientId={} 完成", doctorId, patientId);

                                        logger.info("同步医患关系至呼叫中心开始");
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("id", Util.getUUID());
                                        map.put("doctorId", doctorId);
                                        map.put("memberId", order.get("memberId").toString());
                                        map.put("patientId", patientId);
                                        String result = SendCallCenterUtil.sendCallCenterData(map, CallCenterConfig.PatientRelation);
                                        logger.info("同步医患关系至呼叫中心结束：{}", result);
                                    }
                                    Integer keep = appDoctorDao.queryKeep(order.get("memberId").toString(), doctorId);
                                    logger.info("查询患者是否关注医生：{} 开始", keep);
                                    if (keep == 0) {
                                        appDoctorDao.addKeep(order.get("memberId").toString(), doctorId);
                                        logger.info("查询患者是否关注医生：{} 结束", keep);
                                    }
                                }
                            } catch (Exception e) {
                                logger.error("订单【{}】状态变更为：{}，异常信息：{}", orderNo, paystatus, e);
                                orderDao.addCreateLog(null, "支付模块",
                                        "订单【" + orderNo + "】扣款成功，状态变更为" + paystatus + "操作失败", orderNo,
                                        8, "用户");
                                Map<String, Object> params = new HashMap<>();
                                params.put("msgtype", "text");
                                params.put("toUser", "o92LXv1ZIHTjleF3MYPFlAiV7Bas");
                                params.put("url", "");
                                params.put("content", "订单：" + orderNo + "扣款成功，状态变更失败");
                                sendModelMsgUtil.sendCustomMsg(params, orderDao);
                            }
                        }

                        try {
                            if (orderType == 12) {// 锦旗
                                logger.info("购买锦旗支付完成插入我的锦旗表开始");
                                List<Map<String, Object>> orderDetails = orderDao.queryOrderDetailByOrderNo(orderNo);
                                logger.info("购买锦旗支付完成插入我的锦旗表，锦旗详情：{}", orderDetails);
                                if (null != orderDetails && orderDetails.size() > 0) {
                                    for (Map<String, Object> orderDetail : orderDetails) {
                                        if (orderDetail != null && !StringUtil.isEmpty(orderDetail.get("goodsId"))) {
                                            logger.info("查询锦旗详情开始");
                                            Map<String, Object> myEva = orderDao.queryEvaById(orderDetail.get("goodsId").toString());
                                            if (myEva != null) {
                                                logger.info("查询锦旗详情结束");
                                                int count = Integer.parseInt(orderDetail.get("goodsNum").toString());
                                                logger.info("查询锦旗：{}，购买数量：{}", myEva.get("name"), count);
                                                for (int j = 0; j < count; j++) {
                                                    logger.info("批量插入我的锦旗表中，共{}次，当前{}", count, i);
                                                    MyEvaBanner myEvaBanner = new MyEvaBanner();
                                                    myEvaBanner.setCreateTime(Util.queryNowTime());
                                                    myEvaBanner.setMemberId(order.get("memberId").toString());
                                                    myEvaBanner.setOrderNo(orderNo);
                                                    myEvaBanner.setPrice(Double.parseDouble(orderDetail.get("goodsPrice").toString()));
                                                    myEvaBanner.setType(Integer.valueOf(myEva.get("type").toString()));
                                                    myEvaBanner.setEvaBannerId(myEva.get("id").toString());
                                                    orderDao.addEntityUUID(myEvaBanner);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            logger.error("订单【{}】状态变更为：{}，异常信息：", orderNo, paystatus, e);
                            orderDao.addCreateLog(null, "支付模块",
                                    "订单【" + orderNo + "】扣款成功，状态变更为" + paystatus + "操作失败",
                                    orderNo, 8, "用户");
                            Map<String, Object> params = new HashMap<>();
                            params.put("msgtype", "text");
                            params.put("toUser", "o92LXv1ZIHTjleF3MYPFlAiV7Bas");
                            params.put("url", "");
                            params.put("content", "订单：" + orderNo + "扣款成功，状态变更失败");
                            sendModelMsgUtil.sendCustomMsg(params, orderDao);
                        }
                    }
                } else {
                    logger.info("订单【{}】状态变更为：{}，操作失败", orderNo, paystatus);
                    orderDao.addCreateLog(null, "支付模块",
                            "订单【" + orderNo + "】扣款成功，状态变更为" + paystatus + "操作失败",
                            orderNo, 8, null);
                    Map<String, Object> params = new HashMap<>();
                    params.put("msgtype", "text");
                    params.put("toUser", "o92LXv1ZIHTjleF3MYPFlAiV7Bas");
                    params.put("url", "");
                    params.put("content", "订单：" + orderNo + "扣款成功，状态变更失败");
                    sendModelMsgUtil.sendCustomMsg(params, orderDao);

                    return 0;
                }
            } else {
                logger.info("订单【{}】状态变更为：{}，操作失败", orderNo, paystatus);
                orderDao.addCreateLog(null, "支付模块",
                        "订单【" + orderNo + "】扣款成功，状态变更为" + paystatus + "操作失败",
                        orderNo, 8, "用户");
                Map<String, Object> params = new HashMap<>();
                params.put("msgtype", "text");
                params.put("toUser", "o92LXv1ZIHTjleF3MYPFlAiV7Bas");
                params.put("url", "");
                params.put("content", "订单：" + orderNo + "扣款成功，状态变更失败");
                sendModelMsgUtil.sendCustomMsg(params, orderDao);

                return 0;
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.info("订单【{}】状态变更为：{}，操作失败：{}", orderNo, paystatus, e);
            orderDao.addCreateLog(null, "支付模块",
                    "订单【" + orderNo + "】扣款成功，状态变更为" + paystatus + "操作失败",
                    orderNo, 8, "用户");
            Map<String, Object> params = new HashMap<>();
            params.put("msgtype", "text");
            params.put("toUser", "o92LXv1ZIHTjleF3MYPFlAiV7Bas");
            params.put("url", "");
            params.put("content", "订单：" + orderNo + "扣款成功，状态变更失败");
            sendModelMsgUtil.sendCustomMsg(params, orderDao);

            return 0;
        }
        return i;
    }

    /**
     * 添加支付信息
     *
     * @param orderNo      orderNo
     * @param total_amount total_amount
     * @param buyer_id     buyer_id
     * @param trade_no     trade_no
     * @param trade_status trade_status
     * @return Integer
     */
    @Override
    public Integer addPayment(String orderNo, String total_amount, String buyer_id, String trade_no, String trade_status) {
        return orderDao.addPayment(orderNo, total_amount, buyer_id, trade_no, trade_status);
    }

    /**
     * 查询订单列表
     *
     * @param order order
     * @param page  page
     * @param row   row
     * @return 订单列表
     */
    @Override
    public Map<String, Object> queryOrder(Order order, Integer page, Integer row) {
        List<Map<String, Object>> list = orderDao.queryOrder(order, page, row);
        if (null == list) {
            return Util.resultMap(configCode.code_1015, list);
        }
        return Util.resultMap(configCode.code_1001, list);
    }

    /**
     * 查询退款订单详情
     *
     * @param request request
     * @param orderNo orderNo
     * @return 退款订单详情
     */
    @Override
    public Map<String, Object> queryRefundOrderDetail(HttpServletRequest request, String orderNo) {
        String memberId = GetOpenId.getMemberId(request);

        Map<String, Object> map = orderDao.queryRefundOrderDetail(orderNo, memberId);
        if (null == map) {
            return Util.resultMap(configCode.code_1015, map);
        }
        return Util.resultMap(configCode.code_1001, map);
    }

    /**
     * 根据订单号查询订单信息
     *
     * @param order order
     * @return 订单信息
     */
    @Override
    public Map<String, Object> queryOrderByOrderNo(Order order) {
        Map<String, Object> map = orderDao.queryOrderByOrderNo(order);
        if (null == map || StringUtil.isEmpty(map.get("orderNo"))) {
            return Util.resultMap(configCode.code_1071, map);
        }
        return Util.resultMap(configCode.code_1001, map);
    }

    /**
     * 更新发货时间
     *
     * @param orderNo orderNo
     * @return 更新发货时间
     */
    @Override
    public Integer updateDeliveryTime(String orderNo) {
        return orderDao.updateDeliveryTime(orderNo);
    }

    /**
     * 根据订单号查询订单详情
     *
     * @param orderNo orderNo
     * @return 订单详情
     */
    @Override
    public List<Map<String, Object>> queryOrderDetailByOrderNo(String orderNo) {
        List<Map<String, Object>> map = orderDao
                .queryOrderDetailByOrderNo(orderNo);
        return map;
    }

    /**
     * 查询退款进度
     *
     * @param orderNo orderNo
     * @return 退款进度
     */
    @Override
    public Map<String, Object> queryRefundProgress(String orderNo) {
        Map<String, Object> map = orderDao.queryRefundProgress(orderNo);
        if (null == map) {
            return Util.resultMap(configCode.code_1015, map);
        }
        return Util.resultMap(configCode.code_1001, map);
    }

    /**
     * 查询系统设置
     *
     * @param orderType orderType
     * @return 系统设置
     */
    @Override
    public Integer getLockTimes(Integer orderType) {
        int payTime = 30;
        try {
            if (orderType != null) {
                SysSet sysSet = null == RedisUtil.getVal("sysSet") ? null : (SysSet) RedisUtil.getVal("sysSet");
                if (orderType == 1) {
                    payTime = null == sysSet || sysSet.getGoodPayTime() == 0 ? 30 : sysSet.getGoodPayTime();
                }
                if (orderType == 4) {
                    payTime = null == sysSet || sysSet.getGhPayTime() == 0 ? 30 : sysSet.getGhPayTime();
                }
                if (orderType == 5) {
                    payTime = null == sysSet || sysSet.getPzPayTime() == 0 ? 30 : sysSet.getPzPayTime();
                }
                if (orderType == 6) {
                    payTime = null == sysSet || sysSet.getHzPayTime() == 0 ? 30 : sysSet.getHzPayTime();
                }
                if (orderType == 7) {
                    payTime = null == sysSet || sysSet.getZyPayTime() == 0 ? 30 : sysSet.getZyPayTime();
                }
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return payTime;
    }

    @Override
    public List<Map<String, Object>> queryNoFinishOrders() {
        return null;
    }

    @Override
    public List<Map<String, Object>> operaHosOrder() {
        return null;
    }

    /**
     * 查询订单列表 包含所有类型订单
     *
     * @param memberId memberId
     * @param order    order
     * @param row      row
     * @param page     page
     * @return 订单列表
     */
    @Override
    public Map<String, Object> queryAllOrderList(String memberId, Order order, Integer row, Integer page) {
        List<Map<String, Object>> list = orderDao.queryAllOrderList(memberId, order, row, page);
        if (null == list) {
            return Util.resultMap(configCode.code_1071, null);
        } else {
            return Util.resultMap(configCode.code_1001, list);
        }
    }

    /**
     * 查询订单信息（支付时用）
     *
     * @param request request
     * @param order   order
     * @return 订单信息
     */
    @Override
    public Map<String, Object> queryPayOrderDetail(HttpServletRequest request, Order order) {
        String memberId = null;
        if (StringUtils.isEmpty(order.getMemberId())) {
            memberId = GetOpenId.getMemberId(request);
            order.setMemberId(memberId);
        }
        if (StringUtils.isEmpty(order.getOrderNo())) {
            return Util.resultMap(configCode.code_1029, null);
        } else {
            Map<String, Object> map = orderDao.queryPayOrderDetail(order);
            if (null == map || StringUtil.isEmpty(map.get("orderNo"))) {
                return Util.resultMap(configCode.code_1071, map);
            } else if (map.get("state").toString().equals("0")) {
                return Util.resultMap(configCode.code_1111, map);
            } else if (map.get("paymentStatus").toString().equals("6")) {
                return Util.resultMap(configCode.code_1026, map);
            } else if (!map.get("paymentStatus").toString().equals("1")) {
                return Util.resultMap(configCode.code_1024, map);
            } else {
                map.put("payMemberId", memberId);
                return Util.resultMap(configCode.code_1001, map);
            }
        }
    }

    /**
     * 根据订单号查询该订单是否支付
     *
     * @param orderNo orderNo
     * @return 该订单是否支付
     */
    @Override
    public Integer queryPayResultByOrderNo(String orderNo) {
        return orderDao.queryPayResultByOrderNo(orderNo);
    }

    /**
     * 查询导入人员
     *
     * @param openId openId
     * @return 导入人员
     */
    @Override
    public Map<String, Object> queryFollowIdByOpenId(String openId) {
        return orderDao.queryFollowIdByOpenId(openId);
    }

    /**
     * 根据主订单号查询药品订单
     *
     * @param mainOrderNo mainOrderNo
     * @return 药品订单
     */
    @Override
    public Map<String, Object> queryRecordOrderByMainOrderNo(String mainOrderNo) {
        return orderDao.queryRecordOrderByMainOrderNo(mainOrderNo);
    }

    /**
     * 根据订单号查询药品订单
     *
     * @param mainOrderNo mainOrderNo
     * @return 药品订单
     */
    @Override
    public Map<String, Object> queryRecordOrderByOrderNo(String mainOrderNo) {
        Map<String, Object> map = orderDao.queryRecordOrderByMainOrderNo(mainOrderNo);
        if (null != map && map.get("orderState").toString().equals("0")) {
            return Util.resultMap(configCode.code_1096, null);
        } else if (null != map && map.get("state").toString().equals("0")) {
            return Util.resultMap(configCode.code_1105, null);
        }
        return Util.resultMap(configCode.code_1001, map);
    }

    /**
     * 查询订单详情
     *
     * @param orderNo orderNo
     * @return 订单详情
     */
    @Override
    public Map<String, Object> queryOrderDetail(String orderNo) {
        Map<String, Object> map = orderDao.queryOrderDetail(orderNo);
        if (null == map || map.size() == 0) {
            return Util.resultMap(configCode.code_1015, null);
        } else {
            return Util.resultMap(configCode.code_1001, map);
        }
    }

    /**
     * 根据订单号查询订单信息
     *
     * @param orderNo orderNo
     * @return 订单信息
     */
    @Override
    public Map<String, Object> queryOrderByOrderNo(String orderNo) {
        return orderDao.queryOrderByOrderNo(orderNo);
    }

    /**
     * 查询锦旗信息
     *
     * @param id id
     * @return 锦旗信息
     */
    @Override
    public Map<String, Object> queryMyEvaBannerById(String id) {
        return orderDao.queryMyEvaBannerById(id);
    }

    /**
     * 添加支付成功信息
     *
     * @param request request
     * @param order   order
     * @return Integer
     */
    @Override
    public Integer paySuccessPush(HttpServletRequest request, Map<String, Object> order) {
        if (order != null) {
            setllment(StringUtil.isEmpty(order.get("orderNo")) ? null : order.get("orderNo").toString());
            pushPaySuccess(request, order);
            sendCallCenter(StringUtil.isEmpty(order.get("orderNo")) ? null : order.get("orderNo").toString());
            pushRecordNotice(request, order);
            pushFollow(order);
            pushSms(order, orderDao);
        }
        return 1;
    }

    /**
     * 添加关注信息
     *
     * @param order order
     */
    private void pushFollow(Map<String, Object> order) {
        String doctorId = StringUtil.isEmpty(order.get("doctorId")) ? null : order.get("doctorId").toString();
        String patientId = StringUtil.isEmpty(order.get("patientId")) ? null : order.get("patientId").toString();
        int orderType = Integer.valueOf(order.get("orderType").toString());
        Map<String, Object> follow = this.queryFollowIdByOpenId(StringUtil.isEmpty(order.get("doctorId")) ?
                null : order.get("doctorId").toString());
        int paystatus = 2;
        String orderNo = StringUtil.isEmpty(order.get("orderNo")) ? null : order.get("orderNo").toString();
        try {
            if (null != follow && follow.size() > 0) {
                Map<String, Object> patient = pushService.queryOpenIdByPatientId(patientId);

                if (orderType == 4) {
                    pushService.paysuczxtl(follow.get("followId").toString(), doctorId, follow.get("docName").toString());
                    pushService.doctwtlservice(follow.get("followId").toString(), patient.get("name").toString(),
                            follow.get("docName").toString(), orderNo, doctorId);
                } else if (orderType == 7) {
                    pushService.paysucdhtl(follow.get("followId").toString(), doctorId, follow.get("docName").toString());
                } else if (orderType == 6) {
                    pushService.paysuctwzx(follow.get("followId").toString(), doctorId, follow.get("docName").toString());
                    pushService.doctwzxservice(follow.get("followId").toString(), patient.get("name").toString(),
                            follow.get("docName").toString(), orderNo, doctorId);
                } else if (orderType == 8) {
                    pushService.paysucdhzx(follow.get("followId").toString(), doctorId, follow.get("docName").toString());
                }
            }
        } catch (Exception e) {
            logger.error("订单【{}】状态变更为：{}，异常信息：", orderNo, paystatus, e);
            orderDao.addCreateLog(null, "支付模块",
                    "订单【" + orderNo + "】扣款成功，状态变更为" + paystatus + "操作失败",
                    orderNo, 8, "用户");
            Map<String, Object> params = new HashMap<>();
            params.put("msgtype", "text");
            params.put("toUser", "o92LXv2EU_K1_TmR1Zi2QZZ65rlU");
            params.put("url", "");
            params.put("content", "订单：" + orderNo + "扣款成功，状态变更失败");
            sendModelMsgUtil.sendCustomMsg(params, orderDao);
            params.put("toUser", "o92LXv1ZIHTjleF3MYPFlAiV7Bas");
            params.put("content", "订单：" + orderNo + "扣款成功，状态变更失败10");
            sendModelMsgUtil.sendCustomMsg(params, orderDao);
        }
    }

    /**
     * 添加支付信息
     *
     * @param request request
     * @param order   order
     */
    private void pushPaySuccess(HttpServletRequest request, Map<String, Object> order) {
        int paystatus = 2;
        String orderNo = StringUtil.isEmpty(order.get("orderNo")) ? null : order.get("orderNo").toString();
        try {
            String doctorId = StringUtil.isEmpty(order.get("doctorId")) ? null : order.get("doctorId").toString();
            String patientId = StringUtil.isEmpty(order.get("patientId")) ? null : order.get("patientId").toString();
            Integer orderType = Integer.valueOf(order.get("orderType").toString());
            if (orderType == 1) {
                logger.info("支付成功推送消息给用户开始：memberId={}", order.get("memberId").toString());
                String sendCode = sendModelMsgUtil.sendOrder(order.get("memberId").toString(), orderNo,
                        order.get("receiptsPrice") + "", "商城商品", orderDao,
                        "order/order_detail.html?orderNo=" + orderNo);
                if (StringUtil.isEmpty(sendCode) || !"0".equals(sendCode)) {
                    sendCode = sendModelMsgUtil.sendOrder(order.get("memberId").toString(), orderNo,
                            order.get("receiptsPrice") + "", "商城商品", orderDao,
                            "order/order_detail.html?orderNo=" + orderNo);
                    if (!StringUtil.isEmpty(sendCode) && "0".equals(sendCode)) {
                        orderDao.insertImSendRecord(orderNo, doctorId, patientId, 1, null,
                                order.get("receiptsPrice") + "", "sendOrder");
                    } else {
                        orderDao.insertImSendRecord(orderNo, doctorId, patientId, 0, null,
                                order.get("receiptsPrice") + "", "sendOrder");
                        logger.info("发送推送消息失败，sendCode={}", sendCode);
                        Map<String, Object> params = new HashMap<String, Object>();
                        params.put("msgtype", "text");
                        params.put("toUser", "o92LXv1ZIHTjleF3MYPFlAiV7Bas");
                        params.put("url", "");
                        params.put("content", "发送推送消息失败，方法名称sendOrder" + sendCode);
                        sendModelMsgUtil.sendCustomMsg(params, orderDao);
                    }
                } else {
                    orderDao.insertImSendRecord(orderNo, doctorId, patientId, 1, null,
                            order.get("receiptsPrice") + "", "sendOrder");
                }
                logger.info("支付成功推送消息给用户结束：memberId={}", order.get("memberId").toString());
            }

            Map<String, Object> patientData = orderDao.queryPatientById(patientId);
            String patientName = patientData == null || StringUtil.isEmpty(patientData.get("name")) ?
                    null : patientData.get("name").toString();
            Map<String, Object> doctor = appDoctorDao.queryPushDoctor(doctorId);
            if (patientData != null) {
                if (orderType == 4) {
                    if (!StringUtil.isEmpty(order.get("rsrvStr1")) && order.get("rsrvStr1").equals("1")) {
                        JSONObject json = new JSONObject();
                        json.put("msgType", 16);
                        json.put("orderNo", orderNo);
                        json.put("content", order.get("receiptsPrice"));
                        Map<String, Object> result = GetSig.sendMsg(request, patientId, doctorId, 0, json);
                        if (result != null && result.get("respCode").toString().equals("1001")) {
                            orderDao.insertImSendRecord(orderNo, doctorId, patientId, 1, null,
                                    order.get("receiptsPrice").toString(), "sendMsg");
                        } else {
                            result = GetSig.sendMsg(request, patientId, doctorId, 0, json);
                            if (result != null && !result.get("respCode").toString().equals("1001")) {
                                orderDao.insertImSendRecord(orderNo, doctorId, patientId, 0, null,
                                        order.get("receiptsPrice").toString(), "sendMsg");
                                logger.info("发送IM消息失败，方法名称GetSig.sendMsg");
                                Map<String, Object> params = new HashMap<String, Object>();
                                params.put("msgtype", "text");
                                params.put("toUser", "o92LXv1ZIHTjleF3MYPFlAiV7Bas");
                                params.put("url", "");
                                params.put("content", "发送IM复诊消息失败，方法名称GetSig.sendMsg");
                                sendModelMsgUtil.sendCustomMsg(params, orderDao);
                            }
                        }
                    } else {
                        try {
                            Map<String, Object> result = GetSig.sendMsg(
                                    request, patientId, doctorId, orderNo,
                                    patientName, orderType);
                            if (result != null && result.get("respCode").toString().equals("1001")) {
                                orderDao.insertImSendRecord(orderNo, doctorId, patientId, 1, null,
                                        order.get("receiptsPrice").toString(), "sendMsg");
                            } else {
                                result = GetSig.sendMsg(request, patientId, doctorId, orderNo, patientName, orderType);
                                if (result != null && !result.get("respCode").toString().equals("1001")) {
                                    orderDao.insertImSendRecord(orderNo, doctorId, patientId, 0, null,
                                            order.get("receiptsPrice").toString(), "sendMsg");
                                    logger.info("发送IM消息失败，方法名称GetSig.sendMsg");
                                    Map<String, Object> params = new HashMap<String, Object>();
                                    params.put("msgtype", "text");
                                    params.put("toUser", "o92LXv1ZIHTjleF3MYPFlAiV7Bas");
                                    params.put("url", "");
                                    params.put("content", "发送IM复诊消息失败，方法名称GetSig.sendMsg");
                                    sendModelMsgUtil.sendCustomMsg(params, orderDao);
                                }
                            }
                        } catch (Exception e) {
                            logger.error("订单【{}】，状态变更为{}，异常信息：{}", orderNo, paystatus, e);
                            orderDao.addCreateLog(null, "支付模块",
                                    "订单【" + orderNo + "】扣款成功，状态变更为" + paystatus + "操作失败",
                                    orderNo, 8, "用户");
                            Map<String, Object> params = new HashMap<>();
                            params.put("msgtype", "text");
                            params.put("toUser", "o92LXv1ZIHTjleF3MYPFlAiV7Bas");
                            params.put("url", "");
                            params.put("content", "订单：" + orderNo + "扣款成功,queryDefultSpecial推送失败");
                            sendModelMsgUtil.sendCustomMsg(params, orderDao);
                        }
                    }
                } else if (orderType != 12 && orderType != 1) {
                    Map<String, Object> result = GetSig.sendMsg(request, patientId, doctorId, orderNo, patientName, orderType);
                    if (result != null && result.get("respCode").toString().equals("1001")) {
                        orderDao.insertImSendRecord(orderNo, doctorId, patientId, 1, null,
                                order.get("receiptsPrice").toString(), "sendMsg");
                    } else {
                        result = GetSig.sendMsg(request, patientId, doctorId, orderNo, patientName, orderType);
                        if (result != null && !result.get("respCode").toString().equals("1001")) {
                            orderDao.insertImSendRecord(orderNo, doctorId, patientId, 0, null,
                                    order.get("receiptsPrice").toString(), "sendMsg");
                            logger.error("发送IM消息失败，方法名称GetSig.sendMsg");
                            Map<String, Object> params = new HashMap<>();
                            params.put("msgtype", "text");
                            params.put("toUser", "o92LXv1ZIHTjleF3MYPFlAiV7Bas");
                            params.put("url", "");
                            params.put("content", "发送IM复诊消息失败，方法名称GetSig.sendMsg");
                            sendModelMsgUtil.sendCustomMsg(params, orderDao);
                        }
                    }
                    logger.info("订单【{}】状态变更为：{}", orderNo, paystatus);
                }
                if (null != doctor && doctor.size() > 0) {
                    if (orderType == 4) {
                        logger.info("订单【{}】状态变更为：{}", orderNo, paystatus);
                        String sendCode = sendModelMsgUtil.sendBuySuccess(patientId, "图文调理",
                                doctor.get("docName").toString(), doctor.get("infirmaryName").toString(),
                                patientName, Integer.valueOf(patientData.get("sex") + "") == 0 ? "男" : "女",
                                appDoctorDao, "im/inquiry.html?identifier=" + patientId + "&selToID=" + doctorId);
                        if (StringUtil.isEmpty(sendCode) || !"0".equals(sendCode)) {
                            sendCode = sendModelMsgUtil.sendBuySuccess(patientId, "图文调理",
                                    doctor.get("docName").toString(), doctor.get("infirmaryName").toString(),
                                    patientName, Integer.valueOf(patientData.get("sex") + "") == 0 ? "男" : "女",
                                    appDoctorDao, "im/inquiry.html?identifier=" + patientId + "&selToID=" + doctorId);
                            if (!StringUtil.isEmpty(sendCode) && "0".equals(sendCode)) {
                                orderDao.insertImSendRecord(orderNo, doctorId, patientId, 1, null,
                                        order.get("receiptsPrice") + "", "sendBuySuccess");
                            } else {
                                orderDao.insertImSendRecord(orderNo, doctorId, patientId, 0, null,
                                        order.get("receiptsPrice") + "", "sendBuySuccess");
                                logger.info("发送推送消息失败，方法名称sendBuySuccess");
                                Map<String, Object> params = new HashMap<String, Object>();
                                params.put("msgtype", "text");
                                params.put("toUser", "o92LXv1ZIHTjleF3MYPFlAiV7Bas");
                                params.put("url", "");
                                params.put("content", "发送推送消息失败，方法名称sendBuySuccess"
                                        + sendCode);
                                sendModelMsgUtil.sendCustomMsg(params, orderDao);
                            }
                        } else {
                            orderDao.insertImSendRecord(orderNo, doctorId, patientId, 1, null,
                                    order.get("receiptsPrice") + "", "sendBuySuccess");
                        }
                        pushService.docpaysuczxtl(doctorId, patientId, null);
                        logger.info("订单【{}】状态变更为：{}", orderNo, paystatus);
                        logger.info("短信通知医生患者已购买图文服务开始");
                        Boolean isSend = sendServerSmsNotice(SMSTemplateIdUtil.twServer_SMS, orderType, patientName,
                                StringUtil.isEmpty(doctor.get("docPhone")) ? null : doctor.get("docPhone").toString());
                        if (!isSend) {
                            logger.info("短信通知医生患者已购买图文服务通知失败");
                        }
                        logger.info("短信通知医生患者已购买图文服务结束");
                    } else if (orderType == 7) {
                        logger.info("订单【{}】状态变更为：{}", orderNo, paystatus);
                        String sendCode = sendModelMsgUtil.sendBuySuccess(patientId, "电话调理",
                                doctor.get("docName").toString(), doctor.get("infirmaryName").toString(),
                                patientName, Integer.valueOf(patientData.get("sex") + "") == 0 ? "男" : "女",
                                appDoctorDao, "im/inquiry.html?identifier=" + patientId + "&selToID=" + doctorId);
                        if (StringUtil.isEmpty(sendCode) || !"0".equals(sendCode)) {
                            sendCode = sendModelMsgUtil.sendBuySuccess(patientId, "电话调理",
                                    doctor.get("docName").toString(), doctor.get("infirmaryName").toString(),
                                    patientName, Integer.valueOf(patientData.get("sex") + "") == 0 ? "男" : "女",
                                    appDoctorDao, "im/inquiry.html?identifier=" + patientId + "&selToID=" + doctorId);
                            if (!StringUtil.isEmpty(sendCode) && "0".equals(sendCode)) {
                                orderDao.insertImSendRecord(orderNo, doctorId, patientId, 1, null,
                                        order.get("receiptsPrice") + "", "sendBuySuccess");
                            } else {
                                orderDao.insertImSendRecord(orderNo, doctorId, patientId, 0, null,
                                        order.get("receiptsPrice") + "", "sendBuySuccess");
                                logger.error("发送推送消息失败，方法名称sendBuySuccess");
                                Map<String, Object> params = new HashMap<>();
                                params.put("msgtype", "text");
                                params.put("toUser", "o92LXv1ZIHTjleF3MYPFlAiV7Bas");
                                params.put("url", "");
                                params.put("content", "发送推送消息失败，方法名称sendBuySuccess");
                                sendModelMsgUtil.sendCustomMsg(params, orderDao);
                            }
                        } else {
                            orderDao.insertImSendRecord(orderNo, doctorId, patientId, 1, null,
                                    order.get("receiptsPrice") + "", "sendBuySuccess");
                        }
                        pushService.docpaysucdhtl(doctorId, patientId, patientName);
                        logger.info("短信通知医生患者已购买图文服务开始");
                        Boolean isSend = sendServerSmsNotice(SMSTemplateIdUtil.phoneServer_SMS, orderType, patientName,
                                StringUtil.isEmpty(doctor.get("docPhone")) ? null : doctor.get("docPhone").toString());
                        if (!isSend) {
                            logger.info("短信通知医生患者已购买图文服务通知失败");
                        }
                        logger.info("短信通知医生患者已购买图文服务结束");
                    } else if (orderType == 6) {
                        String sendCode = sendModelMsgUtil.sendBuySuccess(patientId, "图文咨询",
                                doctor.get("docName").toString(), doctor.get("infirmaryName").toString(),
                                patientName, Integer.valueOf(patientData.get("sex") + "") == 0 ? "男" : "女",
                                appDoctorDao, "im/inquiry.html?identifier=" + patientId + "&selToID=" + doctorId);
                        if (StringUtil.isEmpty(sendCode) || !"0".equals(sendCode)) {
                            sendCode = sendModelMsgUtil.sendBuySuccess(patientId, "图文咨询",
                                    doctor.get("docName").toString(), doctor.get("infirmaryName").toString(),
                                    patientName, Integer.valueOf(patientData.get("sex") + "") == 0 ? "男" : "女",
                                    appDoctorDao, "im/inquiry.html?identifier=" + patientId + "&selToID=" + doctorId);
                            if (!StringUtil.isEmpty(sendCode) && "0".equals(sendCode)) {
                                orderDao.insertImSendRecord(orderNo, doctorId, patientId, 1, null,
                                        order.get("receiptsPrice") + "", "sendBuySuccess");
                            } else {
                                orderDao.insertImSendRecord(orderNo, doctorId, patientId, 0, null,
                                        order.get("receiptsPrice") + "", "sendBuySuccess");
                                logger.info("发送推送消息失败，方法名称sendBuySuccess");
                                Map<String, Object> params = new HashMap<>();
                                params.put("msgtype", "text");
                                params.put("toUser", "o92LXv1ZIHTjleF3MYPFlAiV7Bas");
                                params.put("url", "");
                                params.put("content", "发送推送消息失败，方法名称sendBuySuccess" + sendCode);
                                sendModelMsgUtil.sendCustomMsg(params, orderDao);
                            }
                        } else {
                            orderDao.insertImSendRecord(orderNo, doctorId, patientId, 1, null,
                                    order.get("receiptsPrice") + "", "sendBuySuccess");
                        }
                        pushService.docpaysuctwzx(doctorId, patientId, null);
                        logger.info("短信通知医生患者已购买图文服务开始");
                        Boolean isSend = sendServerSmsNotice(SMSTemplateIdUtil.twServer_SMS, orderType, patientName,
                                StringUtil.isEmpty(doctor.get("docPhone")) ? null : doctor.get("docPhone").toString());
                        if (!isSend) {
                            logger.info("短信通知医生患者已购买图文服务通知失败");
                        }
                    } else if (orderType == 8) {
                        String sendCode = sendModelMsgUtil.sendBuySuccess(patientId, "电话咨询",
                                doctor.get("docName").toString(), doctor.get("infirmaryName").toString(),
                                patientName, Integer.valueOf(patientData.get("sex") + "") == 0 ? "男" : "女",
                                appDoctorDao, "im/inquiry.html?identifier=" + patientId + "&selToID=" + doctorId);
                        if (StringUtil.isEmpty(sendCode) || !"0".equals(sendCode)) {
                            sendCode = sendModelMsgUtil.sendBuySuccess(patientId, "电话咨询",
                                    doctor.get("docName").toString(), doctor.get("infirmaryName").toString(),
                                    patientName, Integer.valueOf(patientData.get("sex") + "") == 0 ? "男" : "女",
                                    appDoctorDao, "im/inquiry.html?identifier=" + patientId + "&selToID=" + doctorId);
                            if (!StringUtil.isEmpty(sendCode) && "0".equals(sendCode)) {
                                orderDao.insertImSendRecord(orderNo, doctorId, patientId, 1, null,
                                        order.get("receiptsPrice") + "", "sendBuySuccess");
                            } else {
                                orderDao.insertImSendRecord(orderNo, doctorId, patientId, 0, null,
                                        order.get("receiptsPrice") + "", "sendBuySuccess");
                                logger.info("发送推送消息失败，方法名称sendBuySuccess");
                                Map<String, Object> params = new HashMap<>();
                                params.put("msgtype", "text");
                                params.put("toUser", "o92LXv1ZIHTjleF3MYPFlAiV7Bas");
                                params.put("url", "");
                                params.put("content", "发送推送消息失败，方法名称sendBuySuccess" + sendCode);
                                sendModelMsgUtil.sendCustomMsg(params, orderDao);
                            }
                        } else {
                            orderDao.insertImSendRecord(orderNo, doctorId, patientId, 1, null,
                                    order.get("receiptsPrice") + "", "sendBuySuccess");
                        }
                        pushService.docpaysuctwzx(doctorId, patientId, patientName);
                        logger.info("短信通知医生患者已购买图文服务开始");
                        boolean isSend = sendServerSmsNotice(SMSTemplateIdUtil.phoneServer_SMS, orderType, patientName,
                                StringUtil.isEmpty(doctor.get("docPhone")) ? null : doctor.get("docPhone").toString());
                        if (!isSend) {
                            logger.info("短信通知医生患者已购买图文服务通知失败");
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("订单【{}】状态变更为：{}，异常信息：{}", orderNo, paystatus, e);
            orderDao.addCreateLog(null, "支付模块",
                    "订单【" + orderNo + "】扣款成功，状态变更为" + paystatus + "操作失败", orderNo, 8, "用户");
            Map<String, Object> params = new HashMap<>();
            params.put("msgtype", "text");
            params.put("toUser", "o92LXv1ZIHTjleF3MYPFlAiV7Bas");
            params.put("url", "");
            params.put("content", "订单：" + orderNo + "扣款成功，状态变更失败");
            sendModelMsgUtil.sendCustomMsg(params, orderDao);
        }
    }

    private void pushRecordNotice(HttpServletRequest request, Map<String, Object> order) {
        int paystatus = 2;
        try {
            String orderNo = StringUtil.isEmpty(order.get("orderNo")) ? null : order.get("orderNo").toString();
            String doctorId = StringUtil.isEmpty(order.get("doctorId")) ? null : order.get("doctorId").toString();
            String patientId = StringUtil.isEmpty(order.get("patientId")) ? null : order.get("patientId").toString();
            int orderType = Integer.valueOf(order.get("orderType").toString());
            if (orderType == 10 || orderType == 13 || orderType == 14 || orderType == 15 || orderType == 16 ||
                    orderType == 17 || orderType == 18 || orderType == 19 || orderType == 20) {
                try {
                    JSONObject json = new JSONObject();
                    json.put("msgType", 13);
                    json.put("orderNo", orderNo);
                    json.put("content", order.get("receiptsPrice"));
                    Map<String, Object> result = GetSig.sendMsg(request, patientId, doctorId, 1, json);
                    if (result != null && result.get("respCode").toString().equals("1001")) {
                        orderDao.insertImSendRecord(orderNo, doctorId, patientId, 1, 13,
                                order.get("receiptsPrice").toString(), "sendMsg");
                    } else {
                        result = GetSig.sendMsg(request, patientId, doctorId, 1, json);
                        if (result != null && !result.get("respCode").toString().equals("1001")) {
                            orderDao.insertImSendRecord(orderNo, doctorId, patientId, 0, 13,
                                    order.get("receiptsPrice").toString(), "sendMsg");
                        }
                    }
                    Map<String, Object> follow = this.queryFollowIdByOpenId(StringUtil.isEmpty(order.get("doctorId")) ?
                            null : order.get("doctorId").toString());
                    logger.info("患者支付调理方通知助理收到新的调理方案开始，导流人信息：{}", follow);
                    if (null != follow && follow.size() > 0) {
                        pushService.docprescorder(follow.get("followId").toString(), doctorId, orderNo,
                                follow.get("docName").toString());
                        logger.info("患者支付调理方通知助理收到新的调理方案结束，导流人信息：{}", follow);
                    }
                    logger.info("患者支付调理方通知医生开始。orderNo={}；doctorId={}", orderNo, doctorId);
                    pushService.docpaytlsuc(doctorId, null, orderNo);
                    logger.info("患者支付调理方通知医生结束");
                    if (orderType == 18) {
                        logger.info("拍方抓药发送模版通知患者开始：orderNo={}；patientId={}", orderNo, patientId);
                        String sendCode = sendModelMsgUtil.sendOrder(patientId, orderNo,
                                order.get("receiptsPrice") + "", "调理方", orderDao,
                                "order/order_detail2.html?orderNo=" + orderNo);
                        if (StringUtil.isEmpty(sendCode) || !"0".equals(sendCode)) {
                            sendCode = sendModelMsgUtil.sendOrder(patientId, orderNo, order.get("receiptsPrice") + "",
                                    "调理方", orderDao, "order/order_detail2.html?orderNo=" + orderNo);
                            if (!StringUtil.isEmpty(sendCode) && "0".equals(sendCode)) {
                                orderDao.insertImSendRecord(orderNo, doctorId, patientId, 1, null,
                                        order.get("receiptsPrice") + "", "sendOrder");
                            } else {
                                orderDao.insertImSendRecord(orderNo, doctorId, patientId, 0, null,
                                        order.get("receiptsPrice") + "", "sendOrder");
                                logger.info("发送推送消息失败，方法名称sendOrder");
                                Map<String, Object> params = new HashMap<>();
                                params.put("msgtype", "text");
                                params.put("toUser", "o92LXv1ZIHTjleF3MYPFlAiV7Bas");
                                params.put("url", "");
                                params.put("content", "发送推送消息失败，方法名称sendOrder" + sendCode);
                                sendModelMsgUtil.sendCustomMsg(params, orderDao);
                            }
                        } else {
                            orderDao.insertImSendRecord(orderNo, doctorId, patientId, 1, null,
                                    order.get("receiptsPrice") + "", "sendOrder");
                        }
                        logger.info("拍方抓药发送模版通知患者开结束：orderNo={}；patientId={}", orderNo, patientId);
                    } else {
                        logger.info("发送模版通知患者开始：orderNo={}；patientId={}", orderNo, patientId);
                        String sendCode = sendModelMsgUtil.sendOrder(patientId, orderNo, order.get("receiptsPrice") + "",
                                "调理方", orderDao, "order/order_detail2.html?orderNo=" + orderNo);
                        if (StringUtil.isEmpty(sendCode) || !"0".equals(sendCode)) {
                            sendCode = sendModelMsgUtil.sendOrder(patientId, orderNo,
                                    order.get("receiptsPrice") + "", "调理方", orderDao,
                                    "order/order_detail2.html?orderNo=" + orderNo);
                            if (!StringUtil.isEmpty(sendCode) && "0".equals(sendCode)) {
                                orderDao.insertImSendRecord(orderNo, doctorId, patientId, 1, null,
                                        order.get("receiptsPrice") + "", "sendOrder");
                            } else {
                                orderDao.insertImSendRecord(orderNo, doctorId, patientId, 0, null,
                                        order.get("receiptsPrice") + "", "sendOrder");
                                logger.info("发送推送消息失败，方法名称sendOrder");
                                Map<String, Object> params = new HashMap<>();
                                params.put("msgtype", "text");
                                params.put("toUser", "o92LXv1ZIHTjleF3MYPFlAiV7Bas");
                                params.put("url", "");
                                params.put("content", "发送推送消息失败，方法名称sendOrder" + sendCode);
                                sendModelMsgUtil.sendCustomMsg(params, orderDao);
                            }
                        } else {
                            orderDao.insertImSendRecord(orderNo, doctorId, patientId, 1, null,
                                    order.get("receiptsPrice") + "", "sendOrder");
                        }
                        logger.info("发送模版通知患者结束：orderNo={}；patientId={}", orderNo, patientId);
                    }
                } catch (Exception e) {
                    logger.error(e.getLocalizedMessage());
                }
            }

            // 13-在线开方订单（直接开方） 14-拍照开方订单（非直接开方） 15-拍照开方订单（直接开方） 16-电话开方订单（非直接开方）
            // 17-电话开方订单（直接开方） 18-用户拍方抓药订单 19-抄方订单（抄方订单）
            if (orderType == 10 || orderType == 13 || orderType == 14 || orderType == 15 ||
                    orderType == 16 || orderType == 17 || orderType == 18 || orderType == 19) {
                try {
                    Map<String, Object> lastOrder = appDoctorDao.queryLastOrderNo(patientId, doctorId);
                    if (null != lastOrder && !StringUtil.isEmpty(lastOrder.get("orderNo"))) {
                        JSONObject json = new JSONObject();
                        json.put("msgType", 27);
                        json.put("serverOrderNo", lastOrder.get("orderNo"));
                        Map<String, Object> result = GetSig.sendMsgSync(request, doctorId, patientId, json);
                        if (result != null && result.get("respCode").toString().equals("1001")) {
                            orderDao.insertImSendRecord(null, doctorId, patientId, 1, 27,
                                    null, "sendMsgSync");
                        } else {
                            result = GetSig.sendMsgSync(request, doctorId, patientId, json);
                            if (result != null && !result.get("respCode").toString().equals("1001")) {
                                orderDao.insertImSendRecord(null, doctorId, patientId, 0, 27,
                                        null, "sendMsgSync");
                                logger.info("发送IM消息失败，方法名称GetSig.sendMsgSync");
                                Map<String, Object> params = new HashMap<>();
                                params.put("msgtype", "text");
                                params.put("toUser", "o92LXv1ZIHTjleF3MYPFlAiV7Bas");
                                params.put("url", "");
                                params.put("content", "发送IM消息失败，方法名称GetSig.sendMsgSync");
                                sendModelMsgUtil.sendCustomMsg(params, orderDao);
                            }
                        }
                        Map<String, Object> visitTime = orderDao.queryVisitTimeyOrderNo(orderNo);
                        if (null != visitTime && !StringUtil.isEmpty(visitTime.get("visitTime")) &&
                                !visitTime.get("visitTime").toString().equals("0")) {
                            result = GetSig.sendMsgTest(request, doctorId, patientId,
                                    "建议复诊调理日期：" + DateTime.getDate(Integer.parseInt(visitTime.get("visitTime").toString())),
                                    lastOrder.get("orderNo").toString());
                            if (result != null && result.get("respCode").toString().equals("1001")) {
                                orderDao.insertImSendRecord(lastOrder.get("orderNo").toString(), doctorId, patientId, 1, null,
                                        "建议复诊调理日期：" + DateTime.getDate(Integer.parseInt(visitTime.get("visitTime").toString())),
                                        "sendMsgTest");
                            } else {
                                result = GetSig.sendMsgTest(request, doctorId, patientId,
                                        "建议复诊调理日期：" + DateTime.getDate(Integer.parseInt(visitTime.get("visitTime").toString())),
                                        lastOrder.get("orderNo").toString());
                                if (result != null && !result.get("respCode").toString().equals("1001")) {
                                    orderDao.insertImSendRecord(lastOrder.get("orderNo").toString(), doctorId, patientId,
                                            0, null,
                                            "建议复诊调理日期：" + DateTime.getDate(Integer.parseInt(visitTime.get("visitTime").toString())),
                                            "sendMsgTest");
                                    logger.info("发送IM消息失败，方法名称GetSig.sendMsgTest");
                                    Map<String, Object> params = new HashMap<>();
                                    params.put("msgtype", "text");
                                    params.put("toUser", "o92LXv1ZIHTjleF3MYPFlAiV7Bas");
                                    params.put("url", "");
                                    params.put("content", "发送IM复诊消息失败，方法名称GetSig.sendMsgTest");
                                    sendModelMsgUtil.sendCustomMsg(params, orderDao);
                                }
                            }
                            logger.info("推送建议复诊调理日期结束");
                        }
                    }
                } catch (Exception e) {
                    logger.error("订单【{}】状态变更为：{}，异常信息：{}", orderNo, paystatus, e);
                    orderDao.addCreateLog(null, "支付模块",
                            "订单【" + orderNo + "】扣款成功，状态变更为" + paystatus + "操作失败",
                            orderNo, 8, "用户");
                    Map<String, Object> params = new HashMap<>();
                    params.put("msgtype", "text");
                    params.put("toUser", "o92LXv1ZIHTjleF3MYPFlAiV7Bas");
                    params.put("url", "");
                    params.put("content", "订单：" + orderNo + "扣款成功，状态变更失败");
                    sendModelMsgUtil.sendCustomMsg(params, orderDao);
                }
            }
        } catch (Exception e) {
            logger.error("pushRecordNotice方法报出异常，异常信息：{}", e);
            Map<String, Object> params = new HashMap<>();
            params.put("msgtype", "text");
            params.put("toUser", "o92LXv1ZIHTjleF3MYPFlAiV7Bas");
            params.put("url", "");
            params.put("content", "pushRecordNotice方法报出异常扣款成功，状态变更失败");
            sendModelMsgUtil.sendCustomMsg(params, orderDao);
        }
    }

    private void sendCallCenter(String orderNo) {
        logger.info("同步订单号为 {} 订单信息至呼叫中心开始", orderNo);
        Map<String, Object> map = orderDao.querySendCallCenterData(orderNo);
        try {
            if (map != null) {
                String result = SendCallCenterUtil.sendCallCenterData(map, CallCenterConfig.Order);
                logger.info("同步订单号为 {} 订单信息至呼叫中心完成", orderNo);
            }
        } catch (Exception e) {
            logger.error("sendCallCenter方法报出异常，异常信息：" + e);
            Map<String, Object> params = new HashMap<>();
            params.put("msgtype", "text");
            params.put("toUser", "o92LXv1ZIHTjleF3MYPFlAiV7Bas");
            params.put("url", "");
            params.put("content", "sendCallCenter方法报出异常");
            sendModelMsgUtil.sendCustomMsg(params, orderDao);
        }
    }

    private void setllment(String orderNo) {
        Producer.producer(orderNo);// 结算分成
    }

    /**
     * 更新子订单状态
     *
     * @param request request
     * @param orderNo orderNo
     * @return Integer
     */
    @Override
    public Integer updateChildState(HttpServletRequest request, String orderNo) {
        try {
            String serverOrderNo = null;
            int orderStatus = 1;
            String doctorId;
            String patientId;
            int paystatus;
            Map<String, Object> map = orderDao.queryOrderByOrderNo(orderNo);
            if (map == null) {
                return 0;
            }
            paystatus = Integer.valueOf(map.get("paymentStatus").toString());
            if (paystatus != 2) {
                return 0;
            }
            String tradeNo = map.get("tradeNo").toString();
            List<Map<String, Object>> childOrders = orderDao.queryChildOrderNos(orderNo);
            if (childOrders != null && childOrders.size() > 0) {
                for (Map<String, Object> childOrder : childOrders) {
                    Integer ordertype1;
                    if (!StringUtil.isEmpty(childOrder.get("orderType"))) {
                        ordertype1 = Integer.valueOf(childOrder.get("orderType").toString());
                        doctorId = childOrder.get("doctorId").toString();
                        patientId = childOrder.get("patientId").toString();
                        if (ordertype1 == 4 || ordertype1 == 5
                                || ordertype1 == 6 || ordertype1 == 7
                                || ordertype1 == 8 || ordertype1 == 9
                                || ordertype1 == 21 || ordertype1 == 22) {
                            serverOrderNo = StringUtil.isEmpty(childOrder.get("orderNo")) ?
                                    null : childOrder.get("orderNo").toString();
                            orderDao.updateCFOrderNo(patientId, doctorId, serverOrderNo);
                        }
                        if (ordertype1 == 10 || ordertype1 == 13
                                || ordertype1 == 14 || ordertype1 == 15
                                || ordertype1 == 16 || ordertype1 == 17
                                || ordertype1 == 18 || ordertype1 == 19) {
                            orderStatus = 0;
                            Map<String, Object> lastOrder = appDoctorDao.queryLastOrderNo(patientId, doctorId);
                            if (null != lastOrder) {
                                if (StringUtil.isEmpty(serverOrderNo)) {
                                    serverOrderNo = StringUtil.isEmpty(lastOrder.get("orderNo")) ?
                                            null : lastOrder.get("orderNo").toString();
                                }
                                if (!StringUtil.isEmpty(serverOrderNo)) {
                                    JSONObject json = new JSONObject();
                                    json.put("msgType", 27);
                                    json.put("serverOrderNo", serverOrderNo);
                                    GetSig.sendMsgSync(request, doctorId, patientId, json);
                                    Map<String, Object> visitTime = orderDao.queryVisitTimeyOrderNo(childOrder.get("orderNo").toString());
                                    if (null != visitTime && !StringUtil.isEmpty(visitTime.get("visitTime")) &&
                                            !visitTime.get("visitTime").toString().equals("0")) {
                                        GetSig.sendMsgTest(request, doctorId, patientId,
                                                "建议复诊调理日期：" + DateTime.getDate(Integer.parseInt(visitTime.get("visitTime").toString())),
                                                serverOrderNo);
                                    }
                                }
                            }
                        }
                        orderDao.updateOrderStatus(paystatus, orderStatus, tradeNo, 2,
                                childOrder.get("orderNo").toString(), null, null, 1,null);
                        Integer validityTime = Util.queryNowTime() + (48 * 60 * 60);
                        if (ordertype1 == 4 || ordertype1 == 7 || ordertype1 == 6 || ordertype1 == 8 || ordertype1 == 22) {
                            OrderDetailServer serverDetail = new OrderDetailServer();
                            serverDetail.setOrderNo(childOrder.get("orderNo").toString());
                            serverDetail.setValidityTime(validityTime);
                            orderDao.updateOrderDetailServer(serverDetail);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("订单【{}】，状态变更为：2，异常信息：{}", orderNo, e);
            orderDao.addCreateLog(null, "支付模块",
                    "订单【" + orderNo + "】扣款成功，状态变更为" + 2 + "操作失败", orderNo, 8, "用户");
            Map<String, Object> params = new HashMap<>();
            params.put("msgtype", "text");
            params.put("toUser", "o92LXv2EU_K1_TmR1Zi2QZZ65rlU");
            params.put("url", "");
            params.put("content", "订单：" + orderNo + "扣款成功，状态变更失败");
            sendModelMsgUtil.sendCustomMsg(params, orderDao);
            params.put("toUser", "o92LXv1ZIHTjleF3MYPFlAiV7Bas");
            params.put("content", "订单：" + orderNo + "扣款成功，状态变更失败2");
            sendModelMsgUtil.sendCustomMsg(params, orderDao);
        }
        return 1;
    }

    @SuppressWarnings("unused")
    @Override
    public Map<String, Object> applyRefundMoney(RefundApplyRecord record) {
        Object i = 0;
        try {
            if (StringUtils.isEmpty(record.getOrderNo()) || record.getMoney() == null) {
                return Util.resultMap(configCode.code_1029, null);
            }
            Map<String, Object> paramMap = orderDao.queryOrderByOrderNo(record.getOrderNo());
            if (paramMap == null) {
                return Util.resultMap(configCode.code_1034, null);
            }
            Integer menuId = null;
            int payStatus = Integer.valueOf(paramMap.get("paymentStatus").toString());
            Integer payTime = paramMap.get("payTime") == null || StringUtil.isEmpty(paramMap.get("payTime").toString()) ?
                    null : Integer.valueOf(paramMap.get("payTime").toString());
            if (payStatus == 1 || payStatus == 6 || payTime == null) {
                return Util.resultMap(configCode.code_1023, null);
            }
            if (Double.valueOf(paramMap.get("receiptsPrice").toString()) - record.getMoney() < 0) {
                return Util.resultMap(configCode.code_1135, null);
            }
            if (payStatus == 3 || payStatus == 4) {
                return Util.resultMap(configCode.code_1027, null);
            }
            Map<String, Object> sysSetMap = orderDao.getSysSet();
            Integer noApplyTime = 15;// 提前多长时间不可申请退款(天)
            Integer canApplyTime = 1;// 过多少天可申请退款
            Integer fwTime = null;
            boolean isCan;
            boolean isNoCan = true;
            isCan = Util.compareRefundTime(payTime, canApplyTime, true);
            if (!isCan) {
                return Util.resultMap(configCode.code_1055, null);
            }
            if (!isNoCan) {
                return Util.resultMap(configCode.code_1042, null);
            }
            Integer count = orderDao.queryRefundApply(record.getOrderNo(),
                    record.getDetailId());
            if (isCan && isNoCan && count == 0 && (payStatus == 2 || payStatus == 9)) {
                record.setCreatetime(Util.queryNowTime());
                record.setApplyWay(1);
                record.setUserid(paramMap.get("memberId").toString());
                i = orderDao.addEntity(record);
                if (i != null && Integer.valueOf(i.toString()) > 0) {
                    Integer id = Integer.valueOf(i.toString());
                    Order order = new Order();
                    order.setPaymentStatus(3);
                    order.setOrderNo(record.getOrderNo());
                    i = orderDao.updateOrder(order);
                    if (i != null && Integer.valueOf(i.toString()) > 0) {
                        // 退款申请模版未确定
                        Map<String, Object> sendMap = orderDao.queryApplyRecordById(id);
                        List<Map<String, Object>> roleList;
                        Map<String, Object> minStreamId = orderDao.getMinStreamId();
                        if (minStreamId != null) {
                            // 添加审核记录最底层信息
                            RefundAuditRecord auditRecord = new RefundAuditRecord();
                            auditRecord.setRecordId(id);
                            auditRecord.setCreateTime(Util.queryNowTime());
                            auditRecord.setState(0);
                            auditRecord.setOrderNo(record.getOrderNo());
                            auditRecord.setPid(Integer.valueOf(minStreamId.get("pid").toString()));
                            auditRecord.setStreamId(Integer.valueOf(minStreamId.get("id").toString()));
                            orderDao.addEntityUUID(auditRecord);
                            String[] userids = minStreamId.get("memberIds").toString().split(",");
                            roleList = orderDao.queryAuditUsers(userids, menuId);
                        } else {
                            roleList = orderDao.queryApplyRoleIds(-1);
                        }
                        if (roleList != null) {
                            for (Map<String, Object> map : roleList) {
                                if (map != null && sendMap != null) {
                                    sendMap.put("sendMember", "0");
                                    sendMap.put("openid", map.get("openId"));
                                    sendMap.put("operaName", map.get("realName"));
                                    sendMap.put("loginname", map.get("phone"));
                                    sendMap.put("token", "");
                                    sendMap.put("noticeRemark", "请及时处理");
                                    sendMap.put("first", "您好，有一个新的退款申请待处理。");
                                }
                                pushService.sendApplyRefundMsg(sendMap);
                            }
                        }
                    }
                } else {
                    return Util.resultMap(configCode.code_1036, null);
                }
            } else {
                return Util.resultMap(configCode.code_1019, null);
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Util.resultMap(configCode.code_1066, null);
        }
        return Util.resultMap(configCode.code_1001, i);
    }

    @Override
    public Integer tbGhMoneyToHis() {
        List<Map<String, Object>> list = super
                .queryBysqlList(
                        "SELECT o.orderNo FROM t_order o WHERE o.paymentStatus IN (2, 3, 5) AND FROM_UNIXTIME(payTime, '%Y-%m-%d') > '2018-09-12' AND o.receiptsPrice > 0.0 AND o.orderType IN (4, 7) AND o.orderNo<>'PHONE-T20180913000744' order by payTime ",
                        null);
        for (Map<String, Object> map : list) {
            try {
                Util.addHisServer(map.get("orderNo").toString(), orderDao);
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage());
            }
        }
        return 1;
    }

    private void pushSms(Map<String, Object> order, OrderDao orderDao) {
        String nowDate = DateTime.getDate();
        String nowMonth = DateTime.getNowMonth();
        String firstDate = DateTime.getFirstDateForMonth();
        try {
            order = orderDao.queryRecordOrderSmsByMainOrderNo(order.get("orderNo").toString());
            if (order != null) {
                Map<String, Object> sysMap = orderDao.getSysSet();
                String minMonths = sysMap == null || StringUtil.isEmpty(sysMap.get("basicMonth")) ?
                        "2018-08" : sysMap.get("basicMonth").toString();
                String maxDate = sysMap == null || StringUtil.isEmpty(sysMap.get("maxDate")) ?
                        "2018-12-31" : sysMap.get("maxDate").toString();
                String kfPhone = sysMap == null || StringUtil.isEmpty(sysMap.get("serverPhone")) ?
                        "400-667-0099" : sysMap.get("serverPhone").toString();
                if (order != null && nowDate.compareTo(maxDate) <= 0) {
                    if (!StringUtil.isEmpty(order.get("doctorId"))) {
                        String orderNo = order.get("orderNo").toString();
                        Map<String, Object> follow = orderDao.queryFollowIdByOpenId(order.get("doctorId").toString());
                        Map<String, Object> docMap = orderDao.queryDoctorOne(order.get("doctorId").toString());
                        String srPhone = follow == null || StringUtil.isEmpty(follow.get("phone")) ?
                                null : follow.get("phone").toString();
                        String srName = follow == null || StringUtil.isEmpty(follow.get("srName")) ?
                                null : follow.get("srName").toString();
                        String docPhone = docMap == null || StringUtil.isEmpty(docMap.get("docPhone")) ?
                                null : docMap.get("docPhone").toString();
                        String docName = docMap == null || StringUtil.isEmpty(docMap.get("docName")) ?
                                null : docMap.get("docName").toString();
                        Integer isSd = orderDao.queryRecordCountByDoctorId(order.get("doctorId").toString(),
                                order.get("orderNo").toString());
                        Integer sendDoctor = orderDao.queryIsSendByPhone(nowMonth, docPhone);
                        if (sendDoctor == 0) {
                            if (isSd <= 0) {
                                if (Double.valueOf(order.get("receiptsPrice").toString()) >= 100) {
                                    // 首单短信提醒大于100
                                    sendPhoneSms(orderDao, docName, null, docPhone, SMSTemplateIdUtil.cy100doc_SMS,
                                            nowMonth, nowDate, 0, orderNo, kfPhone);
                                    sendPhoneSms(orderDao, docName, srName, srPhone, SMSTemplateIdUtil.cy100sales_SMS,
                                            nowMonth, nowDate, 0, orderNo, null);
                                } else {
                                    // 首单短信提醒小于100
                                    sendPhoneSms(orderDao, docName, null, docPhone, SMSTemplateIdUtil.wcy100doc_SMS,
                                            nowMonth, nowDate, 1, orderNo, kfPhone);
                                    sendPhoneSms(orderDao, docName, srName, srPhone, SMSTemplateIdUtil.wcy100sales_SMS,
                                            nowMonth, nowDate, 1, orderNo, null);
                                }
                            } else {
                                Map<String, Object> basicMoney = orderDao.queryTotalMoney(order.get("doctorId").toString(),
                                        minMonths, null, null);
                                Map<String, Object> nowMoney = orderDao.queryTotalMoney(order.get("doctorId").toString(),
                                        null, nowDate, firstDate);
                                Double basicprice = basicMoney == null || StringUtil.isEmpty(basicMoney.get("receiptsPrice")) ?
                                        0.0 : Double.valueOf(basicMoney.get("receiptsPrice").toString());
                                Double nowprice = nowMoney == null || StringUtil.isEmpty(nowMoney.get("receiptsPrice")) ?
                                        0.0 : Double.valueOf(nowMoney.get("receiptsPrice").toString());
                                String syMonth = DateTime.getSyMonth();
                                if (syMonth.equals(minMonths)) {
                                    if (nowprice >= basicprice) {
                                        // 超越上月
                                        sendPhoneSms(orderDao, docName, null, docPhone, SMSTemplateIdUtil.cySyMonthdoc_SMS,
                                                nowMonth, nowDate, 2, orderNo, kfPhone);
                                        sendPhoneSms(orderDao, docName, srName, srPhone, SMSTemplateIdUtil.cySyMonthsales_SMS,
                                                nowMonth, nowDate, 2, orderNo, null);
                                    }
                                } else {
                                    Map<String, Object> syMoney = orderDao.queryTotalMoney(order.get("doctorId").toString(),
                                            syMonth, null, null);
                                    Double syprice = syMoney == null || StringUtil.isEmpty(syMoney.get("receiptsPrice")) ?
                                            0.0 : Double.valueOf(syMoney.get("receiptsPrice").toString());
                                    if (nowprice >= syprice) {
                                        if (syprice >= basicprice) {
                                            sendPhoneSms(orderDao, docName, null, docPhone, SMSTemplateIdUtil.cySyMonthdoc_SMS,
                                                    nowMonth, nowDate, 2, orderNo, kfPhone);
                                            sendPhoneSms(orderDao, docName, srName, srPhone, SMSTemplateIdUtil.cySyMonthsales_SMS,
                                                    nowMonth, nowDate, 2, orderNo, null);
                                        } else {
                                            if (nowprice >= basicprice) {
                                                sendPhoneSms(orderDao, docName, null, docPhone, SMSTemplateIdUtil.cySyMonthdoc_SMS,
                                                        nowMonth, nowDate, 2, orderNo, kfPhone);
                                                sendPhoneSms(orderDao, docName, srName, srPhone, SMSTemplateIdUtil.cySyMonthsales_SMS,
                                                        nowMonth, nowDate, 2, orderNo, null);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }
        } catch (Exception e) {
            logger.error("短息提醒异常：{}", e.getCause());
        }
    }

    private static boolean sendPhoneSms(OrderDao orderDao, String docName, String srName, String phone, Integer smsId,
                                        String nowMonth, String nowDate, Integer type, String orderNo, String kfPhone) {
        Boolean flag = false;
        try {
            if (!StringUtil.isEmpty(phone) && !StringUtil.isEmpty(smsId)) {
                if (StringUtil.isEmpty(srName)) {
                    String[] params = {docName, kfPhone};
                    flag = QCloudSmsUtil.sendSmsByTemId(phone, smsId, params);
                } else {
                    String[] params = {srName, docName};
                    flag = QCloudSmsUtil.sendSmsByTemId(phone, smsId, params);
                }
                if (flag) {
                    orderDao.insertSendSmsRecord(nowMonth, nowDate, phone,
                            type, orderNo);
                }
            }
        } catch (Exception e) {
            logger.error("sendPhoneSms：{}", e);
        }

        return flag;
    }

    private boolean sendServerSmsNotice(Integer smsId, Integer orderType, String patientName, String phone) {
        try {
            if (!StringUtil.isEmpty(smsId) && !StringUtil.isEmpty(orderType) && !StringUtil.isEmpty(patientName) &&
                    !StringUtil.isEmpty(phone)) {
                String serverName = "";
                if (orderType == 4) {
                    serverName = "图文调理";
                } else if (orderType == 6) {
                    serverName = "图文咨询";
                } else if (orderType == 7) {
                    serverName = "电话调理";
                } else if (orderType == 8) {
                    serverName = "电话咨询";
                }
                String[] params = {serverName, patientName};
                logger.info("购买服务短息提醒参数=[serverName={}；patientName={}]；医生电话={}；模版ID={}",
                        serverName, patientName, phone, smsId);
                return QCloudSmsUtil.sendSmsByTemId(phone, smsId, params);
            }
            return false;
        } catch (Exception e) {
            logger.error("sendServerSmsNotice：{}", e);
        }
        return false;
    }

}