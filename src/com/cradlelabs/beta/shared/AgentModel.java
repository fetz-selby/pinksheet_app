package com.cradlelabs.beta.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AgentModel implements IsSerializable{
	private int id, constituencyId, pollingStationId;
	private String name, blobKey, msisdn = "", year,email = "",pollingStation, constituency;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getConstituencyId() {
		return constituencyId;
	}
	public void setConstituencyId(int constituencyId) {
		this.constituencyId = constituencyId;
	}
	public int getPollingStationId() {
		return pollingStationId;
	}
	public void setPollingStationId(int pollingStationId) {
		this.pollingStationId = pollingStationId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBlobKey() {
		return blobKey;
	}
	public void setBlobKey(String blobKey) {
		this.blobKey = blobKey;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPollingStation() {
		return pollingStation;
	}
	public void setPollingStation(String pollingStation) {
		this.pollingStation = pollingStation;
	}
	public String getConstituency() {
		return constituency;
	}
	public void setConstituency(String constituency) {
		this.constituency = constituency;
	}
	
}
