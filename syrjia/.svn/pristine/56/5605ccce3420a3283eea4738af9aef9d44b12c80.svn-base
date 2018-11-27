/*     */ package cn.syrjia.company.safety.utils;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URL;
/*     */ import java.net.URLDecoder;
/*     */ import java.security.CodeSource;
/*     */ import java.security.ProtectionDomain;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ 
/*     */ public class XssInit
/*     */ {
/*  16 */   private String path = "xsssafety.properties";
/*  17 */   private static XssInit intstance = new XssInit();
/*     */ 
/*  28 */   public Map<String, String> maps = new HashMap();
/*     */ 
/*     */   public static XssInit getInstance()
/*     */   {
/*  19 */     return intstance;
/*     */   }
/*     */ 
/*     */   private XssInit() {
/*  23 */     init();
/*     */   }
/*     */ 
/*     */   private void init()
/*     */   {
/*  30 */     String configPath = "";
/*  31 */     File file = null;
/*     */     try {
/*  33 */       URL url = Thread.currentThread().getContextClassLoader().getResource(this.path);
/*  34 */       if (url == null) {
/*  35 */         configPath = getJarPath(XssInit.class) + "/" + this.path;
/*     */       }
/*     */       else
/*     */       {
/*  39 */         configPath = url.toString();
/*  40 */         configPath = configPath.replaceFirst("file:/", "");
/*  41 */         configPath = configPath.replaceAll("/lib", "");
/*     */       }
/*     */ 
/*  44 */       file = new File(configPath);
/*  45 */       if (!file.exists()) {
/*  46 */         configPath = getClass().getClassLoader().getResource("/").getPath();
/*  47 */         configPath = configPath.replaceAll("/classes", "");
/*  48 */         configPath = configPath + this.path;
/*     */       }
/*     */ 
/*  51 */       int m = 0;
/*  52 */       File file2 = new File(configPath);
/*  53 */       if (!file2.exists()) {
/*  54 */         InputStream inputStream = getClass().getResourceAsStream("/" + this.path);
/*  55 */         Properties properties1 = getProperties1(inputStream);
/*  56 */         setProperties(properties1);
/*  57 */         m = 1;
/*     */       }
/*     */ 
/*  60 */       if (m == 0) {
/*  61 */         Properties properties = getProperties(configPath);
/*  62 */         setProperties(properties);
/*     */       }
/*     */     } catch (Exception e) {
/*  65 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static String getJarPath(Class<?> type)
/*     */   {
/*  72 */     String path = type.getProtectionDomain().getCodeSource().getLocation().getPath();
/*  73 */     path = path.replaceFirst("file:/", "");
/*  74 */     path = path.replaceFirst("/lib", "");
/*  75 */     path = path.replaceAll("!/", "");
/*  76 */     path = path.replaceAll("\\\\", "/");
/*  77 */     path = path.substring(0, path.lastIndexOf("/"));
/*  78 */     if (path.substring(0, 1).equalsIgnoreCase("/")) {
/*  79 */       String osName = System.getProperty("os.name").toLowerCase();
/*  80 */       if (osName.indexOf("window") >= 0)
/*  81 */         path = path.substring(1);
/*     */     }
/*     */     try
/*     */     {
/*  85 */       return URLDecoder.decode(path, "UTF-8"); } catch (UnsupportedEncodingException ex) {
/*     */     }
/*  87 */     return path;
/*     */   }
/*     */ 
/*     */   public Properties getProperties(String conFile)
/*     */   {
/*  92 */     Properties properties = new Properties();
/*  93 */     InputStream inputStream = null;
/*     */     try {
/*  95 */       inputStream = new FileInputStream(new File(conFile));
/*  96 */       if (inputStream != null)
/*  97 */         properties.load(inputStream);
/*     */     }
/*     */     catch (Exception e) {
/* 100 */       throw new RuntimeException("读取" + conFile + "出错,原因：" + e.getMessage(), e);
/*     */     } finally {
/*     */       try {
/* 103 */         if (inputStream != null)
/* 104 */           inputStream.close();
/*     */       }
/*     */       catch (IOException e) {
/* 107 */         e.printStackTrace();
/*     */       }
/*     */     }
/* 110 */     return properties;
/*     */   }
/*     */ 
/*     */   public Properties getProperties1(InputStream inputStream) {
/* 114 */     Properties properties = new Properties();
/*     */     try
/*     */     {
/* 117 */       if (inputStream != null)
/* 118 */         properties.load(inputStream);
/*     */     }
/*     */     catch (Exception e) {
/* 121 */       throw new RuntimeException("读取流出错,原因：" + e.getMessage(), e);
/*     */     } finally {
/*     */       try {
/* 124 */         if (inputStream != null)
/* 125 */           inputStream.close();
/*     */       }
/*     */       catch (IOException e) {
/* 128 */         e.printStackTrace();
/*     */       }
/*     */     }
/* 131 */     return properties;
/*     */   }
/*     */   private void setProperties(Properties properties) {
/* 134 */     this.maps.put("scriptms", (String)properties.get("xss.scriptm"));
/* 135 */     this.maps.put("evals", (String)properties.get("xss.eval"));
/* 136 */     this.maps.put("expressions", (String)properties.get("xss.expression"));
/* 137 */     this.maps.put("scriptns", (String)properties.get("xss.scriptn"));
/* 138 */     this.maps.put("windows", (String)properties.get("xss.window"));
/* 139 */     this.maps.put("events", (String)properties.get("xss.event"));
/* 140 */     this.maps.put("imports", (String)properties.get("xss.import"));
/* 141 */     this.maps.put("leftbrackets", (String)properties.get("xss.leftbracket"));
/* 142 */     this.maps.put("rightbrackets", (String)properties.get("xss.rightbracket"));
/* 143 */     this.maps.put("andsymbols", (String)properties.get("xss.andsymbol"));
/* 144 */     this.maps.put("doublemarks", (String)properties.get("xss.doublemarks"));
/* 145 */     this.maps.put("singlemarks", (String)properties.get("xss.singlemarks"));
/* 146 */     this.maps.put("backslashms", (String)properties.get("xss.backslashm"));
/* 147 */     this.maps.put("signs", (String)properties.get("xss.sign"));
/* 148 */     this.maps.put("asterisks", (String)properties.get("xss.asterisk"));
/* 149 */     this.maps.put("script", (String)properties.get("xss.script"));
/* 150 */     this.maps.put("intercept.url", (String)properties.get("intercept.url"));
/*     */   }
/*     */ }

/* Location:           C:\Users\FlowerMe\Desktop\sinosafety-1.1.jar
 * Qualified Name:     com.company.safety.utils.XssInit
 * JD-Core Version:    0.6.1
 */