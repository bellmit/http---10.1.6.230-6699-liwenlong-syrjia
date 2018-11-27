package cn.syrjia.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;


public class MSGUtil {

	public static String operate(Map<String,Object> m){
		String inputline = null;
		// 创建StringBuffer对象用来操作字符串
		StringBuffer sb = new StringBuffer("http://api.cnsms.cn/?");
		
		// 向StringBuffer追加用户名
		sb.append("ac=send&uid=103280");
		
		// 向StringBuffer追加密码（密码采用MD5 32位 小写）
		MD532Bit md5 = new MD532Bit();
		String pwd = md5.encryption("yingmaiwenhua12345678");
		sb.append("&pwd="+pwd);

		// 向StringBuffer追加手机号码
		String phone = m.get("phone").toString();
		sb.append("&mobile="+phone);

		// 向StringBuffer追加消息内容转URL标准码
		String content = m.get("content").toString();
		sb.append("&content="+URLEncoder.encode(content));

		// 创建url对象
		URL url;
		try {
			url = new URL(sb.toString());
			// 打开url连接
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			// 设置url请求方式 ‘get’ 或者 ‘post’
			connection.setRequestMethod("POST");

			// 发送
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

			// 返回发送结果
			inputline = in.readLine();

			// 返回结果为‘100’ 发送成功
			System.out.println(inputline);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return inputline;

	}
		
}
