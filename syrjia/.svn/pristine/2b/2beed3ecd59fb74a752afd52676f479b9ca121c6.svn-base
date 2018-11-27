package cn.syrjia.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.context.ContextLoader;

import cn.syrjia.config.Config;
import cn.syrjia.config.configCode;
import cn.syrjia.weixin.util.CommonUtil;

import com.google.gson.Gson;
import com.tls.sigcheck.tls_sigcheck;

@Controller
public class GetSig {
	
	static Config config;

	@Resource
	public void setConfig(Config config) {
		GetSig.config = config;
	}
	/**
	 * @param request
	 * @param identifier
	 *            用户标示符，即用户 id
	 * @return
	 */
	public static Map<String,Object> getSig(HttpServletRequest request, String identifier) {

		try {
			if(!identifier.equals("admin")){
				Map<String,Object> adminSig=getSig(request,"admin");
				if(null!=adminSig){
					String toAccount[]={identifier};
					Map<String,Object> m=new HashMap<String, Object>();
					m.put("To_Account", toAccount);
					String s1=CommonUtil.httpsRequest("https://console.tim.qq.com/v4/openim/im_get_attr?usersig="+adminSig.get("sig").toString()+"&identifier=admin&sdkappid="+config.getSdkAppid()+"&random="+Util.getRandom()+"&contenttype=json","POST",JSONObject.fromMap(m).toString());
					System.out.println(s1);
					Map<String,Object> user=JsonUtil.jsonToMap(s1);
					if(user.get("ErrorCode").toString().equals("90048")){
						m=new HashMap<String, Object>();
						m.put("Identifier", identifier);
						s1=CommonUtil.httpsRequest("https://console.tim.qq.com/v4/im_open_login_svc/account_import?usersig="+adminSig.get("sig").toString()+"&identifier=admin&sdkappid="+config.getSdkAppid()+"&random="+Util.getRandom()+"&contenttype=json","POST",JSONObject.fromMap(m).toString());
						System.out.println(s1);
					}
				}
			}
			
			tls_sigcheck demo = new tls_sigcheck();
			// 使用前请修改动态库的加载路径
			String path= ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath("/");
			demo.loadJniLib(path+"jnisigcheck" + File.separator + "jnisigcheck.dll");
			// demo.loadJniLib("/home/tls/tls_sig_api/src/jnisigcheck.so");
			File priKeyFile = new File(
					path+"jnisigcheck" + File.separator + "ec_key.pem");
			StringBuilder strBuilder = new StringBuilder();
			String s = "";

			BufferedReader br = new BufferedReader(new FileReader(priKeyFile));
			while ((s = br.readLine()) != null) {
				strBuilder.append(s + '\n');
			}
			br.close();
			String priKey = strBuilder.toString();
			int ret = demo.tls_gen_signature_ex2(config.getSdkAppid(),identifier,
					priKey);

			if (0 != ret) {
				System.out.println("ret " + ret + " " + demo.getErrMsg());
			} else {
				System.out.println("sig:\n" + demo.getSig());
			}

			File pubKeyFile = new File(path+"jnisigcheck" + File.separator + "public.pem");
			br = new BufferedReader(new FileReader(pubKeyFile));
			strBuilder.setLength(0);
			while ((s = br.readLine()) != null) {
				strBuilder.append(s + '\n');
			}
			br.close();
			String pubKey = strBuilder.toString();
			ret = demo.tls_check_signature_ex2(demo.getSig(), pubKey,
					config.getSdkAppid(),identifier);
			if (0 != ret) {
				System.out.println("ret " + ret + " " + demo.getErrMsg());
				return null;
			} else {
				System.out.println("--\nverify ok -- expire time "
						+ demo.getExpireTime() + " -- init time "
						+ demo.getInitTime());
				Map<String,Object> map=new HashMap<String, Object>();
				map.put("sig", demo.getSig());
				map.put("sdkAppid",config.getSdkAppid());
				map.put("accountType",config.getAccountType());
				return map;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	
	public static boolean accountImport(HttpServletRequest request,String identifier,String nick,String faceUrl){
		Map<String,Object> m =new HashMap<String,Object>();
		m.put("Identifier", identifier);
		m.put("Nick", nick);
		m.put("FaceUrl", faceUrl);
		m.put("Type",0);
		String s1=CommonUtil.httpsRequest("https://console.tim.qq.com/v4/im_open_login_svc/account_import?usersig="+getSig(request,"admin").get("sig").toString()+"&identifier=admin&sdkappid="+config.getSdkAppid()+"&random="+Util.getRandom()+"&contenttype=json","POST",new Gson().toJson(m).toString());
		if(null==s1){
			return accountImport(request, identifier, nick, faceUrl);
		}
		Map<String,Object> result=JsonUtil.jsonToMap(s1);
		if(result.get("ActionStatus").toString().equals("OK")){
			return true;
		}else{
			System.out.println(identifier);
			return accountImport(request, identifier, nick, faceUrl);
		}
	}
	
	public static Map<String,Object> accountImportRegister(HttpServletRequest request,String identifier,String nick,String faceUrl){
		Map<String,Object> m =new HashMap<String,Object>();
		m.put("Identifier", identifier);
		m.put("Nick", nick);
		m.put("FaceUrl", faceUrl);
		m.put("Type",0);
		String s1=CommonUtil.httpsRequest("https://console.tim.qq.com/v4/im_open_login_svc/account_import?usersig="+getSig(request,"admin").get("sig").toString()+"&identifier=admin&sdkappid="+config.getSdkAppid()+"&random="+Util.getRandom()+"&contenttype=json","POST",new Gson().toJson(m).toString());
		if(null==s1){
			return accountImportRegister(request, identifier, nick, faceUrl);
		}
		Map<String,Object> result=JsonUtil.jsonToMap(s1);
		return result;
	}
	
	public static boolean accountImports(HttpServletRequest request,String[] identifier,String nick,String faceUrl){
		Map<String,Object> m =new HashMap<String,Object>();
		m.put("Accounts", identifier);
		String s1=CommonUtil.httpsRequest("https://console.tim.qq.com/v4/im_open_login_svc/multiaccount_import?usersig="+getSig(request,"admin").get("sig").toString()+"&identifier=admin&sdkappid="+config.getSdkAppid()+"&random="+Util.getRandom()+"&contenttype=json","POST",new Gson().toJson(m).toString());
		Map<String,Object> result=JsonUtil.jsonToMap(s1);
		if(result.get("ActionStatus").toString().equals("OK")){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean friendAdd(HttpServletRequest request,String fromAccount,String toAccount,String faceUrl){
		Map<String,Object> m =new HashMap<String,Object>();
		m.put("From_Account", fromAccount);
		m.put("AddType","Add_Type_Both");
		m.put("ForceAddFlags",1);
		List<Map<String,Object>> addFriendItems=new ArrayList<Map<String,Object>>();
		
		Map<String,Object> addFriendItem=new HashMap<String, Object>();
		addFriendItem.put("To_Account",toAccount);
		addFriendItem.put("AddSource","AddSource_Type_System");
		addFriendItems.add(addFriendItem);
		
		m.put("AddFriendItem",addFriendItems);
		String s1=CommonUtil.httpsRequest("https://console.tim.qq.com/v4/sns/friend_add?usersig="+getSig(request,"admin").get("sig").toString()+"&identifier=admin&sdkappid="+config.getSdkAppid()+"&random="+Util.getRandom()+"&contenttype=json","POST",new Gson().toJson(m).toString());
		Map<String,Object> result=JsonUtil.jsonToMap(s1);
		if(result.get("ActionStatus").toString().equals("OK")){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 发送纯文字消息
	 * @param request
	 * @param fromAccount
	 * @param toAccount
	 * @param data
	 * @return
	 */
	public static Map<String,Object> sendMsg(HttpServletRequest request,String From_Account,String To_Account,Integer pushFlag,JSONObject data) {
		
		String desc=Util.getInfo(data.getString("msgType"));
		
		Map<String,Object> m=new HashMap<String, Object>();
		m.put("SyncOtherMachine",1);
		m.put("From_Account",From_Account);
		m.put("To_Account",To_Account);
		m.put("MsgRandom",Util.queryNowTime());
		m.put("MsgTimeStamp",Util.queryNowTime());
		List<Map<String,Object>> msgBody=new ArrayList<Map<String,Object>>();
		Map<String,Object> map=new HashMap<String, Object>();
		Map<String,Object> msgContent=new HashMap<String, Object>();
		data.put("msgId",Util.getUUID());
		System.out.println(data.toString());
		msgContent.put("Data",data.toString());
		//msgContent.put("msgId",Util.getUUID());
		msgContent.put("Desc",desc);
		msgContent.put("Ext",null);
		map.put("MsgType","TIMCustomElem");
		map.put("MsgContent",msgContent);
		msgBody.add(map);
		
		Map<String,Object> offlinePushInfo=new HashMap<String, Object>();
		offlinePushInfo.put("PushFlag",pushFlag);
		//offlinePushInfo.put("title","您有一条新消息");
		offlinePushInfo.put("Desc",desc);
		JSONObject Ext=new JSONObject();
		Ext.put("type",0);
		Ext.put("patientId",From_Account);
		offlinePushInfo.put("Ext",Ext.toString());
		m.put("OfflinePushInfo",offlinePushInfo);
		
		m.put("MsgBody",msgBody);
		String s1=CommonUtil.httpsRequest("https://console.tim.qq.com/v4/openim/sendmsg?usersig="+getSig(request,"admin").get("sig").toString()+"&identifier=admin&sdkappid="+config.getSdkAppid()+"&random="+Util.getRandom()+"&contenttype=json","POST",new Gson().toJson(m).toString());
		Map<String,Object> result=JsonUtil.jsonToMap(s1);
		if(result.get("ActionStatus").toString().equals("OK")){
			return Util.resultMap(configCode.code_1001,1);
		}else{
			if(result.get("ErrorCode").toString().equals("20003")){
				accountImport(request, To_Account, null,null);
				accountImport(request, From_Account, null,null);
				Integer sign_a = 0;
				do {
					sign_a++;
					sendMsg(request, From_Account, To_Account, pushFlag, data);
				} while (sign_a == 0);
			}
			return Util.resultMap(configCode.code_1066,null);
		}
	}
	
	public static Map<String,Object> sendMsgSync(HttpServletRequest request,String From_Account,String To_Account,JSONObject data) {
		
		String desc=Util.getInfo(data.getString("msgType"));
		
		Map<String,Object> m=new HashMap<String, Object>();
		m.put("SyncOtherMachine",1);
		m.put("From_Account",From_Account);
		m.put("To_Account",To_Account);
		m.put("MsgRandom",Util.queryNowTime());
		m.put("MsgTimeStamp",Util.queryNowTime());
		List<Map<String,Object>> msgBody=new ArrayList<Map<String,Object>>();
		Map<String,Object> map=new HashMap<String, Object>();
		Map<String,Object> msgContent=new HashMap<String, Object>();
		data.put("msgId",Util.getUUID());
		System.out.println(data.toString());
		msgContent.put("Data",data.toString());
		//msgContent.put("msgId",Util.getUUID());
		msgContent.put("Desc",desc);
		msgContent.put("Ext",null);
		map.put("MsgType","TIMCustomElem");
		map.put("MsgContent",msgContent);
		msgBody.add(map);
		
		Map<String,Object> offlinePushInfo=new HashMap<String, Object>();
		offlinePushInfo.put("PushFlag",0);
		//offlinePushInfo.put("title","您有一条新消息");
		offlinePushInfo.put("Desc",desc);
		JSONObject Ext=new JSONObject();
		Ext.put("type",0);
		Ext.put("patientId",From_Account);
		offlinePushInfo.put("Ext",Ext.toString());
		m.put("OfflinePushInfo",offlinePushInfo);
		
		m.put("MsgBody",msgBody);
		String s1=CommonUtil.httpsRequest("https://console.tim.qq.com/v4/openim/sendmsg?usersig="+getSig(request,"admin").get("sig").toString()+"&identifier=admin&sdkappid="+config.getSdkAppid()+"&random="+Util.getRandom()+"&contenttype=json","POST",new Gson().toJson(m).toString());
		Map<String,Object> result=JsonUtil.jsonToMap(s1);
		if(result.get("ActionStatus").toString().equals("OK")){
			return Util.resultMap(configCode.code_1001,1);
		}else{
			if(result.get("ErrorCode").toString().equals("20003")){
				accountImport(request, To_Account, null,null);
				accountImport(request, From_Account, null,null);
				Integer sign_a = 0;
				do {
					sign_a++;
					sendMsgSync(request, From_Account, To_Account, data);
				} while (sign_a == 0);
				
			}
			return Util.resultMap(configCode.code_1066,null);
		}
	}
	
	public static Map<String,Object> sendMsgTest(HttpServletRequest request,String From_Account,String To_Account,String content,String serverOrderNo) {
		Map<String,Object> m=new HashMap<String, Object>();
		m.put("SyncOtherMachine",1);
		m.put("From_Account",From_Account);
		m.put("To_Account",To_Account);
		m.put("MsgRandom",Util.queryNowTime());
		m.put("MsgTimeStamp",Util.queryNowTime());
		List<Map<String,Object>> msgBody=new ArrayList<Map<String,Object>>();
		Map<String,Object> map=new HashMap<String, Object>();
		Map<String,Object> msgContent=new HashMap<String, Object>();
		
		Map<String,Object> custom=new HashMap<String, Object>();
		Map<String,Object> customContent=new HashMap<String, Object>();
		JSONObject json=new JSONObject();
		json.put("msgId",Util.getUUID());
		json.put("serverOrderNo",serverOrderNo);
		customContent.put("Data",json.toString());
		custom.put("MsgType","TIMCustomElem");
		custom.put("MsgContent",customContent);
		
		msgContent.put("Text",content);
		map.put("MsgType","TIMTextElem");
		map.put("MsgContent",msgContent);
		msgBody.add(map);
		msgBody.add(custom);
		
		Map<String,Object> offlinePushInfo=new HashMap<String, Object>();
		offlinePushInfo.put("PushFlag",0);
		//offlinePushInfo.put("title","您有一条新消息");
		offlinePushInfo.put("Desc",content);
		JSONObject Ext=new JSONObject();
		json.put("type",0);
		json.put("patientId",From_Account);
		offlinePushInfo.put("Ext",Ext.toString());
		m.put("OfflinePushInfo",offlinePushInfo);
		
		m.put("MsgBody",msgBody);
		String s1=CommonUtil.httpsRequest("https://console.tim.qq.com/v4/openim/sendmsg?usersig="+getSig(request,"admin").get("sig").toString()+"&identifier=admin&sdkappid="+config.getSdkAppid()+"&random="+Util.getRandom()+"&contenttype=json","POST",new Gson().toJson(m).toString());
		Map<String,Object> result=JsonUtil.jsonToMap(s1);
		if(result.get("ActionStatus").toString().equals("OK")){
			return Util.resultMap(configCode.code_1001,1);
		}else{
			if(result.get("ErrorCode").toString().equals("20003")){
				Integer sign_a = 0;
				do {
					sign_a++;
					accountImport(request, To_Account, null,null);
					sendMsgTest(request, From_Account, To_Account, content, serverOrderNo);
				} while (sign_a == 0);
			}
			return Util.resultMap(configCode.code_1066,null);
		}
	}
	
	public static Map<String,Object> sendMsgTestSync(HttpServletRequest request,String From_Account,String To_Account,String content,String serverOrderNo) {
		Map<String,Object> m=new HashMap<String, Object>();
		m.put("SyncOtherMachine",1);
		m.put("From_Account",From_Account);
		m.put("To_Account",To_Account);
		m.put("MsgRandom",Util.queryNowTime());
		m.put("MsgTimeStamp",Util.queryNowTime());
		List<Map<String,Object>> msgBody=new ArrayList<Map<String,Object>>();
		Map<String,Object> map=new HashMap<String, Object>();
		Map<String,Object> msgContent=new HashMap<String, Object>();
		
		Map<String,Object> custom=new HashMap<String, Object>();
		Map<String,Object> customContent=new HashMap<String, Object>();
		JSONObject json=new JSONObject();
		json.put("msgId",Util.getUUID());
		json.put("serverOrderNo",serverOrderNo);
		customContent.put("Data",json.toString());
		custom.put("MsgType","TIMCustomElem");
		custom.put("MsgContent",customContent);
		
		msgContent.put("Text",content);
		map.put("MsgType","TIMTextElem");
		map.put("MsgContent",msgContent);
		msgBody.add(map);
		msgBody.add(custom);
		
		Map<String,Object> offlinePushInfo=new HashMap<String, Object>();
		offlinePushInfo.put("PushFlag",0);
		//offlinePushInfo.put("title","您有一条新消息");
		offlinePushInfo.put("Desc",content);
		JSONObject Ext=new JSONObject();
		json.put("type",0);
		json.put("patientId",From_Account);
		offlinePushInfo.put("Ext",Ext.toString());
		m.put("OfflinePushInfo",offlinePushInfo);
		
		m.put("MsgBody",msgBody);
		String s1=CommonUtil.httpsRequest("https://console.tim.qq.com/v4/openim/sendmsg?usersig="+getSig(request,"admin").get("sig").toString()+"&identifier=admin&sdkappid="+config.getSdkAppid()+"&random="+Util.getRandom()+"&contenttype=json","POST",new Gson().toJson(m).toString());
		Map<String,Object> result=JsonUtil.jsonToMap(s1);
		if(result.get("ActionStatus").toString().equals("OK")){
			return Util.resultMap(configCode.code_1001,1);
		}else{
			if(result.get("ErrorCode").toString().equals("20003")){
				Integer sign_a = 0;
				do {
					sign_a++;
					accountImport(request, To_Account, null,null);
					sendMsgTestSync(request, From_Account, To_Account, content, serverOrderNo);
				} while (sign_a == 0);
				
			}
			return Util.resultMap(configCode.code_1066,null);
		}
	}
	
	public static Map<String,Object> sendMsgImage(HttpServletRequest request,String From_Account,String To_Account,Map<String,Object> oldMsgContent) {
		Map<String,Object> m=new HashMap<String, Object>();
		m.put("SyncOtherMachine",1);
		m.put("From_Account",From_Account);
		m.put("To_Account",To_Account);
		m.put("MsgRandom",Util.queryNowTime());
		m.put("MsgTimeStamp",Util.queryNowTime());
		List<Map<String,Object>> msgBody=new ArrayList<Map<String,Object>>();
		Map<String,Object> map=new HashMap<String, Object>();
		Map<String,Object> msgContent=new HashMap<String, Object>();
		List<Map<String,Object>> imageInfoArrays=new ArrayList<Map<String,Object>>();
		Map<String,Object> imageInfoArrayB=new HashMap<String, Object>();//大图
		Map<String,Object> imageInfoArrayO=new HashMap<String, Object>();//原图
		Map<String,Object> imageInfoArrayS=new HashMap<String, Object>();//缩量图
		
		imageInfoArrayB.put("Type",2);
		imageInfoArrayB.put("Size",0);
		imageInfoArrayB.put("Width",0);
		imageInfoArrayB.put("Height",0);
		imageInfoArrayB.put("URL",oldMsgContent.get("bigPictureUrl").toString());
		
		imageInfoArrayO.put("Type",1);
		imageInfoArrayO.put("Size",0);
		imageInfoArrayO.put("Width",0);
		imageInfoArrayO.put("Height",0);
		imageInfoArrayO.put("URL",oldMsgContent.get("originalUrl").toString());
		
		imageInfoArrayS.put("Type",3);
		imageInfoArrayS.put("Size",oldMsgContent.get("size").toString());
		imageInfoArrayS.put("Width",oldMsgContent.get("width").toString());
		imageInfoArrayS.put("Height",oldMsgContent.get("height").toString());
		imageInfoArrayS.put("URL",oldMsgContent.get("shrinkingMapUrl").toString());
		
		
		imageInfoArrays.add(imageInfoArrayB);
		imageInfoArrays.add(imageInfoArrayO);
		imageInfoArrays.add(imageInfoArrayS);
		//msgContent.put("UUID",oldMsgContent.get("uuid").toString());
		msgContent.put("ImageFormat",2);
		msgContent.put("ImageInfoArray",imageInfoArrays);
		map.put("MsgType","TIMImageElem");
		map.put("MsgContent",msgContent);
		
		Map<String,Object> custom=new HashMap<String, Object>();
		Map<String,Object> customContent=new HashMap<String, Object>();
		JSONObject json=new JSONObject();
		json.put("msgId",Util.getUUID());
		customContent.put("Data",json.toString());
		custom.put("MsgType","TIMCustomElem");
		custom.put("MsgContent",customContent);
		
		
		msgBody.add(map);
		msgBody.add(custom);
		m.put("MsgBody",msgBody);
		String s1=CommonUtil.httpsRequest("https://console.tim.qq.com/v4/openim/sendmsg?usersig="+getSig(request,"admin").get("sig").toString()+"&identifier=admin&sdkappid="+config.getSdkAppid()+"&random="+Util.getRandom()+"&contenttype=json","POST",new Gson().toJson(m).toString());
		Map<String,Object> result=JsonUtil.jsonToMap(s1);
		if(result.get("ActionStatus").toString().equals("OK")){
			return Util.resultMap(configCode.code_1001,1);
		}else{
			if(result.get("ErrorCode").toString().equals("20003")){
				Integer sign_a = 0;
				do {
					sign_a++;
					accountImport(request, To_Account, null,null);
					sendMsgImage(request, From_Account, To_Account, oldMsgContent);
				} while (sign_a == 0);
			}
			return Util.resultMap(configCode.code_1066,null);
		}
	}
	
	public static Map<String,Object> sendMsg(HttpServletRequest request,String From_Account,String To_Account,String orderNo,String name,Integer type) {
		JSONObject json=new JSONObject();
		if(type==4){
			json.put("msgType",23);
			json.put("serverOrderNo",orderNo);
			json.put("serverId",type);
			json.put("type",type);
			json.put("dataId",name);
			json.put("content","就诊人已支付图文调理服务，请在24小时内进行回复");
			return GetSig.sendMsg(request,From_Account,To_Account,1,json);
		}
		if(type==5){
			json.put("msgType",23);
			json.put("serverOrderNo",orderNo);
			json.put("serverId",type);
			json.put("type",type);
			json.put("dataId",name);
			json.put("content","图文复诊");
			return GetSig.sendMsg(request,From_Account,To_Account,1,json);
		}
		if(type==6){
			json.put("msgType",23);
			json.put("serverOrderNo",orderNo);
			json.put("serverId",type);
			json.put("type",type);
			json.put("dataId",name);
			json.put("content","就诊人已支付图文咨询服务，请在24小时内进行回复");
			return GetSig.sendMsg(request,From_Account,To_Account,1,json);
		}
		if(type==7){
			json.put("msgType",23);
			json.put("serverOrderNo",orderNo);
			json.put("serverId",type);
			json.put("type",type);
			json.put("dataId",name);
			json.put("content","就诊人已支付电话调理服务，客服将在30分钟内致电与您确认调理服务时间，请留意您的来电哦");
			return	GetSig.sendMsg(request,From_Account,To_Account,1,json);
		}
		if(type==8){
			json.put("msgType",23);
			json.put("serverOrderNo",orderNo);
			json.put("serverId",type);
			json.put("type",type);
			json.put("dataId",name);
			json.put("content","就诊人已支付电话咨询服务，客服将在30分钟内致电与您确认调理服务时间，请留意您的来电哦");
			return GetSig.sendMsg(request,From_Account,To_Account,1,json);
		}
		if(type==9){
			json.put("msgType",23);
			json.put("serverOrderNo",orderNo);
			json.put("serverId",type);
			json.put("type",type);
			json.put("dataId",name);
			json.put("content","电话复诊");
			return GetSig.sendMsg(request,From_Account,To_Account,1,json);
		}
		return null;
	}
		
}
