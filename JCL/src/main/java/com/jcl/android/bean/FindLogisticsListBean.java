package com.jcl.android.bean;

import java.util.List;
/**
 * 查找物流公司列表实体
 * @author xuelei
 *
 */
public class FindLogisticsListBean extends BaseBean {
	
		private List<Logistics> data;


	public List<Logistics> getData() {
			return data;
		}

		public void setData(List<Logistics> data) {
			this.data = data;
		}

	public class Logistics {
		
		private String _id;
		private String startarea;// 出发地名称
		private String endarea;// 目的地名称
		private String company;// 公司
		private String linetype;//线路类型
		private String phone;//电话
		private String stopnames;// 经停站名
		private String address;
		private String zhname;
		
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
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		
	}

}
