package cn.syrjia.company.safety.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Encoder;

public class XssShieldUtil
{
/* 15 */   private static final Logger logger = Logger.getLogger(XssShieldUtil.class);

/* 17 */   private static Map<String, List<String>> getXssPatternList(XssInit xsssafe) { Map maps = xsssafe.maps;
/* 18 */     Map maps1 = new HashMap();
/* 19 */     List list = new ArrayList();
/* 20 */     List list1 = new ArrayList();
/* 21 */     if (StringUtils.isNotBlank((String)maps.get("scriptms"))) {
/* 22 */       list.add((String)maps.get("scriptms"));
    }
/* 24 */     if (StringUtils.isNotBlank((String)maps.get("evals"))) {
/* 25 */       list.add((String)maps.get("evals"));
    }
/* 27 */     if (StringUtils.isNotBlank((String)maps.get("expressions"))) {
/* 28 */       list.add((String)maps.get("expressions"));
    }
/* 30 */     if (StringUtils.isNotBlank((String)maps.get("scriptns"))) {
/* 31 */       list.add((String)maps.get("scriptns"));
    }
/* 33 */     if (StringUtils.isNotBlank((String)maps.get("windows"))) {
/* 34 */       list.add((String)maps.get("windows"));
    }
/* 36 */     if (StringUtils.isNotBlank((String)maps.get("events"))) {
/* 37 */       list.add((String)maps.get("events"));
    }
/* 39 */     if (StringUtils.isNotBlank((String)maps.get("imports"))) {
/* 40 */       list1.add((String)maps.get("imports"));
    }
/* 42 */     if (StringUtils.isNotBlank((String)maps.get("andsymbols"))) {
/* 43 */       list1.add((String)maps.get("andsymbols"));
    }
/* 45 */     if (StringUtils.isNotBlank((String)maps.get("leftbrackets"))) {
/* 46 */       list1.add((String)maps.get("leftbrackets"));
    }
/* 48 */     if (StringUtils.isNotBlank((String)maps.get("rightbrackets"))) {
/* 49 */       list1.add((String)maps.get("rightbrackets"));
    }
/* 51 */     if (StringUtils.isNotBlank((String)maps.get("doublemarks"))) {
/* 52 */       list1.add((String)maps.get("doublemarks"));
    }
/* 54 */     if (StringUtils.isNotBlank((String)maps.get("singlemarks"))) {
/* 55 */       list1.add((String)maps.get("singlemarks"));
    }
/* 57 */     if (StringUtils.isNotBlank((String)maps.get("backslashms"))) {
/* 58 */       list1.add((String)maps.get("backslashms"));
    }
/* 60 */     if (StringUtils.isNotBlank((String)maps.get("signs"))) {
/* 61 */       list1.add((String)maps.get("signs"));
    }
/* 63 */     if (StringUtils.isNotBlank((String)maps.get("asterisks"))) {
/* 64 */       list1.add((String)maps.get("asterisks"));
    }
/* 66 */     if (StringUtils.isNotBlank((String)maps.get("script"))) {
/* 67 */       list1.add((String)maps.get("script"));
    }
/* 69 */     maps1.put("list", list);
/* 70 */     maps1.put("list1", list1);
/* 71 */     return maps1; }

  public static String stripXss(String value, XssInit xsssafe) {
/* 74 */     if (StringUtils.isNotBlank(value))
    {
/* 76 */       String value1 = value.toLowerCase();
/* 78 */       for (String pattern : getXssPatternList(xsssafe).get("list")) {
/* 79 */         if (value1.matches(pattern)) {
/* 80 */           String result = ESAPI.encoder().encodeForJavaScript(value);
/* 81 */           value = value.replace(value, result);
/* 82 */           logger.info("xss转码后的值为:" + result);
/* 83 */           return value;
        }

      }

/* 88 */       for (String pattern : getXssPatternList(xsssafe).get("list1")) {
/* 89 */         if (value1.contains(pattern)) {
/* 90 */           String result = ESAPI.encoder().encodeForHTML(pattern);
/* 91 */           value = value.replaceAll(pattern, result);
/* 92 */           logger.info("xss转码后的值为:" + value);
        }
      }
    }

/* 97 */     return value;
  }
}

/* Location:           C:\Users\FlowerMe\Desktop\sinosafety-1.1.jar
 * Qualified Name:     com.company.safety.utils.XssShieldUtil
 * JD-Core Version:    0.6.1
 */