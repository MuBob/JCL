package com.jcl.android.getui;

import java.io.Serializable;

public class PushBean implements Serializable{
	private String type;//0:播报1：通知
	private String title;
	private String content;
	private String bizid;
	private int classify;//0:货源 1：扯远 ，type为1时 可空
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getBizid() {
		return bizid;
	}
	public void setBizid(String bizid) {
		this.bizid = bizid;
	}
	public int getClassify() {
		return classify;
	}
	public void setClassify(int classify) {
		this.classify = classify;
	}
	
}
