package com.cradlelabs.beta.client.mobile.upload;

import com.cradlelabs.beta.client.GlobalResources;
import com.cradlelabs.beta.client.widgets.MobileCustomFileUpload;
import com.cradlelabs.beta.client.widgets.MobileCustomFileUpload.MobileCustomFileUploadEventHandler;
import com.cradlelabs.beta.shared.AgentModel;
import com.cradlelabs.beta.shared.PinkSheetModel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class MobileUploadForm extends Composite {

	private PinkSheetModel model;
	private AgentModel agent;
	private static MobileUploadFormUiBinder uiBinder = GWT
			.create(MobileUploadFormUiBinder.class);

	interface MobileUploadFormUiBinder extends
			UiBinder<Widget, MobileUploadForm> {
	}
	
	@UiField MobileCustomFileUpload uploadWidget;
	@UiField TextBox nameField, msisdnField, consField, pollField;
	@UiField Button submit, reset;
	@UiField DivElement uploadInfo, confirmTitle;

	public MobileUploadForm() {
		initWidget(uiBinder.createAndBindUi(this));
		initComponents();
		initEvents();
	}
	
	private void initComponents(){
		agent = GlobalResources.getInstance().getAgentModel();
		
		nameField.setText(agent.getName());
		msisdnField.setText(agent.getMsisdn());
		//emailField.setText(agent.getEmail());
		consField.setText(agent.getConstituency());
		pollField.setText(agent.getPollingStation());
		
		nameField.setEnabled(false);
		//emailField.setEnabled(false);
		msisdnField.setEnabled(false);
		consField.setEnabled(false);
		pollField.setEnabled(false);
	}
	
	private void initEvents(){
		submit.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				preparePinkSheet();
				
			}
		});
		
		reset.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(model != null && !model.getBlobKey().isEmpty()){
					GlobalResources.getInstance().getUpdateRPC().deleteOrphanBlob(model.getBlobKey(), new AsyncCallback<Void>() {

						@Override
						public void onSuccess(Void result) {
							model.setBlobKey("");
							showSubmitButton(false);
						}

						@Override
						public void onFailure(Throwable caught) {
							showSubmitButton(false);
							Window.alert("Bad internet connection. Please try again later.");
						}
					});
				}	
			}
		});
		
		uploadWidget.setMobileCustomFileUploadEventHandler(new MobileCustomFileUploadEventHandler() {

			@Override
			public void onFormSubmit(final String avatarString, final String blobKey) {
				//Delete old blobKey
				
				if(model == null){
					model = new PinkSheetModel();
				}

				if(model.getBlobKey() != null && !model.getBlobKey().trim().isEmpty()){
					GlobalResources.getInstance().getUpdateRPC().deleteOrphanBlob(model.getBlobKey(), new AsyncCallback<Void>() {

						@Override
						public void onSuccess(Void result) {
							model.setBlobKey(blobKey);	
							showSubmitButton(true);
						}

						@Override
						public void onFailure(Throwable caught) {
							showSubmitButton(false);
							Window.alert("Bad internet connection. Please try again later.");
						}
					});
				}else{
					//img.setUrl(avatarString);
					model.setBlobKey(blobKey);
					showSubmitButton(true);
				}
			}
		});
	}
	
	private void showSuccess(){
		confirmTitle.removeClassName("hide");
		showSubmitButton(false);

		Timer timer = new Timer() {
			
			@Override
			public void run() {
				confirmTitle.addClassName("hide");
			}
		};
		
		timer.schedule(4000);
	}
	
	private void disableSubmit(boolean isDisable){
		if(isDisable){
			submit.setEnabled(false);
		}else{
			submit.setEnabled(true);
		}
	}
	
	private void showSubmitButton(boolean isShow){
		if(isShow){
			submit.removeStyleName("hide");
			reset.removeStyleName("hide");
			showSuccessUpload(true);
		}else{
			submit.addStyleName("hide");
			reset.addStyleName("hide");
			showSuccessUpload(false);
		}
	}
	
	private void showSuccessUpload(boolean isShow){
		if(isShow){
			uploadInfo.setInnerHTML("<i class='fa fa-check pull-left'></i>SUCCESS");
			uploadInfo.removeClassName("bg-primary");
			uploadInfo.addClassName("bg-success");
		}else{
			uploadInfo.setInnerText("Tap to Upload");
			uploadInfo.removeClassName("bg-success");
			uploadInfo.addClassName("bg-primary");
		}
	}

	private void preparePinkSheet(){
		if(model == null){
			model = new PinkSheetModel();
		}
		
		model.setAgentId(agent.getId());
		model.setConstituency(agent.getConstituency());
		model.setConstituencyId(agent.getConstituencyId());
		model.setEmail(agent.getEmail());
		model.setMsisdn(agent.getMsisdn());
		model.setPollId(agent.getPollingStationId());
		model.setPollingStation(agent.getPollingStation());
		model.setAgentName(agent.getName());
		model.setYear(agent.getYear());
		
		savePinkSheet();
	}
	
	private void savePinkSheet(){
		disableSubmit(true);
		GlobalResources.getInstance().getAddRPC().addPinkSheet(model, new AsyncCallback<Integer>() {
			
			@Override
			public void onSuccess(Integer result) {
				if(result > 0){
					showSuccess();
				}
				disableSubmit(false);
				model = null;
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Bad internet connection. Please try again later");
				disableSubmit(false);
			}
		});
	}
}
