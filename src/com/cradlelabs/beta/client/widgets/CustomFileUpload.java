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

public class CustomFileUpload extends Composite {

	private CustomFileUploadEventHandler handler;
	private static CustomFileUploadUiBinder uiBinder = GWT
			.create(CustomFileUploadUiBinder.class);

	interface CustomFileUploadUiBinder extends
	UiBinder<Widget, CustomFileUpload> {
	}

	public interface CustomFileUploadEventHandler{
		void onFormSubmit(String avatarString, String blobKey);
	}

	@UiField FileUpload upload;
	@UiField FormPanel formPanel;

	public CustomFileUpload() {
		initWidget(uiBinder.createAndBindUi(this));
		initUploadWidget();
		initEvent();
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

		//initDetailFormPanel();
	}

	private void initDetailFormPanel(String action){
		formPanel.setAction(action);
		formPanel.setMethod(FormPanel.METHOD_POST);
		formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
	}

	private void initUploadWidget(){
		upload.getElement().setAttribute("name", "image");
		upload.getElement().setAttribute("data-classInput", "form-control inline v-middle input-s");
		upload.getElement().setAttribute("data-classButton", "btn btn-default");
		upload.getElement().setAttribute("data-icon", "false");
	}

	private void initEvent(){
		formPanel.addSubmitCompleteHandler(new SubmitCompleteHandler() {

			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				GlobalResources.getInstance().getListRPC().requestPreviewImage(new AsyncCallback<BlobstoreModel>() {

					@Override
					public void onSuccess(BlobstoreModel result) {
						if(handler != null){
							handler.onFormSubmit(result.getUrl(), result.getBlobkey());
						}						
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

//	private String getProcessedImage(String unProcessedImage){
//		HTML html = new HTML(unProcessedImage);
//
//		return html.getText();
//	}

	public void setCustomFileUploadEventHandler(CustomFileUploadEventHandler handler){
		this.handler = handler;
	}

}
