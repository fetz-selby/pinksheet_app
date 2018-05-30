package com.cradlelabs.beta.client.modules.agents;

import java.util.ArrayList;

import com.cradlelabs.beta.client.GlobalResources;
import com.cradlelabs.beta.client.utils.Utils;
import com.cradlelabs.beta.shared.ConstituencyModel;
import com.cradlelabs.beta.shared.PollingStationModel;
import com.cradlelabs.beta.shared.RegionModel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class AgentControlsWidget extends Composite {

	private AgentControlsWidgetEventHandler handler;
	private String year;
	private int constituencyId, pollingStationId;
	private static PinkSheetControlsWidgetUiBinder uiBinder = GWT
			.create(PinkSheetControlsWidgetUiBinder.class);

	interface PinkSheetControlsWidgetUiBinder extends
			UiBinder<Widget, AgentControlsWidget> {
	}

	public interface AgentControlsWidgetEventHandler{
		void onInvoke(String year, int constituencyId, int pollId);
		void onAddInvoked();
	}

	@UiField ListBox regionBox, constituencyBox, pollingBox, yearBox;
	@UiField Button submitBtn, addBtn;
	
	public AgentControlsWidget() {
		initWidget(uiBinder.createAndBindUi(this));
		loadYear();
		initSubmitButton();
		initAddButton();
	}
	
	private void loadYear(){
		DOM.sinkEvents(yearBox.getElement(), Event.ONCHANGE);
		DOM.setEventListener(yearBox.getElement(), new EventListener() {
			
			@Override
			public void onBrowserEvent(Event event) {
				if(!yearBox.getValue(yearBox.getSelectedIndex()).equalsIgnoreCase("0")){
					year = yearBox.getValue(yearBox.getSelectedIndex());
					loadRegions();
				}
			}
		});
		
		yearBox.addItem("Select Year", "0");
		yearBox.addItem("2016", "2016");
	}
	
	private void loadRegions(){
		DOM.sinkEvents(regionBox.getElement(), Event.ONCHANGE);
		DOM.setEventListener(regionBox.getElement(), new EventListener() {
			
			@Override
			public void onBrowserEvent(Event event) {
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
		DOM.sinkEvents(constituencyBox.getElement(), Event.ONCHANGE);
		DOM.setEventListener(constituencyBox.getElement(), new EventListener() {
			
			@Override
			public void onBrowserEvent(Event event) {
				constituencyId = Integer.parseInt(constituencyBox.getValue(constituencyBox.getSelectedIndex()));
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
		DOM.sinkEvents(pollingBox.getElement(), Event.ONCHANGE);
		DOM.setEventListener(pollingBox.getElement(), new EventListener() {
			
			@Override
			public void onBrowserEvent(Event event) {
				pollingStationId = Integer.parseInt(pollingBox.getValue(pollingBox.getSelectedIndex()));
				//loadTypes();
			}
		});
		
		pollingBox.addItem("Loading ...", "0");
		GlobalResources.getInstance().getListRPC().getPollingStations(constituencyId, year, new AsyncCallback<ArrayList<PollingStationModel>>() {
			
			@Override
			public void onSuccess(ArrayList<PollingStationModel> result) {
				if(result != null){
					pollingBox.clear();
					pollingBox.addItem("Select Polling Station", "0");
					for(PollingStationModel model : result){
						pollingBox.addItem(model.getName(), model.getId()+"");
					}
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void initSubmitButton(){
		DOM.sinkEvents(submitBtn.getElement(), Event.ONCLICK);
		DOM.setEventListener(submitBtn.getElement(), new EventListener() {
			
			@Override
			public void onBrowserEvent(Event event) {
				showLoading(true);
				doValueGrabbing();				
			}
		});
		
	}
	
	private void initAddButton(){
		DOM.sinkEvents(addBtn.getElement(), Event.ONCLICK);
		DOM.setEventListener(addBtn.getElement(), new EventListener() {
			
			@Override
			public void onBrowserEvent(Event event) {
				if(handler != null){
					handler.onAddInvoked();
				}			
			}
		});
	}
	
	private void showLoading(boolean isShow){
		if(isShow){
			submitBtn.setEnabled(false);
		}else{
			submitBtn.setEnabled(true);
		}
	}
	
	private void doValueGrabbing(){
		String year = yearBox.getValue(yearBox.getSelectedIndex());
		int constituencyId = Integer.parseInt(constituencyBox.getValue(constituencyBox.getSelectedIndex()));
		int pollId = Integer.parseInt(pollingBox.getValue(pollingBox.getSelectedIndex()));
		
		if(constituencyId == 0){
			//Show error
			return;
		}
		
		if(constituencyId == 0 && pollId == 0){
			//Show error
			return;
		}
		
		if(year.equalsIgnoreCase("0")){
			//Show error
			return;
		}
		
		if(handler != null){
			handler.onInvoke(year, constituencyId, pollId);
		}
	}
	
	public void doneLoading(){
		showLoading(false);
	}
	
	public void setAgentControlsWidgetEventHandler(AgentControlsWidgetEventHandler handler){
		this.handler = handler;
	}

}
