package cn.syrjia.hospital.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.syrjia.common.impl.BaseServiceImpl;
import cn.syrjia.hospital.dao.ClinicDao;
import cn.syrjia.hospital.entity.HosLabel;
import cn.syrjia.hospital.entity.Hospital;
import cn.syrjia.hospital.entity.MiddleUtil;
import cn.syrjia.hospital.service.ClinicService;

@Service("clinicService")
public class ClinicServiceImpl extends BaseServiceImpl implements ClinicService {

	@Resource(name="clinicDao")
	ClinicDao clinicDao;

	@Override
	public Hospital queryHosById(String id) {
		Hospital h = new Hospital();
		if(StringUtils.isEmpty(id)){
			id = clinicDao.queryIdByName("医珍堂").toString();
		}
		if(!StringUtils.isEmpty(id)){
			h = super.queryById(Hospital.class, id, true);
		}
		 //页面第一张图片
		if(h.getImgList()!=null){
			for(int i=0;i<h.getImgList().size();i++){
				h.setShowImg(h.getImgList().get(0).getPicPathUrl());
			}
		}
		//整合地址  
		String addr = h.getHosAddr();
		if(h.getHosProvice().equals(h.getHosCity())){
			h.setHosAddr(h.getHosProvice()+h.getHosCounty()+addr);
		}else{
			h.setHosAddr(h.getHosProvice()+h.getHosCity()+h.getHosCounty()+addr);
		}
		return h;
	}

	@Override
	public Map<String, Object> queryTopMsg(String hosId) {
		MiddleUtil mu = new MiddleUtil();
		List<MiddleUtil> muList = new ArrayList<MiddleUtil>();
		mu.setType("3");//医馆和标签关联
		mu.setHospitalId(hosId);
		muList =super.query(mu,false);
		List<HosLabel> hosLabels = new ArrayList<HosLabel>();
		if(muList!=null){
			for(MiddleUtil m:muList){
				HosLabel h=super.queryById(HosLabel.class,m.getLabelId());
				hosLabels.add(h);
			}
		}
		Map<String,Object> map =clinicDao.queryDepNumAndDocNum(hosId);//docNum为医生数量 depNum为科室数量
		map.put("hosLabels",hosLabels);
		return map;
	}

	@Override
	public List<Map<String, Object>> queryDepList(String hosId) {
		return clinicDao.queryDepList(hosId);
	}
}
