package com.cradlelabs.beta.client.modules.agents;

import com.cradlelabs.beta.client.GlobalResources;
import com.cradlelabs.beta.client.elements.StrongElement;
import com.cradlelabs.beta.shared.AgentModel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class AddAgentPopup extends Composite {

	private int stageIndex = 0;
	AddAgentPopupEventHandler handler;
	private IsWizardable<AgentModel> wizard;
	private static AddMemberPopupUiBinder uiBinder = GWT
			.create(AddMemberPopupUiBinder.class);

	public interface AddAgentPopupEventHandler{
		void onClose();
	}

	interface AddMemberPopupUiBinder extends UiBinder<Widget, AddAgentPopup> {
	}

	@UiField SimplePanel wizardPanel;
	@UiField Button previousBtn, nextBtn;
	@UiField StrongElement titleErrorMessage;
	@UiField DivElement errorDiv;
	@UiField AnchorElement closeAnchor;
	@UiField SpanElement titleSpan;

	public AddAgentPopup() {
		initWidget(uiBinder.createAndBindUi(this));
		initComponent();
		initEvent();
	}

	public AddAgentPopup(AgentModel model){
		initWidget(uiBinder.createAndBindUi(this));
		initComponent(model);
		initEvent();
	}

	private void initComponent(AgentModel model){
		titleSpan.setInnerText("Add Agent");
		wizard = new StageOne(model);
		initWizardableEvent(wizard);

		wizardPanel.setWidget(wizard);
	}

	private void initComponent(){
		titleSpan.setInnerText("Add Agent");
		wizard = new StageOne();
		initWizardableEvent(wizard);

		wizardPanel.setWidget(wizard);
	}

	private void showError(boolean isShow, String message){
		if(isShow){
			errorDiv.removeClassName("hide");
			titleErrorMessage.setInnerText(message);
		}else{
			errorDiv.addClassName("hide");
		}

	}

	private void showPrevious(boolean isShow){
		if(isShow){
			previousBtn.removeStyleName("hide");
		}else{
			previousBtn.addStyleName("hide");
		}
	}

	private void initWizardableEvent(final IsWizardable<AgentModel> tmpWizard){
		tmpWizard.setHasWizardEvent(new HasWizardEvent<AgentModel>() {

			@Override
			public void onValidateComplete(WizardStage stage, AgentModel model) {

				showError(false, "");

				switch(stage){
				case ONE:
					wizard = new StageOne(model);
					stageIndex = 0;
					break;
				case DONE:
					doSaveAgent(model);
					break;
				default:
					break;

				}

				if(stageIndex > 0){
					showPrevious(true);
				}else{
					showPrevious(false);
				}

				if(stageIndex == 0){
					nextBtn.setText("Done");
				}

				wizardPanel.setWidget(wizard);

				initWizardableEvent(wizard);
			}

			@Override
			public void onError(String message) {
				showError(true, message);
			}
		});
	}

	private void initEvent(){
		previousBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				stageIndex --;

				if(stageIndex == 0){
					showPrevious(false);
				}

				wizard.validateAndReturn();
			}
		});

		nextBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				wizard.validateAndProceed();
			}
		});
		
		DOM.sinkEvents(closeAnchor, Event.ONCLICK);
		DOM.setEventListener(closeAnchor, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if(handler != null){
					handler.onClose();
				}
			}
		});
	}

	private void showAddSuccess(){
		errorDiv.setClassName("alert alert-success");
		titleErrorMessage.setInnerText("Agent Added Successfully");
	}

	private void showAddSuccessAndHide(){
		showAddSuccess();
		Timer timer = new Timer() {

			@Override
			public void run() {
				if(handler != null){
					handler.onClose();
				}
			}
		};

		//Two seconds
		timer.schedule(2*1000);
	}
	
	private void showAndHide(String message){
		showError(true, message);
		Timer timer = new Timer() {

			@Override
			public void run() {
				if(handler != null){
					handler.onClose();
				}
			}
		};

		//Two seconds
		timer.schedule(2*1000);
	}
	
	private void showLoading(){
		errorDiv.setClassName("alert alert-success");
		titleErrorMessage.setInnerText("Loading, please wait ...");
	}
	
	private void hideMessage(){
		errorDiv.setClassName("hide");
	}
	
	private void disableButton(boolean isDisabled){
		if(isDisabled){
			showLoading();
			nextBtn.setEnabled(false);
		}else{
			hideMessage();
			nextBtn.setEnabled(true);
		}
	}

	private void doSaveAgent(AgentModel model){
		disableButton(true);

		GlobalResources.getInstance().getAddRPC().addAgent(model, new AsyncCallback<Integer>() {

			@Override
			public void onSuccess(Integer result) {
				if(result > 0){
					showAddSuccessAndHide();
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				showAndHide("Bad network connection. Please try again later.");
			}
		});
	}

	public void setAddAgentPopupEventHandler(AddAgentPopupEventHandler handler){
		this.handler = handler;
	}

}
