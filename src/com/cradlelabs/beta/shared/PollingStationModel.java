package com.cradlelabs.beta.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PollingStationModel implements IsSerializable{
	private int id, constituenyId;
	private String name, consName;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getConstituenyId() {
		return constituenyId;
	}
	public void setConstituenyId(int constituenyId) {
		this.constituenyId = constituenyId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getConsName() {
		return consName;
	}
	public void setConsName(String consName) {
		this.consName = consName;
	}
	
	
}
