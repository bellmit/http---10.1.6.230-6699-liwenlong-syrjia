package cn.syrjia.entity;

import java.util.Date;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

/**
 * 节假日信息表
 * ClassName: SmHoliday 
 * @Description: TODO
 * @author 王昭阳
 * @date 2017-5-23
 */
@Table(name="t_sm_holiday")
public class SmHoliday {
	
	/**
	 * 主键
	 */
	@Id
	private Integer id;//id
	@Column
	private Date playdate;//所属日期
	@Column
	private String holidayname;//假日名称
	@Column
	private String remark;//备注
	@Column
	private Integer createuserid;//操作人
	@Column
	private Date createtime;//修改时间
	@Column
	private Integer deleteflag;//禁用标示位
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getPlaydate() {
		return playdate;
	}
	public void setPlaydate(Date playdate) {
		this.playdate = playdate;
	}
	public String getHolidayname() {
		return holidayname;
	}
	public void setHolidayname(String holidayname) {
		this.holidayname = holidayname;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getCreateuserid() {
		return createuserid;
	}
	public void setCreateuserid(Integer createuserid) {
		this.createuserid = createuserid;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public Integer getDeleteflag() {
		return deleteflag;
	}
	public void setDeleteflag(Integer deleteflag) {
		this.deleteflag = deleteflag;
	}

}
