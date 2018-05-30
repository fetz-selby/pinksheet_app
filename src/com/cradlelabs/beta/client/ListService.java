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
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("list")
public interface ListService extends RemoteService{
	ArrayList<RegionModel> getAllRegions();
	ArrayList<ConstituencyModel> getParentConstituencies(int regionId);
	ArrayList<PollingStationModel> getPollingStations(int constituencyId, String year);
	ArrayList<AgentModel> getAgentsWithConsId(int constituencyId, String year);
	ArrayList<AgentModel> getAgentsWithPollingId(int pollingStationId, String year);
	ArrayList<UserModel> getUsers(String perm);
	
	ArrayList<PinkSheetModel> getPinkSheets(int pollId, String year);
	
	
	BlobstoreModel retrieveFromBlobstore(String blobKey);
	BlobstoreModel requestPreviewImage();
	String getUploadUrl(String path);
	
	UserModel getUser(String msisdn);
	AgentModel getAgent(String msisdn);
	
	String agentsCount();
	String usersCount();
	String pinkSheetsCount();
	
	String pinkSheetsCount(int pollId);

	
	PSUser requestLogin(String username, String password);
}
