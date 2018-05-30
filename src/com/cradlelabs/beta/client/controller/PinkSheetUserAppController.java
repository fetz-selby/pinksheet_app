package com.cradlelabs.beta.client.controller;

import com.cradlelabs.beta.client.composites.AppHeaderElement;
import com.cradlelabs.beta.client.composites.SidebarElement;
import com.cradlelabs.beta.client.modules.agents.AgentModule;
import com.cradlelabs.beta.client.modules.approve.ApproveModule;
import com.cradlelabs.beta.client.modules.overview.OverviewModule;
import com.cradlelabs.beta.client.modules.pinksheet.PinkSheetModule;
import com.cradlelabs.beta.client.modules.users.UsersModule;
import com.cradlelabs.beta.client.utils.CookieVerifier;
import com.cradlelabs.beta.client.utils.PageDirector;
import com.cradlelabs.beta.shared.AppConstants;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class PinkSheetUserAppController implements ValueChangeHandler<String> {
	private RootPanel contentPanel;
	
	public PinkSheetUserAppController(){
		History.addValueChangeHandler(this);
	}

	private Element getSidebar(){
		SidebarElement sidebar = new SidebarElement();
		return sidebar.getElement();
	}
	
	private Element getAppHeader(){
		AppHeaderElement appHeader = new AppHeaderElement();
		return appHeader.getElement();
	}
	
	private void doElementGrep(){
		Element sidebar = DOM.getElementById("nav_sidebar");
		sidebar.appendChild(getSidebar());
		
		Element header = DOM.getElementById("app_header");
		header.appendChild(getAppHeader());
		
		contentPanel = RootPanel.get("content");
	}
	
	public void load(){
		doElementGrep();
		
		if(History.getToken() != null && !History.getToken().isEmpty()){
			History.fireCurrentHistoryState();
		}else{
			History.newItem(AppConstants.PINK_SHEETS);
		}
	}
	
	private void goToLogin(){
		CookieVerifier.clearCookie();
		PageDirector.getInstance().directTo("index.html", "logout");
	}
	
	private void setContent(Widget widget){
		//contentPanel.setInnerText("");
		//contentPanel.appendChild(widget.getElement());
		
		contentPanel.clear();
		contentPanel.add(widget);
	}
	
	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		String token = event.getValue().trim();
		if(!CookieVerifier.isUser()){
			goToLogin();
			return;
		}
		
		if(token.equalsIgnoreCase(AppConstants.PINK_SHEETS)){
			setContent(new PinkSheetModule());
		}else if(token.equalsIgnoreCase(AppConstants.AGENTS)){
			setContent(new AgentModule());
		}else if(token.equalsIgnoreCase(AppConstants.USERS)){
			setContent(new UsersModule());
		}else if(token.equalsIgnoreCase(AppConstants.APPROVE)){
			setContent(new ApproveModule());
		}else if(token.equalsIgnoreCase(AppConstants.OVERVIEW)){
			setContent(new OverviewModule());
		}else if(token.equalsIgnoreCase(AppConstants.LOGOUT)){
			goToLogin();
		}
	}

}
