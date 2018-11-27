package cn.syrjia.wxPay.wxPayReceive.service.impl;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.syrjia.common.impl.BaseServiceImpl;
import cn.syrjia.wxPay.wxPayReceive.dao.WxPayReciDao;
import cn.syrjia.wxPay.wxPayReceive.service.WxPayReciService;

@Service("wxPayReciService")
public class WXPayHttpRecisServiceImpl extends BaseServiceImpl implements
		WxPayReciService {
	
	// 日志
	@SuppressWarnings("unused")
	private Logger logger = LogManager.getLogger(WXPayHttpRecisServiceImpl.class);
	
	@Resource(name = "wxPayReciDao")
	WxPayReciDao wxPayReciDao;
	
	
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public Object updataDoc() {
		Object i ="";
		/*Object i=wxPayReciDao.updataDoc(order);
		if(!StringUtil.isEmpty(i)&&i!=(Object)0){
			SendKeFuMsgToDoc(request,order);//给医生发送客服消息
			//通知会员挂号成功
			Doctor docor = wxPayReciDao.queryById(Doctor.class, order.getDoctorId());
			if (docor!=null && !StringUtil.isEmpty(docor.getOpenid())) {
				SendTempMsg sendTempMsg = new SendTempMsg();
				sendTempMsg.setTouser(order.getOpenid());
				sendTempMsg
						.setTemplate_id("vzL6Q2F27rYVRM1iUrP_BwSatVEIUmD4XobM0DU5HsI");
				Map<String, TemplateData> map = new HashMap<String, TemplateData>();
				List<TemplateData> templateDatat = new ArrayList<>();
				WeiXinUser user = wxPayReciDao.queryById(WeiXinUser.class, order.getOpenid());
				TemplateData templateData = new TemplateData();
				TemplateData templateData1 = new TemplateData();
				TemplateData templateData2 = new TemplateData();
				TemplateData templateData3 = new TemplateData();
				TemplateData templateData4 = new TemplateData();
				TemplateData templateData5 = new TemplateData();
				TemplateData templateData6 = new TemplateData();
				templateData.setValue("您好，您已成功挂号");
				templateData1.setValue(user.getRealname() == null ? user
						.getNickname() : user.getRealname());// 姓名

				map.put("frist", templateData);
				map.put("keyword1", templateData1);

				templateData2.setValue(1 == user.getSex() ? "男" : "女");// 性别
				map.put("keyword2", templateData2);

				templateData3.setValue(docor.getDepartName());// 挂号科室
				map.put("keyword3", templateData3);

				templateData4.setValue(docor.getDocName());// 挂号医生
				map.put("keyword4", templateData4);

				templateData5.setValue(DateTime.getTime());// 挂号时间
				map.put("keyword5", templateData5);

				templateData6.setValue("医生会在48小时内与您做进一步沟通并制定调理方案，请及时关注。");// 备注
				map.put("keyword6", templateData6);

				templateDatat.add(templateData);
				templateDatat.add(templateData1);
				templateDatat.add(templateData2);
				templateDatat.add(templateData3);
				templateDatat.add(templateData4);
				templateDatat.add(templateData5);
				templateDatat.add(templateData6);

				sendTempMsg.setDatalist(templateDatat);
				String url = request.getScheme() + "://" + request.getServerName() + "/"
						+ "ShangYiJia/toVisitDetailAuth.action";
				String state = order.getDoctorId()+","+order.getOrderNo();
				url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx00a647491b70a8fa&redirect_uri="
						+ url
						+ "&response_type=code&scope=snsapi_base&state="
						+ state + "#wechat_redirect";
				sendTempMsg.setUrl(url);
				SendTempUtil.sendMsg(sendTempMsg, wxPayReciDao);
			}
		}
		if(!StringUtil.isEmpty(order.getCouponid())){
			couponService.useCoupon(order.getOpenid(),order.getCouponid()+"");
		}
		couponService.addCoupon(order.getOpenid(), order.getOrderPrice());*/
		return i;
	}



	@Override
	public Integer queryCount(String orderNo) {
		return wxPayReciDao.queryCount(orderNo);
	}
	
}
