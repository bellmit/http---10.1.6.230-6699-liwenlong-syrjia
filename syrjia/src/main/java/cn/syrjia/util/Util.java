package cn.syrjia.util;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import cn.syrjia.config.configCode;
import cn.syrjia.dao.OrderDao;
import cn.syrjia.entity.Order;
import cn.syrjia.entity.vo.GoodsOrderDetail;
import cn.syrjia.service.GoodsOrderService;
import cn.syrjia.service.GoodsService;
import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Header;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
public class Util {

	public static int toInt(int[] i) {
		int total = 0;
		for (int count : i) {
			total += count;
		}
		return total;
	}

	public static String getUUID() {
		UUID uuid = UUID.randomUUID();
		String str = uuid.toString();
		// 閸樼粯甯�-"缁楋箑褰�
		String temp = str.substring(0, 8) + str.substring(9, 13) + str.substring(14, 18) + str.substring(19, 23)
				+ str.substring(24);
		return temp;
	}

	public static BigDecimal multiply(String s, String s1) {
		BigDecimal unit = new BigDecimal(s);
		BigDecimal n = new BigDecimal(s1);
		return unit.multiply(n).setScale(2, BigDecimal.ROUND_HALF_DOWN);
	}

	public static BigDecimal divide(String s, String s1) {
		BigDecimal unit = new BigDecimal(s);
		BigDecimal n = new BigDecimal(s1);
		return unit.divide(n, 2, BigDecimal.ROUND_HALF_DOWN);
	}

	public static BigDecimal subtract(String s, String s1) {
		BigDecimal unit = new BigDecimal(s);
		BigDecimal n = new BigDecimal(s1);
		return unit.subtract(n).setScale(2, BigDecimal.ROUND_HALF_DOWN);
	}

	public static String bigAdd(String s, String s1) {
		BigDecimal unit = null;
		BigDecimal n = null;
		if (s == null || "".equals(s.trim()))
			unit = new BigDecimal(0);
		else
			unit = new BigDecimal(s);
		if (s1 == null || "".equals(s1.trim()))
			n = new BigDecimal(0);
		else
			n = new BigDecimal(s1);
		return unit.add(n).toString();
	}

	public static String getRandom() {
		try {
			String str = "";
			for (int i = 0; i < 3; i++) {
				int intValue = (int) (Math.random() * 26 + 97);
				str = str + (char) intValue;
			}
			return str;
		} catch (Exception e) {
			return "aaa";
		}
	}

	/**
	 * Object閺佹壆绮嶆潪鐞乶teger閺佹壆绮�
	 * 
	 * @param obj
	 * @return
	 */
	public static Integer[] retValue(Object[] obj) {
		if (null != obj && 0 != obj.length) {
			Integer[] in = new Integer[obj.length];
			for (int i = 0; i < obj.length; i++) {
				in[i] = Integer.parseInt((String) obj[i]);
			}
			return in;
		}
		return null;
	}

	/**
	 * 娑撳﹣绱堕崶鍓у
	 * 
	 * @param multipartFile
	 * @param request
	 * @param fName
	 * @return
	 */
	public static Map<String, Object> uploadImage(MultipartFile multipartFile, HttpServletRequest request, String fName,
			String imgServerIp) {
		Map<String, Object> map = new HashMap<String, Object>();
		String nowpath = System.getProperty("user.dir");
		String path = nowpath.replace("bin", "webapps");
		path = path + File.separator + "uploadFiles" + File.separator;
		if (null == multipartFile) {
			map.put("code", "-1");
			return map;
		}
		String url = request.getScheme() + "://" + imgServerIp + ":" + request.getServerPort() + File.separator
				+ "uploadFiles" + File.separator + fName + File.separator;
		String fileName = multipartFile.getOriginalFilename();
		Long name = System.currentTimeMillis();
		String addr = name.toString() + fileName.substring(fileName.lastIndexOf("."));
		String riskPath = path + fName + File.separator + addr;
		File targetFile = new File(path + File.separator + fName, addr);
		BufferedImage bi;
		try {
			bi = ImageIO.read(multipartFile.getInputStream());
			int fileSize = (int) (multipartFile.getSize() / 1024); // 閸ュ墽澧栨径褍鐨�KB
			if ("body".equals(fName)) {
				if (bi.getWidth() < 60 || bi.getHeight() < 60 || fileSize > 1024) {
					map.put("code", "-2");
					return map;
				}
			} else if (bi.getWidth() < 200 || bi.getHeight() < 200 || fileSize > 5120) {
				map.put("code", "-2");
				return map;
			}
		} catch (Exception e1) {
			map.put("code", "-1");
			return map;
		}
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}
		// 娣囨繂鐡�
		try {
			multipartFile.transferTo(targetFile);
		} catch (Exception e) {
			map.put("code", "-1");
			return map;
		}
		map.put("fwPath", url + addr);
		map.put("riskPath", riskPath);
		map.put("onlyName", addr);
		return map;
	}

	/**
	 * 娑撳﹣绱堕崶鍓у
	 * 
	 * @param multipartFile
	 * @param request
	 * @param fName
	 * @return
	 */
	public static Map<String, Object> uploadVideo(MultipartFile multipartFile, HttpServletRequest request, String fName,
			String imgServerIp) {
		Map<String, Object> map = new HashMap<String, Object>();
		String nowpath = System.getProperty("user.dir");
		String path = nowpath.replace("bin", "webapps");
		path = path + File.separator + "uploadFiles" + File.separator;
		if (null == multipartFile) {
			map.put("code", "-1");
			return map;
		}
		String url = request.getScheme() + "://" + imgServerIp + ":" + request.getServerPort() + File.separator
				+ "uploadFiles" + File.separator + fName + File.separator;

		String fileName = multipartFile.getOriginalFilename();
		Long name = System.currentTimeMillis();
		String addr = name.toString() + fileName.substring(fileName.lastIndexOf("."));
		String riskPath = path + fName + File.separator + addr;
		File targetFile = new File(path + File.separator + fName, addr);
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}
		// 娣囨繂鐡�
		try {
			multipartFile.transferTo(targetFile);
		} catch (Exception e) {
			map.put("code", "-1");
			return map;
		}
		map.put("fwPath", url + addr);
		map.put("riskPath", riskPath);
		// map.put("onlyRiskPath", path + fName);
		map.put("onlyName", addr);
		return map;
	}

	/**
	 * 娑撳﹣绱堕崶鍓у
	 * 
	 * @param multipartFile
	 * @param request
	 * @param fName
	 * @return
	 */
	public static Map<String, String> uploadFoodImage(MultipartFile multipartFile, HttpServletRequest request,
			String fName) {
		Map<String, String> result = new HashMap<>();
		Map<String, Object> map = HttpReuqest.httpPost(fName, multipartFile);
		if (null != map) {
			result.put("url", map.get("fwPath").toString());
			result.put("riskPath", map.get("riskPath").toString());
		}
		return result;
	}

	/**
	 * 娑撳﹣绱堕弬鍥︽
	 * 
	 * @param multipartFile
	 * @param request
	 * @param fName
	 * @return
	 */
	@SuppressWarnings("unused")
	public static String uploadFile(MultipartFile multipartFile, HttpServletRequest request, String name) {

		String nowpath = System.getProperty("user.dir");
		String path = nowpath.replace("bin", "webapps");
		path = path + "/zipTemp";
		if (null == multipartFile) {
			return null;
		}
		String fileName = multipartFile.getOriginalFilename();
		String addr = name.toString() + fileName.substring(fileName.lastIndexOf("."));
		File targetFile = new File(path, addr);
		BufferedImage bi;
		try {
			bi = ImageIO.read(multipartFile.getInputStream());
			int fileSize = (int) (multipartFile.getSize() / 1024); // 閸ュ墽澧栨径褍鐨�KB
			/*
			 * if(bi.getWidth()<200||bi.getHeight()<200||fileSize>5120){ return null; }
			 */
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}
		// 娣囨繂鐡�
		try {
			multipartFile.transferTo(targetFile);
		} catch (Exception e) {
			return null;
		}
		return addr;
	}

	/**
	 * 閸ュ墽澧栨潻浣盒�
	 * 
	 * @param request
	 * @param imgPath
	 * @param dirName
	 * @param fileName
	 * @return
	 */
	public static Map<String, String> imageMove(HttpServletRequest request, File file, String dirName,
			String imgServerIp) {
		String name = System.currentTimeMillis() + file.getName().substring(file.getName().lastIndexOf("."));
		String nowpath = System.getProperty("user.dir");
		String path = nowpath.replace("bin", "webapps");
		path = path + "/uploadFiles";
		String url = request.getScheme() + "://" + imgServerIp + ":" + request.getServerPort() + File.separator
				+ "uploadFiles" + File.separator + dirName + File.separator;
		String riskPath = path + File.separator + dirName + File.separator + name;
		File riskFile = new File(riskPath);
		if (!riskFile.getParentFile().exists()) {
			riskFile.getParentFile().mkdirs();
		}
		InputStream read = null;
		BufferedOutputStream write = null;
		try {
			riskFile.createNewFile();
			read = new FileInputStream(file);
			write = new BufferedOutputStream(new FileOutputStream(riskFile));
			int cha = 0;
			while ((cha = read.read()) != -1) {
				write.write(cha);
			}
			write.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				write.close();
				read.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("fwPath", url + name);
		map.put("riskPath", riskPath);
		return map;
	}

	public static void deleteFile(String sPath) {
		if (!StringUtils.isEmpty(sPath)) {
			File file = new File(sPath);
			// 鐠侯垰绶炴稉鐑樻瀮娴犳湹绗栨稉宥勮礋缁屽搫鍨潻娑滎攽閸掔娀娅�
			if (file.isFile() && file.exists()) {
				file.delete();
			}
		}
	}

	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// 閻╊喖缍嶅銈嗘娑撹櫣鈹栭敍灞藉讲娴犮儱鍨归梽锟�
		return dir.delete();
	}

	public static String AudioPlayTime(String addr) {
		int i = 0;
		String times = "0:0";
		try {
			File file1 = new File(addr);
			FileInputStream fis = new FileInputStream(file1);
			int b = fis.available();
			Bitstream bt = new Bitstream(fis);
			Header h = bt.readFrame();
			int time = (int) h.total_ms(b);
			i = time / 1000;
			times = i / 60 + ":" + i % 60;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (BitstreamException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return times;
	}

	public static String getIp(HttpServletRequest request) {
		String ipAddress = null;
		try {
			ipAddress = null;
			// ipAddress = this.getRequest().getRemoteAddr();
			ipAddress = request.getHeader("x-forwarded-for");
			if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
				ipAddress = request.getHeader("Proxy-Client-IP");
			}
			if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
				ipAddress = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
				ipAddress = request.getRemoteAddr();
				if (ipAddress.equals("127.0.0.1") || "0:0:0:0:0:0:0:1".equals(ipAddress)) {
					// 閺嶈宓佺純鎴濆幢閸欐牗婀伴張娲帳缂冾喚娈慖P
					InetAddress inet = null;
					try {
						inet = InetAddress.getLocalHost();
					} catch (UnknownHostException e) {
						e.printStackTrace();
					}
					ipAddress = inet.getHostAddress();
				}
			}
			// 鐎甸�绨柅姘崇箖婢舵矮閲滄禒锝囨倞閻ㄥ嫭鍎忛崘纰夌礉缁楊兛绔存稉鐙狿娑撳搫顓归幋椋庮伂閻喎鐤処P,婢舵矮閲淚P閹稿鍙�,'閸掑棗澹�
			if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
																// = 15
				if (ipAddress.indexOf(",") > 0) {
					ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
				}
			}
		} catch (Exception e) {
			ipAddress = "Exception";
		}
		return ipAddress;
	}

	/*
	 * public static void main(String[] args) { //1 娴兼艾鎲�2 缁変椒姹夐崠鑽ゆ晸 3
	 * 鏉╂劗娣�4-闁匡拷鏁禒锝堛� JSONObject obj = JSONObject .fromString(
	 * "{\"action_name\": \"QR_LIMIT_SCENE\",\"action_info\": {\"scene\": {\"scene_id\": 1}}}"
	 * ); String s = HttpReuqest .sendPost(
	 * "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=jCbvsnbYb3glL6sAOy27op6VVImWidqws6g1g7IOkU2QbYi385hkmx94-wUdnnsoNa7wSQ6FuBQAeaVDMtbSbrDQMn2-qdl0fS0PvF5rLdmUiR88RiUdodH70aui0zKwOCHiAFAUIN"
	 * , obj); System.out.println(s); }
	 */
	/**
	 * 閺嶈宓侀崘鍛啇缁鐎烽崚銈嗘焽閺傚洣娆㈤幍鈺佺潔閸氾拷
	 * 
	 * @param contentType 閸愬懎顔愮猾璇茬�
	 * @return
	 */
	public static String getFileexpandedName(String contentType) {
		String fileEndWitsh = "";
		if ("image/jpeg".equals(contentType) || "image/jpg".equals(contentType))
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

	// 閸掓稑缂撴禍宀�樊閻拷
	public static Map<String, Object> createQrCode(HttpServletRequest request, String localQrName, String time,
			String scene_str, String dir, String token, String imgServerIp) {
		// 閺堝秴濮熼崳銊問闂傤喛鐭惧锟�
		String path = request.getScheme() + "://" + imgServerIp + ":" + request.getServerPort() + "/" + "uploadFiles"
				+ "/qrCode" + "/" + dir + "/" + scene_str + "/";
		// 閺堝秴濮熼崳銊ょ瑐娴肩姾鐭惧锟�
		String nowpath = System.getProperty("user.dir");
		String upPath = nowpath.replace("bin", "webapps");
		upPath = upPath + File.separator + "uploadFiles" + File.separator + "qrCode" + File.separator + dir
				+ File.separator + scene_str + File.separator;
		if (!new File(upPath).exists()) {
			new File(upPath).mkdirs();
		}
		String ticket = "";
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> map1 = new HashMap<String, Object>();
		Map<String, Object> map2 = new HashMap<String, Object>();

		if (!StringUtils.isEmpty(time)) {
			map.put("action_name", "QR_LIMIT_SCENE");
			map2.put("scene_id", scene_str);
			map.put("expire_seconds", time);
		} else {// 濮橀晲绠欐禍宀�樊閻拷
			map.put("action_name", "QR_LIMIT_STR_SCENE");
			map2.put("scene_str", scene_str);
		}
		map1.put("scene", map2);
		map.put("action_info", map1);
		String jsonStr = JSONObject.fromObject(map).toString();
		System.out.println(map + "jsonStr*********************************");
		JSONObject jsonObject = HttpReuqest
				.httpRequest("https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + token, "POST", jsonStr);
		ticket = jsonObject.get("ticket").toString();
		Map<String, Object> returnMap = new HashMap<String, Object>();
		String fwPath = "";
		String localPath = "";
		try {
			if (!StringUtils.isEmpty(ticket)) {
				HttpURLConnection conn = null;
				@SuppressWarnings("deprecation")
				String requestUrl = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + URLEncoder.encode(ticket);
				// 閸欐垿锟絞et鐠囬攱鐪�
				URL url = new URL(requestUrl);
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true);
				conn.setRequestMethod("GET");
				conn.setConnectTimeout(30000);
				conn.setReadTimeout(30000);
				// 閼惧嘲褰囬弬鍥︽閸氬海绱�
				String fileExt = Util.getFileexpandedName(conn.getHeaderField("Content-Type"));
				String imgName = System.currentTimeMillis() + "";
				String realFileName = imgName + fileExt;
				System.out.println(imgName + "-------------" + realFileName);
				// 閸掋倖鏌囩拠銉︽瀮娴犺泛銇欓弰顖氭儊鐎涙ê婀�閸氾箑鍨崚娑樼紦
				File file = new File(upPath, realFileName);
				if (!file.exists()) {
					file.createNewFile();
				}
				BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
				FileOutputStream fs = new FileOutputStream(file);
				byte[] buff = new byte[1000];
				int size = 0;
				while ((size = bis.read(buff)) != -1) {
					fs.write(buff, 0, size);
				}
				fs.close();
				bis.close();
				fwPath = path + realFileName;
				localPath = upPath + realFileName;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		returnMap.put("fwPath", fwPath);
		returnMap.put("localPath", localPath);
		return returnMap;
	}

	@SuppressWarnings("rawtypes")
	public static void mapToXMLTest2(Map map, StringBuffer sb) {
		Set set = map.keySet();
		for (Iterator it = set.iterator(); it.hasNext();) {
			String key = (String) it.next();
			Object value = map.get(key);
			if (null == value)
				value = "";
			if (value.getClass().getName().equals("java.util.ArrayList")) {
				ArrayList list = (ArrayList) map.get(key);
				sb.append("<" + key + ">");
				for (int i = 0; i < list.size(); i++) {
					HashMap hm = (HashMap) list.get(i);
					mapToXMLTest2(hm, sb);
				}
				sb.append("</" + key + ">");

			} else {
				if (value instanceof HashMap) {
					sb.append("<" + key + ">");
					mapToXMLTest2((HashMap) value, sb);
					sb.append("</" + key + ">");
				} else {
					sb.append("<" + key + ">" + value + "</" + key + ">");
				}

			}

		}
	}

	/**
	 * 閺冨爼妫块幋瀹犳祮閺冦儲婀�
	 * 
	 * @param seconds
	 * @return
	 */
	public static String timeStamp2Date(String seconds) {
		String format = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date(Long.valueOf(seconds + "000")));
	}

	/**
	 * 鐟欙絽甯噝ip
	 * 
	 * @param request
	 * @param file
	 * @throws IOException
	 */
	public static String CopyOfMyzipDecompressing(HttpServletRequest request, MultipartFile file, String name) {
		String fileName = uploadFile(file, request, name);
		String nowpath = System.getProperty("user.dir");
		String path = nowpath.replace("bin", "webapps");
		path = path + File.separator + "zipTemp" + File.separator + fileName;// 鏉堟挸鍤惄顔肩秿
		String path1 = nowpath.replace("bin", "webapps") + File.separator + "zipTemp" + File.separator
				+ fileName.substring(0, fileName.lastIndexOf(".")) + File.separator;
		InputStream in = null;
		ZipInputStream zin = null;
		ZipFile zf = null;
		try {
			zf = new ZipFile(path);
			in = new BufferedInputStream(new FileInputStream(path));
			zin = new ZipInputStream(in);
			ZipEntry entry = null;
			while ((entry = zin.getNextEntry()) != null) {
				if (entry.isDirectory()) {
					continue;
				}
				File tmp = new File(path1 + entry.getName());
				if (!tmp.exists()) {
					File rootDirectoryFile = new File(tmp.getParent());
					if (!rootDirectoryFile.exists()) {
						boolean ifSuccess = rootDirectoryFile.mkdirs();
						if (!ifSuccess) {
							return null;
						}
					}
				}
				tmp.createNewFile();
				InputStream read = zf.getInputStream(entry);
				BufferedOutputStream write = new BufferedOutputStream(new FileOutputStream(tmp));
				int cha = 0;
				while ((cha = read.read()) != -1) {
					write.write(cha);
				}
				write.flush();
				write.close();
				read.close();
			}
		} catch (Exception e) {
			return null;
		} finally {
			try {
				in.close();
				zin.close();
				zf.close();
			} catch (IOException e1) {
				return null;
			}
		}
		return path1;
	}

	public static boolean isEquals(Object o1, Object o2) {
		BigDecimal a = new BigDecimal(o1.toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal b = new BigDecimal(o2.toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
		return a.equals(b);
	}

	public static List<Map<String, Object>> buildTree(List<Map<String, Object>> list) {
		List<Map<String, Object>> l = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> m : list) {
			if (null == m.get("pid") || m.get("pid").toString().equals("0")) {
				Map<String, Object> map = new HashMap<String, Object>();
				Map<String, Object> attrId = new HashMap<String, Object>();
				map.put("title", m.get("name").toString());
				map.put("type", "folder");
				attrId.put("id", "permissios_" + m.get("id").toString());
				attrId.put("permissionsId", m.get("id").toString());
				attrId.put("pid", null == m.get("pid") ? "0" : m.get("pid").toString());
				attrId.put("state", m.get("state").toString());
				attrId.put("_type", m.get("type").toString());
				map.put("attr", attrId);
				build(m, m.get("id").toString(), map, list);
				l.add(map);
			}
		}
		return l;
	}

	private static void build(Map<String, Object> m, String pid, Map<String, Object> map,
			List<Map<String, Object>> list) {
		List<Map<String, Object>> children = getChildren(m, list);
		List<Map<String, Object>> childList = new ArrayList<Map<String, Object>>();
		if (!children.isEmpty() && children.size() > 0) {
			for (Map<String, Object> m1 : children) {
				Map<String, Object> param = new HashMap<String, Object>();
				Map<String, Object> attrId = new HashMap<String, Object>();
				param.put("title", m1.get("name").toString());
				if (0 == getChildren(m1, list).size()) {
					param.put("type", "item");
				} else {
					param.put("type", "folder");
				}
				attrId.put("id", "permissios_" + m1.get("id").toString());
				attrId.put("permissionsId", m1.get("id").toString());
				attrId.put("pid", m1.get("pid").toString());
				attrId.put("state", m1.get("state").toString());
				attrId.put("_type", m1.get("type").toString());
				param.put("attr", attrId);
				String id = m1.get("id").toString();
				build(m1, id, param, list);
				childList.add(param);
			}
			map.put("products", childList);
		} else {
			map.put("products", childList);
		}
	}

	private static List<Map<String, Object>> getChildren(Map<String, Object> m, List<Map<String, Object>> list) {
		List<Map<String, Object>> children = new ArrayList<Map<String, Object>>();
		String id = m.get("id").toString();
		for (Map<String, Object> map : list) {
			if (id.equals(null == map.get("pid") ? "0" : map.get("pid").toString())) {
				children.add(map);
			}
		}
		return children;
	}

	public static List<Map<String, Object>> buildTagTree(List<Map<String, Object>> list) {
		List<Map<String, Object>> l = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> m : list) {
			if (null == m.get("pid") || m.get("pid").toString().equals("0")) {
				Map<String, Object> map = new HashMap<String, Object>();
				Map<String, Object> attrId = new HashMap<String, Object>();
				map.put("title", m.get("name").toString());
				map.put("type", "folder");
				attrId.put("id", "permissios_" + m.get("id").toString());
				attrId.put("permissionsId", m.get("id").toString());
				attrId.put("pid", null == m.get("pid") ? "0" : m.get("pid").toString());
				attrId.put("state", m.get("state").toString());
				attrId.put("_type", m.get("type").toString());
				map.put("attr", attrId);
				buildTags(m, m.get("id").toString(), map, list);
				l.add(map);
			}
		}
		return l;
	}

	private static void buildTags(Map<String, Object> m, String pid, Map<String, Object> map,
			List<Map<String, Object>> list) {
		List<Map<String, Object>> children = getChildren(m, list);
		List<Map<String, Object>> childList = new ArrayList<Map<String, Object>>();
		if (!children.isEmpty() && children.size() > 0) {
			for (Map<String, Object> m1 : children) {
				Map<String, Object> param = new HashMap<String, Object>();
				Map<String, Object> attrId = new HashMap<String, Object>();
				param.put("title", m1.get("name").toString());
				if (0 == getTagChildren(m1, list).size()) {
					param.put("type", "item");
				} else {
					param.put("type", "folder");
				}
				attrId.put("id", "permissios_" + m1.get("id").toString());
				attrId.put("permissionsId", m1.get("id").toString());
				attrId.put("pid", m1.get("pid").toString());
				attrId.put("state", m.get("state").toString());
				attrId.put("_type", m1.get("type").toString());
				param.put("attr", attrId);
				String id = m1.get("id").toString();
				buildTags(m1, id, param, list);
				childList.add(param);
			}
			map.put("products", childList);
		} else {
			map.put("products", childList);
		}
	}

	private static List<Map<String, Object>> getTagChildren(Map<String, Object> m, List<Map<String, Object>> list) {
		List<Map<String, Object>> children = new ArrayList<Map<String, Object>>();
		String id = m.get("id").toString();
		for (Map<String, Object> map : list) {
			if (id.equals(null == map.get("pid") ? "0" : map.get("pid").toString())) {
				children.add(map);
			}
		}
		return children;
	}

	/**
	 * object鏉炵惙son
	 * 
	 * @param obj
	 * @return
	 */
	public static String ObjectToString(Object obj) {
		if (null == obj || "".equals(obj)) {
			return null;
		}
		if (obj instanceof List || obj instanceof Set) {
			JSONArray jsonArray = JSONArray.fromObject(obj);
			return jsonArray.toString();
		} else {
			JSONObject jsonObject = JSONObject.fromObject(obj);
			return jsonObject.toString();
		}

	}

	/**
	 * 鑾峰彇浼氬憳鍗″叚浣嶉殢鏈哄崱鍙�
	 * 
	 * @Description: TODO
	 * @param @return
	 * @return String
	 * @throws @author 鐜嬫槶闃�
	 * @date 2017-6-7
	 */
	public static String getCardRandom() {
		int min = 1;
		int max = 1000000;
		int n = 1;
		int len = max - min + 1;
		int[] source = new int[len];
		if (max < min || n > len) {
			return null;
		}
		for (int i = min; i < min + len; i++) {
			source[i - min] = i;
		}
		int[] result = new int[n];
		Random rd = new Random();
		int index = 0;
		for (int i = 0; i < result.length; i++) {
			index = Math.abs(rd.nextInt() % len--);
			result[i] = source[index];
			source[index] = source[len];
		}
		return String.valueOf(result[0]);
	}

	/**
	 * * 涓や釜Double鏁扮浉鍔�*
	 * 
	 * @param v1 *
	 * @param v2 *
	 * @return Double
	 */
	public static Double add(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1.toString());
		BigDecimal b2 = new BigDecimal(v2.toString());
		return new Double(b1.add(b2).doubleValue());
	}

	public static String getAge1(java.util.Date birthday2) {
		Calendar cal = Calendar.getInstance();
		if (cal.before(birthday2)) {
			return null;
		}
		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH);
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
		cal.setTime((java.util.Date) birthday2);
		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH);
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
		int age = yearNow - yearBirth;
		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				if (dayOfMonthNow < dayOfMonthBirth)
					age--;
			} else {
				age--;
			}
		}
		return String.valueOf(age);

	}

	// 对list实现分页
	public synchronized static List<Map<String, Object>> getListByPager(List<Map<String, Object>> data, Pager pager) {
		List<Map<String, Object>> result = new ArrayList<>();
		if (pager.getStart() < data.size()) {
			int end = pager.getPage() * pager.getRow();
			if (end >= data.size()) {
				end = data.size();
			}
			result = data.subList(pager.getStart(), end);
		}
		return result;
	}

	public static int queryNowTime() {
		return (int) (System.currentTimeMillis() / 1000);
	}

	public static Map<String, Object> resultMap(Integer code, Object data) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("respCode", code);
		map.put("respMsg", configCode.codeDesc(code));
		if (data instanceof String || data instanceof Integer) {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("result", data);
			map.put("data", m);
		} else {
			map.put("data", data);
		}
		return map;
	}
	
	public static Map<String, Object> resultMap1(Integer code, String data) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("respCode", code);
		map.put("respMsg", configCode.codeDesc(code) +"："+data);
		map.put("data", null);
		return map;
	}

	public static String ArrayToString(String[] arr) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			if (i != 0) {
				sb.append(",");
			}
			sb.append("\"" + arr[i] + "\"");
		}
		return sb.toString();
	}

	/**
	 * 判断当前时间是否在某时间段内
	 */
	public static boolean compareAlertTime(String startTime, String xbTime) {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");// 设置日期格式
		Date now = null;
		Date beginTime = null;
		Date endTime = null;
		try {
			if (StringUtils.isEmpty(startTime)) {
				startTime = "09:00";
			}
			if (StringUtils.isEmpty(xbTime)) {
				xbTime = "17:00";
			}
			now = df.parse(df.format(new Date()));
			beginTime = df.parse(startTime);
			endTime = df.parse(xbTime);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Boolean flag = belongCalendar(now, beginTime, endTime);
		return flag;
	}

	/**
	 * 判断时间是否在时间段内
	 * 
	 * @param nowTime
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public static boolean belongCalendar(Date nowTime, Date beginTime, Date endTime) {
		Calendar date = Calendar.getInstance();
		date.setTime(nowTime);

		Calendar begin = Calendar.getInstance();
		begin.setTime(beginTime);

		Calendar end = Calendar.getInstance();
		end.setTime(endTime);

		if (date.after(begin) && date.before(end)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean compareRefundTime(Integer payTime, Integer days, boolean isCan) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			long lt = new Long(payTime);
			Date date = new Date(lt * 1000);
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			if (isCan) {
				calendar.add(Calendar.DATE, days);// 把日期往后增加一天.整数往后推,负数往前移动
			} else {
				calendar.add(Calendar.DATE, -days);// 把日期往后增加一天.整数往后推,负数往前移动
			}
			date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
			String date2 = format.format(date);
			String nowDate = format.format(new Date());
			Date nowTime = df.parse(nowDate);
			Date dt2 = df.parse(date2);
			if (isCan) {
				if (nowTime.getTime() >= dt2.getTime()) {
					return true;
				} else {
					return false;
				}
			} else {
				if (nowTime.getTime() <= dt2.getTime()) {
					return true;
				} else {
					return false;
				}
			}

		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getValue(Map<String, Object> map, String key, T t) {
		return (T) (StringUtil.isEmpty(map.get(key)) ? t : map.get(key));
	}

	public static Integer stringToTimestamp(String dateStr) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (!StringUtil.isEmpty(dateStr)) {
			String time = dateStr;
			if (dateStr.length() <= 14) {
				time = dateStr + " 00:00:00";
			}
			Date date = format.parse(time);
			return (int) (date.getTime() / 1000);
		}
		return (int) (System.currentTimeMillis() / 1000);
	}

	/*
	 * public static void main(String[] args) throws Exception { Map<String,Object>
	 * m=new HashMap<String, Object>(); List<Map<String,Object>> list=new
	 * ArrayList<Map<String,Object>>(); Map<String,Object> m1=new HashMap<String,
	 * Object>(); m1.put("file_type",2106); m1.put("file_id",
	 * "305d02010004563054020100041231343431313532313032333434323139303402037a1afd02041316a3b402045a967e170424343030333337383933393834323432383531315f06c57ba2c6210b47098c0d1e46d0e3400201000201000400"
	 * ); m1.put("storage_platform",0); list.add(m1); m.put("Download_Info",list);
	 * System.out.println(JSONObject.fromMap(m).toString()); String
	 * s1=CommonUtil.httpsRequest(
	 * "https://console.tim.qq.com/v4/rich_media/query_file_url"+
	 * "?sdkappid=1400069272","POST",JSONObject.fromMap(m).toString());
	 * System.out.println(s1); Map<String,Object> mm=JsonUtil.jsonToMap(s1);
	 * 
	 * @SuppressWarnings("unchecked") List<Map<String,Object>>
	 * l=(List<Map<String,Object>>)mm.get("url_info"); String
	 * url=l.get(0).get("url").toString(); System.out.println(url);
	 * System.out.println(mm.get("errorcode").equals(0)); }
	 */

	/**
	 * 获取今天后13天日期（设置坐诊信息用）
	 * 
	 * @return
	 */
	public static String getEndDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar2 = Calendar.getInstance();
		calendar2.add(Calendar.DATE, 13);
		String endDate = sdf.format(calendar2.getTime());
		return endDate;
	}

	/**
	 * 每天重复
	 * 
	 * @param beginDate
	 * @return
	 */
	public static List<String> getDateEveryDay(String beginDate) {
		String endDate = getEndDate();
		List<String> dates = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		try {
			Long c = sdf.parse(endDate).getTime() - sdf.parse(beginDate).getTime();
			long d = (c / 1000 / 60 / 60 / 24);// 天
			System.out.println(d + "天");

			date = sdf.parse(beginDate);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			for (int i = 0; i < d; i++) {
				calendar.add(Calendar.DATE, 1);
				String three_days_after = sdf.format(calendar.getTime());
				System.out.println(three_days_after);
				dates.add(three_days_after);
			}
		} catch (ParseException e) {
			System.out.println(e);
		}
		return dates;
	}

	/**
	 * 每周重复
	 * 
	 * @param beginDate
	 * @return
	 */
	public static List<String> getWeekDate(String beginDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String endDate = Util.getEndDate();
		List<String> dates = new ArrayList<String>();
		Date date;
		try {
			date = sdf.parse(beginDate);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.DATE, 7);
			String weekDate = sdf.format(calendar.getTime());
			if (sdf.parse(endDate).getTime() - sdf.parse(weekDate).getTime() > 0) {
				dates.add(weekDate);
				return dates;
			} else {
				return null;
			}
		} catch (ParseException e) {
		}
		return null;
	}

	/**
	 * 获取工作日
	 * 
	 * @param beginDate
	 * @return
	 */
	public static List<String> getWorkDates(String beginDate) {
		String endDate = Util.getEndDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<String> list = new ArrayList<String>();
		Date date;
		try {
			Long c = sdf.parse(endDate).getTime() - sdf.parse(beginDate).getTime();
			long d = (c / 1000 / 60 / 60 / 24);// 天

			date = sdf.parse(beginDate);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			for (int i = 0; i < d; i++) {
				calendar.add(Calendar.DATE, 1);
				String wookDate = sdf.format(calendar.getTime());
				int dayForWeek = 0;
				if (calendar.get(Calendar.DAY_OF_WEEK) == 1) {
					dayForWeek = 7;
				} else {
					dayForWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
				}
				if (dayForWeek != 6 && dayForWeek != 7) {
					list.add(wookDate);
				}
			}
		} catch (ParseException e) {
		}
		return list;
	}

	public static Double getTwoPointPrice(String price) {
		/*
		 * BigDecimal bg4 = new BigDecimal(price); String result = bg4.setScale(4,
		 * BigDecimal.ROUND_DOWN).doubleValue()+""; BigDecimal bg = new
		 * BigDecimal(result); return bg.setScale(2, BigDecimal.ROUND_UP).doubleValue();
		 */
		BigDecimal b = new BigDecimal(price);
		double result1 = b.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
		BigDecimal b1 = new BigDecimal(result1);
		// 保留2位小数
		double result = b1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return result;
	}

	@SuppressWarnings("unused")
	public static void splitOrder(HttpServletRequest request, GoodsOrderService goodsOrderService,
			GoodsService goodsService, OrderDao orderDao, Order order) {
		GoodsOrderDetail goodsOrderDetail = new GoodsOrderDetail();
		goodsOrderDetail.setOrderNo(order.getOrderNo());
		List<GoodsOrderDetail> goodsOrderDetails = goodsOrderService.query(goodsOrderDetail);
		List<Map<String, Object>> suppliers = goodsOrderService.querySupplierByOrderNo(order.getOrderNo());
		if (null == suppliers || suppliers.size() < 2) {
			return;
		}
		for (Map<String, Object> map : suppliers) {
			String newOrderNo = goodsOrderService.orderNo();
			List<GoodsOrderDetail> suppliersGoodsOrderDetails = new ArrayList<GoodsOrderDetail>();
			Double total = 0.0;
			Double originalTotal = 0.0;
			for (GoodsOrderDetail detail : goodsOrderDetails) {
				String orderDetialId = detail.getId();
				Map<String, Object> goods = goodsService.querySupplierByGoods(detail.getGoodsId());
				if (goods.get("supplierId").toString().equals(map.get("supplierId").toString())) {
					detail.setId(goodsOrderService.orderNo());
					detail.setOrderNo(newOrderNo);
					detail.setType(Integer.parseInt(goods.get("identificationId").toString()));
					detail.setPaymentStatus(2);
					total = Util.add(total + "", detail.getGoodsTotal() + "");
					originalTotal = Util.add(originalTotal + "",
							detail.getGoodsOriginalPrice() * detail.getGoodsNum() + "");

					suppliersGoodsOrderDetails.add(detail);
				}
			}

			goodsOrderService.addEntity(suppliersGoodsOrderDetails);

			order.setOrderNo(newOrderNo);
			order.setCreateTime(Util.queryNowTime());
			order.setType(1);
			order.setSourceOrderNo(order.getOrderNo());
			order.setOrderPrice(originalTotal);
			order.setReceiptsPrice(total);
			order.setOrderStatus(1);
			order.setOrderWay(1);
			order.setOrderType(1);
			order.setPaymentStatus(2);
			goodsOrderService.addEntity(order);
		}
	}

	/**
	 * 判断时间是不是今天
	 * 
	 * @param date
	 * @return 是返回true，不是返回false
	 */
	public static String isNowDayStr(Integer time) {
		try {
			Date now = new Date();
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat todaysf = new SimpleDateFormat("HH时mm分");
			SimpleDateFormat nottodaysf = new SimpleDateFormat("MM月dd日 HH时mm分");
			// 获取今天的日期
			String nowDay = sf.format(now);

			// 对比的时间
			String day = sf.format(new Date(Long.valueOf(time + "000")));

			if (day.equals(nowDay)) {
				return "今日" + todaysf.format(new Date(Long.valueOf(time + "000")));
			} else {
				return nottodaysf.format(new Date(Long.valueOf(time + "000")));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String getInfo(String msgType) {
		String desc = "";
		switch (msgType) {
		case "6":
			desc = "症状描述填写";
			break;
		case "7":
			desc = "症状描述填写完成";
			break;
		case "8":
			desc = "问诊单填写";
			break;
		case "9":
			desc = "问诊单填写完成 ";
			break;
		case "10":
			desc = "复诊单填写";
			break;
		case "11":
			desc = "复诊单填写完成";
			break;
		case "12":
			desc = "调理方案";
			break;
		case "13":
			desc = "调理方案已付款";
			break;
		case "14":
			desc = "赠送提问";
			break;
		case "15":
			desc = "补缴挂号费";
			break;
		case "16":
			desc = "挂号费已补缴";
			break;
		case "17":
			desc = "结束问诊";
			break;
		case "18":
			desc = "消息撤回";
			break;
		case "19":
			desc = "发送文章";
			break;
		case "20":
			desc = "商品推荐";
			break;
		case "21":
			desc = "坐诊信息";
			break;
		case "22":
			desc = "帮忙认证";
			break;
		case "23":
			desc = "购买服务";
			break;
		case "24":
			desc = "问诊将于10分钟后结束";
			break;
		case "25":
			desc = "复诊时间已到";
			break;
		case "26":
			desc = "调理方案作废";
			break;
		case "27":
			desc = "用药说明";
			break;
		case "28":
			desc = "接通电话成功";
			break;
		case "29":
			desc = "接通电话失败";
			break;
		default:
			desc = "您有一条新消息";
			break;
		}
		return desc;
	}

	public static Integer addHisServer(String orderNo, OrderDao orderDao) throws Exception {

		try {
			Map<String, Object> serverOrder = orderDao.queryServerOrder(orderNo);

			if (serverOrder != null) {
				String userName = serverOrder.get("patientName").toString();// 患者姓名
				String sex = serverOrder.get("sex").toString();// 性别
				Integer age = StringUtil.isEmpty(serverOrder.get("age"))?0:Integer.parseInt(serverOrder.get("age").toString());// 年龄
				String phone = serverOrder.get("phone").toString();// 患者手机号
				String docName = StringUtil.isEmpty(serverOrder.get("docName"))?"上医":serverOrder.get("docName").toString();// 医生姓名
				String docDepart = serverOrder.get("departName").toString();// 科室

				ClinitSqlServer.getConnection().setAutoCommit(false);// 开启事务
				// 处方日期
				String createTime = StringUtil.isEmpty(serverOrder.get("createTime"))?DateTime.stampToTime(Util.queryNowTime() + ""):DateTime.stampToTime(serverOrder.get("createTime").toString());
				String doTime = DateTime.getTime();// 操作时间

				Map<String, Object> conditionMap = new HashMap<String, Object>();
				conditionMap.put("cfid", orderNo);// 处方编号
				conditionMap.put("cfrq", createTime);// 处方日期
				conditionMap.put("ghid", orderNo);// 挂号编号
				conditionMap.put("hzxm", userName);// 患者姓名
				conditionMap.put("sjh", phone);// 手机号
				conditionMap.put("xb", sex);// 患者性别
				conditionMap.put("nl", age);// 患者年龄
				conditionMap.put("ysxm", docName);// 医生姓名
				conditionMap.put("cs", 1);// 每日次数
				conditionMap.put("ts", 1);// 服用付数
				conditionMap.put("fs", 1);// 服用付数
				conditionMap.put("czsj", doTime);// 操作时间
				// conditionMap.put("fyff", advise);// 服用方法
				// conditionMap.put("Zd", diaName);// 诊断
				// conditionMap.put("cfyf", useMethod);// 处方用法
				conditionMap.put("ksmc", docDepart);// 科室名称
				conditionMap.put("xxly ", 35);// 科室名称
				conditionMap.put("sf", 0);// 科室名称
				try {
					Integer result = ClinitSqlServer.addInfo("ecom_jkxx", conditionMap);
					if (result > 0) {
						Map<String, Object> drguMap= new HashMap<String, Object>();
						drguMap.put("cfid", orderNo);
						drguMap.put("ypid", "ce128c3e8f0c0ae4b3e843dc7cbab0f7");
						drguMap.put("ypmc", "挂号费");
						drguMap.put("ypzl", serverOrder.get("receiptsPrice").toString());
						// drguMap.put("录入人",realName);
						drguMap.put("录入时间", doTime);

						@SuppressWarnings("unused")
						Integer i = ClinitSqlServer.addInfo("ecom_cfmx", drguMap);
					}
				} catch (Exception e) {
					e.printStackTrace();
					ClinitSqlServer.conn.rollback();
					ClinitSqlServer.closeAll();
					return 0;
				}
				ClinitSqlServer.conn.commit();
				ClinitSqlServer.closeAll();
				return 1;
			}
		} catch (Exception e) {
			return 0;
		}
		return 0;
	}
}
