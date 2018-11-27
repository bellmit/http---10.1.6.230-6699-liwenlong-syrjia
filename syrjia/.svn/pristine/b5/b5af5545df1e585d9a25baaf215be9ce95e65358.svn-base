package cn.syrjia.security;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import cn.syrjia.callCenter.util.CallCenterConfig;
import cn.syrjia.config.configCode;
import cn.syrjia.service.AppService;
import cn.syrjia.util.StringUtil;
import cn.syrjia.util.Util;
import cn.syrjia.weixin.util.RequestHandler;

/**
 * 
 * @param <E>
 */
public class VerifyCallCenterInterceptor {

	@Autowired
	HttpServletRequest request;

	@Resource(name = "appService")
	AppService appService;
	
	/**
	 * 呼叫中心签名验证
	 * 
	 * @throws Throwable
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public Object verifyCallCenterSign(ProceedingJoinPoint joinPoint) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Object retVal;
		try {
			Map<String, String[]> params = request.getParameterMap();
			System.out.println(params+"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!request.getParameterMap();");
			String url = request.getRequestURL()+"";
			String userAgent = request.getHeader("user-agent")
					.toLowerCase();
			try {
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
				System.out.println(params+"$$$$$$params**********************************");
				try {
					System.out.println(params.get("sign")+"params.get()**********************************");
					System.out.println(params.get("sign")[0]+"%%%%%%%%%%%%%params.get()[0];");
				} catch (Exception e) {
					
				}
				String headerSign=request.getHeader("sign");
				String headerTimestamp=request.getHeader("timestamp");
				String centerkey = CallCenterConfig.callCenterKey;
				if(StringUtil.isEmpty(headerSign)||StringUtil.isEmpty(headerTimestamp)){
					if (StringUtil.isEmpty(params.get("sign"))
							|| StringUtil.isEmpty(params.get("timestamp"))) {
						return Util.resultMap(configCode.code_1106, null);
					}else{
						headerSign=params.get("sign")[0];
						headerTimestamp=params.get("timestamp")[0];
					}
				}

				//Integer timestamp=(int)(Long.parseLong(headerTimestamp)/1000);

				Integer time = Util.queryNowTime()- (int)(Long.parseLong(headerTimestamp));

				if (time < -120 || time > 120) {
					return Util.resultMap(configCode.code_1062, null);
				}

				SortedMap<String, String> packageParams = new TreeMap<String, String>();

				for (String key : params.keySet()) {
					if (!"timestamp".equals(key)) {
						for (String param : params.get(key)) {
							if(!StringUtil.isEmpty(param)){
								packageParams.put(key, param);
							}
						}
					}
				}
				
				packageParams.put("key", centerkey);
				packageParams.put("timestamp", headerTimestamp);

				RequestHandler reqHandler = new RequestHandler(null, null);
				reqHandler.init(null,null,centerkey);
				
				String sign = reqHandler.createSign(packageParams);

				System.out.println(params.get("sign")[0].toString());
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
	
/*	public static void main(String[] args) {
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("memberId", "5fe2269d55204167b2ecf5dba56a53776");
		packageParams.put("key", "00d7c0cd6d544f96bb765e7ccbe64d0c");
		packageParams.put("timestamp", "1526637934");

		RequestHandler reqHandler = new RequestHandler(null, null);
		reqHandler.init(null,null,"00d7c0cd6d544f96bb765e7ccbe64d0c");
		String sign = reqHandler.createSign(packageParams);
		System.out.println(sign);
	}
*/
}
