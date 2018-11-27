package cn.syrjia.hospital.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Join;
import cn.syrjia.common.annotation.Table;

/**
 * 
 * @author guomenglei 
 * 科室数据字典表
 */
@Table(name = "t_konwledge")
public class Knowledge implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键
	 */
	@Id(increment = false)
	private String id;
	
	/**
	 * 医师ID
	 */
	@Column
	private String doctorId;
	
	/**
	 * 医生名称
	 */
	@Join(id="doctorId",otherId="doctorId",table="t_doctor")
	private String docName;
	
	/**
	 * 类别ID
	 */
	@Column
	private String typeId;
	
	/**
	 * 标题
	 */
	@Column
	private String title;
	
	/**
	 * 文章详情
	 */
	@Column
	private String content;
	
	/**
	 * 是否热门文章 0-否 1-是
	 */
	@Column
	private Integer isHot;
	
	/**
	 * 排序
	 */
	@Column
	private Integer sort;
	
	/**
	 * 点赞数
	 */
	@Column
	private Integer pointNum;
	
	/**
	 * 创建时间
	 */
	@Column
	private String createTime;
	
	/**
	 * 状态 1-启用 2-禁用 3-删除 4-待审核 5-审核不通过
	 */
	@Column
	private Integer state;

	/**
	 * 阅读数
	 */
	@Column
	private Integer readNum;
	
	/**
	 * 活动标签
	 */
	@Column
	private String labelNames;
	
	/**
	 * 商品ID集合
	 */
	@Column
	private String goodsIds;
	
	/**
	 * 发布人ID
	 */
	@Column
	private String sendId;
	
	/**
	 * 出版社
	 */
	@Column
	private String publisher;
	
	/**
	 * 语音路径
	 */
	@Column
	private String voiceUrl;
	
	/**
	 * 视频路径
	 */
	@Column
	private String videoUrl;
	
	/**
	 * 标志0正常 1网址
	 */
	@Column
	private Integer signUrl;
	
	/**
	 *图片地址 (图片类型)
	 */
	@Column
	private String imgUrl;
	
	/**
	 *视频类型展示图
	 */
	@Column
	private String videoShowUrl;
	
	/**
	 *	0置顶 1不置顶
	 */
	@Column
	private Integer top;
	
	/**
	 *	发布类型 1文章 2视频 3音频 4图文
	 */
	@Column
	private Integer type;
	
	/**
	 * 是否原创 0否 1是
	 */
	@Column
	private Integer original;
	
	/**
	 * 使用次数
	 */
	@Column
	private Integer sendNum;
	
	/**
	 * 多个医生对应唯一的文章(这些文章的flag相同）
	 */
	@Column
	private String flag;
	
	/**
	 * 收藏数
	 */
	@Column
	private Integer collectNum;
	
	/**
	 * 发布时间
	 */
	@Column
	private Integer sendTime;
	
	/**
	 * 病症对应的科室ID，英文逗号分隔
	 */
	@Column
	private String departmentIds;
	
	/**
	 * 标签属于哪个表，1.文章标签，2证型，3病症
	 */
	private String rels;
	
	/**
	 * 标签ID的拼接
	 */
	private String labelIds;
	
	public String getLabelIds() {
		return labelIds;
	}

	public void setLabelIds(String labelIds) {
		this.labelIds = labelIds;
	}

	public String getRels() {
		return rels;
	}

	public void setRels(String rels) {
		this.rels = rels;
	}

	public String getDepartmentIds() {
		return departmentIds;
	}

	public void setDepartmentIds(String departmentIds) {
		this.departmentIds = departmentIds;
	}

	public Integer getSendTime() {
		return sendTime;
	}

	public void setSendTime(Integer sendTime) {
		this.sendTime = sendTime;
	}

	public Integer getSendNum() {
		return sendNum;
	}

	public void setSendNum(Integer sendNum) {
		this.sendNum = sendNum;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Integer getCollectNum() {
		return collectNum;
	}

	public void setCollectNum(Integer collectNum) {
		this.collectNum = collectNum;
	}

	public Integer getOriginal() {
		return original;
	}

	public void setOriginal(Integer original) {
		this.original = original;
	}

	public String getVoiceUrl() {
		return voiceUrl;
	}

	public String getVideoUrl() {
		return videoUrl;
	}


	public Integer getSignUrl() {
		return signUrl;
	}

	public void setSignUrl(Integer signUrl) {
		this.signUrl = signUrl;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public String getVideoShowUrl() {
		return videoShowUrl;
	}

	public Integer getTop() {
		return top;
	}

	public Integer getType() {
		return type;
	}

	public void setVoiceUrl(String voiceUrl) {
		this.voiceUrl = voiceUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}


	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public void setVideoShowUrl(String videoShowUrl) {
		this.videoShowUrl = videoShowUrl;
	}

	public void setTop(Integer top) {
		this.top = top;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getSendId() {
		return sendId;
	}

	public void setSendId(String sendId) {
		this.sendId = sendId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getId() {
		return id;
	}

	public String getDoctorId() {
		return doctorId;
	}

	public String getTypeId() {
		return typeId;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

	public Integer getIsHot() {
		return isHot;
	}

	public Integer getSort() {
		return sort;
	}

	public Integer getPointNum() {
		return pointNum;
	}

	public String getCreateTime() {
		return createTime;
	}

	public Integer getState() {
		return state;
	}

	public Integer getReadNum() {
		return readNum;
	}

	public String getLabelNames() {
		return labelNames;
	}

	public String getGoodsIds() {
		return goodsIds;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setIsHot(Integer isHot) {
		this.isHot = isHot;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public void setPointNum(Integer pointNum) {
		this.pointNum = pointNum;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public void setReadNum(Integer readNum) {
		this.readNum = readNum;
	}

	public void setLabelNames(String labelNames) {
		this.labelNames = labelNames;
	}

	public void setGoodsIds(String goodsIds) {
		this.goodsIds = goodsIds;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}
}
