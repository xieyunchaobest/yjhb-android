package cn.com.xyc.vo;

import java.util.Date;

public class MosVersionSVO {
	private long id;
	
	private String versionNum;
	
	private String publishPath;
	
	private String versionDesc;
	
	private String pfType;
	
	private String remarks;
	
	
	private Date publishDate;


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	 
 


	public String getVersionNum() {
		return versionNum;
	}


	public void setVersionNum(String versionNum) {
		this.versionNum = versionNum;
	}


	public String getPublishPath() {
		return publishPath;
	}


	public void setPublishPath(String publishPath) {
		this.publishPath = publishPath;
	}


	public String getVersionDesc() {
		return versionDesc;
	}


	public void setVersionDesc(String versionDesc) {
		this.versionDesc = versionDesc;
	}


	public String getPfType() {
		return pfType;
	}


	public void setPfType(String pfType) {
		this.pfType = pfType;
	}


	public String getRemarks() {
		return remarks;
	}


	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}


	public Date getPublishDate() {
		return publishDate;
	}


	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}
	 
	
	
}
