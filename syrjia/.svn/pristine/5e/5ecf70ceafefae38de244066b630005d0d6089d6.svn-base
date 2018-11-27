package cn.syrjia.quartz;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import cn.syrjia.common.BaseDaoInterface;
import cn.syrjia.dao.ImDao;
import cn.syrjia.entity.Order;
import cn.syrjia.hospital.dao.AppDoctorDao;
import cn.syrjia.util.GetSig;
import cn.syrjia.util.QuartzManager;
import cn.syrjia.util.StringUtil;
import cn.syrjia.util.Util;
import cn.syrjia.util.sendModelMsgUtil;
import net.sf.json.JSONObject;


public class ServerOverJob implements Job{

	
	 @Autowired
	 private BaseDaoInterface baseDao;
	 
	 @Autowired
	 private AppDoctorDao appDoctorDao;
	 
	 @Autowired
	 private ImDao imDao;
	 
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		String orderNo=context.getJobDetail().getJobDataMap().get("orderNo").toString();
		
		Map<String,Object> validityTime=appDoctorDao.queryOrderDetailServer(orderNo);
		if(null==validityTime){
			return;
		}
		
		
		Order order=new Order();
		order.setOrderNo(orderNo);
		order=baseDao.queryById(Order.class,orderNo);
		String from_account=order.getDoctorId();
		String to_account=order.getPatientId();
		
		if(order.getPaymentStatus()==5){
			return;
		}
		
		Map<String, Object> serverOrder = appDoctorDao.queryLastOrde(to_account,
				from_account);
		
		if(!orderNo.equals(serverOrder.get("orderNo").toString())){
			return;
		}
		
		if(Integer.parseInt(validityTime.get("validityTime").toString())-Util.queryNowTime()>100){
			QuartzManager.addJob("serverOver_"+orderNo,new Date(Integer.parseInt(validityTime.get("validityTime").toString())*1000L),orderNo);
			return;
		}
		
		order=new Order();
		order.setOrderNo(orderNo);
		order.setPaymentStatus(5);
		order.setOrderStatus(5);
		order.setEndTime(Util.queryNowTime());
		baseDao.updateEntity(order);
		
		JSONObject json=new JSONObject();
		json.put("msgType",17);
		json.put("orderNo",orderNo);
		json.put("content","结束问诊");
		GetSig.sendMsg(null,from_account,to_account,0,json);
		
		
		if(!StringUtil.isEmpty(serverOrder.get("twZhZxCount").toString())&&Integer.parseInt(serverOrder.get("twZhZxCount").toString())>0){
			json=new JSONObject();
			json.put("msgType",14);
			json.put("orderNo",orderNo);
			json.put("content",serverOrder.get("twZhZxCount").toString());
			GetSig.sendMsg(null,from_account,to_account,0,json);
			
			imDao.addZxCount(orderNo, Integer.parseInt(serverOrder.get("twZhZxCount").toString()));
			imDao.addZxCount(orderNo,from_account,Integer.parseInt(serverOrder.get("twZhZxCount").toString()));
		}
		
		
		Map<String, Object> doctor = appDoctorDao.queryOneDoctor(from_account);
		
		sendModelMsgUtil.sendEval(
				to_account,
				serverOrder.get("serverName").toString(),
				serverOrder.get("docName").toString(),
				doctor.get("positionName").toString(),
				doctor.get("infirmaryName").toString(),
				serverOrder.get("patientName").toString(),
				serverOrder.get("sex").toString(),
				serverOrder.get("age").toString(),
				appDoctorDao,
				"hospital/evaluate_doctor.html?orderNo="+orderNo);
	}

}
