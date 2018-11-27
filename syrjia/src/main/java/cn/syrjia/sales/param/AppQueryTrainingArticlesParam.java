package cn.syrjia.sales.param;

public class AppQueryTrainingArticlesParam extends BasePaginationParam {

    private static final long serialVersionUID = 1L;

    private Long typeID;
    private String title;

    public Long getTypeID() {
        return typeID;
    }

    public void setTypeID(Long typeID) {
        this.typeID = typeID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
