package cn.syrjia.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class ListTranscoder {
	public static byte[] serialize(Object value) {  
        if (value == null) {  
            throw new NullPointerException("Can't serialize null");  
        }  
        byte[] rv=null;  
        ByteArrayOutputStream bos = null;  
        ObjectOutputStream os = null;  
        try {  
            bos = new ByteArrayOutputStream();  
            os = new ObjectOutputStream(bos);  
            os.writeObject(value);  
            os.close();  
            bos.close();  
            rv = bos.toByteArray();  
        } catch (IOException e) {  
            throw new IllegalArgumentException("Non-serializable object", e);  
        } finally {  
        	try {
        		closeOutputStream(os);
        		closeOutputStream(bos);
			} catch (IOException e) {
			}  
        }  
        return rv;  
    }  

    public static Object deserialize(byte[] in) {  
        Object rv=null;  
        ByteArrayInputStream bis = null;  
        ObjectInputStream is = null;  
        try {  
            if(in != null) {  
                bis=new ByteArrayInputStream(in);  
                is=new ObjectInputStream(bis);  
                rv=is.readObject();  
                is.close();  
                bis.close();  
            }  
        } catch (IOException e) {  
        	e.printStackTrace();
        } catch (ClassNotFoundException e) {  
        	e.printStackTrace();
        } finally {  
        	try {
        		closeInputStream(is);
        		closeInputStream(bis);
			} catch (IOException e) {
			}  
        }  
        return rv;  
    }  
    
    public static void closeInputStream(InputStream is) throws IOException{
    	if(null!=is){
    		is.close();
    	}
    }
    public static void closeOutputStream(OutputStream os) throws IOException{
    	if(null!=os){
    		os.close();
    	}
    }
	
}
