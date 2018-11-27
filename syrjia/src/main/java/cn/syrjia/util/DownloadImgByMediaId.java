package cn.syrjia.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.syrjia.common.BaseDaoInterface;
import cn.syrjia.entity.AccessToken;



public class DownloadImgByMediaId {

	  /**
	   * 根据内容类型判断文件扩展名
	   *
	   * @param contentType 内容类型
	   * @return
	   */
	  public static String getFileexpandedName(String contentType) {
	    String fileEndWitsh = ".amr";
	    if ("image/jpeg".equals(contentType))
	      fileEndWitsh = ".jpg";
	    else if ("audio/mpeg".equals(contentType))
	      fileEndWitsh = ".mp3";
	    else if ("audio/amr".equals(contentType))
	      fileEndWitsh = ".amr";
	    else if ("video/mp4".equals(contentType))
	      fileEndWitsh = ".mp4";
	    else if ("video/mpeg4".equals(contentType))
	      fileEndWitsh = ".mp4";
	    return fileEndWitsh;
	  }
	
	public static <T extends BaseDaoInterface> Map<String,String> downloadMedia(String mediaId,String pathName,HttpServletRequest request,T t,String serverIp,String... type) {  
		String requestUrl = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";
	    //服务器访问路径
		String path = request.getScheme() + "://" + serverIp
				+ ":" + request.getServerPort()+ "/" + "uploadFiles" + "/" +
				pathName+"/";
		//服务器上传路径
		String nowpath=System.getProperty("user.dir");
		String upPath = nowpath.replace("bin", "webapps");
		upPath = upPath +File.separator+"uploadFiles"+File.separator+pathName+File.separator;
		if(!new File(upPath).exists()){
			new File(upPath).mkdirs();
		}
	    //从库中查询token是否过期  否则重新获取
	    AccessToken token = t.queryOne(AccessToken.class, null);
		String acToken = token.getAccess_token();
		
		if(null==type||type.length==0){
			//替换地址中的token和mediaid
			if(!mediaId.contains("[")){
				mediaId="["+mediaId;
			}
			if(!mediaId.contains("]")){
				mediaId=mediaId+"]";
			}
		}
	    requestUrl = requestUrl.replace("ACCESS_TOKEN", acToken).replace(  
	            "MEDIA_ID", mediaId); 
		System.out.println("\nmidiaId:"+mediaId+"\nrequestUrl:"+requestUrl+"\n"); 
	    HttpURLConnection conn = null;  
	    try {  
	    	//发送get请求
	        URL url = new URL(requestUrl);  
	        conn = (HttpURLConnection) url.openConnection();  
	        conn.setDoInput(true);  
	        conn.setRequestMethod("GET");  
	        conn.setConnectTimeout(30000);  
	        conn.setReadTimeout(30000);  
	        //获取文件后缀
		    String fileExt = DownloadImgByMediaId.getFileexpandedName(conn.getHeaderField("Content-Type"));
		    //拼接返回路径字符串
			System.out.println("\fileExt:"+fileExt+"\n"); 
			String imgName = System.currentTimeMillis()+"";
		    String realFileName = imgName+fileExt;
		    System.out.println(imgName+"-------------"+realFileName);
		    //判断该文件夹是否存在 否则创建
		    File file = new File(upPath,realFileName);
		    if(!file.exists()){
			    file.createNewFile();
		    }
		    BufferedInputStream bis = new BufferedInputStream(  
	                conn.getInputStream());  
	        FileOutputStream fs =new FileOutputStream(file);
	        byte[] buff = new byte[1000];    
	        int size = 0;    
	        while ((size = bis.read(buff)) != -1) {    
	            fs.write(buff,0,size);
	        }    
	        fs.close();
	        bis.close();
	        String fwPath = path+realFileName;
	        String riskPath = upPath+realFileName;
	        Map<String,String> map = new HashMap<String, String>();
	        map.put("fwPath",fwPath);
	        map.put("riskPath",riskPath);
	        map.put("imageName", realFileName);
	        return  map;
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    } finally {  
	        if(conn != null){  
	            conn.disconnect();  
	        }  
	    }  
	    return null;  
	}

}	
