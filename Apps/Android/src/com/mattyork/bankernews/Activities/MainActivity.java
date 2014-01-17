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

import com.mattyork.bankernews.R;
import com.mattyork.bankernews.Adapters.PostsCellAdapter;
import com.mattyork.bankernews.Fragments.LeftMenuFragment;
import com.mattyork.bankernews.Helpers.SettingsManager;
import com.mattyork.bankernews.interfaces.OnLeftMenuSettingChangedListener;
import com.mattyork.jarbn.BNWebService.PostFilterType;
import com.mattyork.jarbn.AsyncTasks.LoadPostsWithFilterAsyncTask;
import com.mattyork.jarbn.BNObjects.BNPost;
import com.twotoasters.jazzylistview.JazzyGridView;
import com.twotoasters.jazzylistview.JazzyHelper;

public class MainActivity extends FragmentActivity implements
		OnItemClickListener, OnLeftMenuSettingChangedListener,
		OnRefreshListener {

	private static final String BANKER_NEWS_POSTS = "com.mattyork.bankernews.Activities posts";
	private static final String BANKER_NEWS_TITLE = "com.mattyork.bankernews.Activities title";
	DrawerLayout drawerLayout;
	ActionBarDrawerToggle drawerToggle;
	JazzyGridView postsGridView;
	ArrayList<BNPost> posts = new ArrayList<BNPost>();
	LeftMenuFragment leftMenuFragment;
	PullToRefreshAttacher mPullToRefreshAttacher;
	public static BNPost selectedPost;
	private int mCurrentTransitionEffect = JazzyHelper.SLIDE_IN;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Load default settings
		SettingsManager.getInstance().loadSettingsFromSharedPreferences(this);

		

		// Setup left menu
		setupLeftMenu();

		// Customize UI
		setLayoutBackgroundColor();

		// Customize action bar
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setTitle(R.string.content_top);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// Setup postsTable
		setupPostsListView();

		// Fetch top posts
		if (savedInstanceState != null) {
			posts = savedInstanceState
					.getParcelableArrayList(BANKER_NEWS_POSTS);
			SetPostsListView(posts);
			getActionBar().setTitle(
					savedInstanceState.getString(BANKER_NEWS_TITLE));

		} else {
			getPostsWithFilterType(PostFilterType.PostFilterTypeTop);
		}

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelableArrayList(BANKER_NEWS_POSTS, posts);
		outState.putString(BANKER_NEWS_TITLE, getActionBar().getTitle()
				.toString());
	}

	private void setLayoutBackgroundColor() {
		if (SettingsManager.getInstance().usingNightMode) {
			drawerLayout.setBackgroundColor(getResources().getColor(
					R.color.BackgroundDarkGrey));
		} else {
			drawerLayout.setBackgroundColor(getResources().getColor(
					R.color.PostCellDayBackground));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void getPostsWithFilterType(PostFilterType type) {
		LoadPostsWithFilterAsyncTask task = new LoadPostsWithFilterAsyncTask(
				type) {
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				mPullToRefreshAttacher.setRefreshing(true);
			}

			@Override
			protected void onPostExecute(java.util.ArrayList<BNPost> result) {
				posts = result;
				// Reset pull to refresh
				mPullToRefreshAttacher.setRefreshComplete();
				SetPostsListView(posts);
				if (posts != null && this != null) {

				}

				return;
			};
		};
		task.execute();
	}

	// Adds posts to the listview
	protected void SetPostsListView(ArrayList<BNPost> posts) {
		postsGridView.setAdapter(new PostsCellAdapter(MainActivity.this,getThemedCellLayoutId(), posts));
	}

	public void setupPostsListView() {
		postsGridView = (JazzyGridView) findViewById(R.id.PostsListView);
		setupJazziness(mCurrentTransitionEffect);
		postsGridView.setOnItemClickListener(this);

		// Create a PullToRefreshAttacher instance
		mPullToRefreshAttacher = PullToRefreshAttacher.get(this);

		// Add the Refreshable View and provide the refresh listener
		mPullToRefreshAttacher.addRefreshableView(postsGridView, this);
	}

	private void setupJazziness(int effect) {
		mCurrentTransitionEffect = effect;
		postsGridView.setTransitionEffect(mCurrentTransitionEffect);
	}

	public void setupLeftMenu() {
		leftMenuFragment = (LeftMenuFragment) getSupportFragmentManager()
				.findFragmentById(R.id.leftMenuFragment);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
		drawerToggle = createDrawerToggle();
		drawerLayout.setDrawerListener(drawerToggle);
	}

	private ActionBarDrawerToggle createDrawerToggle() {
		return new ActionBarDrawerToggle(this, drawerLayout,
				R.drawable.ic_drawer, R.string.app_name, R.string.app_name) {

			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);

			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);

			}

		};
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub

		switch (item.getItemId()) {
		case android.R.id.home:
			// Toggle sliding layer
			if (drawerLayout.isDrawerOpen(Gravity.START)) {
				drawerLayout.closeDrawer(Gravity.START);
			} else {
				drawerLayout.openDrawer(Gravity.START);
			}
			break;

		default:
			break;
		}

		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		// Set selected post
		selectedPost = posts.get(position);

		// Build intent and start activity
		Intent i = new Intent(this, LinkCommentsActivity.class);
		i.putExtra("url", posts.get(position).UrlString);
		i.putExtra("selectedPostType", posts.get(position).Type.ordinal());
		i.putExtra("selectedContent", 0);
		this.startActivity(i);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}

	public void didSelectFilterButton(View v) {
		leftMenuFragment.didSelectFilterButton(v);

	}

	@Override
	public void didSelectFilterPosts(PostFilterType type) {
		drawerLayout.closeDrawer(Gravity.START);
		// Clear posts
		postsGridView.setAdapter(new PostsCellAdapter(MainActivity.this,
				getThemedCellLayoutId(), new ArrayList<BNPost>()));
		// Get new posts
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
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
	}

	private int getThemedCellLayoutId() {
		if (SettingsManager.getInstance().usingNightMode) {
			return R.layout.post_night_cell;
		} else {
			return R.layout.post_day_cell;
		}
	}

	private void refreshTable() {
		// Clear posts
		if (postsGridView != null) {
			postsGridView.setAdapter(new PostsCellAdapter(MainActivity.this,
					getThemedCellLayoutId(), posts));
			setLayoutBackgroundColor();
		}
	}

	@Override
	public void didSelectChangeTheme() {
		refreshTable();
	}

}
