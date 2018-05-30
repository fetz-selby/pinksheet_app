package com.cradlelabs.beta.client.modules.pinksheet;

import java.util.ArrayList;
import java.util.HashMap;

import com.cradlelabs.beta.client.GlobalResources;
import com.cradlelabs.beta.client.utils.GeneralEventHandler;
import com.cradlelabs.beta.client.utils.Utils;
import com.cradlelabs.beta.shared.BlobstoreModel;
import com.cradlelabs.beta.shared.PinkSheetModel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class PinkSheetView extends Composite {

	private PinkSheetViewEventHandler handler;
	private static int counter;
	private int checker;
	private static PinkSheetViewUiBinder uiBinder = GWT
			.create(PinkSheetViewUiBinder.class);

	interface PinkSheetViewUiBinder extends UiBinder<Widget, PinkSheetView> {
	}

	public interface PinkSheetViewEventHandler{
		void doneLoading();
	}

	@UiField DivElement sels_c, sorryInfo;

	public PinkSheetView() {
		counter ++;
		initWidget(uiBinder.createAndBindUi(this));
		initComponent();
	}

	private void initComponent(){
		sels_c.setId("sels_container_"+counter);
	}

	private void doNativeRun(final HashMap<String, PinkSheetModel> fileHash){
		Scheduler.get().scheduleDeferred(new Command() {
			@Override public void execute() {
				psRun(sels_c.getId(), getScriptConstruction(fileHash));
				if(handler != null){
					handler.doneLoading();
				}
			}
		});
	}

	private void showNoSheetsAvailable(boolean isShow){
		if(isShow){
			sorryInfo.removeClassName("hide");
		}else{
			sorryInfo.addClassName("hide");
		}
	}

	public void reload(int pollId, String year, final String size){
		GlobalResources.getInstance().getListRPC().getPinkSheets(pollId, year, new AsyncCallback<ArrayList<PinkSheetModel>>() {

			@Override
			public void onSuccess(final ArrayList<PinkSheetModel> result) {

				if(result != null){
					//Show if empty
					if(result.size() == 0){
						showNoSheetsAvailable(true);
						handler.doneLoading();
						return;
					}

					showNoSheetsAvailable(false);
					checker = 0;
					final HashMap<String, PinkSheetModel> filenameHash = new HashMap<String, PinkSheetModel>();

					for(final PinkSheetModel pinkSheet : result){

						//Storing the images in a hash
						Utils.retrieveFromBlobstore(pinkSheet.getBlobKey(), new GeneralEventHandler<BlobstoreModel>() {

							@Override
							public void onSuccess(BlobstoreModel t) {
								checker ++;

								if(t != null){
									if(size.equalsIgnoreCase("M")){
										filenameHash.put(t.getUrl()+"=s600", pinkSheet);
									}else if(size.equalsIgnoreCase("L")){
										filenameHash.put(t.getUrl()+"=s1200", pinkSheet);
									}else if(size.equalsIgnoreCase("S")){
										filenameHash.put(t.getUrl()+"=s300", pinkSheet);
									}else{
										filenameHash.put(t.getUrl(), pinkSheet);
									}
									
									if(checker == result.size()){

										//Go ahead to execute
										doNativeRun(filenameHash);
									}
								}else{
									if(checker == result.size() && handler != null){
										handler.doneLoading();
									}
								}
							}

							@Override
							public void onError() {
								Window.alert("Bad internet connection. Please try again later.");
							}
						});
					}

					if(result.size() == 0 && handler != null){
						handler.doneLoading();
					}
				}else{
					showNoSheetsAvailable(true);
					handler.doneLoading();

					return;
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				handler.doneLoading();
				Window.alert("Bad internet connection. Please try again later.");

			}
		});
	}

	private String getScriptConstruction(HashMap<String, PinkSheetModel> filenameHash){
		String pswpElement = "var pswpElement = $('.pswp')[0];";

		String itemsVar = "var items = [";
		String items = "";

		for(String filename : filenameHash.keySet()){
			PinkSheetModel obj = filenameHash.get(filename);
			items += "{src:'"+filename+"',w:600,h:400,title:'Date "+obj.getDate()+"'},";
		}

		itemsVar = itemsVar+items+"];";


		String options = "var options = {index:0,clickToCloseNonZoomable: false,shareButtons:[{id:'download', label:'Download Pink Sheet', url:'{{raw_image_url}}', download:true}]};";
		String gallery = "var gallery = new PhotoSwipe(pswpElement, PhotoSwipeUI_Default, items, options);";
		String init = "gallery.init();";

		String scriptString = "<script>"+pswpElement+itemsVar+options+gallery+init+"</script>";

		return scriptString;
	}

	public void setPinkSheetViewEventHandler(PinkSheetViewEventHandler handler){
		this.handler = handler;
	}

	private native void psRun(String id, String scriptString)/*-{
		$wnd.$('#'+id).html(scriptString);
	}-*/;

}
