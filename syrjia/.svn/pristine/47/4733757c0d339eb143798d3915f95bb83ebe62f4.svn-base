package cn.syrjia.util;

import java.util.Comparator;
import java.util.Map;

public class ComparatorImpl implements Comparator {

	private String key;
	private int type;
	public ComparatorImpl(String key,int type){
		this.key=key;
		this.type=type;
	}
	@Override
	public int compare(Object arg0, Object arg1) {
		// TODO Auto-generated method stub
		Map<String, Object> map1 = (Map<String, Object>) arg0;
		Map<String, Object> map2 = (Map<String, Object>) arg1;
		int res = 0;
		switch (type) {
		case 1://int long 类型
			// 首先比较年龄，如果年龄相同，则比较名字
			Object clas = map1.get(key);
			if(clas instanceof Long){
				long finish1 = Long.parseLong(map1.get(key).toString());
				long finish2 = Long.parseLong(map2.get(key).toString());
				res =  finish1>finish2?1:-1;
			}else if(clas instanceof Integer){
				
				int finish1 = Integer.parseInt(map1.get(key).toString());
				int finish2 = Integer.parseInt(map2.get(key).toString());
				res =  finish1-finish2;
			}
			
			break;

		case 2://HH:SS 时间 类型
			long beginTime = DateTime.getTimestamp( map1.get(key).toString());
			long endTime = DateTime.getTimestamp(map2.get(key).toString());
			
			boolean flag = false;
			if (beginTime > endTime) {
				flag = true;
			}
			if (flag) {
				res =  1;
			} else {
				res =  -1;
			}
			break;
		case 3://YYYY-MM 时间 类型
			long beginMonth = DateTime.StrToDate( map1.get(key).toString(),"yyyy-MM").getTime();
			long endMonth = DateTime.StrToDate(map2.get(key).toString(),"yyyy-MM").getTime();
			boolean flag2 = false;
			if (beginMonth > endMonth) {
				flag2 = true;
			}
			if (flag2) {
				res =  1;
			} else {
				res =  -1;
			}
			break;
		case 4://String类型转double
			double k1 = Double.parseDouble(map1.get(key).toString());
			double k2 = Double.parseDouble(map2.get(key).toString());
			res = k1 > k2 ? 1 : -1;
			
			break;
		}
		return res;
		
	}

}
