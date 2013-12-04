package com.mattyork.bankernews.Adapters;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mattyork.bankernews.R;
import com.mattyork.bankernews.Activities.LinkCommentsActivity;
import com.mattyork.bankernews.Activities.MainActivity;
import com.mattyork.bankernews.Helpers.SettingsManager;
import com.mattyork.jarbn.BNObjects.BNPost;
import com.mattyork.jarbn.BNObjects.BNPost.PostType;

public class PostsCellAdapter extends ArrayAdapter<BNPost> {

	ArrayList<BNPost> posts;
	int layoutResourceId;
	private Context context;
	LayoutInflater inflater;

	TextView mPostTitleTextView;
	TextView mPointsTextView;
	TextView mDateCreatedTextView;
	TextView mCommentCountTextView;
	ImageView mCommentImageView;
	LinearLayout mFooterLinearLayout;

	public PostsCellAdapter(Context context, int resource,
			ArrayList<BNPost> posts) {
		super(context, resource, posts);
		// TODO Auto-generated constructor stub

		// Make assignments
		this.posts = posts;
		this.layoutResourceId = resource;
		this.context = context;
		this.inflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return this.posts.size();
	}

	public BNPost getItem(int position) throws IndexOutOfBoundsException {
		return this.posts.get(position);
	}

	public long getItemId(int position) throws IndexOutOfBoundsException {
		if (position < getCount() && position >= 0) {
			return position;
		}

		return 0;
	}

	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) { // If the View is not cached
			// Inflates the Common View from XML file
			convertView = this.inflater
					.inflate(layoutResourceId, parent, false);

			// convertView = this.inflater.inflate(R.id.custom_cell, null);
		}

		// Set title
		mPostTitleTextView = (TextView) convertView
				.findViewById(R.id.postCellTitleTextView);
		mPostTitleTextView.setText(posts.get(position).Title);

		// Set points
		mPointsTextView = (TextView) convertView
				.findViewById(R.id.postCellPointsTextView);
		mPointsTextView.setText(posts.get(position).Points + " Points");

		// Set date created
		mDateCreatedTextView = (TextView) convertView
				.findViewById(R.id.postCellDateCreatedTextView);
		mDateCreatedTextView.setText(posts.get(position).TimeCreatedString);

		//Set comment count
		mCommentCountTextView = (TextView)convertView.findViewById(R.id.postCellCommentCountTextView);
		mCommentCountTextView.setText(""+posts.get(position).CommentCount);
		
		//Set frame layout
		mCommentImageView = (ImageView)convertView.findViewById(R.id.postCellCommentBubbleImageView);
		mCommentImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MainActivity.selectedPost = posts.get(position);
				
				//Build intent and start activity
				Intent i = new Intent(context, LinkCommentsActivity.class);
				i.putExtra("url", posts.get(position).UrlString);
				i.putExtra("selectedContent", 1);
				context.startActivity(i);
			}
		});
		
		//Connect footer
		mFooterLinearLayout = (LinearLayout)convertView.findViewById(R.id.PostCellFooterLinearLayout);
		
		//Set background color
		if (posts.get(position).Type == PostType.PostTypeShowHN) {
			if (SettingsManager.getInstance().usingNightMode) {
				convertView.setBackgroundColor(context.getResources().getColor(R.color.ShowHNOrange));
				mPostTitleTextView.setTextColor(context.getResources().getColor(R.color.LightTextColor));
				mFooterLinearLayout.setBackgroundColor(context.getResources().getColor(R.color.ShowHNFooterOrange));
			}
			else {
				convertView.setBackgroundColor(context.getResources().getColor(R.color.ShowHNOrangeDay));
				//mPostTitleTextView.setTextColor(context.getResources().getColor(R.color.LightTextColor));
				mFooterLinearLayout.setBackgroundColor(context.getResources().getColor(R.color.ShowHNFooterOrangeDay));
			}
		} else if (posts.get(position).Type == PostType.PostTypeJobs) {
			if (SettingsManager.getInstance().usingNightMode) {
				convertView.setBackgroundColor(context.getResources().getColor(R.color.JobsHNGreen));
				mPostTitleTextView.setTextColor(context.getResources().getColor(R.color.LightTextColor));
				mFooterLinearLayout.setBackgroundColor(context.getResources().getColor(R.color.JobsHNFooterGreen));
			}
			else {
				convertView.setBackgroundColor(context.getResources().getColor(R.color.JobsHNGreenDay));
				//mPostTitleTextView.setTextColor(context.getResources().getColor(R.color.LightTextColor));
				mFooterLinearLayout.setBackgroundColor(context.getResources().getColor(R.color.JobsHNFooterGreenDay));
			}
		} else if (posts.get(position).Type == PostType.PostTypeDefault) {
			if (SettingsManager.getInstance().usingNightMode) {
				convertView.setBackgroundColor(context.getResources().getColor(R.color.BackgroundDarkGrey));
				mFooterLinearLayout.setBackgroundColor(context.getResources().getColor(R.color.PostCellLightGrey));
			}
			else {
				convertView.setBackgroundColor(context.getResources().getColor(R.color.PostCellDayBackground));
				mFooterLinearLayout.setBackgroundColor(context.getResources().getColor(R.color.PostCellDayFooterBackgroud));
			}
		}

		return convertView;
	}

}
