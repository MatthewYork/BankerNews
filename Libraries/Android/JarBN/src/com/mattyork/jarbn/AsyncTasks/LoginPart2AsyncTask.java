package com.mattyork.jarbn.AsyncTasks;

import org.apache.http.client.methods.HttpGet;

import android.content.Context;
import android.os.AsyncTask;

import com.mattyork.jarbn.Dtos.DtoLoginResponse;

public class LoginPart2AsyncTask extends AsyncTask<Void, Void, DtoLoginResponse> {
	
	Context context;
	String mUsername;
	String mPassword;
	String url;
	
	public LoginPart2AsyncTask(String username , String password) {
		// TODO Auto-generated constructor stub
		this.mUsername = username;
		this.mPassword = password;
		this.url = "https://boredbanker.com/" + "newslogin?whence=%6e%65%77%73";
	}
	
	@Override
	protected DtoLoginResponse doInBackground(
			Void... arg0) {
		
		HttpGet httpGet = new HttpGet(url);/*
		try 
			//Make the request
			HttpResponse response = Service.client.execute(httpGet);
			
			//Get cookie
			List<Cookie> cookies = Service.client.getCookieStore().getCookies();
			if (cookies.size() > 0) {
				@SuppressWarnings("unused")
				Cookie cookie = cookies.get(0);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		return null;
	}
	
}
