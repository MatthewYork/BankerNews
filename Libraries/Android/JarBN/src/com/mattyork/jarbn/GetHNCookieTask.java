package com.mattyork.jarbn;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;

import android.os.AsyncTask;

/***
 * Async Task to fetch the cookie from HN after logging in
 * @author matthewyork
 *
 */
public class GetHNCookieTask extends AsyncTask<Void, Void, Void> {

	@Override
	protected Void doInBackground(Void... arg0) {
		// TODO Auto-generated method stub
		
		HttpGet httpGet = new HttpGet("https://news.ycombinator.com/");
		try {
			//Make the request
			HttpResponse response = BNManager.getInstance().Service.client.execute(httpGet);
			
			//Get cookie
			List<Cookie> cookies = BNManager.getInstance().Service.client.getCookieStore().getCookies();
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
		}
		
		return null;
	}
	
}
