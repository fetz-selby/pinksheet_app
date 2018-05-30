package com.cradlelabs.beta.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PinkSheetModel implements IsSerializable{
	private int id,agentId, constituencyId, pollId;
	private String agentName, blobKey, status, date, msisdn, email, constituency, pollingStation, year;
	public int getAgentId() {
		return agentId;
	}
	public void setAgentId(int agentId) {
		this.agentId = agentId;
	}
	public int getConstituencyId() {
		return constituencyId;
	}
	public void setConstituencyId(int constituencyId) {
		this.constituencyId = constituencyId;
	}
	public int getPollId() {
		return pollId;
	}
	public void setPollId(int pollId) {
		this.pollId = pollId;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getBlobKey() {
		return blobKey;
	}
	public void setBlobKey(String blobKey) {
		this.blobKey = blobKey;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getConstituency() {
		return constituency;
	}
	public void setConstituency(String constituency) {
		this.constituency = constituency;
	}
	public String getPollingStation() {
		return pollingStation;
	}
	public void setPollingStation(String pollingStation) {
		this.pollingStation = pollingStation;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + agentId;
		result = prime * result + ((blobKey == null) ? 0 : blobKey.hashCode());
		result = prime * result + id;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PinkSheetModel other = (PinkSheetModel) obj;
		if (agentId != other.agentId)
			return false;
		if (blobKey == null) {
			if (other.blobKey != null)
				return false;
		} else if (!blobKey.equals(other.blobKey))
			return false;
		if (id != other.id)
			return false;
		return true;
	}
	
}
