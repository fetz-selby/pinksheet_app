package com.cradlelabs.beta.client.modules.users;

import com.cradlelabs.beta.client.elements.AsideElement;
import com.cradlelabs.beta.client.modules.users.AddUserPopup.AddUserPopupEventHandler;
import com.cradlelabs.beta.client.modules.users.EditUserPopup.EditUserPopupEventHandler;
import com.cradlelabs.beta.client.modules.users.UsersControlsWidget.UsersControlsWidgetEventHandler;
import com.cradlelabs.beta.client.modules.users.UsersListWidget.UserEditEventHandler;
import com.cradlelabs.beta.client.widgets.CustomDraggablePopupPanel;
import com.cradlelabs.beta.shared.UserModel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class UsersModule extends Composite {

	private UsersListWidget listWidget;
	private static PinkSheetModuleUiBinder uiBinder = GWT
			.create(PinkSheetModuleUiBinder.class);

	interface PinkSheetModuleUiBinder extends UiBinder<Widget, UsersModule> {
	}

	
	@UiField HTMLPanel contentsPanel;
	@UiField AsideElement controls;
	
	public UsersModule() {
		initWidget(uiBinder.createAndBindUi(this));
		initComponents();
	}
	
	private void initComponents(){
		//Load Controls
		listWidget = new UsersListWidget();

		
		final UsersControlsWidget controlsWidget = new UsersControlsWidget();
		controlsWidget.setUsersControlsWidgetEventHandler(new UsersControlsWidgetEventHandler() {
			
			@Override
			public void onInvoke(String type) {
				listWidget.reload(type);
			}

			@Override
			public void onAddInvoked() {
				showUserPopup();
			}
		});
		
		listWidget.setUserEditEventHandler(new UserEditEventHandler() {
			
			@Override
			public void onUserEditInvoked(UserModel model) {
				showUserEditPopup(model);
			}
			
			@Override
			public void onLoadComplete() {
				controlsWidget.doneLoading();
			}
			
			@Override
			public void onDetailShow(UserModel model) {
				// TODO Auto-generated method stub
				
			}
		});
		
		contentsPanel.add(listWidget);
		controls.appendChild(controlsWidget.getElement());
	}
	
	private void showUserEditPopup(UserModel model){
		final CustomDraggablePopupPanel popup = getPopup();
		EditUserPopup editPopup = new EditUserPopup(model);
		editPopup.setEditUserPopupEventHandler(new EditUserPopupEventHandler() {
			
			@Override
			public void onClose() {
				popup.hide();
			}
		});
		
		popup.add(editPopup);
		popup.center();

	}
	
	private CustomDraggablePopupPanel getPopup(){
		CustomDraggablePopupPanel popup = new CustomDraggablePopupPanel();
		popup.setAutoHideEnabled(false);
		popup.setGlassEnabled(true);
		popup.setGlassStyleName("glassPanel");
		popup.setDraggable(true);

		return popup;
	}
	
	private void showUserPopup(){
		final CustomDraggablePopupPanel popup = getPopup();
		AddUserPopup userPopup = new AddUserPopup();
		userPopup.setAddUserPopupEventHandler(new AddUserPopupEventHandler() {
			
			@Override
			public void onClose() {
				popup.hide();
			}
		});
		popup.add(userPopup);
		popup.center();
	}

}
