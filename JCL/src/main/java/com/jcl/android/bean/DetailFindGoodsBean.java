package com.jcl.android.bean;

public class DetailFindGoodsBean extends BaseBean {
	
	private Data data;

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}



	public class Data{
		private String _id;
		private String goodsid;
		private String startarea;//	出发地	
		private String endarea;//	目的地	
		private String jjdegree;//	是否加急	0否1是
		private String detailname;//	品名	
		private String goodsweight;//	货物总重量	
		private String cartype;//	车型	
		private String carlength;//	车长	
		private String needcarnum;//	需要车数量	
		private String exfhstarttime;//	装货时间	
		private String exfhendtime;//	装货时间	
		private String lastbjtime;//	最晚报价时间	
		private String mytype;//	贸易类型	
		private String fftype;//	付款方式	
		private String needinvoice;//	是否需要发票	
		private String sourceimage;//	货物照片	
		private String ifopen;//	是否公开电话	0是，1不是
		private String ifcollect;
		private String carname;
		private String goodstj;//体积
		private String lastarrivetime;//最晚送达
		private String haspriceheight;//
		private String haspricelow;//
		private String remark;
		private String signorder;//回单
		private String fhlinkmantel;
		private String endlatitude;//
		private String endlongitude;//
		private String latitude;
		private String longitude;//
		private String submittype;//0个人  1企业
		private String companyname;
		private String companyaddress;
		private String fhlinkman;//发货人
		private String carlengthabove;
		private String createtime;
		private userInfo publisher;
		private String publishstatus;//发布来源
		private String ishowremark;//是否显示备注
		private String gxgl;
		private String ponum;
		private String num;
		private String ptchoose;
		private String ispc;
		private String saying;
		private String unit;
		
		public String getUnit() {
			return unit;
		}
		public void setUnit(String unit) {
			this.unit = unit;
		}
		public String getSaying() {
			return saying;
		}
		public void setSaying(String saying) {
			this.saying = saying;
		}
		public String getIspc() {
			return ispc;
		}
		public void setIspc(String ispc) {
			this.ispc = ispc;
		}
		public String getPtchoose() {
			return ptchoose;
		}
		public void setPtchoose(String ptchoose) {
			this.ptchoose = ptchoose;
		}
		public String getNum() {
			return num;
		}
		public void setNum(String num) {
			this.num = num;
		}
		public String getPonum() {
			return ponum;
		}
		public void setPonum(String ponum) {
			this.ponum = ponum;
		}
		public String getGxgl() {
			return gxgl;
		}
		public void setGxgl(String gxgl) {
			this.gxgl = gxgl;
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
		
		public String getExfhendtime() {
			return exfhendtime;
		}
		public void setExfhendtime(String exfhendtime) {
			this.exfhendtime = exfhendtime;
		}
		public String getGoodsid() {
			return goodsid;
		}
		public void setGoodsid(String goodsid) {
			this.goodsid = goodsid;
		}
		public String get_id() {
			return _id;
		}
		public void set_id(String _id) {
			this._id = _id;
		}
		public String getIshowremark() {
			return ishowremark;
		}
		public void setIshowremark(String ishowremark) {
			this.ishowremark = ishowremark;
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
		public String getCreatetime() {
			return createtime;
		}
		public void setCreatetime(String createtime) {
			this.createtime = createtime;
		}
		public String getCarlengthabove() {
			return carlengthabove;
		}
		public void setCarlengthabove(String carlengthabove) {
			this.carlengthabove = carlengthabove;
		}
		public String getFhlinkman() {
			return fhlinkman;
		}
		public void setFhlinkman(String fhlinkman) {
			this.fhlinkman = fhlinkman;
		}
		public String getSubmittype() {
			return submittype;
		}
		public void setSubmittype(String submittype) {
			this.submittype = submittype;
		}
		public String getCompanyname() {
			return companyname;
		}
		public void setCompanyname(String companyname) {
			this.companyname = companyname;
		}
		public String getCompanyaddress() {
			return companyaddress;
		}
		public void setCompanyaddress(String companyaddress) {
			this.companyaddress = companyaddress;
		}
		public String getFhlinkmantel() {
			return fhlinkmantel;
		}
		public void setFhlinkmantel(String fhlinkmantel) {
			this.fhlinkmantel = fhlinkmantel;
		}
		public String getEndlatitude() {
			return endlatitude;
		}
		public void setEndlatitude(String endlatitude) {
			this.endlatitude = endlatitude;
		}
		public String getEndlongitude() {
			return endlongitude;
		}
		public void setEndlongitude(String endlongitude) {
			this.endlongitude = endlongitude;
		}
		public String getLatitude() {
			return latitude;
		}
		public void setLatitude(String latitude) {
			this.latitude = latitude;
		}
		public String getLongitude() {
			return longitude;
		}
		public void setLongitude(String longitude) {
			this.longitude = longitude;
		}
		public String getSignorder() {
			return signorder;
		}
		public void setSignorder(String signorder) {
			this.signorder = signorder;
		}
		public String getRemark() {
			return remark;
		}
		public void setRemark(String remark) {
			this.remark = remark;
		}
		public String getHaspriceheight() {
			return haspriceheight;
		}
		public void setHaspriceheight(String haspriceheight) {
			this.haspriceheight = haspriceheight;
		}
		public String getHaspricelow() {
			return haspricelow;
		}
		public void setHaspricelow(String haspricelow) {
			this.haspricelow = haspricelow;
		}
		public String getLastarrivetime() {
			return lastarrivetime;
		}
		public void setLastarrivetime(String lastarrivetime) {
			this.lastarrivetime = lastarrivetime;
		}
		public String getGoodstj() {
			return goodstj;
		}
		public void setGoodstj(String goodstj) {
			this.goodstj = goodstj;
		}
		public String getCarname() {
			return carname;
		}
		public void setCarname(String carname) {
			this.carname = carname;
		}
		public String getIfcollect() {
			return ifcollect;
		}
		public void setIfcollect(String ifcollect) {
			this.ifcollect = ifcollect;
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
		public String getJjdegree() {
			return jjdegree;
		}
		public void setJjdegree(String jjdegree) {
			this.jjdegree = jjdegree;
		}
		public String getDetailname() {
			return detailname;
		}
		public void setDetailname(String detailname) {
			this.detailname = detailname;
		}
		public String getGoodsweight() {
			return goodsweight;
		}
		public void setGoodsweight(String goodsweight) {
			this.goodsweight = goodsweight;
		}
		public String getCartype() {
			return cartype;
		}
		public void setCartype(String cartype) {
			this.cartype = cartype;
		}
		public String getCarlength() {
			return carlength;
		}
		public void setCarlength(String carlength) {
			this.carlength = carlength;
		}
		public String getNeedcarnum() {
			return needcarnum;
		}
		public void setNeedcarnum(String needcarnum) {
			this.needcarnum = needcarnum;
		}
		public String getExfhstarttime() {
			return exfhstarttime;
		}
		public void setExfhstarttime(String exfhstarttime) {
			this.exfhstarttime = exfhstarttime;
		}
		public String getLastbjtime() {
			return lastbjtime;
		}
		public void setLastbjtime(String lastbjtime) {
			this.lastbjtime = lastbjtime;
		}
		public String getMytype() {
			return mytype;
		}
		public void setMytype(String mytype) {
			this.mytype = mytype;
		}
		public String getFftype() {
			return fftype;
		}
		public void setFftype(String fftype) {
			this.fftype = fftype;
		}
		public String getNeedinvoice() {
			return needinvoice;
		}
		public void setNeedinvoice(String needinvoice) {
			this.needinvoice = needinvoice;
		}
		public String getSourceimage() {
			return sourceimage;
		}
		public void setSourceimage(String sourceimage) {
			this.sourceimage = sourceimage;
		}
		public String getIfopen() {
			return ifopen;
		}
		public void setIfopen(String ifopen) {
			this.ifopen = ifopen;
		}
		
		

	}
}
