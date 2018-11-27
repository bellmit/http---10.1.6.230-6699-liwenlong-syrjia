package cn.syrjia.sales.entity;

import java.math.BigDecimal;

public class StatBean {

    private String day;
    private int doctorOnline;
    private BigDecimal prescriptionSum = new BigDecimal("0.00");
    private int prescriptionQuantity;
    private BigDecimal recuperateSum = new BigDecimal("0.00");
    private int recuperateQuantity;
    private BigDecimal consultationSum = new BigDecimal("0.00");
    private int consultationQuantity;
    private BigDecimal commoditySum = new BigDecimal("0.00");
    private int commodityQuantity;

    private BigDecimal allSum;
    private int allQuantity;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getDoctorOnline() {
        return doctorOnline;
    }

    public void setDoctorOnline(int doctorOnline) {
        this.doctorOnline = doctorOnline;
    }

    public BigDecimal getPrescriptionSum() {
        return prescriptionSum;
    }

    public void setPrescriptionSum(BigDecimal prescriptionSum) {
        this.prescriptionSum = prescriptionSum;
    }

    public int getPrescriptionQuantity() {
        return prescriptionQuantity;
    }

    public void setPrescriptionQuantity(int prescriptionQuantity) {
        this.prescriptionQuantity = prescriptionQuantity;
    }

    public BigDecimal getRecuperateSum() {
        return recuperateSum;
    }

    public void setRecuperateSum(BigDecimal recuperateSum) {
        this.recuperateSum = recuperateSum;
    }

    public int getRecuperateQuantity() {
        return recuperateQuantity;
    }

    public void setRecuperateQuantity(int recuperateQuantity) {
        this.recuperateQuantity = recuperateQuantity;
    }

    public BigDecimal getConsultationSum() {
        return consultationSum;
    }

    public void setConsultationSum(BigDecimal consultationSum) {
        this.consultationSum = consultationSum;
    }

    public int getConsultationQuantity() {
        return consultationQuantity;
    }

    public void setConsultationQuantity(int consultationQuantity) {
        this.consultationQuantity = consultationQuantity;
    }

    public BigDecimal getCommoditySum() {
        return commoditySum;
    }

    public void setCommoditySum(BigDecimal commoditySum) {
        this.commoditySum = commoditySum;
    }

    public int getCommodityQuantity() {
        return commodityQuantity;
    }

    public void setCommodityQuantity(int commodityQuantity) {
        this.commodityQuantity = commodityQuantity;
    }

    public BigDecimal getAllSum() {
        return prescriptionSum.add(recuperateSum).add(consultationSum).add(commoditySum);
    }

    public void setAllSum(BigDecimal allSum) {
        this.allSum = allSum;
    }

    public int getAllQuantity() {
        return prescriptionQuantity + recuperateQuantity + consultationQuantity + commodityQuantity;
    }

    public void setAllQuantity(int allQuantity) {
        this.allQuantity = allQuantity;
    }
}
