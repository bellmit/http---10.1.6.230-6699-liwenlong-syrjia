package cn.syrjia.util.logistics;

import java.security.MessageDigest;
import org.apache.commons.codec.binary.Base64;

public class VerifyCodeUtil
{
  public static String md5EncryptAndBase64(String str) {
    return encodeBase64(md5Encrypt(str));
  }

  private static byte[] md5Encrypt(String encryptStr) {
    try {
      MessageDigest md5 = MessageDigest.getInstance("MD5");
      md5.update(encryptStr.getBytes("utf8"));
      return md5.digest();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static String encodeBase64(byte[] b) {
    String str = new Base64().encodeAsString(b);

    return str;
  }
}