package com.cradlelabs.beta.client;

import com.cradlelabs.beta.shared.AgentModel;
import com.cradlelabs.beta.shared.PinkSheetModel;
import com.cradlelabs.beta.shared.UserModel;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AddServiceAsync {

	void addAgent(AgentModel model, AsyncCallback<Integer> callback);

	void addUser(UserModel model, AsyncCallback<Integer> callback);

	void addPinkSheet(PinkSheetModel model, AsyncCallback<Integer> callback);

}
