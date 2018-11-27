package cn.syrjia.hospital.entity;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.OneToMany;
import cn.syrjia.common.annotation.Table;
import cn.syrjia.entity.Piclib;


/**
 * 
 * @author liwenlong 
 * 医馆表
 */
@Table(name = "h_hospital")
public class Hospital implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键
	 */
	@Id(increment = false)
	private String hospitalId;
	
	/**
	 * 医馆名称
	 */
	@Column
	private String hospitalName;
	
	/**
	 * 医馆所在省
	 */
	@Column
	private String hosProvice;
	
	/**
	 * 医馆所在市
	 */
	@Column
	private String hosCity;
	
	/**
	 * 医馆所在县区
	 */
	@Column
	private String hosCounty;	
	
	/**
	 * 经度
	 */
	private String latitude;
	
	/**
	 * 纬度
	 */
	private String longitude;
	
	
	/**
	 * 医馆详细地址
	 */
	@Column
	private String hosAddr;
	
	/**
	 * 医馆LOGO url
	 */
	@Column
	private String hosUrl;
	
	/**
	 * 医馆LOGO 本地URl
	 */
	@Column
	private String hosLocalUrl;
	
	/**
	 * 医馆启禁用状态 0-禁用 1-启用
	 */
	@Column
	private String hosIsOn;
	
	/**
	 * 医馆删除标识10-在用 30-删除
	 */
	@Column
	private String hosStatus;
	
	/**
	 * 医馆创建时间
	 */
	@Column
	private Integer createTime;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 电话
	 */
	private String phone;
	
	/**
	 * 展示的图片
	 */
	private String showImg;
	
	/**
	 *是否推荐 
	 */
	@Column
	private Integer isRecommend;

	/**
	 * 图片列表
	 */
	@OneToMany(correlationField="hospitalId",correlationOtherField="goodId")
	private List<Piclib> imgList;

	/*@OneToMany(correlationField="hospitalId",correlationOtherField="hosId")
	private List<HosLabel> labelList;*/
	
	/**
	 * 医生数量
	 */
	private Integer docNum;
	/**
	 * 科室数量
	 */
	private Integer depNum;
	
	public String getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(String hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getHosProvice() {
		return hosProvice;
	}

	public void setHosProvice(String hosProvice) {
		this.hosProvice = hosProvice;
	}

	public String getHosCity() {
		return hosCity;
	}

	public void setHosCity(String hosCity) {
		this.hosCity = hosCity;
	}

	public String getHosCounty() {
		return hosCounty;
	}

	public void setHosCounty(String hosCounty) {
		this.hosCounty = hosCounty;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getHosAddr() {
		return hosAddr;
	}

	public void setHosAddr(String hosAddr) {
		this.hosAddr = hosAddr;
	}

	public String getHosUrl() {
		return hosUrl;
	}

	public void setHosUrl(String hosUrl) {
		this.hosUrl = hosUrl;
	}

	public String getHosLocalUrl() {
		return hosLocalUrl;
	}

	public void setHosLocalUrl(String hosLocalUrl) {
		this.hosLocalUrl = hosLocalUrl;
	}

	public String getHosIsOn() {
		return hosIsOn;
	}

	public void setHosIsOn(String hosIsOn) {
		this.hosIsOn = hosIsOn;
	}

	public String getHosStatus() {
		return hosStatus;
	}

	public void setHosStatus(String hosStatus) {
		this.hosStatus = hosStatus;
	}

	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	

	public List<Piclib> getImgList() {
		return imgList;
	}

	public void setImgList(List<Piclib> imgList) {
		this.imgList = imgList;
	}

	/*public List<HosLabel> getLabelList() {
		return labelList;
	}

	public void setLabelList(List<HosLabel> labelList) {
		this.labelList = labelList;
	}*/

	public String getShowImg() {
		return showImg;
	}

	public void setShowImg(String showImg) {
		this.showImg = showImg;
	}

	public Integer getDocNum() {
		return docNum;
	}

	public void setDocNum(Integer docNum) {
		this.docNum = docNum;
	}

	public Integer getDepNum() {
		return depNum;
	}

	public void setDepNum(Integer depNum) {
		this.depNum = depNum;
	}

	public Integer getIsRecommend() {
		return isRecommend;
	}

	public void setIsRecommend(Integer isRecommend) {
		this.isRecommend = isRecommend;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}
	
}
