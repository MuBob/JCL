package com.jcl.android.bean;

import java.util.List;

public class FindDistributionNearBean extends BaseBean {
	private Data data;
	
	public List<Distribution> getData() {
		return data!=null?data.getDistribution():null;
	}

	public void setData(List<Distribution> data) {
		this.data.setDistribution(data);;
	}

	public class Data{
		private List<Distribution> distribution;

		public List<Distribution> getDistribution() {
			return distribution;
		}

		public void setDistribution(List<Distribution> distribution) {
			this.distribution = distribution;
		}
		
		
	}

	public class Distribution {
		private String _id;
		private String zhname;// 公司名称
		private String address;// 公司地址
		private String linkman;// 联系人
		private String phone;// 电话
		private String longitude;// 公司经度
		private String latitude;// 公司纬度
		public String get_id() {
			return _id;
		}
		public void set_id(String _id) {
			this._id = _id;
		}
		public String getZhname() {
			return zhname;
		}
		public void setZhname(String zhname) {
			this.zhname = zhname;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public String getLinkman() {
			return linkman;
		}
		public void setLinkman(String linkman) {
			this.linkman = linkman;
		}
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
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
		
		

	}

}
