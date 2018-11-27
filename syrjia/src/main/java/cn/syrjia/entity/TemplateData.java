package cn.syrjia.entity;

import java.io.Serializable;

public class TemplateData implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 价值
	 */
	private String value; 
	
	/**
	 * 颜色
	 */
    private String color;
    
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }

}
