package cn.syrjia.company.safety.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import cn.syrjia.company.safety.utils.SpringMvcShieldUtil;
import cn.syrjia.company.safety.utils.XssInit;

public class SpringMvcServletRequest extends HttpServletRequestWrapper
{
/* 12 */   XssInit xsssafe = XssInit.getInstance();

/* 14 */   public SpringMvcServletRequest(HttpServletRequest request) { super(request); }


  public String getParameter(String name)
  {
/* 19 */     return SpringMvcShieldUtil.stripXss(super.getParameter(SpringMvcShieldUtil.stripXss(name, this.xsssafe)), this.xsssafe);
  }

  public String[] getParameterValues(String name)
  {
/* 25 */     String[] values = super.getParameterValues(SpringMvcShieldUtil.stripXss(name, this.xsssafe));
/* 26 */     if (values != null) {
/* 27 */       for (int i = 0; i < values.length; i++) {
/* 28 */         values[i] = SpringMvcShieldUtil.stripXss(values[i], this.xsssafe);
      }
    }
/* 31 */     return values;
  }
}

/* Location:           C:\Users\FlowerMe\Desktop\sinosafety-1.1.jar
 * Qualified Name:     com.company.safety.filter.SpringMvcServletRequest
 * JD-Core Version:    0.6.1
 */