package com.cradlelabs.beta.client;

import java.util.ArrayList;

import com.cradlelabs.beta.shared.AgentModel;
import com.cradlelabs.beta.shared.BlobstoreModel;
import com.cradlelabs.beta.shared.ConstituencyModel;
import com.cradlelabs.beta.shared.PSUser;
import com.cradlelabs.beta.shared.PinkSheetModel;
import com.cradlelabs.beta.shared.PollingStationModel;
import com.cradlelabs.beta.shared.RegionModel;
import com.cradlelabs.beta.shared.UserModel;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ListServiceAsync {

	void getAllRegions(AsyncCallback<ArrayList<RegionModel>> callback);

	void getPollingStations(int constituencyId, String year,
			AsyncCallback<ArrayList<PollingStationModel>> callback);

	void getAgentsWithConsId(int constituencyId, String year,
			AsyncCallback<ArrayList<AgentModel>> callback);

	void getAgentsWithPollingId(int pollingStationId, String year,
			AsyncCallback<ArrayList<AgentModel>> callback);

	void getParentConstituencies(int regionId,
			AsyncCallback<ArrayList<ConstituencyModel>> callback);

	void retrieveFromBlobstore(String blobKey,
			AsyncCallback<BlobstoreModel> callback);

	void requestPreviewImage(AsyncCallback<BlobstoreModel> callback);

	void getUploadUrl(String path, AsyncCallback<String> callback);

	void getUser(String msisdn, AsyncCallback<UserModel> callback);

	void getAgent(String msisdn, AsyncCallback<AgentModel> callback);

	void getUsers(String perm, AsyncCallback<ArrayList<UserModel>> callback);

	void requestLogin(String username, String password,
			AsyncCallback<PSUser> callback);

	void getPinkSheets(int pollId, String year,
			AsyncCallback<ArrayList<PinkSheetModel>> callback);

	void agentsCount(AsyncCallback<String> callback);

	void usersCount(AsyncCallback<String> callback);

	void pinkSheetsCount(AsyncCallback<String> callback);

	void pinkSheetsCount(int pollId, AsyncCallback<String> callback);

}
