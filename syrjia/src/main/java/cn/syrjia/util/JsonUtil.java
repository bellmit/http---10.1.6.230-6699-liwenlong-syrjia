package cn.syrjia.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.XML;
import net.sf.json.JSONObject;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;

public class JsonUtil {

	/** 
	    * 将json格式的字符串解析成Map对象 <li> 
	    * json格式：{"name":"admin","retries":"3fff","testname" 
	    * :"ddd","testretries":"fffffffff"} 
	    */  
	public static Map<String, Object> jsonToMap(Object object)  
	   {  
	       Map<String, Object> data = new HashMap<String, Object>();  
	       // 将json字符串转换成jsonObject  
	       JSONObject jsonObject = JSONObject.fromObject(object);  
	       Iterator<?> it = jsonObject.keys();  
	       // 遍历jsonObject数据，添加到Map对象  
	       while (it.hasNext())  
	       {  
	           String key = String.valueOf(it.next());  
	           Object value =jsonObject.get(key);
	           if(value instanceof JSONArray){
	        	   value = parseJSON2List(value);
	           }
	           data.put(key, value);  
	       }  
	       return data;  
	   }  
	 /**
     * 
    * json转换list.
    * <br>详细说明
    * @param jsonStr json字符串
    * @return
    * @return List<Map<String,Object>> list
    * @throws
     */
    @SuppressWarnings("unchecked")
	public static List<Map<String, Object>> parseJSON2List(Object jsonStr){
        JSONArray jsonArr = JSONArray.fromObject(jsonStr);
        List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Iterator<JSONObject> it = jsonArr.iterator();
        while(it.hasNext()){
            JSONObject json2 = it.next();
            list.add(jsonToMap(json2.toString()));
        }
        return list;
    }
    
    public static void transMap2Bean(Map<String, Object> map, Object obj) {  
    	  
        try {  
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());  
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();  
  
            for (PropertyDescriptor property : propertyDescriptors) {  
                String key = property.getName();  
  
                if (map.containsKey(key)) {  
                    Object value = map.get(key);  
                    // 得到property对应的setter方法  
                    Method setter = property.getWriteMethod(); 
                    setter.invoke(obj,value instanceof Long?value.toString():value);  
                }  
  
            }  
  
        } catch (Exception e) {  
            System.out.println("transMap2Bean Error " + e);  
        }  
  
        return;  
  
    }  
    
    public static org.json.JSONObject xml2jsonString(String xml) throws JSONException, IOException {
        org.json.JSONObject xmlJSONObj = XML.toJSONObject(xml);
        return xmlJSONObj;
    }
    
}
