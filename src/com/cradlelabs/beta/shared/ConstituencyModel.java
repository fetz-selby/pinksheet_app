package com.cradlelabs.beta.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ConstituencyModel implements IsSerializable{
	private int id, regionId;
	private String name;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRegionId() {
		return regionId;
	}
	public void setRegionId(int regionId) {
		this.regionId = regionId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
