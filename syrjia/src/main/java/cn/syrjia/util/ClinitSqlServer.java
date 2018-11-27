package cn.syrjia.util;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;



public class ClinitSqlServer {
	final static String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	final static String URL = "jdbc:sqlserver://yzt214935344.gnway.cc:21433;DatabaseName=ecom_jkxx";
	protected static PreparedStatement pstm;
	public static Connection conn;
	
	/**
	 * 创建连接对象
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static Connection getConnection() throws ClassNotFoundException, SQLException{
		Class.forName(DRIVER);
		conn=DriverManager.getConnection(URL,"ecom","Cxc3561712");
		return conn;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Integer addInfo(String tableName,Map<String,Object> map) throws Exception{ 
	    Integer b=0;
	    try{
			/*String sql = "insert into ecom_jkxx(cfid,cfrq,ghid,hzxm,sjh,xb,nl,ysxm,cs,ts,fs,czsj,dqsj,dqzt," +
					"clzt,bz,sf,fyff,Zd,cfyf,ksmc) values()";*/
	    	Object value[]=new Object[map.size()];
			if(map!=null && map.size()>0){
		    	String sql = "insert into "+tableName+" (";
				Set set = map.entrySet();         
				Iterator i = set.iterator();   
				StringBuffer colums=new StringBuffer();
				StringBuffer addColums=new StringBuffer();
				Integer num=1;
				while(i.hasNext()){
				    Map.Entry<String, String> entry1=(Map.Entry<String, String>)i.next();
				    String key = entry1.getKey().toString();
				    Object val=entry1.getValue();
				    if(!StringUtils.isEmpty(key)){
				   	 	colums.append(key);
				   	 	addColums.append("?");
				   	 	value[num-1]=val;
				    	if(num!=map.size()){
				    		colums.append(",");
					   	 	addColums.append(",");
				    	}else{
				    		colums.append(")");
					   	 	addColums.append(")");
				    	}
				    }
				    num++;
				}
				sql+=colums+" values ("+addColums;
				pstm = conn.prepareStatement(sql);
				for(int a=0;a<value.length;a++){
					pstm.setString(a+1, value[a]==null||value[a]==""?"":value[a].toString());
				}
		        b= pstm.executeUpdate();
		    	System.out.println("返回结果："+b+"-----------------------");
			}
	    }catch(Exception e){
	    	e.printStackTrace();
	    	throw e;
	    }finally{
	    	//closeAll();
	    }
	    return b;
	}
	
	public static Integer updateInfo(String key) throws Exception{
		Integer b=0;
		try{
			String sql ="update ecom_jkxx set sf=0 where cfid=?";
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, key);
			b= pstm.executeUpdate();
		}catch(Exception e){
	    	throw e;
		}finally{
			//closeAll();
		}
		return b;
	}
	
	public static Integer getCount(String key) throws Exception{
		Integer b=0;
		try{
			String sql ="select count(1) from ecom_jkxx where cfid=?";
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, key);
			ResultSet rs= pstm.executeQuery();
			if(rs.next()){
				b=rs.getInt(1);
			}
		}catch(Exception e){
	    	throw e;
		}finally{
			//closeAll();
		}
		return b;
	}
	
	public static void closeAll(){
		try{
			if(pstm!=null)	pstm.close();
			if(conn!=null && conn.isClosed()==false)	conn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
