package cn.syrjia.company.safety.filter;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import cn.syrjia.company.safety.utils.XssInit;
import cn.syrjia.company.safety.utils.XssShieldUtil;

public class MHttpServletRequest extends HttpServletRequestWrapper
{
/* 12 */   XssInit xsssafe = XssInit.getInstance();

/* 14 */   public MHttpServletRequest(HttpServletRequest request) { super(request); }


  public String getParameter(String name)
  {
/* 20 */     return XssShieldUtil.stripXss(super.getParameter(XssShieldUtil.stripXss(name, this.xsssafe)), this.xsssafe);
  }

  public String[] getParameterValues(String name)
  {
/* 27 */     String[] values = super.getParameterValues(XssShieldUtil.stripXss(name, this.xsssafe));
/* 28 */     if (values != null) {
/* 29 */       for (int i = 0; i < values.length; i++) {
/* 30 */         values[i] = XssShieldUtil.stripXss(values[i], this.xsssafe);
      }
    }
/* 33 */     return values;
  }
}

