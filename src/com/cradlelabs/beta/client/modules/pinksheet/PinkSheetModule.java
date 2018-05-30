package com.cradlelabs.beta.client.modules.pinksheet;

import com.cradlelabs.beta.client.elements.AsideElement;
import com.cradlelabs.beta.client.modules.pinksheet.PinkSheetControlsWidget.PinkSheetControlsWidgetEventHandler;
import com.cradlelabs.beta.client.modules.pinksheet.PinkSheetView.PinkSheetViewEventHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class PinkSheetModule extends Composite {

	private PinkSheetView viewer;
	private PinkSheetControlsWidget controlsWidget;
	private static PinkSheetModuleUiBinder uiBinder = GWT
			.create(PinkSheetModuleUiBinder.class);

	interface PinkSheetModuleUiBinder extends UiBinder<Widget, PinkSheetModule> {
	}

	
	@UiField HTMLPanel contentsPanel;
	@UiField AsideElement controls;
	
	public PinkSheetModule() {
		initWidget(uiBinder.createAndBindUi(this));
		initComponents();
	}
	
	private void initComponents(){
		viewer = new PinkSheetView();
		
		//Load Controls
		controlsWidget = new PinkSheetControlsWidget();
		controlsWidget.setPinkSheetControlsWidgetEventHandler(new PinkSheetControlsWidgetEventHandler() {
			
			@Override
			public void onInvoke(int pollId, String year, String size) {
				controlsWidget.showLoading();
				viewer.reload(pollId, year, size);
			}
		});
		
		viewer.setPinkSheetViewEventHandler(new PinkSheetViewEventHandler() {
			
			@Override
			public void doneLoading() {
				controlsWidget.doneLoading();
			}
		});
		
		contentsPanel.add(viewer);
		controls.appendChild(controlsWidget.getElement());
	}

}
