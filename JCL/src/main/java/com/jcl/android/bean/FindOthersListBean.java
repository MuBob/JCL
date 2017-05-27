package com.jcl.android.bean;

import java.util.List;

public class FindOthersListBean extends BaseBean {
	private Data data;
	
	public List<Others> getData() {
		return data!=null?data.getOthers():null;
	}

	public void setData(List<Others> data) {
		this.data.setOthers(data);;
	}

	public class Data{
		private List<Others> others;

		public List<Others> getOthers() {
			return others;
		}

		public void setOthers(List<Others> others) {
			this.others = others;
		}
		
		
	}

	public class Others {
		private String _id;
		private String description;// 标题
		private String fbtype;// 发布类别
		private String ywtype;// 业务类别
		private String phone;// 联系电话
		
		

		public String get_id() {
			return _id;
		}
		public void set_id(String _id) {
			this._id = _id;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getFbtype() {
			return fbtype;
		}
		public void setFbtype(String fbtype) {
			this.fbtype = fbtype;
		}
		public String getYwtype() {
			return ywtype;
		}
		public void setYwtype(String ywtype) {
			this.ywtype = ywtype;
		}
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
       
		

	}

}
