package com.jcl.android.bean;

public class DetailFindStorageBean extends BaseBean {
	
	private Data data;

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public class Data{
		private String zhname;//	公司名称	
		private String address;//	地址	
		private String phone;//	电话
		private String linkman;//	联系人
		private String content;//	公司简介
		private String startarea;
		private String endarea;
		private String ifcollect;
		
		
		public String getIfcollect() {
			return ifcollect;
		}
		public void setIfcollect(String ifcollect) {
			this.ifcollect = ifcollect;
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
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		public String getLinkman() {
			return linkman;
		}
		public void setLinkman(String linkman) {
			this.linkman = linkman;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
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
		
		
		

	}
}
