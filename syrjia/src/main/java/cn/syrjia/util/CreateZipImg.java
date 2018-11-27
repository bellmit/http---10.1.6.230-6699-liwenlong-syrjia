package cn.syrjia.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CreateZipImg {

	// 日志
	@SuppressWarnings("unused")
	private Logger logger = LogManager.getLogger(CreateZipImg.class);

	public static boolean writeZip(List<String> impUrl,String zipPathAndName) {
		boolean isOk = false;
		try {
			File zipFile = new File(zipPathAndName);
			if (zipFile.exists()||zipFile .isDirectory()) {
				zipFile.delete();
			}
			zipFile.createNewFile();
			OutputStream os = new BufferedOutputStream(new FileOutputStream(
					zipPathAndName));
			ZipOutputStream zos = new ZipOutputStream(os);
			byte[] buf = new byte[8192];
			int len;
			for (String filename : impUrl) {
				File file = new File(filename);
				if (!file.isFile())
					continue;
				ZipEntry ze = new ZipEntry(file.getName());
				zos.putNextEntry(ze);
				BufferedInputStream bis = new BufferedInputStream(
						new FileInputStream(file));
				while ((len = bis.read(buf)) > 0) {
					zos.write(buf, 0, len);
				}
				zos.closeEntry();
				isOk = true;
			}
			zos.close();
		} catch (Exception e) {
			System.out.println(e);
			isOk = false;
		}
		return isOk;
	}
	
	public static boolean reNameWriteZip(List<Map<String,Object>> impUrl,String zipPathAndName) {
		boolean isOk = false;
		try {
			File zipFile = new File(zipPathAndName);
			if (zipFile.exists()||zipFile .isDirectory()) {
				zipFile.delete();
			}
			zipFile.createNewFile();
			OutputStream os = new BufferedOutputStream(new FileOutputStream(
					zipPathAndName));
			ZipOutputStream zos = new ZipOutputStream(os);
			byte[] buf = new byte[8192];
			int len;
			for (Map<String,Object> map: impUrl) {
				File file = new File(map.get("localUrl").toString());
				if (!file.isFile())
					continue;
				String name = map.get("qrName").toString()
						+ file.getName().substring(file.getName().lastIndexOf("."));
				ZipEntry ze = new ZipEntry(name);
				zos.putNextEntry(ze);
				BufferedInputStream bis = new BufferedInputStream(
						new FileInputStream(file));
				while ((len = bis.read(buf)) > 0) {
					zos.write(buf, 0, len);
				}
				zos.closeEntry();
				isOk = true;
			}
			zos.close();
		} catch (Exception e) {
			System.out.println(e);
			isOk = false;
		}
		return isOk;
	}

}
