package com.mattyork.bankernews.Fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mattyork.bankernews.R;
import com.mattyork.bankernews.Helpers.SettingsManager;
import com.mattyork.bankernews.interfaces.OnLeftMenuSettingChangedListener;
import com.mattyork.jarbn.BNWebService.PostFilterType;

public class LeftMenuFragment extends Fragment implements OnClickListener {

	private static final String BANKER_NEWS_FILTER_HOMEPAGE = "com.mattyork.bankernews.Fragments id";
	private int FilterHomePageId = -1;
	// Top Filter Buttons
	private Button mTopFilterButton;
	private Button mAskFilterButton;
	private Button mNewFilterButton;
	private Button mJobsFilterButton;
	private Button mBestFilterButton;
	private ArrayList<Button> buttonsArrayList = new ArrayList<Button>(); // Makes
																			// changing
																			// them
																			// easier
	// HNProfile
	private LinearLayout mHNProfileLinearLayout;
	private TextView mHNProfileUsernameTextView;
	private TextView mHNProfileKarmaTextView;
	private TextView mHNProfileMySubmissionsTextView;
	private TextView mHNProfileLogout;

	// Login
	private LinearLayout mLoginLinearLayout;
	private EditText mLoginUsernameEditText;
	private EditText mLoginPasswordEditText;
	private TextView mLoginLoginTextView;

	// Settings Linear Layouts
	private LinearLayout mReadabilityLinearLayout;
	private LinearLayout mMarkAsReadLinearLayout;
	private LinearLayout mThemeLinearLayout;
	private ImageView mReadabilityImageView;
	private ImageView mMarkAsReadImageView;
	private ImageView mThemeImageView;
	private TextView mReadabilityTextView;
	private TextView mMarkAsReadTextView;
	private TextView mThemeTextView;

	// Share Linear Layout
	private ImageView mFacebookImageView;
	private ImageView mTwitterImageView;
	private ImageView mEmailImageView;

	// Credits
	private LinearLayout mMattGithubLinearLayout;
	private LinearLayout mMattTwitterLinearLayout;
	private LinearLayout mAaronGithubTwitterLinearLayout;

	// Donation
	private LinearLayout mBitcoinLinearLayout;

	OnLeftMenuSettingChangedListener mCallbackLeftMenuSettingChangedListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);

		try {
			mCallbackLeftMenuSettingChangedListener = (OnLeftMenuSettingChangedListener) activity;
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_left_menu, container);

		// Setup UI
		setupLogin(view);
		setupFilterButtons(view,savedInstanceState);
		setupSettings(view);
		setupShareButtons(view);
		setupCreditsButtons(view);
		setupDonationButtons(view);
		return view;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(BANKER_NEWS_FILTER_HOMEPAGE, FilterHomePageId);
	}

	private void setupDonationButtons(View view) {
		// TODO Auto-generated method stub
		mBitcoinLinearLayout = (LinearLayout) view
				.findViewById(R.id.LeftMenuBitcoinLinearLayout);
		mBitcoinLinearLayout.setOnClickListener(this);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	private void setupFilterButtons(View view, Bundle inState) {
		mTopFilterButton = (Button) view
				.findViewById(R.id.leftMenuTopFilterButton);
		// mAskFilterButton = (Button) view
		// .findViewById(R.id.leftMenuAskFilterButton);
		mNewFilterButton = (Button) view
				.findViewById(R.id.leftMenuNewFilterButton);
		// mJobsFilterButton = (Button) view
		// .findViewById(R.id.leftMenuJobsFilterButton);
		mBestFilterButton = (Button) view
				.findViewById(R.id.leftMenuBestFilterButton);

		// Add buttons to list
		buttonsArrayList.add(mTopFilterButton);
		// buttonsArrayList.add(mAskFilterButton);
		buttonsArrayList.add(mNewFilterButton);
		// buttonsArrayList.add(mJobsFilterButton);
		buttonsArrayList.add(mBestFilterButton);

		// Default to the "Top" filter button being selected
		if(inState!=null){
			FilterHomePageId = inState.getInt(BANKER_NEWS_FILTER_HOMEPAGE);
		}else{
			FilterHomePageId = mTopFilterButton.getId();
		}
		setButtonSelected(FilterHomePageId);
	}

	private void setupSettings(View view) {
		// Setup Linear Layouts
		mReadabilityLinearLayout = (LinearLayout) view
				.findViewById(R.id.leftMenuSettingsReadabilityLinearLayout);
		mMarkAsReadLinearLayout = (LinearLayout) view
				.findViewById(R.id.leftMenuSettingsMarkAsReadLinearLayout);
		mThemeLinearLayout = (LinearLayout) view
				.findViewById(R.id.leftMenuSettingsThemeLinearLayout);

		// Setup ImageViews
		mReadabilityImageView = (ImageView) view
				.findViewById(R.id.leftMenuSettingsReadabilityImageView);
		mMarkAsReadImageView = (ImageView) view
				.findViewById(R.id.leftMenuSettingsMarkAsReadImageView);
		mThemeImageView = (ImageView) view
				.findViewById(R.id.leftMenuSettingsThemeImageView);

		// Setup TextViews
		mReadabilityTextView = (TextView) view
				.findViewById(R.id.leftMenuSettingsReadabilityTextView);
		mMarkAsReadTextView = (TextView) view
				.findViewById(R.id.leftMenuSettingsMarkAsReadTextView);
		mThemeTextView = (TextView) view
				.findViewById(R.id.leftMenuSettingsThemeTextView);

		// Add onClickListeners
		mReadabilityLinearLayout.setOnClickListener(this);
		mMarkAsReadLinearLayout.setOnClickListener(this);
		mThemeLinearLayout.setOnClickListener(this);

		// Setup UI
		setInitialSettingsUI();
	}

	private void setupShareButtons(View view) {
		// Setup ImageViews
		mFacebookImageView = (ImageView) view
				.findViewById(R.id.leftMenuFacebookImageView);
		mTwitterImageView = (ImageView) view
				.findViewById(R.id.leftMenuTwitterImageView);
		mEmailImageView = (ImageView) view
				.findViewById(R.id.leftMenuEmailImageView);

		// Add on-click listeners
		mFacebookImageView.setOnClickListener(this);
		mTwitterImageView.setOnClickListener(this);
		mEmailImageView.setOnClickListener(this);
	}

	private void setupCreditsButtons(View view) {
		mMattGithubLinearLayout = (LinearLayout) view
				.findViewById(R.id.LeftMenuCreditsMattGithub);
		mMattGithubLinearLayout.setOnClickListener(this);
		mMattTwitterLinearLayout = (LinearLayout) view
				.findViewById(R.id.LeftMenuCreditsMattTwitter);
		mMattTwitterLinearLayout.setOnClickListener(this);
		mAaronGithubTwitterLinearLayout = (LinearLayout) view
				.findViewById(R.id.LeftMenuCreditsAaron);
		mAaronGithubTwitterLinearLayout.setOnClickListener(this);
	}

	private void setButtonSelected(int selectedButtonId) {
		FilterHomePageId = selectedButtonId;
		for (Button button : buttonsArrayList) {
			if (button.getId() == selectedButtonId) {
				button.setTextColor(getResources().getColor(R.color.bn_green));
			} else {
				button.setTextColor(getResources().getColor(
						R.color.LeftMenuTextColor));
			}
		}
	}

	public void didSelectFilterButton(View v) {
		setButtonSelected(v.getId());

		switch (v.getId()) {
		case R.id.leftMenuTopFilterButton:
			this.mCallbackLeftMenuSettingChangedListener
					.didSelectFilterPosts(PostFilterType.PostFilterTypeTop);
			getActivity().getActionBar().setTitle(R.string.content_top);
			break;
		// case R.id.leftMenuAskFilterButton:
		// this.mCallbackLeftMenuSettingChangedListener
		// .didSelectFilterPosts(PostFilterType.PostFilterTypeAsk);
		// getActivity().getActionBar().setTitle(R.string.content_ask);
		// break;
		case R.id.leftMenuNewFilterButton:
			this.mCallbackLeftMenuSettingChangedListener
					.didSelectFilterPosts(PostFilterType.PostFilterTypeNew);
			getActivity().getActionBar().setTitle(R.string.content_new);
			break;
		case R.id.leftMenuJobsFilterButton:
			this.mCallbackLeftMenuSettingChangedListener
					.didSelectFilterPosts(PostFilterType.PostFilterTypeJobs);
			getActivity().getActionBar().setTitle(R.string.content_jobs);
			break;
		case R.id.leftMenuBestFilterButton:
			this.mCallbackLeftMenuSettingChangedListener
					.didSelectFilterPosts(PostFilterType.PostFilterTypeBest);
			getActivity().getActionBar().setTitle(R.string.content_best);
			break;
		default:
			break;
		}
	}

	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.leftMenuSettingsReadabilityLinearLayout:
			SettingsManager.getInstance().setusingReadability(
					!SettingsManager.getInstance().usingReadability);
			setReadabilityUI();
			break;
		case R.id.leftMenuSettingsMarkAsReadLinearLayout:
			SettingsManager.getInstance().setUsingMarkAsRead(
					!SettingsManager.getInstance().usingMarkAsRead);
			setMarkAsReadUI();
			break;
		case R.id.leftMenuSettingsThemeLinearLayout:
			SettingsManager.getInstance().setUsingNightMode(
					!SettingsManager.getInstance().usingNightMode);
			setThemeUI();
			break;
		case R.id.LeftMenuHNProfileMySubmissionsTextView:

			break;
		case R.id.LeftMenuHNProfileLogoutTextView:

			break;
		case R.id.LeftMenuLoginLoginTextView:

			break;
		case R.id.LeftMenuCreditsMattGithub:
			goToWebsite("https://github.com/MatthewYork");
			break;
		case R.id.LeftMenuCreditsMattTwitter:
			goToTwitterUser("TheMattYork");
			break;
		case R.id.LeftMenuCreditsAaron:
			goToWebsite("https://github.com/adfleshner");
			break;
		case R.id.LeftMenuBitcoinLinearLayout:
			copyBitcoinAddress();
			break;
		default:
			break;
		}

	}
	
	private void goToWebsite(String url){
		Intent browserIntentAaron = new Intent(Intent.ACTION_VIEW,
				Uri.parse(url));
		startActivity(browserIntentAaron);
	}

	private void goToTwitterUser(String username) {
		try {
			startActivity(new Intent(Intent.ACTION_VIEW,
					Uri.parse("twitter://user?screen_name=" + username)));
		} catch (Exception e) {
			// If Twitter app is not installed, start browser.
			startActivity(new Intent(Intent.ACTION_VIEW,
					Uri.parse("https://twitter.com/" + username)));
		}
	}

	public void setInitialSettingsUI() {
		setReadabilityUI();
		setMarkAsReadUI();
		setThemeUI();
	}

	// ================================================================================
	// Profile Methods
	// ================================================================================

	private void setupHNProfile(View view) {
		// Setup connections to layout
		mHNProfileLinearLayout = (LinearLayout) view
				.findViewById(R.id.LeftMenuHNProfileLinearLayout);
		mHNProfileUsernameTextView = (TextView) view
				.findViewById(R.id.LeftMenuHNProfileUsernameTextView);
		mHNProfileKarmaTextView = (TextView) view
				.findViewById(R.id.LeftMenuHNProfileKarmaTextView);
		mHNProfileMySubmissionsTextView = (TextView) view
				.findViewById(R.id.LeftMenuHNProfileMySubmissionsTextView);
		mHNProfileLogout = (TextView) view
				.findViewById(R.id.LeftMenuHNProfileLogoutTextView);

		// Set content

		// Add listeners
		mHNProfileMySubmissionsTextView.setOnClickListener(this);
		mHNProfileLogout.setOnClickListener(this);
	}

	private void didSelectMySubmissions() {
		// TODO Auto-generated method stub

	}

	private void didSelectLogout() {
		// TODO Auto-generated method stub

	}

	// ================================================================================
	// Profile Methods
	// ================================================================================

	private void setupLogin(View view) {
		// Setup connections to layout
		mLoginLinearLayout = (LinearLayout) view
				.findViewById(R.id.LeftMenuLoginLinearLayout);
		mLoginUsernameEditText = (EditText) view
				.findViewById(R.id.leftMenuLoginUsernameEditText);
		mLoginPasswordEditText = (EditText) view
				.findViewById(R.id.leftMenuLoginPasswordEditText);
		mLoginLoginTextView = (TextView) view
				.findViewById(R.id.LeftMenuLoginLoginTextView);

		// Set content

		// Add listeners
		mLoginLoginTextView.setOnClickListener(this);
	}

	private void didSelectLogin() {

	}

	// ================================================================================
	// Settings Methods
	// ================================================================================

	private void setReadabilityUI() {
		if (SettingsManager.getInstance().usingReadability) {
			mReadabilityImageView
					.setImageResource(R.drawable.nav_readability_on);
			mReadabilityTextView.setText("Readability is ON");
		} else {
			mReadabilityImageView
					.setImageResource(R.drawable.nav_readability_off);
			mReadabilityTextView.setText("Readability is OFF");
		}
	}

	private void setMarkAsReadUI() {
		if (SettingsManager.getInstance().usingMarkAsRead) {
			mMarkAsReadImageView.setImageResource(R.drawable.nav_markasread_on);
			mMarkAsReadTextView.setText("Mark as Read is ON");
		} else {
			mMarkAsReadImageView
					.setImageResource(R.drawable.nav_markasread_off);
			mMarkAsReadTextView.setText("Mark as Read is OFF");
		}
	}

	private void setThemeUI() {
		// Callback to the activity to change the theme
		if (mCallbackLeftMenuSettingChangedListener != null) {
			mCallbackLeftMenuSettingChangedListener.didSelectChangeTheme();
		}

		// Update left fragment UI
		if (SettingsManager.getInstance().usingNightMode) {
			mThemeImageView.setImageResource(R.drawable.nav_nightmode_on);
			mThemeTextView.setText("Theme is Night");
		} else {
			mThemeImageView.setImageResource(R.drawable.nav_nightmode_off);
			mThemeTextView.setText("Theme is Day");
		}
	}

	// ================================================================================
	// Share Methods
	// ================================================================================

	private void goFacebook() {

	}

	private void goTwitter() {

	}

	private void goEmail() {

	}

	private void copyBitcoinAddress() {
		ClipboardManager clipboard = (ClipboardManager)
		        getActivity().getSystemService(getActivity().CLIPBOARD_SERVICE);
		// Creates a new text clip to put on the clipboard
		ClipData clip = ClipData.newPlainText("simple text","15JLNn1nmjJWFV9Cm2n7NBQ5WvaHopbUY6");
		// Set the clipboard's primary clip.
		clipboard.setPrimaryClip(clip);
		
		//Build thanks alert and show
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(getActivity().getResources().getDrawable(R.drawable.ic_launcher)).setTitle("Thank you!").setMessage("The address for bitcoin donation has been copied to your clipboard.").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
        });
        builder.create().show();
	}
}
