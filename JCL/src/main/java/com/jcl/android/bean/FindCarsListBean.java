package com.jcl.android.bean;

import java.util.List;

public class FindCarsListBean extends BaseBean {
	private Data data;
	
	public List<Cars> getData() {
		return data!=null?data.getCars():null;
	}

	public void setData(List<Cars> data) {
		this.data.setCars(data);;
	}

	public class Data{
		private List<Cars> vans;

		public List<Cars> getCars() {
			return vans;
		}

		public void setCars(List<Cars> cars) {
			this.vans = cars;
		}
		
		
	}

	public class Cars {
		private String _id;
		private String linkman;//联系人
		private String tel;//联系电话
		private String startarea;// 出发地名称
		private String endarea;// 目的地名称
		private String exfhstarttime;// 装货时间
		private String emptytimestart;//空车时间开始
		private String emptytimeend;//空车时间结束
		private String cartype;// 车型
		private String cartypecode;
		private String carlength;// 车长
		private String expectfee;//期望运费
		private String longitude;// 发货的经度 查询附近的货源时带出
		private String latitude;// 发货的纬度
		private String position;//当前位置
		private String platenum;//车牌号
		private String ischeck;//车辆认证
		private String carimage1;//车头  图片
		private String carimage2;//车头  图片
		private String carimage3;//车头  图片
		private String isLongTermSource;//长期车源
		private String cytype;
		private String createtime;
		private String gone;
		public String getPosition() {
			return position;
		}
		public void setPosition(String position) {
			this.position = position;
		}
		public String getGone() {
			return gone;
		}
		public void setGone(String gone) {
			this.gone = gone;
		}
		public String getCreatetime() {
			return createtime;
		}
		public void setCreatetime(String createtime) {
			this.createtime = createtime;
		}
		public String getOfficeplace() {
			return officeplace;
		}
		public void setOfficeplace(String officeplace) {
			this.officeplace = officeplace;
		}
		private String jjdegree;
		private String commony;
		private String officeplace;
		
		
		
		
		
		
		
		public String getCommony() {
			return commony;
		}
		public void setCommony(String commony) {
			this.commony = commony;
		}
		public String getJjdegree() {
			return jjdegree;
		}
		public void setJjdegree(String jjdegree) {
			this.jjdegree = jjdegree;
		}
		public String getCytype() {
			return cytype;
		}
		public void setCytype(String cytype) {
			this.cytype = cytype;
		}
		public String getCarimage2() {
			return carimage2;
		}
		public void setCarimage2(String carimage2) {
			this.carimage2 = carimage2;
		}
		public String getCarimage3() {
			return carimage3;
		}
		public void setCarimage3(String carimage3) {
			this.carimage3 = carimage3;
		}
		public String getIsLongTermSource() {
			return isLongTermSource;
		}
		public void setIsLongTermSource(String isLongTermSource) {
			this.isLongTermSource = isLongTermSource;
		}
		public String getCarimage1() {
			return carimage1;
		}
		public void setCarimage1(String carimage1) {
			this.carimage1 = carimage1;
		}
		public String getIscheck() {
			return ischeck;
		}
		public void setIscheck(String ischeck) {
			this.ischeck = ischeck;
		}
		public String getCartypecode() {
			return cartypecode;
		}
		public void setCartypecode(String cartypecode) {
			this.cartypecode = cartypecode;
		}
		public String getEmptytimestart() {
			return emptytimestart;
		}
		public void setEmptytimestart(String emptytimestart) {
			this.emptytimestart = emptytimestart;
		}
		public String getEmptytimeend() {
			return emptytimeend;
		}
		public void setEmptytimeend(String emptytimeend) {
			this.emptytimeend = emptytimeend;
		}
		
		public String getPlatenum() {
			return platenum;
		}
		public void setPlatenum(String platenum) {
			this.platenum = platenum;
		}
		public String getStartarea() {
			return startarea;
		}
		public void setStartarea(String startarea) {
			this.startarea = startarea;
		}
		public String get_id() {
			return _id;
		}
		public void set_id(String _id) {
			this._id = _id;
		}
		public String getLinkman() {
			return linkman;
		}
		public void setLinkman(String linkman) {
			this.linkman = linkman;
		}
		public String getTel() {
			return tel;
		}
		public void setTel(String tel) {
			this.tel = tel;
		}
		public String getExpectfee() {
			return expectfee;
		}
		public void setExpectfee(String expectfee) {
			this.expectfee = expectfee;
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
		
		
	}

}
