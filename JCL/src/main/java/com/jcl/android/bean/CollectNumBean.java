package com.jcl.android.bean;

import java.util.List;

public class CollectNumBean extends BaseBean {
	
	private Data data;

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}



	public static class Data{
		private List<Type> types;//	出发地	

		public List<Type> getTypes() {
			return types;
		}

		public void setTypes(List<Type> types) {
			this.types = types;
		}
		

	}
	
	public static class Type{
		private String count;
		private String type;
		public String getCount() {
			return count;
		}
		public void setCount(String count) {
			this.count = count;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		
	}
}
