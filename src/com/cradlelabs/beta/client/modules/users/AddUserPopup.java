package com.cradlelabs.beta.client.modules.users;

import com.cradlelabs.beta.client.GlobalResources;
import com.cradlelabs.beta.client.elements.StrongElement;
import com.cradlelabs.beta.client.modules.agents.HasWizardEvent;
import com.cradlelabs.beta.client.modules.agents.IsWizardable;
import com.cradlelabs.beta.client.modules.agents.WizardStage;
import com.cradlelabs.beta.shared.UserModel;
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

public class AddUserPopup extends Composite {

	private int stageIndex = 0;
	AddUserPopupEventHandler handler;
	private IsWizardable<UserModel> wizard;
	private static AddMemberPopupUiBinder uiBinder = GWT
			.create(AddMemberPopupUiBinder.class);

	public interface AddUserPopupEventHandler{
		void onClose();
	}

	interface AddMemberPopupUiBinder extends UiBinder<Widget, AddUserPopup> {
	}

	@UiField SimplePanel wizardPanel;
	@UiField Button previousBtn, nextBtn;
	@UiField StrongElement titleErrorMessage;
	@UiField DivElement errorDiv;
	@UiField AnchorElement closeAnchor;
	@UiField SpanElement titleSpan;

	public AddUserPopup() {
		initWidget(uiBinder.createAndBindUi(this));
		initComponent();
		initEvent();
	}

	public AddUserPopup(UserModel model){
		initWidget(uiBinder.createAndBindUi(this));
		initComponent(model);
		initEvent();
	}

	private void initComponent(UserModel model){
		titleSpan.setInnerText("Add User");
		wizard = new StageOne(model);
		initWizardableEvent(wizard);

		wizardPanel.setWidget(wizard);
	}

	private void initComponent(){
		titleSpan.setInnerText("Add User");
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

	private void initWizardableEvent(final IsWizardable<UserModel> tmpWizard){
		tmpWizard.setHasWizardEvent(new HasWizardEvent<UserModel>() {

			@Override
			public void onValidateComplete(WizardStage stage, UserModel model) {

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
		titleErrorMessage.setInnerText("User Added Successfully");
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

	private void doSaveAgent(UserModel model){
		disableButton(true);

		GlobalResources.getInstance().getAddRPC().addUser(model, new AsyncCallback<Integer>() {

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

	public void setAddUserPopupEventHandler(AddUserPopupEventHandler handler){
		this.handler = handler;
	}

}
