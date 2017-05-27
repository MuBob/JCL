package com.jcl.android.bean;

import java.io.Serializable;
import java.util.List;

public class FindDedicateCarsListBean extends BaseBean {
	private Data data;
	
	public List<Line> getData() {
		return data!=null?data.getCars():null;
	}

	public void setData(List<Line> data) {
		this.data.setCars(data);;
	}

	public class Data{
		private List<Line> logistics;

		public List<Line> getCars() {
			return logistics;
		}

		public void setCars(List<Line> cars) {
			this.logistics = cars;
		}
		
		
	}

	public class Line implements Serializable{
		private String _id;
		private String company;//联系人
		private String startarea;// 出发地名称
		private String endarea;// 目的地名称
		private String linetype;// 线路类型（0:直达1:中转）
		private String stopnames;// 经停站（格式为：淄博,济南）
		private String xl_description;//描述
		private String sale;//特价
		private String zx_image;//图片
		private String end_date;//截止时间
		private String wd_address_cf;//出发网点
		private String wd_address_md;//目的网点
		private String name;// 姓名
		private String platenum;// 车牌号
		private String linkman;// 联系人
		private String phone;// 手机号
		private String userid;// 用户编码
		private String startcode;//
		private String endcode;//
		private String price;//价格
		private String remark;//说明
		private String w_price;//重货
		private String l_price;//轻货
		private String m_price;//最低一票
		private String zx_sx;//时效
		private String one_self;//自荐路线 0否
		private String isauth;//用户等级
		
		private String mudi_lat;//自荐路线
		private String mudi_lng;//用户等级
		private String chufa_lat;//自荐路线
		private String chufa_lng;//用户等级
		private String adminid ;//圈子
		private String invitecode ;//圈子
		private String user_type;
		
		
		public String getUser_type() {
			return user_type;
		}
		public void setUser_type(String user_type) {
			this.user_type = user_type;
		}
		public String getInvitecode() {
			return invitecode;
		}
		public void setInvitecode(String invitecode) {
			this.invitecode = invitecode;
		}
		public String getAdminid() {
			return adminid;
		}
		public void setAdminid(String adminid) {
			this.adminid = adminid;
		}
		public String getMudi_lat() {
			return mudi_lat;
		}
		public void setMudi_lat(String mudi_lat) {
			this.mudi_lat = mudi_lat;
		}
		public String getMudi_lng() {
			return mudi_lng;
		}
		public void setMudi_lng(String mudi_lng) {
			this.mudi_lng = mudi_lng;
		}
		public String getChufa_lat() {
			return chufa_lat;
		}
		public void setChufa_lat(String chufa_lat) {
			this.chufa_lat = chufa_lat;
		}
		public String getChufa_lng() {
			return chufa_lng;
		}
		public void setChufa_lng(String chufa_lng) {
			this.chufa_lng = chufa_lng;
		}
		public String getIsauth() {
			return isauth;
		}
		public void setIsauth(String isauth) {
			this.isauth = isauth;
		}
		public String getOne_self() {
			return one_self;
		}
		public void setOne_self(String one_self) {
			this.one_self = one_self;
		}
		public String getW_price() {
			return w_price;
		}
		public void setW_price(String w_price) {
			this.w_price = w_price;
		}
		public String getL_price() {
			return l_price;
		}
		public void setL_price(String l_price) {
			this.l_price = l_price;
		}
		public String getM_price() {
			return m_price;
		}
		public void setM_price(String m_price) {
			this.m_price = m_price;
		}
		public String getZx_sx() {
			return zx_sx;
		}
		public void setZx_sx(String zx_sx) {
			this.zx_sx = zx_sx;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getPlatenum() {
			return platenum;
		}
		public void setPlatenum(String platenum) {
			this.platenum = platenum;
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
		public String getUserid() {
			return userid;
		}
		public void setUserid(String userid) {
			this.userid = userid;
		}
		public String getStartcode() {
			return startcode;
		}
		public void setStartcode(String startcode) {
			this.startcode = startcode;
		}
		public String getEndcode() {
			return endcode;
		}
		public void setEndcode(String endcode) {
			this.endcode = endcode;
		}
		public String getPrice() {
			return price;
		}
		public void setPrice(String price) {
			this.price = price;
		}
		public String getRemark() {
			return remark;
		}
		public void setRemark(String remark) {
			this.remark = remark;
		}
		public String getXl_description() {
			return xl_description;
		}
		public void setXl_description(String xl_description) {
			this.xl_description = xl_description;
		}
		public String getSale() {
			return sale;
		}
		public void setSale(String sale) {
			this.sale = sale;
		}
		public String getZx_image() {
			return zx_image;
		}
		public void setZx_image(String zx_image) {
			this.zx_image = zx_image;
		}
		public String getEnd_date() {
			return end_date;
		}
		public void setEnd_date(String end_date) {
			this.end_date = end_date;
		}
		public String getWd_address_cf() {
			return wd_address_cf;
		}
		public void setWd_address_cf(String wd_address_cf) {
			this.wd_address_cf = wd_address_cf;
		}
		public String getWd_address_md() {
			return wd_address_md;
		}
		public void setWd_address_md(String wd_address_md) {
			this.wd_address_md = wd_address_md;
		}
		public String get_id() {
			return _id;
		}
		public void set_id(String _id) {
			this._id = _id;
		}
		public String getCompany() {
			return company;
		}
		public void setCompany(String company) {
			this.company = company;
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
