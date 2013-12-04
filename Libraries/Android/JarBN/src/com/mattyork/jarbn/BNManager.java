package com.mattyork.jarbn;

import java.util.HashMap;

import org.apache.http.cookie.Cookie;

import com.mattyork.jarbn.BNObjects.BNUser;

public class BNManager {

	/***
	 * HN Manager is a singleton, and as such, needs a static reference.
	 */
	private static BNManager instance = null;
	
	public BNWebService Service;
	public String postFNID;
	String userSubmissionFNID;
	Cookie SessionCookie;
	BNUser SessionUser;
	HashMap<String, Integer> MarkAsReadDictionary;
	
	
	protected BNManager(){
		Service = new BNWebService();
	}
	
	public static BNManager getInstance() {
	      if(instance == null) {
	         instance = new BNManager();
	      }
	      return instance;
	   }
}
