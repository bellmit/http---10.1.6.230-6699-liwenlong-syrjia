package cn.syrjia.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.syrjia.common.BaseDaoInterface;
import cn.syrjia.entity.AccessToken;
import cn.syrjia.entity.SendTempMsg;
import cn.syrjia.entity.TemplateData;

public class SendTempUtil {

	// 日志
	private static Logger logger = LogManager.getLogger(SendTempUtil.class);

	/**
	 * 发送模板消息URL
	 */
	private static final String tempUri = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

	/**
	 * 客服发送消息
	 */
	public static final String sendCustomUrl = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=";

	public static <T extends BaseDaoInterface> String sendMsg(SendTempMsg msg,
			T t) {
 		String jsonString = "";
		String acToken = t.queryToken();
		if(StringUtils.isEmpty(acToken)){
			GetAccessToken<T> getAccessToken = new GetAccessToken<T>(
					t);
			acToken = getAccessToken.getTokenAll();
		}
		String url = tempUri + acToken;// 发送模板消息请求url
		Map<String, Object> mStr = new HashMap<String, Object>();
		mStr.put("touser", msg.getTouser());
		mStr.put("template_id", msg.getTemplate_id());
		mStr.put("url", msg.getUrl());

		Map<String, TemplateData> m = new HashMap<String, TemplateData>();
		List<TemplateData> datalist = msg.getDatalist();
		int i = 0;
		for (TemplateData tdata : datalist) {
			String keyword = "keyword";
			if (i == datalist.size() - 1) {
				m.put("remark", tdata);
			} else if (i == 0) {
				m.put("first", tdata);
			} else {
				keyword = keyword + i;
				m.put(keyword, tdata);
			}
			i++;
		}
		mStr.put("data", m);
		String jsonStr = JSONObject.fromObject(mStr).toString();
		System.out.println("----------微信推送开始："+jsonStr);
		JSONObject jsonObject = HttpReuqest.httpRequest(url, "POST", jsonStr);
		int result = 0;
		if (null != jsonString) {
			if (0 != jsonObject.getInt("errcode")) {
				result = jsonObject.getInt("errcode");
				logger.error("错误 errcode:{} errmsg:{}",
						jsonObject.getInt("errcode"),
						jsonObject.getString("errmsg"));
			}
		}
		logger.info("模板消息发送结果：" + result);
		System.out.println("-------------发送结果："+ result);
		if("40001".equals(result)){
			GetAccessToken<T> getAccessToken = new GetAccessToken<T>(
					t);
			acToken = getAccessToken.getTokenAll();
			sendMsg(msg,t);
		}
		msg.setUrl(msg.getUrl());
		msg.setData(jsonStr);
		jsonString = result + "";
		if (result == 0) {
			msg.setErrmsg("ok");
		} else {
			msg.setErrmsg("fail");
		}
		t.addEntity(msg);// 将发送记录存入本地数据库

		return jsonString;
	}

	/**
	 * 客服发送消息
	 * 
	 * @return
	 */
	public static <T extends BaseDaoInterface> String sendCustomMsg(
			Map<String, Object> params, T t) {
		String jsonString = "";

		if (params != null) {
			String acToken = t.queryOne(AccessToken.class, null)
					.getAccess_token();

			String reqUrl = sendCustomUrl + acToken;// 客服发送消息请求url
			Map<String, Object> mStr = new HashMap<String, Object>();
			mStr.put("touser", params.get("toUser"));
			mStr.put("msgtype", params.get("msgtype"));
			// 设置哪个客服发送
			Map<String, Object> customService = new HashMap<String, Object>();
			customService.put("kf_account", TemplateIdUtil.kf_account);
			mStr.put("customservice", customService);
			Map<String, Object> articles = new HashMap<String, Object>();
			
			String content =params.get("content").toString();
			if(params.get("url")!=null&&!"".equals(params.get("url"))&&!StringUtils.isEmpty(params.get("url").toString())){
				content += "\n<a href=\""+params.get("url").toString()+"\">点击查看详情</a>";
			}
			articles.put("content",content);
			
			/*articles.put("content",
					params.get("content")+"\n<a href=\""+params.get("url")+"\">点击查看详情</a>");*/
			mStr.put("text", articles);

			String jsonStr = JSONObject.fromObject(mStr).toString();
			JSONObject jsonObject = HttpReuqest.httpRequest(reqUrl, "POST",
					jsonStr);
			int result = 0;
			if (null != jsonString) {
				if (0 != jsonObject.getInt("errcode")) {
					result = jsonObject.getInt("errcode");
					logger.error("错误 errcode:{} errmsg:{}",
							jsonObject.getInt("errcode"),
							jsonObject.getString("errmsg"));
				}
			}
			logger.info("模板消息发送结果：" + result);
			jsonString = result + "";
			if("40001".equals(jsonString)){
				GetAccessToken<T> getAccessToken = new GetAccessToken<T>(
						t);
				acToken = getAccessToken.getTokenAll();
				sendCustomMsg(params,t);
			}
		}
		return jsonString;
	}
}
