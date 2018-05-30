package com.cradlelabs.beta.client;

import com.cradlelabs.beta.client.controller.PinkSheetAgentAppController;
import com.cradlelabs.beta.client.controller.PinkSheetLoginAppController;
import com.cradlelabs.beta.client.controller.PinkSheetUserAppController;
import com.cradlelabs.beta.client.utils.CookieVerifier;
import com.cradlelabs.beta.shared.AgentModel;
import com.cradlelabs.beta.shared.PSUser;
import com.cradlelabs.beta.shared.UserModel;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class DigitalPinkSheet implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		
		//Hard User Setting
		//CookieVerifier.addCookie(getDummyUser());

		ListServiceAsync listRPC = GWT.create(ListService.class);
		AddServiceAsync addRPC = GWT.create(AddService.class);
		UpdateServiceAsync updateRPC = GWT.create(UpdateService.class);
		
		GlobalResources.getInstance().setListRPC(listRPC);
		GlobalResources.getInstance().setAddRPC(addRPC);
		GlobalResources.getInstance().setUpdateRPC(updateRPC);
		
		if(CookieVerifier.isAppCookieExist()){
			initGlobals();
		}else{
			//Send to login
			PinkSheetLoginAppController loginController = new PinkSheetLoginAppController();
			loginController.load();
		}
		
	}
	
	private void initGlobals(){
		String msisdn = CookieVerifier.getMsisdn();
		
		if(CookieVerifier.isUser()){
			GlobalResources.getInstance().getListRPC().getUser(msisdn, new AsyncCallback<UserModel>() {
				
				@Override
				public void onSuccess(UserModel result) {
					GlobalResources.getInstance().setUserModel(result);
					PinkSheetUserAppController userApp = new PinkSheetUserAppController();
					userApp.load();
				}
				
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}
			});
		}else if(CookieVerifier.isAgent()){
			GlobalResources.getInstance().getListRPC().getAgent(msisdn, new AsyncCallback<AgentModel>() {
				
				@Override
				public void onSuccess(AgentModel result) {
					GlobalResources.getInstance().setAgentModel(result);
					PinkSheetAgentAppController agentApp = new PinkSheetAgentAppController();
					agentApp.load();
				}
				
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}
			});
		}
	}
	
	private PSUser getDummyUser(){
		
		PSUser model = new PSUser();
		model.setEmail("annan@gmail.com");
		model.setId(1);
		model.setMsisdn("+233244000000");
		model.setType("U");
		
		return model;
	}

}
