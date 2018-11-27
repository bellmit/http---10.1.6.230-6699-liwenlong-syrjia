package cn.syrjia.company.safety.filter;


import java.io.IOException;
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


public class IllegalCharacterFilter implements Filter{
				//初始化编码格式为空
       			String encoding = null;
  public void init(FilterConfig filterConfig) throws ServletException
  {				
	  			//初始化方法获取参数里面的编码格式
       			this.encoding = filterConfig.getInitParameter("encoding");
  }

  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
		  throws IOException, ServletException{
	      //判断编码格式，如果不为空，设置她为输出编码
	  	  if (this.encoding != null) {
	       	 req.setCharacterEncoding(this.encoding);
	       	 //设置内容类型为text格式
	       	 res.setContentType("text/html;charset=" + this.encoding);
	  	  }
	  
	  	  	 //获取参数
	  		 XssInit xsssafe = XssInit.getInstance();
	  		 //转换成map
	  		 Map maps = xsssafe.maps;
	  		 HttpServletRequest request = (HttpServletRequest)req;
	  		 int flag = 0;
	  		 String url = request.getRequestURI();
	  		 String interceptUrl = (String)maps.get("intercept.url");
	  		 if ((interceptUrl != null) && (!interceptUrl.equals(""))) {
	  			 String[] urls = interceptUrl.split(",");
	  			 for (String str : urls) {
	  				 if (url.contains(str)) {
	  					 chain.doFilter(req, res);
	  					 flag = 1;
        }
      }
    }
/* 49 */     if (flag == 0) {
/* 50 */       request = new MHttpServletRequest(request);
/* 51 */       Enumeration value = request.getParameterNames();
/* 52 */       String str = null;
/* 53 */       while (value.hasMoreElements()) {
/* 54 */         str = (String)value.nextElement();
/* 55 */         String[] arrayOfString1 = request.getParameterValues(str);
      }
/* 57 */       chain.doFilter(request, res);
    }
  }

  public void destroy()
  {
/* 63 */     this.encoding = null;
  }
}

