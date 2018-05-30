package com.cradlelabs.beta.client;

import com.cradlelabs.beta.shared.AgentModel;
import com.cradlelabs.beta.shared.UserModel;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("update")
public interface UpdateService extends RemoteService{
	void deleteOrphanBlob(String blobKey);
	int removeAgent(AgentModel model);
	int updateUser(UserModel model);

}
