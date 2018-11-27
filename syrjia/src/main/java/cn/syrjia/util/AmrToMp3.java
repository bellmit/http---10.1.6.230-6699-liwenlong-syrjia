package cn.syrjia.util;

import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import it.sauronsoftware.jave.InputFormatException;

import java.io.File;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;


public class AmrToMp3 {

	/** 
	 * 把amr格式的语音转换成MP3 
	 * @Title: changeToMp3  
	 * @Description: TODO(把amr格式的语音转换成MP3)  
	 * @author  pll 
	 * @param @param sourcePath amr格式文件路径 
	 * @param @param targetPath 存放mp3格式文件路径  
	 * @return void 返回类型  
	 * @throws 
	 */  
	public static void changeToMp3(String sourcePath, String targetPath) {    
	    File source = new File(sourcePath);    
	    File target = new File(targetPath);    
	    AudioAttributes audio = new AudioAttributes();    
	    Encoder encoder = new Encoder();    
	  
	    audio.setCodec("libmp3lame");    
	    EncodingAttributes attrs = new EncodingAttributes();    
	    attrs.setFormat("mp3");    
	    attrs.setAudioAttributes(audio);   
	    try {    
	        encoder.encode(source, target, attrs);
	    } catch (IllegalArgumentException e) {    
	        e.printStackTrace();    
	    } catch (InputFormatException e) {    
	        e.printStackTrace();    
	    } catch (EncoderException e) {    
	        e.printStackTrace();    
	    }    
	} 
	public static String getMp3Time(String filePath){
		File file = new File(filePath);
        try {
            MP3File f = (MP3File)AudioFileIO.read(file);
            MP3AudioHeader audioHeader = (MP3AudioHeader)f.getAudioHeader();
            int time=audioHeader.getTrackLength();  
            String minute=(time%3600/60)+"";  
            String second=(time%60)+"";
            if(minute.length()==1){
            	minute="0"+minute;
            }
            if(second.length()==1){
            	second="0"+second;
            }
            String result=minute+":"+second;
            return result;
        } catch(Exception e) {
        	return "00:00";
        }
	}
	public static void main(String[] args) {
		System.out.println(getMp3Time("D:"+File.separator+"file"+File.separator+"1478254562974.mp3"));
	}
}
