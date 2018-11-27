package cn.syrjia.company.safety.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class SpringMvcShieldUtil
{
/* 12 */   private static final Logger logger = Logger.getLogger(SpringMvcShieldUtil.class);

/* 14 */   private static Map<String, List<String>> getXssPatternList(XssInit xsssafe) { Map maps = xsssafe.maps;
/* 15 */     Map maps1 = new HashMap();
/* 16 */     List list = new ArrayList();
/* 17 */     List list1 = new ArrayList();
/* 18 */     if (StringUtils.isNotBlank((String)maps.get("scriptms"))) {
/* 19 */       list.add((String)maps.get("scriptms"));
    }
/* 21 */     if (StringUtils.isNotBlank((String)maps.get("evals"))) {
/* 22 */       list.add((String)maps.get("evals"));
    }
/* 24 */     if (StringUtils.isNotBlank((String)maps.get("expressions"))) {
/* 25 */       list.add((String)maps.get("expressions"));
    }
/* 27 */     if (StringUtils.isNotBlank((String)maps.get("scriptns"))) {
/* 28 */       list.add((String)maps.get("scriptns"));
    }
/* 30 */     if (StringUtils.isNotBlank((String)maps.get("windows"))) {
/* 31 */       list.add((String)maps.get("windows"));
    }
/* 33 */     if (StringUtils.isNotBlank((String)maps.get("events"))) {
/* 34 */       list.add((String)maps.get("events"));
    }
/* 36 */     if (StringUtils.isNotBlank((String)maps.get("imports"))) {
/* 37 */       list1.add((String)maps.get("imports"));
    }
/* 39 */     if (StringUtils.isNotBlank((String)maps.get("andsymbols"))) {
/* 40 */       list1.add((String)maps.get("andsymbols"));
    }
/* 42 */     if (StringUtils.isNotBlank((String)maps.get("leftbrackets"))) {
/* 43 */       list1.add((String)maps.get("leftbrackets"));
    }
/* 45 */     if (StringUtils.isNotBlank((String)maps.get("rightbrackets"))) {
/* 46 */       list1.add((String)maps.get("rightbrackets"));
    }
/* 48 */     if (StringUtils.isNotBlank((String)maps.get("doublemarks"))) {
/* 49 */       list1.add((String)maps.get("doublemarks"));
    }
/* 51 */     if (StringUtils.isNotBlank((String)maps.get("singlemarks"))) {
/* 52 */       list1.add((String)maps.get("singlemarks"));
    }
/* 54 */     if (StringUtils.isNotBlank((String)maps.get("backslashms"))) {
/* 55 */       list1.add((String)maps.get("backslashms"));
    }
/* 57 */     if (StringUtils.isNotBlank((String)maps.get("signs"))) {
/* 58 */       list1.add((String)maps.get("signs"));
    }
/* 60 */     if (StringUtils.isNotBlank((String)maps.get("asterisks"))) {
/* 61 */       list1.add((String)maps.get("asterisks"));
    }
/* 63 */     if (StringUtils.isNotBlank((String)maps.get("script"))) {
/* 64 */       list1.add((String)maps.get("script"));
    }
/* 66 */     maps1.put("list", list);
/* 67 */     maps1.put("list1", list1);
/* 68 */     return maps1; }

  public static String stripXss(String value, XssInit xsssafe) {
/* 71 */     int flag = 0;
/* 72 */     if (StringUtils.isNotBlank(value)) {
/* 73 */       String value1 = value.toLowerCase();
/* 74 */       for (String pattern : getXssPatternList(xsssafe).get("list")) {
/* 75 */         if (value1.matches(pattern)) {
/* 76 */           logger.info("xss转码后的值为:" + value);
/* 77 */           flag = 1;
/* 78 */           break;
        }
      }
/* 81 */       for (String pattern : getXssPatternList(xsssafe).get("list1")) {
/* 82 */         if (value1.contains(pattern)) {
/* 83 */           logger.info("xss转码后的值为:" + value);
/* 84 */           flag = 1;
/* 85 */           break;
        }
      }
    }
/* 89 */     if (flag == 1) {
/* 90 */       return "XssFalse";
    }
/* 92 */     return value;
  }
}

/* Location:           C:\Users\FlowerMe\Desktop\sinosafety-1.1.jar
 * Qualified Name:     com.company.safety.utils.SpringMvcShieldUtil
 * JD-Core Version:    0.6.1
 */