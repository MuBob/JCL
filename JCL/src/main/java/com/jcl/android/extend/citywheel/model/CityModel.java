package com.jcl.android.extend.citywheel.model;

import java.util.List;

public class CityModel {
	private String name;
	private String id;
	private String latitude;
	private String longitude;
	private List<DistrictModel> districtList;
	
	
	
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

	public CityModel() {
		super();
	}

	public CityModel(String name,String id, List<DistrictModel> districtList) {
		super();
		this.id = id;
		this.name = name;
		this.districtList = districtList;
	}
	public CityModel(String name,String id, List<DistrictModel> districtList,String latitude,String longitude) {
		super();
		this.id = id;
		this.name = name;
		this.districtList = districtList;
		this.latitude=latitude;
		this.longitude=longitude;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<DistrictModel> getDistrictList() {
		return districtList;
	}

	public void setDistrictList(List<DistrictModel> districtList) {
		this.districtList = districtList;
	}

	@Override
	public String toString() {
		return "CityModel [name=" + name + ", id=" + id + ", latitude="
				+ latitude + ", longitude=" + longitude + ", districtList="
				+ districtList + "]";
	}
	
}
