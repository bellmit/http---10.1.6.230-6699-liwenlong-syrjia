package cn.syrjia.sales.entity;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

import java.io.Serializable;

@Table(name = "t_sales_goal_day")
public class SalesGoalDay implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Column(name = "sales_id")
    private String salesId;
    @Column(name = "day")
    private String day;

    @Column(name = "doctor_online")
    private Integer doctorOnline; // 上线医生数量
    @Column(name = "prescription_sum")
    private Double prescriptionSum; // 药单金额
    @Column(name = "prescription_quantity")
    private Integer prescriptionQuantity; // 药单数量
    @Column(name = "graphic_consultation_sum")
    private Double graphicConsultationSum; // 图文咨询金额
    @Column(name = "graphic_consultation_quantity")
    private Integer graphicConsultationQuantity; // 图文咨询数量
    @Column(name = "phone_consultation_sum")
    private Double phoneConsultationSum; // 电话咨询金额
    @Column(name = "phone_consultation_quantity")
    private Integer phoneConsultationQuantity; // 电话咨询数量
    @Column(name = "graphic_recuperate_sum")
    private Double graphicRecuperateSum; // 图文调理金额
    @Column(name = "graphic_recuperate_quantity")
    private Integer graphicRecuperateQuantity; // 图文调理数量
    @Column(name = "phone_recuperate_sum")
    private Double phoneRecuperateSum; // 电话调理金额
    @Column(name = "phone_recuperate_quantity")
    private Integer phoneRecuperateQuantity; // 电话调理数量
    @Column(name = "commodity_sum")
    private Double commoditySum; // 商品金额
    @Column(name = "commodity_quantity")
    private Integer commodityQuantity; // 商品数量
    @Column(name = "doctor_online_self")
    private Integer doctorOnlineSelf; // 上线医生数量
    @Column(name = "prescription_sum_self")
    private Double prescriptionSumSelf; // 药单金额
    @Column(name = "prescription_quantity_self")
    private Integer prescriptionQuantitySelf; // 药单数量
    @Column(name = "consultation_sum_self")
    private Double consultationSumSelf; // 咨询金额
    @Column(name = "consultation_quantity_self")
    private Integer consultationQuantitySelf; // 咨询数量
    @Column(name = "recuperate_sum_self")
    private Double recuperateSumSelf; // 调理金额
    @Column(name = "recuperate_quantity_self")
    private Integer recuperateQuantitySelf; // 调理数量
    @Column(name = "commodity_sum_self")
    private Double commoditySumSelf; // 商品金额
    @Column(name = "commodity_quantity_self")
    private Integer commodityQuantitySelf; // 商品数量

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSalesId() {
        return salesId;
    }

    public void setSalesId(String salesId) {
        this.salesId = salesId;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Integer getDoctorOnline() {
        return doctorOnline;
    }

    public void setDoctorOnline(Integer doctorOnline) {
        this.doctorOnline = doctorOnline;
    }

    public Double getPrescriptionSum() {
        return prescriptionSum;
    }

    public void setPrescriptionSum(Double prescriptionSum) {
        this.prescriptionSum = prescriptionSum;
    }

    public Integer getPrescriptionQuantity() {
        return prescriptionQuantity;
    }

    public void setPrescriptionQuantity(Integer prescriptionQuantity) {
        this.prescriptionQuantity = prescriptionQuantity;
    }

    public Double getGraphicConsultationSum() {
        return graphicConsultationSum;
    }

    public void setGraphicConsultationSum(Double graphicConsultationSum) {
        this.graphicConsultationSum = graphicConsultationSum;
    }

    public Integer getGraphicConsultationQuantity() {
        return graphicConsultationQuantity;
    }

    public void setGraphicConsultationQuantity(Integer graphicConsultationQuantity) {
        this.graphicConsultationQuantity = graphicConsultationQuantity;
    }

    public Double getPhoneConsultationSum() {
        return phoneConsultationSum;
    }

    public void setPhoneConsultationSum(Double phoneConsultationSum) {
        this.phoneConsultationSum = phoneConsultationSum;
    }

    public Integer getPhoneConsultationQuantity() {
        return phoneConsultationQuantity;
    }

    public void setPhoneConsultationQuantity(Integer phoneConsultationQuantity) {
        this.phoneConsultationQuantity = phoneConsultationQuantity;
    }

    public Double getGraphicRecuperateSum() {
        return graphicRecuperateSum;
    }

    public void setGraphicRecuperateSum(Double graphicRecuperateSum) {
        this.graphicRecuperateSum = graphicRecuperateSum;
    }

    public Integer getGraphicRecuperateQuantity() {
        return graphicRecuperateQuantity;
    }

    public void setGraphicRecuperateQuantity(Integer graphicRecuperateQuantity) {
        this.graphicRecuperateQuantity = graphicRecuperateQuantity;
    }

    public Double getPhoneRecuperateSum() {
        return phoneRecuperateSum;
    }

    public void setPhoneRecuperateSum(Double phoneRecuperateSum) {
        this.phoneRecuperateSum = phoneRecuperateSum;
    }

    public Integer getPhoneRecuperateQuantity() {
        return phoneRecuperateQuantity;
    }

    public void setPhoneRecuperateQuantity(Integer phoneRecuperateQuantity) {
        this.phoneRecuperateQuantity = phoneRecuperateQuantity;
    }

    public Double getCommoditySum() {
        return commoditySum;
    }

    public void setCommoditySum(Double commoditySum) {
        this.commoditySum = commoditySum;
    }

    public Integer getCommodityQuantity() {
        return commodityQuantity;
    }

    public void setCommodityQuantity(Integer commodityQuantity) {
        this.commodityQuantity = commodityQuantity;
    }

    public Integer getDoctorOnlineSelf() {
        return doctorOnlineSelf;
    }

    public void setDoctorOnlineSelf(Integer doctorOnlineSelf) {
        this.doctorOnlineSelf = doctorOnlineSelf;
    }

    public Double getPrescriptionSumSelf() {
        return prescriptionSumSelf;
    }

    public void setPrescriptionSumSelf(Double prescriptionSumSelf) {
        this.prescriptionSumSelf = prescriptionSumSelf;
    }

    public Integer getPrescriptionQuantitySelf() {
        return prescriptionQuantitySelf;
    }

    public void setPrescriptionQuantitySelf(Integer prescriptionQuantitySelf) {
        this.prescriptionQuantitySelf = prescriptionQuantitySelf;
    }

    public Double getConsultationSumSelf() {
        return consultationSumSelf;
    }

    public void setConsultationSumSelf(Double consultationSumSelf) {
        this.consultationSumSelf = consultationSumSelf;
    }

    public Integer getConsultationQuantitySelf() {
        return consultationQuantitySelf;
    }

    public void setConsultationQuantitySelf(Integer consultationQuantitySelf) {
        this.consultationQuantitySelf = consultationQuantitySelf;
    }

    public Double getRecuperateSumSelf() {
        return recuperateSumSelf;
    }

    public void setRecuperateSumSelf(Double recuperateSumSelf) {
        this.recuperateSumSelf = recuperateSumSelf;
    }

    public Integer getRecuperateQuantitySelf() {
        return recuperateQuantitySelf;
    }

    public void setRecuperateQuantitySelf(Integer recuperateQuantitySelf) {
        this.recuperateQuantitySelf = recuperateQuantitySelf;
    }

    public Double getCommoditySumSelf() {
        return commoditySumSelf;
    }

    public void setCommoditySumSelf(Double commoditySumSelf) {
        this.commoditySumSelf = commoditySumSelf;
    }

    public Integer getCommodityQuantitySelf() {
        return commodityQuantitySelf;
    }

    public void setCommodityQuantitySelf(Integer commodityQuantitySelf) {
        this.commodityQuantitySelf = commodityQuantitySelf;
    }

}
