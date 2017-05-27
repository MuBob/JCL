package com.jcl.android.extend.citywheel.model;

public class DistrictModel {
	private String name;
	private String id;
	private String latitude;
	private String longitude;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public DistrictModel() {
		super();
	}

	public DistrictModel(String name, String id) {
		super();
		this.name = name;
		this.id = id;
	}
	
	public DistrictModel(String name, String id,String latitude,String longitude) {
		super();
		this.name = name;
		this.id = id;
		this.latitude=latitude;
		this.longitude=longitude;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "DistrictModel [name=" + name + ", id=" + id + ", latitude="
				+ latitude + ", longitude=" + longitude + "]";
	}

}
