package com.cradlelabs.beta.client.controller;

import com.cradlelabs.beta.client.composites.MobileAppHeaderElement;
import com.cradlelabs.beta.client.composites.MobileSidebarElement;
import com.cradlelabs.beta.client.mobile.upload.MobileUploadForm;
import com.cradlelabs.beta.client.modules.overview.MobileOverviewModule;
import com.cradlelabs.beta.client.utils.CookieVerifier;
import com.cradlelabs.beta.client.utils.PageDirector;
import com.cradlelabs.beta.shared.AppConstants;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class PinkSheetAgentAppController implements ValueChangeHandler<String>{

	private RootPanel contentPanel;


	public PinkSheetAgentAppController(){
		History.addValueChangeHandler(this);
	}

	private Element getSidebar(){
		MobileSidebarElement sidebar = new MobileSidebarElement();
		return sidebar.getElement();
	}

	private Element getAppHeader(){
		MobileAppHeaderElement appHeader = new MobileAppHeaderElement();
		return appHeader.getElement();
	}

	private void doElementGrep(){
		Element sidebar = DOM.getElementById("nav_sidebar");
		sidebar.appendChild(getSidebar());

		Element header = DOM.getElementById("app_header");
		header.appendChild(getAppHeader());

		contentPanel = RootPanel.get("content");
	}

	private void goToLogin(){
		CookieVerifier.clearCookie();
		PageDirector.getInstance().directTo("index.html", "logout");
	}

	private void setContent(Widget widget){
		contentPanel.clear();
		contentPanel.add(widget);
	}


	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		String token = event.getValue().trim();
		
		if(!CookieVerifier.isAgent()){
			goToLogin();
			return;
		}

		if(token.equalsIgnoreCase(AppConstants.LOGOUT)){
			goToLogin();
		}else if(token.equalsIgnoreCase(AppConstants.UPLOAD)){
			setContent(new MobileUploadForm());
		}else if(token.equalsIgnoreCase(AppConstants.OVERVIEW)){
			setContent(new MobileOverviewModule());
		}
	}

	public void load(){
		doElementGrep();
		
		if(History.getToken() != null && !History.getToken().isEmpty()){
			History.fireCurrentHistoryState();
		}else{
			History.newItem(AppConstants.UPLOAD);
		}
	}
}
