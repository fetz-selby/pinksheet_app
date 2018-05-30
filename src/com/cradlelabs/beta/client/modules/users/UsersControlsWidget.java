package com.cradlelabs.beta.client.modules.users;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class UsersControlsWidget extends Composite {

	private UsersControlsWidgetEventHandler handler;
	private static PinkSheetControlsWidgetUiBinder uiBinder = GWT
			.create(PinkSheetControlsWidgetUiBinder.class);

	interface PinkSheetControlsWidgetUiBinder extends
			UiBinder<Widget, UsersControlsWidget> {
	}

	public interface UsersControlsWidgetEventHandler{
		void onInvoke(String type);
		void onAddInvoked();
	}

	@UiField ListBox typeBox;
	@UiField Button submitBtn, addBtn;
	
	public UsersControlsWidget() {
		initWidget(uiBinder.createAndBindUi(this));
		loadTypes();
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
	
	private void doValueGrabbing(){
		if(typeBox.getValue(typeBox.getSelectedIndex()).equalsIgnoreCase("0")){
			return;
		}
		
		if(handler != null){
			handler.onInvoke(typeBox.getValue(typeBox.getSelectedIndex()));
		}
	}
	
	private void showLoading(boolean isShow){
		if(isShow){
			submitBtn.setEnabled(false);
		}else{
			submitBtn.setEnabled(true);
		}
	}
	
	private void loadTypes(){
		typeBox.clear();
		
		typeBox.addItem("Select Role", "0");
		typeBox.addItem("Administrator", "A");
		typeBox.addItem("User", "U");
		
		initAddButton();
		initSubmitButton();
	}
	
	public void doneLoading(){
		showLoading(false);
	}
	
	public void setUsersControlsWidgetEventHandler(UsersControlsWidgetEventHandler handler){
		this.handler = handler;
	}

}
