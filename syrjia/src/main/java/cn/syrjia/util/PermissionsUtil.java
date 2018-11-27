package cn.syrjia.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.syrjia.entity.Menu;


/**
 * 生成权限树
 * @author liwenlong
 *
 * 2016-6-22
 */
public class PermissionsUtil {

	private List<Menu> menus;
	
	private List<Map<String,Object>> list=null;
	
	public PermissionsUtil(List<Menu> menus) {
		this.menus=menus;
		this.list=new ArrayList<Map<String,Object>>();
	}
	
	
	public List<Map<String,Object>> buildTree(){
		for(Menu menu:menus){
			if(null==menu.getPid()||menu.getPid()==0){
				Map<String,Object> map=new HashMap<String, Object>();
				Map<String,Object> attrId=new HashMap<String, Object>();
				map.put("title",menu.getName());
				map.put("type","folder");
				attrId.put("id","permissios_"+menu.getId());
				attrId.put("permissionsId",menu.getId());
				attrId.put("pid",menu.getPid());
				map.put("attr",attrId);
				build(menu,menu.getId(),map);
				list.add(map);
			}
		}
		return list;
	}
	
	
	private void build(Menu menu,Integer pid,Map<String,Object> map) {
		List<Menu> children = getChildren(menu);
		List<Map<String,Object>> childList=new ArrayList<Map<String,Object>>();
		if (!children.isEmpty()) {
			for (Menu child : children) {
				Map<String,Object> param=new HashMap<String, Object>();
				Map<String,Object> attrId=new HashMap<String, Object>();
				param.put("title",child.getName());
				if(0==getChildren(child).size()){
					param.put("type","item");
				}else{
					param.put("type","folder");
				}
				attrId.put("id","permissios_"+child.getId());
				attrId.put("permissionsId",child.getId());
				attrId.put("pid",child.getPid());
				param.put("attr",attrId);
				Integer id = child.getId();
				build(child,id,param);
				childList.add(param);
			}
			map.put("products",childList);
		}else{
			map.put("products",childList);
		}
	}
	

	private List<Menu> getChildren(Menu menu) {
		List<Menu> children = new ArrayList<Menu>();
		Integer id = menu.getId();
		for (Menu child : menus) {
			if (id.equals(child.getPid())) {
				children.add(child);
			}
		}
		return children;
	}
}
