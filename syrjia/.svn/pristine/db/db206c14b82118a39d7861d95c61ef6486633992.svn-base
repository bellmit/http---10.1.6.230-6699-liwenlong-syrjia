/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */
package cn.syrjia.wxPay.wxPay.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This example demonstrates how to create secure connections with a custom SSL
 * context.
 */
public class ClientCustomSSL {

	// 日志
	private static Logger logger = LogManager
			.getLogger(ClientCustomSSL.class);

	public static String doRefund(HttpServletRequest request, String url,
			String data,String addType) throws Exception {
		System.out.println("开始退款");
		String responseXML = "";
		String nowpath = System.getProperty("user.dir").replace("bin",
				"webapps");
		String path=request.getSession().getServletContext().getRealPath("/");
		/*String path = nowpath + request.getContextPath()
				+ File.separator + "credentials" + File.separator;*/
		System.out.println("路径：-------"+path);
		/**
		 * 注意PKCS12证书 是从微信商户平台-》账户设置-》 API安全 中下载的
		 */

		String apiPath =path+"credentials" + File.separator + "apiclient_cert.p12";
		String mch_id=WeiXinConfig.mch_id;
		if("app".equals(addType)){
			apiPath = path + "appcredentials" + File.separator+"apiclient_cert.p12";
			mch_id=WeiXinConfig.sub_mch_id;
		}
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		FileInputStream instream = new FileInputStream(new File(apiPath));// P12文件目录
		try {
			/**
			 * 此处要改
			 * */
			keyStore.load(instream, mch_id.toCharArray());// 这里写密码..默认是你的MCHID
		} finally {
			instream.close();
		}
		
		System.out.println("读取后：-------");

		// Trust own CA and all self-signed certs
		/**
		 * 此处要改
		 * */
		SSLContext sslcontext = SSLContexts.custom()
				.loadKeyMaterial(keyStore,mch_id.toCharArray())// 这里也是写密码的
				.build();
		// Allow TLSv1 protocol only
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
				sslcontext, new String[] { "TLSv1" }, null,
				SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		CloseableHttpClient httpclient = HttpClients.custom()
				.setSSLSocketFactory(sslsf).build();
		try {
			
			System.out.println("开始退款发起请求");
			HttpPost httpPost = new HttpPost(url);// 退款接口

			System.out.println("executing request" + httpPost.getRequestLine());
			StringEntity reqEntity = new StringEntity(data, "UTF-8");
			// 设置类型
			reqEntity.setContentType("application/x-www-form-urlencoded");
			httpPost.setEntity(reqEntity);
			CloseableHttpResponse response = httpclient.execute(httpPost);
			try {
				HttpEntity entity = response.getEntity();
				System.out.println("----------------------------------------");
				System.out.println(response.getStatusLine());
				if (entity != null) {
					System.out.println("Response content length: "
							+ entity.getContentLength());
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(entity.getContent(), "utf-8"));
					String text;
					while ((text = bufferedReader.readLine()) != null) {
						responseXML += text;
					}
				}
				EntityUtils.consume(entity);
			} catch (Exception e) {
				response.close();
			}
		} catch (Exception e) {
			httpclient.close();
		}
		System.out.println("退款结束返回：-----------"+responseXML);
		return responseXML;
	}
}
