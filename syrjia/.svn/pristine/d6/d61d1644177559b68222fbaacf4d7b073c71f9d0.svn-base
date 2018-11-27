package cn.syrjia.hospital.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.syrjia.common.impl.BaseServiceImpl;
import cn.syrjia.config.configCode;
import cn.syrjia.hospital.dao.AppDoctorDao;
import cn.syrjia.hospital.dao.HealthProDao;
import cn.syrjia.hospital.service.HealthProductService;
import cn.syrjia.util.StringUtil;
import cn.syrjia.util.Util;

@Service("healthProductService")
public class HealthProductServiceImpl extends BaseServiceImpl implements HealthProductService {

	@Resource(name = "healthProDao")
	HealthProDao healthProDao;
	
	@Resource(name = "appDoctorDao")
	AppDoctorDao appDoctorDao;

	@Override
	public Map<String, Object> queryHealthProducts(String doctorId, String name, String type, Integer page,
			Integer row) {
		List<Map<String, Object>> products = new ArrayList<Map<String, Object>>();
		try {
			Map<String, Object> sysMap = healthProDao.querySysSet();
			String yztId = sysMap == null || StringUtil.isEmpty(sysMap.get("yztId")) ? "YZT000001"
					: sysMap.get("yztId").toString();
			// 新版多医珍堂逻辑
			if (!StringUtil.isEmpty(doctorId)) {
				Map<String, Object> docMap = appDoctorDao.queryDocById(doctorId);
				if (docMap == null) {
					return Util.resultMap(configCode.code_1142, null);
				}
				yztId = !StringUtil.isEmpty(docMap.get("customYztId")) ? docMap.get("customYztId").toString()
						: !StringUtil.isEmpty(docMap.get("defaultYztId")) ? docMap.get("defaultYztId").toString()
								: null;
				if (StringUtil.isEmpty(yztId)) {
					return Util.resultMap(configCode.code_1143, null);
				}
			}
			products = healthProDao.queryHealthProducts(yztId, name, type, page, row);
			if (products == null) {
				return Util.resultMap(configCode.code_1011, products);
			}
			return Util.resultMap(configCode.code_1001, products);
		} catch (Exception e) {
			System.out.println("查询药品名录接口异常:" + e);
			return Util.resultMap(configCode.code_1015, products);
		}
	}

	@Override
	public Map<String, Object> applyAddDrug(String doctorId, String content) {
		if (StringUtil.isEmpty(doctorId) || StringUtil.isEmpty(content)) {
			return Util.resultMap(configCode.code_1029, null);
		} else {
			healthProDao.applyAddDrug(doctorId, content);
			return Util.resultMap(configCode.code_1001, 1);
		}
	}

}
