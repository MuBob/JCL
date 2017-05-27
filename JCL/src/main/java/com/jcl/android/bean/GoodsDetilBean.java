package com.jcl.android.bean;


public class GoodsDetilBean extends BaseBean {
	
	private Data data;

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public class Data{
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
		private String jjdegree;//	紧急程度
		private String ptchooes;//
		private String pztype;//泡货或重货
		
		private String ponum;//Po编号
		private String num;//件数
		private String maxgoodssize;//超尺寸货物
		private String gxgl;//柜型柜量
		private String lastarrivetime;//最晚到达时间
		private String loadingtime;//装货时间
		private String remark;//备注
		private String mytype;//贸易类型
		private String isLongTermSource;//长期货源
		private String ptchoose;//平台选择
		private String fftype;//付费方式
		private String needinvoice;//发票
		private String fhlinkman;//发货人
		private String fhlinkmantel;//发货人电话
		private String shlinkman;//收货人
		private String shlinkmantel;//收货人电话
		private String signorder;//签收回单
		private String incrementerm;//增值服务
		private String oneremark;//航班名次
		private String tworemark;//提单号
		private String lastbjtime;//最晚报价
		private String haspriceheight;//目标价高
		private String haspricelow;//目标价低
		private String carname;
		private String bztype;//包装方式
		private String unit;
		
		public String getUnit() {
			return unit;
		}
		public void setUnit(String unit) {
			this.unit = unit;
		}
		public String getBztype() {
			return bztype;
		}
		public void setBztype(String bztype) {
			this.bztype = bztype;
		}
		public String getCarname() {
			return carname;
		}
		public void setCarname(String carname) {
			this.carname = carname;
		}
		public String getHaspriceheight() {
			return haspriceheight;
		}
		public void setHaspriceheight(String haspriceheight) {
			this.haspriceheight = haspriceheight;
		}
		public String getHaspricelow() {
			return haspricelow;
		}
		public void setHaspricelow(String haspricelow) {
			this.haspricelow = haspricelow;
		}
		public String getLastbjtime() {
			return lastbjtime;
		}
		public void setLastbjtime(String lastbjtime) {
			this.lastbjtime = lastbjtime;
		}
		public String getIncrementerm() {
			return incrementerm;
		}
		public void setIncrementerm(String incrementerm) {
			this.incrementerm = incrementerm;
		}
		public String getOneremark() {
			return oneremark;
		}
		public void setOneremark(String oneremark) {
			this.oneremark = oneremark;
		}
		public String getTworemark() {
			return tworemark;
		}
		public void setTworemark(String tworemark) {
			this.tworemark = tworemark;
		}
		public String getSignorder() {
			return signorder;
		}
		public void setSignorder(String signorder) {
			this.signorder = signorder;
		}
		public String getFhlinkman() {
			return fhlinkman;
		}
		public void setFhlinkman(String fhlinkman) {
			this.fhlinkman = fhlinkman;
		}
		public String getFhlinkmantel() {
			return fhlinkmantel;
		}
		public void setFhlinkmantel(String fhlinkmantel) {
			this.fhlinkmantel = fhlinkmantel;
		}
		public String getShlinkman() {
			return shlinkman;
		}
		public void setShlinkman(String shlinkman) {
			this.shlinkman = shlinkman;
		}
		public String getShlinkmantel() {
			return shlinkmantel;
		}
		public void setShlinkmantel(String shlinkmantel) {
			this.shlinkmantel = shlinkmantel;
		}
		public String getPtchoose() {
			return ptchoose;
		}
		public String getFftype() {
			return fftype;
		}
		public void setFftype(String fftype) {
			this.fftype = fftype;
		}
		public String getNeedinvoice() {
			return needinvoice;
		}
		public void setNeedinvoice(String needinvoice) {
			this.needinvoice = needinvoice;
		}
		public void setPtchoose(String ptchoose) {
			this.ptchoose = ptchoose;
		}
		public String getIsLongTermSource() {
			return isLongTermSource;
		}
		public void setIsLongTermSource(String isLongTermSource) {
			this.isLongTermSource = isLongTermSource;
		}
		public String getMytype() {
			return mytype;
		}
		public void setMytype(String mytype) {
			this.mytype = mytype;
		}
		public String getPonum() {
			return ponum;
		}
		public void setPonum(String ponum) {
			this.ponum = ponum;
		}
		public String getNum() {
			return num;
		}
		public void setNum(String num) {
			this.num = num;
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
		public String getLastarrivetime() {
			return lastarrivetime;
		}
		public void setLastarrivetime(String lastarrivetime) {
			this.lastarrivetime = lastarrivetime;
		}
		public String getLoadingtime() {
			return loadingtime;
		}
		public void setLoadingtime(String loadingtime) {
			this.loadingtime = loadingtime;
		}
		public String getRemark() {
			return remark;
		}
		public void setRemark(String remark) {
			this.remark = remark;
		}
		public String getPtchooes() {
			return ptchooes;
		}
		public void setPtchooes(String ptchooes) {
			this.ptchooes = ptchooes;
		}
		public String getPztype() {
			return pztype;
		}
		public void setPztype(String pztype) {
			this.pztype = pztype;
		}
		public String getIfcollect() {
			return ifcollect;
		}
		public void setIfcollect(String ifcollect) {
			this.ifcollect = ifcollect;
		}
		public String get_id() {
			return _id;
		}
		public void set_id(String _id) {
			this._id = _id;
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
		public String getJjdegree() {
			return jjdegree;
		}
		public void setJjdegree(String jjdegree) {
			this.jjdegree = jjdegree;
		}
		
	}
}
