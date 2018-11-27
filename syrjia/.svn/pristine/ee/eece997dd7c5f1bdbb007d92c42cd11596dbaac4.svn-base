package cn.syrjia.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

@Table(name="t_evaluate_evalabel")
public class EvaluateEvalabel implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id(increment=false,UUID=true)
	private String id;
	
	/**
	 * 评价id
	 */
	private String evaluateId;
	
	/**
	 * 评价名称
	 */
	private String evalableName;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEvaluateId() {
		return evaluateId;
	}
	public void setEvaluateId(String evaluateId) {
		this.evaluateId = evaluateId;
	}
	public String getEvalableName() {
		return evalableName;
	}
	public void setEvalableName(String evalableName) {
		this.evalableName = evalableName;
	}
}
