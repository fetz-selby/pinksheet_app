package com.cradlelabs.beta.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class CustomCheckBox extends Composite {

	private CustomCheckBoxEventHandler handler;
	private String id;
	private String labelName;
	private boolean isChecked;
	private static CustomCheckBoxUiBinder uiBinder = GWT
			.create(CustomCheckBoxUiBinder.class);

	interface CustomCheckBoxUiBinder extends UiBinder<Widget, CustomCheckBox> {
	}
	
	public interface CustomCheckBoxEventHandler{
		void onCheck(String id, String name, boolean isChecked);
	}

	@UiField InputElement checkInput;
	@UiField SpanElement fieldName;
	public CustomCheckBox(String labelName, String id) {
		this.id = id;
		this.labelName = labelName;
		initWidget(uiBinder.createAndBindUi(this));
		initElement();
		initEvent();
	}
	
	private void initElement(){
		fieldName.setInnerText(labelName);
	}
	
	private void initEvent(){
		DOM.sinkEvents(checkInput, Event.ONCLICK);
		DOM.setEventListener(checkInput, new EventListener() {
			
			@Override
			public void onBrowserEvent(Event event) {
				isChecked = !isChecked;
				if(handler != null){
					handler.onCheck(id, labelName, isChecked);
				}
			}
		});
	}
	
	public void setChecked(boolean isChecked){
		if(isChecked){
			checkInput.setAttribute("checked", "");
			this.isChecked = true;
		}else{
			checkInput.removeAttribute("checked");
			this.isChecked = false;
		}
	}
	
	public String getId(){
		return id;
	}
	
	public void setCustomCheckBoxEventHandler(CustomCheckBoxEventHandler handler){
		this.handler = handler;
	}

}
