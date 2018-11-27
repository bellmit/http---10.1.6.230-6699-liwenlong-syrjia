package cn.syrjia.util;

import java.io.File;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.commons.io.FileUtils;

public class DoExcel {

	/**
	 * 下载模板
	 * @param request
	 * @param response
	 */
	public static void DownloadExcel(HttpServletRequest request,HttpServletResponse response,String fileName){
		OutputStream os =null;
		try {
			os = response.getOutputStream();
			response.reset();
			response.setHeader("Content-Disposition","attachment; filename="+fileName+"");
			response.setContentType("application/octet-stream; charset=utf-8");  
	        os.write(FileUtils.readFileToByteArray(getDictionaryFile(request,fileName)));  
	        os.flush();  
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 文件下载路径
	 * @param req
	 * @return
	 */
	private static File getDictionaryFile(HttpServletRequest req,String fileName) {
		File file =new File(req.getSession().getServletContext().getRealPath("/download/"+fileName+""));
		return file;
	}
	
	public static void DownloadZip(HttpServletRequest request,HttpServletResponse response,String upPath,String fileName){
		OutputStream os =null;
		try {
			os = response.getOutputStream();
			response.reset();
			response.setHeader("Content-Disposition","attachment; filename="+fileName+"");
			response.setContentType("application/octet-stream; charset=utf-8");  
	        os.write(FileUtils.readFileToByteArray(new File(upPath)));  
	        os.flush();  
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
