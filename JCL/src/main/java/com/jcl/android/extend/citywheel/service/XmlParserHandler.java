package com.jcl.android.extend.citywheel.service;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.jcl.android.extend.citywheel.model.CityModel;
import com.jcl.android.extend.citywheel.model.DistrictModel;
import com.jcl.android.extend.citywheel.model.ProvinceModel;

public class XmlParserHandler extends DefaultHandler {

	/**
	 * 存储所有的解析对象
	 */
	private List<ProvinceModel> provinceList = new ArrayList<ProvinceModel>();
	 	  
	public XmlParserHandler() {
		
	}

	public List<ProvinceModel> getDataList() {
		return provinceList;
	}

	@Override
	public void startDocument() throws SAXException {
		// 当读到第一个开始标签的时候，会触发这个方法
	}

	ProvinceModel provinceModel = new ProvinceModel();
	CityModel cityModel = new CityModel();
	DistrictModel districtModel = new DistrictModel();
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// 当遇到开始标记的时候，调用这个方法
		if (qName.equals("province")) {
			provinceModel = new ProvinceModel();
			provinceModel.setName(attributes.getValue("name"));
			provinceModel.setId(attributes.getValue("id"));
			provinceModel.setLatitude(attributes.getValue("latitude"));
			provinceModel.setLongitude(attributes.getValue("longitude"));
			provinceModel.setCityList(new ArrayList<CityModel>());
		} else if (qName.equals("city")) {
			cityModel = new CityModel();
			cityModel.setName(attributes.getValue("name"));
			cityModel.setId(attributes.getValue("id"));
			cityModel.setLatitude(attributes.getValue("latitude"));
			cityModel.setLongitude(attributes.getValue("longitude"));
			cityModel.setDistrictList(new ArrayList<DistrictModel>());
		} else if (qName.equals("district")) {
			districtModel = new DistrictModel();
			districtModel.setName(attributes.getValue("name"));
			districtModel.setId(attributes.getValue("id"));
			districtModel.setLatitude(attributes.getValue("latitude"));
			districtModel.setLongitude(attributes.getValue("longitude"));
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// 遇到结束标记的时候，会调用这个方法
		if (qName.equals("district")) {
			cityModel.getDistrictList().add(districtModel);
        } else if (qName.equals("city")) {
        	provinceModel.getCityList().add(cityModel);
        } else if (qName.equals("province")) {
        	provinceList.add(provinceModel);
        }
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
	}

}
