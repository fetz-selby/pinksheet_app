package com.cradlelabs.beta.client;

import com.cradlelabs.beta.shared.AgentModel;
import com.cradlelabs.beta.shared.PinkSheetModel;
import com.cradlelabs.beta.shared.UserModel;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("add")
public interface AddService extends RemoteService{
	int addAgent(AgentModel model);
	int addUser(UserModel model);
	int addPinkSheet(PinkSheetModel model);
}
