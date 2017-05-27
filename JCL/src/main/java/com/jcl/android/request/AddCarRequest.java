package com.jcl.android.request;

public class AddCarRequest  {
	
	private String type;
	private String data;
	private String operate;
	private String key;

	public AddCarRequest(String data) {
		this.type = "1004";
		this.data = data;
	}
	public AddCarRequest(String data,String key,String operate) {
		this.type = "1004";
		this.data = data;
		this.key=key;
		this.operate=operate;
	}
	

	public class AddCarData {
		private String userid;
		private String companyname;
		private String platenum;
		private String cartype;
		private String wx;
		private String approveweight;
		private String carsize;
		private String carlength;
		private String officeplace;
		
		private String xslicence;
		private String yylicence;
		private String bd;
		private String carimage1;//车头
		private String carimage2;//45°
		private String carimage3;//车尾

		private String status;
		private String createtime;
		private String effectivetime;
		private String effectiveflag;
		
		
		public AddCarData(String userid, String companyname, String platenum,
				String cartype, String wx, String approveweight, String carlength,
				String carsize,String officeplace,
				String carimage1, String carimage2,String carimage3,
				String xslicence, String yylicence, String bd) {
			this.userid = userid;
			this.companyname = companyname;
			this.platenum = platenum;
			this.cartype = cartype;
			this.wx = wx;
			this.approveweight = approveweight;
			this.carsize = carsize;
			this.carimage1 = carimage1;
			this.carimage2 = carimage2;
			this.carimage3 = carimage3;
			this.carlength = carlength;
			this.officeplace = officeplace;
			this.xslicence = xslicence;
			this.yylicence = yylicence;
			this.bd = bd;
		}

	}
}
