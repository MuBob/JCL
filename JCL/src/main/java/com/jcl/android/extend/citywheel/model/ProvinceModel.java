package com.jcl.android.extend.citywheel.model;

import java.util.List;

public class ProvinceModel {
	private String name;
	private String id;
	private String latitude;
	private String longitude;
	private List<CityModel> cityList;
	
	
	
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

	public ProvinceModel() {
		super();
	}

	public ProvinceModel(String name,String id, List<CityModel> cityList) {
		super();
		this.name = name;
		this.id = id;
		this.cityList = cityList;
	}
	public ProvinceModel(String name,String id, List<CityModel> cityList,String latitude,String longitude) {
		super();
		this.name = name;
		this.id = id;
		this.cityList = cityList;
		this.latitude=latitude;
		this.longitude=longitude;
	}
	
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<CityModel> getCityList() {
		return cityList;
	}

	public void setCityList(List<CityModel> cityList) {
		this.cityList = cityList;
	}

	@Override
	public String toString() {
		return "ProvinceModel [name=" + name + ", id=" + id + ", latitude="
				+ latitude + ", longitude=" + longitude + ", cityList="
				+ cityList + "]";
	}

	
	
}
