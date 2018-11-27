package cn.syrjia.company.safety.utils;

import java.util.Map;
import org.apache.commons.lang.StringUtils;

public class SqlInjectUtil
{
  public static String getSqlInject(String key)
  {
/* 10 */     SqlInit sqlsafe = SqlInit.getInstance();
/* 11 */     Map maps = sqlsafe.maps;
/* 12 */     if (StringUtils.isNotBlank((String)maps.get(key))) {
/* 13 */       return (String)maps.get(key);
    }
/* 15 */     return "";
  }
}

/* Location:           C:\Users\FlowerMe\Desktop\sinosafety-1.1.jar
 * Qualified Name:     com.company.safety.utils.SqlInjectUtil
 * JD-Core Version:    0.6.1
 */