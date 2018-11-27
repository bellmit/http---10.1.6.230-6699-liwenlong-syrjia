package cn.syrjia.hospital.entity;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Join;
import cn.syrjia.common.annotation.Table;


@Table(name="t_diagnostic")
public class Diagnostic {

	/**
	 * id
	 */
	@Id(UUID=true,increment=false)
	private String Id;
	
	/**
	 * 名称
	 */
	@Column
	private String name;

	/**
	 * 类型
	 */
	@Column
	private Integer type;

	/**
	 * 编码
	 */
	@Column
	private String code;

	/**
	 * 类型名称
	 */
	@Join(table="h_diagnostic_type",id="type",otherId="id",otherName="name")
	private String typeName;

	/**
	 * 使用次数
	 */
	@Column
	private Integer  useCount;

	/**
	 * 拼音
	 */
	@Column
	private String pinyinCode;

	/**
	 * 标识
	 */
	@Column
	private Integer flag;
	
	/**
	 * 1-启用 2-停用3-删除
	 */
	@Column
	private Integer state;

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getUseCount() {
		return useCount;
	}

	public void setUseCount(Integer useCount) {
		this.useCount = useCount;
	}

	public String getPinyinCode() {
		return pinyinCode;
	}

	public void setPinyinCode(String pinyinCode) {
		this.pinyinCode = pinyinCode;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
}
