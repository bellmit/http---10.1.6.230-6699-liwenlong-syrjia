/*     */ package cn.syrjia.company.safety.filter;
/*     */ 
/*     */ import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import cn.syrjia.company.safety.utils.SqlInjectUtil;
/*     */ 
/*     */ public class SQLInjectFilter
/*     */   implements Filter
/*     */ {
/*  25 */   String encoding = null;
/*  26 */   private static final Logger logger = Logger.getLogger(SQLInjectFilter.class);
/*     */ 
/*  28 */   private static final String keyWordStr = SqlInjectUtil.getSqlInject("sql.keyWord");
/*  29 */   private static final String[] keyWords = keyWordStr.split(",");
/*  30 */   private static final String exceptStr = SqlInjectUtil.getSqlInject("sql.exceptWord");
/*  31 */   private static final String[] exceptParams = exceptStr.split(",");
/*     */ 
/*  33 */   private static final String urlDecodeFlag = SqlInjectUtil.getSqlInject("sql.urldecodeflag");
/*     */ 
/*     */   public void init(FilterConfig path)
/*     */     throws ServletException
/*     */   {
/*  38 */     this.encoding = path.getInitParameter("encoding");
/*     */   }
/*     */ 
/*     */   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
/*     */     throws IOException, ServletException
/*     */   {
/*  46 */     if (this.encoding != null) {
/*  47 */       request.setCharacterEncoding(this.encoding);
/*  48 */       response.setContentType("text/html;charset=" + this.encoding);
/*     */     }
/*  50 */     HttpServletRequest httpRequest = (HttpServletRequest)request;
/*     */ 
/*  52 */     String path = httpRequest.getContextPath();
/*  53 */     String basePath = httpRequest.getScheme() + "://" + httpRequest.getServerName() + ":" + httpRequest.getServerPort() + path;
/*  54 */     int flag = 0;
/*  55 */     String url = httpRequest.getRequestURI();
/*  56 */     String interceptUrl = SqlInjectUtil.getSqlInject("intercept.url");
/*  57 */     if ((interceptUrl != null) && (!"".equals(interceptUrl))) {
/*  58 */       String[] urls = interceptUrl.split(",");
/*  59 */       for (String str : urls) {
/*  60 */         if (url.contains(str)) {
/*  61 */           flag = 1;
/*     */         }
/*     */       }
/*     */     }
/*  65 */     if (flag == 0) {
/*  66 */       Enumeration value = httpRequest.getParameterNames();
/*  67 */       StringBuffer parmeters = new StringBuffer();
/*  68 */       String str = "";
/*  69 */       while (value.hasMoreElements()) {
/*  70 */         int flagString = 0;
/*  71 */         str = (String)value.nextElement();
/*  72 */         for (String exceptParam : exceptParams) {
/*  73 */           if (str.equals(exceptParam)) {
/*  74 */             flagString++;
/*     */           }
/*     */         }
/*  77 */         if (flagString == 0) {
/*  78 */           String[] paraValues = httpRequest.getParameterValues(str);
/*  79 */           for (String sqlString : paraValues) {
/*  80 */             parmeters.append("," + sqlString);
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/*  85 */       if (checkParameter(parmeters.toString()))
/*     */       {
/*  87 */         PrintWriter pw = null;
/*     */         try {
/*  89 */           response.setContentType("text/html; charset=utf-8");
/*  90 */           pw = response.getWriter();
/*  91 */           pw.println("<HTML><HEAD></HEAD><BODY>");
/*  92 */           pw.println("录入非法字符，请求被拒绝！");
/*  93 */           pw.println("</BODY></HTML>");
/*     */         } catch (Exception localException1) {
/*     */         } finally {
/*  96 */           if (pw != null) {
/*  97 */             pw.flush();
/*  98 */             pw.close();
/*     */           }
/*     */         }
/*     */       } else {
/* 102 */         chain.doFilter(request, response);
/*     */       }
/*     */     } else {
/* 105 */       chain.doFilter(request, response);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean checkParameter(String parameter)
/*     */   {
/* 115 */     boolean result = false;
/* 116 */     String lowerCasePara = parameter.toLowerCase();
/* 117 */     if ("1".equals(urlDecodeFlag))
/*     */       try {
/* 119 */         lowerCasePara = lowerCasePara.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
/* 120 */         lowerCasePara = URLDecoder.decode(lowerCasePara, "UTF-8");
/*     */       }
/*     */       catch (Exception localException) {
/*     */       }
/* 124 */     for (int i = 0; i < keyWords.length; i++) {
/* 125 */       if (lowerCasePara.indexOf(keyWords[i]) > -1) {
/* 126 */         logger.info("SQLInjectFilter过滤掉的关键字为:" + keyWords[i]);
/* 127 */         result = true;
/* 128 */         break;
/*     */       }
/*     */     }
/*     */ 
/* 132 */     return result;
/*     */   }
/*     */ 
/*     */   public void destroy()
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Users\FlowerMe\Desktop\sinosafety-1.1.jar
 * Qualified Name:     com.company.safety.filter.SQLInjectFilter
 * JD-Core Version:    0.6.1
 */