package com.mattyork.jarbn.AsyncTasks;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

import com.mattyork.jarbn.BNManager;
import com.mattyork.jarbn.Dtos.DtoLoginResponse;

public class LoginAsyncTask extends AsyncTask<Void, Void, DtoLoginResponse> {

	String url;
	String mUsername;
	String mPassword;
	
	public LoginAsyncTask(String username, String password) {
		// TODO Auto-generated constructor stub
		this.mUsername = username;
		this.mPassword = password;
		
		this.url = "https://boredbanker.com/" + "newslogin?whence=%6e%65%77%73";
	}
	
	@Override
	protected DtoLoginResponse doInBackground(
			Void... arg0) {
		// TODO Auto-generated method stub
		
		HttpGet httpGet = new HttpGet(url);

		String htmlResponseString = "";

		HttpResponse response;
		try {
			response = BNManager.getInstance().Service.client.execute(httpGet);
			htmlResponseString = EntityUtils.toString(response.getEntity());

			//Process response by calling second async task

			return null;

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
		
	}

	@Override
	protected void onPostExecute(DtoLoginResponse result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		
	}
	
	
	
}
