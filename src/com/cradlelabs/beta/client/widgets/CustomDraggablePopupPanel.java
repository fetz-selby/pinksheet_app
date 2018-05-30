package com.cradlelabs.beta.client.widgets;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.PopupPanel;


public class CustomDraggablePopupPanel extends PopupPanel{
	private static int widgetCounter;

	public CustomDraggablePopupPanel(){
		super();
		this.getElement().setId(""+widgetCounter++);
	}
	
	public void setDraggable(boolean isDraggable){
		if(isDraggable){
			doDrag();
		}
	}
	
	private void doDrag(){
		 Scheduler.get().scheduleDeferred(new Command() {
		        @Override public void execute() {
					doNativeDrag(CustomDraggablePopupPanel.this.getElement().getId());
		        }
		      });
	}
	
	private native void doNativeDrag(String id)/*-{
		$wnd.$( "#"+id ).draggable();
	}-*/;

}
