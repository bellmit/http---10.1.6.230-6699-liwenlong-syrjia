package cn.syrjia.weixin.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class HttpGetHandler {
	
	public static JsonObject HttpGet(HttpClient client,String url,String can){
		HttpGet get = new HttpGet(url+can);
		JsonParser jsonparer = new JsonParser();// 初始化解析json格式的对象
		HttpResponse res;
		try {
			res = client.execute(get);
			String responseContent = null; // 响应内容
			HttpEntity entity = res.getEntity();
			responseContent = EntityUtils.toString(entity, "UTF-8");
			JsonObject json = jsonparer.parse(responseContent)
					.getAsJsonObject();
			return json;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}
}
