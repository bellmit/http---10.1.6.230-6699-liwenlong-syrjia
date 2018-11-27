package cn.syrjia.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import net.sf.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import cn.syrjia.config.Config;
import cn.syrjia.weixin.util.MyX509TrustManager;

@Controller
public class HttpReuqest {
	
	static Config config;

	@Resource
	public void setConfig(Config config) {
		HttpReuqest.config = config;
	}

	private static Logger logger = LogManager.getLogger(HttpReuqest.class);

	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String sendGet(String url, String param) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url + "?" + param;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			for (String key : map.keySet()) {
				System.out.println(key + "--->" + map.get(key));
			}
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			logger.error(e);
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				logger.error(e2);
			}
		}
		return result;
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, JSONObject param) {
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
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(null==param?null:URLEncoder.encode(param.toString(),"UTF-8"));
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
			System.out.println("发送 POST 请求出现异常！" + e);
			logger.error(e);
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
				logger.error(ex);
			}
		}
		return result;
	}
	
	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendCallCenterPost(String url, JSONObject param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Charsert", "UTF-8");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(null==param?null:URLEncoder.encode(param.toString(),"UTF-8"));
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
			System.out.println("发送 POST 请求出现异常！" + e);
			logger.error(e);
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
				logger.error(ex);
			}
		}
		return result;
	}


	/**
	 * 
	 * 发起https请求并获取结果
	 * 
	 * 
	 * 
	 * @param requestUrl
	 *            请求地址
	 * 
	 * @param requestMethod
	 *            请求方式（GET、POST）
	 * 
	 * @param outputStr
	 *            提交的数据
	 * 
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */

	public static JSONObject httpRequest(String requestUrl,
			String requestMethod, String outputStr) {

		JSONObject jsonObject = null;

		StringBuffer buffer = new StringBuffer();

		try {

			// 创建SSLContext对象，并使用我们指定的信任管理器初始化

			TrustManager[] tm = { new MyX509TrustManager() };

			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");

			sslContext.init(null, tm, new java.security.SecureRandom());

			// 从上述SSLContext对象中得到SSLSocketFactory对象

			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);

			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url
					.openConnection();

			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);

			httpUrlConn.setDoInput(true);

			httpUrlConn.setUseCaches(false);

			// 设置请求方式（GET/POST）

			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod))

				httpUrlConn.connect();

			// 当有数据需要提交时

			if (null != outputStr) {

				OutputStream outputStream = httpUrlConn.getOutputStream();

				// 注意编码格式，防止中文乱码

				outputStream.write(outputStr.getBytes("UTF-8"));

				outputStream.close();

			}

			// 将返回的输入流转换成字符串

			InputStream inputStream = httpUrlConn.getInputStream();

			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "utf-8");

			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			String str = null;

			while ((str = bufferedReader.readLine()) != null) {

				buffer.append(str);

			}

			bufferedReader.close();

			inputStreamReader.close();

			// 释放资源

			inputStream.close();

			inputStream = null;

			httpUrlConn.disconnect();

			jsonObject = JSONObject.fromObject(buffer.toString());

		} catch (ConnectException ce) {

		} catch (Exception e) {

		}

		return jsonObject;

	}
	
	public static Map<String, Object> httpPost(String fName,
			MultipartFile imgFiles) {

		try {
			URL url = new URL(config.getUploadService()+"?fileName="+imgFiles.getOriginalFilename()+"&fName="+fName);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 发送POST请求必须设置如下两行

			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "text/html");
			conn.setRequestProperty("Cache-Control", "no-cache");
			conn.setRequestProperty("Charsert", "UTF-8");
			conn.connect();
			conn.setConnectTimeout(10000);
			OutputStream out = conn.getOutputStream();

			DataInputStream in = new DataInputStream(imgFiles.getInputStream());

			int bytes = 0;
			byte[] buffer = new byte[1024];
			while ((bytes = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytes);
			}
			in.close();
			out.flush();
			out.close();
			int res = conn.getResponseCode();
			InputStream ins = null;
			// 上传成功返回200
			if (res == 200) {
				ins = conn.getInputStream();
				int ch;
				StringBuilder sb2 = new StringBuilder();
				// 保存数据
				while ((ch = ins.read()) != -1) {
					sb2.append((char) ch);
				}
				return JsonUtil.jsonToMap(sb2.toString());
			}
		} catch (Exception e) {
			System.out.println("发送文件出现异常！" + e);
			e.printStackTrace();
		}
		return null;
	}
	
	public static Map<String, Object> httpPostCoutosQrCode(String imgUrl,String dir,String srId,String oldQrUrl) {

		try {
			String text = "https://mobile.syrjia.com/syrjia/weixin/register/doctor_register.html?srId="+srId;
			URL url = new URL(config.getUploadServiceFile()+"qrCode?content="+text+"&imgPath="+imgUrl+"&dir="+dir+"&oldQrUrl="+oldQrUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 发送POST请求必须设置如下两行

			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "text/html");
			conn.setRequestProperty("Cache-Control", "no-cache");
			conn.setRequestProperty("Charsert", "UTF-8");
			conn.connect();
			conn.setConnectTimeout(10000);
			int res = conn.getResponseCode();
			InputStream ins = null;
			// 上传成功返回200
			if (res == 200) {
				ins = conn.getInputStream();
				int ch;
				StringBuilder sb2 = new StringBuilder();
				// 保存数据
				while ((ch = ins.read()) != -1) {
					sb2.append((char) ch);
				}
				ins.close();
				return JsonUtil.jsonToMap(sb2.toString());
			}
		} catch (Exception e) {
			System.out.println("发送文件出现异常！" + e);
			e.printStackTrace();
		}
		return null;
	}
	
	public static Map<String, Object> httpPostQrCode(String accessToken,String localQrName,Integer time,String scene_str,String dir,String docId) {

		try {
			Map<String,Object> map = new HashMap<String, Object>();
	  		Map<String,Object> map1 = new HashMap<String, Object>();
	  		Map<String,Object> map2 = new HashMap<String, Object>();
	  		
	  		
	  		if(!StringUtil.isEmpty(time)){
	  			map.put("action_name", "QR_STR_SCENE");
	  			map2.put("scene_str", scene_str);
	  			map.put("expire_seconds", time);
	  		}else{//永久二维码
	  			map.put("action_name", "QR_LIMIT_STR_SCENE");
	  			map2.put("scene_str", scene_str);
	  		}
	  		map1.put("scene", map2);
	  		map.put("action_info", map1);
	  		String jsonStr = JSONObject.fromObject(map).toString();
	  		System.out.println(map+"jsonStr*********************************");
	  		JSONObject jsonObject = HttpReuqest.httpRequest("https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="+accessToken, "POST",
	  				jsonStr);
	  		System.out.println(jsonObject);
	  		String ticket = jsonObject.get("ticket").toString();
			URL url = new URL(config.getUploadServiceFile()+"createQrCode?ticket="+ticket+"&localQrName="+localQrName+"&dir="+dir+"&doctorId="+docId);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 发送POST请求必须设置如下两行

			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "text/html");
			conn.setRequestProperty("Cache-Control", "no-cache");
			conn.setRequestProperty("Charsert", "UTF-8");
			conn.connect();
			conn.setConnectTimeout(10000);
			int res = conn.getResponseCode();
			InputStream ins = null;
			// 上传成功返回200
			if (res == 200) {
				ins = conn.getInputStream();
				int ch;
				StringBuilder sb2 = new StringBuilder();
				// 保存数据
				while ((ch = ins.read()) != -1) {
					sb2.append((char) ch);
				}
				ins.close();
				return JsonUtil.jsonToMap(sb2.toString());
			}
		} catch (Exception e) {
			System.out.println("发送文件出现异常！" + e);
			e.printStackTrace();
		}
		return null;
	}
}
