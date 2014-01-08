package com.mattyork.bankernews.Fragments;

import com.mattyork.bankernews.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class StoriesListViewFragment extends Fragment implements OnItemClickListener{

	private ListView postsListView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		//inflate the view for the fragment.
		View inflatedView = inflater.inflate(R.layout.fragment_posts,container, false);
		postsListView = (ListView) inflatedView.findViewById(R.id.postsListView);
		return inflatedView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		postsListView.setOnItemClickListener(this);
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

		//If portrait open
		
		//If landscape
		
	}

}
