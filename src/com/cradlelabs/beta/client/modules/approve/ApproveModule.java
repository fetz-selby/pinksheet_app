package com.cradlelabs.beta.client.modules.approve;

import com.cradlelabs.beta.client.elements.AsideElement;
import com.cradlelabs.beta.client.modules.approve.ApproveControlsWidget.ApproveControlsWidgetEventHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class ApproveModule extends Composite {

	private static PinkSheetModuleUiBinder uiBinder = GWT
			.create(PinkSheetModuleUiBinder.class);

	interface PinkSheetModuleUiBinder extends UiBinder<Widget, ApproveModule> {
	}

	
	@UiField HTMLPanel contentsPanel;
	@UiField AsideElement controls;
	
	public ApproveModule() {
		initWidget(uiBinder.createAndBindUi(this));
		initComponents();
	}
	
	private void initComponents(){
		//Load Controls
		ApproveControlsWidget controlsWidget = new ApproveControlsWidget();
		controlsWidget.setApproveControlsWidgetEventHandler(new ApproveControlsWidgetEventHandler() {
			
			@Override
			public void onInvoke(int constituencyId, String type) {
				// TODO Auto-generated method stub
				
			}
		});
		
		controls.appendChild(controlsWidget.getElement());
	}

}
