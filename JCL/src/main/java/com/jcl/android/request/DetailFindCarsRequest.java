package com.jcl.android.request;

public class DetailFindCarsRequest {

	private String filters;
	private String type;
	private String sorts;

	public DetailFindCarsRequest(String filters) {
		this.filters = filters;
		this.type = "2003";
		this.sorts = "";
	}
	
	
	public class Filters {
		private String _id;

		public Filters(String _id) {
			this._id = _id;
		}

	}
}
