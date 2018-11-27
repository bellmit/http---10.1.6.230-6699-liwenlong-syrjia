package cn.syrjia.util;

import java.util.ArrayList;
import java.util.List;

import cn.syrjia.entity.Menu;

public class MenuUtil {

	private StringBuffer html = new StringBuffer();
	private List<Menu> menus;

	public MenuUtil(List<Menu> menus) {
		this.menus = menus;
	}

	public  String buildTree() {
		html.append("<ul class=\"am-list admin-sidebar-list\">");
		for (Menu menu : menus) {
			Integer id = menu.getId();
			if (menu.getPid()== null||menu.getPid()== 0) {
				html.append("<li class=\"admin-parent\">");
				html.append("<a class=\"am-cf\" data-am-collapse=\"{target: '#collapse-"+id+"'}\"><span class='"+menu.getStyle()+"'></span>"+menu.getName()+"<span class=\"am-icon-angle-right am-fr am-margin-right\"></span></a>");
				build(menu,id);
			}
		}
		html.append("\r\n</ul>");
		return html.toString();
	}

	private void build(Menu menu,Integer pid) {
		List<Menu> children = getChildren(menu);
		if (!children.isEmpty()) {
			html.append("\r\n<ul class=\"am-list am-collapse admin-sidebar-sub\" id=\"collapse-"+pid+"\">");
			for (Menu child : children) {
				Integer id = child.getId();
				if(0==getChildren(child).size()){
					html.append("<li>");
					html.append("<a href='"+child.getUrl()+"' class=\"am-cf am-c\" id='menu_"+id+"'><span class='"+child.getStyle()+"'></span>"+child.getName()+"<span class=\" am-fr am-margin-right\"></span></a>");
					html.append("</li>");
				}else{
					html.append("<li class=\"admin-parent\">");
					html.append("<a class=\"am-cf\" data-am-collapse=\"{target: '#collapse-"+id+"'}\"><span class='"+child.getStyle()+"'></span>"+child.getName()+"<span class=\"am-icon-angle-right am-fr am-margin-right\"></span></a>");
					build(child,id);
				}
			}
			html.append("\r\n</ul>");
		}
		
	}

	private List<Menu> getChildren(Menu menu) {
		List<Menu> children = new ArrayList<Menu>();
		Integer id = menu.getId();
		for (Menu child : menus) {
			if (null!=child&&id.equals(child.getPid())) {
				children.add(child);
			}
		}
		return children;
	}
}
