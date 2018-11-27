package cn.syrjia.config;

import org.springframework.stereotype.Component;

@Component("config")
public class Config {

	private String imgIp;

	public String uploadService;

	public String uploadServiceFile;

	public String sdkAppid;

	public String accountType;

	public String textCallCenter;

	public String fileService;

	public String getFileService() {
		return fileService;
	}

	public void setFileService(String fileService) {
		this.fileService = fileService;
	}

	public String getImgIp() {
		return imgIp;
	}

	public void setImgIp(String imgIp) {
		this.imgIp = imgIp;
	}

	public String getUploadService() {
		return uploadService;
	}

	public void setUploadService(String uploadService) {
		this.uploadService = uploadService;
	}

	public String getSdkAppid() {
		return sdkAppid;
	}

	public void setSdkAppid(String sdkAppid) {
		this.sdkAppid = sdkAppid;
	}

	public String getUploadServiceFile() {
		return uploadServiceFile;
	}

	public void setUploadServiceFile(String uploadServiceFile) {
		this.uploadServiceFile = uploadServiceFile;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getTextCallCenter() {
		return textCallCenter;
	}

	public void setTextCallCenter(String textCallCenter) {
		this.textCallCenter = textCallCenter;
	}

}
