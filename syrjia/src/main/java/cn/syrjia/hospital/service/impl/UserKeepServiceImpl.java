package cn.syrjia.hospital.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import cn.syrjia.common.impl.BaseServiceImpl;
import cn.syrjia.hospital.dao.UserKeepDao;
import cn.syrjia.hospital.entity.UserKeep;
import cn.syrjia.hospital.service.UserKeepService;
import cn.syrjia.util.SessionUtil;

@Service("userKeepService")
public class UserKeepServiceImpl extends BaseServiceImpl implements
		UserKeepService {

	@Resource(name = "userKeepDao")
	UserKeepDao userKeepDao;

	@Override
	public Object editKeep(HttpServletRequest request, UserKeep keep) {
		Object i = 0;
		String openid = SessionUtil.getOpenId(request);
		keep.setOpenid(openid);
		keep.setType("0");
		Map<String, Object> map = new HashMap<String, Object>();
		map = userKeepDao.queryDoctorId(keep);
		System.out.println(map.get(keep.getDoctorId()));
		if (null == map.get(keep.getDoctorId())) {
			keep.setCreateTime((int) (System.currentTimeMillis() / 1000));
			i = userKeepDao.addEntity(keep);
		} else {
			i = userKeepDao.deleteUserKeep(keep);
		}
		return i;
	}

}
