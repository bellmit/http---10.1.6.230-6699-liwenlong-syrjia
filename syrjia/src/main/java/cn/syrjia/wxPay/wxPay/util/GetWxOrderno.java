package cn.syrjia.wxPay.wxPay.util;

import cn.syrjia.util.http.HttpClientConnectionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class GetWxOrderno {

	private Logger logger = LogManager.getLogger(GetWxOrderno.class);
	
	public  DefaultHttpClient httpclient;


	public  String getPayNo(String url, String xmlParam) {
		logger.debug("-----------getPayNo start--------url------"+url+"-----xmlParam-------"+xmlParam);
		httpclient = new DefaultHttpClient();
		httpclient = (DefaultHttpClient) HttpClientConnectionManager.getSSLInstance(httpclient);
		DefaultHttpClient client = new DefaultHttpClient();
		client.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS,true);
		HttpPost httpost = HttpClientConnectionManager.getPostMethod(url);
		String prepay_id = "";
		logger.debug("---------httpost------"+httpost);
		try {
			httpost.setEntity(new StringEntity(xmlParam, "UTF-8"));
			HttpResponse response = httpclient.execute(httpost);
			logger.debug("------------------response-----response-----------"+response);
			String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
			logger.debug("----------jsonStr------------------------------"+jsonStr);
			Map<String, Object> dataMap = new HashMap<String, Object>();
			logger.info(jsonStr+"-------------response");
			
			if (jsonStr.indexOf("FAIL") != -1) {
				logger.debug("----------jsonStr-----1111111-------------------------"+prepay_id);
				return prepay_id;
			}
			Map map = doXMLParse(jsonStr);
			
			String return_code = (String) map.get("return_code");
			
			prepay_id = (String) map.get("prepay_id");
			
			logger.debug("----return_code------prepay_id------------------------------"+return_code+"---"+prepay_id);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.debug("-------getPayNo---Exception--eeeeeeeeeeeee-----"+e);
			logger.debug("-------getPayNo---Exception-------"+e.getMessage());
			e.printStackTrace();
		}
		return prepay_id;
	}

	/**
	 * 解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。
	 * 
	 * @param strxml
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 */
	public static Map doXMLParse(String strxml) throws Exception {
		if (null == strxml || "".equals(strxml)) {
			return null;
		}

		Map m = new HashMap();
		InputStream in = String2Inputstream(strxml);
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(in);
		Element root = doc.getRootElement();
		List list = root.getChildren();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Element e = (Element) it.next();
			String k = e.getName();
			String v = "";
			List children = e.getChildren();
			if (children.isEmpty()) {
				v = e.getTextNormalize();
			} else {
				v = getChildrenText(children);
			}

			m.put(k, v);
		}

		// �ر���
		in.close();

		return m;
	}

	/**
	 * 获取子结点的xml
	 * 
	 * @param children
	 * @return String
	 */
	public static String getChildrenText(List children) {
		StringBuffer sb = new StringBuffer();
		if (!children.isEmpty()) {
			Iterator it = children.iterator();
			while (it.hasNext()) {
				Element e = (Element) it.next();
				String name = e.getName();
				String value = e.getTextNormalize();
				List list = e.getChildren();
				sb.append("<" + name + ">");
				if (!list.isEmpty()) {
					sb.append(getChildrenText(list));
				}
				sb.append(value);
				sb.append("</" + name + ">");
			}
		}

		return sb.toString();
	}

	public static InputStream String2Inputstream(String str) {
		return new ByteArrayInputStream(str.getBytes());
	}

}
