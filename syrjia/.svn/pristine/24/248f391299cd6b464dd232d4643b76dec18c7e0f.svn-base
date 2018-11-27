package cn.syrjia.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

import cn.syrjia.common.BaseServiceInterface;
import cn.syrjia.wxPay.wxPay.util.WeiXinConfig;

public class WxScan {

	/**
	 * 调取微信分享时加密
	 * @param request
	 * @param url
	 * @return
	 */
	public static <T extends BaseServiceInterface> Map<String, Object> getSign(
			String url, T t) {
		Map<String, Object> ret = new HashMap<String, Object>();
		Map<String, Object> map = t.getTicket();
		String jsapi_ticket = map.get("ticket").toString();
		String timestamp = Sha1Util.getTimeStamp();
		String nonce_str = Sha1Util.getNonceStr();
        String string1;
        String signature = "";

        //注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapi_ticket +
                  "&noncestr=" + nonce_str +
                  "&timestamp=" + timestamp +
                  "&url=" + url;
        System.out.println(string1);
        System.out.println(url);

        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        ret.put("url", url);
        ret.put("jsapi_ticket", jsapi_ticket);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);
        ret.put("appId", WeiXinConfig.appId);
        return ret;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

}
