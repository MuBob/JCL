package com.jcl.android.bean;


public class VerifyCodeBean extends BaseBean {
	
	private Data data;
	
	public String getData() {
		if(data!=null)
			return data.getRandom();
		else 
			return null;
	}

	public void setData(String random) {
		if(data!=null)
			this.data.setRandom(random);;
	}

	public class Data{
		private String random;

		public String getRandom() {
			return random;
		}

		public void setRandom(String random) {
			this.random = random;
		}
	}

}
