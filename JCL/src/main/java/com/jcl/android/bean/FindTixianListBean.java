package com.jcl.android.bean;

import java.util.List;

public class FindTixianListBean extends BaseBean {
	private Data data;
	
	public List<Applylist> getData() {
		return data!=null?data.getApplylist():null;
	}

	public void setData(List<Applylist> data) {
		this.data.setApplylist(data);;
	}

	public class Data{
		private List<Applylist> applylist;

		public List<Applylist> getApplylist() {
			return applylist;
		}

		public void setApplylist(List<Applylist> applylist) {
			this.applylist = applylist;
		}
		
		
	}

	public class Applylist {
		private String _id;
		private String userid;
		private String price;
		private String status;
		private String applytime;
		public String get_id() {
			return _id;
		}
		public void set_id(String _id) {
			this._id = _id;
		}
		public String getUserid() {
			return userid;
		}
		public void setUserid(String userid) {
			this.userid = userid;
		}
		public String getPrice() {
			return price;
		}
		public void setPrice(String price) {
			this.price = price;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getApplytime() {
			return applytime;
		}
		public void setApplytime(String applytime) {
			this.applytime = applytime;
		}
		 
		
		
		

	}

}
