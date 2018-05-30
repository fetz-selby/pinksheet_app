package com.cradlelabs.beta.client.widgets;

import com.cradlelabs.beta.client.GlobalResources;
import com.cradlelabs.beta.shared.BlobstoreModel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.Widget;

public class MobileCustomFileUpload extends Composite {

	private MobileCustomFileUploadEventHandler handler;
	private static CustomFileUploadUiBinder uiBinder = GWT
			.create(CustomFileUploadUiBinder.class);

	interface CustomFileUploadUiBinder extends
	UiBinder<Widget, MobileCustomFileUpload> {
	}

	public interface MobileCustomFileUploadEventHandler{
		void onFormSubmit(String avatarString, String blobKey);
	}

	@UiField FileUpload upload;
	@UiField FormPanel formPanel;

	public MobileCustomFileUpload() {
		initWidget(uiBinder.createAndBindUi(this));
		initFormPanel();
	}

	private void initFormPanel(){

		GlobalResources.getInstance().getListRPC().getUploadUrl("/blobsave", new AsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {
				initDetailFormPanel(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Bad internet connection. Please try again later.");
			}
		});

	}

	private void initDetailFormPanel(String action){
		formPanel.setAction(action);
		formPanel.setMethod(FormPanel.METHOD_POST);
		formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);

		initUploadWidget();
		initEvent();
	}

	private void initUploadWidget(){
		upload.getElement().setAttribute("name", "image");
		upload.getElement().setAttribute("data-classInput", "form-control inline v-middle input-s");
		upload.getElement().setAttribute("data-classButton", "btn btn-default");
		upload.getElement().setAttribute("data-icon", "false");
	}
	
	private void reInitPanel(final BlobstoreModel model){
		GlobalResources.getInstance().getListRPC().getUploadUrl("/blobsave", new AsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {
				formPanel.setAction(result);
				if(handler != null){
					handler.onFormSubmit(model.getUrl(), model.getBlobkey());
				}	
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Bad internet connection. Please try again later.");
			}
		});
	}

	private void initEvent(){
		formPanel.addSubmitCompleteHandler(new SubmitCompleteHandler() {

			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				
				GlobalResources.getInstance().getListRPC().requestPreviewImage(new AsyncCallback<BlobstoreModel>() {

					@Override
					public void onSuccess(BlobstoreModel result) {
						formPanel.reset();
						reInitPanel(result);
					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Bad internet connection. Please try again later.");
					}
				});
			}
		});

		formPanel.addSubmitHandler(new SubmitHandler() {

			@Override
			public void onSubmit(SubmitEvent event) {
			}
		});

		upload.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				formPanel.submit();
			}
		});

	}

	public void setMobileCustomFileUploadEventHandler(MobileCustomFileUploadEventHandler handler){
		this.handler = handler;
	}

}
