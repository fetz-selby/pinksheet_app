package com.cradlelabs.beta.client.utils;

import java.util.Date;

import com.cradlelabs.beta.shared.PSUser;
import com.google.gwt.user.client.Cookies;

public class CookieVerifier {
	private static CookieVerifier cookie = new CookieVerifier();
	
	private CookieVerifier(){}
	
	public static CookieVerifier getInstance(){
		return cookie;
	}
	
	public static void addCookie(PSUser model){
		Date date = new Date();
		
		//Expires in 1 hour from login
		date.setHours(date.getHours() + 1);
		//date.setMinutes(date.getMinutes() + 1);
		
		Cookies.setCookie("user_id", ""+model.getId());
		Cookies.setCookie("email", model.getEmail());
		Cookies.setCookie("msisdn", model.getMsisdn());
		Cookies.setCookie("type", model.getType());
	}
	
	public static void addForgetCookie(){
	}
	
	public static void clearCookie(){
		Cookies.removeCookie("user_id");
		Cookies.removeCookie("msisdn");
		Cookies.removeCookie("email");
		Cookies.removeCookie("type");
	}
	
	public static boolean isAppCookieExist(){
		if(Cookies.getCookie("user_id") != null && !(Cookies.getCookie("user_id").isEmpty())){
			return true;
		}
		
		return false;
	}
	
	public static boolean isAgent(){
		if(Cookies.getCookie("type").equalsIgnoreCase("A")){
			return true;
		}
		
		return false;
	}
	
	public static boolean isUser(){
		if(Cookies.getCookie("type").equalsIgnoreCase("U")){
			return true;
		}
		
		return false;
	}
	
	public static String getMsisdn(){
		return Cookies.getCookie("msisdn");
	}
	
	
	public static boolean isForgetCookieExist(){
//		if(Cookies.getCookie("apptype") != null && !(Cookies.getCookie("apptype").isEmpty())){
//			return true;
//		}
		
		return false;
	}
	
	public int getId(){
		return Integer.parseInt(Cookies.getCookie("user_id"));
	}

}
