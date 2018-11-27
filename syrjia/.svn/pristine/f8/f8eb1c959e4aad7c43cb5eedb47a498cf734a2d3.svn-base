package cn.syrjia.weixin.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.http.client.ClientProtocolException;

import cn.syrjia.util.JsonUtil;

public class WxMaterialUtil {
	
	/**
	 * 新增永久素材url
	 */
	private static final String addMaterialUri = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=";

	public static Map<String, Object> postFile(String token,String type, String filePath) {
		File file = new File(filePath);
		if (!file.exists())
			return null;
		String result = null;
		String url = addMaterialUri+token+"&type="+type;
		try {
			URL url1 = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(30000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Cache-Control", "no-cache");
			String boundary = "-----------------------------"
					+ System.currentTimeMillis();
			conn.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + boundary);

			OutputStream output = conn.getOutputStream();
			output.write(("--" + boundary + "\r\n").getBytes());
			output.write(String
					.format("Content-Disposition: form-data; name=\"media\"; filename=\"%s\"\r\n",
							file.getName()).getBytes());
			output.write("Content-Type: image/jpeg \r\n\r\n".getBytes());
			byte[] data = new byte[1024];
			int len = 0;
			FileInputStream input = new FileInputStream(file);
			while ((len = input.read(data)) > -1) {
				output.write(data, 0, len);
			}
			output.write(("\r\n--" + boundary + "\r\n\r\n").getBytes());
			output.flush();
			output.close();
			input.close();
			InputStream resp = conn.getInputStream();
			StringBuffer sb = new StringBuffer();
			while ((len = resp.read(data)) > -1)
				sb.append(new String(data, 0, len, "utf-8"));
			resp.close();
			result = sb.toString();
			System.out.println(result);
		} catch (ClientProtocolException e) {
			System.out.println("postFile，不支持http协议");
			System.out.println(e);
		
		} catch (IOException e) {
			System.out.println("postFile数据传输失败");
			System.out.println(e);
		}
		System.out.println(result);
		JSONObject  jasonObject = JSONObject.fromObject(result);
		Map<String, Object> map = JsonUtil.jsonToMap(jasonObject);
		return map;
	}

	public static void main(String[] args) {
		postFile("bHcjw2HXt9Ev9a57G_deyar2lbuNpNohvkAUZ5D4udgzX4zIKibLCj3_-JcxXD4Y5RbIIeCzmKLu0Vjk_b5Zqn1vTMw41VrLFu9368xGqREwRwPLV2xpKEbSZbBZjClMRDRhAAAXJX","image", "D:/cs.jpg");
	}

}
