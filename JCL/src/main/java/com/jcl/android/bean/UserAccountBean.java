package com.jcl.android.bean;


public class UserAccountBean extends BaseBean {
	
	private Data data;
	
	public Data getData() {
		if(data!=null)
			return data;
		else 
			return null;
	}

	public void setData(Data data) {
		if(data!=null)
			this.data=data;;
	}

	public class Data{
		
		private String _id;//id
		private String userid;//用户id
		private String price;//用户余额
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
		

	}

}
