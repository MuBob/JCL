package com.jcl.android.bean;

import java.util.List;

public class FindCarOrderListBean extends BaseBean {
	private Data data;
	
	public List<Vanorders> getData() {
		return data!=null?data.getVanorders():null;
	}

	public void setData(List<Vanorders> data) {
		this.data.setVanorders(data);;
	}

	public class Data{
		private List<Vanorders> vanorders;

		public List<Vanorders> getVanorders() {
			return vanorders;
		}

		public void setVanorders(List<Vanorders> vanorders) {
			this.vanorders = vanorders;
		}
		
		
	}
    //车单
	public class Vanorders {
		private String _id;
		private String startarea;// 出发地名称
		private String endarea;// 目的地名称
		private String status;// 状态
		
		private String goodorder;
		private String ponum;
		private String detailname;
		private String goodsweigt;
		private String price;
		private String goodsid;
		private String linkman;
		private String phone;
		private String zffs;//支付方式
		
		
		

		public String get_id() {
			return _id;
		}
		public void set_id(String _id) {
			this._id = _id;
		}
		public String getDetailname() {
			return detailname;
		}
		public void setDetailname(String detailname) {
			this.detailname = detailname;
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
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getGoodorder() {
			return goodorder;
		}
		public void setGoodorder(String goodorder) {
			this.goodorder = goodorder;
		}
		public String getPonum() {
			return ponum;
		}
		public void setPonum(String ponum) {
			this.ponum = ponum;
		}
		
		public String getGoodsweigt() {
			return goodsweigt;
		}
		public void setGoodsweigt(String goodsweigt) {
			this.goodsweigt = goodsweigt;
		}
		public String getPrice() {
			return price;
		}
		public void setPrice(String price) {
			this.price = price;
		}
		public String getGoodsid() {
			return goodsid;
		}
		public void setGoodsid(String goodsid) {
			this.goodsid = goodsid;
		}
		public String getLinkman() {
			return linkman;
		}
		public void setLinkman(String linkman) {
			this.linkman = linkman;
		}
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		public String getZffs() {
			return zffs;
		}
		public void setZffs(String zffs) {
			this.zffs = zffs;
		}
		
		
		

	}

}
