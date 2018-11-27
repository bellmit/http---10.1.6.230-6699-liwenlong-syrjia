package cn.syrjia.sales.param;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;


@JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
public class Pagination  implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer page;
    private Integer row;
    private Boolean all;
    private Integer total;

    public void setParam(BasePaginationParam param) {
        setAll(param.getAll());
        if (!all){
            setPage(param.getPage());
            setRow(param.getRow());
        }
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Boolean getAll() {
        return all;
    }

    public void setAll(Boolean all) {
        this.all = all;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
