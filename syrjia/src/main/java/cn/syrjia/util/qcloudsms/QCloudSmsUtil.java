package cn.syrjia.util.qcloudsms;

import java.io.IOException;

import org.json.JSONException;

import cn.syrjia.util.SMSTemplateIdUtil;
import cn.syrjia.util.qcloudsms.httpclient.HTTPException;

public class QCloudSmsUtil {

	// 签名
	// NOTE: 这里的签名"腾讯云"只是一个示例，
	// 真实的签名需要在短信控制台中申请，另外
	// 签名参数使用的是`签名内容`，而不是`签名ID`
	String smsSign = "腾讯云";

	public static String singleSendSms(String phone, String code) {
		// 单发短信
		try {
			SmsSingleSender ssender = new SmsSingleSender(
					SMSTemplateIdUtil.accessKeyId,
					SMSTemplateIdUtil.accessKeySecret);
			SmsSingleSenderResult result = ssender.send(0, "86", phone,
					"【腾讯云】您的验证码是: " + code + "", "", "");
			System.out.print(result);
			return result.toString();
		} catch (HTTPException e) {
			// HTTP响应码错误
			e.printStackTrace();
		} catch (JSONException e) {
			// json解析错误
			e.printStackTrace();
		} catch (IOException e) {
			// 网络IO错误
			e.printStackTrace();
		}
		return null;
	}

	// 指定模板ID单发短信
	public static Boolean sendSmsByTemId(String phone, int templateId,
			String[] params) {
		try {
			// 需要发送短信的手机号码
			String[] phoneNumbers = { phone };
			SmsMultiSender msender = new SmsMultiSender(
					SMSTemplateIdUtil.accessKeyId,
					SMSTemplateIdUtil.accessKeySecret);
			SmsMultiSenderResult result = msender.sendWithParam("86",
					phoneNumbers, templateId, params,
					SMSTemplateIdUtil.accessSignName, "", ""); // 签名参数未提供或者为空时，会使用默认签名发送短信
			System.out.print(result);
			if(result.result ==0){
				return true;
			}
		} catch (HTTPException e) {
			// HTTP响应码错误
			e.printStackTrace();
		} catch (JSONException e) {
			// json解析错误
			e.printStackTrace();
		} catch (IOException e) {
			// 网络IO错误
			e.printStackTrace();
		}
		return false;
	}

	// 发送语音验证码
	public static Boolean sendVoiceSms(String phone, String code) {

		try {
			SmsVoiceVerifyCodeSender vvcsender = new SmsVoiceVerifyCodeSender(
					SMSTemplateIdUtil.accessKeyId,
					SMSTemplateIdUtil.accessKeySecret);
			SmsVoiceVerifyCodeSenderResult result = vvcsender.send("86", phone,
					code, 2, "");
			System.out.print(result);
			if(result.result ==0){
				return true;
			}
		} catch (HTTPException e) {
			// HTTP响应码错误
			e.printStackTrace();
		} catch (JSONException e) {
			// json解析错误
			e.printStackTrace();
		} catch (IOException e) {
			// 网络IO错误
			e.printStackTrace();
		}
		return false;
	}

	// 发送语音通知
	public static String sendVoiceSmsNotice(String phone, String code) {

		try {
			SmsVoicePromptSender vpsender = new SmsVoicePromptSender(
					SMSTemplateIdUtil.accessKeyId,
					SMSTemplateIdUtil.accessKeySecret);
			SmsVoicePromptSenderResult result = vpsender.send("86", phone, 2,
					2, code, "");
			System.out.print(result);
		} catch (HTTPException e) {
			// HTTP响应码错误
			e.printStackTrace();
		} catch (JSONException e) {
			// json解析错误
			e.printStackTrace();
		} catch (IOException e) {
			// 网络IO错误
			e.printStackTrace();
		}
		return null;
	}

	// 拉取短信回执以及回复
	public static String querySmsReply(String phone, String code) {
		try {
			// Note: 短信拉取功能需要联系腾讯云短信技术支持(QQ:3012203387)开通权限
			int maxNum = 10; // 单次拉取最大量
			SmsStatusPuller spuller = new SmsStatusPuller(
					SMSTemplateIdUtil.accessKeyId,
					SMSTemplateIdUtil.accessKeySecret);

			// 拉取短信回执
			SmsStatusPullCallbackResult callbackResult = spuller
					.pullCallback(maxNum);
			System.out.println(callbackResult);

			// 拉取回复
			SmsStatusPullReplyResult replyResult = spuller.pullReply(maxNum);
			System.out.println(replyResult);
		} catch (HTTPException e) {
			// HTTP响应码错误
			e.printStackTrace();
		} catch (JSONException e) {
			// json解析错误
			e.printStackTrace();
		} catch (IOException e) {
			// 网络IO错误
			e.printStackTrace();
		}
		return null;
	}

	// 拉取单个手机短信状态 开始时间(unix timestamp)10位
	public static String queryOnePhoneSmsState(Integer beginTime,
			Integer endTime, String phone, String code) {

		try {
			int maxNum = 10; // 单次拉取最大量
			SmsMobileStatusPuller mspuller = new SmsMobileStatusPuller(
					SMSTemplateIdUtil.accessKeyId,
					SMSTemplateIdUtil.accessKeySecret);

			// 拉取短信回执
			SmsStatusPullCallbackResult callbackResult = mspuller.pullCallback(
					"86", phone, beginTime, endTime, maxNum);
			System.out.println(callbackResult);

			// 拉取回复
			SmsStatusPullReplyResult replyResult = mspuller.pullReply("86",
					phone, beginTime, endTime, maxNum);
			System.out.println(replyResult);
		} catch (HTTPException e) {
			// HTTP响应码错误
			e.printStackTrace();
		} catch (JSONException e) {
			// json解析错误
			e.printStackTrace();
		} catch (IOException e) {
			// 网络IO错误
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		String[] params = {"2834"};
		sendSmsByTemId("15226514965", SMSTemplateIdUtil.sendCode_SMS, params);
		sendVoiceSms("15533989147", "8773");
	}
}
