package com.mattyork.jarbn.BNObjects;

import android.R.integer;

public class BNUser {
	String Username;
	int Karma;
	int Age;
	String AboutInfo;

	/***
	 * Creates a user from a given HTML string.
	 * @param htmlString
	 * @return HNUser
	 */
	public static BNUser userFromHTML(String htmlString) {
		return new BNUser();
	}

}
