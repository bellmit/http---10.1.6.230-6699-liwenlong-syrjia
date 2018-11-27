package cn.syrjia.company.safety.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import cn.syrjia.company.safety.utils.XssInit;

public class SpringMvcCharacterFilter
  implements Filter
{
/* 20 */   String encoding = null;

  public void init(FilterConfig filterConfig) throws ServletException
  {
/* 24 */     this.encoding = filterConfig.getInitParameter("encoding");
  }

  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
    throws IOException, ServletException
  {
/* 30 */     if (this.encoding != null) {
/* 31 */       req.setCharacterEncoding(this.encoding);
/* 32 */       res.setContentType("text/html;charset=" + this.encoding);
    }
/* 34 */     XssInit xsssafe = XssInit.getInstance();
/* 35 */     Map maps = xsssafe.maps;
/* 36 */     HttpServletRequest request = (HttpServletRequest)req;
/* 37 */     int flag = 0;
/* 38 */     String url = request.getRequestURI();
/* 39 */     String interceptUrl = (String)maps.get("intercept.url");
/* 40 */     if ((interceptUrl != null) && (!interceptUrl.equals(""))) {
/* 41 */       String[] urls = interceptUrl.split(",");
/* 42 */       for (String str : urls) {
/* 43 */         if (url.contains(str)) {
/* 44 */           chain.doFilter(req, res);
/* 45 */           flag = 1;
        }
      }
    }
/* 49 */     int xssflag = 0;
/* 50 */     if (flag == 0) {
/* 51 */       request = new SpringMvcServletRequest(request);
/* 52 */       Enumeration value = request.getParameterNames();
/* 53 */       String str = null;
/* 54 */       while (value.hasMoreElements()) {
/* 55 */         str = (String)value.nextElement();
/* 56 */         String[] paraValues = request.getParameterValues(str);
/* 57 */         for (String xssString : paraValues) {
/* 58 */           if (xssString.contains("XssFalse")) {
/* 59 */             xssflag = 1;
/* 60 */             break;
          }
        }
      }
/* 64 */       if (xssflag == 1) {
/* 65 */         PrintWriter pw = null;
        try {
/* 67 */           res.setContentType("text/html; charset=utf-8");
/* 68 */           pw = res.getWriter();
/* 69 */           pw.println("<HTML><HEAD></HEAD><BODY>");
/* 70 */           pw.println("xss-录入非法字符，请求被拒绝！");
/* 71 */           pw.println("</BODY></HTML>");
        } catch (Exception localException1) {
        } finally {
/* 74 */           if (pw != null) {
/* 75 */             pw.flush();
/* 76 */             pw.close();
          }
        }
      } else {
/* 80 */         chain.doFilter(request, res);
      }
    }
  }

  public void destroy()
  {
/* 87 */     this.encoding = null;
  }
}

/* Location:           C:\Users\FlowerMe\Desktop\sinosafety-1.1.jar
 * Qualified Name:     com.company.safety.filter.SpringMvcCharacterFilter
 * JD-Core Version:    0.6.1
 */