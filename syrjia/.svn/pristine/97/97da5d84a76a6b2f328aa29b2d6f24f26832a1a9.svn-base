package cn.syrjia.util;

public class Pager {

	private Integer row;
	private Integer page;
	private Integer start;
	public Integer getRow() {
		
		return row;
	}
	public Integer getPage() {
		return page;
	}
	public void setRow(Integer row) {
		setStart(start);
		this.row = row;
	}
	public void setPage(Integer page) {
		setStart(start);
		this.page = page;
	}
	public Integer getStart() {
		return start;
	}
	public void setStart(Integer start) {
		if(null!=page&&(page==1||page<1)){
			this.start = 0;
		}else if(null!=page&&null!=row){
			this.start = (page-1)*row;
		}
	}
	
	
}
