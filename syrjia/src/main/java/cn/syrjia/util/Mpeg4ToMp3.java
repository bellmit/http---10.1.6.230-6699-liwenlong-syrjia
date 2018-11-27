package cn.syrjia.util;

import cn.syrjia.config.Config;
import cn.syrjia.config.configCode;
import it.sauronsoftware.jave.*;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class Mpeg4ToMp3 {

    @Resource(name="config")
    Config config;

    /**
     * {String} 地址 修改为 JAVE
     */
//    private static final String FFMEGPATH = "/Users/maoxian/Documents/data/ffmpeg";

    /**
     * {String} 文件服务器地址
     */
//    private static final String FILEIP = config.getUploadServiceFile();

    /**
     * {String} 文件服务器上传方法
     */
    private static final String FILEUPLOADPATH = "/FileService/uploadFilesByFileName";

    /**
     * {String} 文件夹名称
     */
    private static final String FOLDERPATH = "mpeg4ToMp3";

    /**
     * {String} 文件访问路径
     */
    private static final String FILEPATH = "/uploadFiles/" + FOLDERPATH + "/";


    /**
     * 执行CMD
     * @param cmdList 执行命令
     */
    private static void executeCmd(List<String> cmdList){
        String line;
        Process process = null;

        try {
            process = Runtime.getRuntime().exec(cmdList.toArray(new String[cmdList.size()]));
            BufferedReader bw = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            StringBuffer returnSb = new StringBuffer();
            while((line = bw.readLine())!=null){
                returnSb.append(line).append("\n");
            }
            System.out.println(returnSb);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                process.getErrorStream().close();
                process.getInputStream().close();
                process.getOutputStream().close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     *
     * @param sourcePath
     * @return
     * @throws IllegalArgumentException
     * @throws InputFormatException
     * @throws EncoderException
     */
    private static String toMP3(String sourcePath) {
        try{
            String targetPath = sourcePath.substring(0, sourcePath.lastIndexOf(".") + 1) + "_new.mp3";
            File source = new File(sourcePath);
            File target = new File(targetPath);
            AudioAttributes audio = new AudioAttributes();
            audio.setCodec("libmp3lame");
            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setFormat("mp3");
            attrs.setAudioAttributes(audio);
            Encoder encoder = new Encoder();
            encoder.encode(source, target, attrs);
            return targetPath;
        }catch (Exception e){
            return null;
        }
    }

    /**
     * MP3转换
     * @param oldPath 旧地址
     * @return 返回新地址
     */
//    private static String changeToMp3(String oldPath){
//        List<String> cmdList = new ArrayList<>();
//
//        String newPath = oldPath.substring(0, oldPath.lastIndexOf(".") + 1) + "_new.mp3";
//        cmdList.add(FFMEGPATH);
//        cmdList.add("-i");
//        cmdList.add(oldPath);
//        cmdList.add("-acodec");
//        cmdList.add("libmp3lame");
//        cmdList.add(newPath);
//        Mpeg4ToMp3.executeCmd(cmdList);
//        return newPath;
//    }

    /**
     * 从网络Url中下载文件
     * @param urlStr 网络URL路径
     * @param fileName 文件名称
     * @param savePath 保存路径
     * @throws IOException
     */
    private static void  downLoadFromUrl(String urlStr,String fileName,String savePath) throws IOException{
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(3*1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //获取自己数组
        byte[] getData = readInputStream(inputStream);

        //文件保存位置
        File saveDir = new File(savePath);
        if(!saveDir.exists()){
            saveDir.mkdir();
        }
        File file = new File(saveDir+File.separator+fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        if(fos!=null){
            fos.close();
        }
        if(inputStream!=null){
            inputStream.close();
        }


        System.out.println("info:"+url+" download success");

    }

    /**
     * 创建文件
     * @param filePath 文件路径
     * @return FileItem
     */
    private static FileItem createFileItem(String filePath) {
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        String textFieldName = "textField";
        int num = filePath.lastIndexOf(".");
        String extFile = filePath.substring(num);
        FileItem item = factory.createItem(textFieldName, "text/plain", true,
                "MyFileName" + extFile);
        File newfile = new File(filePath);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try {
            FileInputStream fis = new FileInputStream(newfile);
            OutputStream os = item.getOutputStream();
            while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return item;
    }

    /**
     * 从输入流中获取字节数组
     * @param inputStream inputStream
     * @return byte[]
     * @throws IOException IOException
     */
    private static  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }


    /**
     * 上传文件到文件服务器
     * @param fName 文件夹名称
     * @param fileName 文件名
     * @param imgFiles 文件
     * @return Map<String, Object>
     */
    private static Map<String, Object> uploadFilesByFileName(String fName,
                                               String fileName,
                                               MultipartFile imgFiles,
                                                             String fileService) {

        try {
            URL url = new URL(fileService + FILEUPLOADPATH+"?fileName="+fileName+"&fName="+fName);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 发送POST请求必须设置如下两行

            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "text/html");
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.connect();
            conn.setConnectTimeout(10000);
            OutputStream out = conn.getOutputStream();

            DataInputStream in = new DataInputStream(imgFiles.getInputStream());

            int bytes = 0;
            byte[] buffer = new byte[1024];
            while ((bytes = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytes);
            }
            in.close();
            out.flush();
            out.close();
            int res = conn.getResponseCode();
            InputStream ins = null;
            // 上传成功返回200
            if (res == 200) {
                ins = conn.getInputStream();
                int ch;
                StringBuilder sb2 = new StringBuilder();
                // 保存数据
                while ((ch = ins.read()) != -1) {
                    sb2.append((char) ch);
                }
                return JsonUtil.jsonToMap(sb2.toString());
            }
        } catch (Exception e) {
            System.out.println("发送文件出现异常！" + e);
            e.printStackTrace();
        }
        return null;
    }


    /**
     * MPEG4格式 转换成MP3
     * @param url 腾讯MP3格式地址
     * @param fileName from_account + time
     * @return 地址信息
     */
    public static Map<String, Object> changeToMp3(String url, String fileName, String fileService) {
//        url = "http://223.167.87.123/asn.com/stddownload_common_file?authkey=3043020101043c303a02010102010102040d3bd20a02037a1afe02047b57a7df02047b57a7df02037a1dba0204568ffa3a0204c370fb3a02045ba9cebf0204c36d1af10400&bid=10001&subbid=1400104216&fileid=305d02010004563054020100041231343431313532313134393735353130393702037a1afd02041a16a3b402045ba1e0660424353937393433323231353233353338373439355f0a120406416dd5b1a8e8e4d2423c61cf0201000201000400&filetype=2106&openid=76def023630b474d92c82478227c7155&ver=0";
//        fileName = "aaa";
        if(StringUtil.isEmpty(url)){
            return Util.resultMap(configCode.code_1029, null);
        }
        try{
            // 1.查看远程文件夹是否存在 如果存在直接返回URL 否则上传转换后返回URL
            String urlPath = fileService + FILEPATH + fileName + ".mp3";
            File file = new File(urlPath);
            if(file.exists()){
                // 如果存在直接返回
                return Util.resultMap(configCode.code_1001, urlPath);
            }

            // 2. 下载到本地
            String n =System.currentTimeMillis()+"";
            String nowpath = System.getProperty("user.dir");
            String upPath1 = nowpath.replace("bin", "webapps");
            String upPath = upPath1 + File.separator + "uploadFiles"
                    + File.separator + FOLDERPATH + File.separator + n
                    + File.separator;

            if (!new File(upPath).exists()) {
                new File(upPath).mkdirs();
            }
            Mpeg4ToMp3.downLoadFromUrl(url, "stddownload.mp3", upPath);

            String filePath = upPath + "stddownload.mp3";

            // 3. 格式转换
            String newPath = Mpeg4ToMp3.toMP3(filePath);

            // 4. 上传文件服务器
            FileItem fileItem = Mpeg4ToMp3.createFileItem(newPath);
            MultipartFile mfile = new CommonsMultipartFile(fileItem);
            Map<String, Object> mapFiles = Mpeg4ToMp3.uploadFilesByFileName(FOLDERPATH,fileName + ".mp3", mfile, fileService);

            // 5. 删除本地文件
            file = new File(upPath);
            if (file.exists()) {
                if (file.isFile()) {// 判断是否是文件
                    file.delete();// 删除文件
                } else if (file.isDirectory()) {// 否则如果它是一个目录
                    File[] files = file.listFiles();// 声明目录下所有的文件 files[];
                    for (int i = 0; i < files.length; i++) {// 遍历目录下所有的文件
                        files[i].delete();
                    }
                    file.delete();// 删除文件夹
                }
            }

            return Util.resultMap(configCode.code_1001, mapFiles.get("fwPath"));
        }catch (Exception e){
            return Util.resultMap(configCode.code_1029, null);
        }
    }

}
