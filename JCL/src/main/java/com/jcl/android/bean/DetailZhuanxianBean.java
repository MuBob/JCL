package com.jcl.android.bean;

import com.jcl.android.bean.DetailFindGoodsBean.Data.userInfo;

public class DetailZhuanxianBean extends BaseBean {

	private Data data;
	
	

	public Data getData() {
		return data;
	}



	public void setData(Data data) {
		this.data = data;
	}



	public class Data {

		private String _id;//
		private String company;// 企业名称
		private String linkman;// 联系人
		private String platenum;// 车牌号
		private String phone;// 电话
		private String startarea;// 出发地
		private String endarea;// 目的地
		private String linetype;// 是否中转线路 0不是1是
		private String isstop;// 是否经停站 0：不是1是
		private String stopnames;// 经停站 是中转线路不是经停站会有经停站 例如：青岛,济南
		private String ifcollect;
		
		
		private userInfo publisher;
		private String publishstatus;//发布来源
		private String ishowremark;//是否显示备注
		private String sale;//是否显示备注
		private String xl_description;//是否显示备注
		private String zx_image;//是否显示备注
		private String w_price;//重货
		private String l_price;//轻货
		private String m_price;//最低一票
		private String one_self;////自荐路线 0否
		
		public String getOne_self() {
			return one_self;
		}

		public void setOne_self(String one_self) {
			this.one_self = one_self;
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
		public String getPublishstatus() {
			return publishstatus;
		}

		public void setPublishstatus(String publishstatus) {
			this.publishstatus = publishstatus;
		}

		public String getW_price() {
			return w_price;
		}

		public void setW_price(String w_price) {
			this.w_price = w_price;
		}

		public String getL_price() {
			return l_price;
		}

		public void setL_price(String l_price) {
			this.l_price = l_price;
		}

		public String getM_price() {
			return m_price;
		}

		public void setM_price(String m_price) {
			this.m_price = m_price;
		}

		public String getZx_image() {
			return zx_image;
		}

		public void setZx_image(String zx_image) {
			this.zx_image = zx_image;
		}

		public String getSale() {
			return sale;
		}

		public void setSale(String sale) {
			this.sale = sale;
		}

		public String getXl_description() {
			return xl_description;
		}

		public void setXl_description(String xl_description) {
			this.xl_description = xl_description;
		}

		public String getIshowremark() {
			return ishowremark;
		}

		public void setIshowremark(String ishowremark) {
			this.ishowremark = ishowremark;
		}
		
		public userInfo getPublisher() {
			return publisher;
		}

		public void setPublisher(userInfo publisher) {
			this.publisher = publisher;
		}

		public String getIfcollect() {
			return ifcollect;
		}

		public void setIfcollect(String ifcollect) {
			this.ifcollect = ifcollect;
		}

		public String get_id() {
			return _id;
		}

		public void set_id(String _id) {
			this._id = _id;
		}

		public String getCompany() {
			return company;
		}

		public void setCompany(String company) {
			this.company = company;
		}

		public String getLinkman() {
			return linkman;
		}

		public void setLinkman(String linkman) {
			this.linkman = linkman;
		}

		public String getPlatenum() {
			return platenum;
		}

		public void setPlatenum(String platenum) {
			this.platenum = platenum;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
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

		public String getLinetype() {
			return linetype;
		}

		public void setLinetype(String linetype) {
			this.linetype = linetype;
		}

		public String getIsstop() {
			return isstop;
		}

		public void setIsstop(String isstop) {
			this.isstop = isstop;
		}

		public String getStopnames() {
			return stopnames;
		}

		public void setStopnames(String stopnames) {
			this.stopnames = stopnames;
		}

	}

}
