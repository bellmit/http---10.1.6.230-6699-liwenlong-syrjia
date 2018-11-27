package cn.syrjia.quartz;
import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.syrjia.service.OrderService;

@Component("exampleTask")
public class ExampleTask {
	
	@Resource(name="orderService")
	OrderService orderService;
	
	@Scheduled(cron="0 0 10 * * ?")   //每天10点
    public void excuteEveryMin(){ 
    	/**
    	 * 遍历查询待订单，前一天通知客服处理
    	 */
    	orderService.queryNoFinishOrders();
		
    }
}
