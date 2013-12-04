package com.mattyork.bankernews.Activities;

import java.util.ArrayList;

import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher.OnRefreshListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.mattyork.bankernews.R;
import com.mattyork.bankernews.Adapters.PostsCellAdapter;
import com.mattyork.bankernews.Fragments.LeftMenuFragment;
import com.mattyork.bankernews.Fragments.LeftMenuFragment.OnLeftMenuSettingChangedListener;
import com.mattyork.bankernews.Helpers.SettingsManager;
import com.mattyork.jarbn.BNWebService.PostFilterType;
import com.mattyork.jarbn.AsyncTasks.LoadPostsWithFilterAsyncTask;
import com.mattyork.jarbn.BNObjects.BNPost;

public class MainActivity extends FragmentActivity implements OnItemClickListener, OnLeftMenuSettingChangedListener, OnRefreshListener {
	
	DrawerLayout drawerLayout;
	ActionBarDrawerToggle drawerToggle;
	ListView postsListView;
	ArrayList<BNPost> posts = new ArrayList<BNPost>();
	LeftMenuFragment leftMenuFragment;
	PullToRefreshAttacher mPullToRefreshAttacher;
	public static BNPost selectedPost;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Load default settings
		SettingsManager.getInstance().loadSettingsFromSharedPreferences(this);
		
		setContentView(R.layout.activity_main);
		
		//Setup left menu
		setupLeftMenu();
		
		//Customize UI
		buildUI();
		
		//Customize action bar
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setTitle(R.string.content_top);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		//Setup postsTable
		setupPostsListView();
		
		//Fetch top posts
		getPostsWithFilterType(PostFilterType.PostFilterTypeTop);
		
	}
	
	private void buildUI() {
		if (SettingsManager.getInstance().usingNightMode) {
			drawerLayout.setBackgroundColor(getResources().getColor(R.color.BackgroundDarkGrey));
		}
		else {
			drawerLayout.setBackgroundColor(getResources().getColor(R.color.PostCellDayBackground));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void getPostsWithFilterType(PostFilterType type) {
		
		LoadPostsWithFilterAsyncTask task = new LoadPostsWithFilterAsyncTask(type){
			
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				
				mPullToRefreshAttacher.setRefreshing(true);
			}

			@Override
			protected void onPostExecute(java.util.ArrayList<BNPost> result) {
				posts = result;

				//Reset pull to refresh
				mPullToRefreshAttacher.setRefreshComplete();
				
				if (posts != null && this != null) {
					postsListView.setAdapter(new PostsCellAdapter(MainActivity.this, getThemedCellLayoutId(), posts));
				}
				
				 return;
			};
		};
		task.execute();
	}
	
	public void setupPostsListView() {
		postsListView = (ListView)findViewById(R.id.PostsListView);
		postsListView.setOnItemClickListener(this);
		
		// Create a PullToRefreshAttacher instance
	    mPullToRefreshAttacher = PullToRefreshAttacher.get(this);

	    // Add the Refreshable View and provide the refresh listener
	    mPullToRefreshAttacher.addRefreshableView(postsListView, this);
	}
	
	public void setupLeftMenu() {
		leftMenuFragment = (LeftMenuFragment)getSupportFragmentManager().findFragmentById(R.id.leftMenuFragment);
		
		drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
		drawerToggle = createDrawerToggle();
		drawerLayout.setDrawerListener(drawerToggle);
		
	}
	
	private ActionBarDrawerToggle createDrawerToggle() {
		return new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, R.string.app_name, R.string.app_name){

			@Override
			public void onDrawerClosed(View drawerView) {
				// TODO Auto-generated method stub
				super.onDrawerClosed(drawerView);
				
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				// TODO Auto-generated method stub
				super.onDrawerOpened(drawerView);
				
			}
			
		};
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		
		switch (item.getItemId()) {
		case android.R.id.home:
			//Toggle sliding layer
			if (drawerLayout.isDrawerOpen(Gravity.START)) {
				drawerLayout.closeDrawer(Gravity.START);
			}
			else {
				drawerLayout.openDrawer(Gravity.START);
			}
			break;

		default:
			break;
		}
		
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		//Set selected post
		selectedPost = posts.get(position);
		
		//Build intent and start activity
		Intent i = new Intent(this, LinkCommentsActivity.class);
		i.putExtra("url", posts.get(position).UrlString);
		i.putExtra("selectedPostType", posts.get(position).Type.ordinal());
		i.putExtra("selectedContent", 0);
		this.startActivity(i);
	}
	
	public void didSelectFilterButton(View v) {
		leftMenuFragment.didSelectFilterButton(v);
		
	}

	@Override
	public void didSelectFilterPosts(PostFilterType type) {
		// TODO Auto-generated method stub
		
		drawerLayout.closeDrawer(Gravity.START);
		
		//Clear posts
		postsListView.setAdapter(new PostsCellAdapter(MainActivity.this, getThemedCellLayoutId(), new ArrayList<BNPost>()));
		
		//Get new posts
		SettingsManager.getInstance().currentPostFilterType = type;
		getPostsWithFilterType(type);
	}

	@Override
	public void onRefreshStarted(View view) {
		// TODO Auto-generated method stub
		getPostsWithFilterType(SettingsManager.getInstance().currentPostFilterType);
		
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onPostCreate(savedInstanceState);
		
		drawerToggle.syncState();
	}
	
	private int getThemedCellLayoutId() {
		if (SettingsManager.getInstance().usingNightMode) {
			return R.layout.post_night_cell;
		}
		else {
			return R.layout.post_day_cell;
		}
	}
	
	private void refreshTable(){
		//Clear posts
		if (postsListView != null) {
			postsListView.setAdapter(new PostsCellAdapter(MainActivity.this, getThemedCellLayoutId(), posts));
		}
	}

	@Override
	public void didSelectChangeTheme() {
		// TODO Auto-generated method stub
		refreshTable();
	}
	

}
