package cn.syrjia.sales.param;

public class AppGetSalesMonthGoalParam extends BaseParam {

    private static final long serialVersionUID = 1L;

    private String month; // "201801"

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
