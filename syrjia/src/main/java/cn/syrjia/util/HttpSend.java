package cn.syrjia.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import net.sf.json.JSONObject;

public class HttpSend {
	//private static String doctorUrl = "http://mobile.syrjia.cn/webApnsDoctor/apns";
	//private static String srUrl = "http://mobile.syrjia.cn/webApnsSr/apns";
	private static String doctorUrl = "http://push.syrjia.com/webApnsDoctor/apns";
	private static String srUrl = "http://push.syrjia.com/webApnsSr/apns";
	public static String HttpPost(String url, String optype,
			String buffer1) {

		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Charset", "UTF-8");  
			conn.setRequestProperty("optype", optype);
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			String xx  = buffer1.toString();
			// 发送请求参数
			out.print(buffer1);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("发送 POST 请求出现异常！" + e);
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;

	}

	
	
	public static String HttpPost2(Map<String, Object> map) {
		String r = null;
		try {
			r = HttpReuqest.sendPost(doctorUrl, JSONObject.fromMap(map));
			System.out.println("推送结果"+r);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
		return r;
	}
	
	public static String HttpPost3(Map<String, Object> map) {
		String r = null;
		try {
			r = HttpReuqest.sendPost(srUrl, JSONObject.fromMap(map));
			System.out.println("推送结果"+r);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
		return r;
	}

}
