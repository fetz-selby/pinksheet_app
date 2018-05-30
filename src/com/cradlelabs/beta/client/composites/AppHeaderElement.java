package com.cradlelabs.beta.client.composites;

import com.cradlelabs.beta.client.GlobalResources;
import com.cradlelabs.beta.client.utils.GeneralEventHandler;
import com.cradlelabs.beta.client.utils.Utils;
import com.cradlelabs.beta.shared.BlobstoreModel;
import com.cradlelabs.beta.shared.UserModel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.UIObject;

public class AppHeaderElement extends UIObject {

	private static AppHeaderElementUiBinder uiBinder = GWT
			.create(AppHeaderElementUiBinder.class);

	interface AppHeaderElementUiBinder extends
			UiBinder<Element, AppHeaderElement> {
	}

	@UiField SpanElement userName;
	@UiField ImageElement userAvatar;
	
	public AppHeaderElement() {
		setElement(uiBinder.createAndBindUi(this));
		initComponents();
	}
	
	private void initComponents(){
		UserModel model = GlobalResources.getInstance().getUserModel();
		userName.setInnerText(GlobalResources.getInstance().getUserModel().getName());
		DOM.getElementById("s_n").setInnerText(GlobalResources.getInstance().getUserModel().getName());
		
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
