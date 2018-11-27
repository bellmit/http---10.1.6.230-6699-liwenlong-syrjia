package cn.syrjia.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;

import cn.syrjia.common.LogServiceInterface;

/**
 * 
 * @param <E>
 */
public class LogAop {

	@Autowired
	HttpServletRequest request;

	@Resource(name = "logService")
	LogServiceInterface logService;

	/**
	 * 返回后执行加入日志
	 */
	public void addLog(JoinPoint joinPoint, Object returnVal) {
	/*	try {
			CustomerUserDetails userDetail = (CustomerUserDetails) SecurityContextHolder
					.getContext().getAuthentication().getPrincipal();
			String id = userDetail.getUser().getId();
			
			String userName = userDetail.getUser().getUserName();

			Log log = new Log();

			log.setIp(Util.getIp(request));// Ip

			log.setCreatTime((System.currentTimeMillis() / 1000) + "");// 时间戳

			log.setUserId(id);
			
			StringBuilder details=new StringBuilder();
			
			for(Object obj:joinPoint.getArgs()){
				if(null==obj){
					continue;
				}
				if(obj.getClass().isArray()){
					Object[] o=(Object[])obj;
					details.append(Arrays.toString(o));
				}else{
					details.append(obj.toString());
				}
			}
			
			log.setDetails(details.toString());
			
			String methodName = joinPoint.getSignature().getName();

			if (returnVal instanceof Integer) {
				Integer state = Integer.parseInt(returnVal.toString());
				log.setState(state > 0 ? 0 : 1);
			} else if (returnVal instanceof List) {
				List<?> list = (List<?>) returnVal;
				log.setState(null == list || list.size() == 0 ? 0 : 1);
			} else if (returnVal instanceof Map) {
				Map<?, ?> map = (Map<?, ?>) returnVal;
				log.setState(null == map || map.size() == 0 ? 0 : 1);
			} else {
				log.setState(null == returnVal ? 0 : 1);
			}

			if (methodName.indexOf("addOrUpdate") != -1) {
				Object obj = joinPoint.getArgs()[0];
				if (!obj.getClass().isPrimitive()
						&& !obj.getClass().isAssignableFrom(String.class)) {
					try {
						Object o = DaoUtil.getEntityDeclared(obj,
								DaoUtil.getAnnotationId(obj.getClass()));
						log.setType(null == o ? "增加" : "修改");
					} catch (Exception e) {
						log.setType("修改");
					}
				} else {
					log.setType("增加");
				}
			} else if (methodName.indexOf("add") != -1) {
				log.setType("增加");
			} else if (methodName.indexOf("delete") != -1||methodName.indexOf("batchUpdate") != -1) {
				log.setType("删除");
			} else if (methodName.indexOf("update") != -1||methodName.indexOf("openOrDisableOrDeleteUser") != -1) {
				log.setType("修改");
			}

			Class<?> c = joinPoint.getSignature().getDeclaringType();

			String modular=null;
			
			if (c.isAssignableFrom(UserService.class)) {// 如果是uesr
				log.setModular("用户管理");
				modular="用户信息";
			} else if (c.isAssignableFrom(RoleService.class)) {
				log.setModular("角色管理");
				modular="角色信息";
			} else if (c.isAssignableFrom(MenuService.class)) {
				log.setModular("菜单管理");
				modular="菜单信息";
			}
			log.setContent(details.toString());
			logService.insertLog(log);
		} catch (NumberFormatException e) {

		}*/
	}
}
