package cn.syrjia.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import cn.syrjia.callCenter.util.CallCenterConfig;
import cn.syrjia.callCenter.util.SendCallCenterUtil;
import cn.syrjia.common.impl.BaseServiceImpl;
import cn.syrjia.config.configCode;
import cn.syrjia.dao.PatientDataDao;
import cn.syrjia.entity.PatientData;
import cn.syrjia.service.PatientDataService;
import cn.syrjia.util.GetOpenId;
import cn.syrjia.util.GetSig;
import cn.syrjia.util.PinyinUtil;
import cn.syrjia.util.StringUtil;
import cn.syrjia.util.Util;

/**
 * 患者信息service实现
 */
@Service("patientDataService")
public class PatientDataServiceImpl extends BaseServiceImpl implements
		PatientDataService {

	@Resource(name = "patientDataDao")
	public PatientDataDao patientDataDao;

	/**
	 * 添加患者信息
	 */
	@Override
	@Transactional
	public Map<String, Object> addPatientData(HttpServletRequest request,PatientData patientData) {
		try {
			//查询已有就诊人个数
			Integer ifFirst=patientDataDao.queryPatientCount(patientData.getMemberId(),null);
			if(ifFirst==0){
				patientData.setIsDefaultPer(1);
			}else{
				//查询已有就诊人个数
				Integer count=patientDataDao.queryPatientCount(patientData.getMemberId(),1);
				if(count>=100){
					return Util.resultMap(configCode.code_1126,null);
				}
			}
			if (patientData != null && patientData.getIsDefaultPer()==1) {
				//修改默认患者
				patientDataDao.updatePatientIsDefault(patientData.getMemberId());
			}
			patientData.setNameShort(PinyinUtil.convertHanzi2Pinyin(PinyinUtil.replaceSpecStr(patientData.getName()), false).toUpperCase());
			patientData
					.setCreateTime((int) (System.currentTimeMillis() / 1000));
			//执行新增
			Object i = patientDataDao.addEntityUUID(patientData);
			if (i != null) {
				//查询同步呼叫中心数据
				Map<String,Object> map = patientDataDao.querySendCallCenterData(i.toString());
				if(map!=null){
					String result = SendCallCenterUtil.sendCallCenterData(map,CallCenterConfig.CustomersPatient);
					System.out.println(result);
				}
				GetSig.accountImport(request,i.toString(),patientData.getName(),null);
				return Util.resultMap(configCode.code_1001, i);
			} else {
				return Util.resultMap(configCode.code_1010, i);
			}
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus()
					.setRollbackOnly();
			return Util.resultMap(configCode.code_1066, null);
		}
	}

	/**
	 * 修改患者信息
	 */
	@Override
	public Map<String, Object> updatePatientData(PatientData patientData) {

		try {
			if (patientData != null && patientData.getIsDefaultPer()==1) {
				//修改默认患者
				patientDataDao.updatePatientIsDefault(patientData.getMemberId());
			}
			patientData.setNameShort(PinyinUtil.convertHanzi2Pinyin(PinyinUtil.replaceSpecStr(patientData.getName()), false).toUpperCase());
			patientData
					.setCreateTime((int) (System.currentTimeMillis() / 1000));
			//更新
			Integer i = patientDataDao.updateEntity(patientData);
			if (i > 0) {
				//查询同步呼叫中心数据
				Map<String,Object> map = patientDataDao.querySendCallCenterData(patientData.getId());
				if(map!=null){
					String result = SendCallCenterUtil.sendCallCenterData(map,CallCenterConfig.CustomersPatient);
					System.out.println(result);
				}
				return Util.resultMap(configCode.code_1001, i);
			} else {
				return Util.resultMap(configCode.code_1014, i);
			}
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus()
					.setRollbackOnly();
			return Util.resultMap(configCode.code_1066, null);
		}
	}

	/**
	 * 查询患者信息
	 */
	@Override
	public Map<String, Object> queryPatientList(String userId) {
		List<Map<String, Object>> list = patientDataDao.queryPatientList(userId);
		if(list==null){
			return Util.resultMap(configCode.code_1015, list);
		}else{
			return Util.resultMap(configCode.code_1001, list);
		}
	}

	/**
	 * 删除患者信息（假删）
	 */
	@Override
	public Map<String, Object> deletePatient(String id) {
		if(StringUtil.isEmpty(id)){
			return Util.resultMap(configCode.code_1029, null);
		}
		Integer i = patientDataDao.deletePatient(id);
		if(i>0){
			//查询同步呼叫中心数据
			Map<String,Object> map = patientDataDao.querySendCallCenterData(id);
			if(map!=null){
				String result = SendCallCenterUtil.sendCallCenterData(map,CallCenterConfig.CustomersPatient);
				System.out.println(result);
			}
			return Util.resultMap(configCode.code_1001, i);
		}else{
			return Util.resultMap(configCode.code_1066, null);
		}
	}

	/**
	 * 获取默认患者信息
	 */
	@Override
	public Map<String, Object> queryPatientByDefault(String userId) {
		Map<String, Object> map = patientDataDao.queryPatientByDefault(userId);
		return Util.resultMap(configCode.code_1001, map);
	}

	/**
	 * 获取默认患者关系列表
	 */
	@Override
	public Map<String, Object> queryPatientNexusList() {
		List<Map<String, Object>> nexuslist = patientDataDao.queryPatientNexusList();
		return Util.resultMap(configCode.code_1001, nexuslist);
	}
	
	/**
	 * 设置默认就诊人
	 */
	@Override
	public Map<String, Object> defPatient(String id) {
		Integer i = patientDataDao.defPatient(id);
		if(i>0){
			return Util.resultMap(configCode.code_1001, i);
		}else{
			return Util.resultMap(configCode.code_1066, null);
		}
	}

	/**
	 * 查询已有就诊人个数
	 */
	@Override
	public Map<String, Object> queryPatientCount(HttpServletRequest request,String memberId) {
		if(StringUtil.isEmpty(memberId)){
			memberId = GetOpenId.getMemberId(request);
		}
		if(StringUtil.isEmpty(memberId)){
			return Util.resultMap(configCode.code_1029, null);
		}
		Integer i = patientDataDao.queryPatientCount(memberId,1);
		return Util.resultMap(configCode.code_1001, i);
	}

}