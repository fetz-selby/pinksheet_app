package com.cradlelabs.beta.client.test;

import com.cradlelabs.beta.client.GlobalResources;
import com.cradlelabs.beta.client.modules.agents.HasWizardEvent;
import com.cradlelabs.beta.client.modules.agents.IsWizardable;
import com.cradlelabs.beta.client.modules.agents.WizardStage;
import com.cradlelabs.beta.client.utils.GeneralEventHandler;
import com.cradlelabs.beta.client.utils.Utils;
import com.cradlelabs.beta.client.widgets.CustomFileUpload;
import com.cradlelabs.beta.client.widgets.CustomFileUpload.CustomFileUploadEventHandler;
import com.cradlelabs.beta.shared.BlobstoreModel;
import com.cradlelabs.beta.shared.UserModel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class EditStageOne extends Composite implements IsWizardable<UserModel>{

	private HasWizardEvent<UserModel> wizardHandler;
	private UserModel model;

	private static StageOneUiBinder uiBinder = GWT
			.create(StageOneUiBinder.class);

	interface StageOneUiBinder extends UiBinder<Widget, EditStageOne> {
	}

	@UiField Image img;
	@UiField CustomFileUpload uploadWidget;
	@UiField TextBox nameField, emailField, msisdnField;
	@UiField ListBox typeBox;

	
	public EditStageOne(UserModel model) {
		this.model = model;
		initWidget(uiBinder.createAndBindUi(this));
		initComponent(model);
		loadTypes();
		initEvent();
	}

	public EditStageOne() {
		model = null;
		initWidget(uiBinder.createAndBindUi(this));
		initComponent(null);
		loadTypes();
		initEvent();
	}

	private void initComponent(UserModel model){
		img.setUrl("images/no-image.jpg");

		nameField.getElement().setAttribute("placeholder", "Full Name");
		emailField.getElement().setAttribute("placeholder", "Email");
		msisdnField.getElement().setAttribute("placeholder", "Mobile Number");

		if(model != null){
			nameField.setText(model.getName());
			emailField.setText(model.getEmail());
			msisdnField.setText(model.getMsisdn());
			//img.setUrl(model.getAvatar());

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
	
	private void loadTypes(){
		typeBox.addItem("Select Type", "0");
		typeBox.addItem("Administrators", "A");
		typeBox.addItem("Users", "U");
	}
	
	private void initEvent(){
		uploadWidget.setCustomFileUploadEventHandler(new CustomFileUploadEventHandler() {

			@Override
			public void onFormSubmit(final String avatarString, final String blobKey) {
				//Delete old blobKey
				if(model == null){
					model = new UserModel();
				}

				if(model.getBlobKey() != null && !model.getBlobKey().trim().isEmpty()){
					GlobalResources.getInstance().getUpdateRPC().deleteOrphanBlob(model.getBlobKey(), new AsyncCallback<Void>() {

						@Override
						public void onSuccess(Void result) {
							img.setUrl(avatarString);
							model.setBlobKey(blobKey);	
							
							//Window.alert("[inner] url => "+avatarString+"\nblobKey => "+blobKey);

						}

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub

						}
					});
				}else{
					//Window.alert("url => "+avatarString+"\nblobKey => "+blobKey);
					
					img.setUrl(avatarString);
					model.setBlobKey(blobKey);
				}
			}
		});
	}

	private void doPublishError(String message){
		if(wizardHandler != null){
			wizardHandler.onError(message);
		}
	}

	@Override
	public void validateAndProceed() {

		if(isAvatarEmpty()){
			doPublishError("Please select a picture");
			return;
		}

		if(nameField.getText().trim().isEmpty()){
			doPublishError("Full Name cannot be empty!");
			return;
		}

		if(msisdnField.getText().trim().isEmpty()){
			doPublishError("Mobile cannot be empty!");
			return;
		}
		
		if(typeBox.getValue(typeBox.getSelectedIndex()).equalsIgnoreCase("0")){
			doPublishError("Type cannot be empty!");
			return;
		}
		
		if(!(Utils.isMsisdnValid(msisdnField.getText()) && msisdnField.getText().trim().contains("+"))){
			doPublishError("Invalid mobile number!");
			return;
		}
		
		next();
	}

	private boolean isAvatarEmpty(){

		if(img.getUrl().contains("images/no-image.jpg")){
			return true;
		}
		return false;
	}

	private void doPrepareUsertModel(){
		if(model == null){
			model = new UserModel();
		}

		model.setName(nameField.getText().trim());
		model.setMsisdn(msisdnField.getText().trim());
		model.setEmail(emailField.getText().trim());
		model.setPerm(typeBox.getValue(typeBox.getSelectedIndex()));
	}

	@Override
	public void setHasWizardEvent(HasWizardEvent<UserModel> event) {
		this.wizardHandler = event;
	}

	@Override
	public void validateAndReturn() {
		// TODO Auto-generated method stub

	}

	@Override
	public void next() {
		doPrepareUsertModel();
		wizardHandler.onValidateComplete(WizardStage.DONE, model);		
	}

	@Override
	public void back() {
		// TODO Auto-generated method stub

	}
}
