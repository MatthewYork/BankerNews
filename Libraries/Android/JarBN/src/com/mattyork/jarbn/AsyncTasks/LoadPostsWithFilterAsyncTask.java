package com.mattyork.jarbn.AsyncTasks;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

import com.mattyork.jarbn.BNManager;
import com.mattyork.jarbn.BNObjects.BNPost;
import com.mattyork.jarbn.BNWebService.PostFilterType;

public class LoadPostsWithFilterAsyncTask extends
		AsyncTask<String, Void, ArrayList<BNPost>> {

	String url;

	public LoadPostsWithFilterAsyncTask(PostFilterType filter) {
		// TODO Auto-generated constructor stub

		String pathAddition = "";

		switch (filter) {
		case PostFilterTypeTop:
			pathAddition = "";
			break;
		case PostFilterTypeAsk:
			pathAddition = "ask";
			break;
		case PostFilterTypeBest:
			pathAddition = "best";
			break;
		case PostFilterTypeJobs:
			pathAddition = "jobs";
			break;
		case PostFilterTypeNew:
			pathAddition = "newest";
			break;
		default:
			break;
		}
		this.url = "http://boredbanker.com/" + pathAddition;
	}

	@Override
	protected ArrayList<BNPost> doInBackground(String... params) {
		// TODO Auto-generated method stub

		HttpGet httpGet = new HttpGet(url);

		String htmlResponseString = "";

		HttpResponse response;
		try {
			response = BNManager.getInstance().Service.client.execute(httpGet);
			htmlResponseString = EntityUtils.toString(response.getEntity());

			ArrayList<BNPost> posts = BNPost
					.parsedPostsFromHTML(htmlResponseString);

			return posts;

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
