package cn.syrjia.util;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import cn.syrjia.common.BaseDaoInterface;
import cn.syrjia.entity.AccessToken;
import cn.syrjia.wxPay.wxPay.util.WeiXinConfig;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class GetAccessToken<T extends BaseDaoInterface> {
	
	private T t;
	
	public  GetAccessToken(T t) {
		this.t=t;
	}
	
	/**
	 * 获取微信access_token 方法
	 */
	public String getToken() {
		String turl = String.format(
				"%s?grant_type=client_credential&appid=%s&secret=%s", WeiXinConfig.tokenUri,
				WeiXinConfig.appId, WeiXinConfig.appSecret);
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(turl);
		JsonParser jsonparer = new JsonParser();// 初始化解析json格式的对象
		String accessToken =t.queryToken();
		if(!StringUtils.isEmpty(accessToken)){
			return accessToken;
		}
		try {
			HttpResponse res = client.execute(get);
			String responseContent = null; // 响应内容
			HttpEntity entity = res.getEntity();
			responseContent = EntityUtils.toString(entity, "UTF-8");
			JsonObject json = jsonparer.parse(responseContent)
					.getAsJsonObject();
			// 将json字符串转换为json对象
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				if (json.get("errcode") != null) {
					// 错误时微信会返回错误码等信息，{"errcode":40013,"errmsg":"invalid appid"}
					accessToken = "";
				} else {// 正常情况下{"access_token":"ACCESS_TOKEN","expires_in":7200}
					accessToken = json.get("access_token").getAsString();
				}
			}
			AccessToken token = new AccessToken();
			if(!StringUtils.isEmpty(accessToken)){
				//AccessToken tokens=t.queryById(AccessToken.class,1);
				token.setAccess_token(accessToken);
				token.setAccount_id("1");
				token.setAddTime((System.currentTimeMillis()/1000)+"");
				token.setExpires_in(7200+"");
				//if(null==tokens||"".equals(tokens.getAccount_id())){
				//	t.addEntity(token);
				//}else{
					int i=t.updateEntity(token);
					while(i==0){
						i=t.updateEntity(token);
					}
				//}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭连接 ,释放资源
			client.getConnectionManager().shutdown();
		}
		
		return accessToken;
	}
	
	
	/**
	 * 获取微信access_token 方法
	 * 2017-03-09项目启动时调用此获取token方法
	 */
	public String getTokenAll() {
		String turl = String.format(
				"%s?grant_type=client_credential&appid=%s&secret=%s", WeiXinConfig.tokenUri,
				WeiXinConfig.appId, WeiXinConfig.appSecret);
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(turl);
		JsonParser jsonparer = new JsonParser();// 初始化解析json格式的对象
		String accessToken ="";
		try {
			HttpResponse res = client.execute(get);
			String responseContent = null; // 响应内容
			HttpEntity entity = res.getEntity();
			responseContent = EntityUtils.toString(entity, "UTF-8");
			JsonObject json = jsonparer.parse(responseContent)
					.getAsJsonObject();
			// 将json字符串转换为json对象
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				if (json.get("errcode") != null) {
					// 错误时微信会返回错误码等信息，{"errcode":40013,"errmsg":"invalid appid"}
					accessToken = "";
				} else {// 正常情况下{"access_token":"ACCESS_TOKEN","expires_in":7200}
					accessToken = json.get("access_token").getAsString();
				}
			}
			AccessToken token = new AccessToken();
			if(!StringUtils.isEmpty(accessToken)){
				//AccessToken tokens=t.queryById(AccessToken.class,1);
				token.setAccess_token(accessToken);
				token.setAccount_id("1");
				token.setAddTime((System.currentTimeMillis()/1000)+"");
				token.setExpires_in(7200+"");
				//if(null==tokens||"".equals(tokens.getAccount_id())){
					//t.addEntity(token);
				//}else{
					int i=t.updateEntity(token);
					while(i==0){
						i=t.updateEntity(token);
					}
				//}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭连接 ,释放资源
			client.getConnectionManager().shutdown();
		}
		
		return accessToken;
	}
}
