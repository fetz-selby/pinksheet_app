package com.cradlelabs.beta.server.utils;

import com.google.appengine.api.blobstore.BlobKey;

public class MiscStorage {
	private String imgUrl;
	private BlobKey imgBlobKey;
	private static MiscStorage instance = new MiscStorage();
	
	private MiscStorage(){}
	
	public static MiscStorage getInstance(){
		return instance;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public BlobKey getImgBlobKey() {
		return imgBlobKey;
	}

	public void setImgBlobKey(BlobKey imgBlobKey) {
		this.imgBlobKey = imgBlobKey;
	}
	
}
