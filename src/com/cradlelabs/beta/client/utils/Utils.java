package com.cradlelabs.beta.client.utils;

import com.cradlelabs.beta.client.GlobalResources;
import com.cradlelabs.beta.shared.BlobstoreModel;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class Utils {
	public static String getCapitalizedWord(String word){
		if(word.contains(" ")){
			String[] wordTokens = word.split("[\\s]+");
			String fullword = "";
			for(String tmpWord : wordTokens){
				fullword += tmpWord.substring(0,1).toUpperCase()+tmpWord.substring(1).toLowerCase()+" ";
			}
			
			return fullword.trim();
		}else{
			return word.substring(0,1).toUpperCase()+word.substring(1).toLowerCase();
		}
		
	}
	
	public static boolean isEmailValidFormat(String email){
		if(email.trim().matches("^([a-z0-9_\\.-]+)@([\\d\\p{L}\\a-z\\.-]+)\\.([a-z\\.]{2,6})$")){
			return true;
		}
		return false;
	}
	
	public static String getTruncatedText(String text, int limit){
		if(text == null || text.trim().isEmpty()){
			return "Not Specified";
		}else if(text.length() > limit){
			return text.substring(0, limit - 3)+" ...";
		}

		return text;
	}
	
	public static void retrieveFromBlobstore(String blobKey, final GeneralEventHandler<BlobstoreModel> handler){
		GlobalResources.getInstance().getListRPC().retrieveFromBlobstore(blobKey, new AsyncCallback<BlobstoreModel>() {
			
			@Override
			public void onSuccess(BlobstoreModel result) {
				handler.onSuccess(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public static boolean isNumber(String msisdn){
		if(msisdn != null){
			for(int i = 0; i < msisdn.length(); i++){
				char c = msisdn.charAt(i);
				if(c == '+') continue;
				if(!Character.isDigit(c)){
					return false;
				}
			}
			
			return true;
		}
		
		return false;
	}
	
	public static boolean isMsisdnValid(String msisdn){
		if(msisdn.contains(",")){
			String[] msisdnToken = msisdn.split(",");
			for(String tmpMsisdn : msisdnToken){
				if(tmpMsisdn.length() < 12){
					return false;
				}
				
				if(!isNumber(tmpMsisdn)){
					return false;
				}
			}
			
			return true;
		}else if(msisdn.length() > 11 && isNumber(msisdn)){
			return true;
		}
		
		return false;
	}
	
	public static boolean isNumbers(String msisdn){
		if(msisdn != null){
			for(int i = 0; i < msisdn.length(); i++){
				char c = msisdn.charAt(i);
				if(!Character.isDigit(c)){
					return false;
				}
			}
			
			return true;
		}
		
		return false;
	}

}
