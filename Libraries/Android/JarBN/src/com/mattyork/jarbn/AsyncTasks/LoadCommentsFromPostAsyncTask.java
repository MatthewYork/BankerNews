package com.mattyork.jarbn.AsyncTasks;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

import com.mattyork.jarbn.BNManager;
import com.mattyork.jarbn.BNObjects.BNComment;
import com.mattyork.jarbn.BNObjects.BNPost;

public class LoadCommentsFromPostAsyncTask extends
		AsyncTask<BNPost, Void, ArrayList<BNComment>> {
	
	String url;
	BNPost mHnPost;

	public LoadCommentsFromPostAsyncTask(BNPost mHnPost) {
		super();
		this.mHnPost = mHnPost;
		
		this.url = "http://boredbanker.com/item?id="+mHnPost.PostId;
	}



	@Override
	protected ArrayList<BNComment> doInBackground(BNPost... params) {
		// TODO Auto-generated method stub
		
		HttpGet httpGet = new HttpGet(url);

		String htmlResponseString = "";

		HttpResponse response;
		try {
			response = BNManager.getInstance().Service.client.execute(httpGet);
			htmlResponseString = EntityUtils.toString(response.getEntity());

			ArrayList<BNComment> comments = BNComment
					.parsedCommentsFromHTML(htmlResponseString, mHnPost);

			return comments;

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
