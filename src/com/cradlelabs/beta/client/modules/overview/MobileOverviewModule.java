package com.cradlelabs.beta.client.modules.overview;

import com.cradlelabs.beta.client.GlobalResources;
import com.cradlelabs.beta.shared.AgentModel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class MobileOverviewModule extends Composite {

	private static OverviewModuleUiBinder uiBinder = GWT
			.create(OverviewModuleUiBinder.class);

	interface OverviewModuleUiBinder extends UiBinder<Widget, MobileOverviewModule> {
	}

	@UiField SpanElement constituencySpan, pinkSheetSpan;
	
	public MobileOverviewModule() {
		initWidget(uiBinder.createAndBindUi(this));
		initComponents();
	}
	
	private void initComponents(){
		AgentModel model = GlobalResources.getInstance().getAgentModel();
		
		constituencySpan.setInnerText(model.getConstituency());
		
		GlobalResources.getInstance().getListRPC().pinkSheetsCount(model.getPollingStationId(), new AsyncCallback<String>() {
			
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
