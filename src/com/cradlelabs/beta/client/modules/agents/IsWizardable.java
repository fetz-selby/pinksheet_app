package com.cradlelabs.beta.client.modules.agents;

import com.google.gwt.user.client.ui.IsWidget;

public interface IsWizardable<T> extends IsWidget{
	void validateAndProceed();
	void validateAndReturn();
	void setHasWizardEvent(HasWizardEvent<T> event);
	void next();
	void back();
}
