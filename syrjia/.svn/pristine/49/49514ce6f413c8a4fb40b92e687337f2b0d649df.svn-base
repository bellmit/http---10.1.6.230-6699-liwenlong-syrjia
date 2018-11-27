package cn.syrjia.sales.param;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

@JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
public class ReplyData implements Serializable {
    private static final long serialVersionUID = 1L;

    private String respCode;
    private String respMsg;
    private Object data;
    private Pagination pagination;

    public ReplyData(){
        respCode = "1001";
    }

    public ReplyData(BasePaginationParam param){
        this();
        fromPaginationParam(param);
    }

    public ReplyData(String respCode, String respMsg) {
        respCode = "2002";
        this.respMsg = respMsg;
    }

    public ReplyData(String respMsg) {
        respCode = "2002";
        this.respMsg = respMsg;
    }

    public void fromPaginationParam(BasePaginationParam param) {
        this.pagination = new Pagination();
        this.pagination.setParam(param);
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}
