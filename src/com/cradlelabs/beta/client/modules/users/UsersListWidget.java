package com.cradlelabs.beta.client.modules.users;

import java.util.ArrayList;
import java.util.HashMap;

import com.cradlelabs.beta.client.GlobalResources;
import com.cradlelabs.beta.client.utils.GeneralEventHandler;
import com.cradlelabs.beta.client.utils.Utils;
import com.cradlelabs.beta.shared.BlobstoreModel;
import com.cradlelabs.beta.shared.UserModel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class UsersListWidget extends Composite {

	private HashMap<Integer, Element> liMap;
	private UserEditEventHandler handler;
	private static AgentListWidgetUiBinder uiBinder = GWT
			.create(AgentListWidgetUiBinder.class);

	interface AgentListWidgetUiBinder extends UiBinder<Widget, UsersListWidget> {
	}

	public interface UserEditEventHandler{
		void onUserEditInvoked(UserModel model);
		void onLoadComplete();
		void onDetailShow(UserModel model);
	}

	@UiField UListElement ulListContainer;
	
	public UsersListWidget() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	private Element getLi(final UserModel model, String imgLink){
		Element li = DOM.createElement("li");
		li.setClassName("list-group-item bg-primary list-height-md list-pointer-style");
		
		DOM.sinkEvents(li, Event.ONCLICK);
		DOM.setEventListener(li, new EventListener() {
			
			@Override
			public void onBrowserEvent(Event event) {
				if(handler != null){
					handler.onDetailShow(model);
				}
			}
		});

		Element spanRight = DOM.createElement("span");
		spanRight.setClassName("pull-right agents-list-style");

		Element editIconAnchor = DOM.createElement("a");
		editIconAnchor.setAttribute("href", "javascript:void(0)");

		Element editIcon = DOM.createElement("i");
		editIcon.setClassName("fa fa-pencil fa-fw m-r-xs agents-list-style");

		DOM.sinkEvents(editIconAnchor, Event.ONCLICK);
		DOM.setEventListener(editIconAnchor, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if(handler != null){
					handler.onUserEditInvoked(model);
				}
			}
		});

		Element avatarDiv = DOM.createElement("div");
		avatarDiv.setClassName("col-md-2 col-lg-2 text-white");
		avatarDiv.appendChild(getImgElement(imgLink));

		Element nameDiv = DOM.createElement("div");
		nameDiv.setClassName("col-sm-8 col-md-2 col-lg-2 clear agents-list-style text-white");
		nameDiv.setInnerText(model.getName());

		Element adminDiv = DOM.createElement("div");
		adminDiv.setClassName("col-md-3 col-lg-3 clear agents-list-style text-white");
		adminDiv.setInnerText(model.getPerm().equalsIgnoreCase("A")?"Administrator":"User");

		Element msisdnDiv = DOM.createElement("div");
		msisdnDiv.setClassName("col-md-2 col-lg-2 clear agents-list-style text-white");
		msisdnDiv.setInnerText(model.getMsisdn());

		editIconAnchor.appendChild(editIcon);
		spanRight.appendChild(editIconAnchor);

		li.appendChild(spanRight);
		li.appendChild(avatarDiv);
		li.appendChild(nameDiv);
		li.appendChild(adminDiv);
		li.appendChild(msisdnDiv);

		return li;
	}
	
	private Element getImgElement(String imgUrl){
		Element img = DOM.createElement("img");
		img.setAttribute("src", imgUrl);
		img.addClassName("img-circle pull-left thumb agent-list-avatar-style");
		
		return img;
	}
	
	private void initBlob(final UserModel model){
		if(model.getBlobKey() != null && !model.getBlobKey().trim().isEmpty()){
			Utils.retrieveFromBlobstore(model.getBlobKey(), new GeneralEventHandler<BlobstoreModel>() {
				
				@Override
				public void onSuccess(BlobstoreModel t) {
					Element li = null;
					if(t == null){
						li = getLi(model, "images/no-image.jpg");
					}else{
						li = getLi(model, t.getUrl());
					}
					liMap = new HashMap<Integer, Element>();
					ulListContainer.appendChild(li);
					liMap.put(model.getId(), li);
				}
				
				@Override
				public void onError() {
					// TODO Auto-generated method stub
					
				}
			});
		}else{
			liMap = new HashMap<Integer, Element>();
			Element li = getLi(model, "images/no-image.jpg");
			ulListContainer.appendChild(li);
			liMap.put(model.getId(), li);
		}
	}

	private void doAgentsLoad(String perm){
		ulListContainer.removeAllChildren();
		
		GlobalResources.getInstance().getListRPC().getUsers(perm, new AsyncCallback<ArrayList<UserModel>>() {
			
			@Override
			public void onSuccess(ArrayList<UserModel> result) {
				if(result != null){
					for(UserModel model : result){
						initBlob(model);
					}
					if(handler != null){
						handler.onLoadComplete();
					}	
				}
							
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public void reload(String perm){
		doAgentsLoad(perm);
	}
	
	public void removeFromList(int id){
		if(liMap.containsKey(id)){
			liMap.get(id).removeFromParent();
		}
	}
	
	public void setUserEditEventHandler(UserEditEventHandler handler){
		this.handler = handler;
	}

}
