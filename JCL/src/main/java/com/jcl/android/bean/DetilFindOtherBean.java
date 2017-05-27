package com.jcl.android.bean;

import java.io.Serializable;


public class DetilFindOtherBean extends BaseBean {
	
	private Data data;

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}
	public class Data implements Serializable {

		private String _id;
		private String fbtype;// 发布类别
		private String ywtype;// 业务类别
		private String description;//描述（标题）
		private String saydetal;// 具体需求
		private String ifcollect;//是否收藏
		private String createtime;//时间
		private String other_image;
		
		public String getOther_image() {
			return other_image;
		}
		public void setOther_image(String other_image) {
			this.other_image = other_image;
		}
		public String getCreatetime() {
			return createtime;
		}
		public void setCreatetime(String createtime) {
			this.createtime = createtime;
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
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getSaydetal() {
			return saydetal;
		}
		public void setSaydetal(String saydetal) {
			this.saydetal = saydetal;
		}
		
	}

}
