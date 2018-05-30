package com.cradlelabs.beta.client.modules.agents;

import com.cradlelabs.beta.client.elements.AsideElement;
import com.cradlelabs.beta.client.modules.agents.AddAgentPopup.AddAgentPopupEventHandler;
import com.cradlelabs.beta.client.modules.agents.AgentControlsWidget.AgentControlsWidgetEventHandler;
import com.cradlelabs.beta.client.modules.agents.AgentListWidget.AgentEditEventHandler;
import com.cradlelabs.beta.client.modules.agents.DetailAgentPopup.DetailAgentPopupEventHandler;
import com.cradlelabs.beta.client.widgets.CustomDraggablePopupPanel;
import com.cradlelabs.beta.shared.AgentModel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class AgentModule extends Composite {

	private AgentListWidget listWidget;
	private static AgentModuleUiBinder uiBinder = GWT
			.create(AgentModuleUiBinder.class);

	interface AgentModuleUiBinder extends UiBinder<Widget, AgentModule> {
	}

	@UiField HTMLPanel contentsPanel;
	@UiField AsideElement controls;

	public AgentModule() {
		initWidget(uiBinder.createAndBindUi(this));
		initComponents();
	}

	private void initComponents(){
		listWidget = new AgentListWidget();

		//Load Controls
		final AgentControlsWidget controlsWidget = new AgentControlsWidget();
		controlsWidget.setAgentControlsWidgetEventHandler(new AgentControlsWidgetEventHandler() {

			@Override
			public void onInvoke(String year, int constituencyId, int pollId) {
				listWidget.reload(pollId, constituencyId, year);
			}

			@Override
			public void onAddInvoked() {
				showAgentPopup();
			}
		});

		listWidget.setAgentEditEventHandler(new AgentEditEventHandler() {
			
			@Override
			public void onLoadComplete() {
				controlsWidget.doneLoading();
			}
			
			@Override
			public void onAgentEditInvoked(AgentModel model) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onDetailShow(AgentModel model) {
				showAgentDetail(model);
			}
		});
		
		contentsPanel.add(listWidget);
		controls.appendChild(controlsWidget.getElement());
	}
	
	private CustomDraggablePopupPanel getPopup(){
		CustomDraggablePopupPanel popup = new CustomDraggablePopupPanel();
		popup.setAutoHideEnabled(false);
		popup.setGlassEnabled(true);
		popup.setGlassStyleName("glassPanel");
		popup.setDraggable(true);

		return popup;
	}
	
	private void showAgentPopup(){
		final CustomDraggablePopupPanel popup = getPopup();
		AddAgentPopup agentPopup = new AddAgentPopup();
		agentPopup.setAddAgentPopupEventHandler(new AddAgentPopupEventHandler() {

			@Override
			public void onClose() {
				popup.hide();
			}
		});
		popup.add(agentPopup);
		popup.center();
	}
	
	private void showAgentDetail(AgentModel model){
		final CustomDraggablePopupPanel popup = getPopup();
		DetailAgentPopup agentPopup = new DetailAgentPopup(model);
		agentPopup.setDetailAgentPopupEventHandler(new DetailAgentPopupEventHandler() {

			@Override
			public void onClose() {
				popup.hide();
			}

			@Override
			public void removeFromList(int id) {
				listWidget.removeFromList(id);
				popup.hide();
			}
		});
		popup.add(agentPopup);
		popup.center();
	}

}
