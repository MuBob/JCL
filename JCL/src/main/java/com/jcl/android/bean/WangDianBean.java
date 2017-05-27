package com.jcl.android.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class WangDianBean extends BaseBean{
	private MyData data;
	
	public MyData getData() {
		return data;
	}

	public void setData(MyData data) {
		this.data = data;
	}

	public class MyData{
		private ArrayList<AddressList> addresslist;

		public ArrayList<AddressList> getAddresslist() {
			return addresslist;
		}
		public void setAddresslist(ArrayList<AddressList> addresslist) {
			this.addresslist = addresslist;
		}
	}

	public class AddressList implements Serializable{
		private String _id;
		private String createtime;
		private String a_company;
		private String a_mobile;
		private String weixin;
		private String a_phone;
		private String invoice_d;
		private String email;
		private String ispc;
		private String pname;
		private String userid;
		private String address_xx;
		private String submittype;
		private String type;
		private String qq;
		public String get_id() {
			return _id;
		}
		public void set_id(String _id) {
			this._id = _id;
		}
		public String getCreatetime() {
			return createtime;
		}
		public void setCreatetime(String createtime) {
			this.createtime = createtime;
		}
		public String getA_company() {
			return a_company;
		}
		public void setA_company(String a_company) {
			this.a_company = a_company;
		}
		public String getA_mobile() {
			return a_mobile;
		}
		public void setA_mobile(String a_mobile) {
			this.a_mobile = a_mobile;
		}
		public String getWeixin() {
			return weixin;
		}
		public void setWeixin(String weixin) {
			this.weixin = weixin;
		}
		public String getA_phone() {
			return a_phone;
		}
		public void setA_phone(String a_phone) {
			this.a_phone = a_phone;
		}
		public String getInvoice_d() {
			return invoice_d;
		}
		public void setInvoice_d(String invoice_d) {
			this.invoice_d = invoice_d;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getIspc() {
			return ispc;
		}
		public void setIspc(String ispc) {
			this.ispc = ispc;
		}
		public String getPname() {
			return pname;
		}
		public void setPname(String pname) {
			this.pname = pname;
		}
		public String getUserid() {
			return userid;
		}
		public void setUserid(String userid) {
			this.userid = userid;
		}
		public String getAddress_xx() {
			return address_xx;
		}
		public void setAddress_xx(String address_xx) {
			this.address_xx = address_xx;
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
		public String getQq() {
			return qq;
		}
		public void setQq(String qq) {
			this.qq = qq;
		}
		
	}

}
