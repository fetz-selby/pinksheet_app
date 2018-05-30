package com.cradlelabs.beta.client.modules.agents;


public interface HasWizardEvent<T> {
	void onValidateComplete(WizardStage stage, T model);
	void onError(String message);
}
