package com.jcl.android.bean;

import java.io.Serializable;
import java.util.List;

public class PricesListBean extends BaseBean {

	private Data data;

	public List<Price> getData() {
		return data != null ? data.getPrice() : null;
	}

	public void setData(List<Price> data) {
		this.data.setPrice(data);
		;
	}

	public class Data {
		private List<Price> price;

		public List<Price> getPrice() {
			return price;
		}

		public void setPrice(List<Price> price) {
			this.price = price;
		}

	}

	public class Price implements Serializable {

		private String _id;
		private String phone;// 车主电话
		private String goodsid;// 货源id
		private String price;// 价格
		private String linkman;//联系人
		private String status;//状态
		private String submittype;

		public String get_id() {
			return _id;
		}
		public void set_id(String _id) {
			this._id = _id;
		}
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		public String getGoodsid() {
			return goodsid;
		}
		public void setGoodsid(String goodsid) {
			this.goodsid = goodsid;
		}
		public String getPrice() {
			return price;
		}
		public void setPrice(String price) {
			this.price = price;
		}
		public String getLinkman() {
			return linkman;
		}
		public void setLinkman(String linkman) {
			this.linkman = linkman;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getSubmittype() {
			return submittype;
		}
		public void setSubmittype(String submittype) {
			this.submittype = submittype;
		}
		
		
		

	}

}
