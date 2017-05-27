package com.jcl.android.bean;

import java.io.Serializable;
import java.util.List;

public class GoodsListBean extends BaseBean {

	private Data data;

	public List<GoodsInfo> getData() {
		return data != null ? data.getGoods() : null;
	}

	public void setData(List<GoodsInfo> data) {
		this.data.setGoods(data);
		;
	}

	public class Data {
		private List<GoodsInfo> goods;

		public List<GoodsInfo> getGoods() {
			return goods;
		}

		public void setGoods(List<GoodsInfo> goods) {
			this.goods = goods;
		}

	}

	public class GoodsInfo implements Serializable {

		private String _id;
		private String ponum;// po号码
		private String detailname;// 具体品名
		private String mytype;// 贸易类型 出口0进口1内贸2　
		private String bztype;// 包装方式 　纸箱0，托盘1，卷2，液袋3，其他包装4
		private String num;// 件数 　
		private String goodsweight;// 货物总重量 　
		private String goodstj;// 货物总体积 　
		private String maxgoodssize;// 超长尺寸货物 　
		private String gxgl;//柜型柜量
		private String companyname;//柜型柜量
        private String unit;
		
		public String getUnit() {
			return unit;
		}
		public void setUnit(String unit) {
			this.unit = unit;
		}
		public String getCompanyname() {
			return companyname;
		}
		public void setCompanyname(String companyname) {
			this.companyname = companyname;
		}
		public String get_id() {
			return _id;
		}
		public void set_id(String _id) {
			this._id = _id;
		}
		public String getPonum() {
			return ponum;
		}
		public void setPonum(String ponum) {
			this.ponum = ponum;
		}
		public String getDetailname() {
			return detailname;
		}
		public void setDetailname(String detailname) {
			this.detailname = detailname;
		}
		public String getMytype() {
			return mytype;
		}
		public void setMytype(String mytype) {
			this.mytype = mytype;
		}
		public String getBztype() {
			return bztype;
		}
		public void setBztype(String bztype) {
			this.bztype = bztype;
		}
		public String getNum() {
			return num;
		}
		public void setNum(String num) {
			this.num = num;
		}
		public String getGoodsweight() {
			return goodsweight;
		}
		public void setGoodsweight(String goodsweight) {
			this.goodsweight = goodsweight;
		}
		public String getGoodstj() {
			return goodstj;
		}
		public void setGoodstj(String goodstj) {
			this.goodstj = goodstj;
		}
		public String getMaxgoodssize() {
			return maxgoodssize;
		}
		public void setMaxgoodssize(String maxgoodssize) {
			this.maxgoodssize = maxgoodssize;
		}
		public String getGxgl() {
			return gxgl;
		}
		public void setGxgl(String gxgl) {
			this.gxgl = gxgl;
		}
		
	}

}
