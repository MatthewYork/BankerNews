package com.mattyork.bankernews.Fragments;

import java.util.ArrayList;

import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher.OnRefreshListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.mattyork.bankernews.R;
import com.mattyork.bankernews.Activities.LinkCommentsActivity;
import com.mattyork.bankernews.Activities.MainActivity;
import com.mattyork.bankernews.Adapters.CommentCellAdapter;
import com.mattyork.bankernews.Helpers.SettingsManager;
import com.mattyork.jarbn.AsyncTasks.LoadCommentsFromPostAsyncTask;
import com.mattyork.jarbn.BNObjects.BNComment;

public class CommentsFragment extends Fragment implements OnItemClickListener, OnRefreshListener{
	
	ArrayList<BNComment> comments = new ArrayList<BNComment>();
	ListView mCommentsListView;
	PullToRefreshAttacher mPullToRefreshAttacher;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mPullToRefreshAttacher = ((LinkCommentsActivity)getActivity()).getmPullToRefreshAttacher();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_comments, null);
		
		setupListView(view);
		
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		
		//Fetch comments
		getComments();
	}
	
	private void setupListView(View view) {
		//Setup listview
				mCommentsListView = (ListView)view.findViewById(R.id.CommentsListView);
				mCommentsListView.setOnItemClickListener(this);
				mCommentsListView.setFastScrollEnabled(true);
				
			    // Add the Refreshable View and provide the refresh listener
			    mPullToRefreshAttacher.addRefreshableView(mCommentsListView, this);
	}
	
	private void getComments() {
		LoadCommentsFromPostAsyncTask task = new LoadCommentsFromPostAsyncTask(MainActivity.selectedPost){

			
			
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				
				mPullToRefreshAttacher.setRefreshing(true);
			}

			@Override
			protected void onPostExecute(ArrayList<BNComment> result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				
				//Reset pull to refresh
				mPullToRefreshAttacher.setRefreshComplete();
				
				if(result != null && getActivity() != null){
					mCommentsListView.setAdapter(new CommentCellAdapter(getActivity(), getThemedCellLayoutId(), result));
				}
			}
		};
		task.execute();
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		//Set selected post
		
	}
	
	private int getThemedCellLayoutId() {
		if (SettingsManager.getInstance().usingNightMode) {
			return R.layout.comment_night_cell;
		}
		else {
			return R.layout.comment_day_cell;
		}
	}

	@Override
	public void onRefreshStarted(View view) {
		// TODO Auto-generated method stub
		getComments();
	}
}
