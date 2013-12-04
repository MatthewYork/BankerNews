package com.mattyork.jarbn;

import org.apache.http.impl.client.DefaultHttpClient;

public class BNWebService {
	
	public enum PostFilterType{
		PostFilterTypeTop,
	    PostFilterTypeAsk,
	    PostFilterTypeNew,
	    PostFilterTypeJobs,
	    PostFilterTypeBest
	}
	
	/***
	 * Default constructor
	 */
	public BNWebService(){
		client = new DefaultHttpClient();
	}
	
	public DefaultHttpClient client;
	
	
}
