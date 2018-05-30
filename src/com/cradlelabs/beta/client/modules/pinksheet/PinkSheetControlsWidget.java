package com.cradlelabs.beta.client.modules.pinksheet;

import java.util.ArrayList;

import com.cradlelabs.beta.client.GlobalResources;
import com.cradlelabs.beta.client.utils.Utils;
import com.cradlelabs.beta.shared.ConstituencyModel;
import com.cradlelabs.beta.shared.PollingStationModel;
import com.cradlelabs.beta.shared.RegionModel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class PinkSheetControlsWidget extends Composite {

	private PinkSheetControlsWidgetEventHandler handler;
	private String year;
	private int constituencyId, pollingStationId;
	private static PinkSheetControlsWidgetUiBinder uiBinder = GWT
			.create(PinkSheetControlsWidgetUiBinder.class);

	interface PinkSheetControlsWidgetUiBinder extends
			UiBinder<Widget, PinkSheetControlsWidget> {
	}

	public interface PinkSheetControlsWidgetEventHandler{
		void onInvoke(int pollId, String year, String size);
	}

	@UiField ListBox regionBox, constituencyBox, pollingBox, yearBox, sizeBox;
	@UiField Button submitBtn;
	
	public PinkSheetControlsWidget() {
		initWidget(uiBinder.createAndBindUi(this));
		loadYear();
		initButton();
	}
	
	private void initButton(){
		DOM.sinkEvents(submitBtn.getElement(), Event.ONCLICK);
		DOM.setEventListener(submitBtn.getElement(), new EventListener() {
			
			@Override
			public void onBrowserEvent(Event event) {
				doValidation();				
			}
		});
	}
	
	private void showLoading(boolean isShow){
		if(isShow){
			submitBtn.setText("Loading ...");
			submitBtn.setEnabled(false);
		}else{
			submitBtn.setText("Submit");
			submitBtn.setEnabled(true);
		}
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
				Window.alert("Bad internet connection. Please try again later.");
				
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
				Window.alert("Bad internet connection. Please try again later.");
				
			}
		});
	}
	
	private void loadPollingStations(int constituencyId){
		DOM.sinkEvents(pollingBox.getElement(), Event.ONCHANGE);
		DOM.setEventListener(pollingBox.getElement(), new EventListener() {
			
			@Override
			public void onBrowserEvent(Event event) {
				pollingStationId = Integer.parseInt(pollingBox.getValue(pollingBox.getSelectedIndex()));
				loadImageSizes();
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
				Window.alert("Bad internet connection. Please try again later.");
			}
		});
	}
	
	private void loadImageSizes(){
		DOM.sinkEvents(sizeBox.getElement(), Event.ONCHANGE);
		DOM.setEventListener(sizeBox.getElement(), new EventListener() {
			
			@Override
			public void onBrowserEvent(Event event) {
				
			}
		});
		
		sizeBox.addItem("Medium", "M");
		sizeBox.addItem("Large", "L");
		sizeBox.addItem("Small", "S");
	}
	
	private void doValidation(){
		if(yearBox.getValue(yearBox.getSelectedIndex()).equalsIgnoreCase("0")){
			return;
		}
		
		if(regionBox.getValue(regionBox.getSelectedIndex()).equalsIgnoreCase("0")){
			return;
		}
		
		if(constituencyBox.getValue(constituencyBox.getSelectedIndex()).equalsIgnoreCase("0")){
			return;
		}
		
		if(pollingBox.getValue(pollingBox.getSelectedIndex()).equalsIgnoreCase("0")){
			return;
		}
		
		int pollId = Integer.parseInt(pollingBox.getValue(pollingBox.getSelectedIndex()));
		String year = yearBox.getValue(yearBox.getSelectedIndex());
		String size = sizeBox.getValue(sizeBox.getSelectedIndex());
		
		if(handler != null){
			handler.onInvoke(pollId, year, size);
		}
	}
	
	public void showLoading(){
		showLoading(true);
	}
	
	public void doneLoading(){
		showLoading(false);
	}
	
	public void setPinkSheetControlsWidgetEventHandler(PinkSheetControlsWidgetEventHandler handler){
		this.handler = handler;
	}

}
