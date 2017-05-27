package com.jcl.android.bean;

import java.io.Serializable;
import java.util.List;

import com.jcl.android.bean.DetailFindGoodsBean.Data.userInfo;

public class KuaiXunListBean extends BaseBean {
	
	private Data data;

	public List<KuaiXunInfo> getData() {
		return data != null ? data.getOthers() : null;
	}

	public void setData(List<KuaiXunInfo> data) {
		this.data.setOthers(data);
		;
	}

	public class Data {
		private List<KuaiXunInfo> flash;

		public List<KuaiXunInfo> getOthers() {
			return flash;
		}

		public void setOthers(List<KuaiXunInfo> flash) {
			this.flash = flash;
		}

	}

	public class KuaiXunInfo implements Serializable {

		private String _id;
		private String fbtype;// 发布类别
		private String userid;// 用户编码
		private String description;//描述（标题）
		private String createtime;//时间
		private String remark;
		private String ishowremark;
		private userinfo publisher;

		public String getIshowremark() {
			return ishowremark;
		}
		public void setIshowremark(String ishowremark) {
			this.ishowremark = ishowremark;
		}
		public userinfo getPublisher() {
			return publisher;
		}
		public void setPublisher(userinfo publisher) {
			this.publisher = publisher;
		}
		public class userinfo{
			private String address;
			private String id;
			private String isauth;
			private String level;
			private String mobile;
			private String submittype;
			private String type;
			private String uname;
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
		public String getUserid() {
			return userid;
		}
		public void setUserid(String userid) {
			this.userid = userid;
		}
		public String getRemark() {
			return remark;
		}
		public void setRemark(String remark) {
			this.remark = remark;
		}
		public String getCreatetime() {
			return createtime;
		}
		public void setCreatetime(String createtime) {
			this.createtime = createtime;
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
		
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		
	}

}
