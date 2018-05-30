package com.cradlelabs.beta.client;

import com.cradlelabs.beta.shared.AgentModel;
import com.cradlelabs.beta.shared.UserModel;

public class GlobalResources {
	private ListServiceAsync listRPC;
	private AddServiceAsync addRPC;
	private UpdateServiceAsync updateRPC;
	
	private UserModel userModel;
	private AgentModel agentModel;
	
	private static GlobalResources instance = new GlobalResources();
	
	private GlobalResources(){}
	
	public static GlobalResources getInstance(){
		return instance;
	}

	public ListServiceAsync getListRPC() {
		return listRPC;
	}

	public void setListRPC(ListServiceAsync listRPC) {
		this.listRPC = listRPC;
	}

	public AddServiceAsync getAddRPC() {
		return addRPC;
	}

	public void setAddRPC(AddServiceAsync addRPC) {
		this.addRPC = addRPC;
	}

	public UpdateServiceAsync getUpdateRPC() {
		return updateRPC;
	}

	public void setUpdateRPC(UpdateServiceAsync updateRPC) {
		this.updateRPC = updateRPC;
	}

	public UserModel getUserModel() {
		return userModel;
	}

	public void setUserModel(UserModel userModel) {
		this.userModel = userModel;
	}

	public AgentModel getAgentModel() {
		return agentModel;
	}

	public void setAgentModel(AgentModel agentModel) {
		this.agentModel = agentModel;
	}
	
}
