package cn.syrjia.sales.service.impl;

public enum StateTypeEnum {

    DOCTOR_ON_LINE(0, "doctor_online"),
    PRESCRIPTION_SUM(1, "prescription_sum"),
    PRESCRIPTION_QUANTITY(2, "prescription_quantity"),
    RECUPERATE_SUM(3, "recuperate_sum"),
    RECUPERATE_QUANTITY(4, "recuperate_quantity"),
    CONSULTATION_SUM(5, "consultation_sum"),
    CONSULTATION_QUANTITY(6, "consultation_quantity"),
    COMMODITY_SUM(7, "commodity_sum"),
    COMMODITY_QUANTITY(8, "commodity_quantity");

    private final Integer type;
    private final String value;

    StateTypeEnum(Integer type, String value) {
        this.type = type;
        this.value = value;
    }

    public Integer getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public static StateTypeEnum getByType(Integer type) {
        if (null == type) return null;

        for (StateTypeEnum stateTypeEnum : StateTypeEnum.values()) {
            if (stateTypeEnum.getType().equals(type)) {
                return stateTypeEnum;
            }
        }

        return null;
    }

}
