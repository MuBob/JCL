package com.jcl.android.bean;

import java.io.Serializable;
import java.util.List;


public class CarListBean extends BaseBean {
	
	private Data data;
	
	public List<CarInfo> getData() {
		return data!=null?data.getCars():null;
	}
	public void setData(List<CarInfo> data) {
		this.data.setCars(data);;
	}
	
	
	public class Data{
		private List<CarInfo> vans;

		public List<CarInfo> getCars() {
			return vans;
		}

		public void setCars(List<CarInfo> cars) {
			this.vans = cars;
		}
		
	}
	
	

	

}
