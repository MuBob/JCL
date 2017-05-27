package com.jcl.android.bean;


public class PersonalInfoBean extends BaseBean {
	
	private Data data;
	
	public Data getData() {
		if(data!=null)
			return data;
		else 
			return null;
	}

	public void setData(Data data) {
		if(data!=null)
			this.data=data;;
	}

	public class Data{
		
		private String _id;//用户id
		private String userid;//用户id
		private String mobile;//手机号
		private String submittype;//0:个人1：企业
		private String type;//0:车主/车队；1：货主；2：配货站；3：货代/第三方物流；4：货车生产商；5：仓储
		private String isauth;//0:未认证1：已认证
		private String personid;//个人信息id
		private String head;//头像路径(暂时是64位数据)
		private String nickname;//昵称
		private String email;
		private String post;//公司职务
		private String signname;//签名
		private String level;//等级
		private String trade;//行业
		private String companyname;//公司名称
		private String name;//个人姓名
		private String companyid;//企业信息id
		private String zhname;//公司中文名称
		private String enname;//公司英文名称
		private String linkman;//公司联系人
		private String address;//公司地址
		private String areanum;//公司地址
		private String companynum;//公司座机号
		private String netaddress;//公司网址
		private String content;//公司简介
		private String invoicetitle;//发票抬头
		private String advantage;//优势自荐
		private String addresse;//英文地址
		private String wechat;//微信
		private String qq;//qq
		private String invitecode;//验证码
		private String longitude;//经度
		private String latitude;//纬度
		private String iad;
		private String iadcode;
		private String idetail;
		private String companyinfok;
		
		public String getCompanyinfok() {
			return companyinfok;
		}
		public void setCompanyinfok(String companyinfok) {
			this.companyinfok = companyinfok;
		}
		public String getIad() {
			return iad;
		}
		public void setIad(String iad) {
			this.iad = iad;
		}
		public String getIadcode() {
			return iadcode;
		}
		public void setIadcode(String iadcode) {
			this.iadcode = iadcode;
		}
		public String getIdetail() {
			return idetail;
		}
		public void setIdetail(String idetail) {
			this.idetail = idetail;
		}
		public String getInvitecode() {
			return invitecode;
		}
		public void setInvitecode(String invitecode) {
			this.invitecode = invitecode;
		}
		private String addresscode;//城市编码
		private String city;//城市
		
		private String createtime;//注册时间
		public String getCreatetime() {
			return createtime;
		}
		public void setCreatetime(String createtime) {
			this.createtime = createtime;
		}
		public String getWechat() {
			return wechat;
		}
		public void setWechat(String wechat) {
			this.wechat = wechat;
		}
		public String getQq() {
			return qq;
		}
		public void setQq(String qq) {
			this.qq = qq;
		}
		public String getInvoicetitle() {
			return invoicetitle;
		}
		public void setInvoicetitle(String invoicetitle) {
			this.invoicetitle = invoicetitle;
		}
		public String getAdvantage() {
			return advantage;
		}
		public void setAdvantage(String advantage) {
			this.advantage = advantage;
		}
		public String getAddresse() {
			return addresse;
		}
		public void setAddresse(String addresse) {
			this.addresse = addresse;
		}
		
		public String get_id() {
			return _id;
		}
		public void set_id(String _id) {
			this._id = _id;
		}
		public String getUserid() {
			return userid;
		}
		public void setUserid(String userid) {
			this.userid = userid;
		}
		public String getMobile() {
			return mobile;
		}
		public void setMobile(String mobile) {
			this.mobile = mobile;
		}
		public String getSubmittype() {
			return submittype;
		}
		public void setSubmittype(String submittype) {
			this.submittype = submittype;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getIsauth() {
			return isauth;
		}
		public void setIsauth(String isauth) {
			this.isauth = isauth;
		}
		public String getPersonid() {
			return personid;
		}
		public void setPersonid(String personid) {
			this.personid = personid;
		}
		public String getHead() {
			return head;
		}
		public void setHead(String head) {
			this.head = head;
		}
		public String getNickname() {
			return nickname;
		}
		public void setNickname(String nickname) {
			this.nickname = nickname;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getPost() {
			return post;
		}
		public void setPost(String post) {
			this.post = post;
		}
		public String getSignname() {
			return signname;
		}
		public void setSignname(String signname) {
			this.signname = signname;
		}
		public String getLevel() {
			return level;
		}
		public void setLevel(String level) {
			this.level = level;
		}
		public String getTrade() {
			return trade;
		}
		public void setTrade(String trade) {
			this.trade = trade;
		}
		public String getCompanyname() {
			return companyname;
		}
		public void setCompanyname(String companyname) {
			this.companyname = companyname;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getCompanyid() {
			return companyid;
		}
		public void setCompanyid(String companyid) {
			this.companyid = companyid;
		}
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
		public String getAreanum() {
			return areanum;
		}
		public void setAreanum(String areanum) {
			this.areanum = areanum;
		}
		public String getCompanynum() {
			return companynum;
		}
		public void setCompanynum(String companynum) {
			this.companynum = companynum;
		}
		public String getNetaddress() {
			return netaddress;
		}
		public void setNetaddress(String newaddress) {
			this.netaddress = newaddress;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public String getEnname() {
			return enname;
		}
		public void setEnname(String enname) {
			this.enname = enname;
		}
		public String getLinkman() {
			return linkman;
		}
		public void setLinkman(String linkman) {
			this.linkman = linkman;
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
		public String getAddresscode() {
			return addresscode;
		}
		public void setAddresscode(String addresscode) {
			this.addresscode = addresscode;
		}
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
        
	}

}
