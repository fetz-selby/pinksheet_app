package com.cradlelabs.beta.client.composites;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.UIObject;

public class MobileSidebarElement extends UIObject {

	private static SidebarElementUiBinder uiBinder = GWT
			.create(SidebarElementUiBinder.class);

	interface SidebarElementUiBinder extends UiBinder<Element, MobileSidebarElement> {
	}

	
	public MobileSidebarElement() {
		setElement(uiBinder.createAndBindUi(this));
	//	initComponents();
	}

}
