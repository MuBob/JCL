package com.jcl.android.bean;

import java.io.Serializable;
import java.util.List;

public class VouchsListBean extends BaseBean {

	private Data data;

	public List<VouchInfo> getData() {
		return data != null ? data.getVouchs() : null;
	}

	public void setData(List<VouchInfo> data) {
		this.data.setVouchs(data);
	}

	public class Data {
		private List<VouchInfo> vouchs;

		public List<VouchInfo> getVouchs() {
			return vouchs;
		}

		public void setVouchs(List<VouchInfo> vouchs) {
			this.vouchs = vouchs;
		}

	}

	public class VouchInfo implements Serializable {

		private String _id;
		private String price;
		private String createtime;
		private String status;
		private String ordernum;
		private String userid;
		private String goodsid;
		private String startarea;
		private String endarea;
		public String get_id() {
			return _id;
		}
		public void set_id(String _id) {
			this._id = _id;
		}
		public String getPrice() {
			return price;
		}
		public void setPrice(String price) {
			this.price = price;
		}
		public String getCreatetime() {
			return createtime;
		}
		public void setCreatetime(String createtime) {
			this.createtime = createtime;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getOrdernum() {
			return ordernum;
		}
		public void setOrdernum(String ordernum) {
			this.ordernum = ordernum;
		}
		public String getUserid() {
			return userid;
		}
		public void setUserid(String userid) {
			this.userid = userid;
		}
		public String getGoodsid() {
			return goodsid;
		}
		public void setGoodsid(String goodsid) {
			this.goodsid = goodsid;
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
