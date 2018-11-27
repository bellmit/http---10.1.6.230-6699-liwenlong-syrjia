package cn.syrjia.sales.entity;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.sql.Timestamp;

@Table(name = "t_sales_training_personal_goal")
@JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
public class SalesPersonalGoal implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @Id
    private Long id;

    /**
     * 助理id
     */
    @Column
    private String srId;
    
    /**
     * 月
     */
    @Column
    private String month;
    
    /**
     * 类型
     */
    @Column
    private Integer type;
    
    /**
     * 额度类型
     */
    @Column
    private Integer quotaType;
    
    /**
     * 定额
     */
    @Column
    private Double quota;
    
    /**
     * 开始时间
     */
    @Column
    private String beginDate;
    
    /**
     * 结束时间
     */
    @Column
    private String endDate;
    
    /**
     * 创建时间
     */
    @Column
    private Timestamp createTime;
    
    /**
     * 更新时间
     */
    @Column
    private Timestamp updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSrId() {
        return srId;
    }

    public void setSrId(String srId) {
        this.srId = srId;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getQuotaType() {
        return quotaType;
    }

    public void setQuotaType(Integer quotaType) {
        this.quotaType = quotaType;
    }

    public Double getQuota() {
        return quota;
    }

    public void setQuota(Double quota) {
        this.quota = quota;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
}
