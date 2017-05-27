package com.jcl.android.bean;

import java.util.List;
/**
 * 查找配货站列表实体
 * @author xuelei
 *
 */
public class FindDistributionListBean extends BaseBean {
	private Data data;
	
	public List<Distribution> getData() {
		return data!=null?data.getLogistics():null;
	}

	public void setData(List<Distribution> data) {
		this.data.setLogistics(data);;
	}

	public class Data{
		private List<Distribution> logistics;

		public List<Distribution> getLogistics() {
			return logistics;
		}

		public void setLogistics(List<Distribution> logistics) {
			this.logistics = logistics;
		}
	}

	public class Distribution {
		
		private String _id;
		private String address;// 出发地名称
		private String zhname;// 公司
		private String phone;
		public String get_id() {
			return _id;
		}
		public void set_id(String _id) {
			this._id = _id;
		}
		
		public String getZhname() {
			return zhname;
		}
		public void setZhname(String zhname) {
			this.zhname = zhname;
		}
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		
	}

}
