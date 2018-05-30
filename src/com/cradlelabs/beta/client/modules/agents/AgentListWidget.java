package com.cradlelabs.beta.client.modules.agents;

import java.util.ArrayList;
import java.util.HashMap;

import com.cradlelabs.beta.client.GlobalResources;
import com.cradlelabs.beta.client.utils.GeneralEventHandler;
import com.cradlelabs.beta.client.utils.Utils;
import com.cradlelabs.beta.shared.AgentModel;
import com.cradlelabs.beta.shared.BlobstoreModel;
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

public class AgentListWidget extends Composite {

	private HashMap<Integer, Element> liMap;
	private AgentEditEventHandler handler;
	private static AgentListWidgetUiBinder uiBinder = GWT
			.create(AgentListWidgetUiBinder.class);

	interface AgentListWidgetUiBinder extends UiBinder<Widget, AgentListWidget> {
	}

	public interface AgentEditEventHandler{
		void onAgentEditInvoked(AgentModel model);
		void onLoadComplete();
		void onDetailShow(AgentModel model);
	}

	@UiField UListElement ulListContainer;

	public AgentListWidget() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	private Element getLi(final AgentModel model, String imgLink){
		Element li = DOM.createElement("li");
		li.setClassName("list-group-item bg-dark list-height-md list-pointer-style");

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
					handler.onAgentEditInvoked(model);
				}
			}
		});

		Element avatarDiv = DOM.createElement("div");
		avatarDiv.setClassName("col-md-2 col-lg-2 text-white");
		avatarDiv.appendChild(getImgElement(imgLink));

		Element nameDiv = DOM.createElement("div");
		nameDiv.setClassName("col-sm-8 col-md-2 col-lg-2 clear agents-list-style text-white");
		nameDiv.setInnerText(model.getName());

		Element pollingDiv = DOM.createElement("div");
		pollingDiv.setClassName("col-md-3 col-lg-3 clear agents-list-style text-white");
		pollingDiv.setInnerText(model.getPollingStation());

		Element msisdnDiv = DOM.createElement("div");
		msisdnDiv.setClassName("col-md-2 col-lg-2 clear agents-list-style text-white");
		msisdnDiv.setInnerText(model.getMsisdn());

		editIconAnchor.appendChild(editIcon);
		spanRight.appendChild(editIconAnchor);

		li.appendChild(spanRight);
		li.appendChild(avatarDiv);
		li.appendChild(nameDiv);
		li.appendChild(pollingDiv);
		li.appendChild(msisdnDiv);

		return li;
	}

	private Element getImgElement(String imgUrl){
		Element img = DOM.createElement("img");
		img.setAttribute("src", imgUrl);
		img.addClassName("img-circle pull-left thumb agent-list-avatar-style");

		return img;
	}

	private void initBlob(final AgentModel model){
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

	private void doAgentsLoad(int constituencyId, int pollId, String year){
		ulListContainer.removeAllChildren();

		if(pollId == 0){
			GlobalResources.getInstance().getListRPC().getAgentsWithConsId(constituencyId, year, new AsyncCallback<ArrayList<AgentModel>>() {

				@Override
				public void onSuccess(ArrayList<AgentModel> result) {
					if(result != null){
						for(AgentModel model : result){
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
		}else{
			GlobalResources.getInstance().getListRPC().getAgentsWithPollingId(pollId, year, new AsyncCallback<ArrayList<AgentModel>>() {

				@Override
				public void onSuccess(ArrayList<AgentModel> result) {
					if(result != null){
						for(AgentModel model : result){
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
	}

	public void reload(int pollId, int constituencyId, String year){
		doAgentsLoad(constituencyId, pollId, year);
	}

	public void removeFromList(int id){
		if(liMap.containsKey(id)){
			liMap.get(id).removeFromParent();
		}
	}

	public void setAgentEditEventHandler(AgentEditEventHandler handler){
		this.handler = handler;
	}

}
