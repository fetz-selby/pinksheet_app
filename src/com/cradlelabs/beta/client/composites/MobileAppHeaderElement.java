package com.cradlelabs.beta.client.composites;

import com.cradlelabs.beta.client.GlobalResources;
import com.cradlelabs.beta.client.utils.GeneralEventHandler;
import com.cradlelabs.beta.client.utils.Utils;
import com.cradlelabs.beta.shared.AgentModel;
import com.cradlelabs.beta.shared.BlobstoreModel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.UIObject;

public class MobileAppHeaderElement extends UIObject {

	private static AppHeaderElementUiBinder uiBinder = GWT
			.create(AppHeaderElementUiBinder.class);

	interface AppHeaderElementUiBinder extends
			UiBinder<Element, MobileAppHeaderElement> {
	}

	@UiField SpanElement userName;
	@UiField ImageElement userAvatar;
	
	public MobileAppHeaderElement() {
		setElement(uiBinder.createAndBindUi(this));
		initComponents();
	}
	
	private void initComponents(){
		AgentModel model = GlobalResources.getInstance().getAgentModel();
		userName.setInnerText(GlobalResources.getInstance().getAgentModel().getName());
		DOM.getElementById("s_n").setInnerText(GlobalResources.getInstance().getAgentModel().getName());
		
		Utils.retrieveFromBlobstore(model.getBlobKey(), new GeneralEventHandler<BlobstoreModel>() {
			
			@Override
			public void onSuccess(BlobstoreModel t) {
				userAvatar.setSrc(t.getUrl());
				DOM.getElementById("s_i").setAttribute("src", t.getUrl());
			}
			
			@Override
			public void onError() {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		
	}

}
