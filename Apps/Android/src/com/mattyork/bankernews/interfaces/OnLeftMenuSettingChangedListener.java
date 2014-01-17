package com.mattyork.bankernews.interfaces;

import com.mattyork.jarbn.BNWebService.PostFilterType;

public interface OnLeftMenuSettingChangedListener {
	public void didSelectFilterPosts(PostFilterType type);
	
	public void didSelectChangeTheme();
}
