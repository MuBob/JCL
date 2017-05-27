package com.jcl.android.bean;

import com.jcl.android.bean.DetailFindGoodsBean.Data.userInfo;

public class DetailFindCarBean extends BaseBean {
	
	private Data data;

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}



	public class Data{
		private String _id;
		private String startarea;//	出发地	
		private String startcode;//出发地code
		private String endarea;//	目的地	
		private String endcode;//	目的地	code
		private String emptytimestart;//空城开始时间
		private String emptytimeend;//空车结束时间	
		private String createtime;//创建时间
		private String carimage1;//	车头（照片）	
		private String carimage2;//	45度（照片）
		private String carimage3;//	车尾（照片）
		private String platenum;//	车牌号
		private String tel;//联系电话
		private String approveweight;//	载重	
		private String expectfee;//	期望运费	
		private String bcintroduce;//
		private String linkman;
		private String officeplace;//常驻地
		private String companyname;//公司名称
		private String distance;//长、短途
		private String distancecode;//长、短途code
		private String cytype;//车源类型
		private String jjdegree;//加急
		private String jjdegreecode;//加急code
		private String commony;//平台选择
		private String commonycode;//平台选择code
		private String tjplace;//途径点
		private String ifcollect;
		private String carlength;//车长
		private String carlengthcede;//车长code
		private String cartype;//车型
		private String cartypecode;//车源类型code
		private String longitude;//经度
		private String latitude;//维度
		private String position;//当前位置
		private userInfo publisher;
		private String publishstatus;//发布来源
		private String wx;//外形
		private String wxcode;//外形code
		private String carsize;//车辆尺寸

		public String getPosition() {
			return position;
		}
		public void setPosition(String position) {
			this.position = position;
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
		public String getPublishstatus() {
			return publishstatus;
		}
		public void setPublishstatus(String publishstatus) {
			this.publishstatus = publishstatus;
		}
		
		
		public userInfo getPublisher() {
			return publisher;
		}
		public void setPublisher(userInfo publisher) {
			this.publisher = publisher;
		}
		public String getCarlength() {
			return carlength;
		}
		public void setCarlength(String carlength) {
			this.carlength = carlength;
		}
		public String getStartcode() {
			return startcode;
		}
		public void setStartcode(String startcode) {
			this.startcode = startcode;
		}
		public String getEndcode() {
			return endcode;
		}
		public void setEndcode(String endcode) {
			this.endcode = endcode;
		}
		public String getIfcollect() {
			return ifcollect;
		}
		public void setIfcollect(String ifcollect) {
			this.ifcollect = ifcollect;
		}
		public String getCreatetime() {
			return createtime;
		}
		public void setCreatetime(String createtime) {
			this.createtime = createtime;
		}
		public String getTel() {
			return tel;
		}
		public void setTel(String tel) {
			this.tel = tel;
		}
		public String getBcintroduce() {
			return bcintroduce;
		}
		public void setBcintroduce(String bcintroduce) {
			this.bcintroduce = bcintroduce;
		}
		public String getLinkman() {
			return linkman;
		}
		public void setLinkman(String linkman) {
			this.linkman = linkman;
		}
		public String getOfficeplace() {
			return officeplace;
		}
		public void setOfficeplace(String officeplace) {
			this.officeplace = officeplace;
		}
		public String getCompanyname() {
			return companyname;
		}
		public void setCompanyname(String companyname) {
			this.companyname = companyname;
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
		public String getCytype() {
			return cytype;
		}
		public void setCytype(String cytype) {
			this.cytype = cytype;
		}
		public String getCartypecode() {
			return cartypecode;
		}
		public void setCartypecode(String cartypecode) {
			this.cartypecode = cartypecode;
		}
		public String getJjdegree() {
			return jjdegree;
		}
		public void setJjdegree(String jjdegree) {
			this.jjdegree = jjdegree;
		}
		public String getJjdegreecode() {
			return jjdegreecode;
		}
		public void setJjdegreecode(String jjdegreecode) {
			this.jjdegreecode = jjdegreecode;
		}
		public String getCommony() {
			return commony;
		}
		public void setCommony(String commony) {
			this.commony = commony;
		}
		public String getCommonycode() {
			return commonycode;
		}
		public void setCommonycode(String commonycode) {
			this.commonycode = commonycode;
		}
		public String getTjplace() {
			return tjplace;
		}
		public void setTjplace(String tjplace) {
			this.tjplace = tjplace;
		}
		public String getWx() {
			return wx;
		}
		public void setWx(String wx) {
			this.wx = wx;
		}
		public String getWxcode() {
			return wxcode;
		}
		public void setWxcode(String wxcode) {
			this.wxcode = wxcode;
		}
		public String getCarsize() {
			return carsize;
		}
		public void setCarsize(String carsize) {
			this.carsize = carsize;
		}
	
		public String get_id() {
			return _id;
		}
		public void set_id(String _id) {
			this._id = _id;
		}
		public String getStartarea() {
			return startarea;
		}
		public void setStartarea(String startarea) {
			this.startarea = startarea;
		}
		public String getEndarea() {
			return endarea;
		}
		public void setEndarea(String endarea) {
			this.endarea = endarea;
		}
		public String getEmptytimestart() {
			return emptytimestart;
		}
		public void setEmptytimestart(String emptytimestart) {
			this.emptytimestart = emptytimestart;
		}
		public String getEmptytimeend() {
			return emptytimeend;
		}
		public void setEmptytimeend(String emptytimeend) {
			this.emptytimeend = emptytimeend;
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
		public String getPlatenum() {
			return platenum;
		}
		public void setPlatenum(String platenum) {
			this.platenum = platenum;
		}
		public String getApproveweight() {
			return approveweight;
		}
		public void setApproveweight(String approveweight) {
			this.approveweight = approveweight;
		}
		public String getExpectfee() {
			return expectfee;
		}
		public void setExpectfee(String expectfee) {
			this.expectfee = expectfee;
		}
		public String getCartype() {
			return cartype;
		}
		public void setCartype(String cartype) {
			this.cartype = cartype;
		}

	}
	
	public class userInfo{
		private String id;
		private String isauth;
		private String level;
		private String mobile;
		private String submittype;
		private String type;
		private String uname;
		private String address;
		
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getIsauth() {
			return isauth;
		}
		public void setIsauth(String isauth) {
			this.isauth = isauth;
		}
		public String getLevel() {
			return level;
		}
		public void setLevel(String level) {
			this.level = level;
		}
		public String getMobile() {
			return mobile;
		}
		public void setMobile(String mobile) {
			this.mobile = mobile;
		}
		public String getSubmittype() {
			return submittype;
		}
		public void setSubmittype(String submittype) {
			this.submittype = submittype;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getUname() {
			return uname;
		}
		public void setUname(String uname) {
			this.uname = uname;
		}
		
	}
}
