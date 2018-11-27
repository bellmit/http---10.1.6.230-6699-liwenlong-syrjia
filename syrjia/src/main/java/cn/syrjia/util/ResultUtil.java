package cn.syrjia.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;


/**
 * 
 * @author liwenlong
 * dataTable返回格式
 */
public class ResultUtil {

	
	@SuppressWarnings("unchecked")
	public static <E> Map<String,Object> resultMap(String sEcho,Map<String,Object> map){
		
		Map<String,Object> result=new HashMap<String, Object>();
		
		List<E> list=(List<E>) map.get("data");
		
		Integer total=Integer.parseInt(map.get("total").toString());
		
		Iterator<?> entries = map.entrySet().iterator();  
		
		while (entries.hasNext()) {  
			  
		    Map.Entry<String,Object> entry = (Map.Entry<String,Object>) entries.next();  
		  
		    String key = (String)entry.getKey();  
		    
		    if(!key.equals("data")&&!key.equals("total")){
		    	Object value =entry.getValue();  
		    	result.put(key,value);
		    }
		}  
		
		//实际的行数
		result.put("iTotalRecords",total);
		
		//过滤之后，实际的行数。
		result.put("iTotalDisplayRecords",total);
		
		//来自客户端 sEcho 的没有变化的复制品
		result.put("sEcho",sEcho);
		
		result.put("aaData",list);
		
		return result;
	}
	
	public static Integer getPage(JSONObject json){
		Integer page=Integer.parseInt(json.getJSONArray("iDisplayStart").get(0).toString())+1;
		return page;
	}
	
	public static Integer getRow(JSONObject json){
		Integer row=Integer.parseInt(json.getJSONArray("iDisplayLength").get(0).toString());
		return row;
	}
}
