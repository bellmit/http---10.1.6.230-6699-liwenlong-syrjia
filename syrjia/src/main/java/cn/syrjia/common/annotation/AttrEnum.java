package cn.syrjia.common.annotation;

public enum AttrEnum {
	
	ATTRNAME("attrname"),CLASSNAME("classname");

	private String name;
	
	private AttrEnum(String name) {
		this.name=name;
	}
	
	public String getName(){
		return name;
	}
}
