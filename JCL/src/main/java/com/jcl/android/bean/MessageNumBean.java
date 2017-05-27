package com.jcl.android.bean;



public class MessageNumBean extends BaseBean {
	
	private Data data;

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}



	public static class Data{
		private String count;

		public String getCount() {
			return count;
		}

		public void setCount(String count) {
			this.count = count;
		}
		
	}
	
}
