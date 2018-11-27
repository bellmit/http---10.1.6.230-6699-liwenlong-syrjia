package cn.syrjia.hospital.controller;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.syrjia.entity.WeiXinUser;
import cn.syrjia.util.SessionUtil;
import cn.syrjia.wxPay.wxPay.util.StringUtil;
import cn.syrjia.hospital.entity.Doctor;

@Controller
@RequestMapping("center")
public class ContentController {

	/*@Resource(name="wxUserService")
	WxUserService wxUserService;*/
	
	
	/**
	 * 跳转到我的个人中心
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/center")
	public String baseInfo(HttpServletRequest request){
		String openid = SessionUtil.getOpenId(request);
		/*WeiXinUser user= wxUserService.queryById(WeiXinUser.class,openid, false);
		request.setAttribute("user", user);
		if(StringUtil.isEmpty(SessionUtil.isDoctor(request))){
			User users=new User();
			users.setOpenId(SessionUtil.getOpenId(request));
			User u=wxUserService.queryByEntity(users);
			if(null!=u&&!StringUtil.isEmpty(u.getId())){
				Channel channel=new Channel();
				channel.setUserId(u.getId());
				Channel c=wxUserService.queryByEntity(channel);
				request.setAttribute("qrCode",null!=c&&null!=c.getId()?c.getQrUrl():null);
			}
		}else{
			Doctor c=wxUserService.queryById(Doctor.class,SessionUtil.isDoctor(request));
			request.setAttribute("qrCode",null!=c&&null!=c.getDoctorId()?c.getQrCodeUrl():null);
		}*/
		return "hospital/center";
	}
}
