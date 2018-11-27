package cn.syrjia.hospital.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;


/**
 * 医馆图片展示
 * @author liwenlong
 *
 * 2016-10-28
 */
@Table(name="h_showImage")
public class ShowImage implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id
	private Integer id;
	
	/**
	 * 图片名称
	 */
	@Column
	private String imgName;
	
	/**
	 * 图片访问地址
	 */
	@Column
	private String imgAddr;
	
	/**
	 * 物理地址
	 */
	@Column
	private String physicsAddr;
	
	/**
	 * 状态 0正常 1禁用 2删除
	 */
	@Column
	private Integer state;
	
	/**
	 * 创建时间
	 */
	@Column
	private Integer createTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	public String getImgAddr() {
		return imgAddr;
	}

	public void setImgAddr(String imgAddr) {
		this.imgAddr = imgAddr;
	}

	public String getPhysicsAddr() {
		return physicsAddr;
	}

	public void setPhysicsAddr(String physicsAddr) {
		this.physicsAddr = physicsAddr;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}

}
