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

public class CommentsFragment extends Fragment implements OnItemClickListener,
		OnRefreshListener {

	private static final String BANKERNEWS_COMMENTS = "com.mattyork.bankernews.Fragments Comments";
	ArrayList<BNComment> comments = new ArrayList<BNComment>();
	ListView mCommentsListView;
	PullToRefreshAttacher mPullToRefreshAttacher;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPullToRefreshAttacher = ((LinkCommentsActivity) getActivity())
				.getmPullToRefreshAttacher();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_comments, container,
				false);
		setupListView(view);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (savedInstanceState != null) {
			comments = savedInstanceState.getParcelableArrayList(BANKERNEWS_COMMENTS);
			addCommentsToListView(comments);
		} else {
			getComments();
		}
	}
	
	
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelableArrayList(BANKERNEWS_COMMENTS, comments);
	}

	private void setupListView(View view) {
		// Setup listview
		mCommentsListView = (ListView) view.findViewById(R.id.CommentsListView);
		mCommentsListView.setOnItemClickListener(this);
		mCommentsListView.setFastScrollEnabled(true);

		// Add the Refreshable View and provide the refresh listener
		if (mPullToRefreshAttacher == null) {
			mPullToRefreshAttacher = ((LinkCommentsActivity) getActivity())
					.getmPullToRefreshAttacher();
		}
		mPullToRefreshAttacher.addRefreshableView(mCommentsListView, this);
	}

	private void getComments() {
		LoadCommentsFromPostAsyncTask task = new LoadCommentsFromPostAsyncTask(
				MainActivity.selectedPost) {
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				mPullToRefreshAttacher.setRefreshing(true);
			}

			@Override
			protected void onPostExecute(ArrayList<BNComment> result) {
				super.onPostExecute(result);
				// Reset pull to refresh
				comments = result;
				mPullToRefreshAttacher.setRefreshComplete();
				if (result != null && getActivity() != null) {
					addCommentsToListView(comments);
				}
			}
		};
		task.execute();
	}

	protected void addCommentsToListView(ArrayList<BNComment> comments) {
		mCommentsListView.setAdapter(new CommentCellAdapter(
				getActivity(), getThemedCellLayoutId(), comments));		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// Set selected post

	}

	private int getThemedCellLayoutId() {
		if (SettingsManager.getInstance().usingNightMode) {
			return R.layout.comment_night_cell;
		} else {
			return R.layout.comment_day_cell;
		}
	}

	@Override
	public void onRefreshStarted(View view) {
		// TODO Auto-generated method stub
		getComments();
	}
}
