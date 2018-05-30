package com.cradlelabs.beta.client.composites;

import com.cradlelabs.beta.client.GlobalResources;
import com.cradlelabs.beta.shared.UserModel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.UIObject;

public class SidebarElement extends UIObject {

	private static SidebarElementUiBinder uiBinder = GWT
			.create(SidebarElementUiBinder.class);

	interface SidebarElementUiBinder extends UiBinder<Element, SidebarElement> {
	}

	@UiField LIElement users, agents, approve;
	
	public SidebarElement() {
		setElement(uiBinder.createAndBindUi(this));
		initComponents();
	}
	
	private void initComponents(){
		UserModel model = GlobalResources.getInstance().getUserModel();
		if(model.getPerm().equalsIgnoreCase("A")){
			loadAgents();
			loadUsers();
			//loadApprove();
		}
	}
	
	private void loadUsers(){
		users.appendChild(getUserElement());
	}
	
	private void loadAgents(){
		agents.appendChild(getAgentsElement());
	}
	
	private void loadApprove(){
		approve.appendChild(getApproveElement());
	}
	
	private Element getUserElement(){

		Element a = DOM.createElement("a");
		a.setClassName("auto");
		a.setAttribute("href", "#users");

		Element i = DOM.createElement("i");
		i.setClassName("fa fa-users icon");

		Element span = DOM.createElement("span");
		span.setClassName("font-bold");
		span.setInnerText("Users");

		a.appendChild(i);
		a.appendChild(span);

		return a;
	}
	
	private Element getApproveElement(){

		Element a = DOM.createElement("a");
		a.setClassName("auto");
		a.setAttribute("href", "#approve");

		Element i = DOM.createElement("i");
		i.setClassName("fa fa-check icon");

		Element span = DOM.createElement("span");
		span.setClassName("font-bold");
		span.setInnerText("Approve");

		a.appendChild(i);
		a.appendChild(span);

		return a;
	}
	
	private Element getAgentsElement(){

		Element a = DOM.createElement("a");
		a.setClassName("auto");
		a.setAttribute("href", "#agents");

		Element i = DOM.createElement("i");
		i.setClassName("fa fa-eye icon");

		Element span = DOM.createElement("span");
		span.setClassName("font-bold");
		span.setInnerText("Agents");

		a.appendChild(i);
		a.appendChild(span);

		return a;
	}

}
