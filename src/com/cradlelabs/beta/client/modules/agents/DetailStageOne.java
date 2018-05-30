package com.cradlelabs.beta.client.modules.agents;

import com.cradlelabs.beta.client.utils.GeneralEventHandler;
import com.cradlelabs.beta.client.utils.Utils;
import com.cradlelabs.beta.client.widgets.CustomCheckBox;
import com.cradlelabs.beta.client.widgets.CustomCheckBox.CustomCheckBoxEventHandler;
import com.cradlelabs.beta.shared.AgentModel;
import com.cradlelabs.beta.shared.BlobstoreModel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class DetailStageOne extends Composite implements IsWizardable<AgentModel>{

	private HasWizardEvent<AgentModel> wizardHandler;
	private AgentModel model;
	private boolean isDelete;

	private static StageOneUiBinder uiBinder = GWT
			.create(StageOneUiBinder.class);

	interface StageOneUiBinder extends UiBinder<Widget, DetailStageOne> {
	}

	@UiField Image img;
	@UiField TextBox nameField, emailField, msisdnField, pollingField, constituencyField, yearField;
	@UiField SimplePanel checkboxPanel;

	private CustomCheckBox deleteMember;


	public DetailStageOne(AgentModel model) {
		this.model = model;
		initWidget(uiBinder.createAndBindUi(this));
		initComponent(model);
		initEvent();
	}

	private void initComponent(AgentModel model){

		nameField.setEnabled(false);
		emailField.setEnabled(false);
		msisdnField.setEnabled(false);
		constituencyField.setEnabled(false);
		pollingField.setEnabled(false);
		yearField.setEnabled(false);

		if(model != null){
			nameField.setText(model.getName());
			emailField.setText(model.getEmail());
			msisdnField.setText(model.getMsisdn());
			pollingField.setText(model.getPollingStation());
			constituencyField.setText(model.getConstituency());
			yearField.setText(model.getYear());
			deleteMember = new CustomCheckBox("delete", model.getId()+"");
			deleteMember.setCustomCheckBoxEventHandler(new CustomCheckBoxEventHandler() {

				@Override
				public void onCheck(String id, String name, boolean isChecked) {
					if(isChecked){
						isDelete = true;
					}else{
						isDelete = false;
					}
				}
			});

			checkboxPanel.setWidget(deleteMember);

			if(model.getBlobKey() != null && !model.getBlobKey().trim().isEmpty()){
				Utils.retrieveFromBlobstore(model.getBlobKey(), new GeneralEventHandler<BlobstoreModel>() {

					@Override
					public void onSuccess(BlobstoreModel t) {
						img.setUrl(t.getUrl());
					}

					@Override
					public void onError() {
						// TODO Auto-generated method stub

					}
				});
			}

		}
	}

	private void initEvent(){
	}

	@Override
	public void validateAndProceed() {
		next();
	}

	private void doPrepareAgentModel(){

	}

	@Override
	public void setHasWizardEvent(HasWizardEvent<AgentModel> event) {
		this.wizardHandler = event;
	}

	@Override
	public void validateAndReturn() {
		// TODO Auto-generated method stub

	}

	@Override
	public void next() {
		doPrepareAgentModel();
		if(isDelete){
			wizardHandler.onValidateComplete(WizardStage.OTHER, model);		
		}else{
			wizardHandler.onValidateComplete(WizardStage.DONE, model);		
		}
	}

	@Override
	public void back() {
		// TODO Auto-generated method stub

	}
}
