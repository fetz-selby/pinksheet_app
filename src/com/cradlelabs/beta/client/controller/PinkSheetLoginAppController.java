package com.cradlelabs.beta.client.controller;

import com.cradlelabs.beta.client.GlobalResources;
import com.cradlelabs.beta.client.utils.CookieVerifier;
import com.cradlelabs.beta.client.utils.PageDirector;
import com.cradlelabs.beta.client.utils.Utils;
import com.cradlelabs.beta.shared.AppConstants;
import com.cradlelabs.beta.shared.PSUser;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;

public class PinkSheetLoginAppController implements ValueChangeHandler<String>{
	private boolean isBusy;
	private Element signInElement;
	
	public PinkSheetLoginAppController() {
		History.addValueChangeHandler(this);
	}

	private void bind(){
		History.newItem(AppConstants.LOGOUT);
	}
	
	private void initComponent(){
		
		final Element usernameBox = DOM.getElementById("username");
		final Element passwordBox = DOM.getElementById("password");
		
		signInElement = DOM.getElementById("signin");
		
		DOM.sinkEvents(signInElement, Event.ONCLICK);
		DOM.setEventListener(signInElement, new EventListener() {
			
			@Override
			public void onBrowserEvent(Event event) {
				if(!isBusy){
					doValidation(usernameBox.getPropertyString("value").trim(), passwordBox.getPropertyString("value").trim());
				}
			}
		});
		
	}
	
	private void doValidation(String username, String password){
		if((Utils.isMsisdnValid(username) && password.length() >= 5) || (Utils.isEmailValidFormat(username) && password.length() >= 5)){
			requestLogin(username, password);
		}else {
			return;
		}
	}
	
	private void showLoading(boolean isShow){
		if(isShow){
			signInElement.setInnerText("Signing in ...");
		}else{
			signInElement.setInnerText("Sign in");
		}
	}
	
	private void requestLogin(String username, String password){
		isBusy = true;
		showLoading(true);
		GlobalResources.getInstance().getListRPC().requestLogin(username, password, new AsyncCallback<PSUser>() {
			
			@Override
			public void onSuccess(PSUser result) {
				if(result != null){
					CookieVerifier.addCookie(result);
					if(result.getType().equalsIgnoreCase("A")){
						PageDirector.getInstance().directTo("mobile.html", "");
					}else if(result.getType().equalsIgnoreCase("U")){
						PageDirector.getInstance().directTo("app.html", "");
					}
				}
				isBusy = false;
				showLoading(false);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				isBusy = false;
				showLoading(false);
			}
		});
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		// TODO Auto-generated method stub
		
	}
	
	public void load(){
		bind();
		initComponent();
	}

}
