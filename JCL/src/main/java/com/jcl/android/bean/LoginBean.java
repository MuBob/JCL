package com.jcl.android.bean;


public class LoginBean extends BaseBean {
	
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
		
		public String getUserid() {
			return userid;
		}
		public void setUserid(String userid) {
			this.userid = userid;
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
		public String getIsauth() {
			return isauth;
		}
		public void setIsauth(String isauth) {
			this.isauth = isauth;
		}
		public String getHead() {
			return head;
		}
		public void setHead(String head) {
			this.head = head;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getCompanyname() {
			return companyname;
		}
		public void setCompanyname(String companyname) {
			this.companyname = companyname;
		}
		public String getZhname() {
			return zhname;
		}
		public void setZhname(String zhname) {
			this.zhname = zhname;
		}
		public String getAreanum() {
			return areanum;
		}
		public void setAreanum(String areanum) {
			this.areanum = areanum;
		}
		public String getCompanynum() {
			return companynum;
		}
		public void setCompanynum(String companynum) {
			this.companynum = companynum;
		}
		private String userid;
		private String mobile;
		private String submittype;
		private String type;
		private String isauth;
		private String head;
		private String name;
		private String companyname;
		private String zhname;
		private String areanum;
		private String companynum;
		private String linkman;
		private String invitecode;
		private String icount;
		
		public String getIcount() {
			return icount;
		}
		public void setIcount(String icount) {
			this.icount = icount;
		}
		public String getInvitecode() {
			return invitecode;
		}
		public void setInvitecode(String invitecode) {
			this.invitecode = invitecode;
		}
		public String getLinkman() {
			return linkman;
		}
		public void setLinkman(String linkman) {
			this.linkman = linkman;
		}

	}

}
