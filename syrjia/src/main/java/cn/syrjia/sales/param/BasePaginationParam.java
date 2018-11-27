package cn.syrjia.sales.param;

public abstract class BasePaginationParam extends BaseParam {

    private Integer row;
    private Integer page;
    private Boolean all;

    public Integer getRow() {
        if (row == null) {
            row = 20;
        }
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getPage() {
        if (page==null){
            page = 1;
        }
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Boolean getAll() {
        if (all == null) {
            all = false;
        }
        return all;
    }

    public void setAll(Boolean all) {
        this.all = all;
    }

    public String pageSql() {
        if (!getAll()) {
            return String.format(" limit %d, %d", (getPage()-1)*getRow(), getRow());
        }
        return "";
    }
}
