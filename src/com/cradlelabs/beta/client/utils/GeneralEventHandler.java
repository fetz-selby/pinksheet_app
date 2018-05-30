package com.cradlelabs.beta.client.utils;

public interface GeneralEventHandler<T> {
	void onSuccess(T t);
	void onError();
}
