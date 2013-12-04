package com.mattyork.bankernews.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.mattyork.jarbn.BNWebService.PostFilterType;

public class SettingsManager {
	
	//Singleton instance for settings
	private static SettingsManager settingsManager;
	SharedPreferences mSharedPreferences;
	SharedPreferences.Editor mSharedPreferencesEditor;
	
	
	public PostFilterType currentPostFilterType = PostFilterType.PostFilterTypeTop;
	public Boolean usingReadability = false;
	public Boolean usingMarkAsRead = false;
	public Boolean usingNightMode = false;
	
	protected SettingsManager() {
		
	}
	
	
	public static SettingsManager getInstance() {
		if (settingsManager == null) {
			settingsManager =  new SettingsManager();
		}
		
		return settingsManager;
	}
	
	public void loadSettingsFromSharedPreferences(Context context) {
		mSharedPreferences = context.getSharedPreferences("NewsYCPrefs", Context.MODE_PRIVATE);
		mSharedPreferencesEditor = mSharedPreferences.edit();
		this.usingReadability = mSharedPreferences.getBoolean("usingReadability", true);
		this.usingMarkAsRead = mSharedPreferences.getBoolean("usingMarkAsRead", true);
		this.usingNightMode = mSharedPreferences.getBoolean("usingNightMode", true);
	}
	
	public void setusingReadability(Boolean usingReadability) {
		this.usingReadability = usingReadability;
		mSharedPreferencesEditor.putBoolean("usingReadability", usingReadability);
		mSharedPreferencesEditor.commit();
	}

	public void setUsingMarkAsRead(Boolean usingMarkAsRead) {
		this.usingMarkAsRead = usingMarkAsRead;
		mSharedPreferencesEditor.putBoolean("usingMarkAsRead", usingMarkAsRead);
		mSharedPreferencesEditor.commit();
		Boolean test = mSharedPreferences.getBoolean("usingMarkAsRead", true);
	}


	public void setUsingNightMode(Boolean usingNightMode) {
		this.usingNightMode = usingNightMode;
		mSharedPreferencesEditor.putBoolean("usingNightMode", usingNightMode);
		mSharedPreferencesEditor.commit();
	}
	
	
}
