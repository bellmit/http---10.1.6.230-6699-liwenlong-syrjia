package cn.syrjia.security;

import cn.syrjia.config.configCode;
import cn.syrjia.util.GetOpenId;
import cn.syrjia.util.RedisUtil;
import cn.syrjia.util.StringUtil;
import cn.syrjia.util.Util;
import cn.syrjia.weixin.util.RequestHandler;
import net.sf.json.JSONObject;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class VerifyInterceptor {

    @Autowired
    HttpServletRequest request;

    /**
     * 签名验证
     */
    @SuppressWarnings({"unchecked"})
    public Object verifySign(ProceedingJoinPoint joinPoint) {
        String url = request.getRequestURL() + "";

        if (url.contains("/appSalesStat/index")) { // 请求助理统计首页面
            try {
                return joinPoint.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        Map<String, Object> returnMap = new HashMap<>();
        Object retVal;
        try {
            Map<String, String[]> params = request.getParameterMap();

            String userAgent = request.getHeader("user-agent").toLowerCase();
            boolean flag = url.indexOf("goods/queryMallActivityDetail") > 0
                    || url.indexOf("goods/queryGoodsById") > 0
                    || url.indexOf("evaluate/queryEvaluateRate") > 0
                    || url.indexOf("evaluate/queryEvaluateList") > 0
                    || url.indexOf("goodsShopCart/queryPostageByCityName") > 0
                    || url.indexOf("knowledgeCircle/queryBanner") > 0
                    || url.indexOf("knowledgeCircle/queryUnreadNum") > 0
                    || url.indexOf("knowledgeCircle/queryKnowledgeList") > 0
                    || url.indexOf("knowledgeCircle/queryClassList") > 0
                    || url.indexOf("goods/queryGoodsType") > 0
                    || url.indexOf("banner/queryBannerList") > 0
                    || url.indexOf("alipay/alipayWxPay") > 0
                    || url.indexOf("alipay/alipayAppPay") > 0
                    || url.indexOf("alipay/paySuccess") > 0
                    || url.indexOf("alipay/returnPaySuccess") > 0
                    /*|| url.indexOf("wx/wxJsApiPay") > 0*/
                    || url.indexOf("appDoctor/querySettlement") > 0
                    || url.indexOf("appDoctor/querySettlementTotal") > 0
                    || url.indexOf("order/queryLogistics") > 0
                    || url.indexOf("app/downApp") > 0
                    || url.indexOf("appDoctor/queryBankHistorys") > 0
                    || url.indexOf("appDoctor/queryBindBankById") > 0
                    || url.indexOf("appDoctor/queryBankList") > 0
                    || url.indexOf("appDoctor/editBindBank") > 0
                    || url.indexOf("answer/queryAnswerTypeList") > 0
                    || url.indexOf("goods/queryMallActivity") > 0
                    || url.indexOf("querySysSet") > 0
                    || url.indexOf("appDoctor/queryFollowBySrId") > 0
                    || url.indexOf("doctor/queryDoctorByCard") > 0
                    || url.indexOf("appDoctor/queryOrderQrCodeUrl") > 0
                    || url.indexOf("appSalesTraining") > 0
                    || url.indexOf("appSalesStat") > 0
                    || url.indexOf("order/queryPayOrderDetail") > 0
                    || url.indexOf("member/getMember") > 0;
            try {
                if (1==1||url.contains("syrjia/app/") || userAgent.contains("micromessenger") || flag) {
                    if (url.contains("syrjia/app/") || flag) {
                        return joinPoint.proceed();
                    }
					/*String memberId = StringUtil.isEmpty(request.getSession()
							.getAttribute("memberId")) ? null : request
							.getSession().getAttribute("memberId").toString();*/
                    String openid = StringUtil.isEmpty(request.getSession()
                            .getAttribute("openid")) ? null : request
                            .getSession().getAttribute("openid").toString();
                    Object memberId = null;
                    try {
                        Object obj = RedisUtil.getVal(openid);
                        if (!StringUtil.isEmpty(obj)) {//如果已有toeken则把旧token返回
                            JSONObject json = JSONObject.fromObject(obj);
                            memberId = json.has("memberId") ? json.get("memberId") : null;
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    System.out.println(memberId + "验签接口获取的openId");
                    try {
                        if (StringUtil.isEmpty(memberId)) {
                            memberId = StringUtil.isEmpty(request.getSession()
                                    .getAttribute("openid")) ? null : request
                                    .getSession().getAttribute("openid").toString();
                            if (StringUtil.isEmpty(memberId)) {
                                String code = StringUtil.isEmpty(request.getSession()
                                        .getAttribute("code")) ? null : request
                                        .getSession().getAttribute("code").toString();
                                System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@" + code
                                        + "验签接口获取SESSION中的code");
                                if (StringUtil.isEmpty(memberId) && !StringUtil.isEmpty(code)) {
                                    request.getSession().setAttribute("code", code);
                                    cn.syrjia.wxPay.wxPay.util.http.HttpRequest httpRequests = new cn.syrjia.wxPay.wxPay.util.http.HttpRequest();
                                    String openId = GetOpenId.getOpenId(httpRequests, request, code, null);
                                    //request.getSession().setAttribute("memberId", openId);
                                    JSONObject json = new JSONObject();
                                    json.set("memberId", openId);
                                    RedisUtil.setVal(openId, 60 * 60 * 24 * 365, json.toString());
                                    //RedisUtil.setVal(openId,openId);//设置token超时时间2个小时
                                }
                            } else {
                                JSONObject json = new JSONObject();
                                json.set("memberId", memberId);
                                RedisUtil.setVal(memberId.toString(), 60 * 60 * 24 * 365, json.toString());
                                //request.getSession().setAttribute("memberId", memberId);
                            }
                        }
                    } catch (Exception e) {
                        System.out.println(e + "请求接口重新获取openId异常");
                    }
                    return joinPoint.proceed();
                }
                Object[] args = joinPoint.getArgs();

                if (ServletFileUpload.isMultipartContent(request)) {
                    for (Object obj : args) {
                        if (obj instanceof DefaultMultipartHttpServletRequest) {
                            params = ((DefaultMultipartHttpServletRequest) obj)
                                    .getParameterMap();
                        }
                    }
                } else {
                    params = request.getParameterMap();
                }

                String headerSign = request.getHeader("sign");
                String headerTimestamp = request.getHeader("timestamp");

                if (StringUtil.isEmpty(headerSign)
                        || StringUtil.isEmpty(headerTimestamp)) {
                    if (StringUtil.isEmpty(params.get("sign"))
                            || StringUtil.isEmpty(params.get("timestamp"))) {
                        return Util.resultMap(configCode.code_1106, null);
                    } else {
                        headerSign = params.get("sign")[0];
                        headerTimestamp = params.get("timestamp")[0];
                    }
                }

                Integer timestamp = (int) (Long.parseLong(headerTimestamp) / 1000);
                // Integer time = Util.queryNowTime() - timestamp;
                /*
                 * if (time < -100 || time > 100) { return
                 * Util.resultMap(configCode.code_1062, null); }
                 */

                SortedMap<String, String> packageParams = new TreeMap<>();

                for (String key : params.keySet()) {
                    if (!"timestamp".equals(key) && !"_token".equals(key)) {
                        for (String param : params.get(key)) {
                            packageParams.put(key, param);
                        }
                    }
                }

                String _token = params.get("_token")[0];

                /*
                 * String memberId=appService.queryMemberToken(_token);
                 *
                 * if(StringUtil.isEmpty(memberId)){ return
                 * Util.resultMap(configCode.code_1077, null); }
                 */
                Object obj = RedisUtil.getVal(_token);

                if (StringUtil.isEmpty(obj)) {
                    return Util.resultMap(configCode.code_1078, null);
                }

                String _encry_token = JSONObject.fromObject(obj).getString("_encry_token");

                packageParams.put("timestamp", timestamp + "");

                RequestHandler reqHandler = new RequestHandler(null, null);
                reqHandler.init(null, null, _encry_token);

                String sign = reqHandler.createSign(packageParams);

                if (!sign.equals(headerSign)) {
                    return Util.resultMap(configCode.code_1061, null);
                }
                retVal = joinPoint.proceed();
            } catch (Throwable e) {
                returnMap.put("respCode", configCode.code_1015);
                returnMap.put("respMsg",
                        configCode.codeDesc(configCode.code_1015));
                return returnMap;
            }
        } catch (NumberFormatException e) {
            returnMap.put("respCode", configCode.code_1015);
            returnMap.put("respMsg", configCode.codeDesc(configCode.code_1015));
            return returnMap;
        }
        return retVal;
    }
}
