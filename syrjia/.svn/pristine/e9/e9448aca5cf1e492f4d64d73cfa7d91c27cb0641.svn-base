package cn.syrjia.util.logistics;

public class CallExpressServiceTools
{
  private static CallExpressServiceTools tools = new CallExpressServiceTools();

  public static CallExpressServiceTools getInstance()
  {
    synchronized (CallExpressServiceTools.class) {
      if (tools == null)
        tools = new CallExpressServiceTools();
    }

    return tools;
  }

  public static String callSfExpressServiceByCSIM(String reqURL, String reqXML, String clientCode, String checkword) {
    String result = null;
    String verifyCode = VerifyCodeUtil.md5EncryptAndBase64(reqXML + checkword);
    result = querySFAPIservice(reqURL, reqXML, verifyCode);
    return result;
  }

  public static String querySFAPIservice(String url, String xml, String verifyCode)
  {
    HttpClientUtil httpclient = new HttpClientUtil();

    if (url == null) {
      url = "http://bsp-oisp.sf-express.com/bsp-oisp/sfexpressService";
    }

    String result = null;
    try {
      result = httpclient.postSFAPI(url, xml, verifyCode);

      return result;
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }
}