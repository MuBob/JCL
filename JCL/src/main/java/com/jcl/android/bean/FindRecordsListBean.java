package com.jcl.android.bean;

import java.util.List;

public class FindRecordsListBean extends BaseBean {
	private Data data;
	
	public List<Records> getData() {
		return data!=null?data.getRecords():null;
	}

	public void setData(List<Records> data) {
		this.data.setRecords(data);;
	}

	public class Data{
		private List<Records> changelist;

		public List<Records> getRecords() {
			return changelist;
		}

		public void setRecords(List<Records> records) {
			this.changelist = records;
		}
		
		
	}

	public class Records {
		private String _id;
		private String userid;
		private String beforeprice;
		private String price;
		private String afterprice;
		private String type;
		private String operatetime;
		 
		
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
		public String getBeforeprice() {
			return beforeprice;
		}
		public void setBeforeprice(String beforeprice) {
			this.beforeprice = beforeprice;
		}
		public String getPrice() {
			return price;
		}
		public void setPrice(String price) {
			this.price = price;
		}
		public String getAfterprice() {
			return afterprice;
		}
		public void setAfterprice(String afterprice) {
			this.afterprice = afterprice;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getOperatetime() {
			return operatetime;
		}
		public void setOperatetime(String operatetime) {
			this.operatetime = operatetime;
		}
		

	}

}
