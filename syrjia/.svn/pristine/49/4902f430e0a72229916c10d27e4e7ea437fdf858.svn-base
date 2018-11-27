package cn.syrjia.util.alipay.config;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.3
 *日期：2012-08-10
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
	
 *提示：如何获取安全校验码和合作身份者ID
 *1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *2.点击“商家服务”(https://b.alipay.com/order/myOrder.htm)
 *3.点击“查询合作者身份(PID)”、“查询安全校验码(Key)”

 *安全校验码查看时，输入支付密码后，页面呈灰色的现象，怎么办？
 *解决方法：
 *1、检查浏览器配置，不让浏览器做弹框屏蔽设置
 *2、更换浏览器或电脑，重新登录查询。
 */

public class AlipayConfig {
	
	//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	// 合作身份者ID，以2088开头由16位纯数字组成的字符串
	public static String partner = "2088031866894148";
	
	public static String appId="2018041802578366";
	
	public static String account="hkyyunying@126.com";
	
	public static String md5_key="be0vo3ife38in4tr1acyuom3mhsl79c5";
	
	public static String AES_key = "agQ7dxgiWuNAH9Unmr73Kg==";

	// 商户的私钥
	public static String private_key = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCf3qCab21GjEzvgU64GKSh8cC18129Bn5O7LWPbv9tN/Is9f+7B25tnz2QeRR5T1hkCt9pLCxrqwpOHpUrdeuNOMSr8/biAOUHae0PmIF5XudLDZtAX6uHeC0R4MNC81JQN23jKDw2JdEs0buMdQyoIYh4gaDG6rE9sabWyjj4JnnzYo8LZ7Av9qSP4Lim5fHlVbRMnc0+AI4w7JWlVPjMOehEtN3BP2046ETvMvdSMRe0bzeT+u+4p+qwnjrenE9f2sEDQxp0HGs5181vr7NsO4jkmZuL2rHMd0LoPhlkSbj9NJHGR8+U8KTkF+/t9/LlTI915wKLibVixMT6hfbPAgMBAAECggEAME1aZu7NrGgNgrul5heD/m4FlLPgGZGRrhT00k5uHDiwEgeNaZvYMITRXApJNlHT2f4y/BrdnPZDmdDBryzkq3mAape+exBcIn5Nos2oKNyUs2d8BdoiAD8+Fs27EUplD1l/+QcaEh4g8hK4QLrIxS/EhOxdgFw+iu5XA0R8K/Ux1EV78IHkbkEA0986YMXyrbDvzy58KCZ91ommVPT+cEHiSukaPIisp1Ccs8nU63MNO06MsMBK/GKXMEwYJKBkYhYP0X9Nrp0exTsnKTPm3VjffkTLLfRDUw9gFyahg7Z1E3xVTgY/1G7/FGKfywgBrc5ECvnJmeW2KeQ0VH53GQKBgQDOOSsr3Li7YNThheuNinwRmdK7ZII1vN6O3ZmhIAW6vKqkJDF9R+gCmgSKdPt3lmvsL+ezwPjMTkDYuMZhjbDr9EvewzADx6M7+ee5w/UlcL8JHAM+Q86J816z/XkkmJrCEoO680qtUw+wBEcRGsesW4ugSp+Ld9uV0KTP0w2fnQKBgQDGdTMAfDf5jVHKbDpBfY4fSbgxao3P3XX0wy0j/KL9Mc2tsSdZUPQb8zTriVLO2mYE8mmmWk6Jfc0R2m1FvUErdXSqbhyj+M5+5gh0hw/yicGoET4bkYPlqYJt0B02Ie5HX1cx1lE17QfC/Zt5Cs9nrTe2LS6On765EJM5/XsCWwKBgAh0gZefyhcspi3E7t5zwhh6pRA307reToWvAkB/7pw0Zg6qyTiovRN2Ox9uIm1Jxoz9fdN54vmFb9Uy5jdTtuBFuOKeQBa3lbK80UplhWSOldaKxntEDnDLgKOCjff5Jcc1OR9uIC6xDCCcXEmQPoWLj1L4HEh8lo0y6771mUZlAoGAU0pCwnRfxaNy7UKo3G2GZUk6Pbsbk169r6DuZAkWmq75RGlyO0EHgmd7EVNL5rawVGksv/1ssrWSiGikrb01Cbk84n5eFM9KUuXWLJm2rrL0DMZXy5gxxAR1UgMjD2IEL/4qiScKZFh7hnGLe6x2yWtgU1v5CzCuCduT/qWMp7sCgYBaLoLxXXTo+TWNme/ultwfmJYZoFuaiySnBYdBHe3OlWEZnGHQHyMP8aH9WYfKb27tZKbEJ2g91/sKzoOTPkNTlRjV1IdUnv8avwuPL7qVNjY0B6+TNQX7tE75dNciJ2kMUxdF7r3AKR5qVTUUubMWK/73j9zVsXXA2JeIVnkWlg==";
	
	/**商户公钥**/
	public static String public_key="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAn96gmm9tRoxM74FOuBikofHAtfNdvQZ+Tuy1j27/bTfyLPX/uwdubZ89kHkUeU9YZArfaSwsa6sKTh6VK3XrjTjEq/P24gDlB2ntD5iBeV7nSw2bQF+rh3gtEeDDQvNSUDdt4yg8NiXRLNG7jHUMqCGIeIGgxuqxPbGm1so4+CZ582KPC2ewL/akj+C4puXx5VW0TJ3NPgCOMOyVpVT4zDnoRLTdwT9tOOhE7zL3UjEXtG83k/rvuKfqsJ463pxPX9rBA0MadBxrOdfNb6+zbDuI5Jmbi9qxzHdC6D4ZZEm4/TSRxkfPlPCk5Bfv7ffy5UyPdecCi4m1YsTE+oX2zwIDAQAB";
	
	// 支付宝的公钥，无需修改该值
	public static String ali_public_key  ="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwCTqnXt2Hzt/r+9XzT27g5hgIUa4ydx5ucad1wAqL/lvU+3+kkbzRas6DIkKJdI7buxBD8rwgQxHxBCav/aJRa43RYIhdfUnrzPkRWi/TSzijJukDjtbnBmvZaDtj5JicawHBso846LfPnLUL128bcEs5T/JCPnSD3XqKc6JqdZDGnTQ7UHlmMk6VG+7/9/Luq/0ethnzRneHDwzKI3WjKRqpnE35QkGPUMdBwTv7ewX5zVoHr1XpprukEX9q68hi+898Aa3eGgmX6ovy68pKGqJTvOLfAchZzhp3dpBOkNXvZqJaHcouRcsu6YidpunZvi9q9+5Ao6Weeu44Xig0QIDAQAB"; 

	/**
	 * 
	 */
	public static String aliPay="https://openapi.alipay.com/gateway.do";
	
	
	/**
	 * 成功后回调的地址(异步)
	 */
	public static String notify_url="https://mobile.syrjia.com/syrjia/alipay/paySuccess.action";
	
	/**
	 * APP成功后回调的地址(异步)
	 */
	public static String app_notify_url="https://mobile.syrjia.com/syrjia/alipay/appPaySuccess.action";
	
	/**
	 * 成功后回调的地址
	 */
	public static String return_url="https://mobile.syrjia.com/syrjia/alipay/returnPaySuccess.action";
	
	
	//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
	

	// 调试用，创建TXT日志文件夹路径
	public static String log_path = "D:\\";

	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = "utf-8";
	
	// 签名方式 不需修改
	public static String sign_type = "RSA2";
	
	public static String tradeSuccess = "TRADE_SUCCESS";
	
	public static String tradeFail = "TRADE_CLOSED";
	
	// 返回格式
	public static String FORMAT = "json";

}
