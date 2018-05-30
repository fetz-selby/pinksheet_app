package com.cradlelabs.beta.client.modules.agents;

import java.util.ArrayList;
import java.util.HashMap;

import com.cradlelabs.beta.client.GlobalResources;
import com.cradlelabs.beta.client.utils.GeneralEventHandler;
import com.cradlelabs.beta.client.utils.Utils;
import com.cradlelabs.beta.client.widgets.CustomFileUpload;
import com.cradlelabs.beta.client.widgets.CustomFileUpload.CustomFileUploadEventHandler;
import com.cradlelabs.beta.shared.AgentModel;
import com.cradlelabs.beta.shared.BlobstoreModel;
import com.cradlelabs.beta.shared.ConstituencyModel;
import com.cradlelabs.beta.shared.PollingStationModel;
import com.cradlelabs.beta.shared.RegionModel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class StageOne extends Composite implements IsWizardable<AgentModel>{

	private HasWizardEvent<AgentModel> wizardHandler;
	private AgentModel model;
	private HashMap<Integer, Integer> pollingConstituencyHash;

	private static StageOneUiBinder uiBinder = GWT
			.create(StageOneUiBinder.class);

	interface StageOneUiBinder extends UiBinder<Widget, StageOne> {
	}

	@UiField Image img;
	@UiField CustomFileUpload uploadWidget;
	@UiField TextBox nameField, emailField, msisdnField;
	@UiField ListBox regionBox, constituencyBox, pollingBox, yearBox;

	
	public StageOne(AgentModel model) {
		this.model = model;
		initWidget(uiBinder.createAndBindUi(this));
		initComponent(model);
		loadYear();
		initEvent();
	}

	public StageOne() {
		model = null;
		initWidget(uiBinder.createAndBindUi(this));
		initComponent(null);
		loadYear();
		initEvent();
	}

	private void initComponent(AgentModel model){
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
	
	private void loadYear(){
		
		yearBox.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				if(!yearBox.getValue(yearBox.getSelectedIndex()).equalsIgnoreCase("0")){
					loadRegions();
				}				
			}
		});
		
		yearBox.clear();
		yearBox.addItem("Select Year", "0");
		yearBox.addItem("2016", "2016");
	}
	
	private void loadRegions(){
		
		regionBox.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				loadConstituencies(Integer.parseInt(regionBox.getValue(regionBox.getSelectedIndex())));
			}
		});
		
		regionBox.addItem("Loading ...", "0");
		GlobalResources.getInstance().getListRPC().getAllRegions(new AsyncCallback<ArrayList<RegionModel>>() {
			
			@Override
			public void onSuccess(ArrayList<RegionModel> result) {
				if(result != null){
					regionBox.clear();
					regionBox.addItem("Select Region", "0");
					for(RegionModel model : result){
						regionBox.addItem(Utils.getCapitalizedWord(model.getName()), model.getId()+"");
					}
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	
	private void loadConstituencies(int regionId){
		
		constituencyBox.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				loadPollingStations(Integer.parseInt(constituencyBox.getValue(constituencyBox.getSelectedIndex())));
			}
		});
		
		constituencyBox.addItem("Loading ...", "0");
		GlobalResources.getInstance().getListRPC().getParentConstituencies(regionId, new AsyncCallback<ArrayList<ConstituencyModel>>() {
			
			@Override
			public void onSuccess(ArrayList<ConstituencyModel> result) {
				if(result != null){
					constituencyBox.clear();
					constituencyBox.addItem("Select Constituency", "0");
					for(ConstituencyModel model : result){
						constituencyBox.addItem(model.getName(), model.getId()+"");
					}
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void loadPollingStations(int constituencyId){
		
		pollingBox.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				// TODO Auto-generated method stub
				
			}
		});
		
		pollingBox.addItem("Loading ...", "0");
		GlobalResources.getInstance().getListRPC().getPollingStations(constituencyId, yearBox.getValue(yearBox.getSelectedIndex()), new AsyncCallback<ArrayList<PollingStationModel>>() {
			
			@Override
			public void onSuccess(ArrayList<PollingStationModel> result) {
				if(result != null){
					pollingBox.clear();
					pollingBox.addItem("Select Polling Station", "0");
					pollingConstituencyHash = new HashMap<Integer, Integer>();
					
					for(PollingStationModel model : result){
						pollingBox.addItem(model.getName(), model.getId()+"");
						pollingConstituencyHash.put(model.getId(), model.getConstituenyId());
					}
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	private void initEvent(){
		uploadWidget.setCustomFileUploadEventHandler(new CustomFileUploadEventHandler() {

			@Override
			public void onFormSubmit(final String avatarString, final String blobKey) {
				//Delete old blobKey
				if(model == null){
					model = new AgentModel();
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
		
		if(!(Utils.isMsisdnValid(msisdnField.getText()) && msisdnField.getText().trim().contains("+"))){
			doPublishError("Invalid mobile number!");
			return;
		}

		if(yearBox.getValue(yearBox.getSelectedIndex()).equalsIgnoreCase("0")){
			doPublishError("Year cannot be empty!");
			return;
		}
		
		if(constituencyBox.getValue(constituencyBox.getSelectedIndex()).equalsIgnoreCase("0")){
			doPublishError("Constituency cannot be empty!");
			return;
		}
		
		if(pollingBox.getValue(pollingBox.getSelectedIndex()).equalsIgnoreCase("0")){
			doPublishError("Polling Station cannot be empty!");
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

	private void doPrepareAgentModel(){
		if(model == null){
			model = new AgentModel();
		}

		model.setName(nameField.getText().trim());
		model.setMsisdn(msisdnField.getText().trim());
		model.setEmail(emailField.getText().trim());
		model.setYear(yearBox.getValue(yearBox.getSelectedIndex()));
		model.setConstituencyId(pollingConstituencyHash.get(Integer.parseInt(pollingBox.getValue(pollingBox.getSelectedIndex()))));
		model.setPollingStationId(Integer.parseInt(pollingBox.getValue(pollingBox.getSelectedIndex())));
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
		wizardHandler.onValidateComplete(WizardStage.DONE, model);		
	}

	@Override
	public void back() {
		// TODO Auto-generated method stub

	}
}
