package cn.syrjia.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CreateExcelZip {

	// 日志
	@SuppressWarnings("unused")
	private Logger logger = LogManager.getLogger(CreateExcelZip.class);

	public static boolean createExcelZip(String zipPath, File excelFile) {
		boolean isOk = true;
		ZipOutputStream out=null;
		try {
			File zipFile = new File(zipPath);
			if (zipFile.exists()||zipFile .isDirectory()) {
				zipFile.delete();
			}
			zipFile.createNewFile();
			
			out = new ZipOutputStream(new FileOutputStream(
					zipPath));
			File[] fl = excelFile.listFiles();
			FileInputStream fileInputStream = null;
			byte[] buf = new byte[1024];
			int len = 0;
			if (fl != null && fl.length > 0) {
				for (File f : fl) {
					String fileName = f.getName();
					fileInputStream = new FileInputStream(f);
					// 放入压缩zip包中;
					out.putNextEntry(new ZipEntry(fileName));
					// 读取文件;
					while ((len = fileInputStream.read(buf)) > 0) {
						out.write(buf, 0, len);
					}
					// 关闭;
					out.closeEntry();
					if (fileInputStream != null) {
						fileInputStream.close();
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e);
			isOk = false;
		}finally{
			if(out !=null){  
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}  
	        }  
		}
		return isOk;

	}
}
