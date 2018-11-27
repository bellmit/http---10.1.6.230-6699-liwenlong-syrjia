package cn.syrjia.util;

import java.io.IOException;
import java.util.Properties;

public class Property
{
  public static String getProperty(String k)
  {
    Properties prop = new Properties();
    try {
        prop.load(Property.class.getResourceAsStream("sys.properties"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return prop.getProperty(k);
  }
}