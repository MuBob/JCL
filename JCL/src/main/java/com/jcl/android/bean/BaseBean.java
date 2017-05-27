package com.jcl.android.bean;
/**
 * bean基类
 * @author msz
 *
 */
public class BaseBean {
	
	private String code;
	private String msg;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	@Override
	public String toString() {
		return "BaseBean [code=" + code + ", msg=" + msg + "]";
	}
	
	
	

}
