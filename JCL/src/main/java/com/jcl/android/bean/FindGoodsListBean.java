package com.jcl.android.bean;

import java.util.List;

public class FindGoodsListBean extends BaseBean {
	private Data data;
	
	public List<Goods> getData() {
		return data!=null?data.getGoods():null;
	}

	public void setData(List<Goods> data) {
		this.data.setGoods(data);;
	}

	public class Data{
		private List<Goods> goods;

		public List<Goods> getGoods() {
			return goods;
		}

		public void setGoods(List<Goods> goods) {
			this.goods = goods;
		}
		
		
	}

	public class Goods {
		private String _id;
		private String startarea;// 出发地名称
		private String endarea;// 目的地名称
		private String exfhstarttime;// 装货时间
		private String cartype;// 车型
		private String carlength;// 车长
		private String status;// 状态
								// 0无报价,有报价（1未采用报价，2已采用报价，3已承运，4货物已送达，5已成交，）9被删除
		private String longitude;// 发货的经度 查询附近的货源时带出
		private String latitude;// 发货的纬度
		private String detailname;//	品名
		private String goodstj;//	体积
		private String goodsweight;//	重量
		
		private String createtime;//
		private String effectiveflag;//	重量
		private String effectivetime;//	重量
		private String exfhendtime;//装货时间
		private String hwtype;//货物类型
		private String ifcollect;//	是否已收藏  0,未收藏，1已收藏 
		private String jjdegree;//	是否加急  0否1是
		private String carname;
		private String goodorder;
		private String price;//承运价格
		private String linkman;//联系人
		private String phone;//联系电话
		private String zffs;//支付方式
		private String publishstatus; // 发布
		private String carlengthabove;
		private String gone;
		private String sourceimage;
		
		public String getSourceimage() {
			return sourceimage;
		}
		public void setSourceimage(String sourceimage) {
			this.sourceimage = sourceimage;
		}
		public String getGone() {
			return gone;
		}
		public void setGone(String gone) {
			this.gone = gone;
		}
		public String getCarlengthabove() {
			return carlengthabove;
		}
		public void setCarlengthabove(String carlengthabove) {
			this.carlengthabove = carlengthabove;
		}
		public String getPublishstatus() {
			return publishstatus;
		}
		public void setPublishstatus(String publishstatus) {
			this.publishstatus = publishstatus;
		}
		private String ponum;//Po号
		
		public String getPonum() {
			return ponum;
		}
		public void setPonum(String ponum) {
			this.ponum = ponum;
		}
		public String getCarname() {
			return carname;
		}
		public void setCarname(String carname) {
			this.carname = carname;
		}
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
		public String getGoodstj() {
			return goodstj;
		}
		public void setGoodstj(String goodstj) {
			this.goodstj = goodstj;
		}
		public String getGoodsweight() {
			return goodsweight;
		}
		public void setGoodsweight(String goodsweight) {
			this.goodsweight = goodsweight;
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
		public String getExfhstarttime() {
			return exfhstarttime;
		}
		public void setExfhstarttime(String exfhstarttime) {
			this.exfhstarttime = exfhstarttime;
		}
		public String getCartype() {
			return cartype;
		}
		public void setCartype(String cartype) {
			this.cartype = cartype;
		}
		public String getCarlength() {
			return carlength;
		}
		public void setCarlength(String carlength) {
			this.carlength = carlength;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getLongitude() {
			return longitude;
		}
		public void setLongitude(String longitude) {
			this.longitude = longitude;
		}
		public String getLatitude() {
			return latitude;
		}
		public void setLatitude(String latitude) {
			this.latitude = latitude;
		}
		public String getCreatetime() {
			return createtime;
		}
		public void setCreatetime(String createtime) {
			this.createtime = createtime;
		}
		public String getEffectiveflag() {
			return effectiveflag;
		}
		public void setEffectiveflag(String effectiveflag) {
			this.effectiveflag = effectiveflag;
		}
		public String getEffectivetime() {
			return effectivetime;
		}
		public void setEffectivetime(String effectivetime) {
			this.effectivetime = effectivetime;
		}
		public String getExfhendtime() {
			return exfhendtime;
		}
		public void setExfhendtime(String exfhendtime) {
			this.exfhendtime = exfhendtime;
		}
		public String getHwtype() {
			return hwtype;
		}
		public void setHwtype(String hwtype) {
			this.hwtype = hwtype;
		}
		public String getIfcollect() {
			return ifcollect;
		}
		public void setIfcollect(String ifcollect) {
			this.ifcollect = ifcollect;
		}
		public String getJjdegree() {
			return jjdegree;
		}
		public void setJjdegree(String jjdegree) {
			this.jjdegree = jjdegree;
		}
		public String getGoodorder() {
			return goodorder;
		}
		public void setGoodorder(String goodorder) {
			this.goodorder = goodorder;
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
