package com.cradlelabs.beta.client;

import com.cradlelabs.beta.shared.AgentModel;
import com.cradlelabs.beta.shared.UserModel;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UpdateServiceAsync {

	void deleteOrphanBlob(String blobKey, AsyncCallback<Void> callback);

	void removeAgent(AgentModel model, AsyncCallback<Integer> callback);

	void updateUser(UserModel model, AsyncCallback<Integer> callback);

}
