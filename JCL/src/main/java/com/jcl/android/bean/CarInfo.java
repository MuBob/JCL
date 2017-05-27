package com.jcl.android.bean;

import java.io.Serializable;

public class CarInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8523428191580360413L;
	private String _id;
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getPlatenum() {
		return platenum;
	}
	public void setPlatenum(String platenum) {
		this.platenum = platenum;
	}
	public String getCompanyname() {
		return companyname;
	}
	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}
	public String getCartype() {
		return cartype;
	}
	public void setCartype(String cartype) {
		this.cartype = cartype;
	}
	public String getWx() {
		return wx;
	}
	public void setWx(String wx) {
		this.wx = wx;
	}
	public String getApproveweight() {
		return approveweight;
	}
	public void setApproveweight(String approveweight) {
		this.approveweight = approveweight;
	}
	public String getCarsize() {
		return carsize;
	}
	public void setCarsize(String carsize) {
		this.carsize = carsize;
	}
	public String getCarimage1() {
		return carimage1;
	}
	public void setCarimage1(String carimage1) {
		this.carimage1 = carimage1;
	}
	public String getCarimage2() {
		return carimage2;
	}
	public void setCarimage2(String carimage2) {
		this.carimage2 = carimage2;
	}
	public String getCarimage3() {
		return carimage3;
	}
	public void setCarimage3(String carimage3) {
		this.carimage3 = carimage3;
	}
	public String getCarlength() {
		return carlength;
	}
	public void setCarlength(String carlength) {
		this.carlength = carlength;
	}
	public String getOfficeplace() {
		return officeplace;
	}
	public void setOfficeplace(String officeplace) {
		this.officeplace = officeplace;
	}
	public String getXslicence() {
		return xslicence;
	}
	public void setXslicence(String xslicence) {
		this.xslicence = xslicence;
	}
	public String getYylicence() {
		return yylicence;
	}
	public void setYylicence(String yylicence) {
		this.yylicence = yylicence;
	}
	public String getBd() {
		return bd;
	}
	public void setBd(String bd) {
		this.bd = bd;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getEffectivetime() {
		return effectivetime;
	}
	public void setEffectivetime(String effectivetime) {
		this.effectivetime = effectivetime;
	}
	public String getEffectiveflag() {
		return effectiveflag;
	}
	public void setEffectiveflag(String effectiveflag) {
		this.effectiveflag = effectiveflag;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getIscheck() {
		return ischeck;
	}
	public void setIscheck(String ischeck) {
		this.ischeck = ischeck;
	}
	public String getIsauth() {
		return isauth;
	}
	public void setIsauth(String isauth) {
		this.isauth = isauth;
	}
	private String platenum;//车牌号
	private String companyname;//公司名称
	private String cartype;//车型
	private String cartypecode;//车型码
	public String getCartypecode() {
		return cartypecode;
	}
	public void setCartypecode(String cartypecode) {
		this.cartypecode = cartypecode;
	}
	private String wx;//外形
	private String approveweight;//载重
	private String carsize;//车辆尺寸
	private String carimage1;//车头
	private String carimage2;//45°照
	private String carimage3;//车尾照
	private String carlength;//车长
	private String officeplace;//常驻地
	private String xslicence;//行驶证
	private String yylicence;//运营证
	private String bd;//保单
	private String status;
	private String createtime;
	private String effectivetime;
	private String effectiveflag;
	private String userid;
	private String ischeck;//车辆审核
	private String isauth;//实名认证
	private String distance;//长短途
	private String distancecode;
	private String gone ;
	private String longitude;//
	private String latitude;//
	private String position;//当前位置
	private String load;//装载状态    开始装载为1
	
	
	public String getLoad() {
		return load;
	}
	public void setLoad(String load) {
		this.load = load;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getGone() {
		return gone;
	}
	public void setGone(String gone) {
		this.gone = gone;
	}
	
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getDistancecode() {
		return distancecode;
	}
	public void setDistancecode(String distancecode) {
		this.distancecode = distancecode;
	}
	
	
	
}