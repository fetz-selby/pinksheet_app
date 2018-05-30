package com.cradlelabs.beta.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class BlobstoreModel implements IsSerializable{

	private String blobkey,url;

	public String getBlobkey() {
		return blobkey;
	}

	public void setBlobkey(String blobkey) {
		this.blobkey = blobkey;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
