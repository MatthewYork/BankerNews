package com.mattyork.bankernews.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.mattyork.bankernews.R;
import com.mattyork.bankernews.Activities.LinkCommentsActivity;
import com.mattyork.bankernews.Helpers.SettingsManager;

public class LinkFragment extends Fragment {

	private static final String BANKER_NEWS_READABILITY = "com.mattyork.bankernews.Fragments usingReadability";
	WebView linkWebView;
	ProgressBar linkProgressBar;
	private boolean usingReadability = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

		// Set readability from settings
		usingReadability = SettingsManager.getInstance().usingReadability;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.fragment_link, null);
		linkProgressBar = (ProgressBar) view.findViewById(R.id.linkProgressBar);

		// Setup link web view
		setupWebView(view, savedInstanceState);

		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// Save the state of the WebView
		linkWebView.saveState(outState);
		outState.putBoolean(BANKER_NEWS_READABILITY,usingReadability);
	}

	public void setupWebView(View view, Bundle inState) {
		linkWebView = (WebView) view.findViewById(R.id.linkWebView);
		linkWebView.getSettings().setJavaScriptEnabled(true);
		linkWebView.getSettings().setSupportZoom(true);
		linkWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		linkWebView.setScrollbarFadingEnabled(true);
		linkWebView.getSettings().setLoadsImagesAutomatically(true);
		if (inState != null) {
			// Restore the state of the WebView
			linkWebView.restoreState(inState);
			usingReadability = inState.getBoolean(BANKER_NEWS_READABILITY);
		} else {
			setLinkUrl(LinkCommentsActivity.selectedLinkUrlString);
		}

		linkWebView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				linkProgressBar.setVisibility(View.GONE);
				linkWebView.setVisibility(View.VISIBLE);
			}

		});
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	public void setLinkUrl(String linkUrl) {
		linkWebView.loadUrl(linkUrl);
		linkProgressBar.setVisibility(View.VISIBLE);
		linkWebView.setVisibility(View.GONE);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {

		case R.id.menu_nav_readability:
			toggleReadability();
			break;
		default:
			break;
		}

		return true;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		getActivity().getMenuInflater().inflate(R.menu.readability_menu, menu);
	}

	private void toggleReadability() {
		usingReadability = !usingReadability;

		// TODO Auto-generated method stub
		if (((LinkCommentsActivity) getActivity()).formattedUrl
				.contains("http")) {
			if (usingReadability) {
				setLinkUrl("http://www.readability.com/m?url="
						+ ((LinkCommentsActivity) getActivity()).formattedUrl);
			} else {
				setLinkUrl(((LinkCommentsActivity) getActivity()).formattedUrl);
			}
		}
	}

}
