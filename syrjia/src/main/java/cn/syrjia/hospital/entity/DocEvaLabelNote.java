package cn.syrjia.hospital.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;


/**
 * 
 * @author liwenlong
 * 评价标签关联表
 */
@Table(name = "h_doctor_evaLabel_note")
public class DocEvaLabelNote implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * evaLabelNoteId
	 */
	@Id
	private Integer evaLabelNoteId;
	
	/**
	 * 评价id
	 */
	@Column
	private String evaluateId;
	
	/**
	 * 评价名称
	 */
	@Column
	private String evaLabelName;

	public Integer getEvaLabelNoteId() {
		return evaLabelNoteId;
	}

	public void setEvaLabelNoteId(Integer evaLabelNoteId) {
		this.evaLabelNoteId = evaLabelNoteId;
	}

	public String getEvaluateId() {
		return evaluateId;
	}

	public void setEvaluateId(String evaluateId) {
		this.evaluateId = evaluateId;
	}

	public String getEvaLabelName() {
		return evaLabelName;
	}

	public void setEvaLabelName(String evaLabelName) {
		this.evaLabelName = evaLabelName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
