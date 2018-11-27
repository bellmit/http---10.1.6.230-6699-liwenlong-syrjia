/*     */ package cn.syrjia.company.safety.filter;
/*     */ 
/*     */ import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.owasp.esapi.ESAPI;

import cn.syrjia.company.safety.utils.XssInit;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
/*     */ 
/*     */ public class XssInterceptor extends AbstractInterceptor
/*     */ {
/*  21 */   private static final Logger logger = Logger.getLogger(XssInterceptor.class);
/*     */ 
/*     */   public String intercept(ActionInvocation invocation) throws Exception
/*     */   {
/*  25 */     XssInit xsssafe = XssInit.getInstance();
/*  26 */     Map maps = xsssafe.maps;
/*  27 */     Map maps1 = new HashMap();
/*  28 */     List list = new ArrayList();
/*  29 */     List list1 = new ArrayList();
/*  30 */     if (StringUtils.isNotBlank((String)maps.get("scriptms"))) {
/*  31 */       list.add((String)maps.get("scriptms"));
/*     */     }
/*  33 */     if (StringUtils.isNotBlank((String)maps.get("evals"))) {
/*  34 */       list.add((String)maps.get("evals"));
/*     */     }
/*  36 */     if (StringUtils.isNotBlank((String)maps.get("expressions"))) {
/*  37 */       list.add((String)maps.get("expressions"));
/*     */     }
/*  39 */     if (StringUtils.isNotBlank((String)maps.get("scriptns"))) {
/*  40 */       list.add((String)maps.get("scriptns"));
/*     */     }
/*  42 */     if (StringUtils.isNotBlank((String)maps.get("windows"))) {
/*  43 */       list.add((String)maps.get("windows"));
/*     */     }
/*  45 */     if (StringUtils.isNotBlank((String)maps.get("events"))) {
/*  46 */       list.add((String)maps.get("events"));
/*     */     }
/*  48 */     if (StringUtils.isNotBlank((String)maps.get("imports"))) {
/*  49 */       list1.add((String)maps.get("imports"));
/*     */     }
/*  51 */     if (StringUtils.isNotBlank((String)maps.get("andsymbols"))) {
/*  52 */       list1.add((String)maps.get("andsymbols"));
/*     */     }
/*  54 */     if (StringUtils.isNotBlank((String)maps.get("leftbrackets"))) {
/*  55 */       list1.add((String)maps.get("leftbrackets"));
/*     */     }
/*  57 */     if (StringUtils.isNotBlank((String)maps.get("rightbrackets"))) {
/*  58 */       list1.add((String)maps.get("rightbrackets"));
/*     */     }
/*  60 */     if (StringUtils.isNotBlank((String)maps.get("doublemarks"))) {
/*  61 */       list1.add((String)maps.get("doublemarks"));
/*     */     }
/*  63 */     if (StringUtils.isNotBlank((String)maps.get("singlemarks"))) {
/*  64 */       list1.add((String)maps.get("singlemarks"));
/*     */     }
/*  66 */     if (StringUtils.isNotBlank((String)maps.get("backslashms"))) {
/*  67 */       list1.add((String)maps.get("backslashms"));
/*     */     }
/*  69 */     if (StringUtils.isNotBlank((String)maps.get("signs"))) {
/*  70 */       list1.add((String)maps.get("signs"));
/*     */     }
/*  72 */     if (StringUtils.isNotBlank((String)maps.get("asterisks"))) {
/*  73 */       list1.add((String)maps.get("asterisks"));
/*     */     }
/*  75 */     if (StringUtils.isNotBlank((String)maps.get("script"))) {
/*  76 */       list1.add((String)maps.get("script"));
/*     */     }
/*  78 */     maps1.put("list", list);
/*  79 */     maps1.put("list1", list1);
/*  80 */     ActionContext actionContext = invocation.getInvocationContext();
/*  81 */     String actionName = actionContext.getName();
/*  82 */     logger.info("actionName================" + actionName);
/*  83 */     int flag = 0;
/*  84 */     String interceptUrl = (String)maps.get("intercept.url");
/*  85 */     if ((interceptUrl != null) && (!interceptUrl.equals(""))) {
/*  86 */       String[] urls = interceptUrl.split(",");
/*  87 */       for (String str : urls) {
/*  88 */         if (actionName.contains(str)) {
/*  89 */           flag = 1;
/*     */         }
/*     */       }
/*     */     }
/*  93 */     if (flag == 0) {
/*  94 */       Map map2 = actionContext.getParameters();
				Iterator it=map2.entrySet().iterator();
/*  95 */       while (it.hasNext()) {
				  Map.Entry entry = (Map.Entry)it.next();
/*  96 */         String value = ((String[])entry.getValue())[0];
/*  97 */         if (StringUtils.isNotBlank(value))
/*     */         {
/*  99 */           String value1 = value.toLowerCase();
/*     */ 			for (int i = 0; i < list1.size(); i++) {
	
}
/* 102 */           for (int i = 0; i < list1.size(); i++) {
/* 103 */             if (value1.contains(list1.get(i).toString())) {
/* 104 */               String result = ESAPI.encoder().encodeForHTML(list1.get(i).toString());
/* 105 */               entry.setValue(result);
/* 106 */               logger.info("xss转码后的值为:" + result);
/*     */             }
/*     */           }
/*     */ 
/* 110 */           for (int i = 0; i < list.size(); i++) {
/* 111 */             if (value1.matches(list.get(i).toString())) {
/* 112 */               String result = ESAPI.encoder().encodeForJavaScript(
/* 113 */                 value);
/* 114 */               entry.setValue(result);
/* 115 */               logger.info("xss转码后的值为:" + result);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 122 */     return invocation.invoke();
/*     */   }
/*     */ }

/* Location:           C:\Users\FlowerMe\Desktop\sinosafety-1.1.jar
 * Qualified Name:     com.company.safety.filter.XssInterceptor
 * JD-Core Version:    0.6.1
 */