package cn.syrjia.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import sun.misc.BASE64Decoder;

public class UploadImages {
	@SuppressWarnings("unused")
	public static List<Map<String, Object>> uploadImages(
			MultipartFile[] multipartFile, HttpServletRequest request,
			String id, String dirName, String serverIp) {
		List<Map<String, Object>> urlList = new ArrayList<Map<String, Object>>();
		// String path =
		// request.getSession().getServletContext().getRealPath("uploadFiles");
		String nowpath = System.getProperty("user.dir");
		String path = nowpath.replace("bin", "webapps");
		path = path + "/uploadFiles";
		if (multipartFile.length == 0) {
			return null;
		}
		for (MultipartFile file : multipartFile) {
			// 保存
			try {
				String url = request.getScheme() + "://" + serverIp + ":"
						+ request.getServerPort() + File.separator
						+ "uploadFiles" + File.separator + dirName;
				Map<String, Object> map = new HashMap<String, Object>();
				String fileName = file.getOriginalFilename();

				Long name = System.currentTimeMillis();
				String addr = name.toString()
						+ fileName.substring(fileName.lastIndexOf("."));
				String furl = path + File.separator + dirName;
				BufferedImage bi = ImageIO.read(file.getInputStream());
				int fileSize = (int) (file.getSize() / 1024); // 图片大小 KB

				if (fileSize > 500000) {
					break;
				}
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				String ymd = sdf.format(date);
				if (!StringUtils.isEmpty(id)) {// 图片
					furl = furl + File.separator + id + File.separator + ymd;
					url = url + File.separator + id + File.separator + ymd;
				}
				File targetFile = new File(furl, addr);
				if (!targetFile.exists()) {
					targetFile.mkdirs();
				}

				file.transferTo(targetFile);
				String pathUrl = furl + File.separator + addr;
				String fwUrl = url + File.separator + addr;
				map.put("localUrl", pathUrl);
				map.put("serverUrl", fwUrl);
				map.put("fileName", addr);
				urlList.add(map);
			} catch (Exception e) {
				return null;
			}
		}
		return urlList;
	}

	@SuppressWarnings("unused")
	public static Map<String, Object> uploadThumbnailImg(
			HttpServletRequest request, HttpServletResponse response,
			String dirName, String serverIp, String fileData,
			Integer fileLength, String id) throws Exception {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		try {
			// 文件保存目录路径
			String savePath = System.getProperty("user.dir");
			savePath = savePath.replace("bin", "webapps");
			savePath = savePath + "/uploadFiles";
			// 文件保存目录URL
			String saveUrl = request.getScheme() + "://" + serverIp + ":"
					+ request.getServerPort() + File.separator + "uploadFiles"
					+ File.separator;
			// 定义允许上传的文件扩展名
			HashMap<String, String> extMap = new HashMap<String, String>();
			extMap.put("image", "gif,jpg,jpeg,png,bmp,png");
			extMap.put("flash", "swf,flv");
			extMap.put("media",
					"swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
			extMap.put("file",
					"doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2");
			if (fileLength != fileData.length()) {
				try {
					return getError("上传数据出错");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// 检查目录
			File uploadDir = new File(savePath);
			if (!uploadDir.isDirectory()) {
				try {
					uploadDir.mkdirs();
				} catch (Exception e) {
					return getError("上传目录不存在。");
				}
			}
			// 检查目录写权限
			if (!uploadDir.canWrite()) {
				try {
					return getError("上传目录没有写权限。");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (StringUtils.isEmpty(dirName)) {
				dirName = "image";
			}
			// 创建文件夹
			savePath += File.separator + dirName + File.separator;
			saveUrl += dirName + File.separator;
			if(!StringUtil.isEmpty(id)){
				savePath += id + "/";
				saveUrl += id + "/";
			}
			File saveDirFile = new File(savePath);
			if (!saveDirFile.exists()) {
				saveDirFile.mkdirs();
			}

			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String ymd = sdf.format(date);
			savePath += ymd + "/";
			saveUrl += ymd + "/";
			File dirFile = new File(savePath);
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}

			// 检查扩展名
			String fileExt = "png";
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			String newFileName = df.format(new Date()) + "." + fileExt;
			File f = new File(savePath, newFileName);
			// 使用BASE64对图片文件数据进行解码操作
			BASE64Decoder decoder = new BASE64Decoder();
			// 通过Base64解密，将图片数据解密成字节数组
			fileData = fileData.substring(fileData.indexOf(",") + 1);
			byte[] bytes = decoder.decodeBuffer(fileData);
			// 构造字节数组输入流
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			// 读取输入流的数据
			BufferedImage bi = ImageIO.read(bais);
			// 将数据信息写进图片文件中
			ImageIO.write(bi, "jpg", f);// 不管输出什么格式图片，此处不需改动
			bais.close();
			// 生成缩略图
			// Thumbnails.of(new File(savePath + newFileName));
			boolean isOk = ImageUti.GenerateImage(fileData, savePath
					+ newFileName);
			returnMap.put("error", 0);
			returnMap.put("localUrl", savePath + newFileName);
			returnMap.put("serverUrl", saveUrl + newFileName);
			returnMap.put("fileName", newFileName);
			return returnMap;
		} catch (IOException e) {
			try {
				return getError("上传文件失败。");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return null;
	}

	private static Map<String, Object> getError(String message)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("error", 1);
		map.put("message", message);
		return map;
	}
}
