package cn.syrjia.wxPay.wxPay.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

/**
 * User: rizenguo Date: 2014/10/23 Time: 14:59
 */
public class Util {
	
	public static String getIp(HttpServletRequest request) {
		String ipAddress = null;
		try {
			ipAddress = null;
			// ipAddress = this.getRequest().getRemoteAddr();
			ipAddress = request.getHeader("x-forwarded-for");
			if (ipAddress == null || ipAddress.length() == 0
					|| "unknown".equalsIgnoreCase(ipAddress)) {
				ipAddress = request.getHeader("Proxy-Client-IP");
			}
			if (ipAddress == null || ipAddress.length() == 0
					|| "unknown".equalsIgnoreCase(ipAddress)) {
				ipAddress = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ipAddress == null || ipAddress.length() == 0
					|| "unknown".equalsIgnoreCase(ipAddress)) {
				ipAddress = request.getRemoteAddr();
				if (ipAddress.equals("127.0.0.1")
						|| "0:0:0:0:0:0:0:1".equals(ipAddress)) {
					// 根据网卡取本机配置的IP
					InetAddress inet = null;
					try {
						inet = InetAddress.getLocalHost();
					} catch (UnknownHostException e) {
						e.printStackTrace();
					}
					ipAddress = inet.getHostAddress();
				}
			}
			// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
			if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
																// = 15
				if (ipAddress.indexOf(",") > 0) {
					ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
				}
			}
		} catch (Exception e) {
			ipAddress = "Exception";
		}
		return ipAddress;
	}

	/**
	 * 通过反射的方式遍历对象的属性和属性值，方便调试
	 * 
	 * @param o
	 *            要遍历的对象
	 * @throws Exception
	 */
	public static void reflect(Object o) throws Exception {
		Class cls = o.getClass();
		Field[] fields = cls.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field f = fields[i];
			f.setAccessible(true);
			Util.log(f.getName() + " -> " + f.get(o));
		}
	}

	public static byte[] readInput(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int len = 0;
		byte[] buffer = new byte[1024];
		while ((len = in.read(buffer)) > 0) {
			out.write(buffer, 0, len);
		}
		out.close();
		in.close();
		return out.toByteArray();
	}

	public static String inputStreamToString(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i;
		while ((i = is.read()) != -1) {
			baos.write(i);
		}
		return baos.toString();
	}

	public static InputStream getStringStream(String sInputString) {
		ByteArrayInputStream tInputStringStream = null;
		if (sInputString != null && !sInputString.trim().equals("")) {
			tInputStringStream = new ByteArrayInputStream(
					sInputString.getBytes());
		}
		return tInputStringStream;
	}

	/*
	 * public static Object getObjectFromXML(String xml, Class tClass) { //
	 * 将从API返回的XML数据映射到Java对象 XStream xStreamForResponseData = new XStream();
	 * xStreamForResponseData.alias("xml", tClass);
	 * xStreamForResponseData.ignoreUnknownElements();// 暂时忽略掉一些新增的字段 return
	 * xStreamForResponseData.fromXML(xml); }
	 */

	public static String getStringFromMap(Map<String, Object> map, String key,
			String defaultValue) {
		if (key == "" || key == null) {
			return defaultValue;
		}
		String result = (String) map.get(key);
		if (result == null) {
			return defaultValue;
		} else {
			return result;
		}
	}

	public static int getIntFromMap(Map<String, Object> map, String key) {
		if (key == "" || key == null) {
			return 0;
		}
		if (map.get(key) == null) {
			return 0;
		}
		return Integer.parseInt((String) map.get(key));
	}

	/**
	 * 打log接口
	 * 
	 * @param log
	 *            要打印的log字符串
	 * @return 返回log
	 */
	public static String log(Object log) {

		// System.out.println(log);
		return log.toString();
	}

	/**
	 * 读取本地的xml数据，一般用来自测用
	 * 
	 * @param localPath
	 *            本地xml文件路径
	 * @return 读到的xml字符串
	 */
	public static String getLocalXMLString(String localPath) throws IOException {
		return Util.inputStreamToString(Util.class
				.getResourceAsStream(localPath));
	}

	public Map parseXmlToList2(String xml) {
		Map retMap = new HashMap();
		try {
			StringReader read = new StringReader(xml);
			// 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
			InputSource source = new InputSource(read);
			// 创建一个新的SAXBuilder
			SAXBuilder sb = new SAXBuilder();
			// 通过输入源构造一个Document
			Document doc = (Document) sb.build(source);
			Element root = doc.getRootElement();// 指向根节点
			List<Element> es = root.getChildren();
			if (es != null && es.size() != 0) {
				for (Element element : es) {
					retMap.put(element.getName(), element.getValue());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retMap;
	}

}
