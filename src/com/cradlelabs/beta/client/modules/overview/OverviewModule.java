package com.cradlelabs.beta.client.modules.overview;

import com.cradlelabs.beta.client.GlobalResources;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class OverviewModule extends Composite {

	private static OverviewModuleUiBinder uiBinder = GWT
			.create(OverviewModuleUiBinder.class);

	interface OverviewModuleUiBinder extends UiBinder<Widget, OverviewModule> {
	}

	@UiField SpanElement agentsSpan, usersSpan, pinkSheetSpan;
	
	public OverviewModule() {
		initWidget(uiBinder.createAndBindUi(this));
		initComponents();
	}
	
	private void initComponents(){
		GlobalResources.getInstance().getListRPC().agentsCount(new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				agentsSpan.setInnerText(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Bad internet connection. Please try again later.");
			}
		});
		
		GlobalResources.getInstance().getListRPC().usersCount(new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				usersSpan.setInnerText(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Bad internet connection. Please try again later.");
				
			}
		});
		
		GlobalResources.getInstance().getListRPC().pinkSheetsCount(new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				pinkSheetSpan.setInnerText(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Bad internet connection. Please try again later.");
			}
		});
	}

}
