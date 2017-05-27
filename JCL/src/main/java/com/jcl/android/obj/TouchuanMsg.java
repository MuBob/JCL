package com.jcl.android.obj;

public class TouchuanMsg {
	//{type:"1",bizid:"HY11111",title:"标题",content:"内容"}
	private int type;
	private String bizid;
	private String title;
	private String content;
	private int classify;//0:货源 1：扯远 ，type为1时 可空
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getBizid() {
		return bizid;
	}
	public void setBizid(String bizid) {
		this.bizid = bizid;
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
	public int getClassify() {
		return classify;
	}
	public void setClassify(int classify) {
		this.classify = classify;
	}
	
}
