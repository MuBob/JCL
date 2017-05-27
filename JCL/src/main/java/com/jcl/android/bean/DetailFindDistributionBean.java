package com.jcl.android.bean;

public class DetailFindDistributionBean extends BaseBean {
	
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
		private String ifcollect;
		
		private String start;// 出发地名称
		private String end;// 目的地名称
		private String linetype;//线路类型
		private String stopnames;// 经停站名
		
		
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
		public String getStart() {
			return start;
		}
		public void setStart(String start) {
			this.start = start;
		}
		public String getEnd() {
			return end;
		}
		public void setEnd(String end) {
			this.end = end;
		}
		public String getLinetype() {
			return linetype;
		}
		public void setLinetype(String linetype) {
			this.linetype = linetype;
		}
		public String getStopnames() {
			return stopnames;
		}
		public void setStopnames(String stopnames) {
			this.stopnames = stopnames;
		}
		
		
		

	}
}
