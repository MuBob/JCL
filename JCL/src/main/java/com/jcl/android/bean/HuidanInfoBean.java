package com.jcl.android.bean;


public class HuidanInfoBean extends BaseBean {
	
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
		private String receiptimage;

		public String getReceiptimage() {
			return receiptimage;
		}

		public void setReceiptimage(String receiptimage) {
			this.receiptimage = receiptimage;
		}
		
		   
	}

}
